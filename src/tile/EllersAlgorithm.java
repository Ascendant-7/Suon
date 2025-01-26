package tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import main.GamePanel;

public class EllersAlgorithm {

    // MAZE
    int rows, cols;
    int[][] grid;

    // TOOLS
    HashMap<Integer, HashSet<Vector2D>> map = new HashMap<>();
    HashMap<Integer, HashSet<Integer>> temp_map = new HashMap<>();
    Random random = new Random();
    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<Vector2D> deadends = new ArrayList<>();
    public ArrayList<Vector2D> monsterLocations = new ArrayList<>();
    public ArrayList<Vector2D> chestNExit = new ArrayList<>();
    Vector2D door;

    static int set_id;     // FOR CELL ASIGNMENT
    static int current, last;  // FOR CELL PAIR JOINING
    public EllersAlgorithm(GamePanel gp) {
        rows = gp.mapWorldRow;
        cols = gp.mapWorldCol;
        grid = new int[cols][rows];
    }
    
    public int[][] loadNewMap() {
        
        initialize();
        generate();
        transform();
        spawnEntities();
        transformV2();
        setExit();
        display();
        
        return grid;
    }
    public void initialize() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (row % 2 == 0 || col % 2 == 0)
                    grid[col][row] = -1;
                else
                    grid[col][row] = 0;
            }
        }
    }
    public void generate() {

        // SET DEFAULT VALUES
        set_id = 1;

        for (int row = 1; row < rows-1; row+=2) {

            assignSets(row);
            mergeSets(row);

            if (row == rows-2) break;

            connectVerticalPaths(row);

            
            temp_map.clear();
        }
    }
    public void assignSets(int row) {

        // CELL/PATH ITERATION
        for (int col = 1; col < cols-1; col+=2) {

            if (grid[col][row] == 0) {

                grid[col][row] = set_id;
                HashSet<Vector2D> set = new HashSet<Vector2D>();
                set.add(new Vector2D(col, row));
                map.put(set_id, set);
                set_id++;
            }
        }
    }
    public void mergeSets(int row) {

        // CELL/PATH PAIR ITERATION
        for (int col = 3; col < cols-1; col+=2) {

            current = grid[col][row];
            last = grid[col-2][row];

            if (current == last) continue;

            if (row == rows-2 || random.nextInt(2) == 0) {
                map.get(current).add(new Vector2D(col-1, row));

            for (Vector2D v : map.get(current)) {
                grid[v.x][v.y] = last;
            }

            map.get(last).addAll(map.get(current));

            map.remove(current);
            }
        }
    }
    public void connectVerticalPaths(int row) {

        // VERTICAL JOINING
        // get all sets within the row
        for (int col = 1; col < cols-1; col+=2) {

            current = grid[col][row];
            if (!temp_map.containsKey(current)) {

                HashSet<Integer> set = new HashSet<>();
                set.add(col);
                temp_map.put(current, set);
            }
            else temp_map.get(current).add(col);
        }
        // randomly join vertical cells
        for (Integer set : temp_map.keySet()) {

            list.addAll(temp_map.get(set));
            Collections.shuffle(list);

            for (int i = 0; i < (list.size()+1)/2; i++) {
                
                int col = list.get(i);
                current = grid[col][row];

                grid[col][row+1] = current;
                grid[col][row+2] = current;

                map.get(current).add(new Vector2D(col, row+1));
                map.get(current).add(new Vector2D(col, row+2));
            }

            list.clear();
        }
    }
    public void transform() {
        // DEFAULT TRANSFORM
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[col][row] = grid[col][row] == -1 ? 1 : 0;
            }
        }
    }
    public void spawnEntities() {
        // MONSTER SPAWN
        monsterLocations.clear();
        deadends = new ArrayList<>();
        monsterLocations = new ArrayList<>();
        for (int row = 1; row < rows-1; row+=2) {
            for (int col = 1; col < cols-1; col+=2) {
                int walls = 0;
                if (grid[col][row+1] == 1)
                    walls++;
                if (grid[col][row-1] == 1)
                    walls++;
                if (grid[col+1][row] == 1) 
                    walls++;
                if (grid[col-1][row] == 1)
                    walls++;

                if (walls == 3) {
                    deadends.add(new Vector2D(col, row));
                }

            }
        }
        System.out.println("deadends: "+deadends.size());
        Collections.shuffle(deadends);

        deadends.removeIf(v -> Math.abs(25 - v.x) < 10 && Math.abs(25 - v.y) < 10);
        for (int i = 0; i < 10; i++) {
            if (deadends.size() <= i)
                break;
            monsterLocations.add(deadends.get(i));
            deadends.remove(i);
        }
        for (int i = 0; i < 4; i++) {
            if (deadends.size() <= i)
                break;
            chestNExit.add(deadends.get(i));
            deadends.remove(i);
        }

        deadends.removeIf(v -> Math.abs(25 - v.x) < 20 && Math.abs(25 - v.y) < 20);
        chestNExit.add(deadends.get(0));
        deadends.remove(0);
        
        deadends.removeIf(v -> (v.y == 1 || v.y == 49) && Math.abs(chestNExit.getLast().x - v.x) < 20 && Math.abs(chestNExit.getLast().y - v.y) < 20);
        door = deadends.get(0);
        if (door.y == 1) door.y = 0;
        else door.y = 50;
        System.out.println("exit is at "+door.x+", "+door.y);
        grid[door.x][door.y] = 0;
        chestNExit.add(door);

        display();
        
        System.out.println("monsters at: "+monsterLocations);
        System.out.println("keys, chest and door at: "+chestNExit);
        deadends.clear();

    }
    public void transformV2() {
        // SPRITE TRANSFORM
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (row == rows-1 || (grid[col][row] == 1 && grid[col][row+1] == 0))
                    grid[col][row] = 2;
            }
        }
    }
    public void setExit() {
        grid[door.x][door.y] = 0;
    }
    public void display() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                
                switch (grid[col][row]) {
                    case 0:
                        System.out.print(" ");
                        break;
                    case 1:
                        System.out.print("#");
                        break;
                    case 2:
                        System.out.print("+");
                        break;
                    case 3:
                        System.out.print("X");
                        break;
                    case 4:
                        System.out.print("M");
                        break;
                }
                System.out.print(col < cols-1 ? " " : "\n");
            }
        }
    }
    public static class Vector2D {

        public int x, y;
    
        public Vector2D (int x, int y) {
            this.x = x;
            this.y = y;
        }
    
        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) return false;
            Vector2D other = (Vector2D)obj;
            return this.x == other.x && this.y == other.y;
        }
    
        @Override
        public int hashCode() {
            return 31*x + y;
        }
    
        @Override
        public String toString() {
            return "("+this.x+", "+this.y+")";
        }
    }
}
