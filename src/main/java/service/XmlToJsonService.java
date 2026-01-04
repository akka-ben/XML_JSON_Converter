package service;

import org.w3c.dom.*; 
// API DOM : permet de représenter un document XML sous forme d’arbre (Document, Element, Node…)

import javax.xml.parsers.DocumentBuilder; 
// Objet chargé de construire le DOM à partir du XML

import javax.xml.parsers.DocumentBuilderFactory;
// Fabrique qui fournit un DocumentBuilder configuré

import java.io.ByteArrayInputStream;
// Permet de lire une chaîne XML comme un flux d’entrée

import java.nio.charset.StandardCharsets;
// Définit l’encodage (UTF-8)

/**
 * Service de conversion XML vers JSON (from scratch)
 * Le XML est parsé avec DOM et le JSON est construit manuellement
 */
public class XmlToJsonService {

    // Construit progressivement la chaîne JSON
    private StringBuilder jsonBuilder;

    // Niveau d’imbrication pour gérer l’indentation
    private int indentLevel;

    // Indentation utilisée dans le JSON final
    private static final String INDENT = "  ";

    /**
     * Méthode principale de conversion
     * - Gère le document XML dans sa globalité
     * - Lance la conversion récursive à partir de la racine
     */
    public String convert(String xmlContent) throws Exception {

        // Initialisation du constructeur JSON
        jsonBuilder = new StringBuilder();
        indentLevel = 0;

        // Création du parser DOM
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false); // Pas de gestion des namespaces
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Conversion de la chaîne XML en flux d’entrée
        ByteArrayInputStream input = new ByteArrayInputStream(
            xmlContent.getBytes(StandardCharsets.UTF_8)
        );

        // Parsing du XML → création de l’arbre DOM
        Document doc = builder.parse(input);
        doc.getDocumentElement().normalize();

        // Récupération de l’élément racine
        Element root = doc.getDocumentElement();

        // Début de l’objet JSON racine
        jsonBuilder.append("{\n");
        indentLevel++;

        // Ajout du nom de la racine comme clé JSON
        appendIndent();
        jsonBuilder.append("\"").append(root.getNodeName()).append("\": ");

        // Conversion récursive de l’élément racine
        convertElement(root);

        // Fermeture de l’objet JSON
        jsonBuilder.append("\n");
        indentLevel--;
        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    /**
     * Convertit un élément XML en structure JSON
     * Méthode récursive appelée pour chaque balise XML
     */
    private void convertElement(Element element) {

        // Récupération des enfants et des attributs
        NodeList children = element.getChildNodes();
        NamedNodeMap attributes = element.getAttributes();

        boolean hasTextContent = false;   // Indique la présence de texte
        boolean hasChildElements = false; // Indique la présence d’éléments enfants
        String textContent = "";

        // Analyse du contenu de l’élément
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            // Si le nœud est une balise XML
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                hasChildElements = true;

            // Si le nœud est du texte
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getNodeValue().trim();
                if (!text.isEmpty()) {
                    hasTextContent = true;
                    textContent = text;
                }
            }
        }

        // Cas 1 : élément simple (texte uniquement)
        if (hasTextContent && !hasChildElements && attributes.getLength() == 0) {
            jsonBuilder.append("\"")
                       .append(escapeJson(textContent))
                       .append("\"");
            return;
        }

        // Cas 2 : élément vide
        if (!hasTextContent && !hasChildElements && attributes.getLength() == 0) {
            jsonBuilder.append("null");
            return;
        }

        // Cas 3 : élément complexe (attributs et/ou enfants)
        jsonBuilder.append("{\n");
        indentLevel++;

        boolean firstProperty = true;

        // Ajout des attributs XML sous forme de propriétés JSON
        if (attributes.getLength() > 0) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);

                if (!firstProperty) {
                    jsonBuilder.append(",\n");
                }

                appendIndent();
                jsonBuilder.append("\"@")
                           .append(attr.getNodeName())
                           .append("\": \"")
                           .append(escapeJson(attr.getNodeValue()))
                           .append("\"");

                firstProperty = false;
            }
        }

        // Ajout du texte si l’élément contient à la fois texte et enfants
        if (hasTextContent && hasChildElements) {
            if (!firstProperty) {
                jsonBuilder.append(",\n");
            }

            appendIndent();
            jsonBuilder.append("\"#text\": \"")
                       .append(escapeJson(textContent))
                       .append("\"");

            firstProperty = false;
        }

        // Traitement des éléments enfants
        if (hasChildElements) {
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);

                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) child;
                    String childName = childElement.getNodeName();

                    // Vérifie si plusieurs enfants ont le même nom
                    int count = countElementsByName(element, childName);

                    // Cas tableau JSON
                    if (count > 1) {
                        if (!isAlreadyProcessed(element, childName, i)) {

                            if (!firstProperty) {
                                jsonBuilder.append(",\n");
                            }

                            appendIndent();
                            jsonBuilder.append("\"")
                                       .append(childName)
                                       .append("\": [\n");

                            indentLevel++;
                            boolean firstArrayElement = true;

                            // Parcours de tous les éléments du tableau
                            for (int j = 0; j < children.getLength(); j++) {
                                Node arrayChild = children.item(j);

                                if (arrayChild.getNodeType() == Node.ELEMENT_NODE &&
                                    arrayChild.getNodeName().equals(childName)) {

                                    if (!firstArrayElement) {
                                        jsonBuilder.append(",\n");
                                    }

                                    appendIndent();
                                    convertElement((Element) arrayChild);
                                    firstArrayElement = false;
                                }
                            }

                            jsonBuilder.append("\n");
                            indentLevel--;
                            appendIndent();
                            jsonBuilder.append("]");
                            firstProperty = false;
                        }

                    // Cas élément unique
                    } else {
                        if (!firstProperty) {
                            jsonBuilder.append(",\n");
                        }

                        appendIndent();
                        jsonBuilder.append("\"")
                                   .append(childName)
                                   .append("\": ");

                        convertElement(childElement);
                        firstProperty = false;
                    }
                }
            }
        }

        // Fermeture de l’objet JSON courant
        jsonBuilder.append("\n");
        indentLevel--;
        appendIndent();
        jsonBuilder.append("}");
    }

    /**
     * Compte le nombre d’enfants portant un nom donné
     * Utilisé pour détecter les tableaux JSON
     */
    private int countElementsByName(Element parent, String name) {
        int count = 0;
        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE &&
                child.getNodeName().equals(name)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Évite de traiter plusieurs fois un même tableau
     */
    private boolean isAlreadyProcessed(Element parent, String name, int currentIndex) {
        NodeList children = parent.getChildNodes();

        for (int i = 0; i < currentIndex; i++) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE &&
                child.getNodeName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Échappe les caractères spéciaux pour respecter la syntaxe JSON
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    /**
     * Ajoute l’indentation selon le niveau d’imbrication
     */
    private void appendIndent() {
        for (int i = 0; i < indentLevel; i++) {
            jsonBuilder.append(INDENT);
        }
    }
}