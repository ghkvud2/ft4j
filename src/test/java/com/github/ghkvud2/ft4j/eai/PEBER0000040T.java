package com.github.ghkvud2.ft4j.eai;

import com.github.ghkvud2.ft4j.annotation.*;

public class PEBER0000040T {

	@Order(1)
	@Message(length = 6)
	private String SBR_CD;

	@Order(2)
	@Message(length = 8)
	private String OPR_CD;

	@Order(3)
	@LongValue(length = 5)
	private long CNT_SUM;

	public PEBER0000040T() {
		super();
	}

	public String getSBR_CD() {
		return SBR_CD;
	}

	public void setSBR_CD(String sBR_CD) {
		SBR_CD = sBR_CD;
	}

	public String getOPR_CD() {
		return OPR_CD;
	}

	public void setOPR_CD(String oPR_CD) {
		OPR_CD = oPR_CD;
	}

	public long getCNT_SUM() {
		return CNT_SUM;
	}

	public void setCNT_SUM(long cNT_SUM) {
		CNT_SUM = cNT_SUM;
	}

}
