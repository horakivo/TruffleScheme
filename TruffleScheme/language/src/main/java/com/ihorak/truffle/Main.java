package com.ihorak.truffle;

import org.graalvm.polyglot.Context;
import com.ihorak.truffle.parser.Reader;
import org.antlr.v4.runtime.CharStreams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) {

        Context context = Context.create();

        var test = context.eval("scm", "5");
        System.out.println(test);


//        var reader = new BufferedReader(new InputStreamReader(System.in));
//        GlobalEnvironment globalEnvironment = new GlobalEnvironment();
//        while (true) {
//            try {
//                System.out.print("> ");
//                String program = reader.readLine();
//
//                var expr = Reader.readRuntimeExpr(CharStreams.fromString(program));
//                var result= expr.executeGeneric(globalEnvironment.getGlobalVirtualFrame());
//                System.out.println(result);
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }
    }
}

//pavel tichnovsky na rootu
