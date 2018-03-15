import java.util.ArrayList;
import java.util.List;

class TSJavaStack<T> {

    private int maxSize;
    private int top = -1;
    private List<T> elements = new ArrayList<>(maxSize);

    public TSJavaStack(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.maxSize = maxSize;
    }

    public synchronized void push(T t) {
        while ((top + 1) == maxSize) {
            try {
                wait();
            } catch (InterruptedException e){}
        }
        System.out.println(t + " pushed at " + (top + 1));
        top++;
        elements.add(t);
        notifyAll();
    }

    public synchronized T pop() {
        while (top < 0) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        System.out.println(elements.get(top) + " popped at " + top);
        notifyAll();
        return elements.get(top--);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
