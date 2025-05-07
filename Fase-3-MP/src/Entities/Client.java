package Entities;

import Factories.*;
import System.MainSystem;
import System.Terminal;
import java.util.*;
import System.UserFileReader;
import System.UserFileWriter;

public class Client extends User {

    /**A continuación se definen atributos**/
    private final String USER_FILE_PATH = "Fase-3-MP/src/Files/UserRegister.txt";
    private String register;
    private Character character;

    /**A continuación se definen Getters y Setters**/

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public char getLetter() {
        return (char) (Math.random() * 26 + 'a');
    }

    public char getNumber() {
        return (char) (Math.random() * 10 + '0');
    }

    /**A continuación se definen operaciones**/

    public int getGold() {
        return character != null ? character.getGold() : 0;
    }

    public int getHp() {
        return character != null ? character.getHealth() : 0;
    }

    public String generateRegisterNumber() {
        UserFileReader userFileReader = new UserFileReader();
        ArrayList<Client> list = userFileReader.userFileReader();
        String strBuilder = null;
        boolean valid = false;

        while (!valid) { //FORMATO LNNLL
            strBuilder = String.valueOf(getLetter()) +
                    getNumber() + getNumber() + getLetter() + getLetter();
            valid = true; // asumimos que es válido hasta que se demuestre lo contrario

            for (Client client : list) {
                if (client.getRegister().equals(strBuilder)) {
                    valid = false; // se encontró un duplicado, no es válido
                }
            }
        }
        return strBuilder;
    }

    public void toChallenge(Client cliente) { //desafiar -> toChallenge
        Challenge challenge = new Challenge();
        challenge.createChallenge(cliente);
    }
    /**
     * Método para eliminar un personaje del cliente
     * @param client Usuario logueado (puede ser Client o Administrator)
     */
    public void deleteCharacter(Client client) {
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        UserFileReader userFileReader = new UserFileReader();
        ArrayList<Client> clientList = userFileReader.userFileReader();
        UserFileWriter userFileWriter = new UserFileWriter();

        terminal.confirmDeleteCharacter();
        String confirmation = sc.nextLine().trim();
        if (confirmation.equalsIgnoreCase("ELIMINAR")) {
            // Buscar y actualizar el personaje del cliente dentro de la lista
            for (Client c : clientList) {
                if (c.getName().equals(client.getName())) {
                    c.setCharacter(null);
                    break;
                }
            }
            userFileWriter.rewriteUserFile(clientList);
            terminal.deletingCharacter();
            terminal.deletedCharacter();
        } else {
            terminal.cancelOperation();
        }
    }

    /**
     * Creación de los Vampiros
     * @return vampire
     */
    public Vampire createVampire() {
        boolean[] aux1 = new boolean[]{true, true};
        boolean[] aux2 = new boolean[]{true, false};
        boolean rightValue;

        VampireFactory vampireFactory = new VampireFactory();
        Terminal terminal = new Terminal();
        Vampire vampire = new Vampire();
        Discipline discipline = new Discipline();
        ArrayList<Weapon> Weapons = new ArrayList<>();
        ArrayList<Weapon> WeaponsActivas = new ArrayList<>();
        ArrayList<Armor> Weaponduras = new ArrayList<>();
        Weakness weakness = new Weakness();
        Strength strength = new Strength();
        ArrayList<Weakness> debilidades = new ArrayList<>();
        ArrayList<Strength> fortalezas = new ArrayList<>();
        Armor armor = new Armor();
        ArrayList<MinionsComposit> minionsComposits = new ArrayList<>();

        setNameNDAbilityVampire(vampireFactory, terminal, vampire, discipline);
        setAllWeaponsVampire(aux1, aux2, vampireFactory, terminal, vampire, Weapons, WeaponsActivas);
        setAllArmorsVampire(vampireFactory, terminal, vampire, Weaponduras, armor);
        setGoldPowerHPVampire(vampireFactory, terminal, vampire);
        setVampireModifiers(vampireFactory, terminal, vampire, weakness, strength, debilidades, fortalezas);
        terminal.askVampireAge();
        vampireFactory.setAge(vampire);
       do {
            terminal.askVampireBlood();
            rightValue = vampireFactory.initializeBlood(vampire);
        } while (!rightValue);
        setVampireMinions(vampireFactory, terminal, vampire, minionsComposits);
        vampire.setType("VAMPIRO");
        return vampire;
    }

