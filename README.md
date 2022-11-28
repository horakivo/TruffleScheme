# TruffleScheme
Scheme (R5RS) implementation on Truffle framework.

#Build
Building is done using Maven. Please for now skip tests since a lot of stuff is WIP and some tests are currently failing.

#+begin_src shell-script
    cd TruffleScheme
    mvn clean install -DskipTests
#+end_src


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
