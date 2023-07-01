#!/usr/bin/env bash
#
# Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# The Universal Permissive License (UPL), Version 1.0
#
# Subject to the condition set forth below, permission is hereby granted to any
# person obtaining a copy of this software, associated documentation and/or
# data (collectively the "Software"), free of charge and under any and all
# copyright rights in the Software, and any and all patent rights owned or
# freely licensable by each licensor hereunder covering either (i) the
# unmodified Software as contributed to or provided by such licensor, or (ii)
# the Larger Works (as defined below), to deal in both
#
# (a) the Software, and
#
# (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
# one is included with the Software each a "Larger Work" to which the Software
# is contributed by such licensors),
#
# without restriction, including without limitation the rights to copy, create
# derivative works of, display, perform, and distribute the Software and make,
# use, sell, offer for sale, import, export, have made, and have sold the
# Software and the Larger Work(s), and to sublicense the foregoing rights on
# either these or other terms.
#
# This license is subject to the following condition:
#
# The above copyright notice and either this complete permission notice or at a
# minimum a reference to the UPL must be included in all copies or substantial
# portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#

if [[ $SCM_BUILD_NATIVE == "false" ]]; then
    echo "Skipping the native image build because SCM_BUILD_NATIVE is set to false."
    exit 0
fi

LANGUAGE_ARGS=()

for opt in "$@"; do
  case $opt in
  --python)
    RESULT=$(gu list python 2>&1)
    if [ "$RESULT" == "No components found." ]; then
      echo "You need to install python component. Please use 'gu install python' first."
      exit 1
    else
      LANGUAGE_ARGS+=("--language:python")
    fi
    ;;
  --ruby)
    RESULT=$(gu list ruby 2>&1)
    if [ "$RESULT" == "No components found." ]; then
      echo "You need to install ruby component. Please use 'gu install ruby' first."
      exit 1
    else
      LANGUAGE_ARGS+=("--language:ruby")
    fi
    ;;
  --js)
    RESULT=$(gu list js 2>&1)
    if [ "$RESULT" == "No components found." ]; then
      echo "You need to install javascript component. Please use 'gu install js' first."
      exit 1
    else
      LANGUAGE_ARGS+=("--language:js")
    fi
    ;;
  *)
    if [ $opt == "llvm" ]; then
      echo "LLVM is not supported in TruffleScheme, since it only supports binary based sources."
      exit 1
    fi
    echo "Unknown argument: ${opt}. TruffleScheme only supports these languages: Javascript, Python, Ruby."
    exit 1
    ;;
  esac
done

"$JAVA_HOME"/bin/native-image \
    "${LANGUAGE_ARGS[@]}" --macro:truffle --no-fallback --initialize-at-build-time \
    -cp ../language/target/truffleScheme.jar:../launcher/target/scm-launcher.jar \
    com.ihorak.truffle.launcher.Main \
    scm_native
