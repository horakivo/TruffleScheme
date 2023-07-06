## Překladač funkcionálního programovacího jazyka na platformě GraalVM

Autor práce: Bc. Ivo Horák <br>
Vedoucí práce: Mgr. Petr Krajča, Ph.D.

Univerzita Palackého v Olomouci <br>
Přírodovědecká fakulta <br>
Katedra informatiky <br>


### Spuštění (bez sestavení)
1. Nejprve je potřeba stáhlnout GraalVM verze 22.3.1 pro Javu 17. JDK je možné stáhnout na
``
https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.3.1
``
2. Poté je potřeba nastavit proměnnou prostředí (environment variable) JAVA_HOME na JDK GraalVM verze 22.3.1. <br>



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

4. Následně je možné spustit interpret dvěma způsoby: <br>

#### Nad JVM:
```shell
cd src/TruffleScheme/
./scm src/TruffleScheme/language/tests/fib.scm
```

#### Pomocí předen vytvořených spustitelných souborů
> **Pozn:** Binární soubory jsou sestaveny na architekturu MacOS aarch64 (Apple silicon) a Linux amd64

Linux
```shell
cd bin/amd64
./scm-native <soubor.scm>
```

MacOS
```shell
cd bin/aarch64
./scm-native <soubor.scm>
```

> **Pozn:** Jednotlivé složky obsahují soubor scm-component.jar, který lze využít k instalaci jazyka jako komponenty
pomocí Graal Updater. (viz krok 7 v BUILD sekci)




    