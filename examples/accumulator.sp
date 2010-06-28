
;; mutating version

foo: #(n, #(i, n += i)),

;; actor version

foop = #(n,
  receive(
    (p, i), val = n + i. p <- val. foop(val))),

foo: #(n,
  p = ->(foop(n))
  #(i,
    p <- (currentProcess, i)
    receive(
      v, v)))
