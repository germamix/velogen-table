/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at 

     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ws.michalski.velogen.plugins.table.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ws.michalski.velogen.plugins.table.as400.AS400Field;
import ws.michalski.velogen.plugins.table.as400.ApiQUSLFLD;
import ws.michalski.velogen.plugins.table.as400.CallApiException;





public class DataBase {

	final public static String[] TABLES = {"TABLE", "VIEW"};
	
	
	// Verbindungsdaten
	private String driver 	= null;
	private String url		= null;
	private String user		= null;
	private String password	= null;
	private String usLib	= null;


	private String sqlKeywords;
	private String sqlFunctions;
	private String sqlSysFunctions;
	private String dbType;
	private String dbVersion;
	
	DatabaseMetaData meta;
	Connection       conn;
	
	public DataBase(String driver, String url, String user, String password){
		this.driver 	= driver;
		this.url		= url;
		this.user		= user;
		this.password	= password;
		this.usLib		= "QGPL";		
		
		initDataBase();
		
	}

	public DataBase(String driver, String url, String user, String password, String usLib){
		this.driver 	= driver;
		this.url		= url;
		this.user		= user;
		this.password	= password;
		this.usLib		= usLib;
		
		initDataBase();
		
	}
	
	

	public void initDataBase() {
		try {
			conn = getConnection();
			meta = getDatabaseMetaData();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private Connection getConnection() throws SQLException, ClassNotFoundException{
		if(driver == null || url == null || user == null || password ==null){
			return null;
		}
		Class.forName(driver);
		
		return DriverManager.getConnection(url, user, password);
	}


	private DatabaseMetaData getDatabaseMetaData() throws SQLException{
		meta = conn.getMetaData();
		sqlKeywords = meta.getSQLKeywords();
		sqlFunctions= meta.getStringFunctions();
		sqlSysFunctions = meta.getSystemFunctions();
		dbType		= meta.getDatabaseProductName();
		dbVersion   = meta.getDatabaseProductName() + " " +
					  meta.getDriverMajorVersion() + "." +
					  meta.getDriverMinorVersion();

		return meta;
	}
	
	
	public List<String> getCatalogs() throws SQLException{
		List<String> catalogs = new ArrayList<String>();

		ResultSet rs = null;
	    rs = meta.getCatalogs();

	     while (rs.next()) {
	       catalogs.add(rs.getString(1));  //"TABLE_CATALOG"
	     }

		return catalogs;
	}
	
	
	public List<DbSchema> getSchemas() throws SQLException{
		List<DbSchema> schemas = new ArrayList<DbSchema>();

		ResultSet rs = null;
	    rs = meta.getSchemas();

	     while (rs.next()) {
	       schemas.add(new DbSchema(rs.getString(1), rs.getString(2)));
	     }

		return schemas;
	}
	
	
	// TODO Umbauen von Table() auf String oä.
	public Map<String, Table> getTables(String catalog, String schema) throws SQLException{
		Map<String, Table> tables = new HashMap<String, Table>();

		ResultSet rs = null;
	    rs = meta.getTables(catalog, schema, null, TABLES);

	    Table table;

	    while (rs.next()) {
	    //  Sollte Schema null sein (werden möglicherweise mehrere Schemas eingelesen)
	    //  Map-Key muss eindeutig sein und wird erstellt aus Schema + . + Tabellen-Namen
	    //  sonst wird nur Name der Tabelle verwendet.

	    	table = new Table(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));

	    	if(schema == null)
	    		 tables.put(rs.getString(2).trim() +"."+ rs.getString(3).trim(), table);
	    	else
	    		 tables.put(rs.getString(3).trim(), table);

	    	table.setComment(rs.getString(5));


	    }

		return tables;
	}
	
	
	
	
	public Table getTable( String catalog, String schema, String tableName) throws SQLException{
		
		ApiQUSLFLD i5api;
		Map<String, AS400Field> i5fields = null;
		
		if(schema == null || tableName == null){
			throw new SQLException("Schema or Tablename is null.");
		}
		
		// Nur für AS400 Tabellen
		if(meta.getDatabaseProductName().startsWith("DB2 UDB for AS/400")){
			// i5 Object URL festlegen
			int posa, pose;
			posa = meta.getURL().indexOf("://");
			posa = posa + 3;
			pose = meta.getURL().indexOf("/", posa);

			i5api = new ApiQUSLFLD(meta.getURL().substring(posa, pose), user, password, usLib);
			try {
				i5api.call(schema, tableName);
				i5fields = i5api.getFields();
//				for (AS400Field f : i5fields.values()) {
//					System.out.println(f.getFieldName() + " " + f.getColHdg1() + " " + f.getColHdg2());
//				}
			} catch (CallApiException e) {
				// TODO Logger
				e.printStackTrace();
			}
		}
		
		Table table = null;

		ResultSet rs = null;
	    rs = meta.getTables(catalog, schema, tableName, TABLES);

	    // Es wird nur ein mal gelesen
	    if(rs.next()){
	    	table = new Table(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), rs.getString(4).trim());
	    	table.setComment(rs.getString(5));
	    	
	    	table.setPrimaryKeys(listPrimaryKeys(table.getCatalog(), table.getSchema(), table.getName()));
	    	
	    	table.setIndexes(listIndex(table.getCatalog(), table.getSchema(), table.getName()));
	    	
	    	table.setColumns(loadColumnsList(table, table.getPrimaryKeys(), i5fields));

	    	table.setForeignKeys(listForeignKeys(table.getCatalog(), table.getSchema(), table.getName()));
	    } 
	    
		
		return table;
	}
	
	
	/**
	 *  1. TABLE_CAT String => table catalog (may be null)
	 *  2. TABLE_SCHEM String => table schema (may be null)
	 *  3. TABLE_NAME String => table name
	 *  4. NON_UNIQUE boolean => Can index values be non-unique. false when TYPE is tableIndexStatistic
	 *  5. INDEX_QUALIFIER String => index catalog (may be null); null when TYPE is tableIndexStatistic
	 *  6. INDEX_NAME String => index name; null when TYPE is tableIndexStatistic
	 *  7. TYPE short => index type:
	 *		tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions
	 *		tableIndexClustered - this is a clustered index
	 *		tableIndexHashed - this is a hashed index
	 *		tableIndexOther - this is some other style of index
	 *  8. ORDINAL_POSITION short => column sequence number within index; zero when TYPE is tableIndexStatistic
	 *  9. COLUMN_NAME String => column name; null when TYPE is tableIndexStatistic
	 * 10. ASC_OR_DESC String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic
	 * 11. CARDINALITY int => When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise, it is the number of unique values in the index.
	 * 12. PAGES int => When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index.
	 * 13. FILTER_CONDITION String => Filter condition, if any. (may be null)

	 *
	 * @param catalog
	 * @param schema
	 * @param name
	 * @throws SQLException
	 */
	public List<Index> listIndex( String catalog, String schema, String name)
			throws SQLException {
		ResultSet rs = null;
		rs = meta.getIndexInfo(catalog, schema, name, false, false);

		List<Index> indexList = new ArrayList<Index>();
		Index index = null;
		String altName = "";
		
		while (rs.next()) {

			String indexName = rs.getString(6);
			boolean notUnique = rs.getBoolean(4);
			String columnName = rs.getString(9);
			int columnPosition = rs.getInt(8);
			String sortType = rs.getString(10);

			// OS400 Version 6.1 liefert da zus�tzlich am Ende Satz mit allen Feldern gleich NULL
			// Pr�fen auf null eingebaut BUG OR FEATURE ?
			if(indexName != null){
				if (!indexName.equals(altName)) {
					// System.out.println("###### Gruppe ######");
					index = new Index(indexName, !notUnique);
					indexList.add(index);
					altName = indexName;
				}
				index.addIndexColumn(columnName, columnPosition, sortType);
			}
		}
		return indexList;

	}
	
