(define countdown
  (lambda (n)
    (if (= 0 n)
        0
        (countdown (- n 1)))))




(define loop
  (lambda (n)
    (countdown 100000)
    (if (> n 0) (loop (- n 1)))))

(loop 100)
(loop 100)
(loop 100)

(countdown 10)
