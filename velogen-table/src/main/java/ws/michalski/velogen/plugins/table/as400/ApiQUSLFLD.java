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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400UnsignedBin4;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.UserSpace;

// TODO Logger einbauen
public class ApiQUSLFLD {

	private AS400 i5system;
	private UserSpace us;
	private String usLib;
	private String usName;
	private AS400Message[] messageList;
	private Map<String, AS400Field> fields;
	
	public ApiQUSLFLD(AS400 i5system) {
		super();
		this.i5system = i5system;
		this.usLib = "QTEMP";
		this.usName = "QUSLFLD";
		
	}
	public ApiQUSLFLD(String system, String user, String password, String usLib){
		super();
		this.usName = "QUSLFLD";
		this.usLib  = usLib;
		
		
		i5system = new AS400(system, user, password);
		
		
	}
	public ApiQUSLFLD(AS400 i5system, String usLib) {
		super();
		this.i5system = i5system;
		this.usLib = usLib;
		this.usName = "QUSLFLD";
		
	}

	public void call (String libName, String tableName) throws CallApiException{
		
		
		try {
			
			createUserSpace();
			if( ! callAs400Api(libName, tableName)){
				throw new CallApiException("Error CallApis");
			}
			
			// Alles OK
			return;
			
		} catch (AS400SecurityException e) {
			e.printStackTrace();
		} catch (ErrorCompletingRequestException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ObjectDoesNotExistException e) {
			e.printStackTrace();
		}
		
		throw new CallApiException("Error");
		
	}
	
