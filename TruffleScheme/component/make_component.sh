#!/usr/bin/env bash

declare -r JAVA_VERSION="${1:?First argument must be java version.}"
declare -r GRAALVM_VERSION="${2:?Second argument must be GraalVM version.}"
readonly COMPONENT_DIR="component_temp_dir"
readonly LANGUAGE_PATH="$COMPONENT_DIR/languages/scm"


# I am not currently supporting native build
#
#if [[ -f ../native/slnative ]]; then
#    INCLUDE_SLNATIVE="TRUE"
#fi

rm -rf COMPONENT_DIR

mkdir -p "$LANGUAGE_PATH"
cp ../language/target/truffleScheme.jar "$LANGUAGE_PATH"

mkdir -p "$LANGUAGE_PATH/launcher"
cp ../launcher/target/scm-launcher.jar "$LANGUAGE_PATH/launcher/"

mkdir -p "$LANGUAGE_PATH/bin"
#cp ../sl $LANGUAGE_PATH/bin/

#if [[ $INCLUDE_SLNATIVE = "TRUE" ]]; then
#    cp ../native/slnative $LANGUAGE_PATH/bin/
#fi

touch "$LANGUAGE_PATH/native-image.properties"

mkdir -p "$COMPONENT_DIR/META-INF"
{
    echo "Bundle-Name: Truffle Scheme";
    echo "Bundle-Symbolic-Name: com.ihorak.truffle.scm";
    echo "Bundle-Version: $GRAALVM_VERSION";
    echo "Bundle-RequireCapability: org.graalvm; filter:=\"(&(graalvm_version=$GRAALVM_VERSION)(os_arch=aarch64))\"";
} > "$COMPONENT_DIR/META-INF/MANIFEST.MF"

(
cd $COMPONENT_DIR || exit 1
$JAVA_HOME/bin/jar cfm ../scm-component.jar META-INF/MANIFEST.MF .

#echo "bin/sl = ../languages/sl/bin/sl" > META-INF/symlinks

#if [[ $INCLUDE_SLNATIVE = "TRUE" ]]; then
#    echo "bin/slnative = ../languages/sl/bin/slnative" >> META-INF/symlinks
#fi

#$JAVA_HOME/bin/jar uf ../scm-component.jar META-INF/symlinks

#{
#    echo 'languages/sl/bin/sl = rwxrwxr-x'
#    echo 'languages/sl/bin/slnative = rwxrwxr-x'
#} > META-INF/permissions
#$JAVA_HOME/bin/jar uf ../scm-component.jar META-INF/permissions
)
rm -rf $COMPONENT_DIR
