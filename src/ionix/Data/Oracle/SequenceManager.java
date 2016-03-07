package ionix.Data.Oracle;


import ionix.Data.DbAccess;
import ionix.Data.SqlQuery;
import ionix.Utils.Ext;

import java.math.BigDecimal;
import java.util.HashMap;

final class SequenceManager {

    private static final HashMap<String, String> dic = new HashMap<>(8);

    static synchronized String getSequenceName(DbAccess dataAccess, String tableName, String pkColumn) 
    {
            tableName = tableName.toUpperCase();

            String sequenceName = dic.get(tableName);
            if (null == sequenceName)
            {
                sequenceName = SequenceManager.getSequenceName(dataAccess, tableName, pkColumn, true).toString();

                dic.put(tableName, sequenceName);
            }

            return sequenceName;
    }

    private static String getSequenceName(DbAccess dataAccess, String tableName, String pkColumnName, boolean checkSequence)
    {
        if (dataAccess == null)
            throw new IllegalArgumentException("dataAccess is null");
        if (Ext.isNullOrEmpty(tableName))
            throw new IllegalArgumentException("tableName is null or empty");
        if (Ext.isNullOrEmpty(pkColumnName))
            throw new IllegalArgumentException("pkColumnName is null or empty");

        String[] arr = tableName.split("\\.");
        boolean withUser = arr.length > 1;
        String sequenceName = null;
        if (withUser)
            sequenceName = arr[0] + ".SQE_" + arr[1];
        else
            sequenceName = "SQE_" + tableName;

        if (checkSequence)
        {
            BigDecimal temp = null;
            BigDecimal ret = BigDecimal.ZERO, minVal = new BigDecimal(1), curVal = BigDecimal.ZERO;

            SqlQuery query = new SqlQuery();
            StringBuilder text = query.getText();

            try
            {
                text.append("SELECT MAX(");
                text.append(tableName);
                text.append('.');
                text.append(pkColumnName);
                text.append(") FROM ");
                text.append(tableName);
                temp = dataAccess.executeScalar(BigDecimal.class, query);
                if (temp != null )
                {
                    minVal = temp.add(new BigDecimal(1));
                }

                query.clear();
                text = query.getText();

                text.append(" SELECT COUNT(*) FROM");
                if (withUser)
                {
                    text.append(" ALL_SEQUENCES T WHERE T.SEQUENCE_OWNER = ? AND");
                    query.parameter(arr[0]);
                    query.parameter("SQE_" + arr[1]);
                }
                else
                {
                    text.append(" USER_SEQUENCES T WHERE");
                    query.parameter(sequenceName);
                }
                text.append(" T.SEQUENCE_NAME = ?");
                ret = dataAccess.executeScalar(BigDecimal.class, query);
                if (ret == BigDecimal.ZERO)
                {
                    query.clear();
                    text = query.getText();

                    text.append("CREATE SEQUENCE ");
                    text.append(sequenceName);
                    text.append("\n");
                    text.append("MINVALUE 0");
                    text.append("\n");
                    text.append("MAXVALUE 9999999999999999999999999");
                    text.append("\n");
                    text.append("START WITH ");
                    text.append(minVal);
                    text.append("\n");
                    text.append("INCREMENT BY 1");
                    text.append("\n");
                    text.append("CACHE 20");
                    text.append("\n");

                    dataAccess.execute(query);
                }
                else
                {
                    query.clear();
                    text = query.getText();

                    text.append("SELECT T.LAST_NUMBER FROM");
                    if (withUser)
                    {
                        text.append(" ALL_SEQUENCES T WHERE T.SEQUENCE_OWNER = ? AND T.SEQUENCE_NAME = ?");
                        query.parameter(arr[0]);
                        query.parameter("SQE_" + arr[1]);
                    }
                    else
                    {
                        text.append(" USER_SEQUENCES T WHERE T.SEQUENCE_NAME = ?");
                        query.parameter(sequenceName).sql(null);
                    }
                    curVal = dataAccess.executeScalar(BigDecimal.class, query);
                    if (minVal.compareTo(curVal) > 0)// minVal > curVal
                    {
                        query.clear();
                        text = query.getText();

                        text.append("ALTER SEQUENCE ");
                        text.append(sequenceName);
                        text.append(" INCREMENT BY ?;");
                        query.parameter(minVal.add(curVal.multiply(new BigDecimal(-1))));
                        text.append("\n");
                        text.append("SELECT ");
                        text.append(sequenceName);
                        text.append(".NEXTVAL FROM DUAL;");
                        text.append("\n");
                        text.append("ALTER SEQUENCE ");
                        text.append(sequenceName);
                        text.append(" INCREMENT BY 1;");
                        dataAccess.executeScalar(query);
                    }
                }
            }
            catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
        }
        return sequenceName;
    }

}
