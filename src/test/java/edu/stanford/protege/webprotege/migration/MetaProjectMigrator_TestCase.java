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

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Apr 2017
 */
public class MetaProjectMigrator_TestCase {

    private static final String TEST_DB_NAME = "meta-project-migration-test";

    private MetaProjectMigrator migrator;

    private MongoClient mongoClient;

    private MongoDatabase database;

    @Before
    public void setUp() throws Exception {
        mongoClient = new MongoClient();
        database = mongoClient.getDatabase(TEST_DB_NAME);
        Path dataDirectory = Paths.get(MetaProjectMigrator_TestCase.class.getResource("/data-directory").toURI());
        MetaProjectResolver metaProjectResolver = new MetaProjectResolver(dataDirectory);
        MetaProject metaProject = new MetaProjectImpl(metaProjectResolver.resolve().toUri());
        ProjectDirectoryResolver projectDirectoryResolver = new ProjectDirectoryResolver(
                dataDirectory
                );
        ChangeLogFileResolver changeLogFileResolver = new ChangeLogFileResolver(
                projectDirectoryResolver
        );
        migrator = new MetaProjectMigrator(
                metaProject,
                new ProjectDetailsConverterFactory(
                        projectDirectoryResolver,
                        changeLogFileResolver
                ),
                database
        );
        migrator.performMigration();
    }

    @Test
    public void shouldMigrateProjectDetails() {
        MongoCollection<Document> projectDetails = database.getCollection("ProjectDetails");
        Document projectDetailsDocument = projectDetails.find().first();
        assertThat(projectDetailsDocument.get("_id"), is("e27a98c2-ab30-4d53-9131-2680962c62c8"));
        assertThat(projectDetailsDocument.getString("owner"), is("M Horridge"));
        assertThat(projectDetailsDocument.getString("displayName"), is("Migration Test Project"));
        assertThat(projectDetailsDocument.getString("description"), is("This is a project for the WebProtege 2.6 to 3.0 migration."));

        assertThat(projectDetailsDocument.getLong("createdAt"), is(greaterThan(0L)));
        assertThat(projectDetailsDocument.getString("createdBy"), is("M Horridge"));

        assertThat(projectDetailsDocument.getLong("modifiedAt"), is(greaterThan(0L)));
        assertThat(projectDetailsDocument.getString("modifiedBy"), is("M Horridge"));
    }

    @Test
    public void shouldMigrateUserDetails() {
        MongoCollection<Document> users = database.getCollection("Users");
        Document userDocument = users.find().first();
        assertThat(userDocument.get("_id"), is("M Horridge"));
        assertThat(userDocument.getString("realName"), is("M Horridge"));
        assertThat(userDocument.getString("emailAddress"), is("user.name@server.com"));
        assertThat(userDocument.getString("salt"), is(notNullValue()));
        assertThat(userDocument.getString("saltedPasswordDigest"), is(notNullValue()));
    }

    @Test
    public void shouldMigratePermissions() {
        MongoCollection<Document> users = database.getCollection(DbCollections.ROLE_ASSIGNMENTS);
        Document document = users.find().first();
        assertThat(document.getString("projectId"), is("e27a98c2-ab30-4d53-9131-2680962c62c8"));
        assertThat(document.getString("userName"), is("M Horridge"));
        assertThat(document.get("assignedRoles"), is(singletonList("CanManage")));
    }

    @After
    public void tearDown() {
        database.drop();
        mongoClient.close();
    }
}
