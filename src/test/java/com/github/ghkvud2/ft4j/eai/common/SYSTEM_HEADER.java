package com.github.ghkvud2.ft4j.eai.common;

import com.github.ghkvud2.ft4j.annotation.*;

public class SYSTEM_HEADER {

	@Order(1)
	@LongValue(length = 8)
	private long ALL_TLM_LEN;

	@Order(2)
	@Message(length = 4, defaultValue = "3110")
	private String VER;

	@Order(3)
	@Message(length = 1, defaultValue = "1")
	private String TLM_ENCY_DSCD;

	@Order(4)
	@Message(length = 32)
	private String ORG_GLBL_ID;

	@Order(5)
	@Message(length = 32)
	private String GLBL_ID;

	@Order(6)
	@IntValue(length = 4, defaultValue = "0000")
	private int GLBL_D_PRG_SRNO;

	@Order(7)
	@Message(length = 3, defaultValue = "INT")
	private String CHNL_CD;

	@Order(8)
	@Message(length = 3, defaultValue = "INT")
	private String CHNL_DSCD;

	@Order(9)
	@Message(length = 39)
	private String CLNT_IPAD;

	@Order(10)
	@Message(length = 12, defaultValue = "000000000000")
	private String CLNT_MAC;

	@Order(11)
	@Message(length = 1, defaultValue = "D")
	private String ENV_INF_DSCD;

	@Order(12)
	@Message(length = 3, defaultValue = "PEB")
	private String FST_TMS_SYS_CD;

	@Order(13)
	@Message(length = 2, defaultValue = "KR")
	private String LANG_DSCD;

	@Order(14)
	@Message(length = 3, defaultValue = "PEB")
	private String TMS_SYS_CD;

	@Order(15)
	@Message(length = 8, defaultValue = "ESST1")
	private String MD_KDCD;

	@Order(16)
	@Message(length = 12)
	private String INTF_ID;

	@Order(17)
	@Message(length = 25)
	private String MCA_CHNL_SESS_ID;

	@Order(18)
	@Message(length = 5)
	private String INTF_SYS_NODE_NO;

	@Order(19)
	@Message(length = 3)
	private String REQ_SYS_CD;

	@Order(20)
	@Message(length = 3)
	private String REQ_SYS_NODE_ID;

	@Order(21)
	@Message(length = 1)
	private String TRN_SYN_DSCD;

	@Order(22)
	@Message(length = 1)
	private String REQ_RSP_DSCD;

	@Order(23)
	@Message(length = 17)
	private String TLM_REQ_DTM;

	@Order(24)
	@Message(length = 1)
	private String TTL_USG_YN;

	@Order(25)
	@Message(length = 6)
	private String FST_STA_TM;

	@Order(26)
	@IntValue(length = 31)
	private int VLD_TIM;

	@Order(27)
	@Message(length = 3)
	private String RSP_SYS_CD;

	@Order(28)
	@Message(length = 17)
	private String TLM_RSP_DTM;

	@Order(29)
	@Message(length = 1)
	private String RSP_RST_DSCD;

	@Order(30)
	@Message(length = 3)
	private String MSG_OCC_SYS_CD;

	@Order(31)
	@Message(length = 10)
	private String MSG_CD;

	@Order(32)
	@Message(length = 3)
	private String LST_CHNL_TYCD;

	@Order(33)
	@IntValue(length = 4)
	private int CHNL_TLM_CMNP_LEN;

	@Order(34)
	@Message(length = 1)
	private String SYS_SMLT_DISV;

	@Order(35)
	@Message(length = 1)
	private String REQ_SRVC_CETR_DSCD;

	@Order(36)
	@Message(length = 1)
	private String REQ_SRVC_TP_DSCD;

	@Order(37)
	@Message(length = 6)
	private String REQ_SRVC_ID;

	@Order(38)
	@Message(length = 118)
	private String SPR;

	public SYSTEM_HEADER() {
		super();
	}

}
