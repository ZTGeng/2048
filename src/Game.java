import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Runnable {
    
    final static int INITIAL_NUM = 3;
    final static int NUM_PER_ROUND = 1;
    final static int WIN_NUM = 11; // 2 to 11 == 2048
    final static Font font1 = new Font("SansSerif", Font.BOLD, 32);
    final static Font font2 = new Font("SansSerif", Font.BOLD, 14);
    final static String string1 = "W - UP; S - DOWN; A - LEFT; D - RIGHT";
    final static String string2 = "Press any key to exit!";
    final static int LEFT = 0;
    final static int UP = 1;
    final static int RIGHT = 2;
    final static int DOWN = 3;
    
    private int row, col;
    private int[][] board;
    private boolean win;
    private Drop drop;

    public Game(int r, int c, Drop drop) {
        row = r;
        col = c;
        board = new int[col][row];
        win = false;
        add2(INITIAL_NUM);
        this.drop = drop;
        
        double width = Math.max(col, row);
        StdDraw.setXscale(0, width * 10);
        StdDraw.setYscale(0, width * 10);
    }
    
    public Game(Drop drop) {
        this(4, 4, drop);
    }
    
    private boolean merge(int[] line) {
        
        int n = line.length;
        int cur = 0;
        int hand = -1;
        
        int zeroTail = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (line[i] == 0)
                zeroTail++;
            else
                break;
        }
        
        for (int i = 0; i < n; i++) {
            if (line[i] == 0) {
                continue;
            }
            if (hand == -1) {
                hand = line[i];
                continue;
            }
            if (hand != line[i]) {
                line[cur++] = hand;
                hand = line[i];
            } else {
                line[cur++] = ++hand;
                if (hand >= WIN_NUM)
                    win = true;
                hand = -1;
            }
        }
        if (hand != -1)
            line[cur++] = hand;
        zeroTail = n - zeroTail - cur;
        for (; cur < n; cur++)
            line[cur] = 0;
        return zeroTail > 0;
    }
    
    private boolean move(int d) {
        boolean changed = false;
        int in, out;
        if (d % 2 == 0) {
            out = col; in = row;
        } else {
            out = row; in = col;
        }
        int[] line = new int[in];
        for (int o = 0; o < out; o++) {
            for (int i = 0; i < in; i++) {
                int l = (d < 2) ? i : in - i - 1;
                line[i] = (d % 2 == 0) ? board[o][l] : board[l][o];
            }
            changed = merge(line) || changed;
            for (int i = 0; i < in; i++) {
                int l = (d < 2) ? i : in - i - 1;
                if (d % 2 == 0)
                    board[o][l] = line[i];
                else
                    board[l][o] = line[i];
            }
        }
        return changed;
    }
    
    private boolean fail() {
        
        int[][] backup = new int[col][row];
        for (int c = 0; c < col; c++) {
            for (int r = 0; r < row; r++) {
                backup[c][r] = board[c][r];
            }
        }
        if (move(LEFT) || move(UP)) {
            board = backup;
            return false;
        } else {
            return true;
        }
    }
    
    private boolean add2(int num) {
        
        ArrayList<int[]> zeros = new ArrayList<int[]>();
        
        for (int c = 0; c < col; c++) {
            for (int r = 0; r < row; r++) {
                if (board[c][r] == 0) {
                    int[] temp = { c, r };
                    zeros.add(temp);
                }
            }
        }
        
        // if already full, return true.
        if (zeros.size() == 0)
            return true;
        
        Random random = new Random();
        
        if (zeros.size() < num)
            num = zeros.size();
        for (int i = 0; i < num; i++) {
            int pos = random.nextInt(zeros.size());
            board[zeros.get(pos)[0]][zeros.get(pos)[1]] = 1;
            zeros.remove(pos);
        }
        
        // if full, return true. not full, false.
        return zeros.size() == 0;
    }
    
    public void run() {
        
        draw();

        while (true) {
            int direct = -1;
//            System.out.println("Need next");
            direct = drop.take();
            
            if (direct == -1)
                continue;

            if (!move(direct))
                continue;
            
            draw(1000);
            
            if (win) {
                win();
                break;
            }
            
            if (add2(NUM_PER_ROUND)) {
                if (fail()) {
                    draw();
                    end();
                    break;
                }
            }
            draw();
        }
    }
    
    
    private void draw(int t) {
        StdDraw.clear();
        double width = Math.max(col, row);
        StdDraw.setPenRadius(0.01);
        StdDraw.setFont(font1);
        for (int c = 0; c < col; c++) {
            for (int r = 0; r < row; r++) {
                double x = (width - row + r * 2 + 1) * 5;
                double y = (width + col - c * 2 - 1) * 5;
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(x, y, 5);
                if (board[c][r] != 0) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.text(x, y, 
                            String.valueOf(1 << board[c][r]));
                }
            }
        }
        StdDraw.setFont(font2);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(width * 5, width * 10.3, string1);
        
        StdDraw.show(t);
    }
    
    private void draw() {
        draw(0);
    }
    
    private void win() {
        double width = Math.max(col, row);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledRectangle(width * 5, width * 5, width * 3, width / 2);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(width * 5, width * 5, "YOU WIN!!");
        StdDraw.setFont(font2);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(width * 5, -width * 0.3, string2);
        StdDraw.show();
        while (!StdDraw.hasNextKeyTyped()) {}
        System.exit(1);
    }
    
    private void end() {
        double width = Math.max(col, row);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledRectangle(width * 5, width * 5, width * 3, width / 2);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(width * 5, width * 5, "GAME OVER!!");
        StdDraw.setFont(font2);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(width * 5, -width * 0.3, string2);
        StdDraw.show();
        while (!StdDraw.hasNextKeyTyped()) {}
        System.exit(1);
    }
    
}
