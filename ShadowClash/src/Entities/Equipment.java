package Entities;

public class Equipment {

    /**A continuación se definen los atributos**/
    private String name;
    private int attackModifier;
    private int defenseModifier;

    /**A continuación se define el constructor**/
    public Equipment() {}

    /**A continuación se definen Getters y Setters**/
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAttackModifier() {
        return this.attackModifier;
    }
    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public int getDefenseModifier() {
        return this.defenseModifier;
    }
    public void setDefenseModifier(int defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    /** Métodos adicionales **/
    public int getAttack() {
        return this.attackModifier;
    }

    public int getDefense() {
        return this.defenseModifier;
    }
}

