import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Worker extends Ant {
    boolean followingTrail;
    boolean carryingFood;
    int foodCarried;
    boolean pathfinding;

    List<Pheromone<Float>> trail = new ArrayList<>();

    public Worker(int health, int stamina) {
        super(health, stamina);
        this.followingTrail = false;
        this.carryingFood = false;
        this.foodCarried = 0;
        this.position = new Point(10, 10);
        this.pathfinding = false;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public void setFollowingTrail(boolean followingTrail) {
        this.followingTrail = followingTrail;
    }

    public boolean isFollowingTrail() {
        return followingTrail;
    }

    public void setCarryingFood(boolean carryingFood) {
        this.carryingFood = carryingFood;
    }

    public boolean isCarryingFood() {
        return carryingFood;
    }

    public int getFoodCarried() {
        return foodCarried;
    }

    public void pickUpFood(int amount) {
        this.foodCarried = amount;
        this.carryingFood = true;
    }

    public boolean isPathfinding() {
        return pathfinding;
    }

    public void followTrail(int gridWidth, int gridHeight) {
        Point target = findStrongestPheromone(gridWidth, gridHeight);

        if (target != null) {
            int dx = Integer.compare(target.x, position.x);
            int dy = Integer.compare(target.y, position.y);

            int newX = position.x + dx;
            int newY = position.y + dy;

            if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
                position.setLocation(newX, newY);
            }
        }

        if (Main.grid[position.x][position.y] == Main.CellType.FOOD) {
            carryingFood = true;
            pathfinding = true;
        }
    }

    private Point findStrongestPheromone(int gridWidth, int gridHeight) {
        Pheromone<Float> strongest = null;
        int strongestX = position.x;
        int strongestY = position.y;

        int minX = Math.max(0, position.x - 3);
        int maxX = Math.min(gridWidth - 1, position.x + 3);
        int minY = Math.max(0, position.y - 3);
        int maxY = Math.min(gridHeight - 1, position.y + 3);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Pheromone<Float> candidate = Main.pheromoneGrid[x][y];
                if (candidate != null && (strongest == null || candidate.getStrength() > strongest.getStrength())) {
                    strongest = candidate;
                    strongestX = x;
                    strongestY = y;
                }
            }
        }

        if (strongest == null) {
            return null;
        }

        return new Point(strongestX, strongestY);
    }

    public void returnToColony(int gridWidth, int gridHeight) {
        int colonyX = Main.WIDTH / 2;
        int colonyY = Main.HEIGHT / 2;

        int dx = Integer.compare(colonyX, position.x);
        int dy = Integer.compare(colonyY, position.y);

        int newX = position.x + dx;
        int newY = position.y + dy;

        position.setLocation(newX, newY);

        if (newX == colonyX && newY == colonyY) {
            pathfinding = false;
        }
    }
}
