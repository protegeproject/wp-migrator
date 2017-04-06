package edu.stanford.protege.webprotege.migration;

import javax.annotation.Nonnull;

import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
public class NotesOntologyDocumentResolver {

    private static final String NOTES_DATA = "notes-data";

    private static final String NOTES_DATA_BINARY = "notes-data.binary";

    @Nonnull
    private final ProjectDirectoryResolver projectDirectoryResolver;

    public NotesOntologyDocumentResolver(@Nonnull ProjectDirectoryResolver projectDirectoryResolver) {
        this.projectDirectoryResolver = checkNotNull(projectDirectoryResolver);
    }

    @Nonnull
    public Path resolve(String projectId) {
        return projectDirectoryResolver.resolve(projectId).resolve(NOTES_DATA).resolve(NOTES_DATA_BINARY);
    }
}
