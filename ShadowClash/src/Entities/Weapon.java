package Entities;

public class Weapon extends Equipment {

    /**A continuación se definen los Atributos**/
    private boolean singleHanded;

    /**A continuación se define el constructor**/
    public Weapon(){}

    /**A continuación se definen los Getters, Setters y operaciones**/
    public boolean isSingleHand() {return this.singleHanded;}
    public void setSingleHand(boolean singleHanded) {this.singleHanded = singleHanded;}
}// FIN