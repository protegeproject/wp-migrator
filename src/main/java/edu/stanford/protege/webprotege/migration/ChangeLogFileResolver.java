package edu.stanford.protege.webprotege.migration;

import javax.annotation.Nonnull;

import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
public class ChangeLogFileResolver {

    private static final String CHANGE_DATA = "change-data";

    private static final String CHANGE_DATA_BINARY = "change-data.binary";

    @Nonnull
    private final ProjectDirectoryResolver projectDirectoryResolver;

    public ChangeLogFileResolver(@Nonnull ProjectDirectoryResolver projectDirectoryResolver) {
        this.projectDirectoryResolver = checkNotNull(projectDirectoryResolver);
    }

    public Path resolve(String projectId) {
        return projectDirectoryResolver.resolve(projectId).resolve(CHANGE_DATA).resolve(CHANGE_DATA_BINARY);
    }
}
