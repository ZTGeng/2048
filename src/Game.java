import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
    
    final static int INITIAL_NUM = 3;
    final static int NUM_PER_ROUND = 2;
    final static int WIN_NUM = 2048;
    
    int row, col;
    int[][] board;
    boolean win;
    
    public Game(int r, int c) {
        row = r;
        col = c;
        board = new int[col][row];
        win = false;
        add2(INITIAL_NUM);
    }
    
    public Game() {
        this(4, 4);
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
                line[cur++] = hand * 2;
                if (hand * 2 >= WIN_NUM)
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
    
    private boolean left() {
        boolean changed = false;
        int[] line = new int[row];
        for (int c = 0; c < col; c++) {
            for (int r = 0; r < row; r++) {
                line[r] = board[c][r];
            }
            changed = merge(line) || changed;
            for (int r = 0; r < row; r++) {
                board[c][r] = line[r];
            }
        }
        return changed;
    }
    
    private boolean right() {
        boolean changed = false;
        int[] line = new int[row];
        for (int c = 0; c < col; c++) {
            for (int r = 0; r < row; r++) {
                line[r] = board[c][row - r - 1];
            }
            changed = merge(line) || changed;
            for (int r = 0; r < row; r++) {
                board[c][row - r - 1] = line[r];
            }
        }
        return changed;
    }
    
    private boolean up() {
        boolean changed = false;
        int[] line = new int[col];
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                line[c] = board[c][r];
            }
            changed = merge(line) || changed;
            for (int c = 0; c < col; c++) {
                board[c][r] = line[c];
            }
        }
        return changed;
    }
    
    private boolean down() {
        boolean changed = false;
        int[] line = new int[col];
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                line[c] = board[col - c - 1][r];
            }
            changed = merge(line) || changed;
            for (int c = 0; c < col; c++) {
                board[col - c - 1][r] = line[c];
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
        if (left() || up()) {
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
            board[zeros.get(pos)[0]][zeros.get(pos)[1]] = 2;
            zeros.remove(pos);
        }
        
        // if full, return true. not full, false.
        return zeros.size() == 0;
    }
    
    public void start() {
        
        System.out.println("GAME START!!");
        show();
        draw();
        Scanner scanner = new Scanner(System.in);
        System.out.println("W - UP; S - DOWN; A - LEFT; D - RIGHT");
        while (true) {
            String act = scanner.nextLine();
            if (act.equals("A") || act.equals("a")) {
                if (!left())
                    continue;
            } else if (act.equals("D") || act.equals("d")) {
                if (!right())
                    continue;
            } else if (act.equals("W") || act.equals("w")) {
                if (!up())
                    continue;
            } else if (act.equals("S") || act.equals("s")) {
                if (!down())
                    continue;
            } else {
                continue;
            }
            
            show();
            draw(1000);
            
            if (win) {
                System.out.println("YOU WIN!!");
                break;
            }
            
            if (add2(NUM_PER_ROUND)) {
                if (fail()) {
                    show();
                    draw();
                    System.out.println("GAME OVER!!");
                    break;
                }
            }
            
            show();
            draw();
            System.out.println("W - UP; S - DOWN; A - LEFT; D - RIGHT");
            
        }
        scanner.close();
    }
    
    private void show() {
        for (int c = 0; c < col; c++) {
            System.out.print("|");
            for (int r = 0; r < row; r++) {
                System.out.printf("%4d|", board[c][r]);
            }
            System.out.println();
        }
        for (int r = 0; r < row; r++) {
            System.out.print("-----");
        }
        System.out.println("-");
    }
    
    private void draw(int t) {
        StdDraw.clear();
        double windth = Math.max(col, row);
        StdDraw.setXscale(0, windth * 10);
        StdDraw.setYscale(0, windth * 10);
        StdDraw.setPenRadius(0.01);
        StdDraw.setFont(new Font("SansSerif", Font.BOLD, 32));
        for (int c = 0; c < col; c++) {
            for (int r = 0; r < row; r++) {
                double x = (windth - row + r * 2 + 1) * 5;
                double y = (windth + col - c * 2 - 1) * 5;
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.square(x, y, 5);
                if (board[c][r] != 0) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.text(x, y, String.valueOf(board[c][r]));
                }
            }
        }
        StdDraw.show(t);
    }
    
    private void draw() {
        draw(0);
    }
    
    public static void main(String[] args) {
        
        Game g = new Game();
        g.start();
        
    }
    
}
