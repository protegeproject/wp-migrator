package edu.stanford.protege.webprotege.migration;

import com.google.common.base.Stopwatch;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.User;
import org.bson.Document;

import javax.annotation.Nonnull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class MetaProjectMigrator {

    private final MetaProject metaProject;

    private final ProjectDetailsConverterFactory projectDetailsConverterFactory;

    private final MongoDatabase database;

    private final Set<String> migratedProjectDetails = new HashSet<>();

    public MetaProjectMigrator(@Nonnull MetaProject metaProject,
                               @Nonnull ProjectDetailsConverterFactory projectDetailsConverterFactory,
                               @Nonnull MongoDatabase database) {
        this.metaProject = checkNotNull(metaProject);
        this.projectDetailsConverterFactory = checkNotNull(projectDetailsConverterFactory);
        this.database = checkNotNull(database);
    }

    public void performMigration() {
        migrateUsers();
        migrateProjectDetails();
        migratePermissions();
    }

    private void migratePermissions() {
        System.out.printf("-----------------------------\n" );
        System.out.printf("Migrating project permissions\n");
        System.out.printf("-----------------------------\n\n");
        // Migrate the project permissions
        MongoCollection<Document> roleAssignmentCollections = database.getCollection(DbCollections.ROLE_ASSIGNMENTS);
        Set<ProjectInstance> projects = metaProject.getProjects();
        int counter = 0;
        for(ProjectInstance projectInstance : projects) {
            counter++;
            if (migratedProjectDetails.contains(projectInstance.getName())) {
                System.out.printf("[Project %s] Migrating permissions (%d of %d)\n", projectInstance.getName(), counter, projects.size());
                PermissionsConverter converter = new PermissionsConverter(projectInstance);
                List<Document> convert = converter.convert();
                for(Document document : convert) {
                    try {
                        roleAssignmentCollections.insertOne(document);
                    } catch (Exception e) {
                        System.out.printf("\tAn error occurred whilst inserting role assignments for project %s.  " +
                                                  "Role assignments for this project have been skipped.\n\tCause: %s.\n",
                                          projectInstance.getName(),
                                          e.getMessage());
                    }
                }
                System.out.printf("\tMigrated %d permissions\n\n", convert.size());
            }
        }
        System.out.printf("Finished migrating project permissions\n\n");
    }

    private void migrateUsers() {
        System.out.printf("---------------\n");
        System.out.printf("Migrating users\n");
        System.out.printf("---------------\n\n");
        // Migrate the Users
        MongoCollection<Document> usersCollection = database.getCollection(DbCollections.USERS);
        Set<User> users = metaProject.getUsers();
        int counter = 0;
        for(User user : users) {
            counter++;
            System.out.printf("[User '%s'] (%d of %d)\n", user.getName(), counter, users.size());
            try {
                UserDetailsConverter converter = new UserDetailsConverter(user);
                converter.convert().ifPresent(usersCollection::insertOne);
                System.out.printf("\tMigrated user\n\n");
            } catch (Exception e) {
                System.out.printf("\tAn error occurred whilst inserting the user details for %s.  " +
                                          "This user has been skipped.\n\tCause: %s.\n\n",
                                  user.getName(),
                                  e.getMessage());
            }
        }
        System.out.printf("Finished migrating users\n\n");
    }

    private void migrateProjectDetails() {
        System.out.printf("-------------------------\n");
        System.out.printf("Migrating project details\n");
        System.out.printf("-------------------------\n\n");

        MongoCollection<Document> projectDetailsCollection = database.getCollection(DbCollections.PROJECT_DETAILS);
        Set<ProjectInstance> projects = metaProject.getProjects();
        int projectCount = projects.size();
        int count = 0;
        for(ProjectInstance projectInstance : projects) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            try {
                count++;
                System.out.printf("[Project %s] (%d/%d) Migrating project details\n", projectInstance.getName(), count, projectCount);
                ProjectDetailsConverter converter = projectDetailsConverterFactory.get(projectInstance);
                converter.convert().ifPresent(doc -> {
                    projectDetailsCollection.insertOne(doc);
                    migratedProjectDetails.add(projectInstance.getName());
                });
            } catch (DuplicateKeyException e) {
                System.out.printf("\tProject details already exist for %s.  " +
                                          "This project has been skipped.\n",
                                  projectInstance.getName());
            } catch (MongoException e) {
                System.out.printf("\tAn error occurred whilst inserting the project details for %s.  " +
                                          "This project has been skipped.\n\tCause: %s.\n",
                                  projectInstance.getName(),
                                  e.getMessage());
            } finally {
                System.out.printf("\tFinished in %d ms\n\n", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            }
        }
        System.out.printf("Finished migrating project details\n\n");
    }
}
