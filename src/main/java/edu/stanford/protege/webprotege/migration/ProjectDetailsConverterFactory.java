package edu.stanford.protege.webprotege.migration;

import edu.stanford.smi.protege.server.metaproject.ProjectInstance;

import javax.annotation.Nonnull;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
public class ProjectDetailsConverterFactory {

    @Nonnull
    private final ProjectDirectoryResolver projectDirectoryResolver;

    @Nonnull
    private final ChangeLogFileResolver changeLogFileResolver;

    public ProjectDetailsConverterFactory(@Nonnull ProjectDirectoryResolver projectDirectoryResolver,
                                          @Nonnull ChangeLogFileResolver changeLogFileResolver) {
        this.projectDirectoryResolver = checkNotNull(projectDirectoryResolver);
        this.changeLogFileResolver = checkNotNull(changeLogFileResolver);
    }

    public ProjectDetailsConverter get(ProjectInstance projectInstance) {
        return new ProjectDetailsConverter(projectInstance, projectDirectoryResolver, changeLogFileResolver);
    }
}
