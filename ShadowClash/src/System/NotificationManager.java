package System;

import Entities.Challenge;
import Entities.Client;
import Entities.Combat;
import java.util.ArrayList;
import java.util.Scanner;

public class NotificationManager {

    /**
     * Notify the rival that has been challenged
     */
    public void notifyChallenge(Client client, Terminal terminal, ArrayList<Challenge> challenges, int challengeNumber, Client desafiante, String regNumber) {
        int opcion;
        do {
            terminal.askChallenge(challenges.get(challengeNumber));
            opcion = askNum();
        } while (opcion < 1 || opcion > 2);
        if (opcion == 1) { //ACEPTAR
            doCombat(client, terminal, challenges, challengeNumber);
        } else { // 2 = NO ACEPTAR
            doNotAcceptCombat(client, challenges, challengeNumber); //10% oro
            terminal.restandoOro();
        }
        deleteChallengeFromFile(challenges.get(challengeNumber), challenges);
    }

    public int askNum() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    /**
     * Unsubscribe the challenge from the list of challenges
     * @param challenge Challenge to unsubscribe
     * @param challenges ArrayList of challenges
     */
    private void deleteChallengeFromFile(Challenge challenge, ArrayList<Challenge> challenges) {
        for (int numDesafio = 0; numDesafio < challenges.size(); numDesafio++) {
            if (challenge.getRegister().equals(challenges.get(numDesafio).getRegister())) {
                challenges.remove(numDesafio);
                ChallengeFileWriter challengeFileWriter = new ChallengeFileWriter();
                challengeFileWriter.rewriteChallengeFile(challenges);
                break;
            }
        }
    }

    private void doCombat(Client client, Terminal terminal, ArrayList<Challenge> challenges, int numDesafio) {
        // Selección de equipo (sin cambios)
        int cambioEquipo;
        do {
            terminal.changeTeam();
            cambioEquipo = askNum();
        } while (cambioEquipo < 1 || cambioEquipo > 2);

        if (cambioEquipo == 1) {
            client.selectTeam(client);
        }
        // Inicialización del combate
        Challenge desafioActual = challenges.get(numDesafio);
        int oroApostado = desafioActual.getGold();

        Combat combate = new Combat();
        combate.setGoldBet(oroApostado);
        combate = combate.initializeCombat(
                desafioActual.getChallenger(),
                client,
                desafioActual.getDate(),
                oroApostado,
                desafioActual.getModifiers(),
                desafioActual.getRegister()
        );

        // Ejecución del combate
        combate = combate.startCombatFromFile();

        // Cargar lista de clientes
        UserFileReader userFileReader = new UserFileReader();
        ArrayList<Client> clients = userFileReader.userFileReader();

        // Buscar ambos jugadores en la lista
        Client desafiante = null;
        Client clienteActual = null;

        for (Client c : clients) {
            if (c.getNick().equals(desafioActual.getChallenger().getNick())) {
                desafiante = c;
            }
            if (c.getNick().equals(client.getNick())) {
                clienteActual = c;
            }
        }

        // Manejo de resultados
        if (combate.getWinner() != null) {
            if (combate.getWinner().getNick().equals(client.getNick())) {
                // CLIENTE GANA: recibe el oro apostado del desafiante
                clienteActual.getCharacter().setGold(clienteActual.getCharacter().getGold() + oroApostado);
                // DESAFIANTE PIERDE: se le resta el oro apostado
                desafiante.getCharacter().setGold(
                        Math.max(desafiante.getCharacter().getGold() - oroApostado, 0));
            } else {
                // DESAFIANTE GANA: recibe el oro apostado del cliente
                desafiante.getCharacter().setGold(desafiante.getCharacter().getGold() + oroApostado);
                // CLIENTE PIERDE: se le resta el oro apostado
                clienteActual.getCharacter().setGold(
                        Math.max(clienteActual.getCharacter().getGold() - oroApostado, 0)
                );
            }
        } else {
            // EMPATE: no se transfiere oro
            terminal.goldStayTheSame();
        }

        // Actualizar ambos clientes en la lista
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            if (c.getNick().equals(clienteActual.getNick())) {
                clients.set(i, clienteActual);
            } else if (c.getNick().equals(desafiante.getNick())) {
                clients.set(i, desafiante);
            }
        }
        // Guardar cambios
        UserFileWriter userFileWriter = new UserFileWriter();
        userFileWriter.rewriteUserFile(clients);

        // Registrar combate y mostrar animación (sin cambios)
        CombatFileWriter combatFileWriter = new CombatFileWriter();
        combatFileWriter.combatFileWriter(combate);
        terminal.showClashAnimation();
    }

    private void doNotAcceptCombat(Client cliente, ArrayList<Challenge> challenges, int numDesafio) {
        Challenge desafio = challenges.get(numDesafio);
        int oroApostado = desafio.getGold();
        int penalizacion = oroApostado / 10; // 10% del oro apostado

        // Aplicar penalización al que rechazó
        int oroActual = cliente.getCharacter().getGold();
        int nuevoOroRechazador = Math.max(oroActual - penalizacion, 0);
        cliente.getCharacter().setGold(nuevoOroRechazador);

        // Recompensar al desafiante solo con la penalización (10%)
        UserFileReader userFileReader = new UserFileReader();
        ArrayList<Client> clients = userFileReader.userFileReader();
        String nickDesafiante = desafio.getChallenger().getNick();

        for (Client clienteActual : clients) {
            if (clienteActual.getNick().equals(nickDesafiante)) {
                int oroDesafiante = clienteActual.getCharacter().getGold();
                clienteActual.getCharacter().setGold(oroDesafiante + penalizacion);
                break;
            }
        }
        // Actualizar al cliente que rechazó en la lista
        for (int i = 0; i < clients.size(); i++) {
            if (cliente.getNick().equals(clients.get(i).getNick())) {
                clients.set(i, cliente);
                break;
            }
        }
        // Guardar cambios
        UserFileWriter userFileWriter = new UserFileWriter();
        userFileWriter.rewriteUserFile(clients);
    }
}
