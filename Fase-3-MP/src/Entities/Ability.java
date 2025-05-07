package Entities;

public class Ability {

    /**A continuación se definen Atributos**/
    private String name;
    private int attack;
    private int defense;

    /**A continuación se define el constructor**/
    public Ability() {}

    /**A continuación se definen Getters y Setters**/
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAttack() {
        return this.attack;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return this.defense;
    }
    public void setDefense(int defense) {
        this.defense = defense;
    }
}
