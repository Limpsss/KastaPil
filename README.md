Beskrivning:

Denna app är till för dig som tycker om att spela dart. Appen beräknar och håller koll på dina poäng när du spelar dart. Du kan antingen spela själv och träna upp din kastarm utan och hålla koll på hur du faktiskt kastar utan att själv behöva räkna, eller så kan du spela mot din kompis och tävla vem som är den bästa dartspelaren. 

I appen så sätter upp ett spel i en meny med olika regler, sedan kommer du in i ett spel och kan börja att beräkna poängen. Tanken är att du i verkliga liver spelar dart. Du kommer att kasta din pilar och i appen manuellt trycka in vart du träffade på tavlan så kommer appen att räkna ihop poängen automatiskt så du slipper beräkna poängen själv. När du har spelat klart ett spel och någon har vunnit så kommer en del data från matchen att sparas i en databas. All data som sparas kan du också ta del utav i statistiksidan i appen. Där kan du kolla vart på tavlan du oftast träffar, hur många pilar du kastat, vilken dubbel du oftast går ut på och liknande statistik.

Jag hade ambitionen för en 5a, men eftersom jag kör kotlin och jetpack compose så fanns det mindre tekniska krav att välja bland men jag hoppas det räcker.

Avklarade projektkrav (enligt mig)
Tekniska krav:
1. "Modulär kod". Jag använder mig av MVVM architecture som google rekommenderar
2. "Portabla Fragment". 
3. "Hantering av bakåtknapp". Jag hanterar backstacken och jag anropar dialoger så att bakåtknappen inte förstör användarupplevelsen.
4. "Hantera, dvs. hålla koll på inloggningsstatus". Jag håller koll på om en användare är inloggad via google eller enbart som gäst. Jag har implementerat så att endast inloggade användare kan spara och se sin statistik.
5. "Eget förslag på någon teknisk finess" Hur jag löste touch-hantering så jag vet vart på tavlan jag släppte fingret för att räkna poäng i InGameScreen.

Entreprenöriella krav:
1. "Firestore" Jag använder mig av firestore för att spara data.
2. "Multispråk-stöd" Appen stödjer både svenska och engelska.
3. "UX inställning" Appen stödjer mörkt och ljust tema.
4. "Kontohantering" Jag sparar statistik för varje inloggad användare som visas på statistiksidan
5. "Tredjepartsinloggning" Inloggning med google

