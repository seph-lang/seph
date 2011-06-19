
; reading cells in self object or objects in inheritance chain at different depths
; also try this with objects of different sizes

small_direct_early = #(
  obj = Something with(x: 42)
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
)

medium_direct_early = #(
  obj = Something with(x: 42, y: 55, z: 123, q: 4665)
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
)

medium_direct_late = #(
  obj = Something with(x: 42, y: 55, z: 123, q: 4665)
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
  obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. obj q. 
)

large_direct_early = #(
  obj = Something with(x1: 1, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
  obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. obj x1. 
)

large_direct_mid = #(
  obj = Something with(x1: 1, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
  obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. obj x5. 
)

large_direct_late = #(
  obj = Something with(x1: 1, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
  obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. obj x11. 
)

ancestor = #(
  obj = Something with(x: 1) with(y: 2) with(z: 3) with(q: 42)
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. 
  obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x. obj x
)

get_x = #(obj,
  obj x
)

small_direct_early_poly = #(
  obj1 = Something with(x: 42)
  obj2 = Something with(x: 43)
  obj3 = Something with(x: 44)
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3). get_x(obj1). get_x(obj2). get_x(obj3).
  get_x(obj1)
)

get_x2 = #(obj,
  obj x
)

medium_direct_early_poly = #(
  obj1 = Something with(x: 42, y: 55, z: 123, q: 4665)
  obj2 = Something with(x: 43, y: 55, z: 123, q: 4665)
  obj3 = Something with(x: 43, y: 55, z: 123, q: 4665)
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3). get_x2(obj1). get_x2(obj2). get_x2(obj3).
  get_x2(obj1)
)

get_x3 = #(obj,
  obj x
)

medium_direct_late_poly = #(
  obj1 = Something with(q: 42, y: 55, z: 123, x: 4665)
  obj2 = Something with(q: 42, y: 55, z: 123, x: 32432)
  obj3 = Something with(q: 42, y: 55, x: 123, z: 12)
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3). get_x3(obj1). get_x3(obj2). get_x3(obj3).
  get_x3(obj1)
)

get_x4 = #(obj,
  obj x
)

large_direct_early_poly = #(
  obj1 = Something with(x: 1, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  obj2 = Something with(x: 2, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  obj3 = Something with(x: 3, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3). get_x4(obj1). get_x4(obj2). get_x4(obj3).
  get_x4(obj1)
)

get_x5 = #(obj,
  obj x
)

large_direct_mid_poly = #(
  obj1 = Something with(x0: 1, x2: 2, x3: 3, x4: 4, x5: 5, x: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  obj2 = Something with(x0: 2, x2: 2, x3: 3, x4: 4, x: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  obj3 = Something with(x0: 3, x2: 2, x3: 3, x4: 4, x: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x11: 11)
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3). get_x5(obj1). get_x5(obj2). get_x5(obj3).
  get_x5(obj1)
)

get_x6 = #(obj,
  obj x
)

large_direct_late_poly = #(
  obj1 = Something with(x0: 1, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x: 11)
  obj2 = Something with(x0: 2, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x: 10, x11: 11)
  obj3 = Something with(x0: 3, x2: 2, x3: 3, x4: 4, x5: 5, x6: 6, x7: 7, x8: 8, x9: 9, x10: 10, x: 11)
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3). get_x6(obj1). get_x6(obj2). get_x6(obj3).
  get_x6(obj1)
)

get_x7 = #(obj,
  obj x
)

ancestor_poly = #(
  obj = Something with(x: 1) with(y: 2) with(z: 3) 
  obj1 = obj with(q: 42)
  obj2 = obj with(q: 43)
  obj3 = obj with(x: 3)
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3). get_x7(obj1). get_x7(obj2). get_x7(obj3).
  get_x7(obj1)
)

benchmark("cell lookup: small, direct, early", 10, 10000, small_direct_early)
benchmark("cell lookup: medium, direct, early", 10, 10000, medium_direct_early)
benchmark("cell lookup: medium, direct, late", 10, 10000, medium_direct_late)
benchmark("cell lookup: large, direct, early", 10, 10000, large_direct_early)
benchmark("cell lookup: large, direct, mid", 10, 10000, large_direct_mid)
benchmark("cell lookup: large, direct, late", 10, 10000, large_direct_late)
benchmark("cell lookup: deep ancestor", 10, 10000, ancestor)
benchmark("cell lookup: small, direct, early, polymorphic", 10, 10000, small_direct_early_poly)
benchmark("cell lookup: medium, direct, early, polymorphic", 10, 10000, medium_direct_early_poly)
benchmark("cell lookup: medium, direct, late, polymorphic", 10, 10000, medium_direct_late_poly)
benchmark("cell lookup: large, direct, early, polymorphic", 10, 10000, large_direct_early_poly)
benchmark("cell lookup: large, direct, mid, polymorphic", 10, 10000, large_direct_mid_poly)
benchmark("cell lookup: large, direct, late, polymorphic", 10, 10000, large_direct_late_poly)
benchmark("cell lookup: deep ancestor, polymorphic", 10, 10000, ancestor_poly)