	/**
	    1. PKTABLE_CAT String => primary key table catalog being imported (may be null) 
 		2. PKTABLE_SCHEM String => primary key table schema being imported (may be null) 
 		3. PKTABLE_NAME String => primary key table name being imported 
 		4. PKCOLUMN_NAME String => primary key column name being imported 
 		5. FKTABLE_CAT String => foreign key table catalog (may be null) 
 		6. FKTABLE_SCHEM String => foreign key table schema (may be null) 
 		7. FKTABLE_NAME String => foreign key table name 
 		8. FKCOLUMN_NAME String => foreign key column name 
 		9. KEY_SEQ short => sequence number within a foreign key( a value of 1 represents the first column of the foreign key, a value of 	2 would represent the second column within the foreign key). 
		10. UPDATE_RULE short => What happens to a foreign key when the primary key is updated: 
				importedNoAction - do not allow update of primary key if it has been imported 
				importedKeyCascade - change imported key to agree with primary key update 
				importedKeySetNull - change imported key to NULL if its primary key has been updated 
				importedKeySetDefault - change imported key to default values if its primary key has been updated 
				importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 
		11. DELETE_RULE short => What happens to the foreign key when primary is deleted. 
				importedKeyNoAction - do not allow delete of primary key if it has been imported 
				importedKeyCascade - delete rows that import a deleted key 
				importedKeySetNull - change imported key to NULL if its primary key has been deleted 
				importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 
				importedKeySetDefault - change imported key to default if its primary key has been deleted 
		12. FK_NAME String => foreign key name (may be null) 
		13. PK_NAME String => primary key name (may be null) 
		14. DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit 
				importedKeyInitiallyDeferred - see SQL92 for definition 
				importedKeyInitiallyImmediate - see SQL92 for definition 
				importedKeyNotDeferrable - see SQL92 for definition   
	 * 
	 * 
	 * @param meta
	 * @param catalog
	 * @param schema
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public List<ForeignKeyGroup> listForeignKeys(String catalog, String schema, String name) throws SQLException{
		
		ResultSet rs = null;
		
		rs = meta.getImportedKeys(catalog, schema, name);
		List<ForeignKeyGroup> foreignKeyList = new ArrayList<>();
		String altName ="";
		ForeignKey fk = null;
		ForeignKeyGroup fkGroup = null;
		
		while(rs.next()){
			
			String fkName = rs.getString(12);
			if(fkName != null){
				// neu FK
				fk = new ForeignKey();
				
				fk.setForeignKeyName(fkName);
				
				fk.setPrimaryKeyTableCatalog(rs.getString(1));
				fk.setPrimaryKeyTableSchema(rs.getString(2));
				fk.setPrimaryKeyTableName(rs.getString(3));
				fk.setPrimaryKeyColumnName(rs.getString(4));
				
				fk.setForeignKeyTableCatalog(rs.getString(5));
				fk.setForeignKeyTableSchema(rs.getString(6));
				fk.setForeignKeyTableName(rs.getString(7));
				fk.setForeignKeyColumnName(rs.getString(8));
				
				fk.setSequence(rs.getInt(9));
				
				fk.setUpdateRule(rs.getInt(10));
				fk.setDeleteRule(rs.getInt(11));
				
				fk.setPrimaryKeyName(rs.getString(13));
				
				// Prüfen ob die Gruppe gleich
				if(! fkName.equals(altName)){
					// Neuanlage Gruppe
					fkGroup = new ForeignKeyGroup();
					foreignKeyList.add(fkGroup);
					altName = fkName;
				}
				fkGroup.getForeignKeys().add(fk);
			}
			
		}
		
		return foreignKeyList;
	}
	
	
	/**
	 * Each primary key column description has the following columns:
	 *
	 * 1. TABLE_CAT String => table catalog (may be null)
	 * 2. TABLE_SCHEM String => table schema (may be null)
	 * 3. TABLE_NAME String => table name
	 * 4. COLUMN_NAME String => column name
	 * 5. KEY_SEQ short => sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key).
	 * 6. PK_NAME String => primary key name (may be null)

	 *
	 * @param catalog
	 * @param schema
	 * @param name
	 * @throws SQLException
	 */

