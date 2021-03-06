package ionix.Data.Oracle;

import ionix.Data.*;
import ionix.Data.EntitySqlQueryBuilderInsert;
import ionix.Utils.*;

import java.util.HashSet;


public class EntityCommandInsert<TEntity> extends ionix.Data.EntityCommandInsert<TEntity>  {

    public EntityCommandInsert(Class<TEntity> entityClass, DbAccess dataAccess) {
        super(entityClass, dataAccess);
    }

    @Override
    public int execute(TEntity entity, EntityMetaDataProvider provider) {
        EntitySqlQueryBuilderInsert builder = this.createInsertBuilder();
        builder.setInsertFields(this.getInsertFields());

        EntitySqlQueryBuilderResult result = builder.createQuery(entity, provider.createEntityMetaData(this.getEntityClass()));
        FieldMetaData sequenceIdentity = result.Identity;
        ExecuteResult er;
        if (sequenceIdentity != null){
            er = this.getDataAccess().executeUpdateSequence(result.Query, sequenceIdentity.getSchema().getColumnName());
            Ref.setValueSafely(sequenceIdentity.getField(), entity, er.getGeneratedKey());
        }
        else
            er = this.getDataAccess().executeUpdate(result.Query, null);

        return er.getValue();
    }

    @Override
    protected EntitySqlQueryBuilderInsert createInsertBuilder(){
        return new ionix.Data.Oracle.EntitySqlQueryBuilderInsert(this.getDataAccess());
    }
}