package service;

import java.util.*;

/**
 * Service de conversion JSON vers XML (from scratch)
 * Parse le JSON manuellement et construit le XML
 */
public class JsonToXmlService {
    
    private StringBuilder xmlBuilder;
    private int indentLevel;
    private static final String INDENT = "  ";
    
    /**
     * Convertit une chaîne JSON en XML
     * @param jsonContent Contenu JSON
     * @return Chaîne XML formatée
     */
    public String convert(String jsonContent) throws Exception {
        xmlBuilder = new StringBuilder();
        indentLevel = 0;
        
        // Ajouter la déclaration XML
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        
        // Parser le JSON
        Object jsonObj = parseJson(jsonContent.trim());
        
        // Convertir en XML
        if (jsonObj instanceof Map) {
            Map<String, Object> rootMap = (Map<String, Object>) jsonObj;
            for (Map.Entry<String, Object> entry : rootMap.entrySet()) {
                convertToXml(entry.getKey(), entry.getValue());
            }
        } else {
            throw new Exception("Le JSON doit commencer par un objet");
        }
        
        return xmlBuilder.toString();
    }
    
    /**
     * Parse une chaîne JSON en structure Java
     */
    private Object parseJson(String json) throws Exception {
        json = json.trim();
        
        if (json.startsWith("{")) {
            return parseObject(json);
        } else if (json.startsWith("[")) {
            return parseArray(json);
        } else if (json.startsWith("\"")) {
            return parseString(json);
        } else if (json.equals("null")) {
            return null;
        } else if (json.equals("true") || json.equals("false")) {
            return Boolean.parseBoolean(json);
        } else {
            // Nombre
            try {
                if (json.contains(".")) {
                    return Double.parseDouble(json);
                } else {
                    return Integer.parseInt(json);
                }
            } catch (NumberFormatException e) {
                throw new Exception("Valeur JSON invalide: " + json);
            }
        }
    }
    
    /**
     * Parse un objet JSON
     */
    private Map<String, Object> parseObject(String json) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        
        // Retirer les accolades
        json = json.substring(1, json.length() - 1).trim();
        
        if (json.isEmpty()) {
            return map;
        }
        
        // Parser les paires clé-valeur
        int pos = 0;
        while (pos < json.length()) {
            // Sauter les espaces
            while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                pos++;
            }
            
            if (pos >= json.length()) break;
            
            // Parser la clé
            if (json.charAt(pos) != '"') {
                throw new Exception("Clé attendue à la position " + pos);
            }
            
            int keyStart = pos + 1;
            int keyEnd = findStringEnd(json, pos);
            String key = json.substring(keyStart, keyEnd);
            pos = keyEnd + 1;
            
