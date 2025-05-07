package System;

import Entities.Challenge;
import Entities.Client;
import java.util.ArrayList;
import java.io.Console;
import java.util.Optional;
import java.util.Scanner;
import Entities.Administrator;

public class MainSystem {
    public static Console out;

    /**
     * A continuación se definen las operaciones
     **/
    public void selector() {
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        terminal.showStart();
        int opcion = sc.nextInt();
        switch (opcion) {
            case 1 -> {
                terminal.userRegistrerMenu();
                int user = sc.nextInt();
                registerUser(user);
            }
            case 2 -> {
                //INICIO DE SESION COMO CLIENTE
                Client client = new Client();
                client = loginClient(client);
                if (client != null) {
                    Menu menu = new Menu();

                    ChallengeFileReader challengeFileReader = new ChallengeFileReader();
                    ArrayList<Challenge> listaDesafios = (ArrayList<Challenge>) challengeFileReader.readChallenges();

                    for (int i = 0; i < listaDesafios.size(); i++) { //tiene desafio pensdiente?. Mostramos mensaje
                        if (listaDesafios.get(i).isValidated() && listaDesafios.get(i).getRival().getNick().equals(client.getNick())) {
                            Client desafiante = listaDesafios.get(i).getChallenger();
                            String regNumber = listaDesafios.get(i).getRegister();
                            NotificationManager notificationManager = new NotificationManager();
                            notificationManager.notifyChallenge(client, terminal, listaDesafios, i, desafiante, regNumber);
                            i--;
                        }
                    }
                    menu.selectorClient(client, this);
                }
            }
            case 3 -> {
                //INICIO DE SESION COMO ADMIN
                Administrator admin = new Administrator();
                admin = loginAdmin(admin);
                if (admin != null) {
                    Menu menu = new Menu();
                    menu.adminSelector(admin, this);
                }
            }
            case 4 -> {
                terminal.logout();
                System.exit(0);
            }
            default -> terminal.error();
        }
    }

