package edu.stanford.protege.webprotege.migration;

import com.mongodb.client.MongoDatabase;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;

import javax.annotation.Nonnull;

import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class WebProtegeMigrator {

    @Nonnull
    private final EntityCrudKitSettingsRenamer entityCrudKitSettingsRenamer;

    @Nonnull
    private final MetaProjectMigrator metaProjectMigrator;

    private final NotesMigrator notesMigrator;

    public WebProtegeMigrator(@Nonnull EntityCrudKitSettingsRenamer entityCrudKitSettingsRenamer,
                              @Nonnull MetaProjectMigrator metaProjectMigrator,
                              @Nonnull NotesMigrator notesMigrator) {
        this.entityCrudKitSettingsRenamer = checkNotNull(entityCrudKitSettingsRenamer);
        this.metaProjectMigrator = checkNotNull(metaProjectMigrator);
        this.notesMigrator = checkNotNull(notesMigrator);
    }

    public void performMigration() {
        // Rename collections
        entityCrudKitSettingsRenamer.rename();

        // Migrate metaproject
        metaProjectMigrator.performMigration();

        // Migrate notes
        notesMigrator.performMigration();
    }
}
