;;; http://www.cs.hofstra.edu/~cscccl/csc123/quicksort.scm

(define generate
  (lambda (len p q s result)
    (if (= len 0)
        result
        (let ((value (modulo (* s s) (* p q))))
          (generate (- len 1) p q value (cons value result))))))

(define pivot
  (lambda (l)
    (cond ((null? l) 'done)
          ((null? (cdr l)) 'done)
          ((<= (car l) (car (cdr l))) (pivot (cdr l)))
          (#t (car l)))))



(define partition
  (lambda (piv l p1 p2)
    (if (null? l) (list p1 p2)
        (if (< (car l) piv)
            (partition piv (cdr l) (cons (car l) p1) p2)
            (partition piv (cdr l) p1 (cons (car l) p2))))))

(define quicksort1
  (lambda (l)
    (let ((piv (pivot l)))
      (if (equal? piv 'done)
          l
          (let ((parts (partition piv l '() '())))
            (append (quicksort1 (car parts))
                    (quicksort1 (car (cdr parts)))))))))

(define random-list
  (lambda (len)
    (generate len 101 17 3 '())))



(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))
(quicksort1 (random-list 500000))



(define start (current-milliseconds))

(quicksort1 (random-list 500000))

(define end (current-milliseconds))
(- end start)

