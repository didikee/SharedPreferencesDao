package com.didikee.spdaolib.bizs;

/**
 * Created by didik on 2016/12/8.
 */

public class SPDaoException extends Exception {
    private int errorCode;
    private String errorMsg;

    public SPDaoException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
