#!/usr/bin/env bash

declare -r JAVA_VERSION="${1:?First argument must be java version.}"
declare -r GRAALVM_VERSION="${2:?Second argument must be GraalVM version.}"
readonly COMPONENT_DIR="component_temp_dir"
readonly LANGUAGE_PATH="$COMPONENT_DIR/languages/scm"

if [[ -f ../native/scm_native ]]; then
  INCLUDE_SCM_NATIVE="TRUE"
fi

rm -rf COMPONENT_DIR

mkdir -p "$LANGUAGE_PATH"
cp ../language/target/truffleScheme.jar "$LANGUAGE_PATH"

mkdir -p "$LANGUAGE_PATH/launcher"
cp ../launcher/target/scm-launcher.jar "$LANGUAGE_PATH/launcher/"

mkdir -p "$LANGUAGE_PATH/bin"
cp ../scm $LANGUAGE_PATH/bin/

if [[ $INCLUDE_SCM_NATIVE = "TRUE" ]]; then
  cp ../native/scm_native $LANGUAGE_PATH/bin/
fi

touch "$LANGUAGE_PATH/native-image.properties"

mkdir -p "$COMPONENT_DIR/META-INF"
{
  echo "Bundle-Name: Truffle Scheme"
  echo "Bundle-Symbolic-Name: scm"
  echo "Bundle-Version: $GRAALVM_VERSION"
  echo "Bundle-RequireCapability: org.graalvm; filter:=\"(&(graalvm_version=$GRAALVM_VERSION))\""
} >"$COMPONENT_DIR/META-INF/MANIFEST.MF"

(
  cd $COMPONENT_DIR || exit 1
  $JAVA_HOME/bin/jar cfm ../scm-component.jar META-INF/MANIFEST.MF .

  echo "bin/scm = ../languages/scm/bin/scm" >META-INF/symlinks

  if [[ $INCLUDE_SCM_NATIVE = "TRUE" ]]; then
    echo "bin/scm_native = ../languages/scm/bin/scm_native" >>META-INF/symlinks
  fi

  $JAVA_HOME/bin/jar uf ../scm-component.jar META-INF/symlinks

  {
    echo 'languages/scm/bin/scm = rwxrwxr-x'
    echo 'languages/scm/bin/scm_native = rwxrwxr-x'
  } >META-INF/permissions
  $JAVA_HOME/bin/jar uf ../scm-component.jar META-INF/permissions
)

rm -rf $COMPONENT_DIR
