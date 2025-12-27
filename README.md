# MetaTune – Gestion de playlists et métadonnées MP3

**Projet POO & Java – L2-I 2025-2026**
 
**Auteurs : BOUATMANE Nesrine & BERRANDOU Nassim**

**Groupe TD : Groupe TD B-10**

---

## Description du projet

MetaTune est une application Java permettant de :

- Extraire et afficher les métadonnées de fichiers MP3 (titre, artiste, album, année, durée, genre, numéro de piste, pochette).
- Explorer des répertoires pour détecter tous les fichiers MP3 et générer automatiquement une playlist.
- Créer des playlists personnalisées (M3U8, XSPF ou JSPF) en sélectionnant les morceaux souhaités.
- Charger des playlists existantes pour les visualiser dans l’interface graphique.

L’application propose deux modes :

1. **Mode console (CLI)** – Pour manipuler fichiers et playlists depuis un terminal.
2. **Mode graphique (GUI)** – Pour explorer, visualiser et gérer les MP3 et playlists via une interface JavaFX.

---

## Prérequis

- Java SE 21 ou supérieur.
- Les JAR fournis contiennent toutes les dépendances nécessaires, donc aucun module-path n’est requis pour exécuter les JAR.

---

## Contenu du projet

Le projet contient les fichiers et dossiers suivantes:

```bash
MetaTune/
│
├─ src/ # Code source Java
│
├─ bin/ # Fichiers compilés (.class)
│
├─ lib/ # Bibliothèques externes
│
├─ ressources/ # Ressources supplémentaires
│ └─ Musique/ # Musiques utilisées par l'application
│
├─ diagrammes/ # Diagrammes du projet
│ ├─ diagramme_classes.png
│ ├─ diagramme_cas_utilisation.png
│ └─ diagramme_gantt.png
│
├─ jar/ # Fichiers JAR exécutables
│ ├─ gui.jar
│ └─ cli.jar
│
├─ rapport/ # Rapport de projet (docx/pdf)
│
├─ javadoc/ # Documentation Java générée
│
├─ test/ # Musiques et playlists de test
│
├─ README.md # Ce fichier
└─ sujet_projet.pdf # Sujet du projet
```

---

## Installation

1. Télécharger ou cloner le projet depuis le dépôt.
2. Assurez-vous que Java SE 21 est installé et disponible dans le PATH.
3. Les fichiers `gui.jar` et `cli.jar` sont prêts à l’emploi.

---

## Usage

### Mode console (CLI)

```bash
# Afficher l'aide
java -jar cli.jar -h

# Analyser un fichier MP3
java -jar cli.jar -f "chemin/vers/fichier.mp3"

# Explorer un répertoire pour générer une playlist par défaut
java -jar cli.jar -d "chemin/vers/dossier/"

# Générer une playlist dans un format spécifique
java -jar cli.jar -d "chemin/vers/dossier/" -o "chemin/vers/playlist.(m3u8, xspf, jspf)"
```

---

### Mode graphique (GUI)

```bash
# Lancer l'application GUI
java --module-path ../lib/javafx/lib --add-modules javafx.controls,javafx.fxml -jar gui.jar
```
---

## Auteurs  

BOUATMANE Nesrine
BERRANDOU Nassim
