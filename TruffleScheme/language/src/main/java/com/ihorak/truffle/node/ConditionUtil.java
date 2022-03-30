package com.ihorak.truffle.node;

public class ConditionUtil {


    /**
     * This is how scheme evaluates conditions. Basically everything except #f is true
     * Even () is true
     * */
    public static boolean convertObjectToBoolean(Object object) {
        return !(object instanceof Boolean) || (boolean) object;
    }
}