    private void setVampireMinions(VampireFactory vampireFactory, Terminal terminal, Vampire vampire, ArrayList<MinionsComposit> minionsComposits) {
        terminal.askForMinionsNum();
        int minionsNum = vampireFactory.askNumber();
        for (int i = 1; i <= minionsNum; i++) {
            MinionsComposit minion = new MinionsComposit();
            minion = minion.createMinion(true);
            minionsComposits.add(minion);
        }
        vampire.setMinions(minionsComposits);
    }

    private void setVampireModifiers(VampireFactory vampireFactory, Terminal terminal, Vampire vampire, Weakness weakness, Strength strength, ArrayList<Weakness> weaknesses, ArrayList<Strength> strengths) {
        // Validación para el número de debilidades entre 1 y 5
        int weaknessNum;
        do {
            terminal.askNumWeakness();
            weaknessNum = vampireFactory.askNumber();
            if (weaknessNum < 1 || weaknessNum > 5) {
                terminal.invalidValue();  // Asegúrate de tener este mensaje en tu clase Terminal
            }
        } while (weaknessNum < 1 || weaknessNum > 5);

        for (int i = 1; i <= weaknessNum; i++) {
            terminal.askWeaknessName();
            vampireFactory.initializeWeaknessName(weakness);
            terminal.askWeaknessValue();
            vampireFactory.initializeWeaknessValue(weakness);
            vampireFactory.addWeakness(weaknesses, weakness);
        }
        vampireFactory.setWeaknesses(vampire, weaknesses);

        // Validación para el número de fortalezas entre 1 y 5
        int numFortalezas;
        do {
            terminal.askNumStrengths();
            numFortalezas = vampireFactory.askNumber();
            if (numFortalezas < 1 || numFortalezas > 5) {
                terminal.invalidValue();  // Asegúrate de tener este mensaje en tu clase Terminal
            }
        } while (numFortalezas < 1 || numFortalezas > 5);

        for (int iterator = 1; iterator <= numFortalezas; iterator++) {
            terminal.askStrengthName();
            vampireFactory.initializeStrengthName(strength);
            terminal.askStrengthValue();
            vampireFactory.initializeStrengthValue(strength);
            vampireFactory.addStrength(strengths, strength);
        }
        vampireFactory.setStrengths(vampire, strengths);
    }

    private void setGoldPowerHPVampire(VampireFactory vampireFactory, Terminal terminal, Vampire vampire) {
        boolean rightValue;
        do {
            terminal.askGold();
            rightValue = vampireFactory.initializeGold(vampire);
        } while (!rightValue);
        do {
            terminal.askForHp();
            rightValue = vampireFactory.initializeHP(vampire);
        } while (!rightValue);
        do {
            terminal.askPower();
            rightValue = vampireFactory.initializePower(vampire);
        } while (!rightValue);
    }

    private void setAllArmorsVampire(VampireFactory vampireFactory, Terminal terminal, Vampire vampire, ArrayList<Armor> armors, Armor armor) {
        boolean rightValue;
        int numArmors;
        do {
            terminal.askNumArmors();
            numArmors = vampireFactory.askNumber();
        } while (numArmors < 1);
        for (int i = 1; i <= numArmors; i++) {
            armor = new Armor();
            terminal.askNameArmors();
            vampireFactory.initializeArmorName(armor);
            do {
                terminal.askForDefenceArmor();
                rightValue = vampireFactory.initializeArmorDefense(armor);
            } while (!rightValue);
            do {
                terminal.askForAttackeArmor();
                rightValue = vampireFactory.initializeArmorAttack(armor);
            } while (!rightValue);
            vampireFactory.addArmor(armor, armors);
        }
        vampireFactory.setArmors(vampire, armors);
        do {
            terminal.showArmors(armors);
            rightValue = vampireFactory.addActiveArmor(vampire, armor, armors);
        } while (!rightValue);
    }

