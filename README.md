# Rapunzel
Rapunzel is a web-based public scoreboard for judgels programming contest.

Rapunzel is build to minimalize request from public traffic to judgels server, and can be deployed on separate machine.

To minimalize request traffic, rapunzel use cache, so the scoreboard isn't up to date with real-time contest scoreboard. Contestant submission will be updated every 5 minute, and user data will be updated every 15 minute.

## How To Run
### Build Project
* `git pull [RAPUNZEL GIT]`
* `cd [RAPUNZEL DIRECTORY]`
* `./gradlew build`

### Add Custom Properties
* `cd build/libs/`
* Create application.properties, and override following variables with your own
  * `nano application.properties`
    ```
    server.port=[OPTIONAL, DEFAULT VALUE IS 8001]
    jophiel.host=[JOPHIEL URL]
    uriel.host=[URIEL URL]
    uriel.containerJid=[VALUES FROM CONTEST JID]
    uriel.scoreboardSecret=[VALUES FROM URIEL application.conf -> uriel.scoreboardSecret]
    uriel.scoreboardType=[SCOREBOARD TYPE (FROZEN OR OFFICIAL)]
    rapunzel.title=[SCOREBOARD TITLE]
    rapunzel.logos=[LINK TO LOGO_1],[LINK TO LOGO 2],[..ETC]
    ```

### Run Project
* `java -jar rapunzel-[VERSION].war`
* Open rapunzel in browser, by default the link is `http://localhost:8001`

## Screenshot
![Scoreboard Penyisihan Gemastik 11](https://image.ibb.co/catfKU/Screenshot-from-2018-10-12-11-48-56.png)
