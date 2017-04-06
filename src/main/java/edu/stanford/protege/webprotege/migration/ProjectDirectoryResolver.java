package edu.stanford.protege.webprotege.migration;

import javax.annotation.Nonnull;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
public class ProjectDirectoryResolver {

    private static final String DATA_STORE = "data-store";

    private static final String PROJECT_DATA = "project-data";

    @Nonnull
    private final Path dataDirectory;

    public ProjectDirectoryResolver(@Nonnull Path dataDirectory) {
        this.dataDirectory = checkNotNull(dataDirectory);
    }

    public Path resolve(String projectId) {
        return dataDirectory.resolve(DATA_STORE).resolve(PROJECT_DATA).resolve(projectId);
    }
}
