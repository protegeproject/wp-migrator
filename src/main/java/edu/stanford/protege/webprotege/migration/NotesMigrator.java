package edu.stanford.protege.webprotege.migration;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class NotesMigrator {

    private static final String ENTITY_DISCUSSION_THREADS_COLLECTION = "EntityDiscussionThreads";

    @Nonnull
    private final MetaProject metaProject;

    @Nonnull
    private final ChangeLogFileResolver changeLogFileResolver;

    @Nonnull
    private final NotesOntologyDocumentResolver notesOntologyDocumentResolver;

    @Nonnull
    private final MongoDatabase database;

    public NotesMigrator(@Nonnull MetaProject metaProject,
                         @Nonnull ChangeLogFileResolver changeLogFileResolver,
                         @Nonnull NotesOntologyDocumentResolver notesOntologyDocumentResolver,
                         @Nonnull MongoDatabase database) {
        this.metaProject = checkNotNull(metaProject);
        this.changeLogFileResolver = checkNotNull(changeLogFileResolver);
        this.notesOntologyDocumentResolver = checkNotNull(notesOntologyDocumentResolver);
        this.database = checkNotNull(database);
    }

    public void performMigration() {
        for(ProjectInstance projectInstance : metaProject.getProjects()) {
            String projectId = projectInstance.getName();
            Path notesDocumentFile = notesOntologyDocumentResolver.resolve(projectId);
            Path changeLogFile = changeLogFileResolver.resolve(projectId);
            if(Files.exists(notesDocumentFile)) {
                NotesDocumentConverter converter = new NotesDocumentConverter(projectId,
                                                                              notesDocumentFile,
                                                                              new HasSignatureImpl(changeLogFile));
                List<Document> threadDocuments = converter.convert();
                if (!threadDocuments.isEmpty()) {
                    MongoCollection<Document> entityDiscussionThreadsCollection = database.getCollection(
                            ENTITY_DISCUSSION_THREADS_COLLECTION);
                    try {
                        entityDiscussionThreadsCollection.insertMany(threadDocuments);
                    } catch (Exception e) {
                        System.out.printf("There was a problem inserting the discussion threads for %s.  Threads not inserted. Cause: %s\n",
                                          projectId,
                                          e.getMessage());
                    }
                }

            }
        }
    }
}