    private void setAllWeaponsVampire(boolean[] aux1, boolean[] aux2, VampireFactory vampireFactory, Terminal terminal, Vampire vampire, ArrayList<Weapon> weapons, ArrayList<Weapon> activeWeapons) {
        boolean[] rightWeapon;
        boolean rightValue;
        int weaponNum;
        do {
            terminal.askNumWeapons();
            weaponNum = vampireFactory.askNumber();
        } while (weaponNum < 1);
        for (int i = 1; i <= weaponNum; i++) {
            Weapon weapon = new Weapon();
            terminal.askWeapName();
            vampireFactory.initializeWeaponName(weapon);
            do {
                terminal.askWeapAttack();
                rightValue = vampireFactory.initializeWeaponAttack(weapon);
            } while (!rightValue);
            do {
                terminal.askWeapDefence();
                rightValue = vampireFactory.initializeWeaponDefense(weapon);
            } while (!rightValue);
            do {
                terminal.isWeaponSingleHanded();
                rightValue = vampireFactory.initializeWeaponSingleHand(weapon);
            } while (!rightValue);
            vampireFactory.addWeapon(weapons, weapon);
        }
        vampireFactory.setWeapons(vampire, weapons);
        do {
            terminal.showWeapons(weapons);
            rightWeapon = vampireFactory.addActiveWeapon(weapons, activeWeapons);
        } while (!Arrays.equals(rightWeapon, aux1) && !Arrays.equals(rightWeapon, aux2));
        if (Arrays.equals(rightWeapon, aux1)) {
            do {
                terminal.anotherWeapon(weapons, activeWeapons.get(0));
                rightValue = vampireFactory.addActiveWeapon2(weapons, activeWeapons);
                if (!rightValue) {
                    terminal.noCorrectNumSelecction();
                }
            } while (!rightValue);
        }
        vampireFactory.setActiveWeapons(vampire, activeWeapons);
    }

    private void setNameNDAbilityVampire(VampireFactory vampireFactory, Terminal terminal, Vampire vampire, Discipline discipline) {
        boolean rightValue;
        terminal.askVampireName();
        vampireFactory.initializeName(vampire);
        terminal.askAbilityName();
        vampireFactory.initializeAbilityName(discipline);
        do {
            terminal.askAbilityAttack();
            rightValue = vampireFactory.initializeAbilityAttack(discipline);
        } while (!rightValue);
        do {
            terminal.askAbilityDefence();
            rightValue = vampireFactory.initializeAbilityDefense(discipline);
        } while (!rightValue);
        do {
            terminal.askCostAbility();
            rightValue = vampireFactory.initializeAbilityCost(discipline);
        } while (!rightValue);
        vampireFactory.setAbility(vampire, discipline);
    }

    /** A continuación se definen los métodos de creación de Cazador */

