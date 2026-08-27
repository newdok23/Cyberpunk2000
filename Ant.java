public class Ant {

         int health;
         int stamina;
        boolean alive ;
        // static final cost ;
        

        interface void takedamage (ant a) { 
                a.damage () ;
        }


        interface AntBehaviour {

                void setStamina(int stamina);
                int getStamina();
                void setHealth(int health);
                int getHealth();
                void die(boolean alive);
                void move(float coordinates);
                float getCoordinates();

        }
}

