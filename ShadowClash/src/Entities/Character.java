package Entities;

import java.util.ArrayList;

public class Character {
    /** Atributos **/
    private String name;
    private Ability ability;
    private ArrayList<Weapon> weapons;
    private ArrayList<Weapon> activeWeapons;
    private ArrayList<Armor> armors;
    private Armor activeArmor;
    private ArrayList<Weakness> weaknesses;
    private ArrayList<Strength> strengths;
    private int gold;
    private int health;
    private int power;
    private String type;
    private ArrayList<MinionsComposit> minionsComposit;

    /** Setters y Getters **/

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Ability getAbility() {
        return this.ability;
    }
    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public ArrayList<Weapon> getWeapons() {
        return this.weapons;
    }
    public void setWeapons(ArrayList<Weapon> weapons) {
        this.weapons = weapons;
    }

    public ArrayList<Weapon> getActiveWeapons() {
        return this.activeWeapons;
    }
    public void setActiveWeapons(ArrayList<Weapon> activeWeapons) {
        this.activeWeapons = activeWeapons;
    }

    public ArrayList<Armor> getArmors() {
        return this.armors;
    }
    public void setArmors(ArrayList<Armor> armors) {
        this.armors = armors;
    }

    public Armor getActiveArmor() {
        return this.activeArmor;
    }
    public void setActiveArmor(Armor activeArmor) {
        this.activeArmor = activeArmor;
    }

    public ArrayList<Weakness> getWeaknesses() {
        return this.weaknesses;
    }
    public void setWeaknesses(ArrayList<Weakness> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public ArrayList<Strength> getStrengths() {
        return this.strengths;
    }
    public void setStrengths(ArrayList<Strength> strengths) {
        this.strengths = strengths;
    }

    public int getGold() {
        return this.gold;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }

    public ArrayList<MinionsComposit> getMinions() {
        return minionsComposit;
    }
    public void setMinions(ArrayList<MinionsComposit> minionsComposit) {
        this.minionsComposit = minionsComposit;
    }

    public int getHealth() {
        return this.health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public int getPower() {
        return this.power;
    }
    public void setPower(int power) {
        this.power = power;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /** Métodos adicionales necesarios para el combate **/

    public int getAttack() {
        int totalAttack = power;
        if (activeWeapons != null) {
            for (Weapon weapon : activeWeapons) {
                totalAttack += weapon.getAttack();
            }
        }
        if (activeArmor != null) {
            totalAttack += activeArmor.getAttack();
        }
        return totalAttack;
    }

    public int getDefense() {
        int totalDefense = 0;
        if (activeWeapons != null) {
            for (Weapon weapon : activeWeapons) {
                totalDefense += weapon.getDefense();
            }
        }
        if (activeArmor != null) {
            totalDefense += activeArmor.getDefense();
        }
        return totalDefense;
    }
}

