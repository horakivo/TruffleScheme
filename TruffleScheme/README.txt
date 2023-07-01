SPUŠTĚNÍ (bez build)

1. Nejprve je potřeba nastavit proměnnou prostředí (environment variable) JAVA_HOME na JDK GraalVM verze 22.3.1.
Složka jdks obsahuje jednotlivá JDK pro různé architektury.

2. Následně je možné spustit interpretr dvěma způsoby:
    a. nad JVM pomocí skriptu: './src/TruffleScheme/scm src/TruffleScheme/language/tests/fib.scm'
    pozn. nutné nastavit exekuční práva tomuto souboru: chmod +x src/TruffleScheme/scm





|-----------------|
|      BUILD      |
|-----------------|

1. Nejprve je potřeba nastavit proměnnou prostředí (environment variable) JAVA_HOME na JDK GraalVM verze 22.3.1.
Složka jdks obsahuje jednotlivá JDK pro různé architektury.
2. Dále je potřeba, aby počítač měl nainstalovaný Maven. Ten lze stáhnout z: https://maven.apache.org/download.cgi
3. Následně je možné projekt sestavit (build) pomocí příkazu: 'mvn clean package' ve složce src/TruffleScheme
Tento příkaz vytvoří:
    a. ve složce src/TruffleScheme/native exekuční soubor 'scm_native' (AOT compiled)
    b. ve složce src/TruffleScheme/component vytvoří soubor 'scm-component.jar', což je komponenta, která lze využít
    k instalaci TruffleScheme pomocí Graal Updater (viz níže).

4. V tuto chvíli je možné interpret spustit pomocí výše vygenerovaného exekučního programu 'scm_native'
Např.: ./TruffleScheme/native/scm_native TruffleScheme/language/tests/fib.scm

5. Možné je také využít komponentu a jazyk nainstalovat pomocí Graal Updater, který je součástí GraalVM.
Pomocí příkazu: 'gu install -L TruffleScheme/component/scm-component.jar' je nainstalován TruffleScheme.
Úspěšná instalace lze ověřit dvěma způsoby:
    a. pomocí příkazu 'gu list'. Katalog by měl obsahovat komponentu s ID 'scm'
    b. Ve složce $JAVE_HOME/bin by měly dva nové soubory: 'scm' a 'scm_native'.
Poznámka: Komponentu lze odstranit pomocí příkazu: 'gu remove <componentId>'. Např: 'gu remove scm'.

6. V případě, že je komponenta úspěšně nainstalována, interpret možné spustit dvěmi způsoby
    a. nad JVM příkazem: 'scm src/TruffleScheme/language/tests/fib.scm'
    b. pomocí native-image (AOT) příkazem: 'scm-native src/TruffleScheme/language/tests/fib.scm'
    pozn. jedná se o stejný exekuční soubor jako v kroku 4.

