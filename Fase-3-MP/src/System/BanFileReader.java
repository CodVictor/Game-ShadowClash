package System;

import Entities.Client;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BanFileReader {
    private static final String BAN_FILE_PATH = "Fase-3-MP/src/Files/BanRegister.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ArrayList<Client> readBannedUsers() {
        ArrayList<Client> listaCliente = new ArrayList<>();

        File archivo = new File(BAN_FILE_PATH);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return listaCliente;
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // buscamos el inicio de un bloque baneado
                if (!linea.equals("========== USUARIO BANEADO =========="))
                    continue;
                Client cliente = new Client();
                // 1) FECHA-Y-HORA-DEL-BANEO
                linea = br.readLine();
                if (linea != null && linea.startsWith("FECHA-Y-HORA-DEL-BANEO ")) {
                    String textoFecha = linea.split(" ", 2)[1];
                    cliente.setBanDateTime(LocalDateTime.parse(textoFecha, formatter));
                }
                // 2) NOMBRE
                linea = br.readLine();
                if (linea != null && linea.startsWith("NOMBRE ")) {
                    cliente.setName(linea.split(" ", 2)[1]);
                }
                // 3) NICK
                linea = br.readLine();
                if (linea != null && linea.startsWith("NICK ")) {
                    cliente.setNick(linea.split(" ", 2)[1]);
                }
                // 4) PASSWORD
                linea = br.readLine();
                if (linea != null && linea.startsWith("PASSWORD ")) {
                    cliente.setPassword(linea.split(" ", 2)[1]);
                }
                // 5) REGISTRO
                linea = br.readLine();
                if (linea != null && linea.startsWith("REGISTRO ")) {
                    cliente.setRegister(linea.split(" ", 2)[1]);
                }
                // 6) MOTIVO-DEL-BANEO
                linea = br.readLine();
                if (linea != null && linea.startsWith("MOTIVO-DEL-BANEO ")) {
                    // split en 2 para no descartar posibles espacios en el motivo
                    cliente.setBanMotive(linea.split(" ", 2)[1]);
                }
                // 7) avanzar hasta el fin de bloque
                while ((linea = br.readLine()) != null
                        && !linea.equals("========== FIN USUARIO BANEADO ==========")) {
                    // nada, sólo saltamos
                }
                listaCliente.add(cliente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaCliente;
    }
}