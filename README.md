# Système de gestion des alarmes (SGAUL)
Cet API permet de gérer un système d'alarme d'un campus universitaire. Elle permet de retourner les actions nécessaires à faire en cas d'incendie à partir d'événements reçu. L'API dispose de deux services, le premier s'occupe de faire la gestion des incendies, le deuxième permet de visualiser les urgences.


## Intégration Docker

Un Dockerfile est également fournis si vous désirez essayer de rouler votre code sur les mêmes images docker que nous utiliserons.

Pour ce faire:

```bash
docker build -t application-glo4002 .
docker run -t -p 8181:8181 application-glo4002
```

## Démarrer le projet

Vous pouvez démarrer le serveur (`ApplicationServer`).

Le `main()` ne demande pas d'argument.

Vous pouvez également rouler le serveur via maven :

```bash
mvn clean install
mvn exec:java -pl application
```

## Utilisation

La route du système de gestion d'incendie est la suivante.\
POST `/evenement-securite`
```java
{
  "nom": "<nom événement>"::string,
  "heure": "<ISO-8601>"::string,
  "zone": "<id zone>"::string,
  "parametres": [
    {"parametre": "<nom paramètre>"::string, "valeur": "<valeur>"::string},
    {"parametre": "<nom paramètre>"::string, "valeur": "<valeur>"::string},
  ]::array(object({parametre, valeur})
}
```
La documentation entière des différents évenements peut être trouvée [ici](https://projet2025.qualitelogicielle.ca/enonce/evenements/).\
__Réponse__\
200 OK
```java
{
  "actions": [
    {
      "nom": "<nom action>"::string,
      "parametres": [
        { "parametre": "<nom paramètre>"::string, "valeur": "<valeur>"::string},
        { "parametre": "<nom paramètre>"::string, "valeur": "<valeur>"::string},
      ]
    }
  ]
}
```
La documentation entière des différentes actions peut être trouvée [ici](https://projet2025.qualitelogicielle.ca/enonce/actions/).

## Fonctionnalités

- La gestion d'évenement de préalarme d'incendie.
- La gestion d'évenement de préalarme expirée (découlant d'Incendie).
- La gestion d'évenement de préalarme annulée.
- La gestion d'évenement de préalarme confirmée (découlant d'Incendie).
- La gestion d'évenement de présence de fumée.
- La gestion d'évenement d'alarme d'incendie.
- La gestion d'évenement d'incendie terminé.
- La gestion de la base de donnée contenant les états actuelles du système.
- La gestion de la base de donnée contenant les individus.
- Le service de visualisation des urgences.
- Le service de gestion des entrées/sorties dans les locaux.
- La gestion de l'occupation des locaux.
- La réinitialisation du système.
