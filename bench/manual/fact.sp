
fact = #(n,
  facc = #(acc, n,
    if(n == 0,
      acc,
      facc(n * acc, n - 1)))
  facc(1, n))

benchmark("tail recursive factorial(1001)", 10, 100, fact(1001))
