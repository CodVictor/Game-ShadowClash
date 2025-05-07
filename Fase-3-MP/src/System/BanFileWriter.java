package System;

import Entities.Client;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BanFileWriter {

    private static final String BAN_FILE_PATH = "Fase-3-MP/src/Files/BanRegister.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void banUser(Client client) {
        try {
            FileWriter fw = new FileWriter(BAN_FILE_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            LocalDateTime banStart = LocalDateTime.now();

            bw.write("========== USUARIO BANEADO ==========");
            bw.newLine();
            bw.write("FECHA-Y-HORA-DEL-BANEO " + banStart.format(formatter));
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
            bw.write("MOTIVO-DEL-BANEO ");
            bw.write(client.getBanMotive());
            bw.newLine();
            bw.write("========== FIN USUARIO BANEADO ==========");
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para reescribir el archivo de baneos
    public void rewriteBanFile(List<Client> bannedClients) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BAN_FILE_PATH))) {
            LocalDateTime banStart = LocalDateTime.now();

            for (Client client: bannedClients) {
                bw.newLine();
                bw.write("========== USUARIO BANEADO ==========");
                bw.newLine();
                bw.write("FECHA-Y-HORA-DEL-BANEO " + banStart.format(formatter));
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
                bw.write("MOTIVO-DEL-BANEO ");
                bw.write(client.getBanMotive());
                bw.newLine();
                bw.write("========== FIN USUARIO BANEADO ==========");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


