
flat_short_scope_control = #(
  x = 42
)

flat_short_scope = #(
  x = 42
  x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. x. 
)

benchmark("control", 10, 10000000, flat_short_scope_control)
benchmark("variable reading", 10, 10000000, flat_short_scope)
