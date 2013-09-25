package dbunit;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.h2.H2DataTypeFactory;

public class H2DbUnitManagerImpl extends DefaultDbUnitManagerImpl {

	public H2DbUnitManagerImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected IDatabaseConnection getDbUnitConnection()
			throws DatabaseUnitException, SQLException {
		IDatabaseConnection dbconn = new DatabaseConnection(getConnection());
		dbconn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
		return dbconn;
	}

}