    public Hunter createHunter() {
        boolean[] aux1 = new boolean[]{true, true};
        boolean[] aux2 = new boolean[]{true, false};

        HunterFactory hunterFactory = new HunterFactory();
        Terminal terminal = new Terminal();
        Hunter hunter = new Hunter();
        ArrayList<Weapon> Weapons = new ArrayList<>();
        ArrayList<Weapon> WeaponsActivas = new ArrayList<>();
        ArrayList<Armor> Weaponduras = new ArrayList<>();
        Weakness weakness = new Weakness();
        Strength strength = new Strength();
        ArrayList<Weakness> debilities = new ArrayList<>();
        ArrayList<Strength> fortress = new ArrayList<>();
        Armor armor = new Armor();
        ArrayList<MinionsComposit> minionsComposits = new ArrayList<>();
        Talent talent = new Talent();

        setNameAndAbilityHunter(hunterFactory, terminal, hunter, talent);
        setAllWeaponsHunter(aux1, aux2, hunterFactory, terminal, hunter, Weapons, WeaponsActivas);
        setAllArmorsHunter(hunterFactory, terminal, hunter, Weaponduras, armor);
        setGoldPowerHPHunter(hunterFactory, terminal, hunter);
        setHunterModifiers(hunterFactory, terminal, hunter, weakness, strength, debilities, fortress);
        setHunterMinions(hunterFactory, hunter, minionsComposits);
        hunterFactory.initializeHunterWillpower(hunter);
        hunter.setType("CAZADOR");
        hunter.setAbility(talent);
        return hunter;
    }
    private void setHunterMinions(HunterFactory hunterFactory, Hunter hunter, ArrayList<MinionsComposit> minions) {
        int numMinions = hunterFactory.askNumber();
        for (int iterator = 1; iterator <= numMinions; iterator++) {
            MinionsComposit minion = new MinionsComposit();
            minion = minion.createMinion(false);
            minions.add(minion);
        }
        hunter.setMinions(minions);
    }

    private void setHunterModifiers(HunterFactory hunterFactory, Terminal terminal, Hunter hunter, Weakness weakness, Strength strength, ArrayList<Weakness> weaknesses, ArrayList<Strength> strengths) {
        terminal.askNumWeakness();
        int numWeaknesss = hunterFactory.askNumber();
        for (int iterator = 1; iterator <= numWeaknesss; iterator++) {
            terminal.askWeaknessName();
            hunterFactory.initializeWeaknessName(weakness);
            terminal.askWeaknessValue();
            hunterFactory.initializeWeaknessValue(weakness);
            hunterFactory.addWeakness(weaknesses, weakness);
        }
        hunterFactory.setWeaknesses(hunter, weaknesses);
        terminal.askNumStrengths();
        int numFortalezas = hunterFactory.askNumber();
        for (int iterator = 1; iterator <= numFortalezas; iterator++) {
            terminal.askStrengthName();
            hunterFactory.initializeStrengthName(strength);
            terminal.askStrengthValue();
            hunterFactory.initializeStrengthValue(strength);
            hunterFactory.addStrength(strengths, strength);
        }
        hunterFactory.setStrengths(hunter, strengths);
        terminal.askForMinionsNum();
    }

    private void setGoldPowerHPHunter(HunterFactory hunterFactory, Terminal terminal, Hunter hunter) {
        boolean rightValue;
        do {
            terminal.askGold();
            rightValue = hunterFactory.initializeGold(hunter);
        } while (!rightValue);
        do {
            terminal.askForHp();
            rightValue = hunterFactory.initializeHP(hunter);
        } while (!rightValue);
        do {
            terminal.askPower();
            rightValue = hunterFactory.initializePower(hunter);
        } while (!rightValue);
    }

    private void setAllArmorsHunter(HunterFactory hunterFactory, Terminal terminal, Hunter hunter, ArrayList<Armor> armors, Armor armor) {
        boolean rightValue;
        int numArmors;
        do {
            terminal.askNumArmors();
            numArmors = hunterFactory.askNumber();
        } while (numArmors < 1);
        for (int iterator = 1; iterator <= numArmors; iterator++) {
            armor = new Armor();
            terminal.askNameArmors();
            hunterFactory.initializeArmorName(armor);
            do {
                terminal.askForDefenceArmor();
                rightValue = hunterFactory.initializeArmorDefense(armor);
            } while (!rightValue);
            do {
                terminal.askForAttackeArmor();
                rightValue = hunterFactory.initializeArmorAttack(armor);
            } while (!rightValue);
            hunterFactory.addArmor(armor, armors);
        }
        hunterFactory.setArmors(hunter, armors);
        do {
            terminal.showArmors(armors);
            rightValue = hunterFactory.addActiveArmor(hunter, armor, armors);
        } while (!rightValue);
    }

