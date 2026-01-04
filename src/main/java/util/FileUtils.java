


//FileUtils est une classe utilitaire qui centralise la lecture et l’écriture des 
//fichiers afin d’éviter la duplication de code dans le contrôleur

package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Classe utilitaire pour la lecture et l'écriture de fichiers
 */
public class FileUtils {
    
    /**
     * Lit le contenu complet d'un fichier
     * @param file Fichier à lire
     * @return Contenu du fichier sous forme de String
     * @throws IOException Si une erreur de lecture survient
     */
    public static String readFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException("Le fichier n'existe pas");
        }
        
        if (!file.canRead()) {
            throw new IOException("Impossible de lire le fichier");
        }
        
        // Lire tout le contenu du fichier
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, StandardCharsets.UTF_8);
    }
    
    /**
     * Écrit du contenu dans un fichier
     * @param file Fichier destination
     * @param content Contenu à écrire
     * @throws IOException Si une erreur d'écriture survient
     */
    public static void writeFile(File file, String content) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Le fichier ne peut pas être null");
        }
        
        if (content == null) {
            content = "";
        }
        
        // Créer les répertoires parents si nécessaire
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        
        // Écrire le contenu
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(file), 
                    StandardCharsets.UTF_8
                )
            )) {
            writer.write(content);
        }
    }
    
    /**
     * Vérifie si un fichier est un fichier XML valide
     * @param file Fichier à vérifier
     * @return true si le fichier a l'extension .xml
     */
    public static boolean isXmlFile(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return name.endsWith(".xml");
    }
    
    /**
     * Vérifie si un fichier est un fichier JSON valide
     * @param file Fichier à vérifier
     * @return true si le fichier a l'extension .json
     */
    public static boolean isJsonFile(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return name.endsWith(".json");
    }
    
    /**
     * Obtient l'extension d'un fichier
     * @param file Fichier
     * @return Extension du fichier (sans le point) ou chaîne vide
     */
    public static String getFileExtension(File file) {
        if (file == null) {
            return "";
        }
        
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        
        if (lastDot > 0 && lastDot < name.length() - 1) {
            return name.substring(lastDot + 1).toLowerCase();
        }
        
        return "";
    }
}