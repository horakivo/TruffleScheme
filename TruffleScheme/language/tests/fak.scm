(define fak
  (lambda (x)
    (define iter
      (lambda (n result)
        (if (= n 0)
            result
            (iter (- n 1) (+ n result)))))

    (iter x 1)))

(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)
(fak 10000000)



(define start (current-milliseconds))

(fak 10000000)

(define end (current-milliseconds))
(- end start)


