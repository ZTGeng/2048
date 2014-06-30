

public class Keyboard implements Runnable {
    
    private Drop drop;
    
    public Keyboard(Drop drop) {
        this.drop = drop;
    }

    @Override
    public void run() {
//        int count = 0;
        while (true) {
//            System.out.println("loop: " + count++);
            drop.put();
        }
    }
    
}
