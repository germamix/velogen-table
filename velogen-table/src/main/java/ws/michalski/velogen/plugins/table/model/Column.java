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

import java.util.Arrays;

import ws.michalski.velogen.plugins.table.as400.AS400Field;

public class Column {

	/**
	 * 
	 *    
	
   		4. COLUMN_NAME String => column name
   		5. DATA_TYPE int => SQL type from java.sql.Types
   		6. TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
   		7. COLUMN_SIZE int => column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision.
      	9. DECIMAL_DIGITS int => the number of fractional digits
  		10. NUM_PREC_RADIX int => Radix (typically either 10 or 2)
  		11. NULLABLE int => is NULL allowed.
          * columnNoNulls - might not allow NULL values
          * columnNullable - definitely allows NULL values
          * columnNullableUnknown - nullability unknown 
  		12. REMARKS String => comment describing column (may be null)
  		13. COLUMN_DEF String => default value (may be null)
  		15. SQL_DATETIME_SUB int => unused
  		16. CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
  		17. ORDINAL_POSITION int => index of column in table (starting at 1)
  		18. IS_NULLABLE String => "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values. An empty string means nobody knows.
  		19. SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
  		20. SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
  		21. SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)
  		22. SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) 
	
	
	
		Zus�tzlich werden auch die Felder f�r AS400 Objekt eingef�hrt. 
	 */
	
	private final String[] PRIMITIVEFIELDS = {"INTEGER", "TINYINT", "SMALLINT", "SMALLINT",
											"BIGINT", "FLOAT", "DOUBLE"};
	
	private final String[] LOBEFIELDS = {"CLOB", "LOB", "DBLOB"};
	
	private String  name		= null;
	private int		type		= 0;
	private String	typeName	= null;
	private String  remarks		= null;
	private int		size		= 0;
	private int 	digits		= 0;
	private int		nullable	= 0;
	private String	definition	= null;
	private int		position	= 0;
	
	// Zus�tzliche Eigenschaften
	private boolean idColumn	  = false;
	private AS400Field as400field = null;
	
	public boolean isIdColumng(){
		return idColumn;
	}
	
	public boolean isPrimitive() {
		
		if(Arrays.asList(PRIMITIVEFIELDS).contains(typeName.toUpperCase())){
			return true;
		}
		return false;
	}
	
	public boolean isLob() {
		if(Arrays.asList(LOBEFIELDS).contains(typeName.toUpperCase())){
			return true;
		}
		return false;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getDigits() {
		return digits;
	}
	public void setDigits(int digits) {
		this.digits = digits;
	}
	public int getNullable() {
		return nullable;
	}
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public void setIdColumn(boolean idColumn){
		this.idColumn = idColumn;
	}

	public AS400Field getAs400field() {
		return as400field;
	}

	public void setAs400field(AS400Field as400field) {
		this.as400field = as400field;
	}
	
}
