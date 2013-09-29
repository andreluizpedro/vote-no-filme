package dbunit;

import static org.dbunit.operation.DatabaseOperation.CLEAN_INSERT;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dbUnitManager")
public class DefaultDbUnitManagerImpl implements DbUnitManager {

	public static final String XML_COM_DADOS_BASICOS = "";
	DataSource dataSource;

	@Autowired
	public DefaultDbUnitManagerImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected Connection getConnection() {
		Connection conn;
		try {
			conn = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Erro ao tentar obter uma conexão com o banco: "
							+ e.getMessage());
		}
		return conn;
	}

	/**
	 * Executa operações do DBUnit no dataset <code>dbUnitXmlPath</code>.
	 */
	protected void execute(DatabaseOperation operation, String dbUnitXmlPath) {
		try {
			IDatabaseConnection dbconn = this.getDbUnitConnection();
			operation.execute(dbconn, this.getDataSetFrom(dbUnitXmlPath));
			dbconn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void cleanAndInsert(String dbUnitXmlPath) {
		execute(CLEAN_INSERT, dbUnitXmlPath);
	}

	private IDataSet getDataSetFrom(String dbUnitXmlPath) throws IOException,
			DataSetException, FileNotFoundException {
		return new FlatXmlDataSetBuilder().build(new FileInputStream(
				dbUnitXmlPath));
	}

	/**
	 * Instancia, configura e retorna um <code>IDatabaseConnection</code> a
	 * partir de uma conexão Jdbc.
	 */
	protected IDatabaseConnection getDbUnitConnection()
			throws DatabaseUnitException, SQLException {
		IDatabaseConnection dbconn = new DatabaseConnection(
				this.getConnection());
		return dbconn;
	}

}