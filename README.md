# MarketShop - Kotlin (Jetpack Compose)

**Auteur** : Koudousse Tchedre  
**Technologie** : Kotlin avec Jetpack Compose  

## Description
Application mobile de mini e-commerce développée avec Kotlin et Jetpack Compose, répondant aux exigences du projet. Elle permet de parcourir un catalogue de produits, gérer un panier, passer commande, consulter l’historique des commandes, et personnaliser son profil (mode sombre, modification des informations). Les données sont persistées localement (Room) et les produits proviennent de l’API publique FakeStoreAPI.

## Fonctionnalités implémentées

- [x] **Catalogue** : affichage en grille 2 colonnes, filtre par catégorie, indicateur de chargement, gestion d’erreur, navigation vers le détail.
- [x] **Détail produit** : image, titre, prix, description, catégorie, note, sélecteur de quantité, ajout au panier avec confirmation visuelle (Snackbar).
- [x] **Panier** : liste des produits (image, titre, prix unitaire, quantité, sous-total), modification des quantités, suppression d’articles, calcul du total général, bouton « Passer commande », message si vide.
- [x] **Commande** : formulaire (nom, téléphone, adresse, ville) avec validation de tous les champs, récapitulatif du panier, sauvegarde de la commande dans la base locale, vidage du panier, redirection vers l’historique.
- [x] **Historique** : liste des commandes passées (numéro, date, nombre d’articles, montant total), tri de la plus récente à la plus ancienne, clic pour afficher le détail des produits commandés, message si aucune commande.
- [x] **Profil** : affichage et modification du nom, email, téléphone, switch pour activer/désactiver le mode sombre (persistant), bouton « Vider mes données » avec confirmation, section « À propos » avec version de l’application.
- [x] **Authentification** : inscription/connexion locale obligatoire (nom utilisateur, email optionnel) avec stockage dans SharedPreferences.
- [x] **Persistance** : Room (panier et commandes), SharedPreferences (préférences utilisateur et thème).
- [x] **Appels réseau** : Retrofit + Gson pour l’API FakeStore.

## Bibliothèques utilisées

| Bibliothèque | Version |
|--------------|---------|
| Kotlin       | 1.9.0   |
| Jetpack Compose | 1.5.4 |
| Retrofit     | 2.9.0   |
| Room         | 2.6.1   |
| Coil         | 2.5.0   |
| Coroutines   | 1.7.3   |
| Navigation Compose | 2.7.6 |
| ViewModel Compose | 2.7.0 |

## Captures d’écran

*(Placez vos trois captures d’écran dans un dossier `screenshots/` à la racine du dépôt et décommentez les lignes suivantes)*

1. Écran Catalogue  
   ![Catalogue](screenshots/catalogue.png)
2. Écran Panier  
   ![Panier](screenshots/panier.png)
3. Écran Profil (mode sombre)  
   ![Profil](screenshots/profil.png)

## Difficultés rencontrées

1. **Gestion des conversions de type avec Room** : les méthodes `fromTimestamp` et `dateToTimestamp` étaient en conflit (même signature). Nous avons résolu le problème en gardant une seule méthode de conversion (`fromTimestamp`) et en supprimant le doublon.
2. **Compatibilité Kotlin / Compose Compiler** : une incompatibilité entre Kotlin 1.9.24 et le compilateur Compose 1.5.4 a provoqué une erreur de compilation. La solution a été de mettre à jour le compilateur Compose vers la version 1.5.14.
3. **Thème personnalisé absent dans AndroidManifest** : les ressources de thème n’étaient pas définies, causant une erreur de build. Nous avons supprimé les références à `Theme.MarketShop` dans le manifeste, car le thème est entièrement géré par Jetpack Compose.

## Améliorations possibles

- Ajouter une **connexion Google réelle** (Firebase Auth) pour une authentification plus professionnelle.
- Permettre à l’utilisateur de choisir une **photo de profil** (depuis la galerie ou l’appareil photo).
- Implémenter une **synchronisation en ligne** du panier et des commandes (Firebase Firestore) pour partager les données entre plusieurs appareils.
- Ajouter des **notifications push** pour confirmer les commandes.
- Optimiser la taille de l’APK (actuellement en mode debug) en générant une version release signée.

## Lien vers la version Flutter

[MarketShop Flutter](https://github.com/koudousse-ab/marketshop-flutter-Aboudoul-koudouse-TCHEDRE.git)

---

**Dépôt GitHub** : [marketshop-kotlin-prenom-nom](https://github.com/votre-compte/marketshop-kotlin-Aboudoul-koudouse-TCHEDRE)  
**Date de livraison** : 29 mai 2026