	public List<PrimaryKey> listPrimaryKeys(String catalog, String schema, String name) throws SQLException {

		List<PrimaryKey> primaryKeys = new ArrayList<>(); 
		
		ResultSet rs = null;
		rs = meta.getPrimaryKeys(catalog, schema, name);
		
		while (rs.next()){
			primaryKeys.add( new PrimaryKey(rs.getString(4), rs.getInt(5), rs.getString(6)) );
		}
		return primaryKeys;
	}


	
	public List<Column> loadColumnsList(Table table, List<PrimaryKey> primaryKeys, Map<String, AS400Field> i5fields) throws SQLException{
	
	List<Column> columns = new ArrayList<>();
	
	ResultSet rs = null;
	rs = meta.getColumns(table.getCatalog(), table.getSchema(), table.getName(), "%");
	
			
	while (rs.next()){	
				
		Column column = new Column();
		column.setName(rs.getString(4));
		column.setType(rs.getInt(5));
		column.setTypeName(rs.getString(6));
		column.setSize(rs.getInt(7));
		column.setDigits(rs.getInt(9));
		column.setNullable(rs.getInt(11));
		column.setDefinition(rs.getString(13));
		column.setRemarks(rs.getString(12));
		column.setPosition(rs.getInt(17));

		// Setzen von Zusatzfeldern
		if (primaryKeys != null) {
			column.setIdColumn(primaryKeys.contains(column.getName()));
		} else {
			column.setIdColumn(false);
		}

		if( i5fields != null){
			column.setAs400field(i5fields.get(column.getName().toLowerCase()));
		} else {
			column.setAs400field(null);
		}
		
		
		
		columns.add(column);	
		
	}
	
	
	return columns;
}
	
	
	public String getDriver() {
		return driver;
	}


	public void setDriver(String driver) {
		this.driver = driver;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSqlKeywords() {
		return sqlKeywords;
	}

	public String getSqlFunctions() {
		return sqlFunctions;
	}

	public String getSqlSysFunctions() {
		return sqlSysFunctions;
	}

	public String getDbType() {
		return dbType;
	}

	public String getDbVersion() {
		return dbVersion;
	}
	
}
