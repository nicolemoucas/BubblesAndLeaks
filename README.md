# Bubbles & Leaks 🫧

Bienvenue à Bubbles & Leaks ! 🫧

L'application **Bubbles & Leaks** vous permet de simuler le remplissage d'une baignoire qui fuit parallèlement.

Elle a été réalisée en Java avec JavaFX pour faire l'interface graphique interactive pour l'utilisateur.

## Table des matières
- [Installation](#installation)
- [Utilisation](#utilisation)
    - [Utiliser le simulateur](#utiliser-simulateur)
    - [Graphique à partir du fichier CSV](#graphique-csv)
- [À propos](#propos)


## [Installation](#installation)
Pour utiliser Bubbles & Leaks, vous aurez besoin de [Java17](https://www.oracle.com/fr/java/technologies/downloads/#java17) et de [Maven](https://maven.apache.org/download.cgi).

Pour installer Bubbles & Leaks vous devez suivre les étapes suivantes :

1. Récupérer le dossier `bubbles-1.0-SNAPSHOT-src.zip` puis le dézipper.
2. Ouvrir le dossier avec un environnement de développement tel que [Eclipse](https://eclipseide.org/), [Visual Studio Code](https://code.visualstudio.com/download) ou [IntelliJ](https://www.jetbrains.com/idea/download/?section=mac).
3. Lancer la commande `mvn install` pour installer les dépendances du projet et le compiler.

## [Utilisation](#utilisation)
La baignoire a une capacité maximale et d'un nombre de robinets et de fuites qui sont à saisir par l'utilisateur.

### [Utiliser le simulateur](#utiliser-simulateur)
Pour utiliser l'application il faut suivre les étapes suivantes :

1. Déplacez vous dans le dossier `bin` avec `cd bindist/bin`
2. Utilisez la commande `./bubbles` pour lancer l'application. Vous pouvez ajouter l'option `-d` ou `--debug` pour lancer l'application en mode débogage, ce qui permet de voir plus de détails dans la console pendant l'exécution du programme.
3. Saisissez la capacité de la baignoire souhaitée pour la simulation ainsi que le nombre de robinets et de fuites.
4. Allez sur la fenêtre de l'interface graphique et cliquez sur "Commencer !".
5. Vous verrez à gauche une liste de fuites et une liste de robinets. Pour modifier le débit de chacune sélectionnez la fuite ou robinet souhaité puis faites glisser le _slider_.
6. Une fois que vous aurez fini de modifier les débits, cliquez sur "Démarrer" pour commencer la simulation.
7. Vous pouvez toujours modifier le débit des robinets, mais pas celui des fuites. Celles-ci peuvent maintenant être réparées, il suffit de sélectionner la fuite que vous voulez réparer.

### [Graphique à partir du fichier CSV](#graphique-csv)
À la fin de la simulation, si elle s'est exécutée sans erreurs, vous trouverez le fichier `simulationBaignoire.csv` dans le dossier `/src/java` avec l'évolution du niveau de la baignoire et le temps. Celui-ci vous permettra de faire un graphique pour voir l'évolution du niveau de la baignoire en fonction du temps.

Pour générer le graphique, suivez les étapes suivantes :

1. Ouvrez le fichier CSV dans un logiciel de feuilles de calcul tel que [Excel](https://www.microsoft.com/en-us/microsoft-365/excel) ou [Google Sheets](https://docs.google.com/spreadsheets/).
2. Cliquez sur `Insérer` pour insérer un graphique en courbe (_line chart_).
3. Vérifiez que l'axe des abscisses (_x_) corresponde au temps et que l'axe des ordonnées (_y_) corresponde au niveau de la baignoire.


## [À propos](#propos)
Ce projet a été créé par Nicole CASTRO MOUCHERON dans le cadre du cours de Programmation Orientée Objet Avancée pour le Master Méthodes Informatiques Appliquées à la Gestion des Entreprises I (MIAGE I) .

Le projet a été réalisé entre mai et juin 2024. 
