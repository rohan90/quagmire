package com.rohan90.quagmire;

/**
 * Created by rohan on 2/11/17.
 */

public class RequestPermissionException extends Throwable {
    private Exception exception;
    private String permissionString;

    public RequestPermissionException(Exception e) {
        this.exception = e;
    }

    @Override
    public void printStackTrace() {
        exception.printStackTrace();
    }

    @Override
    public String getMessage() {
        return exception.getMessage();
    }

    public RequestPermissionException(String permission) {
        this.permissionString = permission;
    }

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }
}
