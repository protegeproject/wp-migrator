package edu.stanford.protege.webprotege.migration;

import javax.annotation.Nonnull;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
public class MetaProjectResolver {

    @Nonnull
    private final Path dataDirectory;

    public MetaProjectResolver(@Nonnull Path dataDirectory) {
        this.dataDirectory = checkNotNull(dataDirectory);
    }

    public Path resolve() {
        return dataDirectory.resolve("metaproject").resolve("metaproject.pprj");
    }
}
