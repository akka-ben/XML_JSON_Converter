package service.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

public class XmlToJsonApiService {
    
    public String convert(String xmlContent) throws Exception {
        // 1️⃣ Parse XML → arbre (API)
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode rootNode = xmlMapper.readTree(xmlContent.getBytes());
        
        // 2️⃣ Structuration légère (attributs / texte)
        JsonNode structuredNode = normalizeXmlNode(xmlContent);
        
        // 3️⃣ Génération JSON formaté
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(structuredNode);
    }
    
    /**
     * Transforme la structure Jackson XML en JSON cohérent
     * - attributs → "@attr"
     * - texte → "#text"
     */
    private JsonNode normalizeXmlNode(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlContent.getBytes("UTF-8")));
        
        return convertElement(doc.getDocumentElement());
    }
    
    private JsonNode convertElement(Element element) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        
        // Traiter les attributs avec @
        NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            result.put("@" + attr.getNodeName(), attr.getNodeValue());
        }
        
        // Traiter les enfants
        NodeList children = element.getChildNodes();
        StringBuilder textContent = new StringBuilder();
        
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            
            if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getNodeValue().trim();
                if (!text.isEmpty()) {
                    textContent.append(text);
                }
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElem = (Element) child;
                String key = childElem.getNodeName();
                JsonNode value = convertElement(childElem);
                
                if (result.has(key)) {
                    JsonNode existing = result.get(key);
                    if (existing.isArray()) {
                        ((com.fasterxml.jackson.databind.node.ArrayNode) existing).add(value);
                    } else {
                        com.fasterxml.jackson.databind.node.ArrayNode array = mapper.createArrayNode();
                        array.add(existing);
                        array.add(value);
                        result.set(key, array);
                    }
                } else {
                    result.set(key, value);
                }
            }
        }
        
        // Ajouter le texte si présent
        if (textContent.length() > 0) {
            result.put("#text", textContent.toString());
        }
        
        // Si que du texte sans attributs, retourner juste le texte
        if (result.size() == 1 && result.has("#text") && attrs.getLength() == 0) {
            return mapper.getNodeFactory().textNode(result.get("#text").asText());
        }
        
        return result;
    }
}