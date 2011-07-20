moves = 0

doHanoi = method(n, to, from, using,
  if(n > 0,
    doHanoi(n-1, using, from, to)
    moves++
    doHanoi(n-1, to, using, from)
  )
)

hanoi = method(n,
  doHanoi(n, 3, 1, 2)
  "#{moves} moves total" println
)

hanoi(20)
