package Entities;

public class Werewolf extends Character {
    /**A continuación se definen los atributos**/
    private int rage;

    /**A continuación se define el constructor**/
    public Werewolf() {}

    /**A continuación se definen los Getters y Setters**/

    public int getRage() {return this.rage;} //valor de Rabia
    public void setRage(int rage) {
        this.rage = rage;
    }
}