    private void setAllWeaponsHunter(boolean[] aux1, boolean[] aux2, HunterFactory hunterFactory, Terminal terminal, Hunter hunter, ArrayList<Weapon> weapons, ArrayList<Weapon> activeWeapons) {
        boolean rightValue;
        boolean[] rightWeapon;
        Weapon weapon;
        int numWeapons;
        do {
            terminal.askNumWeapons();
            numWeapons = hunterFactory.askNumber();
        } while (numWeapons < 1);
        for (int iterator = 1; iterator <= numWeapons; iterator++) {
            weapon = new Weapon();
            terminal.askWeapName();
            hunterFactory.initializeWeaponName(weapon);
            do {
                terminal.askWeapAttack();
                rightValue = hunterFactory.initializeWeaponAttack(weapon);
            } while (!rightValue);
            do {
                terminal.askWeapDefence();
                rightValue = hunterFactory.initializeWeaponDefense(weapon);
            } while (!rightValue);
            do {
                terminal.isWeaponSingleHanded();
                rightValue = hunterFactory.initializeWeaponSingleHand(weapon);
            } while (!rightValue);
            hunterFactory.addWeapon(weapons, weapon);
        }
        hunterFactory.setWeapons(hunter, weapons);
        do {
            terminal.showWeapons(weapons);
            rightWeapon = hunterFactory.addActiveWeapon(weapons, activeWeapons);
        } while (!Arrays.equals(rightWeapon, aux1) && !Arrays.equals(rightWeapon, aux2));
        if (Arrays.equals(rightWeapon, aux1)) {
            do {
                terminal.anotherWeapon(weapons, activeWeapons.get(0));
                rightValue = hunterFactory.addActiveWeapon2(weapons, activeWeapons);
                if (!rightValue) {
                    terminal.noCorrectNumSelecction();
                }
            } while (!rightValue);
        }
        hunterFactory.setActiveWeapons(hunter, activeWeapons);
    }

    private void setNameAndAbilityHunter(HunterFactory hunterFactory, Terminal terminal, Hunter hunter, Talent talent) {
        boolean rightValue;
        terminal.askHunterName();
        hunterFactory.initializeName(hunter);
        terminal.askAbilityName();
        hunterFactory.initializeAbilityName(talent);
        do {
            terminal.askAbilityAttack();
            rightValue = hunterFactory.initializeAbilityAttack(talent);
        } while (!rightValue);
        do {
            terminal.askAbilityDefence();
            rightValue = hunterFactory.initializeAbilityDefense(talent);
        } while (!rightValue);
    }

    /**
     * A continuación se definen los métodos de creación de Licántropo
     */

    public Werewolf createWerewolf() {
        boolean[] aux1 = new boolean[]{true, true};
        boolean[] aux2 = new boolean[]{true, false};

        WerewolfFactory werewolfFactory = new WerewolfFactory();
        Terminal terminal = new Terminal();
        Werewolf werewolf = new Werewolf();
        Don don = new Don();
        ArrayList<Weapon> Weapons = new ArrayList<>();
        ArrayList<Weapon> WeaponsActivas = new ArrayList<>();
        ArrayList<Armor> Weaponduras = new ArrayList<>();
        Weakness weakness = new Weakness();
        Strength strength = new Strength();
        ArrayList<Weakness> debilities = new ArrayList<>();
        ArrayList<Strength> fortress = new ArrayList<>();
        Armor armor = new Armor();
        ArrayList<MinionsComposit> minionsComposits = new ArrayList<>();

        setNameAndAbilityWerewolf(werewolfFactory, terminal, werewolf, don);
        setAllWeaponsWerewolf(aux1, aux2, werewolfFactory, terminal, werewolf, Weapons, WeaponsActivas);
        setAllArmorsWerewolf(werewolfFactory, terminal, werewolf, armor, Weaponduras);
        setGoldPowerHPWerewolf(werewolfFactory, terminal, werewolf);
        setWerewolfModifiers(werewolfFactory, terminal, werewolf, weakness, strength, debilities, fortress);
        setWerewolfMinions(werewolfFactory, werewolf, minionsComposits);
        werewolf.setType("LICANTROPO");
        werewolf.setAbility(don);
        return werewolf;
    }

