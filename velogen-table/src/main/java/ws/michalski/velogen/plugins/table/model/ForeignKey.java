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


public class ForeignKey {

	
	private String primaryKeyTableCatalog;
	private String primaryKeyTableSchema;
	private String primaryKeyTableName;
	private String primaryKeyColumnName;
	
	private String foreignKeyTableCatalog;
	private String foreignKeyTableSchema;
	private String foreignKeyTableName;
	private String foreignKeyColumnName;
	
	private int    sequence;
	
	private int    updateRule;
	private int    deleteRule;
	
	private String foreignKeyName;
	private String primaryKeyName;
	
	
	
	public String getPrimaryKeyTableCatalog() {
		return primaryKeyTableCatalog;
	}
	public void setPrimaryKeyTableCatalog(String primaryKeyTableCatalog) {
		this.primaryKeyTableCatalog = primaryKeyTableCatalog;
	}
	public String getPrimaryKeyTableSchema() {
		return primaryKeyTableSchema;
	}
	public void setPrimaryKeyTableSchema(String primaryKeyTableSchema) {
		this.primaryKeyTableSchema = primaryKeyTableSchema;
	}
	public String getPrimaryKeyTableName() {
		return primaryKeyTableName;
	}
	public void setPrimaryKeyTableName(String primaryKeyTableName) {
		this.primaryKeyTableName = primaryKeyTableName;
	}
	public String getPrimaryKeyColumnName() {
		return primaryKeyColumnName;
	}
	public void setPrimaryKeyColumnName(String primaryKeyColumnName) {
		this.primaryKeyColumnName = primaryKeyColumnName;
	}
	public String getForeignKeyTableCatalog() {
		return foreignKeyTableCatalog;
	}
	public void setForeignKeyTableCatalog(String foreignKeyTableCatalog) {
		this.foreignKeyTableCatalog = foreignKeyTableCatalog;
	}
	public String getForeignKeyTableSchema() {
		return foreignKeyTableSchema;
	}
	public void setForeignKeyTableSchema(String foreignKeyTableSchema) {
		this.foreignKeyTableSchema = foreignKeyTableSchema;
	}
	public String getForeignKeyTableName() {
		return foreignKeyTableName;
	}
	public void setForeignKeyTableName(String foreignKeyTableName) {
		this.foreignKeyTableName = foreignKeyTableName;
	}
	public String getForeignKeyColumnName() {
		return foreignKeyColumnName;
	}
	public void setForeignKeyColumnName(String foreignKeyColumnName) {
		this.foreignKeyColumnName = foreignKeyColumnName;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getUpdateRule() {
		return updateRule;
	}
	public void setUpdateRule(int updateRule) {
		this.updateRule = updateRule;
	}
	public int getDeleteRule() {
		return deleteRule;
	}
	public void setDeleteRule(int deleteRule) {
		this.deleteRule = deleteRule;
	}
	public String getForeignKeyName() {
		return foreignKeyName;
	}
	public void setForeignKeyName(String foreignKeyName) {
		this.foreignKeyName = foreignKeyName;
	}
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}
	
	
	
}
