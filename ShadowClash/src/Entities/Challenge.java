package Entities;

import java.util.*;
import System.*;

public class Challenge {
    /** Atributos **/
    private Client rival;
    private Client challenger;
    private int gold;
    private ArrayList<Modifier> modifiers;
    private boolean validated;
    private String register;
    private Date date;

    /** Setters y Getters **/
    public void setRival(Client rival) { this.rival = rival; }
    public Client getRival() { return rival; }

    public void setChallenger(Client challenger) { this.challenger = challenger; }
    public Client getChallenger() { return challenger; }

    public void setGold(int gold) { this.gold = gold; }
    public int getGold() { return gold; }

    public void setModifiers(ArrayList<Modifier> modifiers) { this.modifiers = modifiers; }
    public ArrayList<Modifier> getModifiers() { return modifiers; }

    public void setValidated(boolean validated) { this.validated = validated; }
    public boolean isValidated() { return validated; }

    public void setDate(Date date) { this.date = date; }
    public Date getDate() { return date; }

    public void setRegister(String register) { this.register = register; }
    public String getRegister() { return register; }

    /**
     * Método para crear un desafío.
     * @param client El cliente que crea el desafío.
     */
    public void createChallenge(Client client) {
        Terminal terminal = new Terminal();
        UserFileReader userFileReader = new UserFileReader();
        ArrayList<Client> clientsList = userFileReader.userFileReader();
        int goldAmount = -1;
        int rivalNum = -1;

        terminal.searchingRivals();
        String myNick = client.getNick();
        Iterator<Client> iterator = clientsList.iterator(); //Patron Iterator

        while (iterator.hasNext()) {
            Client current = iterator.next();
            if (current.getNick().equals(myNick) || current.getCharacter() == null) {
                iterator.remove();
            }else if (current.getCharacter().getGold() == 0) {
                iterator.remove();
            }
        }

        if (clientsList.isEmpty()) {
            terminal.notAvaliableRival();
        } else {
            terminal.welcomeChallenge();
            terminal.showAvaliableRivals(clientsList);
            do {
                try {
                    terminal.validNumber();
                    rivalNum = askForNumber();
                } catch (NumberFormatException e) {
                    terminal.invalidInput();
                }
            } while (rivalNum < 0 || rivalNum > clientsList.size() || clientsList.get(rivalNum - 1).getCharacter() == null);

            setRival(clientsList.get(rivalNum - 1));
            boolean validInput = false;

            terminal.askForGoldBet(client);
            do {
                try {
                    goldAmount = askForNumber();
                    if (goldAmount <= 0) {
                        terminal.lessThanZero();
                    } else if (goldAmount > client.getCharacter().getGold()) {
                        terminal.moreThanMyGold(client.getCharacter().getGold());
                    } else if (goldAmount > getRival().getCharacter().getGold()) {
                        terminal.moreThanRivalGold(getRival().getCharacter().getGold());
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    terminal.invalidInput();
                }
            } while (!validInput);
            setGold(goldAmount);
            client.getCharacter().setGold(client.getCharacter().getGold() - goldAmount);

            for (int numCliente = 0; numCliente < clientsList.size(); numCliente++) {
                if (clientsList.get(numCliente).getNick().equals(client.getNick())) {
                    clientsList.remove(numCliente);
                    clientsList.add(client);
                    break;
                }
            }

            setChallenger(client);
            setValidated(false);
            String registro = generateRegisterNumber();
            setRegister(registro);
            setModifiers(new ArrayList<>());
            setDate(new Date());

            terminal.showClashAnimation();
            terminal.challengeCreated();

            ChallengeFileReader fileReader = new ChallengeFileReader();
            ArrayList<Challenge> challenges = (ArrayList<Challenge>) fileReader.readChallenges();
            challenges.add(this); // AÑADES siempre el nuevo desafío
            ChallengeFileWriter fileWriter = new ChallengeFileWriter();
            fileWriter.rewriteChallengeFile(challenges); // Reescribes con la lista actualizada
            if (isValidated()) { //true -> si está validado, se envía la notificación a rival
                Client desafiante = getChallenger();
                NotificationManager notificationManager = new NotificationManager();
                notificationManager.notifyChallenge(client, terminal, challenges, 1, desafiante, registro);
            }
        }
    }

    private int askForNumber() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    public String generateRegisterNumber() {
        return Long.valueOf(System.currentTimeMillis()).toString();
    }
}//FIN