    private void setWerewolfMinions(WerewolfFactory werewolfFactory, Werewolf werewolf, ArrayList<MinionsComposit> minions) {
        Terminal terminal = new Terminal();
        terminal.askForMinionsNum();
        int numEsbirros = werewolfFactory.askNumber();
        for (int iterator = 1; iterator <= numEsbirros; iterator++) {
            MinionsComposit minion = new MinionsComposit();
            minion = minion.createMinion(false);
            minions.add(minion);
        }
        werewolf.setMinions(minions);
    }

    private void setWerewolfModifiers(WerewolfFactory werewolfFactory, Terminal terminal, Werewolf werewolf, Weakness weakness, Strength strength, ArrayList<Weakness> weaknesses, ArrayList<Strength> strengths) {
        terminal.askNumWeakness();
        int numDebilidades = werewolfFactory.askNumber();
        for (int iterator = 1; iterator <= numDebilidades; iterator++) {
            terminal.askWeaknessName();
            werewolfFactory.initializeWeaknessName(weakness);
            terminal.askWeaknessValue();
            werewolfFactory.initializeWeaknessValue(weakness);
            werewolfFactory.addWeakness(weaknesses, weakness);
        }
        werewolfFactory.setWeaknesses(werewolf, weaknesses);
        terminal.askNumStrengths();
        int numFortalezas = werewolfFactory.askNumber();
        for (int iterator = 1; iterator <= numFortalezas; iterator++) {
            terminal.askStrengthName();
            werewolfFactory.initializeStrengthName(strength);
            terminal.askStrengthValue();
            werewolfFactory.initializeStrengthValue(strength);
            werewolfFactory.addStrength(strengths, strength);
        }
        werewolfFactory.setStrengths(werewolf, strengths);
    }

    private void setGoldPowerHPWerewolf(WerewolfFactory werewolfFactory, Terminal terminal, Werewolf werewolf) {
        boolean rightValue;
        do {
            terminal.askGold();
            rightValue = werewolfFactory.initializeGold(werewolf);
        } while (!rightValue);
        do {
            terminal.askForHp();
            rightValue = werewolfFactory.initializeHP(werewolf);
        } while (!rightValue);
        do {
            terminal.askPower();
            rightValue = werewolfFactory.initializePower(werewolf);
        } while (!rightValue);
    }

    private void setAllArmorsWerewolf(WerewolfFactory werewolfFactory, Terminal terminal, Werewolf werewolf, Armor armor, ArrayList<Armor> armors) {
        boolean rightValue;
        terminal.askNumArmors();
        int numWeaponduras = werewolfFactory.askNumber();
        for (int iterator = 1; iterator <= numWeaponduras; iterator++) {
            armor = new Armor();
            terminal.askNameArmors();
            werewolfFactory.initializeArmorName(armor);
            do {
                terminal.askForDefenceArmor();
                rightValue = werewolfFactory.initializeArmorDefense(armor);
            } while (!rightValue);
            do {
                terminal.askForAttackeArmor();
                rightValue = werewolfFactory.initializeArmorAttack(armor);
            } while (!rightValue);
            werewolfFactory.addArmor(armor, armors);
        }
        werewolfFactory.setArmors(werewolf, armors);
        do {
            terminal.showArmors(armors);
            rightValue = werewolfFactory.addActiveArmor(werewolf, armor, armors);
        } while (!rightValue);
    }

