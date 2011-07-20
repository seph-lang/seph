moves = 0

doHanoi = #(n, to, from, using,
  if(n > 0,
    doHanoi(n - 1, using, from, to)
    moves = moves + 1
    doHanoi(n - 1, to, using, from)
  )
)

hanoi = #(n,
  doHanoi(n, 3, 1, 2)
  moves print
  " moves total" println
)

hanoi(20)
