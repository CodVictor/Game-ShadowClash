package Entities;

import System.Terminal;

import java.util.ArrayList;

public class Round {
    /**A continuación se definen los atributos**/

    private int hpChallengerEnd;
    private int hpRivalEnd;
    /**A continuación se definen las operaciones**/

    public int getHpChallengerEnd() {
        return hpChallengerEnd;
    }

    public void setHpChallengerEnd(int hpChallengerEnd) {
        this.hpChallengerEnd = hpChallengerEnd;
    }

    public int getHpRivalEnd() {
        return hpRivalEnd;
    }

    public void setHpRivalEnd(int hpRivalEnd) {
        this.hpRivalEnd = hpRivalEnd;
    }

    public boolean startRound(int hpChallenger, int hpRival, Client challenger, Client rival, ArrayList<Modifier> modifiers) {
        Terminal terminal = new Terminal();

        // Inicialización de armaduras
        int challengerArmorAttack = getArmourAttack(challenger, 0);
        int rivalArmorAttack = getArmourAttack(rival, 0);

        int challengerAttackPotential = challenger.getCharacter().getPower() + challenger.getCharacter().getAbility().getAttack() + challengerArmorAttack;
        int rivalAttackPotential = rival.getCharacter().getPower() + rival.getCharacter().getAbility().getAttack() + rivalArmorAttack;

        challengerAttackPotential = getAttackPotential(challenger, challengerAttackPotential);
        rivalAttackPotential = getAttackPotential(rival, rivalAttackPotential);

        // Inicialización de defensas
        int challengerArmorDefence = getArmourDefence(challenger, 0);
        int rivalArmorDefence = getArmourDefence(rival, 0);

        int challengerDefencePotential = challenger.getCharacter().getPower() + challenger.getCharacter().getAbility().getDefense() + challengerArmorDefence;
        int rivalDefencePotential = rival.getCharacter().getPower() + rival.getCharacter().getAbility().getDefense() + rivalArmorDefence;

        challengerDefencePotential = getDefencePotential(challenger, challengerDefencePotential);
        rivalDefencePotential = getDefencePotential(rival, rivalDefencePotential);

        // Aplicar modificadores
        int challengerModifiersValue = getChallengerMofifiersValues(challenger, modifiers, 0);
        int rivalModifiersValue = getChallengerMofifiersValues(rival, modifiers, 0);

        challengerDefencePotential += challengerModifiersValue;
        rivalDefencePotential += rivalModifiersValue;

        terminal.startRound(hpChallenger, hpRival, challenger.getNick(), rival.getNick(), challengerAttackPotential, challengerDefencePotential, rivalAttackPotential, rivalDefencePotential);

        // Realizar disparos (ataques y defensas)
        int challengerAttack = doRoundShots(challengerAttackPotential);
        int rivalAttack = doRoundShots(rivalAttackPotential);

        int challengerDefence = doRoundShots(challengerDefencePotential);
        int rivalDefence = doRoundShots(rivalDefencePotential);

        // Calcular nuevos HP
        hpChallenger = getHp(challengerAttack, rivalDefence, hpRival, challenger, rival);
        hpRival = getHp(rivalAttack, challengerDefence, hpChallenger, rival, challenger);

        setHpChallengerEnd(hpChallenger);
        setHpRivalEnd(hpRival);

        return (getHpChallengerEnd() == 0 || getHpRivalEnd() == 0);
    }

    private int getHp(int ataqueDesafiante, int defensaContrincante, int hpContrincante, Client challenger, Client rival) {
        int damage = Math.max(ataqueDesafiante - defensaContrincante, 0);
        hpContrincante -= damage;
        modifyValues(challenger, rival);
        return hpContrincante;
    }

    private int doRoundShots(int potential) {
        int result = 0;
        for (int shot = 1; shot <= potential; shot++) {
            double numRandom = Math.random() * 6 + 1;
            if (numRandom >= 5) {
                result += 1;
            }
        }
        return result;
    }

    private int getArmourDefence(Client client, int armourDefence) {
        for (int numWeapon = 0; numWeapon < client.getCharacter().getActiveWeapons().size(); numWeapon++) {
            armourDefence += client.getCharacter().getActiveWeapons().get(0).getDefenseModifier();
        }
        armourDefence += client.getCharacter().getActiveWeapons().get(0).getDefenseModifier();
        return armourDefence;
    }


    private int getArmourAttack(Client client, int armourAttack) {
        for (int numWeapon = 0; numWeapon < client.getCharacter().getActiveWeapons().size(); numWeapon++) {
            armourAttack += client.getCharacter().getActiveWeapons().get(numWeapon).getAttackModifier();
        }
        return armourAttack;
    }


