package com.futuretongfu.model.exception;

/**
 * Created by ChenXiaoPeng on 2017/6/23.
 */

public class NetworkException extends Exception{
    private int errorCode;
    private String errorMessage;

    public NetworkException(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage(){
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString(){
        return errorCode + ":" + errorMessage;
    }


}
