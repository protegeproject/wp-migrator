package edu.stanford.protege.webprotege.migration;

import com.mongodb.client.MongoCollection;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.User;
import org.bson.Document;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class ProjectDetailsConverter {

    private static final String CREATED_AT = "createdAt";

    private static final String CREATED_BY = "createdBy";

    private static final String MODIFIED_AT = "modifiedAt";

    private static final String MODIFIED_BY = "modifiedBy";


    private final ProjectInstance projectInstance;

    private final ProjectDirectoryResolver projectDirectoryResolver;

    private final ChangeLogFileResolver changeLogFileResolver;

    private final MongoCollection<Document> migrationMetadataCollection;


    public ProjectDetailsConverter(ProjectInstance projectInstance,
                                   ProjectDirectoryResolver projectDirectoryResolver,
                                   ChangeLogFileResolver changeLogFileResolver,
                                   MongoCollection<Document> migrationMetadataCollection) {
        this.projectInstance = checkNotNull(projectInstance);
        this.projectDirectoryResolver = checkNotNull(projectDirectoryResolver);
        this.changeLogFileResolver = checkNotNull(changeLogFileResolver);
        this.migrationMetadataCollection = checkNotNull(migrationMetadataCollection);
    }

    public Optional<Document> convert() {
        String projectId = projectInstance.getName();
        Path projectDirectory = projectDirectoryResolver.resolve(projectId);

        if(!Files.exists(projectDirectory)) {
            System.out.printf("\tProject directory %s does not exist.  Skipping.\n",
                              projectDirectory.toAbsolutePath());
            return Optional.empty();
        }

        Optional<String> owner = Optional.ofNullable(projectInstance.getOwner()).map(User::getName);
        if(!owner.isPresent()) {
            System.out.printf("\tThe owner of this project is not specified.  Skipping.\n");
            return Optional.empty();
        }

        // Convert the basic details
        BasicProjectDetailsConverter converter = new BasicProjectDetailsConverter(projectInstance);
        Optional<Document> projectDetailsDocument = converter.convertToDocument();

        // Augment with timestamps, which we extract from the change log
        Path changeDataFile = changeLogFileResolver.resolve(projectId);
        if(!Files.exists(changeDataFile)) {
            System.out.printf("\tChange data file %s does not exist.  Skipping\n",
                              changeDataFile.toAbsolutePath());
            return Optional.empty();
        }
        projectDetailsDocument.ifPresent(document -> {
            try {
                long size = Files.size(changeDataFile);
                System.out.printf("\tSize of change log: %,d bytes.\n", size);
                ChangeDataExtractor changeDataExtractor = new ChangeDataExtractor(changeDataFile);
                changeDataExtractor.run();

                changeDataExtractor.getCreatedAtTimestamp().ifPresent(createdAt -> document.append(CREATED_AT, new Date(createdAt)));
                owner.ifPresent(userId -> document.append(CREATED_BY, userId));
                changeDataExtractor.getLastModifiedTimestamp().ifPresent(modifiedAt -> document.append(MODIFIED_AT, new Date(modifiedAt)));
                changeDataExtractor.getLastModifiedBy().ifPresent(user -> {
                    if("system".equals(user)) {
                        document.append(MODIFIED_BY, owner.get());
                    }
                    else {
                        document.append(MODIFIED_BY, user);
                    }
                });
                Document metadata = new Document();
                metadata.putAll(document);
                metadata.append("changeLogSize", changeDataExtractor.getChangeLogSizeInBytes())
                        .append("changeSetCount", changeDataExtractor.getChangeSetCount());
                migrationMetadataCollection.insertOne(metadata);
            } catch (Throwable e) {
                System.out.printf("\tCould not read change data file.  Modification data not migrated. Cause: %s\n", e.getMessage());
            }
        });
        return projectDetailsDocument;
    }


}