    private void setAllWeaponsWerewolf(boolean[] aux1, boolean[] aux2, WerewolfFactory werewolfFactory, Terminal terminal, Werewolf werewolf, ArrayList<Weapon> weapons, ArrayList<Weapon> activeWeapons) {
        boolean[] rightWeapon;
        boolean rightValue;
        terminal.askNumWeapons();
        int numWeapons = werewolfFactory.askNumber();
        for (int iterator = 1; iterator <= numWeapons; iterator++) {
            Weapon weapon = new Weapon();
            terminal.askWeapName();
            werewolfFactory.initializeWeaponName(weapon);
            do {
                terminal.askWeapAttack();
                rightValue = werewolfFactory.initializeWeaponAttack(weapon);
            } while (!rightValue);
            do {
                terminal.askWeapDefence();
                rightValue = werewolfFactory.initializeWeaponDefense(weapon);
            } while (!rightValue);
            do {
                terminal.isWeaponSingleHanded();
                rightValue = werewolfFactory.initializeWeaponSingleHand(weapon);
            } while (!rightValue);
            werewolfFactory.addWeapon(weapons, weapon);
        }
        werewolfFactory.setWeapons(werewolf, weapons);
        do {
            terminal.showWeapons(weapons);
            rightWeapon = werewolfFactory.addActiveWeapon(weapons, activeWeapons);
        } while (!Arrays.equals(rightWeapon, aux1) && !Arrays.equals(rightWeapon, aux2));
        if (Arrays.equals(rightWeapon, aux1)) {
            do {
                terminal.anotherWeapon(weapons, activeWeapons.get(0));
                rightValue = werewolfFactory.addActiveWeapon2(weapons, activeWeapons);
                if (!rightValue) {
                    terminal.noCorrectNumSelecction();
                }
            } while (!rightValue);
        }
        werewolfFactory.setActiveWeapons(werewolf, activeWeapons);
    }

    private void setNameAndAbilityWerewolf(WerewolfFactory werewolfFactory, Terminal terminal, Werewolf werewolf, Don don) {
        boolean rightValue;
        terminal.askWerewolfName();
        werewolfFactory.initializeName(werewolf);
        terminal.askAbilityName();
        werewolfFactory.initializeAbilityName(don);
        werewolf.setRage(0);
        don.setAttack(0);
        don.setDefense(0);
        do {
            terminal.askAbilityRage();
            rightValue = werewolfFactory.initializeRageAbility(don);
        } while (!rightValue);
        werewolfFactory.setAbility(werewolf, don);
    }


    /**
     * Elimina permanentemente una cuenta del sistema
     * @param client Usuario logueado (puede ser Client o Administrator)
     * @param system Referencia al sistema principal
     */
    public void deleteAccount(Client client, MainSystem system) {
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);

        // Mostrar advertencia y solicitar confirmación
        terminal.advertency();
        terminal.writeConfirm();
        // Leer confirmación
        String confirmation = sc.nextLine().trim();

