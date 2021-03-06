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

import java.util.ArrayList;
import java.util.List;


public class Index {

	private String	indexName;
	private boolean unique;
	private List<IndexColumn> columns;
	
	
	public Index(String indexName, boolean unique){
		this.indexName = indexName;
		this.unique = unique;
		columns = new ArrayList<IndexColumn>();
	}


	public void add(IndexColumn indexColumn){
		columns.add(indexColumn);
	}
	
	public void addIndexColumn(String columnName, int ordinalPosition, String sortType){
		columns.add(new IndexColumn(columnName, ordinalPosition, sortType));
	}
	
	public String getIndexName() {
		return indexName;
	}


	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}


	public boolean isUnique() {
		return unique;
	}


	public void setUnique(boolean unique) {
		this.unique = unique;
	}


	public List<IndexColumn> getColumns() {
		return columns;
	}


	public void setColumns(List<IndexColumn> columns) {
		this.columns = columns;
	}
	
	
}
