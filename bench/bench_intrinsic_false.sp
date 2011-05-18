
hundred_false = #(
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false. 
  false.  false. false. false. false. false. false. false. false. false.
)

benchmark("intrinsic: false", 10, 1000, hundred_false)
Something with(false: 42)
benchmark("intrinsic: false (with deopt)", 10, 1000, hundred_false)

