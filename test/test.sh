#!/bin/bash

# ==============================
# Répertoires et fichiers
# ==============================
SRC_DIR="../src"
BIN_DIR="../bin"
LIB_JAUDIO="../lib/jaudiotagger-2.2.6.jar"

# JavaFX (SDK Windows)
JAVA_FX_LIB="../lib/javafx/lib"
JAVA_FX_BIN="../lib/javafx/bin"

MP3_TEST="test.mp3"
REPERTOIRE="../ressources/Musique"
PLAYLIST_OUT="playlist_test.m3u"
PLAYLIST_XSPF="playlist_test.xspf"
PLAYLIST_JSPF="playlist_test.jspf"

# ==============================
# Nettoyage du dossier bin
# ==============================
echo "=============================="
echo " Compilation du projet"
echo "=============================="

rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

# Compilation avec JavaFX et Jaudiotagger
javac -d "$BIN_DIR" \
  --module-path "$JAVA_FX_LIB" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$LIB_JAUDIO" \
  $(find "$SRC_DIR" -name "*.java")

if [ $? -ne 0 ]; then
    echo " Erreur de compilation"
    exit 1
fi

echo " Compilation réussie"
echo

# ==============================
# Vérification MP3 de test
# ==============================
if [ ! -f "$MP3_TEST" ]; then
    echo " MP3 de test '$MP3_TEST' introuvable."
    echo "   Veuillez placer un fichier MP3 de test."
fi

# Classpath (Windows => ;)
CLASSPATH="$BIN_DIR;$LIB_JAUDIO"

# ==============================
# Tests CLI
# ==============================
echo "=============================="
echo " Test 1 : Aide (-h)"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -h
echo

echo "=============================="
echo " Test 2 : Sans arguments"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest
echo

echo "=============================="
echo " Test 3 : Mode fichier (-f)"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -f "$MP3_TEST"
echo

echo "=============================="
echo " Test 4 : Mode répertoire (-d)"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -d "$REPERTOIRE"
echo

echo "=============================="
echo " Test 6 : Mode répertoire + export XSPF"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -d "$REPERTOIRE" -o "$PLAYLIST_XSPF"
echo

echo "=============================="
echo " Test 7 : Mode répertoire + export JSPF"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -d "$REPERTOIRE" -o "$PLAYLIST_JSPF"
echo

echo "=============================="
echo " Test 8 : Mode répertoire + export M3U"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -d "$REPERTOIRE" -o "$PLAYLIST_OUT"
echo

echo "=============================="
echo " Test 9 : Argument invalide"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -x
echo

# ==============================
# Test Application GUI (JavaFX)
# ==============================
echo "=============================="
echo " Test 10 : Application GUI"
echo "=============================="
echo " (Test interactif : fermez la fenêtre pour continuer)"

java --enable-native-access=javafx.graphics \
     -Djava.library.path="$JAVA_FX_BIN" \
     --module-path "$JAVA_FX_LIB" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "$CLASSPATH" \
     scr.Application

echo
echo "=============================="
echo " Tests terminés"
echo "=============================="
