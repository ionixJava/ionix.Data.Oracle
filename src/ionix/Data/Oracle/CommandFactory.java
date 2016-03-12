package ionix.Data.Oracle;


import ionix.Data.*;
import ionix.Data.Oracle.Conversion.OracleConvert;

public class CommandFactory extends CommandFactoryBase {
    public CommandFactory(DbAccess dataAccess) {
        super(dataAccess);
    }

    @Override
    public <TEntity> EntityCommandSelect<TEntity> createSelectCommand(Class<TEntity> cls) {
        if (null == cls)
            throw new IllegalArgumentException("cls is null");

        return new EntityCommandSelect<>(cls, this.getDataAccess(), OracleConvert.Instance);
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
