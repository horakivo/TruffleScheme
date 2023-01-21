(define random-list
  (lambda (len)
    (define generate
      (lambda (len p q s result)
        (if (= len 0)
            result
            ((lambda (value)
               (generate (- len 1) p q value (cons value result)))
             (modulo (* s s) (* p q))))))
      (generate len 101 17 3 '())))


(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)
(random-list 500000)

(define start (current-milliseconds))

(random-list 500000)

(define end (current-milliseconds))
(- end start)