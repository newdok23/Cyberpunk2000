import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class Worker extends Ant{
    /*1. Worker needs to detect pheromones nearby
    2. Follow the strongest pheromone trail
    3. Pick up food when it reaches the source
    4. Carry food back to colony  */
    boolean followingTrail;
    boolean carryingFood;
    int foodCarried;
    
    //pheremone trail lists
    List<Pheromone<Float>> trail = new ArrayList<>();
    

    public Worker (int health, int stamina) {
       super(health,stamina);
       this.position = new Point(10, 10); // Spawns the worker at the center colony
       this.followingTrail = false;
       this.carryingFood = false;
       this.foodCarried = 0;
    }

    public void setFollowingTrail(boolean followingTrail) {
        this.followingTrail = followingTrail;
    }
    public boolean isFollowingTrail() { return followingTrail;}
    public void setCarryingFood(boolean carryingFood) {
        this.carryingFood = carryingFood;
    }
    public boolean isCarryingFood () { return carryingFood;} 
    public int getFoodCarried() { return foodCarried; }
    public void pickUpFood(int amount) {
        this.foodCarried = amount;
        this.carryingFood = true;
    }
public void followTrail() { 
        int cx = position.x;
        int cy = position.y;

        int nextX = cx;
        int nextY = cy;
        
        float best = 999.0f; // MUST be outside the loop!

        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        
        for (int i = 0; i < 4; i++) {
            try {
                int checkX = cx + dirs[i][0];
                int checkY = cy + dirs[i][1];
                
                if (Main.pheromoneGrid[checkX][checkY] != null) {
                    //generic type requirement
                    float str = ((Number) Main.pheromoneGrid[checkX][checkY].getStrength()).floatValue();
                    if (str < best && str > 0.01f) {
                        best = str;
                        nextX = checkX;
                        nextY = checkY;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // try/catch requirement
                System.out.println("worker at edge of map");
            }
        }
        
        // dirty bandaid: if ant is stuck and didn't move, just force it to step right
        if (nextX == cx && nextY == cy) {
            nextX = cx + 1; 
        }
        
        this.position.setLocation(nextX, nextY);
    }



public void dropFood() throws Exception {
        if (this.carryingFood == false) {
            throw new Exception("ant doesnt have food to drop");
        } else {
            this.carryingFood = false;
            this.foodCarried = 0;
            
            // Queen +50 stamina
            Main.queen.setStamina(Main.queen.getStamina() + 50); 
        }
    }
    
}


 