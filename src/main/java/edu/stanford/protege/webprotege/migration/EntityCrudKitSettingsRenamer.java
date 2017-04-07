package edu.stanford.protege.webprotege.migration;

import com.mongodb.MongoNamespace;
import com.mongodb.MongoServerException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
public class EntityCrudKitSettingsRenamer {

    private static final String OLD_NAME = "projectEntityCrudKitSettings";

    private static final String NEW_NAME = "EntityCrudKitSettings";

    @Nonnull
    private final MongoDatabase database;

    public EntityCrudKitSettingsRenamer(@Nonnull MongoDatabase database) {
        this.database = checkNotNull(database);
    }

    public void performRename() {
        try {
            MongoCollection<Document> entityCrudKitSettingsCollection = database.getCollection(
                    OLD_NAME);
            entityCrudKitSettingsCollection.renameCollection(new MongoNamespace(database.getName(),
                                                                                NEW_NAME));
        } catch (MongoServerException e) {
            System.out.printf("An error occurred whilst renaming the %s collection to %s.  Cause: %s.\n",
                               OLD_NAME,
                               NEW_NAME,
                               e.getMessage());
        }
    }
}
