# 📌 XML ⇄ JSON Converter

Application Java avec interface graphique JavaFX pour la conversion bidirectionnelle XML/JSON

## 🗓 Deadline: January 5, 2026

## 🎯 Description

Cette application JavaFX permet de convertir des documents XML en JSON et inversement, en implémentant deux approches complémentaires :

- une approche **from scratch**, basée sur DOM et un parsing manuel,
- une approche basée sur des **APIs (Jackson)**, combinée à une structuration légère.

L'objectif est de comparer une approche pédagogique bas niveau et une approche plus proche des pratiques professionnelles.

## ✨ Fonctionnalités principales

- ✅ Conversion XML → JSON
- ✅ Conversion JSON → XML
- ✅ Interface graphique intuitive avec JavaFX
- ✅ Chargement de fichiers depuis le système
- ✅ Sauvegarde des résultats
- ✅ Affichage en temps réel du contenu et des résultats
- ✅ Gestion des erreurs avec messages clairs
- ✅ Support des attributs XML
- ✅ Support des tableaux JSON
- ✅ Formatage automatique du code (indentation)
- ✅ Sélection de la méthode de conversion (From Scratch / With API)
- ✅ Comparaison pédagogique des deux approches

## 🛠 Technologies utilisées

- **Java 17** : Langage de programmation
- **JavaFX 17** : Framework pour l'interface graphique
- **Maven** : Gestionnaire de dépendances et build
- **DOM Parser** : Pour le parsing XML (from scratch)
- **Parser JSON manuel** : Implémentation custom (from scratch)
- **Jackson** (approche API)

## 📦 Structure du projet

```
XML_JSON_Converter/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── application/
│   │   │   │   └── Main.java                 # Point d'entrée JavaFX
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── ConverterController.java  # Contrôleur de l'interface graphique
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── XmlToJsonService.java     # Conversion XML → JSON (from scratch)
│   │   │   │   └── JsonToXmlService.java     # Conversion JSON → XML (from scratch)
│   │   │   │
│   │   │   ├── service/api/
│   │   │   │   ├── XmlToJsonApiService.java  # Conversion XML → JSON (API-based)
│   │   │   │   └── JsonToXmlApiService.java  # Conversion JSON → XML (API-based)
│   │   │   │
│   │   │   └── util/
│   │   │       └── FileUtils.java            # Utilitaires de lecture/écriture de fichiers
│   │   │
│   │   └── resources/
│   │       ├── view/
│   │       │   └── main_view.fxml            # Interface graphique JavaFX (FXML)
│   │       │
│   │       └── sample/
│   │           ├── example.xml
│   │           ├── example.json
│   │           └── output.xml
│   │
├── pom.xml                                   # Configuration Maven (dépendances, JavaFX, plugins)
├── .gitignore
└── README.md
```

## 🚀 Installation et exécution

### Prérequis

- Java JDK 17 ou supérieur
- Maven 3.6+
- JavaFX SDK 17 (géré automatiquement par Maven)

### Étapes d'installation

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/votre-username/xml-json-converter.git
   cd xml-json-converter
   ```

2. **Compiler le projet avec Maven**
   ```bash
   mvn clean compile
   ```

3. **Exécuter l'application**
   ```bash
   mvn javafx:run
   ```

4. **Créer un JAR exécutable**
   ```bash
   mvn clean package
   ```
   Le JAR sera généré dans le dossier `target/`.

## 💻 Utilisation

### Interface principale

L'application propose une interface simple et intuitive :

**Charger un fichier :**
- Cliquez sur "Load XML" ou "Load JSON"
- Sélectionnez votre fichier dans l'explorateur

**Convertir :**
- Choisir la méthode de conversion
- Sélectionner **From Scratch** ou **With API** via le menu déroulant
- Cliquez sur "XML → JSON" pour convertir XML en JSON
- Cliquez sur "JSON → XML" pour convertir JSON en XML
- Le résultat s'affiche automatiquement dans la zone de sortie

**Sauvegarder :**
- Cliquez sur "Save" pour enregistrer le résultat
- Choisissez l'emplacement et le nom du fichier

**Effacer :**
- Cliquez sur "Clear" pour réinitialiser les zones de texte

## 🔧 Architecture technique

### Conversion XML → JSON

**From Scratch**
- Parsing XML avec DOM (DocumentBuilder)
- Parcours récursif de l'arbre
- Construction du JSON avec StringBuilder

**Using APIs**
- Parsing XML avec Jackson XmlMapper
- Structuration légère (attributs, texte)
- Génération JSON formatée avec ObjectMapper

### Conversion JSON → XML

**From Scratch**
- Parsing JSON manuel
- Création de structures Map / List
- Génération XML récursive avec indentation

**Using APIs**
- Parsing JSON avec ObjectMapper
- Génération XML avec XmlMapper
- Respect des conventions @attribute et #text

## 🎨 Captures d'écran

### Interface principale
*[Show Image]*

### Conversion XML → JSON
*[Show Image]*

### Conversion JSON → XML
*[Show Image]*

## ⚠️ Limitations connues

- Les commentaires XML et JSON ne sont pas préservés
- Les espaces de noms XML complexes ne sont pas totalement supportés
- Les nombres JSON peuvent être convertis en chaînes dans certains cas
- Taille maximale des fichiers recommandée: 10 MB

## 🔮 Améliorations futures

- [ ] Support des espaces de noms XML complets
- [ ] Validation de schéma (XSD/JSON Schema)
- [ ] Mode batch pour convertir plusieurs fichiers
- [ ] Support du format YAML
- [ ] Historique des conversions
- [ ] Thèmes personnalisables (mode sombre)
- [ ] Export en CSV

## 👨‍💻 Auteur

**BEN AKKA OUAYAD Mohammed**

- GitHub: [@akka-ben](https://github.com/akka-ben)
- Email: mohammed.benakkaouayad@usmba.ac.ma

## 📄 Licence

Projet académique – usage pédagogique.

---

### Pour toute question ou problème :

- Ouvrir une issue sur GitHub
- Contacter par email
- Consulter la documentation JavaFX

---

© 2025 - XML/JSON Converter | Projet JavaFX
