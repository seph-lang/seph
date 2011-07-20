
class Hanoi
  def initialize
    @moves = 0
  end
  
  def do_hanoi(n, to, from, using)
    if n > 0
      do_hanoi(n-1, using, from, to)
      @moves += 1
      do_hanoi(n-1, to, using, from)
    end
  end
  
  def hanoi(n)
    do_hanoi(n, 3, 1, 2)
    puts "#@moves moves total"
  end
end

Hanoi.new.hanoi(20)
