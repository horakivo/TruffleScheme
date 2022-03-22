package com.ihorak.truffle;

import org.graalvm.polyglot.Context;

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
