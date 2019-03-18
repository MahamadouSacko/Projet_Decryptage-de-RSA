Decryptage RSA/DES

pour lancer le programme, l'utiliser doit utiliser un terminal ensuite taper la ligne de commande suivante:
java -jar "ProjetDecryptage.java" pour demarer le serveur du programme.
une fois que le serveur est demaré l'utilisateur peut lancer netcat.  
apres avoir lancer le programme, l'utilisateur doit se connecter au serveur avec: localhost 1234
Ensuite il faut entrer le nom de l'utilisateur en tapant
NAME
nom_utilisateur
apres pour tester le RSA l'utilisateur doit entrer les informations suivantes:longueur de la cle,valeur de E et la valeur de N en tapant

KEY RSA
longueur de la cle (512)
valeur de E
valeur N est les produit des deux nombre premier

ensuite il doit taper JOIN RSA pour participer au decryptage de la cle.
Pour lancer le decryptage il doit taper REQUEST RSA.

si il lance une deuxieme, troisieme.... fenetre de netcat, lui aussi il doit se connecter au serveur et entrer son nom d'utilisateur comme indiquer plus haut apres  il suffura juste de taper JOIN RSA 
pour faire parti de ceux qui decrypte la clé ensuite il lance le decryptage en tapant REQUEST RSA

Protocole du programme
NAME
Nom_utilisateur
Cette commande envoie le nom de l’utilisateur au serveur. Si l’utilisateur a déjà un nom, alors il change de nom.
NAME OK
Cette commande envoyée par le serveur indique que le nom est accepté.NAME BAD
Cette commande envoyée par le serveur indique que le nom est rejeté.
ULIST
Utilisateur1
Utilisateur2
...
Utilisateurn
.
Par cette commande le serveur envoie la liste des utilisateurs au client. La liste se termine par le symbole “.” en début de ligne.
AULIST
Cette commande demande au serveur la liste des utilisateurs.
KEY RSA
Cette commande envoie au serveur la taille de la clé, la valeur de E et la valeur de N.
JOIN RSA
Cette commande envoie au serveur une demande pour faire partir du groupe de calcul.
REQUEST RSA
Cette commande envoie au serveur une demande pour faire le calcul (pour trouver le P et Q).
QUIT
Cette commande termine la session.Pour tester le code pour le décryptage de code RSA, l’utilisateur doit lancer « netcat » après avoir démarrer le serveur.
Une fois l’application netcat lancé l’utilisation se doit de respecter le protocole décrit ci-dessus.