            // Trouver le ':'
            while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                pos++;
            }
            if (pos >= json.length() || json.charAt(pos) != ':') {
                throw new Exception("':' attendu après la clé");
            }
            pos++;
            
            // Parser la valeur
            while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                pos++;
            }
            
            int valueStart = pos;
            int valueEnd = findValueEnd(json, pos);
            String valueStr = json.substring(valueStart, valueEnd).trim();
            
            Object value = parseJson(valueStr);
            map.put(key, value);
            
            pos = valueEnd;
            
            // Chercher la virgule
            while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                pos++;
            }
            if (pos < json.length() && json.charAt(pos) == ',') {
                pos++;
            }
        }
        
        return map;
    }
    
    /**
     * Parse un tableau JSON
     */
    private List<Object> parseArray(String json) throws Exception {
        List<Object> list = new ArrayList<>();
        
        // Retirer les crochets
        json = json.substring(1, json.length() - 1).trim();
        
        if (json.isEmpty()) {
            return list;
        }
        
        int pos = 0;
        while (pos < json.length()) {
            // Sauter les espaces
            while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                pos++;
            }
            
            if (pos >= json.length()) break;
            
            int valueStart = pos;
            int valueEnd = findValueEnd(json, pos);
            String valueStr = json.substring(valueStart, valueEnd).trim();
            
            Object value = parseJson(valueStr);
            list.add(value);
            
            pos = valueEnd;
            
            // Chercher la virgule
            while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                pos++;
            }
            if (pos < json.length() && json.charAt(pos) == ',') {
                pos++;
            }
        }
        
        return list;
    }
    
    /**
     * Parse une chaîne JSON
     */
    private String parseString(String json) {
        // Retirer les guillemets et gérer les échappements
        String str = json.substring(1, json.length() - 1);
        str = str.replace("\\\"", "\"")
                 .replace("\\\\", "\\")
                 .replace("\\n", "\n")
                 .replace("\\r", "\r")
                 .replace("\\t", "\t");
        return str;
    }
    
    /**
     * Trouve la fin d'une chaîne JSON
     */
    private int findStringEnd(String json, int start) throws Exception {
        int pos = start + 1;
        while (pos < json.length()) {
            char c = json.charAt(pos);
            if (c == '"' && json.charAt(pos - 1) != '\\') {
                return pos;
            }
            pos++;
        }
        throw new Exception("Chaîne non terminée");
    }
    
    /**
     * Trouve la fin d'une valeur JSON
     */
    private int findValueEnd(String json, int start) {
        char firstChar = json.charAt(start);
        
        if (firstChar == '"') {
            try {
                return findStringEnd(json, start) + 1;
            } catch (Exception e) {
                return json.length();
            }
        } else if (firstChar == '{') {
            return findMatchingBrace(json, start, '{', '}') + 1;
        } else if (firstChar == '[') {
            return findMatchingBrace(json, start, '[', ']') + 1;
        } else {
            // Valeur simple (nombre, booléen, null)
            int pos = start;
            while (pos < json.length()) {
                char c = json.charAt(pos);
                if (c == ',' || c == '}' || c == ']') {
                    break;
                }
                pos++;
            }
            return pos;
        }
    }
    
    /**
     * Trouve l'accolade/crochet fermant correspondant
     */
    private int findMatchingBrace(String json, int start, char open, char close) {
        int count = 1;
        int pos = start + 1;
        boolean inString = false;
        
        while (pos < json.length() && count > 0) {
            char c = json.charAt(pos);
            
            if (c == '"' && json.charAt(pos - 1) != '\\') {
                inString = !inString;
            }
            
            if (!inString) {
                if (c == open) count++;
                if (c == close) count--;
            }
            
            pos++;
        }
        
        return pos - 1;
    }
    
    /**
     * Convertit un objet Java en XML
     */
    private void convertToXml(String tagName, Object value) {
        if (tagName.startsWith("@")) {
            // Les attributs sont ignorés ici, ils seront gérés par l'élément parent
            return;
        }
        
        if (value == null) {
            appendIndent();
            xmlBuilder.append("<").append(tagName).append("/>\n");
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            appendIndent();
            xmlBuilder.append("<").append(tagName);
            
            // Ajouter les attributs
            Map<String, Object> attributes = new LinkedHashMap<>();
            Map<String, Object> elements = new LinkedHashMap<>();
            String textContent = null;
            
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().startsWith("@")) {
                    attributes.put(entry.getKey().substring(1), entry.getValue());
                } else if (entry.getKey().equals("#text")) {
                    textContent = String.valueOf(entry.getValue());
                } else {
                    elements.put(entry.getKey(), entry.getValue());
                }
            }
            
            // Écrire les attributs
            for (Map.Entry<String, Object> attr : attributes.entrySet()) {
                xmlBuilder.append(" ").append(attr.getKey()).append("=\"");
                xmlBuilder.append(escapeXml(String.valueOf(attr.getValue()))).append("\"");
            }
            
            if (elements.isEmpty() && textContent == null) {
                xmlBuilder.append("/>\n");
            } else {
                xmlBuilder.append(">");
                
                if (!elements.isEmpty()) {
                    xmlBuilder.append("\n");
                    indentLevel++;
                    for (Map.Entry<String, Object> element : elements.entrySet()) {
                        convertToXml(element.getKey(), element.getValue());
                    }
                    indentLevel--;
                    appendIndent();
                } else if (textContent != null) {
                    xmlBuilder.append(escapeXml(textContent));
                }
                
                xmlBuilder.append("</").append(tagName).append(">\n");
            }
        } else if (value instanceof List) {
            // Chaque élément du tableau devient un élément XML avec le même nom
            List<Object> list = (List<Object>) value;
            for (Object item : list) {
                convertToXml(tagName, item);
            }
        } else {
            // Valeur simple
            appendIndent();
            xmlBuilder.append("<").append(tagName).append(">");
            xmlBuilder.append(escapeXml(String.valueOf(value)));
            xmlBuilder.append("</").append(tagName).append(">\n");
        }
    }
    
    /**
     * Échappe les caractères spéciaux XML
     */
    private String escapeXml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
    
    /**
     * Ajoute l'indentation courante
     */
    private void appendIndent() {
        for (int i = 0; i < indentLevel; i++) {
            xmlBuilder.append(INDENT);
        }
    }
}