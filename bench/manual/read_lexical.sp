flat_short_scope = #(
  x = 42
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
)

benchmark("variable reading, flat short lexical scope", 2, 100000, flat_short_scope)
