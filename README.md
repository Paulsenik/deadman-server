# Dead-Man Switch

A simple Dead-Man Switch that notices, when a user has not reported its status over http and sends Emergency-Mails to
the users's specified Mail-List

***Local Testing with username "testuser"***:

_Test_
> http://localhost:8000/testuser/test?key=testkey

_Alive-Check_
> http://localhost:8000/testuser/alive?key=testkey

## Server

Server expects HTTP-GET-Request:

- The User makes Alive-Checks with:

> /$USER$/alive?key=$KEY$&device=$DEVICE$&message=$MESSAGE$

- The User triggers a Test-Mail:

> /$USER$/test?key=$KEY$&device=$DEVICE$&message=$MESSAGE$

Where:

- **$USER$** : is the Username of the Person (Without Spaces)
- **$KEY$** : is a private key/phrase only the user knows
- **$DEVICE$** : is the type of device the user sent the alive-message
- **$MESSAGE$** : is a optional message

## TODO

- Dockerisiert
    - Setzen von Empfängern, Zeiten & Rules
    - Text o. Dateien setzten
    - Manuelles Testen der Vorwarnung
    - Monatliche Test-Mail
- Android-App für "Alive-Check"
    - Rules:
        - Öffnen bestimmter App
        - Online-Zeit pro Tag
        - Manuelles bestätigen mit Code
    - Manuelles Testen der Vorwarnung
- Send Mails
    - Wenn Warnzeit verstrichen ist an USER
    - 1 Tag vor Veröffentlichung an USER,RECEIVER
    - Tag der Veröffentlichung an USER,RECEIVER
- Server erwartet HTTP-Get-Request
    - Schlüssel für Authentifizeirung
    - (Zeitstempel)
    - Gerät
    - (Standort)