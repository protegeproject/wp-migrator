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



    public MetaProjectMigrator(@Nonnull MetaProject metaProject,
                               @Nonnull ProjectDetailsConverterFactory projectDetailsConverterFactory,
                               @Nonnull MongoDatabase database) {
        this.metaProject = checkNotNull(metaProject);
        this.projectDetailsConverterFactory = checkNotNull(projectDetailsConverterFactory);
        this.database = checkNotNull(database);
    }

    public void performMigration() {
        // Migrate the projects
        migrateProjectDetails();
        migratingPermissions();
        migrateUsers();
    }

    private void migratingPermissions() {
        System.out.println("Migrating project permissions");
        // Migrate the project permissions
        MongoCollection<Document> roleAssignmentCollections = database.getCollection("RoleAssignment");
        Set<ProjectInstance> projects = metaProject.getProjects();
        for(ProjectInstance projectInstance : projects) {
            System.out.printf("[Project %s] Migrating permissions\n", projectInstance.getName());
            PermissionsConverter converter = new PermissionsConverter(projectInstance);
            for(Document document : converter.convert()) {
                try {
                    roleAssignmentCollections.insertOne(document);
                } catch (Exception e) {
                    System.out.printf("An error occurred whilst inserting role assignments for project %s.  " +
                                              "Role assignments for this project have been skipped.  Cause: %s.\n",
                                      projectInstance.getName(),
                                      e.getMessage());
                }
            }
        }
    }

    private void migrateUsers() {
        System.out.println("Migrating users");
        // Migrate the Users
        MongoCollection<Document> usersCollection = database.getCollection("Users");
        for(User user : metaProject.getUsers()) {
            try {
                UserDetailsConverter converter = new UserDetailsConverter(user);
                converter.convert().ifPresent(usersCollection::insertOne);
            } catch (Exception e) {
                System.out.printf("An error occurred whilst inserting the user details for %s.  " +
                                          "This user has been skipped.  Cause: %s.\n",
                                  user.getName(),
                                  e.getMessage());
            }
        }
    }

    private void migrateProjectDetails() {
        System.out.println("Migrating project details");
        MongoCollection<Document> projectDetailsCollection = database.getCollection("ProjectDetails");
        Set<ProjectInstance> projects = metaProject.getProjects();
        int projectCount = projects.size();
        int count = 0;
        for(ProjectInstance projectInstance : projects) {
            try {
                count++;
                Stopwatch stopwatch = Stopwatch.createStarted();
                System.out.printf("[Project %s] (%d/%d) Migrating project details\n", projectInstance.getName(), count, projectCount);
                ProjectDetailsConverter converter = projectDetailsConverterFactory.get(projectInstance);
                converter.convert().ifPresent(projectDetailsCollection::insertOne);
                System.out.printf("    ... finished in %d ms\n", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            } catch (DuplicateKeyException e) {
                System.out.printf("Project details already exist for %s.  " +
                                          "This project has been skipped.\n",
                                  projectInstance.getName());
            } catch (MongoException e) {
                System.out.printf("An error occurred whilst inserting the project details for %s.  " +
                                          "This project has been skipped.  Cause: %s.\n",
                                  projectInstance.getName(),
                                  e.getMessage());
            }
        }
    }
}