    public void registerUser(int option) {
        Scanner sc = new Scanner(System.in);
        Terminal terminal = new Terminal();
        switch (option) {
            case 1 -> { // Modo jugador
                Client client = new Client();
                UserFileReader userFileReader = new UserFileReader();
                ArrayList<Client> listClient = userFileReader.userFileReader();

                // Validación nombre (no vacío)
                String name;
                do {
                    terminal.askNameUser();
                    name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        terminal.emptyName(); // Mensaje específico para nombre vacío
                    }
                } while (name.isEmpty());
                client.setName(name);

                // Validación nick (único y no vacío)
                String nick;
                boolean nickExists;
                do {
                    nickExists = false;
                    do {
                        terminal.askNick();
                        nick = sc.nextLine().trim();
                        if (nick.isEmpty()) {
                            terminal.emptyNick(); // Mensaje específico para nick vacío
                        }
                    } while (nick.isEmpty());

                    // Verificar unicidad (case sensitive)
                    for (Client c : listClient) {
                        if (c.getNick().equals(nick)) {
                            terminal.nickExists();
                            nickExists = true;
                            break;
                        }
                    }
                } while (nickExists);
                client.setNick(nick);

                // Validación contraseña (no vacía, coincidente y correcta longitud)
                String password, confirm;
                do {
                    do {
                        terminal.askPassword();
                        password = sc.nextLine().trim();
                        if (password.isEmpty()) {
                            terminal.emptyPassword(); // Mensaje específico para contraseña vacía
                        } else if (password.length() < 8 || password.length() > 12) {
                            terminal.passwordTooShort(); // Mensaje específico para contraseña corta
                        }
                    } while (password.length() < 8 || password.length() > 12);

                    terminal.confirmPassword();
                    confirm = sc.nextLine().trim();

                    if (!password.equals(confirm)) {
                        terminal.errorPassword();
                    }
                } while (!password.equals(confirm));
                client.setPassword(password);

                // Generación de datos adicionales
                client.setRegister(client.generateRegisterNumber());
                client.setCharacter(null);

                // Registro del usuario
                new UserFileWriter().userRegister(client);
                terminal.savingNewUser();
                terminal.confirmNewUser(name);
            }

            case 2 -> { // Modo administrador
                Administrator client = new Administrator();
                AdministratorFileReader administratorFileReader = new AdministratorFileReader();
                ArrayList<Administrator> listClient = administratorFileReader.adminFileReader();

                // Validación nombre (no vacío)
                String name;
                do {
                    terminal.askNameUser();
                    name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        terminal.emptyName();
                    }
                } while (name.isEmpty());
                client.setName(name);

                // Validación nick (único y no vacío)
                String nick;
                boolean nickExists;
                do {
                    nickExists = false;
                    do {
                        terminal.askNick();
                        nick = sc.nextLine().trim();
                        if (nick.isEmpty()) {
                            terminal.emptyNick();
                        }
                    } while (nick.isEmpty());

                    // Verificar unicidad
                    for (Administrator admin : listClient) {
                        if (admin.getNick().equals(nick)) {
                            terminal.nickExists();
                            nickExists = true;
                            break;
                        }
                    }
                } while (nickExists);
                client.setNick(nick);

                // Validación contraseña (no vacía, coincidente y correcta longitud)
                String password, confirm;
                do {
                    do {
                        terminal.askPassword();
                        password = sc.nextLine().trim();
                        if (password.isEmpty()) {
                            terminal.emptyPassword(); // Mensaje específico para contraseña vacía
                        } else if (password.length() < 8 || password.length() > 12) {
                            terminal.passwordTooShort(); // Mensaje específico para contraseña corta
                        }
                    } while (password.length() < 8 || password.length() > 12);

                    terminal.confirmPassword();
                    confirm = sc.nextLine().trim();

                    if (!password.equals(confirm)) {
                        terminal.errorPassword();
                    }
                } while (!password.equals(confirm));
                client.setPassword(password);
                // Registro del administrador
                new AdministratorFileWriter().adminRegister(client);
                terminal.savingNewUser();
                terminal.confirmNewAdmin(name);
            }
            case 3 -> {
            }
            default -> terminal.error();
        }
    }

    public Client loginClient(Client client) {
        Scanner sc = new Scanner(System.in);
        Terminal terminal = new Terminal();
        UserFileReader userFileReader = new UserFileReader();
        BanFileReader banFileReader   = new BanFileReader();

        // 1) Cargar listas
        ArrayList<Client> users       = userFileReader.userFileReader();
        ArrayList<Client> bannedUsers = banFileReader.readBannedUsers();

        if (users.isEmpty()) {
            if (bannedUsers.isEmpty()) {
                terminal.noUsersError();
            } else {
                terminal.allusersAreBanned();
            }
            return null;
        }

        // 2) Pedir y buscar nick
        terminal.askNick();
        String nick = sc.nextLine().trim();
        Optional<Client> maybeUser = users.stream()
                .filter(u -> u.getNick().equalsIgnoreCase(nick))
                .findFirst();

        if (maybeUser.isEmpty()) {
            terminal.nickNotFoundError();
            return null;
        }
        Client foundUser = maybeUser.get();

        // 3) Verificar baneo
        boolean isBanned = bannedUsers.stream()
                .map(Client::getNick)
                .anyMatch(bannedNick -> bannedNick.equalsIgnoreCase(nick));

        if (isBanned) {
            terminal.userIsBanned(nick);
            return null;
        }

        // 4) Verificar contraseña
        while (true) {
            terminal.askPassword();
            String attempt = sc.nextLine();
            if (foundUser.getPassword().equals(attempt)) {
                break;
            } else {
                terminal.errorPassword();
            }
        }

        // 5) Éxito
        terminal.hiAgainUser(foundUser.getName());
        return foundUser;
    }

    public Administrator loginAdmin(Administrator admin) {
        Scanner sc = new Scanner(System.in);
        Terminal terminal = new Terminal();
        AdministratorFileReader operatorFileReader = new AdministratorFileReader();
        ArrayList<Administrator> list = operatorFileReader.adminFileReader();
        if (list.isEmpty()) {
            terminal.noAdmins();
            return null;
        }
        terminal.askNick();
        String nick = sc.nextLine();
        boolean found = false;
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNick().equals(nick)) {
                found = true;
                index = i;
                break;  // Salir del bucle en cuanto se encuentre
            }
        }
        if (!found) {
            terminal.nickNotFoundError();
            return null;
        }
        boolean passCorrect = false;
        do {
            terminal.askPassword();
            String password = sc.nextLine();
            passCorrect = list.get(index).getPassword().equals(password);
            if (!passCorrect) {
                terminal.errorPassword();
            }
        } while (!passCorrect);

        // Retornar el administrador encontrado, no el 'operator' pasado como parámetro.
        return list.get(index);
    }
}
