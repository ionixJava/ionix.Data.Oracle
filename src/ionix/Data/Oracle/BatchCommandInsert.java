package ionix.Data.Oracle;

import ionix.Data.*;
import ionix.Utils.Ext;
import ionix.Utils.Triplet;

import java.util.HashSet;
import java.util.List;

public class BatchCommandInsert<TEntity> extends ionix.Data.BatchCommandInsert<TEntity>  {

    public BatchCommandInsert(Class<TEntity> entityClass, TransactionalDbAccess dataAccess){
        super(entityClass, dataAccess);
    }

    @Override
    protected ionix.Data.EntitySqlQueryBuilderInsert createInsertBuilder(){
        return new ionix.Data.Oracle.EntitySqlQueryBuilderInsert(this.getDataAccess());
    }
}
