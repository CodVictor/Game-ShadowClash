package Factories;

import java.util.ArrayList;
import java.util.Scanner;
import Entities.Weapon;
import Entities.Armor;
import Entities.Weakness;
import Entities.Don;
import Entities.Strength;
import Entities.Werewolf;

public class WerewolfFactory {
    public WerewolfFactory() {}

    public void initializeName(Werewolf werewolf) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        werewolf.setName(name);
    }

    public void initializeAbilityName(Don don) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        don.setName(name);
    }

    public boolean initializeRageAbility(Don don) {
        Scanner sc = new Scanner(System.in);
        int rage = sc.nextInt();
        if (rage < 0) {
            return false;
        } else {
            don.setMinimumValue(rage);
            return true;
        }
    }

    public void setAbility(Werewolf werewolf, Don don) {
        werewolf.setAbility(don);
    }

    public int askNumber() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    public void initializeWeaponName(Weapon weapon) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        weapon.setName(name);
    }

    public boolean initializeWeaponAttack(Weapon weapon) {
        Scanner sc = new Scanner(System.in);
        int attack = sc.nextInt();
        if (attack >= 1 && attack <= 3) {
            weapon.setAttackModifier(attack);
            return true;
        } else {
            return false;
        }
    }

    public boolean initializeWeaponDefense(Weapon weapon) {
        Scanner sc = new Scanner(System.in);
        int defense = sc.nextInt();
        if (defense >= 0 && defense <= 3) {
            weapon.setDefenseModifier(defense);
            return true;
        } else {
            return false;
        }
    }

    public boolean initializeWeaponSingleHand(Weapon weapon) {
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        if (option == 1) {
            weapon.setSingleHand(true);
            return true;
        } else if (option == 2) {
            weapon.setSingleHand(false);
            return true;
        } else {
            return false;
        }
    }

    public void addWeapon(ArrayList<Weapon> weapons, Weapon weapon) {
        weapons.add(weapon);
    }

    public void setWeapons(Werewolf werewolf, ArrayList<Weapon> weapons) {
        werewolf.setWeapons(weapons);
    }

    public boolean[] addActiveWeapon(ArrayList<Weapon> weapons, ArrayList<Weapon> activeWeapons) {
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        if (option >= 1 && option <= weapons.size()) {
            activeWeapons.add(weapons.get(option - 1));
            return new boolean[]{true, weapons.get(option - 1).isSingleHand()};
        } else {
            return new boolean[]{false, false};
        }
    }

    public boolean addActiveWeapon2(ArrayList<Weapon> weapons, ArrayList<Weapon> activeWeapons) {
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        if (option >= 0 && option <= weapons.size()) {
            if (option == 0) {
                return true;
            } else if (weapons.get(option - 1) != activeWeapons.get(0) && weapons.get(option - 1).isSingleHand()) {
                activeWeapons.add(weapons.get(option - 1));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setActiveWeapons(Werewolf werewolf, ArrayList<Weapon> activeWeapons) {
        werewolf.setActiveWeapons(activeWeapons);
    }

    public void initializeArmorName(Armor armor) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        armor.setName(name);
    }

    public boolean initializeArmorDefense(Armor armor) {
        Scanner sc = new Scanner(System.in);
        int defense = sc.nextInt();
        if (defense >= 1 && defense <= 3) {
            armor.setDefenseModifier(defense);
            return true;
        } else {
            return false;
        }
    }

    public boolean initializeArmorAttack(Armor armor) {
        Scanner sc = new Scanner(System.in);
        int attack = sc.nextInt();
        if (attack >= 0 && attack <= 3) {
            armor.setAttackModifier(attack);
            return true;
        } else {
            return false;
        }
    }

    public void addArmor(Armor armor, ArrayList<Armor> armors) {
        armors.add(armor);
    }

    public void setArmors(Werewolf werewolf, ArrayList<Armor> armors) {
        werewolf.setArmors(armors);
    }

    public boolean addActiveArmor(Werewolf werewolf, Armor armor, ArrayList<Armor> armors) {
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        if (option >= 1 && option <= armors.size()) {
            armor = armors.get(option - 1);
            werewolf.setActiveArmor(armor);
            return true;
        } else {
            return false;
        }
    }

    public boolean initializeGold(Werewolf werewolf) {
        Scanner sc = new Scanner(System.in);
        int gold = sc.nextInt();
        if (gold < 0) {
            return false;
        } else {
            werewolf.setGold(gold);
            return true;
        }
    }

    public boolean initializeHP(Werewolf werewolf) {
        Scanner sc = new Scanner(System.in);
        int hp = sc.nextInt();
        if (hp >= 0 && hp <= 5) {
            werewolf.setHealth(hp);
            return true;
        } else {
            return false;
        }
    }

    public boolean initializePower(Werewolf werewolf) {
        Scanner sc = new Scanner(System.in);
        int power = sc.nextInt();
        if (power >= 1 && power <= 5) {
            werewolf.setPower(power);
            return true;
        } else {
            return false;
        }
    }

    public void initializeWeaknessName(Weakness weakness) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        weakness.setName(name);
    }

    public void initializeWeaknessValue(Weakness weakness) {
        Scanner sc = new Scanner(System.in);
        int value = sc.nextInt();
        weakness.setValue(value);
    }

    public void addWeakness(ArrayList<Weakness> weaknesses, Weakness weakness) {
        weaknesses.add(weakness);
    }

    public void setWeaknesses(Werewolf werewolf, ArrayList<Weakness> weaknesses) {
        werewolf.setWeaknesses(weaknesses);
    }

    public void initializeStrengthName(Strength strength) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        strength.setName(name);
    }

    public void initializeStrengthValue(Strength strength) {
        Scanner sc = new Scanner(System.in);
        int value = sc.nextInt();
        strength.setValue(value);
    }

    public void addStrength(ArrayList<Strength> strengths, Strength strength) {
        strengths.add(strength);
    }

    public void setStrengths(Werewolf werewolf, ArrayList<Strength> strengths) {
        werewolf.setStrengths(strengths);
    }
}