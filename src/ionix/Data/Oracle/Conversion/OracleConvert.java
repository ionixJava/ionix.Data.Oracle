package ionix.Data.Oracle.Conversion;

import ionix.Conversion.Convert;
import ionix.Conversion.Convertible;
import oracle.sql.BLOB;

import java.sql.Blob;
import java.util.HashMap;


public class OracleConvert extends Convert {

    protected OracleConvert(){

    }
    public static final OracleConvert Instance = new OracleConvert();

    @Override
    protected void putConverters(HashMap<Class, Convertible> converterRepo){
        super.putConverters(converterRepo);

        converterRepo.put(BLOB.class, new BlobConvertible());
    }

    @Override
    protected Object convert(Convertible c,Object value, Class targetClass){
        Object convertedValue = super.convert(c, value, targetClass);
        if (null == convertedValue && targetClass == BLOB.class){
            return c.toByteArray(value);
        }
        return convertedValue;
    }
}
