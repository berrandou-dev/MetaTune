#!/bin/bash

# Répertoires et fichiers
CLASSES_DIR=$HOME/classes
MP3_TEST=test.mp3
REPERTOIRE_TEST=Musique

echo "=============================="
echo " Compilation du projet"
echo "=============================="
javac -d $CLASSES_DIR -cp $CLASSES_DIR *.java

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
java -cp $CLASSES_DIR scr.ApplicationCLITest -h
echo

echo "=============================="
echo " Test 2 : Sans arguments"
echo "=============================="
java -cp $CLASSES_DIR scr.ApplicationCLITest
echo

echo "=============================="
echo " Test 3 : Mode fichier"
echo "=============================="
java -cp $CLASSES_DIR scr.ApplicationCLITest -f $MP3_TEST
echo

echo "=============================="
echo " Test 4 : Mode répertoire"
echo "=============================="
java -cp $CLASSES_DIR scr.ApplicationCLITest -r $REPERTOIRE_TEST/
echo

echo "=============================="
echo " Test 5 : Argument invalide"
echo "=============================="
java -cp $CLASSES_DIR scr.ApplicationCLITest -x
echo

echo "=============================="
echo " Tests terminés"
echo "=============================="
