package edu.stanford.protege.webprotege.migration;

import com.mongodb.MongoNamespace;
import com.mongodb.MongoServerException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
public class CollectionInit {

    private static final String OLD_NAME = "projectEntityCrudKitSettings";

    private static final String NEW_NAME = "EntityCrudKitSettings";

    @Nonnull
    private final MongoDatabase database;

    public CollectionInit(@Nonnull MongoDatabase database) {
        this.database = checkNotNull(database);
    }

    public void initCollections() {
        System.out.println("Initialising mongodb collections" );
        try {
            Optional<String> crudKitCollection = StreamSupport.stream(database.listCollectionNames().spliterator(), false)
                                                       .filter(name -> name.equals(OLD_NAME))
                                                       .findFirst();
            if (crudKitCollection.isPresent()) {

                MongoCollection<Document> entityCrudKitSettingsCollection = database.getCollection(
                        OLD_NAME);
                entityCrudKitSettingsCollection.renameCollection(new MongoNamespace(database.getName(),
                                                                                    NEW_NAME));
            }
            database.getCollection(DbCollections.ROLE_ASSIGNMENTS).createIndex(
                    new Document("projectId", 1).append("userName", 1).append("unique", true)
            );
        } catch (MongoServerException e) {
            System.out.printf("An error occurred whilst renaming the %s collection to %s.  Cause: %s.\n" ,
                              OLD_NAME,
                              NEW_NAME,
                              e.getMessage());
        }
        System.out.println("\tdone");
    }
}
