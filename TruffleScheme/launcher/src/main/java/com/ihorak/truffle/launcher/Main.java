package com.ihorak.truffle.launcher;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public final class Main {

    private static final String SCM = "scm";

    public static void main(String[] args) throws IOException {

        Source source;
        Map<String, String> options = new HashMap<>();
        String file = null;
        for (String arg : args) {
            if (parseOption(options, arg)) {
                continue;
            } else {
                if (file == null) {
                    file = arg;
                }
            }
        }

        if (file == null) {
            throw new IllegalArgumentException("No file");
        } else {
            source = Source.newBuilder(SCM, new File(file)).build();
        }

        System.exit(executeSource(source, options));
    }

    private static int executeSource(Source source, Map<String, String> options) {
        Context context;
        var in = System.in;
        var out = System.out;
        PrintStream err = System.err;
        try {
            context = Context.newBuilder()
                    .in(in)
                    .out(out)
                    .options(options)
                    .allowAllAccess(true)
                    .build();
        } catch (IllegalArgumentException e) {
            err.println(e.getMessage());
            return 1;
        }
        out.println("== running on " + context.getEngine());

        try {
            Value result = context.eval(source);
            if (!result.isNull()) {
                out.println(result);
            }
            return 0;
        } catch (PolyglotException ex) {
            if (ex.isInternalError()) {
                // for internal errors we print the full stack trace
                ex.printStackTrace();
            } else {
                err.println(ex.getMessage());
            }
            return 1;
        } finally {
            context.close();
        }
    }


    private static boolean parseOption(Map<String, String> options, String arg) {
        if (arg.length() <= 2 || !arg.startsWith("--")) {
            return false;
        }
        int eqIdx = arg.indexOf('=');
        String key;
        String value;
        if (eqIdx < 0) {
            key = arg.substring(2);
            value = null;
        } else {
            key = arg.substring(2, eqIdx);
            value = arg.substring(eqIdx + 1);
        }

        if (value == null) {
            value = "true";
        }
        int index = key.indexOf('.');
        String group = key;
        if (index >= 0) {
            group = group.substring(0, index);
        }
        options.put(key, value);
        return true;
    }
}
