package ionix.Data.Oracle;


import ionix.Data.*;

public class CommandFactory extends CommandFactoryBase {
    public CommandFactory(DbAccess dataAccess) {
        super(dataAccess);
    }

    @Override
    protected <TEntity> EntityCommandInsert<TEntity> createEntityCommandInsert(Class<TEntity> cls, DbAccess dataAccess) {
        return new ionix.Data.Oracle.EntityCommandInsert<>(cls, dataAccess);
    }

    @Override
    protected <TEntity> BatchCommandInsert<TEntity> createBatchCommandInsert(Class<TEntity> cls, TransactionalDbAccess dataAccess) {
        return new ionix.Data.Oracle.BatchCommandInsert<>(cls, dataAccess);
    }
}
