import java.awt.Font;

public class My2048 {
    
    final static Font font1 = new Font("SansSerif", Font.BOLD, 128);
    final static Font font2 = new Font("SansSerif", Font.BOLD, 32);
    final static String string1 = "W - UP; S - DOWN; A - LEFT; D - RIGHT";
    final static String string2 = "Press any key to exit!";
    
    static int row = 4;
    static int col = 4;
    
    private static void draw() {
        
        draw(0);
        while (!StdDraw.hasNextKeyTyped());
        row = Integer.parseInt(String.valueOf(StdDraw.nextKeyTyped()));
        draw(1);
        while (!StdDraw.hasNextKeyTyped());
        col = Integer.parseInt(String.valueOf(StdDraw.nextKeyTyped()));
        draw(2);
    }

    private static void draw(int t) {
        
        StdDraw.clear();
        StdDraw.setPenRadius(0.01);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.text(25, 35, "2048");
        
        StdDraw.setFont(font2);
        StdDraw.setPenColor(t == 0 ? StdDraw.RED : StdDraw.BLACK);
        StdDraw.text(15, 20, "Input Row:");
        StdDraw.text(30, 20, String.valueOf(row));
        StdDraw.setPenColor(t == 1 ? StdDraw.RED : StdDraw.BLACK);
        StdDraw.text(15, 10, "Input Col:");
        StdDraw.text(30, 10, String.valueOf(col));
        StdDraw.show();

    }
    
    public static void main(String[] args) {
        
        StdDraw.setXscale(0, 50);
        StdDraw.setYscale(0, 50);
        //draw();
        Drop drop = new Drop();
        Thread game = new Thread(new Game(row, col, drop));
        Thread keyboard = new Thread(new Keyboard(drop));
        game.start();
        keyboard.start();
        
    }
    
}
