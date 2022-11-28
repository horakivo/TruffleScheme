# TruffleScheme
Scheme (R5RS) implementation on Truffle framework.

# Build
Building is done using Maven. Please for now skip tests since a lot of stuff is WIP and some tests are currently failing.

```
    cd TruffleScheme
    mvn clean install -DskipTests
```

# Run
You can run the the tests (after you build it) using this command

```
cd TruffleScheme

java -cp launcher/target/scm-launcher.jar -Dtruffle.class.path.append=language/target/truffleScheme.jar com.ihorak.truffle.launcher.Main language/tests/tak.scm
```

## Supported
- +, -, *, /
- pairs and lists
- car, cdr, cons, length, list, map*
- \>, >=, <, <=, = 
- and, or
- lambda, define
- let, let*
- if, cond
- quote, quasiquote
- macros (not hygienic rn)
- eval
- current-milliseconds, display, newline
