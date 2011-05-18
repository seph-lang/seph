
fib = #(n,
  if(n < 2,
    n,
    fib(n - 1) + fib(n - 2)))

fibIter = #(n
  i = 0
  j = 1
  cur = 1
  while(cur <= n,
    k = i
    i = j
    j = k + j
    cur += 1)
  i
)

benchmark("recursive fib(15)", 10, 20, fib(15))
benchmark("recursive fib(30)", 10,  1, fib(30))
; benchmark("iterative fib(300000)", 10, 1, fibIter(300000))
