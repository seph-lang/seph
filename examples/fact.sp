
fact_rec = #(n,
  if(n == 0,
    1,
    n * fact_rec(n - 1)))

fact_trec = #(n,
  fact_acc = #(acc, n,
    if(n == 0,
      acc,
      fact_acc(n * acc, n - 1)))
  fact_acc(1, n))

fact_rec(10) println

    
