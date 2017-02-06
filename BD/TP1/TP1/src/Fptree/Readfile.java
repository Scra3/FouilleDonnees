package tp1Bis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author scra
 */

public class Readfile {

    /*Permet de lire un fichier de le sotcker dans un string*/
    protected static String readFileQuick(String chemin, Charset encodage) throws IOException {
        byte[] encode = Files.readAllBytes(Paths.get(chemin));
        return new String(encode, encodage);
    }

    /*Retourne le contenu du fichier*/
    protected static String getFile(String chemin) {
        String jeuDonnees = "Rien";
        try {
            jeuDonnees = Readfile.readFileQuick(chemin, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e);
        }
        return jeuDonnees;
    }    
}
