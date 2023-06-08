package com.github.ghkvud2.ft4j.eai;

import com.github.ghkvud2.ft4j.annotation.Message;
import com.github.ghkvud2.ft4j.annotation.Order;
import com.github.ghkvud2.ft4j.eai.common.*;

public class EaiCommon<T> {

	@Order(1)
	private SYSTEM_HEADER systemHeader;

	@Order(2)
	private TRN_HEADER trnHeader;

	@Order(3)
	private DAT_HDR datHdr;

	@Order(4)
	private T body;

	@Order(5)
	@Message(length = 2, defaultValue = "@@")
	private String end;

	public EaiCommon() {
		super();
	}

	public SYSTEM_HEADER getSystemHeader() {
		return systemHeader;
	}

	public TRN_HEADER getTrnHeader() {
		return trnHeader;
	}

	public DAT_HDR getDatHdr() {
		return datHdr;
	}

	public T getBody() {
		return body;
	}

	public String getEnd() {
		return end;
	}

	public void setSystemHeader(SYSTEM_HEADER systemHeader) {
		this.systemHeader = systemHeader;
	}

	public void setTrnHeader(TRN_HEADER trnHeader) {
		this.trnHeader = trnHeader;
	}

	public void setDatHdr(DAT_HDR datHdr) {
		this.datHdr = datHdr;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
