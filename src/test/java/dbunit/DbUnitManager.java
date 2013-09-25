package dbunit;

public interface DbUnitManager {

	public void refresh(String dbUnitXmlPath);

	public void cleanAndInsert(String dbUnitXmlPath);

	public void insert(String dbUnitXmlPath);

	public void update(String dbUnitXmlPath);

	public void delete(String dbUnitXmlPath);

	public void deleteAll(String dbUnitXmlPath);

	public void clear();

	public void dump(String dbUnitXmlPath);
	
}	
