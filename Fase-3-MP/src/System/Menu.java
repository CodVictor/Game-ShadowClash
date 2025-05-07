package System;

import Entities.*;
import Entities.Character;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    public void selectorClient(Client client, MainSystem system) {
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        int option;
        do {
            terminal.showMenu();
            option = sc.nextInt();
            switch (option) {
                case 1:
                    if (client.getCharacter() == null) { //crear personaje
                        terminal.showTipesOfCharacters();
                        selectFactory(client);
                    } else {
                        terminal.deleteCharacToCreateAnother();
                    }
                    break;
                case 2: //borrar personaje
                    if (client.getCharacter() != null) {
                        client.deleteCharacter(client);
                    } else {
                        terminal.youDontHaveCharacter();
                    }
                    break;
                case 3:// armaduras etc
                    if (client.getCharacter() != null) {
                        client.selectTeam(client);
                    } else {
                        terminal.youDontHaveTeam();
                    }
                    break;
                case 4: //desafiar
                    if (client.getGold() == 0) {
                        terminal.noGoldUser();
                        break;
                    }
                    if (client.getCharacter() != null) {
                        checkChallengeFile(client, terminal);
                    } else {
                        terminal.youDontHaveCharacter();
                    }
                    break;
                case 5: // consulta de batallas
                    terminal.combatsInformation(client);
                    break;
                case 6: //consultar desafios pendientes
                    consultarPendingChallenges(client);
                    break;
                case 7: //consultar ranking global
                    client.globalRanking();
                    break;
                case 8: //salir
                    terminal.logout();
                    system.selector();
                    break;
                case 9: //borrar cuenta
                    client.deleteAccount(client, system);
                    break;
                default:
                    terminal.error();
                    break;
            }
        } while (option != 8 && option != 9);
    }

    public void consultarPendingChallenges(Client client){
        Terminal terminal = new Terminal();
        ChallengeFileReader challengeFileReader = new ChallengeFileReader();

        ArrayList<Challenge> challenges = (ArrayList<Challenge>) challengeFileReader.readChallenges();

        terminal.showPendingChallenges(challenges, client);
    }

    private void checkChallengeFile(Client client, Terminal terminal) {
        ChallengeFileReader challengeFileReader = new ChallengeFileReader();
        ArrayList<Challenge> challengeList = (ArrayList<Challenge>) challengeFileReader.readChallenges();

        boolean isAlreadyInChallenge = false;

        // Verificamos si el cliente está como challenger en algún desafío
        for (Challenge ch : challengeList) {
            if (ch.getChallenger().getNick().equals(client.getNick())) {
                isAlreadyInChallenge = true;
                break;
            }
        }
        if (isAlreadyInChallenge) {
            // Ya está en un desafío → no puede desafiar a nadie más
            terminal.alreadyInAChallenge();
        } else if (client.getCharacter() != null) {
            // No está en desafío y tiene personaje → puede desafiar
            client.toChallenge(client);
        } else {
            // No tiene personaje → debe crear uno antes de desafiar
            terminal.youHaveToCreateACharacter();
        }
    }

    /**
     * Elección de tipo de personaje a crear
     * @param client Usuario que creará el personaje x
     */
    public void selectFactory(Client client) {
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        Character charac = null;
        int opcion = sc.nextInt();
        switch (opcion) {
            case 1 -> charac = client.createVampire(); // 1 Vampiro
            case 2 -> charac = client.createWerewolf();// 2 Licantropo
            case 3 -> charac = client.createHunter(); //  3 Cazador
            default -> terminal.errorInNumberInserted();
        }
        client.setCharacter(charac);
        if (client.getCharacter() != null) {
            terminal.savingCharacter();
            terminal.createdCharacterMsg(); //mensaje de personaje creado con éxito
        }
        UserFileReader userFileReader = new UserFileReader();
        UserFileWriter userFileWriter = new UserFileWriter();
        ArrayList<Client> clientList = userFileReader.userFileReader();

        for (int userNum = 0; userNum < clientList.size(); userNum++){
            if (client.getNick().equals(clientList.get(userNum).getNick())){
                clientList.remove(userNum);
                clientList.add(client);
                userFileWriter.rewriteUserFile(clientList);
                break;
            }
        }
    }

    public void adminSelector(Administrator admin, MainSystem system) {
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        Client client = new Client();
        int opcion;
        do {
            terminal.adminMenu();
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    admin.modifyCharacter();
                    break;
                case 2:
                    admin.validatingChallenge();
                    break;
                case 3:
                    admin.banUser(client);
                    break;
                case 4:
                    admin.unbanUser();
                    break;
                case 5: //logout
                    terminal.logout();
                    system.selector();
                    break;
                case 6: //borrar cuenta
                    admin.deleteAdminAccount(admin, system);
            }
        } while (opcion != 5 && opcion != 6);
    }
}
