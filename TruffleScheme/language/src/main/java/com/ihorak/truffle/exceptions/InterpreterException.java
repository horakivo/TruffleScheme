package com.ihorak.truffle.exceptions;

public final class InterpreterException {

    private InterpreterException() {}


    public static RuntimeException shouldNotReachHere() {
        throw shouldNotReachHere(null, null);
    }

    public static RuntimeException shouldNotReachHere(Throwable cause) {
        throw shouldNotReachHere(null, cause);
    }

    public static RuntimeException shouldNotReachHere(String message, Throwable cause) {
        throw new ShouldNotReachHere(message, cause);
    }


    static final class ShouldNotReachHere extends AssertionError {
        public ShouldNotReachHere(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
