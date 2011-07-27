
fib = #(
  #(a, b, [b, a + b]) iterate(1, 1) mapped(first)
)

(fib indexed(from: 1) takeWhile(second < 1000) last first + 1) println
fib indexed(from: 1) droppedWhile(second < 1000) first first println
