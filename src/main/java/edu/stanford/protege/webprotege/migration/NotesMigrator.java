package edu.stanford.protege.webprotege.migration;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import org.bson.Document;
import org.protege.notesapi.NotesException;
import org.protege.notesapi.NotesManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import javax.annotation.Nonnull;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.migration.NotesOntologyConverter.CHANGES_ONTOLOGY_IRI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class NotesMigrator {

    private static final String ENTITY_DISCUSSION_THREADS_COLLECTION = "EntityDiscussionThreads";

    public static final String CHANGES_ONTOLOGY_FILE_NAME = "changes.owl";

    @Nonnull
    private final MetaProject metaProject;

    @Nonnull
    private final ChangeLogFileResolver changeLogFileResolver;

    @Nonnull
    private final NotesOntologyDocumentResolver notesOntologyDocumentResolver;

    @Nonnull
    private final MongoDatabase database;

    @Nonnull
    private final OWLOntologyManager notesManager;

    public NotesMigrator(@Nonnull MetaProject metaProject,
                         @Nonnull ChangeLogFileResolver changeLogFileResolver,
                         @Nonnull NotesOntologyDocumentResolver notesOntologyDocumentResolver,
                         @Nonnull MongoDatabase database) {
        this.metaProject = checkNotNull(metaProject);
        this.changeLogFileResolver = checkNotNull(changeLogFileResolver);
        this.notesOntologyDocumentResolver = checkNotNull(notesOntologyDocumentResolver);
        this.database = checkNotNull(database);
        this.notesManager = OWLManager.createOWLOntologyManager();
    }


    private static IRI getChangeOntologyDocumentIRI() {
        URL changeOntologyURL = NotesOntologyConverter.class.getResource("/" + CHANGES_ONTOLOGY_FILE_NAME);
        if (changeOntologyURL == null) {
            throw new RuntimeException(
                    "Changes ontology not found.  Please make sure the changes ontology document is placed in the class path with a file name of " + CHANGES_ONTOLOGY_FILE_NAME);
        }
        String uriString = changeOntologyURL.toString();
        return IRI.create(uriString);
    }

    public void performMigration() {
        try {
            notesManager.addIRIMapper(new SimpleIRIMapper(CHANGES_ONTOLOGY_IRI, getChangeOntologyDocumentIRI()));
            OWLOntology chao = notesManager.loadOntology(CHANGES_ONTOLOGY_IRI);
            // Believe it or not, the following line appears inconsequential, like a bug almost,
            // but it's absolutely necessary for the proper functioning of this notes API thing.
            NotesManager.createNotesManager(chao);

            Set<ProjectInstance> projects = metaProject.getProjects();
            int counter = 0;
            for(ProjectInstance projectInstance : projects) {
                counter++;
                String projectId = projectInstance.getName();
                System.out.printf("[Project %s] (%s of %s) Migrating notes.\n", projectId, counter, projects.size());
                Path notesDocumentFile = notesOntologyDocumentResolver.resolve(projectId);
                Path changeLogFile = changeLogFileResolver.resolve(projectId);
                if(Files.exists(notesDocumentFile)) {
                    NotesOntologyConverter converter = new NotesOntologyConverter(projectId,
                                                                                  notesDocumentFile,
                                                                                  new HasSignatureImpl(changeLogFile),
                                                                                  notesManager);
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
        } catch (OWLOntologyCreationException | NotesException e) {
            System.out.printf("An error occurred loading the notes ontology.  Cannot migrate notes. Cause: %s", e.getMessage());
        }
    }
}
