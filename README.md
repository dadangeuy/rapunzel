# Rapunzel
Rapunzel is a web-based public scoreboard for judgels programming contest.

Rapunzel is build to minimalize request from public traffic to judgels server, and can be deployed on separate machine.

To minimalize request traffic, rapunzel use cache, so the scoreboard isn't up to date with real-time contest scoreboard. Contestant submission will be updated every 5 minute, and user data will be updated every 15 minute.

## How To Run
### Build Project
* `git pull [RAPUNZEL GIT]`
* `cd [RAPUNZEL DIRECTORY]`
* `./gradlew bootJar`

### Add Custom Properties
* Create application.properties, and override following variables with your own
  * `nano application.properties`
    ```
    server.port=[OPTIONAL, DEFAULT VALUE IS 8080]
    
    judgels.jophiel.host=[JOPHIEL URL]
    judgels.uriel.host=[URIEL URL]
    judgels.uriel.scoreboardSecret=[VALUES FROM URIEL application.conf -> uriel.scoreboardSecret]
    
    rapunzel.scoreboard.paths=[LIST OF DESIRED SCOREBOARD URL]
    rapunzel.scoreboard.jids=[LIST OF CONTEST JID (from uriel)]
    rapunzel.scoreboard.titles=[LIST OF CONTEST TITLE]
    rapunzel.scoreboard.types=[LIST OF SCOREBOARD TYPE (FROZEN or OFFICIAL)]
    rapunzel.scoreboard.defaultPath=[DEFAULT PATH]
    
    rapunzel.web.logos=[LIST OF LOGO SHOWN IN SCOREBOARD HEADER]
    rapunzel.icon=[WEB ICON]
    ```
  * example properties:
    ```
    server.port=14045
    
    judgels.jophiel.host=http://cpsso-gemastik.its.ac.id/
    judgels.uriel.host=http://cpcompetition-gemastik.its.ac.id/
    judgels.uriel.scoreboardSecret=rahasia-dong

    rapunzel.scoreboard.paths=ujicoba,penyisihan
    rapunzel.scoreboard.jids=JIDCONT001,JIDCONT002
    rapunzel.scoreboard.titles=Uji Coba Lomba Pemrograman,Penyisihan Lomba Pemrograman - Gemastik 11
    rapunzel.scoreboard.types=FROZEN,FROZEN
    rapunzel.scoreboard.defaultPath=penyisihan

    rapunzel.web.logos=https://arek.its.ac.id/gemastik/images/ristekdikti.png,https://arek.its.ac.id/gemastik/images/logo-its-putih-transparan.png,https://arek.its.ac.id/gemastik/images/gemastikwhite@3x.png
    rapunzel.web.icon=https://arek.its.ac.id/gemastik/images/gemastik.png
    ```
* Put application.properties inside `[RAPUNZEL JAR DIRECTORY]` or `/var/rapunzel/`
  * `mv application.properties build/libs/` or `mv application.properties /var/rapunzel/`

### Run Project
* `cd build/libs`
* `java -jar rapunzel-[VERSION].jar`
* Open rapunzel in browser, by default the link is `http://localhost:8080`

## Screenshot
![Scoreboard Penyisihan Gemastik 11](https://image.ibb.co/catfKU/Screenshot-from-2018-10-12-11-48-56.png)

![Rapunzel Image](https://media.giphy.com/media/Ic0krtgPvuLSg/giphy.gif)
