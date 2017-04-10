package edu.stanford.protege.webprotege.migration;

import javax.annotation.Nonnull;

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
        entityCrudKitSettingsRenamer.performRename();

        // Migrate metaproject
        metaProjectMigrator.performMigration();

        // Migrate notes
        notesMigrator.performMigration();

        System.out.println("Migration complete.");
        System.out.println("You may remove the metaproject directory from the data directory as it is no longer used by WebProtege.");
        System.out.println("You may also remove the notes-data directories that are contained within project data directories as these are non longer used by WebProtege.");
    }
}
