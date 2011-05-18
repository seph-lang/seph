
fact = #(n,
  facc = #(acc, n,
    if(n == 0,
      acc,
      facc(n * acc, n - 1)))
  facc(1, n))


benchmark("tail recursive factorial(30001)", 10, 1, fact(30001))
