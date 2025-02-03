# musicphone
*musicphone* è un'applicazione desktop Java per appassionati di musica che vogliono scoprire nuovi artisti e costruire itinerari musicali.

## Descrizione
A partire da un artista in riproduzione, l'utente può chiedere a *musicphone* di raccomandargli degli artisti che potrebbero piacergli. Per raccomandare degli artisti, *musicphone* considera gli artisti favoriti degli utenti di _last.fm_ che sono top fan dell'artista in riproduzione. Per ogni artista raccomandato, *musicphone* mostra i suoi prossimi concerti (le informazioni sui concerti sono presi da _last.fm_). Inoltre, l'utente può chiedere a *musicphone* di costruire un itinerario per i concerti di artisti da lui selezionati. L'itinerario costruito da *musicphone* include il primo concerto di ciascun artista selezionato il cui giorno non è già occupato da un altro concerto nell'itinerario (in altre parole, un itinerario non può includere due concerti di artisti diversi nello stesso giorno). Per ogni concerto nell'itinerario, *musicphone* calcola la distanza dall'attuale posizione in miglia o chilometri.

## Istruzioni per gli sviluppatori
Il progetto è basato su *Maven*. Seguono alcune istruzioni utili per gli sviluppatori.

| Per fare qusto | Fai questo |
| -----------|-----------|
| Per ripulire il progetto | Digita `mvn clean` |
| Per modificare il codice sorgente | Modifica un file, o più file, in `src/main/java`.<br> Il main file, _App.java_, si trova nel package _app_ |
| Per compilare il progetto | Digita `mvn compile` |
| Per eseguire il progetto | Digita `mvn compile exec:java` |

**Nota che:** questa versione di *musicphone* usa un dump di _last.fm_ (vedi la cartella `xmlData`) con concerti risalenti al 2009.
