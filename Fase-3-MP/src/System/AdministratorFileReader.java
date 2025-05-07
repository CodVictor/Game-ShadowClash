package System;

import Entities.Administrator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class AdministratorFileReader {
    public ArrayList<Administrator> adminFileReader() {

        FileReader fr = null;
        ArrayList<Administrator> listAdmin = new ArrayList<>();
        try {
            File archivo = new File("Fase-3-MP/src/Files/AdminRegister.txt");
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);

            // Lectura del fichero
            String linea = br.readLine();
            String[] textoSeparado;

            linea = br.readLine();

            while (linea != null) {
                Administrator admin = new Administrator();

                textoSeparado = linea.split(" ");
                admin.setName(textoSeparado[1]);
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                admin.setNick(textoSeparado[1]);
                linea = br.readLine();
                textoSeparado = linea.split(" ");
                admin.setPassword(textoSeparado[1]);

                listAdmin.add(admin);
                linea = br.readLine();
                linea = br.readLine();
                linea = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {// En el finally cerramos el fichero, para asegurarnos
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return listAdmin;
    }
}//FIN
