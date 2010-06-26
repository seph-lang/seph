IntSet: Something with(
  empty?: false,
  adjoin: #(x, Adjoin with(s: self, obj: x)),
  union: #(x, Union with(left: self, right: x))
),

Adjoin: IntSet with(
  contains?: #(y, obj == y || s contains?(y))
),

Union: IntSet with(
  empty?:    #(left empty? && right empty?),
  contains?: #(y, obj == y || s contains?(y))
),

Empty: IntSet with(
  empty?: true,
  contains?: #(_, false)
),

IntegersMod: IntSet with(
  contains?: #(y, y % n == 0)
),




s = Empty
k = 2
n = 0
while(n < 1_000_000,
  if(prime?(k),
    s = s adjoin(k)
    n++
  )
  k++
)

s contains?(13) println
