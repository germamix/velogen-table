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

public class AS400Field {

	private String fieldName;
	private String dataTyp;
	private String use;
	private int outBufferPos;
	private int inpBufferPos;
	private int binFieldLen;
	private int digits;
	private int decPos;
	private String fieldDesc;
	private String edtCde;
	private int edtWrdLen;
	private String edtWrd;
	private String colHdg1;
	private String colHdg2;
	private String colHdg3;
	private String intFeldName;
	private String altFeldName;
	private int altFieldLen;
	private int nbrDBCS;
	private String nullValAllowed;
	private String varFieldInd;
	private String datTimFmt;
	private String datTimSep;
	private String varLenFieldInd;
	private int fldDscCCSID;
	private int fldDtaCCSID;
	
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getDataTyp() {
		return dataTyp;
	}
	public void setDataTyp(String dataTyp) {
		this.dataTyp = dataTyp;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public int getOutBufferPos() {
		return outBufferPos;
	}
	public void setOutBufferPos(int outBufferPos) {
		this.outBufferPos = outBufferPos;
	}
	public int getInpBufferPos() {
		return inpBufferPos;
	}
	public void setInpBufferPos(int inpBufferPos) {
		this.inpBufferPos = inpBufferPos;
	}
	public int getDigits() {
		return digits;
	}
	public void setDigits(int digits) {
		this.digits = digits;
	}
	public int getDecPos() {
		return decPos;
	}
	public void setDecPos(int decPos) {
		this.decPos = decPos;
	}
	public String getFieldDesc() {
		return fieldDesc;
	}
	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}
	public String getEdtCde() {
		return edtCde;
	}
	public void setEdtCde(String edtCde) {
		this.edtCde = edtCde;
	}
	public int getEdtWrdLen() {
		return edtWrdLen;
	}
	public void setEdtWrdLen(int edtWrdLen) {
		this.edtWrdLen = edtWrdLen;
	}
	public String getEdtWrd() {
		return edtWrd;
	}
	public void setEdtWrd(String edtWrd) {
		this.edtWrd = edtWrd;
	}
	public String getColHdg1() {
		return colHdg1;
	}
	public void setColHdg1(String colHdg1) {
		this.colHdg1 = colHdg1;
	}
	public String getColHdg2() {
		return colHdg2;
	}
	public void setColHdg2(String colHdg2) {
		this.colHdg2 = colHdg2;
	}
	public String getColHdg3() {
		return colHdg3;
	}
	public void setColHdg3(String colHdg3) {
		this.colHdg3 = colHdg3;
	}
	public String getIntFeldName() {
		return intFeldName;
	}
	public void setIntFeldName(String intFeldName) {
		this.intFeldName = intFeldName;
	}
	public String getAltFeldName() {
		return altFeldName;
	}
	public void setAltFeldName(String altFeldName) {
		this.altFeldName = altFeldName;
	}
	public int getAltFieldLen() {
		return altFieldLen;
	}
	public void setAltFieldLen(int altFieldLen) {
		this.altFieldLen = altFieldLen;
	}
	public int getNbrDBCS() {
		return nbrDBCS;
	}
	public void setNbrDBCS(int nbrDBCS) {
		this.nbrDBCS = nbrDBCS;
	}
	public String getNullValAllowed() {
		return nullValAllowed;
	}
	public void setNullValAllowed(String nullValAllowed) {
		this.nullValAllowed = nullValAllowed;
	}
	public String getVarFieldInd() {
		return varFieldInd;
	}
	public void setVarFieldInd(String varFieldInd) {
		this.varFieldInd = varFieldInd;
	}
	public String getDatTimFmt() {
		return datTimFmt;
	}
	public void setDatTimFmt(String datTimFmt) {
		this.datTimFmt = datTimFmt;
	}
	public String getDatTimSep() {
		return datTimSep;
	}
	public void setDatTimSep(String datTimSep) {
		this.datTimSep = datTimSep;
	}
	public String getVarLenFieldInd() {
		return varLenFieldInd;
	}
	public void setVarLenFieldInd(String varLenFieldInd) {
		this.varLenFieldInd = varLenFieldInd;
	}
	public int getFldDscCCSID() {
		return fldDscCCSID;
	}
	public void setFldDscCCSID(int fldDscCCSID) {
		this.fldDscCCSID = fldDscCCSID;
	}
	public int getFldDtaCCSID() {
		return fldDtaCCSID;
	}
	public void setFldDtaCCSID(int fldDtaCCSID) {
		this.fldDtaCCSID = fldDtaCCSID;
	}
	public void setBinFieldLen(int binFieldLen) {
		this.binFieldLen = binFieldLen;
	}
	public int getBinFieldLen() {
		return binFieldLen;
	}
	
	
	
}
