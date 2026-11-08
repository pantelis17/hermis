package com.hermis.exception;

public class PlatformException extends RuntimeException {
    public PlatformException(Exception ex) {
        super(ex);
    }

    public PlatformException(String reason) {
        super(new RuntimeException(reason));
    }

    public PlatformException(Exception ex, String reason) {
        super(reason, ex);
    }
}
