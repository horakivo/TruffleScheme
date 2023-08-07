## Překladač funkcionálního programovacího jazyka na platformě GraalVM

Autor práce: Bc. Ivo Horák <br>
Vedoucí práce: Mgr. Petr Krajča, Ph.D.

Univerzita Palackého v Olomouci <br>
Přírodovědecká fakulta <br>
Katedra informatiky <br>


## Spuštění (bez sestavení)
1. Nejprve je potřeba stáhnout [JDK GraalVM verze 22.3.1](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.3.1) pro Javu 17. 
2. Poté je potřeba nastavit proměnnou prostředí (environment variable) `JAVA_HOME` na JDK GraalVM verze 22.3.1. <br>

MacOS: 
```shell
export JAVA_HOME=path/to/jdk/graalvm-ce-java17-22.3.1/Contents/Home/
```

Linux: 
```shell
export JAVA_HOME=path/to/jdk/graalvm-ce-java17-22.3.1/
```
3. Poté je potřeba upravit proměnnou prostředí PATH

```shell
export PATH=$JAVA_HOME/bin:$PATH
```

Následně je možné spustit interpret dvěma způsoby: <br>

#### 1. Nad JVM:
```shell
cd src/TruffleScheme/
./scm language/tests/fib.scm
```

#### 2. Pomocí předem vytvořených spustitelných souborů
> **Pozn:** Binární soubory jsou sestaveny pro architekturu MacOS aarch64 (Apple silicon) a Linux amd64

Linux
```shell
cd bin/amd64
./scm_native ../../examples/fib.scm
```

MacOS
```shell
cd bin/aarch64
./scm_native ../../examples/fib.scm
```

> **Pozn:** Jednotlivé složky obsahují soubor scm-component.jar, který lze využít k instalaci jazyka jako komponenty
pomocí Graal Updater (viz krok 6 v BUILD sekci).



## Sestavení (build)
1. Nejprve je potřeba stáhnout [JDK GraalVM verze 22.3.1](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.3.1) pro Javu 17.
2. Poté je potřeba nastavit proměnnou prostředí (environment variable) `JAVA_HOME` na JDK GraalVM verze 22.3.1. <br>

MacOS:
```shell
export JAVA_HOME=path/to/jdk/graalvm-ce-java17-22.3.1/Contents/Home/
```

Linux:
```shell
export JAVA_HOME=path/to/jdk/graalvm-ce-java17-22.3.1/
```
3. Poté je potřeba upravit proměnnou prostředí PATH

```shell
export PATH=$JAVA_HOME/bin:$PATH
```
4. Dále je potřeba, aby počítač měl nainstalovaný [Maven](https://maven.apache.org/download.cgi).
5. Jelikož součástí sestavení je tvorba native-image, je potřeba nainstalovat komponentu s vuyžitím Graal Updater. Ten je součástí distribuce GraalVM.
```shell
gu install native-image
```
6. Následně je možné projekt sestavit (build) pomocí příkazu:
```shell
cd src/TruffleScheme
mvn clean package -DskipTests
```
> **Pozn:** Testy byly vynechány z důvodu, že jejich součástí jsou i testy pro interoperabilitu. K tomu, aby testy mohly být vykonány, je potřeba instalovat python a javascript komponenty: `gu install js python`.

Tento příkaz vytvoří:
   - ve složce `native` spustitelný soubor `scm_native` (AOT compiled).
   - ve složce `component` soubor `scm-component.jar`, což je komponenta, kterou lze využít k instalaci TruffleScheme pomocí Graal Updater (viz níže).

7. V tuto chvíli je možné interpret spustit pomocí výše vygenerovaného programu `scm_native`
```shell
./native/scm_native language/tests/fib.scm
```
8. Možné je také využít vytvořenou komponentu a jazyk nainstalovat pomocí Graal Updater.
```shell
gu install -L component/scm-component.jar
```
> **Pozn:**  `-L` značí, že se jedná o instalaci z lokálního souboru. Komponentu lze odstranit pomocí příkazu ``gu remove scm``

Úspěšná instalace lze ověřit dvěma způsoby:
   - pomocí příkazu ``gu list``. Katalog by měl obsahovat komponentu s ID `scm`
   - Ve složce `$JAVA_HOME/bin` by měly dva nové soubory: `scm` a `scm_native`.
9. V případě, že je komponenta úspěšně nainstalována, interpret je možné spustit dvěma způsoby:
   - nad JVM příkazem: `scm ../../examples/fib.scm`
   - pomocí native-image (AOT) příkazem: `scm_native ../../examples/fib.scm`


##  Vytvoření native-image
Binární soubor `scm_native` vytvořený pomocí výše zmíněného `mvn clean package` obsahuje pouze implementaci jazyka Scheme.
K vytvoření binárního soubor obsahující i jiné podporované jazyky je potřeba využít skript uložený ve složce ``src/TruffleScheme/native/make_native_polyglot.sh``.
Tento skript je možné zavolat s těmito argumenty:
   - `--python` pro instalaci jazyka Python
   - `--js` pro instalaci jazyka JavaScript
   - `--ruby` pro instalaci jazyka Ruby

Tento script vytvoří native-image obsahující implementaci jazyka JavaScript a Python
```shell
cd src/TruffleScheme/native
./make_native_polyglot.sh --python --js
```
> **Pozn:** Důležité je podotknout, že před vyhodnocením tohoto příkazu je potřeba mít nainstalované komponenty js a python. Ty je možné nainstalovat pomocí Graal Updater.

Komponenty lze nainstalovat například pomocí následujících příkazů:
```shell
gu install python
gu install js
gu install ruby
```




    