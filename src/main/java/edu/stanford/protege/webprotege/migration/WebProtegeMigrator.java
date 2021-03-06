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
    private final CollectionInit collectionInit;

    @Nonnull
    private final MetaProjectMigrator metaProjectMigrator;

    private final NotesMigrator notesMigrator;

    public WebProtegeMigrator(@Nonnull CollectionInit collectionInit,
                              @Nonnull MetaProjectMigrator metaProjectMigrator,
                              @Nonnull NotesMigrator notesMigrator) {
        this.collectionInit = checkNotNull(collectionInit);
        this.metaProjectMigrator = checkNotNull(metaProjectMigrator);
        this.notesMigrator = checkNotNull(notesMigrator);
    }

    public void performMigration() {
        // Rename collections
        collectionInit.initCollections();

        // Migrate metaproject
        metaProjectMigrator.performMigration();

        // Migrate notes
        notesMigrator.performMigration();

        System.out.printf("------------------\n" );
        System.out.printf("Migration complete\n");
        System.out.printf("------------------\n\n" );
        System.out.printf("\t- You may remove the metaproject directory from the data directory as it is no longer used by WebProtege.\n\n");
        System.out.printf("\t- You may remove the notes-data directories that are contained within project data directories as these are no longer used by WebProtege.\n\n");
    }
}
