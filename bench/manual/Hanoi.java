
public class Hanoi {
    private int moves;
    public Hanoi() {
        this.moves = 0;
    }

    private void doHanoi(int n, int to, int from, int using) {
        if(n > 0) {
            doHanoi(n - 1, using, from, to);
            moves++;
            doHanoi(n - 1, to, using, from);
        }
    }

    public void hanoi(int n) {
        doHanoi(n, 3, 1, 2);
        System.out.println("" + moves + " moves total");
    }

    public static void main(String[] args) throws Exception {
        new Hanoi().hanoi(20);
    }
}
