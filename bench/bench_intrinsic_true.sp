
hundred_true = #(
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true. 
  true.  true. true. true. true. true. true. true. true. true.
)

benchmark("intrinsic: true", 10, 1000, hundred_true)
Something with(true: 42)
benchmark("intrinsic: true (with deopt)", 10, 1000, hundred_true)
