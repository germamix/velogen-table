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
package ws.michalski.velogen.plugins.table.as400;

public class Db400Mapping {

	/**
	 * 
	 * Die gültige AS400 Datentypen (Achtung nicht alle sind implementiert!)
	 * 
	 * @param type
	 * A *	Alphanumeric (character)
	 * B 	Binary
	 * D 	Digits only
	 * E 	Either DBCS or alphanumeric
	 * F 	Floating point
	 * G 	Graphic data type
	 * H 	Hexadecimal
	 * I 	Inhibit entry
	 * J 	Double-byte character set (DBCS) data only
	 * L 	Date
	 * M 	Numeric only
	 * N 	Numeric shift
	 * O 	(Open) Both DBCS and alphanumeric
	 * P *	Packed decimal
	 * S *	Zoned decimal
	 * T 	Time
	 * W 	Katakana
	 * X 	Alphabetic only (character)
	 * Y 	Numeric only
	 * Z 	Timestamp
	 * 1 	Binary large object (BLOB)
	 * 2 	Character large object (CLOB)
	 * 3 	Graphic data large object (DBCLOB)
	 * 4 	Datalink
	 * 5 	Binary character
	 * 
	 * @param fieldLen
	 * @param digits
	 * @param decPos
	 * @param fieldName
	 * 
	 * @return 
	 */
	public static String getFieldString(String type, int fieldLen, int digits, int decPos, String fieldName){
		
		if(type.toUpperCase().equals("A"))
			return "CharacterFieldDescription(new AS400Text(" + fieldLen + ", i5system),\"" + fieldName  +  "\")";
		if(type.toUpperCase().equals("B"))
			return "bin";
		if(type.toUpperCase().equals("L"))
			return "date";
		if(type.toUpperCase().equals("P"))
			return "PackedDecimalFieldDescription(new AS400PackedDecimal(" + digits + "," + decPos + "), \"" + fieldName + "\")";
		if(type.toUpperCase().equals("S"))
			return "ZonedDecimalFieldDescription(new AS400ZonedDecimal(" + digits + "," + decPos + "), \"" + fieldName + "\")";
		if(type.toUpperCase().equals("T"))
			return "time";
		if(type.toUpperCase().equals("Z"))
			return "timestamp";
		
		
		
		return "*** nicht implementiert ***";
	}
	
	/**
	 * Pr�ft, ob Feld String ist
	 * 
	 * 
	 * @param type
	 * @return true oder false
	 */
	public static boolean isString(String type){
		if(type.toUpperCase().equals("A") || type.toUpperCase().equals("2"))
			return true;
		else
			return false;
	}
	
	public static boolean isNumDate(String type, String edtwrd){
		
		if((type.toUpperCase().equals("P") || type.toUpperCase().equals("S")) 
				&& edtwrd.equals("  .  .    ") ){
			return true;
		}
			
		return false;
	}
}
