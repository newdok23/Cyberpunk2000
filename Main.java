public class Main {


    interface Explore {

        findResource() ;
        leavePheromon() ;

    }

    interface Pathfinding {

        void findpheromon () ;
    }

    interface Carrier {
        
        void carry () ;
    }

    interface ProduceAnt {

        prodScout () ;
        prodWorker () ;
    }

    
    class Ant {

        int health;
        int stamina;
        boolean alive ;
        // static final cost ;
        

        void setStamina( int stamina) {this.stamina = stamina ;}
        int getStamina() { return stamina ;}

        void setHealth(int health) { this.health = health; }
        int getHealth() { return health ;}
    }

    class Scout extends Ant { 
        boolean pathfinding;
    
        void pathfinding(boolean pathfinding) { this.pathfinding = pathfinding; }
        boolean pathfinding() { return pathfinding; }
    
    } 

    class Worker extends Ant { 
        
    
    } 


    class Queen extends Ant { 

        int eat ;
    
        void setEat(int eat) { this.eat = eat; }
        int getEat() { return eat ; }
    
    } 

    class resourcePoint {};

    

}
