import java.awt.Font;

public class My2048 {
    
    final static Font font1 = new Font("SansSerif", Font.BOLD, 128);
    final static Font font2 = new Font("SansSerif", Font.BOLD, 32);
    final static Font font3 = new Font("SansSerif", Font.BOLD, 24);
    final static String string1 = "W - UP; S - DOWN; A - LEFT; D - RIGHT";
    final static String string2 = "Press any key to exit!";
    
    static int row = 4;
    static int col = 4;
    
    private static void draw() {
        
        String s = "123456789";
        draw(0);
        
        while (!StdDraw.hasNextKeyTyped());
        String act = String.valueOf(StdDraw.nextKeyTyped());
        if (!s.contains(act)) return;
        row = Integer.parseInt(act);
        draw(1);
        
        while (!StdDraw.hasNextKeyTyped());
        act = String.valueOf(StdDraw.nextKeyTyped());
        if (!s.contains(act)) return;
        col = Integer.parseInt(act);
        draw(2);
        
        while (!StdDraw.hasNextKeyTyped());
        return;
    }

    private static void draw(int t) {
        
        StdDraw.clear();
        StdDraw.setPenRadius(0.01);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.text(25, 35, "2048");
        
        StdDraw.setFont(font2);
        StdDraw.setPenColor(t == 0 ? StdDraw.RED : StdDraw.BLACK);
        StdDraw.text(15, 22.5, "Input Width:");
        StdDraw.text(30, 22.5, String.valueOf(row));
        StdDraw.setPenColor(t == 1 ? StdDraw.RED : StdDraw.BLACK);
        StdDraw.text(15, 15, "Input Height:");
        StdDraw.text(30, 15, String.valueOf(col));
        StdDraw.setFont(font3);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(25, 7, "Input a Integer between 1 and 9, or");
        StdDraw.setPenColor(t == 2 ? StdDraw.RED : StdDraw.BLACK);
        StdDraw.text(25, 3, "Press Any Key to Start!");
        StdDraw.show();

    }
    
    public static void main(String[] args) {
        
        StdDraw.setXscale(0, 50);
        StdDraw.setYscale(0, 50);
        draw();
        Drop drop = new Drop();
        Thread game = new Thread(new Game(row, col, drop));
        Thread keyboard = new Thread(new Keyboard(drop));
        game.start();
        keyboard.start();
        
    }
    
}
