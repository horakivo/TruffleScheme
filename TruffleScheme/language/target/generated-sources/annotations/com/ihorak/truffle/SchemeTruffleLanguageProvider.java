// CheckStyle: start generated
package com.ihorak.truffle;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleFile.FileTypeDetector;
import com.oracle.truffle.api.TruffleLanguage.Provider;
import com.oracle.truffle.api.TruffleLanguage.Registration;
import com.oracle.truffle.api.dsl.GeneratedBy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@GeneratedBy(SchemeTruffleLanguage.class)
@Registration(id = "scm", name = "Scheme")
public final class SchemeTruffleLanguageProvider implements Provider {

    @Override
    public String getLanguageClassName() {
        return "com.ihorak.truffle.SchemeTruffleLanguage";
    }

    @Override
    public TruffleLanguage<?> create() {
        return new SchemeTruffleLanguage();
    }

    @Override
    public List<FileTypeDetector> createFileTypeDetectors() {
        return Collections.emptyList();
    }

    @Override
    public Collection<String> getServicesClassNames() {
        return Collections.emptySet();
    }

}
