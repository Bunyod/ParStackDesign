import java.util.Random;

public class TSRunner {
    public static void main(String[ ] args) {
        TSJavaStack stack = new TSJavaStack<Integer>(5);
        new Popper(stack).start();
        new Pusher(stack).start();
        new Pusher(stack).start();
    }
}

class Pusher extends Thread {
    private Random rand = new Random();
    private TSJavaStack<Integer> stack;

    Pusher(TSJavaStack<Integer> stack) { this.stack = stack; }

    @Override
    public void run() {
        while (true) {
            stack.push(rand.nextInt(100));
            try {
                Thread.sleep(rand.nextInt(200));
            }  catch(InterruptedException e) { }
        }
    }
}

class Popper extends Thread {
    private Random rand = new Random();
    private TSJavaStack<Integer> stack;

    Popper(TSJavaStack<Integer> stack) { this.stack = stack; }

    @Override
    public void run() {
        while (true) {
            stack.pop();
            try {
                Thread.sleep(rand.nextInt(100));
            } catch(InterruptedException e) { }
        }
    }
}
