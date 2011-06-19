
hundred_ifs = #(
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). 
  if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1). if(true, 0, 1)
)

benchmark("intrinsic: if", 10, 1000, hundred_ifs)
Something with(if: 42)
benchmark("intrinsic: if (with deopt)", 10, 1000, hundred_ifs)