        if (confirmation.equalsIgnoreCase("ELIMINAR")) {
            try {
                // Leer lista actual de clientes
                UserFileReader userFileReader = new UserFileReader();
                ArrayList<Client> clientList = userFileReader.userFileReader();

                // Buscar y eliminar cliente
                boolean removed = clientList.removeIf(c -> c.getRegister().equals(client.getRegister()));

                if (removed) {
                    // Guardar lista actualizada
                    UserFileWriter userFileWriter = new UserFileWriter();
                    userFileWriter.rewriteUserFile(clientList);

                    // Cerrar sesión
                    terminal.deletingUser();
                    terminal.deletedAccountOK();
                    terminal.logout();
                    system.selector();
                } else {
                    terminal.noAccountAvaliable();
                }
            } catch (Exception e) {
                terminal.error();
                e.getMessage();
            }
        } else {
            terminal.cancelOperation();
            terminal.closedSesion4Security();
            system.selector();
        }
    }
    public void selectTeam(Client client) {
        boolean rightValue;
        boolean[] rightWeapon;
        boolean[] aux1 = new boolean[]{true, true};  // Dos armas activas
        boolean[] aux2 = new boolean[]{true, false}; // Una arma activa

        ArrayList<Weapon> activeWeapons = new ArrayList<>();
        Terminal terminal = new Terminal();

        // Mostrar armas y pedir al cliente que seleccione las activas
        do {
            terminal.showWeapons(client.getCharacter().getWeapons());
            rightWeapon = addActiveWeapon(client.getCharacter().getWeapons(), activeWeapons);
        } while (!Arrays.equals(rightWeapon, aux1) && !Arrays.equals(rightWeapon, aux2));

        // Si el usuario ha seleccionado una arma, se pregunta si quiere agregar otra
        if (Arrays.equals(rightWeapon, aux1)&& !activeWeapons.get(0).isSingleHand()) { //si era single hand no mostramos
            // Si tiene dos armas, mostramos la opción de añadir otra arma
            do {
                terminal.anotherWeapon(client.getCharacter().getWeapons(), client.getCharacter().getActiveWeapons().get(0));
                rightValue = addActiveWeapon2(client.getCharacter().getWeapons(), activeWeapons);
            } while (!rightValue);
        }
        // Establecemos las armas activas seleccionadas
        client.getCharacter().setActiveWeapons(activeWeapons);
        // Selección y equipamiento de armaduras
        do {
            terminal.showArmors(client.getCharacter().getArmors());
            rightValue = addActiveArmor(client.getCharacter(), client.getCharacter().getArmors());
        } while (!rightValue);

        // Leer y actualizar los datos de usuario en el fichero
        UserFileReader lecturaFicheroUsuarios = new UserFileReader();
        UserFileWriter escrituraFicheroUsuario = new UserFileWriter();
        ArrayList<Client> clientsList = lecturaFicheroUsuarios.userFileReader();

        for (int numCliente = 0; numCliente < clientsList.size(); numCliente++) {
            if (client.getNick().equals(clientsList.get(numCliente).getNick())) {
                clientsList.remove(numCliente);
                clientsList.add(client);
                escrituraFicheroUsuario.rewriteUserFile(clientsList);
                break;
            }
        }

        // Confirmar que las armas han sido equipadas
        terminal.equipingWeapons();
        terminal.finishEquipar();
    }

    private boolean[] addActiveWeapon(ArrayList<Weapon> Weapons, ArrayList<Weapon> WeaponsActivas) {
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        if ((opcion < 1) || (opcion > Weapons.size() + 1)) {
            return new boolean[]{false, false};
        }
        WeaponsActivas.add(Weapons.get(opcion - 1));
        return new boolean[]{true, Weapons.get(opcion - 1).isSingleHand()};
    }

    private boolean addActiveWeapon2(ArrayList<Weapon> Weapons, ArrayList<Weapon> WeaponsActivas) {
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        if ((opcion < 0) || (opcion > Weapons.size() + 1)) {
            return false;
        }
        if (opcion == 0) {
            return true;
        }
        if (!Weapons.get(opcion + 1).getName().equals(WeaponsActivas.get(0).getName()) && Weapons.get(opcion + 1).isSingleHand()) {
            WeaponsActivas.add(Weapons.get(opcion - 1));
            return true;
        }
        return false;
    }

    private boolean addActiveArmor(Character character, ArrayList<Armor> Armors) {
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        if ((opcion < 1) || (opcion > Armors.size() + 1)) {
            return false;
        }
        Armor Weapondura = Armors.get(opcion - 1);
        character.setActiveArmor(Weapondura);
        return true;
    }

    /**
     * Método para mostrar el ranking de oro de los usuarios
     */
    public void globalRanking() {
        Terminal terminal = new Terminal();
        terminal.rankingMessage();
        // Usamos directamente el lector basado en Map.Entry<String, Integer> es decir ORO 45 en el fichero User..txt
        List<Map.Entry<String, Integer>> lista = UserFileReader.goldReaderForRanking(USER_FILE_PATH);
        // Ordenamos por oro descendente (los que no tienen oro, se consideran 0)
        lista.sort((a, b) -> {
            int oro1 = (a.getValue() != null) ? a.getValue() : 0;
            int oro2 = (b.getValue() != null) ? b.getValue() : 0;
            return Integer.compare(oro2, oro1); // descendente
        });
        // Mostrar ranking con formato bonito
        terminal.showGoldRankingSimple(lista);
    }
}
