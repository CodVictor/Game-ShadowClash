package Entities;

public class Discipline extends Ability {
    /**A continuación se definen atributos**/
    private int cost;

    /**A continuación se define el constructor**/
    public Discipline() {}

    /**A continuación se definen Getters y Setters**/
    public int getCost() {
        return this.cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
}

