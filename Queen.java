import java.io.Console;

class Queen {
    
    int eat ;
    
    void setEat(int eat) { this.eat = eat; }
    int getEat() { return eat ; }

    public Worker prodWorker() {
        if (energy < WORKER_COST) {
            System.out.println("Queen: not enough energy to spawn a worker.");
            return null;
        }

        energy -= WORKER_COST;
        Worker worker = new Worker(nextAntId++);
        workers.add(worker);
        System.out.println("Queen spawned " + worker + ". Energy left: " + energy);
        return worker;
    }

    public Scout prodScout() {
        if (energy < SCOUT_COST) {
            System.out.println("Queen: not enough energy to spawn a scout.");
            return null;
        }

        energy -= SCOUT_COST;
        Scout scout = new Scout(nextAntId++);
        scouts.add(scout);
        System.out.println("Queen spawned " + scout + ". Energy left: " + energy);
        return scout;
    }

}


