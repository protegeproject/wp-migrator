package edu.stanford.protege.webprotege.migration;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import org.bson.Document;

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

    @Nonnull
    private final MongoCollection<Document> migrationMetadataCollection;

    public ProjectDetailsConverterFactory(@Nonnull ProjectDirectoryResolver projectDirectoryResolver,
                                          @Nonnull ChangeLogFileResolver changeLogFileResolver,
                                          @Nonnull MongoCollection<Document> migrationMetadataCollection) {
        this.projectDirectoryResolver = checkNotNull(projectDirectoryResolver);
        this.changeLogFileResolver = checkNotNull(changeLogFileResolver);
        this.migrationMetadataCollection = checkNotNull(migrationMetadataCollection);
    }

    public ProjectDetailsConverter get(ProjectInstance projectInstance) {
        return new ProjectDetailsConverter(projectInstance, projectDirectoryResolver, changeLogFileResolver, migrationMetadataCollection);
    }
}
