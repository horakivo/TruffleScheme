;;; generates random list with numbers.

(define random-list
  (lambda (len)
    (generate len 101 17 3 '())))


(define generate
  (lambda (len p q s result)
    (if (= len 0)
        result
        (tmp (modulo (* s s) (* p q)) len p q result))))


(define tmp
  (lambda (value len p q result)
           (generate (- len 1) p q value (cons value result))))


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