package Entities;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import Factories.HunterFactory;
import Factories.VampireFactory;
import Factories.WerewolfFactory;
import System.*;


public class Administrator extends User{

    /**
     * Elimina permanentemente una cuenta del sistema
     * @param admin Usuario logueado (puede ser Client o Administrator)
     * @param system Referencia al sistema principal
     */
    public void deleteAdminAccount(Administrator admin, MainSystem system) {
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);

        // Mostrar advertencia y solicitar confirmación
        terminal.advertency();
        terminal.writeConfirm();

        // Leer confirmación
        String confirmation = sc.nextLine().trim();

        if (confirmation.equalsIgnoreCase("ELIMINAR")) {
            try {
                // Leer lista actual de administradores
                AdministratorFileReader adminFileReader = new AdministratorFileReader();
                ArrayList<Administrator> adminList = adminFileReader.adminFileReader();

                // Buscar y eliminar por nick (case-sensitive)
                boolean removed = adminList.removeIf(a -> a.getNick().equals(admin.getNick()));

                if (removed) {
                    // Guardar lista actualizada
                    AdministratorFileWriter adminFileWriter = new AdministratorFileWriter();
                    adminFileWriter.rewriteAdminFile(adminList);

                    // Cerrar sesión
                    terminal.deletingAdmin();
                    terminal.deletedAccountOK();
                    terminal.logout();
                    system.selector();
                } else {
                    terminal.noAccountAvaliable();
                }
            } catch (Exception e) {
                terminal.error();
                terminal.error();
                e.getMessage(); // Log para depuración
            }
        } else {
            terminal.cancelOperation();
            terminal.closedSesion4Security();
            system.selector();
        }
    }

    /**Modifica los atributos de un personaje**/
    public void modifyCharacter(){
        Scanner sc = new Scanner(System.in);
        Terminal terminal = new Terminal();
        UserFileReader userFileReader = new UserFileReader();
        ArrayList<Client> listaclients = userFileReader.userFileReader();
        Iterator<Client> iterator = listaclients.iterator(); //PATRON ITERATOR
        while (iterator.hasNext()) {
            Client client = iterator.next();
            if (client.getCharacter() == null) {
                iterator.remove(); // Elimina de manera segura
            }
        }
        Client client = new Client();
        boolean encontrado = false;
        do {
            terminal.chargingUsers();
            terminal.showNicks(listaclients);
            terminal.askNickToAdmin();
            String nick = sc.nextLine();
            for (int i = 0; i < listaclients.size(); i++) {
                if (listaclients.get(i).getNick().equals(nick)) {
                    encontrado = true;
                    client = listaclients.get(i);
                    i = listaclients.size();
                }
            }
            if (!encontrado) {
                terminal.errorNick();
            }
        } while (!encontrado);
        VampireFactory VampireFactory = new VampireFactory();
        HunterFactory HunterFactory = new HunterFactory();
        WerewolfFactory WerewolfFactory = new WerewolfFactory();
        int opcion;
        do {
            terminal.menuModifyCharacterAtributes();
            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> changeNombre(terminal, client);
                case 2 -> changeAbility(terminal, client, VampireFactory, HunterFactory, WerewolfFactory);
                case 3 -> changeWeapons(sc, terminal, client, HunterFactory);
                case 4 -> changeActiveWeapons(terminal, client, VampireFactory, HunterFactory, WerewolfFactory);
                case 5 -> changeArmors(sc, terminal, client, HunterFactory);
                case 6 -> changeActiveArmors(terminal, client);
                case 7 -> changeMinions(sc, terminal, client);
                case 8 -> changeGold(sc, terminal, client);
                case 9 -> changeHealth(sc, terminal, client);
                case 10 -> changePower(sc, terminal, client);
                case 11 -> changeWaknesses(sc, terminal, client, HunterFactory);
                case 12 -> changeStrengths(sc, terminal, client, HunterFactory);
                case 13 -> {
                    terminal.savingChanges();
                    terminal.changesSaved();}
                default -> terminal.error();
            }
        } while (opcion != 13);
        UserFileWriter userFileWriter = new UserFileWriter();
        for (int numclient = 0; numclient < listaclients.size(); numclient++){
            if (client.getNick().equals(listaclients.get(numclient).getNick())){
                listaclients.remove(numclient);
                listaclients.add(client);
                userFileWriter.rewriteUserFile(listaclients);
                break;
            }
        }
    }

    /**Valida los desafíos pendientes**/
    public void validatingChallenge() {
        Scanner sc = new Scanner(System.in);
        Terminal terminal = new Terminal();

        // Carga clientes y desafíos
        ArrayList<Client> clientsList = new UserFileReader().userFileReader();
        Map<String, Client> clientsByNick = new HashMap<>();
        for (Client client : clientsList) {
            clientsByNick.put(client.getNick(), client);
        }
        ArrayList<Challenge> challengeList =
                (ArrayList<Challenge>) new ChallengeFileReader().readChallenges();

        // Filtrar pendientes
        ArrayList<Challenge> pending = new ArrayList<>();
        for (Challenge ch : challengeList) {
            if (!ch.isValidated()) pending.add(ch);
        }
        if (pending.isEmpty()) {
            terminal.noDesafiosParaValidar();
            return;
        }

        // 1) Mostrar pendientes con índice
        terminal.mostrarDesafiosPendientes(pending);

        // 2) Pedir selección
        int sel;
        do {
            terminal.pedirNumeroDesafio(pending.size());
            sel = sc.nextInt();
            if (sel < 1 || sel > pending.size()) terminal.validNumber();
        } while (sel < 1 || sel > pending.size());

        Challenge challenge = pending.get(sel - 1);
        Client challenger = challenge.getChallenger();
        Client rival = challenge.getRival();

        // Nueva lógica de verificación de combates recientes
        List<Combat> combates = new CombatFileReader().readCombats();
        boolean debeBanear = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        for (Combat combate : combates) {
            if (combate.getChallenger().getNick().equals(challenger.getNick()) &&
                    combate.getWinner() != null &&
                    !combate.getWinner().getNick().equals(challenger.getNick())) {

                // Conversión correcta de fechas
                Date fechaCombate = combate.getDate();
                Date ahora = new Date();

                // Calcular diferencia en horas
                long diferenciaMillis = ahora.getTime() - fechaCombate.getTime();
                long horasDiferencia = diferenciaMillis / (60 * 60 * 1000);

                if (horasDiferencia <= 24) {
                    debeBanear = true;
                    break;
                }
            }
        }

        if (debeBanear) {
            terminal.mostrarAdvertenciaBaneo(challenger.getNick());
            int opcionBan = sc.nextInt();
            if (opcionBan == 1) {
                // Buscar y banear al usuario
                for (Client c : clientsList) {
                    if (c.getNick().equals(challenger.getNick())) {
                        ban24User(c);
                        new UserFileWriter().rewriteUserFile(clientsList);
                        // Eliminar desafío actual
                        challengeList.remove(challenge);
                        new ChallengeFileWriter().rewriteChallengeFile(challengeList);
                        return;
                    }
                }
            }
        }

        ArrayList<Modifier> modifiers = challenge.getModifiers();
        if (!modifiers.isEmpty()) {
            terminal.haveModifiersToChose();
            terminal.showChallengeModifiers(challenger, rival);
            System.out.println("=======================================");
        }
        // Pedir VALIDAR o CANCELAR
        int opcion;
        do {
            terminal.pedirValidacion();
            opcion = sc.nextInt();
            if (opcion != 1 && opcion != 2) terminal.validNumber();
        } while (opcion != 1 && opcion != 2);

        if (opcion == 1) {
            // VALIDAR
            challenge.setValidated(true);
            ArrayList<Modifier> selected = new ArrayList<>();

            if(!modifiers.isEmpty()) {
                terminal.electModifiers();
                sc.nextLine(); // limpiar buffer
                String input;
                do {
                    input = sc.nextLine();
                    if (!input.equals("salir")) {
                        boolean found = false;
                        for (Weakness w : challenger.getCharacter().getWeaknesses()) {
                            if (w.getName().equals(input)) {
                                selected.add(w);
                                found = true;
                                break;
                            }
                        }
                        for (Strength s : challenger.getCharacter().getStrengths()) {
                            if (!found && s.getName().equals(input)) {
                                selected.add(s);
                                found = true;
                                break;
                            }
                        }
                        if (!found) terminal.errorMod();
                    }
                } while (!input.equals("salir"));
                challenge.setModifiers(selected);
            }
            terminal.desafioValidadoCorrectamente(challenge);

        } else {
            // CANCELAR
            challengeList.remove(challenge);
            terminal.desafioCancelado(challenge);
        }
        // 4) Guardar cambios
        new ChallengeFileWriter().rewriteChallengeFile(challengeList);
    }

    public void ban24User(Client client) {
        BanFileReader banFileReader = new BanFileReader();
        BanFileWriter banWriter = new BanFileWriter();
        UserFileWriter userWriter = new UserFileWriter();
        Terminal terminal = new Terminal();

        // 1. Verificar si ya está baneado
        ArrayList<Client> bannedClients = banFileReader.readBannedUsers();
        boolean yaBaneado = bannedClients.stream()
                .anyMatch(c -> c.getNick().equalsIgnoreCase(client.getNick()));

        if (yaBaneado) {
            terminal.userAlreadyBanned();
            return;
        }

        try {
            // 2. Configurar datos de baneo
            client.setBanDateTime(LocalDateTime.now());
            client.setBanMotive("Baneo automático por pérdida en desafío dentro de 24h");
            // 3. Actualizar archivos
            // Añadir a lista de baneados
            bannedClients.add(client);
            banWriter.rewriteBanFile(bannedClients);
            // Actualizar usuario en archivo principal
            ArrayList<Client> allUsers = new UserFileReader().userFileReader();
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getNick().equals(client.getNick())) {
                    allUsers.set(i, client);
                    break;
                }
            }
            userWriter.rewriteUserFile(allUsers);

            terminal.banned(client.getNick());

        } catch (Exception e) {
            terminal.error();
            e.printStackTrace();
        }
    }

    /**
     * Desbaneado de un usuario
     */
    public void unbanUser() {
        UserFileReader userFileReader = new UserFileReader();
        BanFileReader banFileReader = new BanFileReader();
        BanFileWriter banFileWriter = new BanFileWriter();
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);

        // Leer usuarios y baneados
        ArrayList<Client> userList = userFileReader.userFileReader();
        ArrayList<Client> bannedClients = banFileReader.readBannedUsers();

        if (bannedClients.isEmpty()) {
            terminal.noUsersBannedError();
            return;
            }

        // Mostrar lista de baneados numerada
        terminal.showBannedUsers(bannedClients);
        terminal.whatUserToUnBan();

        try {
            int selection = sc.nextInt();
            sc.nextLine();

            if (selection == 0) {
                terminal.cancelOperation();
                return;
            }

            if (selection < 1 || selection > bannedClients.size()) {
                terminal.noNumberIn();
                return;
            }

            Client userToUnban = bannedClients.get(selection - 1);
            String bannedNick = userToUnban.getNick();

            terminal.confirmUnban(bannedNick);
            String confirm = sc.nextLine().trim().toUpperCase();

            if (confirm.equals("DESBANEAR")) {
                // 1. Eliminar del archivo de baneados
                bannedClients.remove(userToUnban);
                userToUnban.setBanMotive("");
                banFileWriter.rewriteBanFile(bannedClients);
                //2. Mensajes de confirmación
                terminal.unbanningUser();
                terminal.unbbanedUser(bannedNick);

            } else {
                terminal.cancelOperation();
            }
        } catch (InputMismatchException e) {
            terminal.noNumberIn();
            sc.nextLine();
        } catch (Exception e) {
            terminal.error();
            e.printStackTrace();
        }
    }

    /**
     * Banea a un usuario
     * @param client Usuario a banear
     */
    public void banUser(Client client) {
        UserFileReader userFileReader = new UserFileReader();
        BanFileReader  banFileReader  = new BanFileReader();
        BanFileWriter  banWriter      = new BanFileWriter();
        Terminal       terminal       = new Terminal();
        Scanner        sc             = new Scanner(System.in);

        // 1) Cargo todas las listas
        ArrayList<Client> allUsers      = userFileReader.userFileReader();
        ArrayList<Client> bannedClients = banFileReader.readBannedUsers();

        // 2) Preparo la lista de candidatos a banear (todos - ya baneados)
        Set<String> bannedNicks = bannedClients.stream()
                .map(Client::getNick)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        ArrayList<Client> toBan = allUsers.stream()
                .filter(u -> {
                    String nick = u.getNick();
                    return nick != null && !bannedNicks.contains(nick.toLowerCase());
                })
                .collect(Collectors.toCollection(ArrayList::new));

        if (toBan.isEmpty()) {
            terminal.noUsersToBanError();  // “No hay usuarios disponibles para banear”
            return;
        }

        // 3) Muestro sólo los no baneados
        terminal.allUsers(toBan);
        terminal.whatUserToBan();

        try {
            int selection = sc.nextInt();
            sc.nextLine();

            if (selection == 0) {
                terminal.cancelOperation();
                return;
            }
            if (selection < 1 || selection > toBan.size()) {
                terminal.invalidSelecction();
                return;
            }

            Client userToBan = toBan.get(selection - 1);
            String nick     = userToBan.getNick();

            terminal.confirmBan(nick);
            String confirm = sc.nextLine().trim().toUpperCase();
            if (!confirm.equals("BANEAR")) {
                terminal.cancelOperation();
                return;
            }

            // Razón del baneo
            terminal.whyDoYouBannedThisUser(nick);
            String motivo = sc.nextLine().trim();
            if (motivo.isEmpty()) motivo = "Sin motivo especificado";

            // Fijamos datos de baneo
            userToBan.setBanMotive(motivo);
            LocalDateTime ahora = LocalDateTime.now();
            userToBan.setBanDateTime(ahora);

            // 4) Añadimos al fichero de baneos
            banWriter.banUser(userToBan);

            terminal.bannigUser();
            terminal.banned(nick);

        } catch (InputMismatchException e) {
            terminal.noNumberIn();
            sc.nextLine();
        } catch (Exception e) {
            terminal.error();
            e.printStackTrace();
        }
    }
    private void changeNombre(Terminal terminal, Client client) {
        Scanner sc = new Scanner(System.in);
        terminal.showCharacName(client.getCharacter());
        terminal.newAtributeValue();
        String newName = sc.nextLine();
        client.getCharacter().setName(newName);
    }

    private void changeAbility(Terminal terminal, Client client, VampireFactory vampireFactory, HunterFactory hunterFactory, WerewolfFactory werewolfFactory) {
        boolean rightValue;
        terminal.showCharacType(client);
        switch (client.getCharacter().getType()) {
            case "VAMPIRO" -> {
                changeDiscipline(terminal, client, vampireFactory);
            }
            case "CAZADOR" -> {
                changeTalent(terminal, client, hunterFactory);
            }
            case "LICANTROPO" -> {
                changeDon(terminal, client, werewolfFactory);
            }
        }
    }

    private void changeDon(Terminal terminal, Client client, WerewolfFactory werewolfFactory) {
        boolean rightValue;
        Werewolf licantropo = (Werewolf) client.getCharacter();
        Don don = (Don) licantropo.getAbility();
        terminal.showDon(don);
        terminal.askAbilityName();
        werewolfFactory.initializeAbilityName(don);
        licantropo.setRage(0);
        do {
            terminal.askAbilityRage();
            rightValue = werewolfFactory.initializeRageAbility(don);
        } while (!rightValue);
        werewolfFactory.setAbility(licantropo, don);
    }

    private void changeTalent(Terminal terminal, Client client, HunterFactory hunterFactory) {
        boolean rightValue;
        Hunter hunter = (Hunter) client.getCharacter();
        Talent talent = (Talent) hunter.getAbility();
        terminal.showTalent(talent);
        terminal.askAbilityAge();
        hunterFactory.initializeAbilityAge(talent);
        do {
            terminal.askAbilityAttack();
            rightValue = hunterFactory.initializeAbilityAttack(talent);
        } while (!rightValue);
        do {
            terminal.askAbilityDefence();
            rightValue = hunterFactory.initializeAbilityDefense(talent);
        } while (!rightValue);
        terminal.askAbilityAge();
        hunterFactory.initializeAbilityAge(talent);
        hunterFactory.setAbility(hunter, talent);
    }

    private void changeDiscipline(Terminal terminal, Client client, VampireFactory vampireFactory) {
        boolean rightValue;
        Vampire vampire = (Vampire) client.getCharacter();
        Discipline discipline = (Discipline) vampire.getAbility();
        terminal.showDiscipline(discipline);
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

    private void changeWeapons(Scanner sc, Terminal terminal, Client client, HunterFactory hunterFactory) {
        int opcion;
        boolean rightValue;
        //modificar Weapons
        ArrayList<Weapon> Weapons = client.getCharacter().getWeapons();
        ArrayList<Weapon> WeaponsActivas = client.getCharacter().getActiveWeapons();
        terminal.characWeapons(Weapons);
        boolean salir = false;
        do {
            terminal.modifyWeapon();
            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {
                    //aÃ±adir Weapons
                    int numWeapons;
                    do {
                        terminal.askNumWeapons();
                        numWeapons = sc.nextInt();
                    } while (numWeapons < 1);
                    for (int iterator = 1; iterator <= numWeapons; iterator++) {
                        Weapon Weapon = new Weapon();
                        terminal.askWeapName();
                        hunterFactory.initializeWeaponName(Weapon);
                        do {
                            terminal.askWeapAttack();
                            rightValue = hunterFactory.initializeWeaponAttack(Weapon);
                        } while (!rightValue);
                        do {
                            terminal.askWeapDefence();
                            rightValue = hunterFactory.initializeWeaponDefense(Weapon);
                        } while (!rightValue);
                        do {
                            terminal.isWeaponSingleHanded();
                            rightValue = hunterFactory.initializeWeaponSingleHand(Weapon);
                        } while (!rightValue);
                        hunterFactory.addWeapon(Weapons, Weapon);
                    }
                }
                case 2 -> {//eliminar arma
                    int Weapon;
                    do {
                        terminal.askWeapToDelete();
                        Weapon = sc.nextInt();
                    } while (Weapon < 1 && Weapon > Weapons.size() + 1);
                    if (Weapons.get(Weapon - 1).getName().equals(WeaponsActivas.get(0).getName()) ||
                            Weapons.get(Weapon - 1).getName().equals(WeaponsActivas.get(1).getName())) {
                        terminal.errorWeaponIsActive();
                    } else {
                        Weapons.remove(Weapon - 1);
                    }
                }
                case 3 -> {//salir
                    terminal.exit();
                    salir = true;
                }
            }
        } while (!salir);
    }

    private void changeActiveWeapons(Terminal terminal, Client client, VampireFactory vampireFactory, HunterFactory HunterFactory, WerewolfFactory werewolfFactory) {
        ArrayList<Weapon> Weapons;
        ArrayList<Weapon> WeaponsActivas;
        boolean rightValue;
        //modificar Weapons activas
        boolean[] rightWeapon;
        boolean[] aux1 = new boolean[]{true, true};
        boolean[] aux2 = new boolean[]{true, false};
        Weapons = client.getCharacter().getWeapons();
        WeaponsActivas = new ArrayList<>();
        do {
            terminal.showWeapons(Weapons);
            rightWeapon = HunterFactory.addActiveWeapon(Weapons, WeaponsActivas);
        } while (!Arrays.equals(rightWeapon, aux1) && !Arrays.equals(rightWeapon, aux2));
        if (Arrays.equals(rightWeapon, aux1)) {
            do {
                terminal.anotherWeapon(Weapons, WeaponsActivas.get(0));
                rightValue = HunterFactory.addActiveWeapon2(Weapons, WeaponsActivas);
                if (!rightValue) {
                    terminal.invalidValue();
                }
            } while (!rightValue);
        }
        switch (client.getCharacter().getType()) {
            case "VAMPIRO" -> {
                Vampire vampire = (Vampire) client.getCharacter();
                vampireFactory.setActiveWeapons(vampire, WeaponsActivas);
            }
            case "CAZADOR" -> {
                Hunter hunter = (Hunter) client.getCharacter();
                HunterFactory.setActiveWeapons(hunter, WeaponsActivas);
            }
            case "LICANTROPO" -> {
                Werewolf werewolf = (Werewolf) client.getCharacter();
                werewolfFactory.setActiveWeapons(werewolf, WeaponsActivas);
            }
        }
    }

    private void changeArmors(Scanner sc, Terminal terminal, Client client, HunterFactory hunterFactory) {
        int opcion;
        boolean rightValue;
        boolean salir;
        //modificar Weaponduras
        ArrayList<Armor> Weaponduras = client.getCharacter().getArmors();
        Armor WeapondurasActivas = client.getCharacter().getActiveArmor();
        terminal.characterArmors(Weaponduras);
        salir = false;
        do {
            terminal.modifyArmor();
            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {//añadir armaduras
                    int numWeaponduras;
                    do {
                        terminal.askNumArmors();
                        numWeaponduras = sc.nextInt();
                    } while (numWeaponduras < 1);
                    for (int iterator = 1; iterator <= numWeaponduras; iterator++) {
                        Armor Armor = new Armor();
                        terminal.askNameArmors();
                        hunterFactory.initializeArmorName(Armor);
                        do {
                            terminal.askForDefenceArmor();
                            rightValue = hunterFactory.initializeArmorDefense(Armor);
                        } while (!rightValue);
                        do {
                            terminal.askForAttackeArmor();
                            rightValue = hunterFactory.initializeArmorAttack(Armor);
                        } while (!rightValue);
                        hunterFactory.addArmor(Armor, Weaponduras);
                    }
                }
                case 2 -> {//eliminar Armaduras
                    int Weapondura;
                    do {
                        terminal.askArmorToDelete();
                        Weapondura = sc.nextInt();
                    } while (Weapondura < 1 && Weapondura > Weaponduras.size() + 1);
                    if (Weaponduras.get(Weapondura - 1).getName().equals(WeapondurasActivas.getName())) {
                        terminal.errorActiveArmor();
                    } else {
                        Weaponduras.remove(Weapondura - 1);
                    }
                }
                case 3 -> {//salir
                    terminal.exit();
                    salir = true;
                }
            }
        } while (!salir);
    }

    private void changeActiveArmors(Terminal terminal, Client client) {
        boolean rightValue;
        do {
            terminal.showArmors(client.getCharacter().getArmors());
            rightValue = addActiveArmor(client.getCharacter(), client.getCharacter().getArmors());
        } while (!rightValue);
    }

    private boolean addActiveArmor(Character personaje, ArrayList<Armor> armors) {
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        if ((opcion < 1) || (opcion > armors.size() + 1)) {
            return false;
        }
        Armor armor = armors.get(opcion - 1);
        personaje.setActiveArmor(armor);
        return true;
    }

    private void changeMinions(Scanner sc, Terminal terminal, Client client) {
        int opcion;
        boolean salir;
        ArrayList<MinionsComposit> esbirros = client.getCharacter().getMinions();
        terminal.characMinions(esbirros);
        salir = false;
        do {
            terminal.modifyMinions();
            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {//añadir esbirros
                    int numEsbirros;
                    do {
                        terminal.askForMinionsNum();
                        numEsbirros = sc.nextInt();
                    } while (numEsbirros < 1);
                    for (int iterator = 1; iterator <= numEsbirros; iterator++) {
                        MinionsComposit nuevoEsbirro = new MinionsComposit();
                        if (client.getCharacter().getType().equals("VAMPIRO")) {
                            nuevoEsbirro = nuevoEsbirro.createMinion(true);
                        } else {
                            nuevoEsbirro = nuevoEsbirro.createMinion(false);
                        }
                        esbirros.add(nuevoEsbirro);
                    }
                }
                case 2 -> {//eliminar esbirros
                    int esbirro;
                    do {
                        terminal.askMinionToDelete();
                        esbirro = sc.nextInt();
                    } while (esbirro < 1 && esbirro > esbirros.size() + 1);
                    esbirros.remove(esbirro - 1);
                }
                case 3 -> { //salir
                    terminal.exit();
                    salir = true;
                }
            }
        } while (!salir);
    }

    private void changeGold(Scanner sc, Terminal terminal, Client client) {
        terminal.showGold(client);
        terminal.newAtributeValue();
        int oro = sc.nextInt();
        client.getCharacter().setGold(oro);
    }

    private void changeHealth(Scanner sc, Terminal terminal, Client client) {
        terminal.showHp(client);
        terminal.newAtributeValue();
        int hp = sc.nextInt();
        client.getCharacter().setHealth(hp);
    }

    private void changePower(Scanner sc, Terminal terminal, Client client) {
        terminal.showPower(client);
        terminal.newAtributeValue();
        int poder = sc.nextInt();
        client.getCharacter().setPower(poder);
    }

    private void changeType(Scanner sc, Terminal terminal, Client client, VampireFactory VampireFactory, HunterFactory HunterFactory, WerewolfFactory WerewolfFactory) {
        int opcion;
        boolean rightValue;
        rightValue = false;
        terminal.showType();
        terminal.selectType();
        opcion = sc.nextInt();
        switch (opcion) {
            case 1 -> {
                client.getCharacter().setType("VAMPIRO");
                changeDiscipline(terminal, client, VampireFactory);
            }
            case 2 -> {
                client.getCharacter().setType("CAZADOR");
                changeTalent(terminal, client, HunterFactory);
            }
            case 3 -> {
                client.getCharacter().setType("LICANTROPO");
                changeDon(terminal, client, WerewolfFactory);
            }
        }
    }

    private void changeStrengths(Scanner sc, Terminal terminal, Client client, HunterFactory HunterFactory) {
        int opcion;
        boolean salir;
        //modificar fortalezas
        ArrayList<Strength> fortalezas = client.getCharacter().getStrengths();
        salir = false;
        do {
            terminal.characStrengths(fortalezas);
            terminal.modifyStrengths();
            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {//añadir fortalezas
                    int numFortalezas;
                    do {
                        terminal.askNumStrengths();
                        numFortalezas = sc.nextInt();
                    } while (numFortalezas < 1);
                    for (int iterator = 1; iterator <= numFortalezas; iterator++) {
                        Strength newStrength = new Strength();
                        terminal.askStrengthName();
                        HunterFactory.initializeStrengthName(newStrength);
                        terminal.askStrengthValue();
                        HunterFactory.initializeStrengthValue(newStrength);
                        HunterFactory.addStrength(fortalezas, newStrength);
                    }
                }
                case 2 -> {//eliminar fortalezas
                    int fortaleza;
                    do {
                        terminal.askArmorToDelete();
                        fortaleza = sc.nextInt();
                    } while (fortaleza < 1 && fortaleza > fortalezas.size() + 1);
                    fortalezas.remove(fortaleza - 1);
                }
                case 3 -> {//salir
                    terminal.exit();
                    salir = true;
                }
            }
        } while (!salir);
    }

    private void changeWaknesses(Scanner sc, Terminal terminal, Client client, HunterFactory HunterFactory) {
        int opcion;
        boolean salir;
        ArrayList<Weakness> weaknesses = client.getCharacter().getWeaknesses();
        terminal.characWeakness(weaknesses);
        salir = false;
        do {
            terminal.modifyWeaknesses();
            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {//añadir debilidades
                    int numDebilidades;
                    do {
                        terminal.askNumWeakness();
                        numDebilidades = sc.nextInt();
                    } while (numDebilidades < 1);
                    for (int iterator = 1; iterator <= numDebilidades; iterator++) {
                        Weakness newWeakness = new Weakness();
                        terminal.askWeaknessName();
                        HunterFactory.initializeWeaknessName(newWeakness);
                        terminal.askWeaknessValue();
                        HunterFactory.initializeWeaknessValue(newWeakness);
                        HunterFactory.addWeakness(weaknesses, newWeakness);
                    }
                }
                case 2 -> {//eliminar debilidades
                    int debilidad;
                    do {
                        terminal.askArmorToDelete();
                        debilidad = sc.nextInt();
                    } while (debilidad < 1 && debilidad > weaknesses.size() + 1);
                    weaknesses.remove(debilidad - 1);
                }
                case 3 -> {//salir
                    terminal.exit();
                    salir = true;
                }
            }
        } while (!salir);
    }
}
