;;; TAK -- A vanilla version of the TAKeuchi function.

(define tak
  (lambda (x y z)
    (if (not (< y x))
        z
        (tak (tak (- x 1) y z)
           (tak (- y 1) z x)
           (tak (- z 1) x y)))))


(define loop
  (lambda (n)
    (tak 18 12 6)
    (if (> n 0) (loop (- n 1)))))


(loop 100)
(loop 100)
(loop 100)
(loop 100)
(loop 100)
(loop 100)




(define start (current-milliseconds))

(loop 100)

(define end (current-milliseconds))
(- end start)
