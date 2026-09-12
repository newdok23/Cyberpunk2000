import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;


public class Main {
//program settings
static final int WIDTH = 20;
static final int HEIGHT = 20;
static final int CELL_SIZE = 30;
static final int TOTAL_FOOD = 10;

//respawn rate
static int respawnTimer = 0 ;
static final int RESPAWN_RATE = 50;// so every 50 ticks
static int scoutMoveTimer = 0 ;
static final int SCOUT_MOVE_RATE = 2; // move every 3 ticks
static int ticks_since_ant_came_back = 0; // timer for queen to assume ant is dead
//queen
static final int workerSPAWN_RATE = 45; // slightly slower than scouts
static final int MAX_WORKERS = 5;



static Queen queen;
static List<Scout> scouts = new ArrayList<>();
static List<Worker> workers = new ArrayList<>();



enum CellType {
    EMPTY , FOOD , WALL , COLONY , QUEEN
}

static void update() {
    respawnTimer++;
    if (respawnTimer >= RESPAWN_RATE){
        respawnFood();
        respawnTimer = 0 ;
    }

    // add to the panic timer
    ticks_since_ant_came_back++;
    if (ticks_since_ant_came_back > 300) {
        // queen makes a scout because she thinks they all died
        Scout emergency_scout = queen.prodScout();
        if (emergency_scout != null) {
            scouts.add(emergency_scout);
            ticks_since_ant_came_back = 0; // reset
        }
    }

    // MOVE COUT
    scoutMoveTimer++;
    if (scoutMoveTimer >= SCOUT_MOVE_RATE) {
        for (Scout scout : scouts) {
            if (scout.isPathfinding()) {
                scout.returnToColony(WIDTH, HEIGHT);
                
                // check if scout got to the center
                if (scout.getPosition().x == WIDTH / 2) {
                    if (scout.getPosition().y == HEIGHT / 2) { // nested if 
                        ticks_since_ant_came_back = 0; // reset
                        // make a worker to go get the food
                        Worker new_worker_ant = queen.prodWorker();
                        if (new_worker_ant != null) {
                            workers.add(new_worker_ant);
                        }
                    }
                }
                
            }else {
                scout.wander(WIDTH, HEIGHT);
            }
        }
        scoutMoveTimer = 0 ;
    }

    // move workers
    for (Worker w : workers) {
        if (w.isCarryingFood()) {
            // clunky math to walk back to center
            int dx = Integer.compare(WIDTH / 2, w.getPosition().x);
            int dy = Integer.compare(HEIGHT / 2, w.getPosition().y);
            w.getPosition().translate(dx, dy);
            
            if (w.getPosition().x == WIDTH/2 && w.getPosition().y == HEIGHT/2) {
                ticks_since_ant_came_back = 0; // worker came back!
                try {
                    w.dropFood();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            w.followTrail();
            
            // grab food if standing on it
            if (grid[w.getPosition().x][w.getPosition().y] == CellType.FOOD) {
                w.pickUpFood(1);
                grid[w.getPosition().x][w.getPosition().y] = CellType.EMPTY;
            }
        }
    }
    
    // for each cell in the pheromone grid, decay the pheromone strength and clear it if it falls below a threshold
    for (int x = 0; x < WIDTH; x++) {
        for (int y = 0; y < HEIGHT; y++) {
            if (pheromoneGrid[x][y] != null) {
                pheromoneGrid[x][y].decay();
                if (pheromoneGrid[x][y].getStrength() < 0.05f) {
                    pheromoneGrid[x][y] = null; // Clear when faded
                }

            }
        }
    }
}


static int countFood() {
    int count = 0;
    for (int x = 0; x < WIDTH ; x++) {
        for (int y = 0 ; y < HEIGHT; y++) {
            if (grid[x][y] == CellType.FOOD) count++ ;
        }
    }
    return count;
}


static Random random = new Random() ;

static void respawnFood() { 
    if (countFood() >= TOTAL_FOOD) return; // stop food from spawning if it exceeds counter

int foodPlaced = 0;
while (foodPlaced < 3){
    int x = random.nextInt(WIDTH);
    int y = random.nextInt(HEIGHT);
    if (grid[x][y] == CellType.EMPTY) {
        grid[x][y] = CellType.FOOD;
        foodPlaced++;
    
        }

    }
}



static CellType[][] grid = new CellType[WIDTH][HEIGHT];
@SuppressWarnings("unchecked")
static Pheromone<Float>[][] pheromoneGrid = new Pheromone[WIDTH][HEIGHT];

public static void main(String[] args) {
    
    initGrid();
    JFrame frame = new JFrame("Ant Colony");

    JPanel panel = new JPanel() {
        @Override 
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGrid(g);
        }
    };

    




    panel.setPreferredSize(new Dimension(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE));
    frame.add(panel);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    Timer timer = new Timer(100, e -> {
        update();
        panel.repaint();
});
        timer.start();
}



static void drawGrid(Graphics g) {
    for (int x = 0; x < WIDTH; x++){
        for (int y = 0 ; y < HEIGHT; y++){
            switch (grid[x][y]){
                case EMPTY:     g.setColor(Color.BLACK); break;
                case FOOD:      g.setColor(Color.YELLOW); break;
                case WALL:      g.setColor(Color.RED);  break;
                //case COLONY:    g.setColor(Color.MAGENTA);  break;
                case QUEEN:     g.setColor(Color.PINK); break;
                default:        g.setColor(Color.BLACK);    break;
                
            }
            
            g.fillRect(x * CELL_SIZE,y* CELL_SIZE , CELL_SIZE, CELL_SIZE);
            
            // check if pheromon is there
            if (pheromoneGrid[x][y] != null) {
                if (grid[x][y] == CellType.EMPTY) { // only draw on empty spaces
                    float s = pheromoneGrid[x][y].getStrength();
                    
                    // set transparency
                    int trans = (int)(s * 180);
                    if (trans > 255) {
                        trans = 255;
                    }
                    if (trans < 0) {
                        trans = 0;
                    }
                    
                    Color pheroColor = new Color(0, 255, 255, trans);
                    g.setColor(pheroColor);
                    g.fillRect(x * CELL_SIZE, y *CELL_SIZE, CELL_SIZE , CELL_SIZE);
                }
            }

            g.setColor(Color.DARK_GRAY);
            g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    g.setColor(Color.GREEN);
    for (Scout scout : scouts) {
        Point p = scout.getPosition();
        g.fillOval(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE , CELL_SIZE);
    }

    g.setColor(Color.BLUE);
    for (Scout scout : scouts) {
        Point p = scout.getPosition();
        g.fillOval(p.x * CELL_SIZE + 5, p.y * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }

    // draw workers
    g.setColor(Color.RED);
    for (Worker worker : workers) {
        Point p = worker.getPosition();
        // make them slightly smaller than the cell size so they fit
        g.fillOval(p.x * CELL_SIZE + 5, p.y * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }
}



static void initGrid () {

//empty cell
for (int x = 0; x < WIDTH; x++) {
    for (int y = 0; y < HEIGHT; y++) {
        grid[x][y] = CellType.EMPTY;
    }
}
// Spawn Queen
queen = new Queen(100,100);
grid[WIDTH / 2][HEIGHT / 2] = CellType.QUEEN;
// Spawn Scouts
scouts.add(new Scout(100, 100));
scouts.add(new Scout(100, 100));
scouts.add(new Scout(100, 100));

// scatter food 
Random random = new Random();
int foodPlaced = 0;
while (foodPlaced < 10){
    int x = random.nextInt(WIDTH);
    int y = random.nextInt(HEIGHT);
    if (grid[x][y] == CellType.EMPTY) {
        grid[x][y] = CellType.FOOD;
        foodPlaced++;
    }
}
}

}