	private boolean callAs400Api(String libName, String tableName) throws CallApiException {
		
		if(libName.length() > 10 || tableName.length() > 10)
			throw new CallApiException("Lenght for Library or File to long.");
		
		AS400Text spcName = new AS400Text(20, i5system);
		AS400Text format = new AS400Text(8, i5system);
		AS400Text pfName = new AS400Text(20, i5system);
		AS400Text pfmName = new AS400Text(10, i5system);
		AS400Text override = new AS400Text(1, i5system);
		
		
		ProgramParameter[] params = new ProgramParameter[5];
		params[0] = new ProgramParameter(spcName.toBytes(setFullApiName(usLib, usName)));
		params[1] = new ProgramParameter(format.toBytes("FLDL0100"));
		params[2] = new ProgramParameter(pfName.toBytes(setFullApiName(libName, tableName)));
		params[3] = new ProgramParameter(pfmName.toBytes("*FIRST"));
		params[4] = new ProgramParameter(override.toBytes("0"));
		
		ProgramCall pgm = new ProgramCall(i5system, "/QSYS.LIB/QUSLFLD.PGM", params);
		try {
			if(pgm.run() == true){
				
				readFields();
				us.delete();
				
				return true;
			} else {
				messageList = pgm.getMessageList();
				return false;
			}
		} catch (AS400SecurityException e) {
			e.printStackTrace();
		} catch (ErrorCompletingRequestException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ObjectDoesNotExistException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private void readFields() throws AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException {

		fields = new Hashtable<String, AS400Field>();
		int usHeaderLen = getUserSpaceRecord().getRecordLength();
		byte[] usHeader = new byte[usHeaderLen];
		us.read(usHeader, 0);
		Record usr = getUserSpaceRecord(usHeader);
		
		int offset = ((Long) usr.getField("LOFFSET")).intValue();
		int count  = ((Long) usr.getField("ECOUNT")).intValue();
		int esize  = ((Long) usr.getField("ESIZE")).intValue();
		
		
		byte[] usItem = new byte[esize];
		for(int i = 0; i < count;i++){
			us.read(usItem, offset);
			Record r = getApiDataRecord(usItem);
			
			AS400Field fld = new AS400Field();
			fld.setFieldName(((String) r.getField("FLDNAME")).trim());
			fld.setDataTyp(((String) r.getField("DATATYP")).trim());
			fld.setUse(((String) r.getField("USE")).trim());
			fld.setOutBufferPos(((Long) r.getField("OUTBUFPOS")).intValue());
			fld.setInpBufferPos(((Long) r.getField("INPBUFPOS")).intValue());
			fld.setBinFieldLen(((Long) r.getField("BFLDLEN")).intValue());
			fld.setDigits(((Long) r.getField("DIGITS")).intValue());
			fld.setDecPos(((Long) r.getField("DECPOS")).intValue());
			fld.setFieldDesc(((String) r.getField("FLDDSC")).trim());
			fld.setEdtCde(((String) r.getField("EDTCDE")).trim());
			fld.setEdtWrdLen(((Long) r.getField("EDTWRDLEN")).intValue());
			fld.setEdtWrd(((String) r.getField("EDTWRD")).trim());
			fld.setColHdg1(((String) r.getField("COLHDG1")).trim());
			fld.setColHdg2(((String) r.getField("COLHDG2")).trim());
			fld.setColHdg3(((String) r.getField("COLHDG3")).trim());
			fld.setIntFeldName(((String) r.getField("IFLDNAM")).trim());
			fld.setAltFeldName(((String) r.getField("AFLDNAM")).trim());
			fld.setAltFieldLen(((Long) r.getField("LENAFLD")).intValue());
			fld.setNbrDBCS(((Long) r.getField("NBRDBCS")).intValue());
			fld.setNullValAllowed(((String) r.getField("NVALALLOW")).trim());
			fld.setVarFieldInd(((String) r.getField("VARFLDIND")).trim());
			fld.setDatTimFmt(((String) r.getField("DATTIMFMT")).trim());
			fld.setDatTimSep(((String) r.getField("DATTIMSEP")).trim());
			fld.setVarLenFieldInd(((String) r.getField("VARLENIND")).trim());
			fld.setFldDscCCSID(((Long) r.getField("DSCCCSID")).intValue());
			fld.setFldDtaCCSID(((Long) r.getField("FLDCCSID")).intValue());
			
			/*
			 F�r Tabellen, die mit SQL definiert sind kann es vorkommen, dass
			 Feldname und Alternativer Name nicht gleich sind. Aus der sicht der AS400
			 ist Kurzanme auch Feldname. SQL sieht das allerdings anders.
			 Hier wird �berpr�ft, ob Namen unterschiedlich sind und werden ausgetauscht.
			 */
			String nameAS400 = ((String) r.getField("FLDNAME")).trim();
			String nameSQL   = ((String) r.getField("AFLDNAM")).trim();
			
			if( nameSQL != null && nameSQL.trim().length() > 0 && ! nameAS400.equalsIgnoreCase(nameSQL)){
				fields.put(nameSQL.toLowerCase(), fld);
			} else {
				fields.put(nameAS400.toLowerCase(), fld);
			}
			offset = offset + esize;
		}
		
		
	}

	private String setFullApiName(String lib, String file){
		String _lib = lib;
		String _file = file;
		while(_lib.length() < 10)
			_lib = _lib + " ";
		while(_file.length() < 10)
			_file = _file + " ";
		
		return _file.toUpperCase() + _lib.toUpperCase();
	}

	private void createUserSpace() throws AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException, CallApiException {
		if(usName.length() > 10 || usLib.length() > 10)
			throw new CallApiException("UserSpace Name is not valid.");
		us = new UserSpace(i5system, "/QSYS.LIB/" + usLib.toUpperCase() + ".LIB/" + usName.toUpperCase() + ".USRSPC");
		us.create(10240, true, " ", (byte) 0x00, "UserSpace for QUSLFLD", "*USE");
	}

	private Record getUserSpaceRecord(){
		return createUserSpaceHeader().getNewRecord();
	}
	private Record getUserSpaceRecord(byte[] data) throws UnsupportedEncodingException{
		return createUserSpaceHeader().getNewRecord(data);
	}
	
	private RecordFormat createUserSpaceHeader(){
		RecordFormat userSpaceHeader = new RecordFormat();
		userSpaceHeader.addFieldDescription(new CharacterFieldDescription(new AS400Text(103, i5system), "FILLER1"));
		userSpaceHeader.addFieldDescription(new CharacterFieldDescription(new AS400Text(1, i5system), "STATUS"));
		userSpaceHeader.addFieldDescription(new CharacterFieldDescription(new AS400Text(12, i5system), "FILLER2"));
		userSpaceHeader.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "HOFFSET"));
		userSpaceHeader.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "HSIZE"));
		userSpaceHeader.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "LOFFSET"));
		userSpaceHeader.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "LSIZE"));
		userSpaceHeader.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "ECOUNT"));
		userSpaceHeader.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "ESIZE"));

		
		return userSpaceHeader;
	}
	
	private Record getApiDataRecord(byte[] data) throws UnsupportedEncodingException{
		return createApiDataFormat().getNewRecord(data);
	}
	
	
	private RecordFormat createApiDataFormat(){
		RecordFormat apiDataFormat = new RecordFormat();
		
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, i5system), "FLDNAME"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(1, i5system), "DATATYP"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(1, i5system), "USE"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "OUTBUFPOS"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "INPBUFPOS"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "BFLDLEN"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "DIGITS"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "DECPOS"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(50, i5system), "FLDDSC"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(2, i5system), "EDTCDE"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "EDTWRDLEN"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(64, i5system), "EDTWRD"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(20, i5system), "COLHDG1"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(20, i5system), "COLHDG2"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(20, i5system), "COLHDG3"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(10, i5system), "IFLDNAM"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(30, i5system), "AFLDNAM"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "LENAFLD"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "NBRDBCS"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(1, i5system), "NVALALLOW"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(1, i5system), "VARFLDIND"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(4, i5system), "DATTIMFMT"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(1, i5system), "DATTIMSEP"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(1, i5system), "VARLENIND"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "DSCCCSID"));
		apiDataFormat.addFieldDescription(new BinaryFieldDescription(new AS400UnsignedBin4(), "FLDCCSID"));
		apiDataFormat.addFieldDescription(new CharacterFieldDescription(new AS400Text(268, i5system), "REST"));
		
		return apiDataFormat;
	}
	
	public AS400Message[] getMessageList() {
		return messageList;
	}

	public Map<String, AS400Field> getFields() {
		return fields;
	}
	
	
}
