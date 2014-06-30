

public class Drop {
    
    final static int LEFT = 0;
    final static int UP = 1;
    final static int RIGHT = 2;
    final static int DOWN = 3;
    
    private int message;
    private boolean inNeed = false;
    private boolean empty = true;

    public synchronized int take() {

        while (empty) {
            inNeed = true;
            notifyAll();
            try {
//                System.out.println("game waiting");
                wait();
            } catch (InterruptedException e) {}
        }
        empty = true;
        inNeed = false;
        return message;
    }

    public synchronized void put() {
        while (!inNeed) {
            try { 
//                System.out.println("keyboard waiting");
                wait();
            } catch (InterruptedException e) {}
        }
        
        this.message = input();
        empty = false;
        inNeed = false;
        
        notifyAll();
    }
    
    private int input() {
        
        int act;
        char c;
        while (StdDraw.hasNextKeyTyped()) {
            c = StdDraw.nextKeyTyped();
//            System.out.println("clear type");
        }
        while (!StdDraw.hasNextKeyTyped());
        c = StdDraw.nextKeyTyped();

        switch (c) {
        case 'A':
        case 'a':
            act = LEFT;
            break;
        case 'D':
        case 'd':
            act = RIGHT;
            break;
        case 'W':
        case 'w':
            act = UP;
            break;
        case 'S':
        case 's':
            act = DOWN;
            break;
        default:
            act = -1;
        }
//        System.out.println("input: " + act);
        return act;
    }
}
