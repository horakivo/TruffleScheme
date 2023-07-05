
|--------------------------------|
|      SPUŠTĚNÍ (bez build)      |
|--------------------------------|

1. Nejprve je potřeba nastavit proměnnou prostředí (environment variable) JAVA_HOME na JDK GraalVM verze 22.3.1.
Složka jdks obsahuje jednotlivá JDK pro různé architektury.

2. Poté je potřeba upravit proměnnou prostředí PATH: 'export PATH=$JAVA_HOME/bin:$PATH'.

3. Následně je možné spustit interpret dvěma způsoby:
    a. nad JVM pomocí skriptu: './src/TruffleScheme/scm src/TruffleScheme/language/tests/fib.scm'
    pozn. možná je nutné nastavit spustitelná práva tomuto souboru: chmod +x src/TruffleScheme/scm
    b. pomocí předem vytvořených spustitelných souborů (podporuje architektury aarch64 a amd64)
        Pro aarch64 ve složce bin/aarch64 vyhodnotit příkaz: './scm-native <soubor.scm>'
        Pro amd64 ve složce bin/amd64 vyhodnotit příkaz: './scm-native <soubor.scm>'

   pozn. jednotlivé složky obsahují soubor scm-component.jar, který lze využít k instalaci jazyka jako komponenty
   pomocí Graal Updater. (viz krok 7 v BUILD sekci)



|-----------------|
|      BUILD      |
|-----------------|

1. Nejprve je potřeba nastavit proměnnou prostředí (environment variable) JAVA_HOME na JDK GraalVM verze 22.3.1.
Složka jdks obsahuje jednotlivá JDK pro různé architektury.

2. Poté je potřeba upravit proměnnou prostředí PATH: 'export PATH=$JAVA_HOME/bin:$PATH'.

3. Dále je potřeba, aby počítač měl nainstalovaný Maven. Ten lze stáhnout z: https://maven.apache.org/download.cgi

4. Jelikož součástí build procesu je tvorba native-image, je potřeba nainstalovat komponentu s vuyžitím Graal Updater.
Ten je součástí distribuce GraalVM. Proto stačí provést příkaz: 'gu install native-image'

5. Přejdeme do složky src/TruffleScheme

6. Následně je možné projekt sestavit (build) pomocí příkazu: 'mvn clean package -DskipTests'
Tento příkaz vytvoří:
    a. ve složce /native spustitelný soubor 'scm_native' (AOT compiled)
    b. ve složce /component vytvoří soubor 'scm-component.jar', což je komponenta, kterou lze využít
    k instalaci TruffleScheme pomocí Graal Updater (viz níže).
pozn. testy byly vynechány z důvodu, že jejich součástí jsou i testy pro interoperabilitu. K tomu, aby testy mohly být
vykonány, je potřeba instalovat python a javascript komponenty pomocí příkazů:
    a. javascript: 'gu install js'
    b. python: 'gu install python'

7. V tuto chvíli je možné interpret spustit pomocí výše vygenerovaného programu 'scm_native'
Např.: ./native/scm_native /language/tests/fib.scm
8. Možné je také využít komponentu a jazyk nainstalovat pomocí Graal Updater.
Pomocí příkazu: 'gu install -L /component/scm-component.jar' je nainstalován TruffleScheme.
pozn. -L značí, že se jedná o instalaci z lokálního souboru
Úspěšná instalace lze ověřit dvěma způsoby:
    a. pomocí příkazu 'gu list'. Katalog by měl obsahovat komponentu s ID 'scm'
    b. Ve složce $JAVE_HOME/bin by měly dva nové soubory: 'scm' a 'scm_native'.
Poznámka: Komponentu lze odstranit pomocí příkazu: 'gu remove <componentId>'. Např: 'gu remove scm'.

9. V případě, že je komponenta úspěšně nainstalována, interpret je možné spustit dvěmi způsoby
    a. nad JVM příkazem: 'scm src/TruffleScheme/language/tests/fib.scm'
    b. pomocí native-image (AOT) příkazem: 'scm-native src/TruffleScheme/language/tests/fib.scm'
    pozn. jedná se o stejný spustitelný soubor jako v kroku 4.



|----------------------------------|
|      Vytvoření native-image      |
|----------------------------------|

Native-image vytvořený pomocí výše zmíněného 'mvn clean package' vytvoří spustitelný soubor obsahující implementaci
pouze jazyka Scheme. K vytvoření spustitelného souboru obsahující i jiné podporované jazyky, je potřeba využít skript ve
složce 'src/TruffleScheme/native/make_native_polyglot.sh'. Tento skript příjímá tři argumenty:
    a. --python pro instalici jazyka Python
    b. --js pro instalaci jazyka Javascript
    c. --ruby pro instalaci jazyka Ruby

ve složce src/TruffleScheme/native stačí proto vyhodnotit příkaz: './make_native_polyglot.sh --python --js' a bude vytvořený
native-image obsahující implementaci jazyka Scheme, Python a Javascript. Důležité je podotknout, že před vyhodnocením tohoto
příkazu je potřeba mít nainstalované komponenty js a python. Ty je možné nainstalovat pomocí Graal Updater.

Instalace komponenty python: 'gu install python'
Instalace komponenty js: 'gu install js'

