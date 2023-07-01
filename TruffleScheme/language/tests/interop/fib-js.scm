(eval-source "js" "function fib(n) {
                       if (n < 2) {
                           return n;
                       }
                       return fib(n - 1) + fib(n - 2);
                   }")

((read-global-scope "js" "fib") 5)