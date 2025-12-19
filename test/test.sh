#!/bin/bash

# Répertoires et fichiers
SRC_DIR=../src
BIN_DIR=../bin
LIB_EXT=../lib/jaudiotagger-2.2.6.jar

MP3_TEST=test.mp3
REPERTOIRE=../ressources/Musique
PLAYLIST_OUT=playlist_test.m3u

echo "=============================="
echo " Compilation du projet"
echo "=============================="

# Nettoyage du dossier bin
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

# Compilation
javac -d "$BIN_DIR" -cp "$LIB_EXT" $(find "$SRC_DIR" -name "*.java")
if [ $? -ne 0 ]; then
  echo " Erreur de compilation"
  exit 1
fi

echo " Compilation réussie"
echo

# Vérifie si le MP3 de test existe
if [ ! -f "$MP3_TEST" ]; then
  echo " MP3 de test '$MP3_TEST' introuvable."
  echo "   Veuillez placer un fichier MP3 de test."
fi

CLASSPATH="$BIN_DIR:$LIB_EXT"

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
echo " Test 5 : Mode répertoire + export (-o)"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -d "$REPERTOIRE" -o "$PLAYLIST_OUT"
echo

echo "=============================="
echo " Test 7 : Argument invalide"
echo "=============================="
java -cp "$CLASSPATH" scr.ApplicationCLITest -x
echo

echo "=============================="
echo " Tests terminés"
echo "=============================="

