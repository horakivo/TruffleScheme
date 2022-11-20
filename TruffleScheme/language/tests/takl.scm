(define listn
  (lambda (n)
    (if (= n 0)
      '()
      (cons n (listn (- n 1))))))

(define l18 (listn 18))
(define l12 (listn 12))
(define  l6 (listn 6))

(define mas
  (lambda (x y z)
    (if (not (shorterp y x))
        z
        (mas (mas (cdr x) y z)
             (mas (cdr y) z x)
             (mas (cdr z) x y)))))

(define shorterp
  (lambda (x y)
    (and (not (null? y))
         (or (null? x)
             (shorterp (cdr x)
                       (cdr y))))))

(define loop
  (lambda (n)
    (mas l18 l12 l6)
    (if (> n 0) (loop (- n 1)))))


(define start (current-milliseconds))

(loop 10)

(define end (current-milliseconds))
(- end start)
