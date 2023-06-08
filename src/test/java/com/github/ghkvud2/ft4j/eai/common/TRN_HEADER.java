package com.github.ghkvud2.ft4j.eai.common;

import com.github.ghkvud2.ft4j.annotation.*;

public class TRN_HEADER {

	@Order(1)
	@Message(length = 6)
	private String TRM_BRNO;

	@Order(2)
	@Message(length = 12)
	private String TRN_TRM_NO;

	@Order(3)
	@Message(length = 6)
	private String DL_BRCD;

	@Order(4)
	@Message(length = 8)
	private String OPR_NO;

	@Order(5)
	@Message(length = 1)
	private String RLPE_APV_DSCD;

	@Order(6)
	@Message(length = 1)
	private String AVLPE_RSP_RTCD;

	@Order(7)
	@Message(length = 1)
	private String RLPE_APV_SQCN;

	@Order(8)
	@Message(length = 11)
	private String SCRN_ID;

	@Order(9)
	@Message(length = 5)
	private String TRN_SCRN_NO;

	@Order(10)
	@Message(length = 9)
	private String TRN_CD;

	@Order(11)
	@Message(length = 11)
	private String SRVC_ID;

	@Order(12)
	@Message(length = 2)
	private String FUNC_CD;

	@Order(13)
	@Message(length = 1)
	private String CAN_TRN_DSCD;

	@Order(14)
	@Message(length = 1)
	private String INP_TLM_TYCD;

	@Order(15)
	@Message(length = 1)
	private String OUP_TLM_TYCD;

	@Order(16)
	@Message(length = 1)
	private String LQTY_DAT_PRC_DIS;

	@Order(17)
	@IntValue(length = 4)
	private int TLM_CTIN_SRNO;

	@Order(18)
	@Message(length = 1)
	private String LQTY_INP_CTIN_YN;

	@Order(19)
	@Message(length = 1)
	private String NXT_PAGE_RQU_YN;

	@Order(20)
	@Message(length = 1)
	private String SMLT_TRN_DSCD;

	@Order(21)
	@Message(length = 1)
	private String ACC_MOD_DSCD;

	@Order(22)
	@Message(length = 1)
	private String DL_OPNG_DSCD;

	@Order(23)
	@Message(length = 56)
	private String TRN_LOG_KEY_VAL;

	@Order(24)
	@Message(length = 1)
	private String CC_ONL_STCD;

	@Order(25)
	@Message(length = 1)
	private String DL_RSP_INP_RCOVR_DSCD;

	@Order(26)
	@Message(length = 1)
	private String FROT_RSP_OUP_WAIT_STCD;

	@Order(27)
	@Message(length = 1)
	private String TRF_NACRD_YN;

	@Order(28)
	@Message(length = 9)
	private String RST_RECP_TRN_CD;

	@Order(29)
	@Message(length = 11)
	private String RST_RECP_SRVC_ID;

	@Order(30)
	@Message(length = 1)
	private String EXNK_DSCD;

	@Order(31)
	@Message(length = 4)
	private String PBOK_SRNO;

	@Order(32)
	@Message(length = 1)
	private String MSK_NTGT_TRN_YN;

	@Order(33)
	@Message(length = 1)
	private String RLY_TRN_DSCG_YN;

	@Order(34)
	@Message(length = 11)
	private String ITCSNO;

	@Order(35)
	@Message(length = 116)
	private String SPR;

	public TRN_HEADER() {
		super();
	}

}
