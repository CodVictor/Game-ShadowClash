package System;

import Entities.Administrator;
import java.io.*;
import java.util.ArrayList;

public class AdministratorFileWriter {
    private final String ADMIN_FILE_PATH = "Fase-3-MP/src/Files/AdminRegister.txt";
    // Registra un administrador en el fichero AdminRegister.txt
    public void adminRegister(Administrator admin) {
        try {
            File file = new File(ADMIN_FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
            // Se abre en modo adjuntar (true)
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("========== ADMIN ==========");
            bw.newLine();
            bw.write("NOMBRE " + admin.getName());
            bw.newLine();
            bw.write("NICK " + admin.getNick());
            bw.newLine();
            bw.write("PASSWORD " + admin.getPassword());
            bw.newLine();
            bw.write("========== FIN ADMIN ==========");
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error se puede volver al selector
            new MainSystem().selector();
        }
    }
    // Reescribe el fichero de usuarios (para clientes)
    public void rewriteAdminFile(ArrayList<Administrator> clientArrayList) {
        try {
            File file = new File(ADMIN_FILE_PATH);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Administrator client : clientArrayList) {
                bw.write("=========== ADMIN ===========");
                bw.newLine();
                bw.write("NOMBRE " + client.getName());
                bw.newLine();
                bw.write("NICK " + client.getNick());
                bw.newLine();
                bw.write("PASSWORD " + client.getPassword());
                bw.newLine();
                bw.write("========== FIN ADMIN ==========");
                bw.newLine();
            }
            bw.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            new MainSystem().selector();
        }
    }
}


