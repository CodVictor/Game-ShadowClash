package Factories;

import java.util.ArrayList;
import java.util.Scanner;
import Entities.Weapon;
import Entities.Armor;
import Entities.Hunter;
import Entities.Weakness;
import Entities.Strength;
import Entities.Talent;

public class HunterFactory {
    public HunterFactory() {}

    public void initializeName(Hunter hunter) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        hunter.setName(name);
    }

    public void initializeAbilityName(Talent talent) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        talent.setName(name);
    }

    public boolean initializeAbilityAttack(Talent talent) {
        Scanner sc = new Scanner(System.in);
        int attack = sc.nextInt();
        if (attack >= 1 && attack <= 3) {
            talent.setAttack(attack);
            return true;
        } else {
            return false;
        }
    }

    public boolean initializeAbilityDefense(Talent talent) {
        Scanner sc = new Scanner(System.in);
        int defense = sc.nextInt();
        if (defense >= 1 && defense <= 3) {
            talent.setDefense(defense);
            return true;
        } else {
            return false;
        }
    }

    public void initializeAbilityAge(Talent talent) {
        Scanner sc = new Scanner(System.in);
        int age = sc.nextInt();
        talent.setAge(age);
    }

    public void setAbility(Hunter hunter, Talent talent) {
        hunter.setAbility(talent);
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

    public void setWeapons(Hunter hunter, ArrayList<Weapon> weapons) {
        hunter.setWeapons(weapons);
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

    public void setActiveWeapons(Hunter hunter, ArrayList<Weapon> activeWeapons) {
        hunter.setActiveWeapons(activeWeapons);
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

    public void setArmors(Hunter hunter, ArrayList<Armor> armors) {
        hunter.setArmors(armors);
    }

    public boolean addActiveArmor(Hunter hunter, Armor armor, ArrayList<Armor> armors) {
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        if (option >= 1 && option <= armors.size()) {
            armor = armors.get(option - 1);
            hunter.setActiveArmor(armor);
            return true;
        } else {
            return false;
        }
    }

    public boolean initializeGold(Hunter hunter) {
        Scanner sc = new Scanner(System.in);
        int gold = sc.nextInt();
        if (gold < 0) {
            return false;
        } else {
            hunter.setGold(gold);
            return true;
        }
    }

    public boolean initializeHP(Hunter hunter) {
        Scanner sc = new Scanner(System.in);
        int hp = sc.nextInt();
        if (hp >= 0 && hp <= 5) {
            hunter.setHealth(hp);
            return true;
        } else {
            return false;
        }
    }

    public boolean initializePower(Hunter hunter) {
        Scanner sc = new Scanner(System.in);
        int power = sc.nextInt();
        if (power >= 1 && power <= 5) {
            hunter.setPower(power);
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

    public void setWeaknesses(Hunter hunter, ArrayList<Weakness> weaknesses) {
        hunter.setWeaknesses(weaknesses);
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

    public void setStrengths(Hunter hunter, ArrayList<Strength> strengths) {
        hunter.setStrengths(strengths);
    }

    public void initializeHunterWillpower(Hunter hunter){
        hunter.setWillpower(3);
    }
}