package System;

import Entities.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class UserFileWriter {

    private static final String USER_FILE_PATH = "Fase-3-MP/src/Files/UserRegister.txt";

    /**A continuación se definen las operaciones de escrituras**/
    public void userRegister(Client client) {
        try {
            File file = new File(USER_FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("========== USUARIO ==========");
            bw.newLine();
            bw.write("NOMBRE ");
            bw.write(client.getName());
            bw.newLine();
            bw.write("NICK ");
            bw.write(client.getNick());
            bw.newLine();
            bw.write("PASSWORD ");
            bw.write(client.getPassword());
            bw.newLine();
            bw.write("REGISTRO ");
            bw.write(client.getRegister());
            bw.newLine();
            bw.write("TIPO-PERSONAJE null");
            bw.newLine();
            bw.write("========== FIN USUARIO ==========");
            bw.newLine();

            bw.close();
        } catch (Exception e) {
            MainSystem system = new MainSystem();
            system.selector();
            e.printStackTrace();
        }
    }

    public void rewriteUserFile(ArrayList<Client> clientArrayList) {
        try {
            File file = new File(USER_FILE_PATH);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            //recorre la lista de usuarios
            for (Client client : clientArrayList) {

                bw.write("========== USUARIO ==========");
                bw.newLine();
                bw.write("NOMBRE ");
                bw.write(client.getName());
                bw.newLine();
                bw.write("NICK ");
                bw.write(client.getNick());
                bw.newLine();
                bw.write("PASSWORD ");
                bw.write(client.getPassword());
                bw.newLine();
                bw.write("REGISTRO ");
                bw.write(client.getRegister());
                bw.newLine();
                if (client.getCharacter() == null) {
                    bw.write("TIPO-PERSONAJE null");
                    bw.newLine();
                    bw.write("========== FIN USUARIO ==========");
                    bw.newLine();
                } else {
                    String characterType = client.getCharacter().getType();
                    switch (characterType) {
                       case "VAMPIRO" -> vampireWriter(clientArrayList, clientArrayList.indexOf(client), bw); //escribimos y guardamos los atributos de los vampiros
                       case "LICANTROPO" -> werewolfWriter(clientArrayList, clientArrayList.indexOf(client), bw); //idem.
                       case "CAZADOR" -> hunterWriter(clientArrayList, clientArrayList.indexOf(client), bw); //idem.

                    }
                }
            }
            bw.close();
        } catch (Exception exception) {
            MainSystem system = new MainSystem();
            system.selector();
            exception.printStackTrace();
        }
    }

    public void vampireWriter(ArrayList<Client> listaCliente, int i, BufferedWriter bw) throws IOException {

        Vampire vampiro = (Vampire) listaCliente.get(i).getCharacter();
        Discipline disciplina = (Discipline) vampiro.getAbility();

        //TIPO PERSONAJE
        bw.write("TIPO-PERSONAJE ");
        bw.write(listaCliente.get(i).getCharacter().getType());
        bw.newLine();
        //NOMBRE PERSONAJE
        bw.write("NOMBRE-PERSONAJE ");
        bw.write(listaCliente.get(i).getCharacter().getName());
        bw.newLine();
        //PUNTOS DE SANGRE
        bw.write("SANGRE ");
        bw.write(String.valueOf(vampiro.getBlood()));
        bw.newLine();
        //NOMBRE DE HABILIDAD
        bw.write("NOMNRE-HABILIDAD ");
        bw.write(disciplina.getName());
        bw.newLine();

        //VALOR ATAQUE
        bw.write("VALOR-ATAQUE ");
        bw.write(String.valueOf(disciplina.getAttack()));
        bw.newLine();

        //VALOR DEFENSA
        bw.write("VALOR-DEFENSA ");
        bw.write(String.valueOf(disciplina.getDefense()));
        bw.newLine();

        //COSTE HABILIDAD
        bw.write("COSTE-HABILIDAD ");
        bw.write(String.valueOf(disciplina.getCost()));
        bw.newLine();

        //ARMAS
        bw.write("NUMERO-ARMAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getWeapons().size()));
        bw.newLine();

        for (int variableArma = 0; variableArma < (listaCliente.get(i).getCharacter().getWeapons().size()); variableArma++) {
            Weapon arma = vampiro.getWeapons().get(variableArma);
            bw.write("NOMBRE-ARMA ");
            bw.write(arma.getName());
            bw.newLine();

            bw.write("ATAQUE-ARMA ");
            bw.write(String.valueOf(arma.getAttackModifier()));
            bw.newLine();

            bw.write("DEFENSA-ARMA ");
            bw.write(String.valueOf(arma.getDefenseModifier()));
            bw.newLine();

            //si es true es de 1 mano, si es false es de dos manos
            bw.write("EMPUÑADURA ");
            if (arma.isSingleHand()) {
                bw.write("true");
            } else {
                bw.write("false");
            }
            bw.newLine();

        }
        bw.newLine();

        //NUMERO DE ARMAS ACTIVAS
        bw.write("NUMERO-ARMAS-ACTIVAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getActiveWeapons().size()));
        bw.newLine();
        for (int variableArmaActiva = 0; variableArmaActiva < (listaCliente.get(i).getCharacter().getActiveWeapons().size()); variableArmaActiva++) {
            Weapon armaActiva = vampiro.getActiveWeapons().get(variableArmaActiva);

            bw.write("NOMBRE-ARMAS-ACTIVAS ");
            bw.write(armaActiva.getName());
            bw.newLine();

            bw.write("ATAQUE-ARMA-ACTIVAS ");
            bw.write(String.valueOf(armaActiva.getAttackModifier()));
            bw.newLine();

            bw.write("DEFENSA-ARMA-ACTIVAS ");
            bw.write(String.valueOf(armaActiva.getDefenseModifier()));
            bw.newLine();

            //si es true es de 1 mano, si es false es de dos manos
            bw.write("EMPUÑADURA ");
            if (armaActiva.isSingleHand()) {
                bw.write("true");
            } else {
                bw.write("false");
            }
            bw.newLine();
        }
        bw.newLine();

        //ARMADURAS
        //NUMERO DE ARMADURAS
        bw.write("NUMERO-ARMADURAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getArmors().size()));
        bw.newLine();
        for (int j = 0; j < (listaCliente.get(i).getCharacter().getArmors().size()); j++) {
            Armor armadura = (Armor) vampiro.getArmors().get(j);
            bw.write("NOMBRE-ARMADURA ");
            bw.write(armadura.getName());
            bw.newLine();

            bw.write("DEFENSA-ARMADURA ");
            bw.write(String.valueOf(armadura.getDefenseModifier()));
            bw.newLine();

            bw.write("ATAQUE-ARMADURA ");
            bw.write(String.valueOf(armadura.getAttackModifier()));
            bw.newLine();
        }
        bw.newLine();

        bw.write("NOMBRE-ARMADURA-ACTIVA ");
        bw.write(vampiro.getActiveArmor().getName());
        bw.newLine();

        bw.write("DEFENSA-ARMADURA-ACTIVA ");
        bw.write(String.valueOf(vampiro.getActiveArmor().getDefenseModifier()));
        bw.newLine();

        bw.write("ATAQUE-ARMADURA-ACTIVA ");
        bw.write(String.valueOf(vampiro.getActiveArmor().getAttackModifier()));
        bw.newLine();

        bw.newLine();

        //ORO
        bw.write("ORO ");
        bw.write(String.valueOf(vampiro.getGold()));
        bw.newLine();

        //EDAD VAMPIRO
        bw.write("EDAD-VAMPIRO ");
        bw.write(String.valueOf(vampiro.getAge()));
        bw.newLine();

        bw.write("HP ");
        bw.write(String.valueOf(vampiro.getHealth()));
        bw.newLine();

        bw.write("PODER ");
        bw.write(String.valueOf(vampiro.getPower()));
        bw.newLine();

        bw.write("NUMERO-FORTALEZAS ");
        bw.write(String.valueOf(vampiro.getStrengths().size()));
        bw.newLine();

        for (int j = 0; j < (listaCliente.get(i).getCharacter().getStrengths().size()); j++) {
            Strength fortaleza = listaCliente.get(i).getCharacter().getStrengths().get(j);
            bw.write("NOMBRE-FORTALEZA ");
            bw.write(fortaleza.getName());
            bw.newLine();

            bw.write("VALOR-FORTALEZA ");
            bw.write(String.valueOf(fortaleza.getValue()));
            bw.newLine();
        }

        bw.write("NUMERO-DEBILIDADES ");
        bw.write(String.valueOf(vampiro.getWeaknesses().size()));
        bw.newLine();

        for (int j = 0; j < (listaCliente.get(i).getCharacter().getWeaknesses().size()); j++) {
            Weakness debilidad = listaCliente.get(i).getCharacter().getWeaknesses().get(j);
            bw.write("NOMBRE-DEBILIDAD ");
            bw.write(debilidad.getName());
            bw.newLine();

            bw.write("VALOR-DEBILIDAD ");
            bw.write(String.valueOf(debilidad.getValue()));
            bw.newLine();
        }

        bw.write("NUMERO-ESBIRROS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getMinions().size()));
        bw.newLine();

        for(MinionsComposit minion: vampiro.getMinions()){
            minionsWriter(listaCliente, i, minion, bw);
        }

        bw.write("========== FIN USUARIO ==========");
        bw.newLine();
    }

    public void werewolfWriter(ArrayList<Client> listaCliente, int i, BufferedWriter bw) throws IOException {

        Werewolf werewolf = (Werewolf) listaCliente.get(i).getCharacter();
        Don don = (Don) werewolf.getAbility();

        // TIPO PERSONAJE
        bw.write("TIPO-PERSONAJE ");
        bw.write(listaCliente.get(i).getCharacter().getType());
        bw.newLine();
        // NOMBRE PERSONAJE
        bw.write("NOMBRE-PERSONAJE ");
        bw.write(listaCliente.get(i).getCharacter().getName());
        bw.newLine();

        // NOMBRE DE HABILIDAD
        bw.write("NOMBRE-HABILIDAD ");
        bw.write(don.getName());
        bw.newLine();

        // RABIA
        bw.write("RABIA ");
        bw.write(String.valueOf(werewolf.getRage()));
        bw.newLine();

        // ATAQUE DON
        bw.write("ATAQUE-DON ");
        bw.write(String.valueOf(don.getAttack()));
        bw.newLine();

        // DEFENSA DON
        bw.write("DEFENSA-DON ");
        bw.write(String.valueOf(don.getDefense()));
        bw.newLine();

        // ARMAS
        bw.write("NUMERO-ARMAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getWeapons().size()));
        bw.newLine();

        for (int variableArma = 0; variableArma < (listaCliente.get(i).getCharacter().getWeapons().size()); variableArma++) {
            Weapon arma = werewolf.getWeapons().get(variableArma);
            bw.write("NOMBRE-ARMA ");
            bw.write(arma.getName());
            bw.newLine();

            bw.write("ATAQUE-ARMA ");
            bw.write(String.valueOf(arma.getAttackModifier()));
            bw.newLine();

            bw.write("DEFENSA-ARMA ");
            bw.write(String.valueOf(arma.getDefenseModifier()));
            bw.newLine();

            // si es true es de 1 mano, si es false es de dos manos
            bw.write("EMPUÑADURA ");
            if (arma.isSingleHand()) {
                bw.write("true");
            } else {
                bw.write("false");
            }
            bw.newLine();
        }
        bw.newLine();

        // NUMERO DE ARMAS ACTIVAS
        bw.write("NUMERO-ARMAS-ACTIVAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getActiveWeapons().size()));
        bw.newLine();
        for (int variableArmaActiva = 0; variableArmaActiva < (listaCliente.get(i).getCharacter().getActiveWeapons().size()); variableArmaActiva++) {
            Weapon armaActiva = werewolf.getActiveWeapons().get(variableArmaActiva);

            bw.write("NOMBRE-ARMAS-ACTIVAS ");
            bw.write(armaActiva.getName());
            bw.newLine();

            bw.write("ATAQUE-ARMA-ACTIVAS ");
            bw.write(String.valueOf(armaActiva.getAttackModifier()));
            bw.newLine();

            bw.write("DEFENSA-ARMA-ACTIVAS ");
            bw.write(String.valueOf(armaActiva.getDefenseModifier()));
            bw.newLine();

            // si es true es de 1 mano, si es false es de dos manos
            bw.write("EMPUÑADURA ");
            if (armaActiva.isSingleHand()) {
                bw.write("true");
            } else {
                bw.write("false");
            }
            bw.newLine();
        }
        bw.newLine();

        // ARMADURAS
        // NUMERO DE ARMADURAS
        bw.write("NUMERO-ARMADURAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getArmors().size()));
        bw.newLine();
        for (int j = 0; j < (listaCliente.get(i).getCharacter().getArmors().size()); j++) {
            Armor armadura = (Armor) werewolf.getArmors().get(j);
            bw.write("NOMBRE-ARMADURA ");
            bw.write(armadura.getName());
            bw.newLine();

            bw.write("DEFENSA-ARMADURA ");
            bw.write(String.valueOf(armadura.getDefenseModifier()));
            bw.newLine();

            bw.write("ATAQUE-ARMADURA ");
            bw.write(String.valueOf(armadura.getAttackModifier()));
            bw.newLine();
        }
        bw.newLine();

        bw.write("NOMBRE-ARMADURA-ACTIVA ");
        bw.write(werewolf.getActiveArmor().getName());
        bw.newLine();

        bw.write("DEFENSA-ARMADURA-ACTIVA ");
        bw.write(String.valueOf(werewolf.getActiveArmor().getDefenseModifier()));
        bw.newLine();

        bw.write("ATAQUE-ARMADURA-ACTIVA ");
        bw.write(String.valueOf(werewolf.getActiveArmor().getAttackModifier()));
        bw.newLine();

        bw.newLine();

        // ORO
        bw.write("ORO ");
        bw.write(String.valueOf(werewolf.getGold()));
        bw.newLine();

        // HP
        bw.write("HP ");
        bw.write(String.valueOf(werewolf.getHealth()));
        bw.newLine();

        // PODER
        bw.write("PODER ");
        bw.write(String.valueOf(werewolf.getPower()));
        bw.newLine();

        bw.write("NUMERO-DEBILIDADES ");
        bw.write(String.valueOf(werewolf.getWeaknesses().size()));
        bw.newLine();

        for (int j = 0; j < (listaCliente.get(i).getCharacter().getWeaknesses().size()); j++) {
            Weakness debilidad = listaCliente.get(i).getCharacter().getWeaknesses().get(j);
            bw.write("NOMBRE-DEBILIDAD ");
            bw.write(debilidad.getName());
            bw.newLine();

            bw.write("VALOR-DEBILIDAD ");
            bw.write(String.valueOf(debilidad.getValue()));
            bw.newLine();
        }

        bw.write("NUMERO-FORTALEZAS ");
        bw.write(String.valueOf(werewolf.getStrengths().size()));
        bw.newLine();

        for (int j = 0; j < (listaCliente.get(i).getCharacter().getStrengths().size()); j++) {
            Strength fortaleza = listaCliente.get(i).getCharacter().getStrengths().get(j);
            bw.write("NOMBRE-FORTALEZA ");
            bw.write(fortaleza.getName());
            bw.newLine();

            bw.write("VALOR-FORTALEZA ");
            bw.write(String.valueOf(fortaleza.getValue()));
            bw.newLine();
        }

        // ESBIRROS
        // NUMERO DE ESBIRROS
        bw.write("NUMERO-ESBIRROS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getMinions().size()));
        bw.newLine();

        // ESBIRROS
        for(MinionsComposit minion: werewolf.getMinions()) {
            minionsWriter(listaCliente, i, minion, bw);
        }

        bw.write("========== FIN USUARIO ==========");
        bw.newLine();
    }

    public void hunterWriter(ArrayList<Client> listaCliente, int i, BufferedWriter bw) throws IOException {

        Hunter hunter = (Hunter) listaCliente.get(i).getCharacter();
        Talent talent = (Talent) hunter.getAbility();

        // TIPO PERSONAJE
        bw.write("TIPO-PERSONAJE ");
        bw.write(listaCliente.get(i).getCharacter().getType());
        bw.newLine();
        // NOMBRE PERSONAJE
        bw.write("NOMBRE-PERSONAJE ");
        bw.write(listaCliente.get(i).getCharacter().getName());
        bw.newLine();
        // VOLUNTAD
        bw.write("VOLUNTAD ");
        bw.write(String.valueOf(hunter.getWillpower()));
        bw.newLine();
        // NOMBRE DE HABILIDAD
        bw.write("NOMBRE-HABILIDAD ");
        bw.write(talent.getName());
        bw.newLine();

        // VALOR ATAQUE
        bw.write("VALOR-ATAQUE ");
        bw.write(String.valueOf(talent.getAttack()));
        bw.newLine();

        // VALOR DEFENSA
        bw.write("VALOR-DEFENSA ");
        bw.write(String.valueOf(talent.getDefense()));
        bw.newLine();

        // EDAD
        bw.write("EDAD ");
        bw.write(String.valueOf(talent.getAge()));
        bw.newLine();

        // ARMAS
        bw.write("NUMERO-ARMAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getWeapons().size()));
        bw.newLine();

        for (int variableArma = 0; variableArma < (listaCliente.get(i).getCharacter().getWeapons().size()); variableArma++) {
            Weapon arma = hunter.getWeapons().get(variableArma);
            bw.write("NOMBRE-ARMA ");
            bw.write(arma.getName());
            bw.newLine();

            bw.write("ATAQUE-ARMA ");
            bw.write(String.valueOf(arma.getAttackModifier()));
            bw.newLine();

            bw.write("DEFENSA-ARMA ");
            bw.write(String.valueOf(arma.getDefenseModifier()));
            bw.newLine();

            // si es true es de 1 mano, si es false es de dos manos
            bw.write("EMPUÑADURA ");
            if (arma.isSingleHand()) {
                bw.write("true");
            } else {
                bw.write("false");
            }
            bw.newLine();
        }
        bw.newLine();

        // NUMERO DE ARMAS ACTIVAS
        bw.write("NUMERO-ARMAS-ACTIVAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getActiveWeapons().size()));
        bw.newLine();
        for (int variableArmaActiva = 0; variableArmaActiva < (listaCliente.get(i).getCharacter().getActiveWeapons().size()); variableArmaActiva++) {
            Weapon armaActiva = hunter.getActiveWeapons().get(variableArmaActiva);

            bw.write("NOMBRE-ARMAS-ACTIVAS ");
            bw.write(armaActiva.getName());
            bw.newLine();

            bw.write("ATAQUE-ARMA-ACTIVAS ");
            bw.write(String.valueOf(armaActiva.getAttackModifier()));
            bw.newLine();

            bw.write("DEFENSA-ARMA-ACTIVAS ");
            bw.write(String.valueOf(armaActiva.getDefenseModifier()));
            bw.newLine();

            // si es true es de 1 mano, si es false es de dos manos
            bw.write("EMPUÑADURA ");
            if (armaActiva.isSingleHand()) {
                bw.write("true");
            } else {
                bw.write("false");
            }
            bw.newLine();
        }
        bw.newLine();

        // ARMADURAS
        // NUMERO DE ARMADURAS
        bw.write("NUMERO-ARMADURAS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getArmors().size()));
        bw.newLine();
        for (int j = 0; j < (listaCliente.get(i).getCharacter().getArmors().size()); j++) {
            Armor armadura = (Armor) hunter.getArmors().get(j);
            bw.write("NOMBRE-ARMADURA ");
            bw.write(armadura.getName());
            bw.newLine();

            bw.write("DEFENSA-ARMADURA ");
            bw.write(String.valueOf(armadura.getDefenseModifier()));
            bw.newLine();

            bw.write("ATAQUE-ARMADURA ");
            bw.write(String.valueOf(armadura.getAttackModifier()));
            bw.newLine();
        }
        bw.newLine();

        bw.write("NOMBRE-ARMADURA-ACTIVA ");
        bw.write(hunter.getActiveArmor().getName());
        bw.newLine();

        bw.write("DEFENSA-ARMADURA-ACTIVA ");
        bw.write(String.valueOf(hunter.getActiveArmor().getDefenseModifier()));
        bw.newLine();

        bw.write("ATAQUE-ARMADURA-ACTIVA ");
        bw.write(String.valueOf(hunter.getActiveArmor().getAttackModifier()));
        bw.newLine();

        bw.newLine();

        // ORO
        bw.write("ORO ");
        bw.write(String.valueOf(hunter.getGold()));
        bw.newLine();

        // HP
        bw.write("HP ");
        bw.write(String.valueOf(hunter.getHealth()));
        bw.newLine();

        // PODER
        bw.write("PODER ");
        bw.write(String.valueOf(hunter.getPower()));
        bw.newLine();

        bw.write("NUMERO-FORTALEZAS ");
        bw.write(String.valueOf(hunter.getStrengths().size()));
        bw.newLine();

        for (int j = 0; j < (listaCliente.get(i).getCharacter().getStrengths().size()); j++) {
            Strength fortaleza = listaCliente.get(i).getCharacter().getStrengths().get(j);
            bw.write("NOMBRE-FORTALEZA ");
            bw.write(fortaleza.getName());
            bw.newLine();

            bw.write("VALOR-FORTALEZA ");
            bw.write(String.valueOf(fortaleza.getValue()));
            bw.newLine();
        }

        bw.write("NUMERO-DEBILIDADES ");
        bw.write(String.valueOf(hunter.getWeaknesses().size()));
        bw.newLine();

        for (int j = 0; j < (listaCliente.get(i).getCharacter().getWeaknesses().size()); j++) {
            Weakness debilidad = listaCliente.get(i).getCharacter().getWeaknesses().get(j);
            bw.write("NOMBRE-DEBILIDAD ");
            bw.write(debilidad.getName());
            bw.newLine();

            bw.write("VALOR-DEBILIDAD ");
            bw.write(String.valueOf(debilidad.getValue()));
            bw.newLine();
        }

        // ESBIRROS
        // NUMERO DE ESBIRROS
        bw.write("NUMERO-ESBIRROS ");
        bw.write(String.valueOf(listaCliente.get(i).getCharacter().getMinions().size()));
        bw.newLine();

        // ESBIRROS
        for(MinionsComposit minion: hunter.getMinions()){
            minionsWriter(listaCliente, i, minion, bw);
        }

        bw.write("========== FIN USUARIO ==========");
        bw.newLine();
    }

    private void minionsWriter(ArrayList<Client> clientArrayList, int i, MinionsComposit minion, BufferedWriter bw) throws IOException {

        String tipo = minion.getType();
        bw.write("TIPO-DE-ESBIRRO " + tipo);
        bw.newLine();
        bw.write("NOMBRE-DEL-ESBIRRO " + minion.getName());
        bw.newLine();
        bw.write("VIDA-DEL-ESBIRRO " + minion.getHp());
        bw.newLine();

        switch (tipo) {
            case "HUMANO" -> {
                Human human = (Human) minion;
                bw.write("LEALTAD " + human.getLoyalty());
                bw.newLine();
            }
            case "GHOUL" -> {
                Ghoul ghoul = (Ghoul) minion;
                bw.write("DEPENDENCIA " + ghoul.getDependency());
                bw.newLine();
            }
            case "DEMONIO" -> {
                Demon demon = (Demon) minion;
                bw.write("DESCRIPCION " + demon.getDescription());
                bw.newLine();
                bw.write("NUMERO-DE-ESBIRROS-EXTRA " + ((Demon) minion).getMinionsComposits().size());
                bw.newLine();
                for (MinionsComposit minionList: demon.getMinionsComposits()) {
                    minionsWriter(clientArrayList, i, minionList, bw); // llamada recursiva
                }
            }
        }
    }
}