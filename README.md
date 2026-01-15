This project was carried out by a team of 5, from September 2025 to December 2025. It was forked from an existing private repository.
# Alarm Management System
This API manages a **fire alarm system** for a university campus. It provides the **necessary actions to take in case of fire** based on received events. The API includes **three services**: the first handles fire management, the second allows emergency visualization, and the third manages access control.


## Starting the Project

You can start the server (`ApplicationServer`).

The `main()` method does not require any arguments.
A Docker file is provided.

You can also run the server via Maven :

```bash
mvn clean install
mvn exec:java -pl application
```

## Usage

The fire management system route is:\
POST `/evenement-securite`
```java
{
  "nom": "<event name>::string",
  "heure": "<ISO-8601>::string",
  "zone": "<zone id>::string",
  "parametres": [
    {"parametre": "<parameter name>::string", "valeur": "<value>::string"},
    {"parametre": "<parameter name>::string", "valeur": "<value>::string"}
  ]
}
```
Full documentation of the different events can be found [here](https://projet2025.qualitelogicielle.ca/enonce/evenements/).\
__Response__\
200 OK
```java
{
  "actions": [
    {
      "nom": "<action name>::string",
      "parametres": [
        {"parametre": "<parameter name>::string", "valeur": "<value>::string"},
        {"parametre": "<parameter name>::string", "valeur": "<value>::string"}
      ]
    }
  ]
}
```
Full documentation of the different actions can be found [here](https://projet2025.qualitelogicielle.ca/enonce/actions/).

## Features

- Handling pre-fire alarm events.
- Handling expired pre-alarm events (from fire).
- Handling canceled pre-alarm events.
- Handling confirmed pre-alarm events (from fire).
- Handling smoke detection events.
- Handling fire alarm events.
- Handling completed fire events.
- Management of the database containing the current system states.
- Management of the database containing individual users.
- Emergency visualization service.
- Service for managing entry/exit in buildings.
- Management of building occupancy.
- System reset.
