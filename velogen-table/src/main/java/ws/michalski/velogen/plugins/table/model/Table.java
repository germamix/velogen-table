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

import java.util.List;


public class Table {

	/**
	 *    
	1.  TABLE_CAT String => table catalog (may be null)
   	2. TABLE_SCHEM String => table schema (may be null)
   	3. TABLE_NAME String => table name
   	4. TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
   	5. REMARKS String => explanatory comment on the table
   	6. TYPE_CAT String => the types catalog (may be null)
   	7. TYPE_SCHEM String => the types schema (may be null)
   	8. TYPE_NAME String => type name (may be null)
   	9. SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
  	10. REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null) 
	 */
	
	private String catalog 	 = null;
	private String schema	 = null;
	private String name		 = null;
	private String type		 = null;
	private String comment	 = null;
	
	private List<Column> columns;
	private List<PrimaryKey> primaryKeys;
	private List<Index>  indexes;
	private List<ForeignKeyGroup> foreignKeys;
		
	public Table(String catalog, String schema, String name, String type) {
		super();
		this.catalog = catalog;
		this.schema = schema;
		this.name = name;
		this.type = type;
		
	}

	
	public boolean hasLobField(){
		
		for (Column column : columns) {
			if(column.isLob()){
				return true;
			}
		}
		return false;
	}

	public String toString(){
		return name;
	}
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isComplexPrimaryKey() {
		
		if(primaryKeys != null && primaryKeys.size() > 1){
			return true;
		}
		return false;
	}
			
	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}


	public List<Index> getIndexes() {
		return indexes;
	}


	public void setIndexes(List<Index> indexes) {
		this.indexes = indexes;
	}


	public List<ForeignKeyGroup> getForeignKeys() {
		return foreignKeys;
	}


	public void setForeignKeys(List<ForeignKeyGroup> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}


	public List<PrimaryKey> getPrimaryKeys() {
		return primaryKeys;
	}


	public void setPrimaryKeys(List<PrimaryKey> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	


	
}
