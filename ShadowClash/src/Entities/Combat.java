package Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import System.Terminal;
import System.*;

public class Combat {

    /**A continuación se definen los atributos**/
    private Client challenger;
    private Client rival;
    private ArrayList<Round> rounds;
    private Date date;
    private Client winner;
    private boolean challengerMinion;
    private boolean rivalMinion;
    private int gold;
    private ArrayList<Modifier> modifiers;
    private String register;
    private boolean seen;
    private String status;
    private int goldBet;
    private int hpChallenger;
    private int hpRival;


    /**A continuación se definen los Setters y Getters**/

    public Client getChallenger() {
        return challenger;
    }
    public void setChallenger(Client challenger) {
        this.challenger = challenger;
    }

    public Client getRival() {
        return rival;
    }
    public void setRival(Client rival) {
        this.rival = rival;
    }

    public ArrayList<Round> getRounds() {
        return rounds;
    }
    public void setRounds(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public Client getWinner() {
        return winner;
    }
    public void setWinner(Client winner) {
        this.winner = winner;
    }

    public boolean isChallengerMinion() {
        return challengerMinion;
    }
    public void setChallengerMinion(boolean challengerMinion) {
        this.challengerMinion = challengerMinion;
    }

    public boolean isRivalMinion() {
        return rivalMinion;
    }
    public void setRivalMinion(boolean rivalMinion) {this.rivalMinion = rivalMinion;}

    public int getGold() {
        return gold;
    }
    public void setGold(int oro) {
        this.gold = gold;
    }

    public ArrayList<Modifier> getModifiers() {
        return modifiers;
    }
    public void setModifiers(ArrayList<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public String getRegister() {
        return register;
    }
    public void setRegister(String register) {
        this.register = register;
    }

    public boolean isSeen() {
        return seen;
    }
    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public int getGoldBet() {
        return goldBet;
    }
    public void setGoldBet(int goldBet) {
        this.goldBet = goldBet;
    }

    /**A continuación se definen las Operaciones**/

    // Método para establecer el estado del combate
    public void setStatus(String status) {
        this.status = status;
    }

    // Método para obtener el estado del combate
    public String getStatus() {
        return status;
    }
    
    public Combat initializeCombat(Client challenger, Client rival, Date date,int gold, ArrayList<Modifier> modifiers, String register) {
        setChallenger(challenger);
        setRival(rival);
        setDate(date);
        setGoldBet(gold); //Antes estaba como setGold. No aseguraba que se guardara el oro apostado.
        setModifiers(modifiers);
        setRegister(register);
        setSeen(false);
        return this;
    }

    public Combat startCombatFromFile() {
        ChallengeFileReader challengeReader = new ChallengeFileReader();
        List<Challenge> challenges = challengeReader.readChallenges();

        if (challenges.isEmpty()) {
            throw new IllegalStateException("No hay desafíos disponibles.");
        }

        Challenge challenge = challenges.get(0); // Puedes adaptar esto para recorrer más desafíos

        // Leemos los clientes desde el fichero de usuarios
        UserFileReader userReader = new UserFileReader(); // Clase que tú ya tienes implementada
        List<Client> clients = userReader.userFileReader();

        Client fullChallenger = null;
        Client fullRival = null;

        // Buscamos los clientes con nick coincidente
        for (Client client : clients) {
            if (client.getNick().equals(challenge.getChallenger().getNick())) {
                fullChallenger = client;
            } else if (client.getNick().equals(challenge.getRival().getNick())) {
                fullRival = client;
            }
        }

        if (fullChallenger == null || fullRival == null) {
            throw new IllegalStateException("No se encontró uno o ambos participantes en el fichero de usuarios.");
        }

        // Inicializamos el combate con los datos completos
        Combat combat = new Combat();
        combat.initializeCombat(
                fullChallenger,
                fullRival,
                challenge.getDate(),
                challenge.getGold(),
                challenge.getModifiers(),
                challenge.getRegister()
        );
        // Ejecutamos el combate como ya lo tienes definido
        return startCombat(combat);
    }


    public Combat startCombat(Combat combat) {

        hpChallenger = restartInitHp(combat.getChallenger().getCharacter(), hpChallenger);
        hpRival = restartInitHp(combat.getRival().getCharacter(), hpRival);
        boolean endOfTheCombat = false;
        ArrayList<Round> rounds = new ArrayList<>();
        Terminal terminal = new Terminal();
        int numOfRound = 1;

        while (!endOfTheCombat) {
            Round round = new Round();
            terminal.showRound(numOfRound);
            endOfTheCombat = round.startRound(hpChallenger, hpRival, combat.getChallenger(), combat.getRival(), combat.getModifiers());
            hpChallenger = round.getHpChallengerEnd();
            hpRival = round.getHpRivalEnd();
            rounds.add(round);
            numOfRound++;
            if(combat.getChallenger().getCharacter().getType().equals("CAZADOR")) {
                System.out.println("Entra");
            }

            // Corte por vida igual o menor a 0
            if (hpChallenger <= 0 || hpRival <= 0) {
                endOfTheCombat = true;
            }
        }
        combat.setRounds(rounds);
        // Determinar ganador
        if (hpChallenger > 0 && hpRival <= 0) {
            combat.setWinner(combat.getChallenger());
            combat.setChallengerMinion(rounds.get(rounds.size() - 1).getHpChallengerEnd() < combat.getChallenger().getCharacter().getHealth());
        } else if (hpRival > 0 && hpChallenger <= 0) {
            combat.setWinner(combat.getRival());
            combat.setChallengerMinion(false);
        } else {
            combat.setWinner(null); // Empate
        }
        terminal.showRounds(combat);
        return combat;
    }



    private int addMinionsHp(MinionsComposit minion, int hp) {
            hp += minion.getHp();
            if (minion.getType().equals("DEMONIO")) {
                Demon demon = (Demon) minion;
                for (MinionsComposit subMinion: demon.getMinionsComposits()){

                    hp = addMinionsHp(subMinion, hp);
            }
        }
        return hp;
    }
    private int restartInitHp(Character character, int hp) {
        hp += character.getHealth();
        for (int numEsbirro = 0; numEsbirro < character.getMinions().size(); numEsbirro++) {
            hp = addMinionsHp(character.getMinions().get(numEsbirro), hp);
        }
        return hp;
    }
}
