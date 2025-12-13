#!/bin/bash

# Répertoires et fichiers
SRC_DIR=../src
BIN_DIR=../bin

LIB_EXT=../lib/jaudiotagger-2.2.6.jar
MP3_TEST=test.mp3
REPERTOIRE=../ressources/Musique

echo "=============================="
echo " Compilation du projet"
echo "=============================="
javac -d $BIN_DIR -cp $LIB_EXT $(find $SRC_DIR -name "*.java")

if [ $? -ne 0 ]; then
  echo "Erreur de compilation"
  exit 1
fi

echo " Compilation réussie"
echo

# Vérifie si le MP3 de test existe
if [ ! -f "$MP3_TEST" ]; then
  echo " MP3 de test '$MP3_TEST' introuvable. Téléchargez un MP3 avec ou sans ID3v1."
fi

echo "=============================="
echo " Test 1 : Aide (-h)"
echo "=============================="
java -cp $BIN_DIR:$LIB_EXT scr.ApplicationCLITest -h
echo

echo "=============================="
echo " Test 2 : Sans arguments"
echo "=============================="
java -cp $BIN_DIR:$LIB_EXT scr.ApplicationCLITest
echo

echo "=============================="
echo " Test 3 : Mode fichier"
echo "=============================="
java -cp $BIN_DIR:$LIB_EXT scr.ApplicationCLITest -f $MP3_TEST
echo

echo "=============================="
echo " Test 4 : Mode répertoire"
echo "=============================="
java -cp $BIN_DIR:$LIB_EXT scr.ApplicationCLITest -r $REPERTOIRE
echo

echo "=============================="
echo " Test 5 : Argument invalide"
echo "=============================="
java -cp $BIN_DIR:$LIB_EXT scr.ApplicationCLITest -x
echo

echo "=============================="
echo " Tests terminés"
echo "=============================="