    private void modifyValues(Client challenger, Client rival) {
        if (challenger.getCharacter().getType().equals("VAMPIRO")) {
            Vampire vampire = (Vampire) challenger.getCharacter();
            vampire.setBlood(vampire.getBlood() + 4);
            if (vampire.getBlood() > 10) {
                vampire.setBlood(10);
            }
        }
        if (rival.getCharacter().getType().equals("LICANTROPO")) {
            Werewolf werewolf = (Werewolf) rival.getCharacter();
            if (werewolf.getRage() != 3) {
                werewolf.setRage(werewolf.getRage() + 1);
            }
        } else if (rival.getCharacter().getType().equals("CAZADOR")) {
            Hunter hunter = (Hunter) rival.getCharacter();
            if (hunter.getWillpower() != 0) { //willpower = voluntad
                hunter.setWillpower(hunter.getWillpower() - 1);
            }
        }
    }

    private int getChallengerMofifiersValues(Client client, ArrayList<Modifier> modifiers, int valueOfMods) {
        for (int weaknessNum = 0; weaknessNum < client.getCharacter().getWeaknesses().size(); weaknessNum++) {
            for (Modifier mod : modifiers) {
                if (client.getCharacter().getWeaknesses().get(weaknessNum).getName().equals(mod.getName())) {
                    valueOfMods -= client.getCharacter().getWeaknesses().get(weaknessNum).getValue();
                }
            }
        }
        for (int strengthNum = 0; strengthNum < client.getCharacter().getStrengths().size(); strengthNum++) {
            for (Modifier mod : modifiers) {
                if (client.getCharacter().getStrengths().get(strengthNum).getName().equals(mod.getName())) {
                    valueOfMods += client.getCharacter().getStrengths().get(strengthNum).getValue();
                }
            }
        }
        return valueOfMods;
    }

    private int getDefencePotential(Client client, int defencePotential) {
        if (client.getCharacter().getType().equals("VAMPIRO")) {
            Vampire vampire = (Vampire) client.getCharacter();
            Discipline discipline = (Discipline) vampire.getAbility();
            Terminal terminal = new Terminal();

            if (vampire.getBlood() >= 5 && vampire.getBlood() >= discipline.getCost()) {
                terminal.defenceAbility(vampire.getName(), discipline.getName());
                defencePotential += 2;
                vampire.setBlood(vampire.getBlood() - discipline.getCost());
            }
        } else if (client.getCharacter().getType().equals("LICANTROPO")) {
            Werewolf werewolf = (Werewolf) client.getCharacter();
            Don don = (Don) werewolf.getAbility();
            Terminal terminal = new Terminal();
            if (don.getMinimumValue() <= werewolf.getRage()) {
                terminal.defenceAbility(werewolf.getName(), don.getName());
                defencePotential += werewolf.getRage();
            }
        } else {
            Terminal terminal = new Terminal();
            Hunter hunter = (Hunter) client.getCharacter();
            Talent talent = (Talent) hunter.getAbility();
            terminal.defenceAbility(hunter.getName(), talent.getName());
            defencePotential += talent.getDefense();
        }
        return defencePotential;
    }

    private int getAttackPotential(Client client, int attackPotential) {
        if (client.getCharacter().getType().equals("VAMPIRO")) {

            Vampire vampire = (Vampire) client.getCharacter();
            Discipline discipline = (Discipline) vampire.getAbility();
            Terminal terminal = new Terminal();

            if (vampire.getBlood() >= 5 && vampire.getBlood() >= discipline.getCost()) {
                terminal.attackAbility(vampire.getName(), discipline.getName());
                attackPotential += 2;
                vampire.setBlood(vampire.getBlood() - discipline.getCost());
            }
        } else if (client.getCharacter().getType().equals("LICANTROPO")) {
            Werewolf werewolf = (Werewolf) client.getCharacter();
            Don don = (Don) werewolf.getAbility();
            Terminal terminal = new Terminal();
            if (don.getMinimumValue() <= werewolf.getRage()) {
                terminal.attackAbility(werewolf.getName(), don.getName());
                attackPotential += werewolf.getRage();
            }
        } else {
            Hunter hunter = (Hunter) client.getCharacter();
            Talent talent = (Talent) hunter.getAbility();
            Terminal terminal = new Terminal();
            terminal.attackAbility(hunter.getName(), talent.getName());
            attackPotential += talent.getAttack();
        }
        return attackPotential;
    }
}
