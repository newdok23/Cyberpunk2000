public class Main {
    
    class Ant {

        float health;

        void setHealth(float health) { this.health = health; }
        float getHealth() { return health ;}
        }

    class Scout extends Ant { 
        int stamina;
    
        void setStamina(int stamina) { this.stamina = stamina; }
        int getStamina() { return stamina; }
    
    } 

    class Worker extends Ant { 
        int stamina;

    
        void setStamina(int stamina) { this.stamina = stamina; }
        int getStamina() { return stamina; }
    
    } 

     class Warrior extends Ant { 
        int stamina;
    
        void setStamina(int stamina) { this.stamina = stamina; }
        int getStamina() { return stamina; }
    
    } 


    class Queen extends Ant { 
        int stamina;
    
        void setStamina(int stamina) { this.stamina = stamina; }
        int getStamina() { return stamina; }
    
    } 

    

}
