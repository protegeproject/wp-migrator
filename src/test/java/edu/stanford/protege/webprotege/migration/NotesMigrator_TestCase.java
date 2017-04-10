package edu.stanford.protege.webprotege.migration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.notNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Apr 2017
 */
public class NotesMigrator_TestCase {

    private static final String TEST_DB_NAME = "notes-migration-test";

    private NotesMigrator migrator;

    private MongoClient mongoClient;

    private MongoDatabase database;

    @Before
    public void setUp() throws Exception {
        mongoClient = new MongoClient();
        database = mongoClient.getDatabase(TEST_DB_NAME);
        Path dataDirectory = Paths.get(MetaProjectMigrator_TestCase.class.getResource("/data-directory" ).toURI());
        MetaProjectResolver metaProjectResolver = new MetaProjectResolver(dataDirectory);
        MetaProject metaProject = new MetaProjectImpl(metaProjectResolver.resolve().toUri());
        ProjectDirectoryResolver projectDirectoryResolver = new ProjectDirectoryResolver(dataDirectory);

        migrator = new NotesMigrator(
                metaProject,
                new ChangeLogFileResolver(projectDirectoryResolver),
                new NotesOntologyDocumentResolver(projectDirectoryResolver),
                database
        );
        migrator.performMigration();
    }

    @Test
    public void shouldCreateDiscussionThreadsOnClass() {
        MongoCollection<Document> collection = database.getCollection("EntityDiscussionThreads" );
        Document thread = new Document();
        thread.append("projectId" , "e27a98c2-ab30-4d53-9131-2680962c62c8" )
              .append("entity" , new Document("type" , "Class" )
                      .append("iri" , "http://webprotege.stanford.edu/RBXsNNCWPFlFEKsSp4Jjp0t" ))
              .append("status" , "OPEN" )
              .append("comments.0.createdBy", "M Horridge");
        Document found = collection.find(thread).limit(1).first();
        assertThat(found != null, is(true));
    }

    @Test
    public void shouldCreateDiscussionThreadsOnObjectProperty() {
        MongoCollection<Document> collection = database.getCollection("EntityDiscussionThreads" );
        Document thread = new Document();
        thread.append("projectId" , "e27a98c2-ab30-4d53-9131-2680962c62c8" )
              .append("entity" , new Document("type" , "ObjectProperty" )
                      .append("iri" , "http://webprotege.stanford.edu/R70kJH8LTmeoorh3fkoWNuR" ))
              .append("status" , "OPEN" )
              .append("comments.0.createdBy", "M Horridge");
        Document found = collection.find(thread).limit(1).first();
        assertThat(found != null, is(true));
    }

    @After
    public void tearDown() {
        database.drop();
        mongoClient.close();
    }
}
