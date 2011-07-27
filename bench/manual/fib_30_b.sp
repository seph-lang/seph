
fib = #(n,
  if(n < 2,
    n,
    fib(n - 1) + fib(n - 2)))

benchmark("fib(30)", 10, 1, fib(30))
