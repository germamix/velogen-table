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
package ws.michalski.velogen.plugins.table;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ws.michalski.velogen.interfaces.VeloGenPlugin;
import ws.michalski.velogen.plugins.table.model.Column;
import ws.michalski.velogen.plugins.table.model.DataBase;
import ws.michalski.velogen.plugins.table.model.Index;
import ws.michalski.velogen.plugins.table.model.IndexColumn;
import ws.michalski.velogen.plugins.table.model.Table;

public class TablePlugIn extends VeloGenPlugin {

	private Object parameters;
	
	
	public TablePlugIn() {
		parameters = new TablePlugInCmd(); 
	}
	
	@Override
	public Map<String, Object> run(Object parameter) {
		
		Map<String, Object> intContext = new HashMap<>();
		
		TablePlugInCmd inpParam = (TablePlugInCmd) parameter;
		
		// Parameter prüfen
		checkParams(inpParam);
		
		DataBase dataBase = new DataBase(inpParam.getDriverClassName(),
					  							  inpParam.getUrlDatabase(),
					  							  inpParam.getUser(),
					  							  inpParam.getPass());
		
		try {
			
//			List<String> catalogs = dataBase.getCatalogs();
//			for (String catalog : catalogs) {
//				System.out.println(catalog);
//			}
//			
//			List<DbSchema> schemas = dataBase.getSchemas();
//			for (DbSchema dbSchema : schemas) {
//				System.out.println(dbSchema.getCatalog() + " " + dbSchema.getSchema());
//			}
			
			System.out.println("Data Base Type:");
			System.out.println(dataBase.getDbType());
			System.out.println(dataBase.getDbVersion());
			System.out.println("SQL Functions:");
			System.out.println(dataBase.getSqlFunctions());
			System.out.println("SQL Keywords:");
			System.out.println(dataBase.getSqlKeywords());
			System.out.println("SQL System Functions");
			System.out.println(dataBase.getSqlSysFunctions());
			
			
			Table t = dataBase.getTable(null, 
										inpParam.getDbSchema(), 
										inpParam.getTable().get(0));
			
			System.out.println(t.getComment());
			List<Column> cl = t.getColumns();
			for (Column column : cl) {
				System.out.println(column.getPosition() + "\t" + column.getName());
			}
			
			System.out.println("INDEXES");
			List<Index> indexes = t.getIndexes();
			for (Index index : indexes) {
				System.out.println("Indexname: " + index.getIndexName() + " UNIQUE = " + index.isUnique());
				for (IndexColumn ic : index.getColumns()) {
					System.out.println(ic.getOrdinalPosition() + "\t" + ic.getColumnName());
				}
				System.out.println(" ");
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.fillInStackTrace();
			logger.error(e.getMessage());
		}
		
		System.out.println("Password is: " + inpParam.getPass());
		
		logger.debug("Nachricht...");
//		intContext.put("ITEMS", items.getItems());
		
		return intContext;
	}

	
	
	private void checkParams(TablePlugInCmd inpParam) {
		
		
	}

	@Override
	public Object getCommandParam() {
		return parameters;
	}

}
