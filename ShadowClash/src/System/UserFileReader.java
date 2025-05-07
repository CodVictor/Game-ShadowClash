package System;

import Entities.*;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserFileReader {

    private static final String USER_FILE_PATH = "Fase-3-MP/src/Files/UserRegister.txt";

    public ArrayList<Client> userFileReader() {

        FileReader fr = null;
        ArrayList<Client> listaCliente = new ArrayList<>();

        try {
            File archivo = new File(USER_FILE_PATH);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            br.readLine();

            linea = br.readLine();

            while (linea != null) {
                Client cliente = new Client();

                //NOMBRE
                String[] textoSeparado = linea.split(" ");
                cliente.setName(textoSeparado[1]);

                //NICK
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                cliente.setNick(textoSeparado[1]);

                //PASSWORD
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                cliente.setPassword(textoSeparado[1]);

                //NUMERO_REGISTRO
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                cliente.setRegister(textoSeparado[1]);

                //PERSONAJE
                linea = br.readLine();
                textoSeparado = linea.split(" ");

                if (!textoSeparado[1].equals("null")) {
                    //LECTURA SI ES DE TIPO VAMPIRO
                    switch (textoSeparado[1]) {
                        case "VAMPIRO" -> {
                            Vampire vampire = vampireReader(br);
                            cliente.setCharacter(vampire);
                        }
                        //LECTURA SI ES DE TIPO LICANTROPO
                        case "LICANTROPO" -> {
                            Werewolf werewolf = werewolfReader(br);
                            cliente.setCharacter(werewolf);
                        }
                        //LECTURA SI ES DE TIPO CAZADOR
                        case "CAZADOR" -> {
                            Hunter hunter = hunterReader(br);
                            cliente.setCharacter(hunter);
                        }
                    }
                }
                listaCliente.add(cliente);
                br.readLine();
                linea = br.readLine();
                if (linea != null && linea.equals("========== USUARIO ==========")) {
                    linea = br.readLine();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return listaCliente; //devolver la lista de cliente
    }

    public static List<Map.Entry<String, Integer>> goldReaderForRanking(String archivo) {
        List<Map.Entry<String, Integer>> clientes = new ArrayList<>();
        String nombre = null;
        Integer oro = null;
        boolean dentroUsuario = false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.equals("========== USUARIO ==========")) {
                    dentroUsuario = true;
                    nombre = null;
                    oro = null;
                } else if (linea.equals("========== FIN USUARIO ==========")) {
                    if (nombre != null) {
                        clientes.add(new AbstractMap.SimpleEntry<>(nombre, oro));
                    }
                    dentroUsuario = false;
                } else if (dentroUsuario) {
                    if (linea.startsWith("NOMBRE ") && nombre == null) {
                        nombre = linea.substring(7).trim();
                    } else if (linea.startsWith("ORO ")) {
                        try {
                            oro = Integer.parseInt(linea.substring(4).trim());
                        } catch (NumberFormatException e) {
                            oro = null; // por si oro no es un número válido
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    //LECTURA PERSONAJES
    public Vampire vampireReader(BufferedReader br) {
        Vampire vampiro = new Vampire();
        Discipline disciplina = new Discipline();

        FileReader fr = null;
        try {
            // Lectura del fichero
            String linea;
            linea = br.readLine();

            while (!linea.equals("========== FIN USUARIO ==========")) {
                //NOMBRE VAMPIRO
                String[] textoSeparado = linea.split(" ");
                vampiro.setType("VAMPIRO");
                vampiro.setName(textoSeparado[1]);

                //SANGRE VAMPIRO
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                vampiro.setBlood(Integer.parseInt(textoSeparado[1]));

                //NOMBRE HABILIDAD
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                disciplina.setName(textoSeparado[1]);

                //VALOR ATAQUE
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                disciplina.setAttack(Integer.parseInt(textoSeparado[1]));

                //VALOR DEFENSA
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                disciplina.setDefense(Integer.parseInt(textoSeparado[1]));

                //COSTE HABILIDAD
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                disciplina.setCost(Integer.parseInt(textoSeparado[1]));

                vampiro.setAbility(disciplina);

                //NUMERO DE ARMAS
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                ArrayList<Weapon> armas = new ArrayList<>();
                int tope = Integer.parseInt(textoSeparado[1]);
                for (int i = 0; i < tope; i++) {

                    Weapon arma = new Weapon();

                    //NOMBRE ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setName(textoSeparado[1]);

                    //NIVEL ATAQUE ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setAttackModifier((Integer.parseInt(textoSeparado[1])));

                    //NIVEL DEFENSA ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setDefenseModifier((Integer.parseInt(textoSeparado[1])));

                    //EMPUÑADURA DE ARMA: si es de 1 o 2 manos
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setSingleHand(textoSeparado[1].equals("true"));

                    armas.add(arma);
                }
                vampiro.setWeapons(armas);

                ArrayList<Weapon> armasActivas = new ArrayList<>();
                //NUMERO DE ARMAS ACTIVAS
                br.readLine();
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                tope = Integer.parseInt(textoSeparado[1]);
                for (int i = 0; i < tope; i++) {

                    Weapon arma = new Weapon();

                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setName(textoSeparado[1]);

                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setAttackModifier((Integer.parseInt(textoSeparado[1])));

                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setDefenseModifier((Integer.parseInt(textoSeparado[1])));

                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setSingleHand(textoSeparado[1].equals("true"));
                    armasActivas.add(arma);
                }
                vampiro.setActiveWeapons(armasActivas);

                //ARMADURAS
                br.readLine();
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                ArrayList<Armor> armaduras = new ArrayList<>();
                tope = Integer.parseInt(textoSeparado[1]);
                for (int i = 0; i < tope; i++) {

                    Armor armadura = new Armor();

                    //NOMBRE ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    armadura.setName(textoSeparado[1]);

                    //NIVEL DEFENSA ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    armadura.setDefenseModifier((Integer.parseInt(textoSeparado[1])));

                    //NIVEL ATAQUE ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    armadura.setAttackModifier((Integer.parseInt(textoSeparado[1])));

                    armaduras.add(armadura);
                }
                vampiro.setArmors(armaduras);

                br.readLine();
                Armor armadura = new Armor();

                //NOMBRE ARMADURA
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                armadura.setName(textoSeparado[1]);

                //DEFENSA ARMADURA
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                armadura.setDefenseModifier((Integer.parseInt(textoSeparado[1])));

                //ATAQUE ARMADURA
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                armadura.setAttackModifier((Integer.parseInt(textoSeparado[1])));

                vampiro.setActiveArmor(armadura);

                br.readLine();
                //CANTIDAD ORO
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                vampiro.setGold(Integer.parseInt(textoSeparado[1]));

                //EDAD VAMPIRO
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                vampiro.setAge(Integer.parseInt(textoSeparado[1]));

                //CANTIDAD VIDA
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                vampiro.setHealth(Integer.parseInt(textoSeparado[1]));

                //PODER
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                vampiro.setPower(Integer.parseInt(textoSeparado[1]));

                // FORTALEZAS
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                tope = Integer.parseInt(textoSeparado[1]);
                ArrayList<Strength> fortalezas = new ArrayList<>();
                for (int i = 0; i < tope; i++) {

                    Strength fortaleza = new Strength();

                    //NOMBRE FORTALEZA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    fortaleza.setName(textoSeparado[1]);

                    //VALOR FORTALEZA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    fortaleza.setValue((Integer.parseInt(textoSeparado[1])));

                    fortalezas.add(fortaleza);
                }
                vampiro.setStrengths(fortalezas);

                // NUMERO DE DEBILIDADES
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                tope = Integer.parseInt(textoSeparado[1]);
                ArrayList<Weakness> debilidades = new ArrayList<>();
                for (int i = 0; i < tope; i++) {

                    Weakness debilidad = new Weakness();

                    //NOMBRE DE DEBILIADAD
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    debilidad.setName((textoSeparado[1]));

                    //VALOR DEBILIDAD
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    debilidad.setValue((Integer.parseInt(textoSeparado[1])));

                    debilidades.add(debilidad);
                }
                vampiro.setWeaknesses(debilidades);

                //METODO ESBIRRO
                ArrayList<MinionsComposit> listaEsbirros = new ArrayList<>();
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                tope = Integer.parseInt(textoSeparado[1]);
                for (int i = 0; i < tope; i++) {
                    MinionsComposit esbirro = minionsFile(linea, br, textoSeparado);
                    listaEsbirros.add(esbirro);
                }
                vampiro.setMinions(listaEsbirros);
                linea = "========== FIN USUARIO ==========";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally { // En el finally cerramos el fichero
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return vampiro;
    }

    public Hunter hunterReader(BufferedReader br) {
        Hunter hunter = new Hunter();
        Talent talent = new Talent();

        try {
            // Lectura del fichero
            String linea;
            linea = br.readLine();

            while (!linea.equals("========== FIN USUARIO ==========")) {
                // NOMBRE HUNTER
                String[] textoSeparado = linea.split(" ");
                hunter.setType("CAZADOR");
                hunter.setName(textoSeparado[1]);

                // VOLUNTAD
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                hunter.setWillpower(Integer.parseInt(textoSeparado[1]));

                // Nombre TALENTO
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                talent.setName(textoSeparado[1]);

                // ATAQUE TALENTO
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                talent.setAttack(Integer.parseInt(textoSeparado[1]));

                // DEFENSA TALENTO
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                talent.setDefense(Integer.parseInt(textoSeparado[1]));

                // EDAD TALENTO
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                talent.setAge(Integer.parseInt(textoSeparado[1]));

                hunter.setAbility(talent);

                // ARMAS
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                ArrayList<Weapon> armas = new ArrayList<>();
                int tope = Integer.parseInt(textoSeparado[1]);
                for (int i = 0; i < tope; i++) {
                    Weapon arma = new Weapon();

                    // NOMBRE ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setName(textoSeparado[1]);

                    // ATAQUE ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setAttackModifier(Integer.parseInt(textoSeparado[1]));

                    // DEFENSA ARMA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setDefenseModifier(Integer.parseInt(textoSeparado[1]));

                    // EMPUÑADURA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setSingleHand(textoSeparado[1].equals("true"));

                    armas.add(arma);
                }
                hunter.setWeapons(armas);

                // ARMAS ACTIVAS
                ArrayList<Weapon> armasActivas = new ArrayList<>();
                br.readLine();
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                tope = Integer.parseInt(textoSeparado[1]);
                for (int i = 0; i < tope; i++) {
                    Weapon arma = new Weapon();

                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setName(textoSeparado[1]);

                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setAttackModifier(Integer.parseInt(textoSeparado[1]));

                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setDefenseModifier(Integer.parseInt(textoSeparado[1]));

                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    arma.setSingleHand(textoSeparado[1].equals("true"));

                    armasActivas.add(arma);
                }
                hunter.setActiveWeapons(armasActivas);

                // ARMADURAS
                br.readLine();
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                ArrayList<Armor> armaduras = new ArrayList<>();
                tope = Integer.parseInt(textoSeparado[1]);
                for (int i = 0; i < tope; i++) {
                    Armor armadura = new Armor();

                    // NOMBRE ARMADURA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    armadura.setName(textoSeparado[1]);

                    // DEFENSA ARMADURA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    armadura.setDefenseModifier(Integer.parseInt(textoSeparado[1]));

                    // ATAQUE ARMADURA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    armadura.setAttackModifier(Integer.parseInt(textoSeparado[1]));

                    armaduras.add(armadura);
                }
                hunter.setArmors(armaduras);

                // ARMADURA ACTIVA
                br.readLine();
                Armor armadura = new Armor();

                linea = br.readLine();
                textoSeparado = linea.split(" ");
                armadura.setName(textoSeparado[1]);

                linea = br.readLine();
                textoSeparado = linea.split(" ");
                armadura.setDefenseModifier(Integer.parseInt(textoSeparado[1]));

                linea = br.readLine();
                textoSeparado = linea.split(" ");
                armadura.setAttackModifier(Integer.parseInt(textoSeparado[1]));

                hunter.setActiveArmor(armadura);

                // ORO
                br.readLine();
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                hunter.setGold(Integer.parseInt(textoSeparado[1]));

                // HP
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                hunter.setHealth(Integer.parseInt(textoSeparado[1]));

                // PODER
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                hunter.setPower(Integer.parseInt(textoSeparado[1]));

                // FORTALEZAS
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                tope = Integer.parseInt(textoSeparado[1]);
                ArrayList<Strength> fortalezas = new ArrayList<>();
                for (int i = 0; i < tope; i++) {
                    Strength fortaleza = new Strength();

                    // NOMBRE FORTALEZA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    fortaleza.setName(textoSeparado[1]);

                    // VALOR FORTALEZA
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    fortaleza.setValue(Integer.parseInt(textoSeparado[1]));

                    fortalezas.add(fortaleza);
                }
                hunter.setStrengths(fortalezas);

                // DEBILIDADES
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                tope = Integer.parseInt(textoSeparado[1]);
                ArrayList<Weakness> debilidades = new ArrayList<>();
                for (int i = 0; i < tope; i++) {
                    Weakness debilidad = new Weakness();

                    // NOMBRE DEBILIDAD
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    debilidad.setName(textoSeparado[1]);

                    // VALOR DEBILIDAD
                    linea = br.readLine();
                    textoSeparado = linea.split(" ");
                    debilidad.setValue(Integer.parseInt(textoSeparado[1]));

                    debilidades.add(debilidad);
                }
                hunter.setWeaknesses(debilidades);

                // ESBIRROS
                ArrayList<MinionsComposit> listaEsbirros = new ArrayList<>();
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                tope = Integer.parseInt(textoSeparado[1]);
                for (int i = 0; i < tope; i++) {
                    MinionsComposit esbirro = minionsFile(linea, br, textoSeparado);
                    listaEsbirros.add(esbirro);
                }
                hunter.setMinions(listaEsbirros);

                linea = "========== FIN USUARIO ==========";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hunter;
    }

    public Werewolf werewolfReader(BufferedReader br) {
        Werewolf Werewolf = new Werewolf();
        Don don = new Don();

        FileReader fr = null;
        try {
            String linea;

            linea = br.readLine();

            while (!linea.equals("========== FIN USUARIO ==========")) {

                Werewolf.setType("LICANTROPO");

                String[] spaceBtwText = linea.split(" "  );
                Werewolf.setName(spaceBtwText[1]);
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                don.setName(spaceBtwText[1]);
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                Werewolf.setRage(Integer.parseInt(spaceBtwText[1]));
                linea = br.readLine();
                spaceBtwText = linea.split(" ");
                don.setAttack(Integer.parseInt(spaceBtwText[1]));
                linea = br.readLine();
                spaceBtwText = linea.split(" ");
                don.setDefense(Integer.parseInt(spaceBtwText[1]));
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                ArrayList<Weapon> listaWeapons = new ArrayList<>();
                int tope = Integer.parseInt(spaceBtwText[1]);
                for (int i = 0; i < tope; i++) {

                    Weapon Weapon = new Weapon();

                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weapon.setName(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weapon.setAttackModifier((Integer.parseInt(spaceBtwText[1])));
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weapon.setDefenseModifier((Integer.parseInt(spaceBtwText[1])));
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weapon.setSingleHand(spaceBtwText[1].equals("true"));

                    listaWeapons.add(Weapon);
                }
                Werewolf.setWeapons(listaWeapons);
                br.readLine();

                ArrayList<Weapon> WeaponsActivas = new ArrayList<>();
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                tope = Integer.parseInt(spaceBtwText[1]);
                for (int i = 0; i < tope; i++) {

                    Weapon Weapon = new Weapon();

                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weapon.setName(spaceBtwText[1]);

                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weapon.setAttackModifier((Integer.parseInt(spaceBtwText[1])));

                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weapon.setDefenseModifier((Integer.parseInt(spaceBtwText[1])));

                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weapon.setSingleHand(spaceBtwText[1].equals("true"));
                    WeaponsActivas.add(Weapon);
                }
                Werewolf.setActiveWeapons(WeaponsActivas);
                br.readLine();

                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                ArrayList<Armor> Armors = new ArrayList<>();
                tope = Integer.parseInt(spaceBtwText[1]);
                for (int i = 0; i < tope; i++) {

                    Armor Armor = new Armor();

                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Armor.setName(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Armor.setDefenseModifier((Integer.parseInt(spaceBtwText[1])));
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Armor.setAttackModifier((Integer.parseInt(spaceBtwText[1])));

                    Armors.add(Armor);
                }
                Werewolf.setArmors(Armors);
                br.readLine();

                Armor Armor = new Armor();
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                Armor.setName(spaceBtwText[1]);
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                Armor.setDefenseModifier((Integer.parseInt(spaceBtwText[1])));
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                Armor.setAttackModifier((Integer.parseInt(spaceBtwText[1])));
                Werewolf.setActiveArmor(Armor);
                br.readLine();
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                Werewolf.setGold(Integer.parseInt(spaceBtwText[1]));
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                Werewolf.setHealth(Integer.parseInt(spaceBtwText[1]));
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                Werewolf.setPower(Integer.parseInt(spaceBtwText[1]));
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                tope = Integer.parseInt(spaceBtwText[1]);
                ArrayList<Weakness> Weaknesses = new ArrayList<>();
                for (int i = 0; i < tope; i++) {

                    Weakness Weakness = new Weakness();

                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weakness.setName((spaceBtwText[1]));
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Weakness.setValue((Integer.parseInt(spaceBtwText[1])));

                    Weaknesses.add(Weakness);
                }
                Werewolf.setWeaknesses(Weaknesses);

                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                tope = Integer.parseInt(spaceBtwText[1]);
                ArrayList<Strength> Strengths = new ArrayList<>();
                for (int i = 0; i < tope; i++) {

                    Strength Strength = new Strength();

                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Strength.setName(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    Strength.setValue((Integer.parseInt(spaceBtwText[1])));

                    Strengths.add(Strength);
                }
                Werewolf.setStrengths(Strengths);

                ArrayList<MinionsComposit> listaEsbirros = new ArrayList<>();
                linea = br.readLine();
                spaceBtwText = linea.split(" "  );
                tope = Integer.parseInt(spaceBtwText[1]);
                for (int i = 0; i < tope; i++) {
                    MinionsComposit esbirro = minionsFile(linea, br, spaceBtwText);
                    listaEsbirros.add(esbirro);
                }
                Werewolf.setMinions(listaEsbirros);
                Werewolf.setAbility(don);
                linea = "========== FIN USUARIO ==========";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {// En el finally cerramos el fichero
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return Werewolf;
    }

    private MinionsComposit minionsFile(String linea, BufferedReader br, String[] spaceBtwText) throws NumberFormatException, IOException {
        int tope = Integer.parseInt(spaceBtwText[1]);
        for (int i = 0; i < tope; i++) {
            linea = br.readLine();
            spaceBtwText = linea.split(" "  );
            switch (spaceBtwText[1]) {
                case "HUMANO" -> {
                    Human human = new Human();

                    human.setType(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    human.setName(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    human.setHp((Integer.parseInt(spaceBtwText[1])));
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    if (spaceBtwText[1].equals("ALTA")) {
                        human.setLoyalty(Human.Loyalty.ALTA);
                    } else if (spaceBtwText[1].equals("MEDIA")) {
                        human.setLoyalty(Human.Loyalty.MEDIA);
                    } else {
                        human.setLoyalty(Human.Loyalty.BAJA);
                    }
                    return human;
                }
                case "GHOUL" -> {
                    Ghoul ghoul = new Ghoul();
                    ghoul.setType(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    ghoul.setName(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    ghoul.setHp((Integer.parseInt(spaceBtwText[1])));
                    linea = br.readLine();
                    spaceBtwText = linea.split(" "  );
                    ghoul.setDependency((Integer.parseInt(spaceBtwText[1])));
                    return ghoul;
                }
                case "DEMONIO" -> {
                    Demon Demon = new Demon();

                    Demon.setType(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" ");
                    Demon.setName(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" ");
                    Demon.setHp((Integer.parseInt(spaceBtwText[1])));
                    linea = br.readLine();
                    spaceBtwText = linea.split(" ");
                    Demon.setDescripcion(spaceBtwText[1]);
                    linea = br.readLine();
                    spaceBtwText = linea.split(" ");
                    ArrayList<MinionsComposit> esbirrosDemon = new ArrayList<>();
                    for (int j = 0; j < (Integer.parseInt(spaceBtwText[1])); j++) {
                        MinionsComposit esbirro = minionsFile(linea, br, spaceBtwText);
                        esbirrosDemon.add(esbirro);
                    }
                    Demon.setMinionsComposites(esbirrosDemon);
                    return Demon;
                }
            }
        }
        return null;
    }
}
