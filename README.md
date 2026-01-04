# 📌 XML ⇄ JSON Converter

Java application with JavaFX graphical interface for bidirectional XML/JSON conversion

## 🗓 Deadline: January 5, 2026

## 🎯 Description

This JavaFX application allows you to convert XML documents to JSON and vice versa, implementing two complementary approaches:

- a **from scratch** approach, based on DOM and manual parsing,
- an **API-based approach (Jackson)**, combined with lightweight structuring.

The goal is to compare a low-level pedagogical approach with an approach closer to professional practices.

## 🎥 Video Demonstration

👉 Watch the project demo video:

[![Watch the video](https://img.youtube.com/vi/Jbo_x-zQVsc/0.jpg)](https://youtu.be/Jbo_x-zQVsc)

## ✨ Key Features

- ✅ XML → JSON conversion
- ✅ JSON → XML conversion
- ✅ Intuitive graphical interface with JavaFX
- ✅ Load files from the system
- ✅ Save results
- ✅ Real-time display of content and results
- ✅ Error handling with clear messages
- ✅ XML attributes support
- ✅ JSON arrays support
- ✅ Automatic code formatting (indentation)
- ✅ Conversion method selection (From Scratch / With API)
- ✅ Pedagogical comparison of both approaches

## 🛠 Technologies Used

- **Java 17**: Programming language
- **JavaFX 17**: Graphical interface framework
- **Maven**: Dependency and build manager
- **DOM Parser**: For XML parsing (from scratch)
- **Manual JSON Parser**: Custom implementation (from scratch)
- **Jackson** (API approach)

## 📦 Project Structure

```
XML_JSON_Converter/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── application/
│   │   │   │   └── Main.java                 # JavaFX entry point
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── ConverterController.java  # GUI controller
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── XmlToJsonService.java     # XML → JSON conversion (from scratch)
│   │   │   │   └── JsonToXmlService.java     # JSON → XML conversion (from scratch)
│   │   │   │
│   │   │   ├── service/api/
│   │   │   │   ├── XmlToJsonApiService.java  # XML → JSON conversion (API-based)
│   │   │   │   └── JsonToXmlApiService.java  # JSON → XML conversion (API-based)
│   │   │   │
│   │   │   └── util/
│   │   │       └── FileUtils.java            # File read/write utilities
│   │   │
│   │   └── resources/
│   │       ├── view/
│   │       │   └── main_view.fxml            # JavaFX GUI (FXML)
│   │       │
│   │       └── sample/
│   │           ├── example.xml
│   │           ├── example.json
│   │           └── output.xml
│   │
├── pom.xml                                   # Maven configuration (dependencies, JavaFX, plugins)
├── .gitignore
└── README.md
```

## 🚀 Installation and Execution

### Prerequisites

- Java JDK 17 or higher
- Maven 3.6+
- JavaFX SDK 17 (automatically managed by Maven)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/akka-ben/XML_JSON_Converter.git
   cd XML_JSON_Converter
   ```

2. **Compile the project with Maven**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

4. **Create an executable JAR**
   ```bash
   mvn clean package
   ```
   The JAR will be generated in the `target/` folder.

## 💻 Usage

### Main Interface

The application offers a simple and intuitive interface:

**Load a file:**
- Click on "Load XML" or "Load JSON"
- Select your file in the file explorer

**Convert:**
- Choose the conversion method
- Select **From Scratch** or **With API** via the dropdown menu
- Click on "XML → JSON" to convert XML to JSON
- Click on "JSON → XML" to convert JSON to XML
- The result is automatically displayed in the output area

**Save:**
- Click on "Save" to save the result
- Choose the location and file name

**Clear:**
- Click on "Clear" to reset the text areas

## 🔧 Technical Architecture

### XML → JSON Conversion

**From Scratch**
- XML parsing with DOM (DocumentBuilder)
- Recursive tree traversal
- JSON construction with StringBuilder

**Using APIs**
- XML parsing with Jackson XmlMapper
- Lightweight structuring (attributes, text)
- Formatted JSON generation with ObjectMapper

### JSON → XML Conversion

**From Scratch**
- Manual JSON parsing
- Creation of Map/List structures
- Recursive XML generation with indentation

**Using APIs**
- JSON parsing with ObjectMapper
- XML generation with XmlMapper
- Respect for @attribute and #text conventions

## 🎨 Screenshots

### Main Interface
<img width="891" height="720" alt="Screenshot 2026-01-04 at 14 16 38" src="https://github.com/user-attachments/assets/775e618a-a3db-4d28-88d0-bf709bdee9a0" />

### XML → JSON Conversion
<img width="923" height="792" alt="Screenshot 2026-01-04 at 14 17 46" src="https://github.com/user-attachments/assets/db6a523b-64c8-4fdc-bcb7-bef738e69a46" />

### JSON → XML Conversion
<img width="923" height="792" alt="Screenshot 2026-01-04 at 14 18 13" src="https://github.com/user-attachments/assets/fe6318b2-17eb-40d5-8a87-393daf153786" />

## ⚠️ Known Limitations

- XML and JSON comments are not preserved
- Complex XML namespaces are not fully supported
- JSON numbers may be converted to strings in some cases
- Recommended maximum file size: 10 MB

## 🔮 Future Improvements

- [ ] Full XML namespace support
- [ ] Schema validation (XSD/JSON Schema)
- [ ] Batch mode to convert multiple files
- [ ] YAML format support
- [ ] Conversion history
- [ ] Customizable themes (dark mode)
- [ ] CSV export

## 👨‍💻 Author

**BEN AKKA OUAYAD Mohammed**

- GitHub: [@akka-ben](https://github.com/akka-ben)
- Email: mohammed.benakkaouayad@usmba.ac.ma

## 📄 License

Academic project – educational use.

---

### For any questions or issues:

- Open an issue on GitHub
- Contact by email
- Consult the JavaFX documentation

---

© 2025 - XML/JSON Converter | JavaFX Project
