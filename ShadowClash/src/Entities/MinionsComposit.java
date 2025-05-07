package Entities;

import System.Terminal;
import java.util.ArrayList;
import java.util.Scanner;

public class MinionsComposit {
    /**A continuación se definen los atributos**/
    private String name;
    private int hp;
    private String type;

    /**A continuación se definen los Getters y Setters**/
    public String getType() {return type;}
    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }

    public MinionsComposit createMinion(boolean isVampire) {
        Terminal terminal = new Terminal();
        terminal.askMinionType();
        return minionSelector(isVampire);
    }

    private MinionsComposit minionSelector(boolean isVampiro) {
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        Terminal terminal = new Terminal();
        switch (opcion) {
            case 1 -> {
                if (!isVampiro) {
                    return createHuman();
                } else {
                    terminal.errorHuman();
                    terminal.askForMinionsNum();
                    return createMinion(isVampiro);
                }
            }
            case 2 -> {
                return createGhoul();
            }
            case 3 -> {
                ArrayList<MinionsComposit> minions = new ArrayList<>();
                return createDemon(minions, isVampiro);
            }
            default -> {
                terminal.error();
                createMinion(isVampiro);
            }
        }
        return null;
    }
    private Human createHuman() {
        Terminal terminal = new Terminal();
        Human human = new Human();
        Scanner sc = new Scanner(System.in);
        int hp;
        int loyaltyOption;
        Human.Loyalty loyalty;
        terminal.askMinionName();
        String name = sc.nextLine();
        human.setName(name);
        do {
            terminal.askForHpMinion();
            hp = sc.nextInt();
        } while (hp < 1 || hp > 3);
        human.setHp(hp);
        do {
            terminal.askForLoyalty();
            loyaltyOption = sc.nextInt();
        } while (loyaltyOption < 1 || loyaltyOption > 3);
        if (loyaltyOption == 1) {
            human.setLoyalty(Human.Loyalty.ALTA);
        } else if (loyaltyOption == 2) {
            human.setLoyalty(Human.Loyalty.MEDIA);
        } else {
            human.setLoyalty(Human.Loyalty.BAJA);
        }
        human.setType("HUMANO");
        return human;
    }
    private Ghoul createGhoul() {
        Terminal terminal = new Terminal();
        Ghoul ghoul = new Ghoul();
        Scanner sc = new Scanner(System.in);
        int hp;
        int dependendy;
        terminal.askMinionName();
        String name = sc.nextLine();
        ghoul.setName(name);
        do {
            terminal.askForHpMinion();
            hp = sc.nextInt();
        } while (hp < 1 || hp > 3);
        ghoul.setHp(hp);
        do {
            terminal.askForDependency();
            dependendy = sc.nextInt();
        } while (dependendy < 1 || dependendy > 5);
        ghoul.setDependency(dependendy);
        ghoul.setType("GHOUL");
        return ghoul;
    }
    private Demon createDemon(ArrayList<MinionsComposit> minions, boolean isVampiro) {
        Terminal terminal = new Terminal();
        Demon demon = new Demon();
        Scanner sc = new Scanner(System.in);
        int hp;
        terminal.askMinionName();
        String name = sc.nextLine();
        demon.setName(name);
        do {
            terminal.askForHpMinion();
            hp = sc.nextInt();
        } while (hp < 1 || hp > 3);
        demon.setHp(hp);
        terminal.askForPact();
        Scanner sc2 = new Scanner(System.in);
        String pact = sc2.nextLine();
        demon.setDescripcion(pact);
        terminal.askForMinionsNum();
        int numOfMinions = askNum();
        for (int iterator = 1; iterator <= numOfMinions; iterator++) {
            MinionsComposit minion = new MinionsComposit();
            minion = minion.createMinion(isVampiro);
            minions.add(minion);
        }
        demon.setMinionsComposites(minions);
        demon.setType("DEMONIO");
        return demon;
    }

    public int askNum() {
        //falta system.out.p creo
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }
}
