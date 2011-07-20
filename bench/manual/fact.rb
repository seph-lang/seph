
def facc(acc, n)
  if n == 0
    acc
  else
    facc(n * acc, n - 1)
  end
end

def fact(n)
  facc(1, n)
end

fact(1001)
