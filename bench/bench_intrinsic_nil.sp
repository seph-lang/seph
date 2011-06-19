
hundred_nil = #(
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil. 
  nil.  nil. nil. nil. nil. nil. nil. nil. nil. nil.
)

benchmark("intrinsic: nil", 10, 100000, hundred_nil)
Something with(nil: 42)
benchmark("intrinsic: nil (with deopt)", 10, 100000, hundred_nil)
