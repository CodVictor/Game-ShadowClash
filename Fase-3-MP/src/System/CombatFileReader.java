package System;

import Entities.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CombatFileReader {
    private static final String COMBAT_FILE_PATH = "Fase-3-MP/src/Files/CombatRegister.txt";

    public List<Combat> readCombats() {
        List<Combat> combats = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COMBAT_FILE_PATH))) {
            String line;
            Combat combat = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("========== COMBATE ==========")) {
                    combat = new Combat();
                } else if (line.startsWith("DESAFIANTE")) {
                    String challengerName = line.split(" ", 2)[1];
                    Client challenger = new Client();
                    challenger.setNick(challengerName);
                    combat.setChallenger(challenger);
                } else if (line.startsWith("NICK") && combat.getChallenger() != null && combat.getRival() == null) {
                    String nick = line.split(" ", 2)[1];
                    combat.getChallenger().setNick(nick);
                } else if (line.startsWith("CONTRINCANTE")) {
                    String rivalName = line.split(" ", 2)[1];
                    Client rival = new Client();
                    rival.setNick(rivalName);
                    combat.setRival(rival);
                } else if (line.startsWith("NICK") && combat.getRival() != null) {
                    String nick = line.split(" ", 2)[1];
                    combat.getRival().setNick(nick);
                } else if (line.startsWith("FECHA")) {
                    String dateStr = line.split(" ", 2)[1];
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        combat.setDate(sdf.parse(dateStr));
                    } catch (ParseException e) {
                        combat.setDate(null);
                    }
                } else if (line.startsWith("ORO-APOSTADO")) {
                    try {
                        int gold = Integer.parseInt(line.split(" ", 2)[1]);
                        combat.setGoldBet(gold); // Cambiado de setGold a setGoldBet
                    } catch (NumberFormatException e) {
                        combat.setGoldBet(0); // Valor por defecto si hay un error
                    }
                } else if (line.startsWith("ESTADO-COMBATE")) {
                    combat.setStatus(line.split(" ", 2)[1]);
                }else if (line.startsWith("GANADOR")) {
                    String winnerName = line.split(" ", 2)[1];
                    Client winner = new Client();
                    winner.setNick(winnerName);
                    combat.setWinner(winner);
                } else if (line.equals("EMPATE")) {
                    combat.setWinner(null); // Si es empate, no hay ganador
                } else if (line.startsWith("========== FIN COMBATE ==========")) {
                    combats.add(combat);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return combats;
    }

}
