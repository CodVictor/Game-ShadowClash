package System;

import Entities.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChallengeFileWriter {
    private final String CHALLENGE_REGISTER_PATH = "Fase-3-MP/src/Files/ChallengeRegister.txt";

    public void rewriteChallengeFile(ArrayList<Challenge> challenges){
        try {
            File file = new File(CHALLENGE_REGISTER_PATH);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            UserFileReader userFileReader = new UserFileReader();
            ArrayList<Client> clients = userFileReader.userFileReader();
            //recorre la lista de desafios
            for (Challenge Challenge : challenges) {
                bw.write("========== DESAFIO ==========");
                bw.newLine();
                bw.write("DESAFIANTE ");
                bw.write(Challenge.getChallenger().getName());
                bw.newLine();
                bw.write("NICK ");
                bw.write(Challenge.getChallenger().getNick());
                bw.newLine();
                bw.write("REGISTRO ");
                bw.write(Challenge.getChallenger().getRegister());
                bw.newLine();

                bw.write("CONTRINCANTE ");
                bw.write(Challenge.getRival().getName());
                bw.newLine();

                bw.write("NICK ");
                bw.write(Challenge.getRival().getNick());
                bw.newLine();

                bw.write("REGISTRO ");
                bw.write(Challenge.getRival().getRegister());
                bw.newLine();

                bw.write("==== INFORMACION DEL DESAFIO ====");
                bw.newLine();
                bw.write("ORO-APOSTADO ");
                bw.write(String.valueOf(Challenge.getGold()));
                bw.newLine();

                bw.write("CANTIDAD_MODIFICADORES ");
                bw.write(String.valueOf(Challenge.getModifiers().size()));
                bw.newLine();
                for (int j = 0; j < (Challenge.getModifiers().size()); j++) {
                    Modifier modificador = Challenge.getModifiers().get(j);
                    bw.write("NOMBRE_MODIFICADOR ");
                    bw.write(modificador.getName());
                    bw.newLine();

                    bw.write("VALOR_MODIFICADOR ");
                    bw.write(String.valueOf(modificador.getValue()));
                    bw.newLine();
                }
                bw.newLine();

                String pattern = "dd-MM-yyyy HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(Challenge.getDate());
                bw.write("FECHA ");
                bw.write(date);
                bw.newLine();

                bw.write("VALIDADO ");
                if (Challenge.isValidated()) {
                    bw.write("true");
                } else {
                    bw.write("false");
                }
                bw.newLine();

                bw.write("REGISTRO ");
                bw.write(Challenge.getRegister());
                bw.newLine();

                bw.write("========== FIN DESAFIO ==========");
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            MainSystem mainSystem = new MainSystem();
            mainSystem.selector();
            e.printStackTrace();
        }
    }
}
