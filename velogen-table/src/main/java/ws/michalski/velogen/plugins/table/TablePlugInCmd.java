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

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription="input_table")
public class TablePlugInCmd {

	@Parameter(description="", required=true, arity=1)
	private List<String> table;

	
	@Parameter(names={"-d", "--driver"}, description="JDBC driver Class", required=true)
	private String driverClassName;
	
	@Parameter(names={"-u", "--url-database"}, description="URL Data Base", required=true)
	private String urlDatabase;
	
	@Parameter(names={"-s", "--schema"}, description="DB Schema", required=true)
	private String dbSchema;
	
	
	@Parameter(names={"-U", "--user"}, description="Login User", required=false)
	private String user;
	
	@Parameter(names={"-P", "--pass"}, description="Password", required=false, password=false)
	private String pass;

	@Parameter(names={"--constructedPrimKey"}, description="Create primary key from unique index", required=false)
	private String contructedPrimKey;
	
	// Geter

	
	public List<String> getTable() {
		return table;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public String getUrlDatabase() {
		return urlDatabase;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getContructedPrimKey() {
		return contructedPrimKey;
	}
	
	
	
}
