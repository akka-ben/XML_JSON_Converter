package service.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

public class JsonToXmlApiService {
    
    private final ObjectMapper jsonMapper;
    
    public JsonToXmlApiService() {
        this.jsonMapper = new ObjectMapper();
    }
    
    public String convert(String jsonContent) throws Exception {
        JsonNode rootNode = jsonMapper.readTree(jsonContent);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        // Déterminer élément racine
        String rootName = "root";
        JsonNode dataNode = rootNode;
        
        if (rootNode.isObject() && rootNode.size() == 1) {
            rootName = rootNode.fieldNames().next();
            dataNode = rootNode.get(rootName);
        }
        
        Element rootElement = doc.createElement(rootName);
        doc.appendChild(rootElement);
        
        processNode(doc, rootElement, dataNode);
        
        return documentToString(doc);
    }
    
    private void processNode(Document doc, Element parent, JsonNode node) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            StringBuilder textContent = new StringBuilder();
            
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode value = field.getValue();
                
                // Attributs (@)
                if (key.startsWith("@")) {
                    parent.setAttribute(key.substring(1), value.asText());
                }
                // Contenu texte (#text)
                else if (key.equals("#text")) {
                    textContent.append(value.asText());
                }
                // Éléments
                else {
                    if (value.isArray()) {
                        for (JsonNode item : value) {
                            Element child = doc.createElement(key);
                            parent.appendChild(child);
                            processNode(doc, child, item);
                        }
                    } else {
                        Element child = doc.createElement(key);
                        parent.appendChild(child);
                        processNode(doc, child, value);
                    }
                }
            }
            
            if (textContent.length() > 0) {
                parent.setTextContent(textContent.toString());
            }
        } else if (node.isValueNode()) {
            parent.setTextContent(node.asText());
        }
    }
    
    private String documentToString(Document doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }
}