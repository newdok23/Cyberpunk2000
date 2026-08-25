public class Ant {

         int health;
         int stamina;
        boolean alive ;
        // static final cost ;
        

        interface void takedamage (ant a) { 
                a.damage () ;
        }

        void setStamina( int stamina) {this.stamina = stamina ;}
        int getStamina() { return stamina ;}

        void setHealth(int health) { this.health = health; }
        int getHealth() { return health ;}
    
}

