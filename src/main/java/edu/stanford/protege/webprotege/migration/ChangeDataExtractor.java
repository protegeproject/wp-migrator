package edu.stanford.protege.webprotege.migration;

import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.change.OntologyChangeRecordList;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import sun.rmi.runtime.Log;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class ChangeDataExtractor {

    private static final int _100_MEGS = 100 * 1024 * 1024;

    private final Path changeDataFile;

    private long firstTimeStamp = -1L;

    private long lastTimeStamp = -1L;

    private int changeSetCount = 0;

    @Nullable
    private String lastModifiedBy = null;



    public ChangeDataExtractor(@Nonnull Path changeDataFile) {
        this.changeDataFile = checkNotNull(changeDataFile);
    }

    public void run() throws IOException {
        BinaryOWLOntologyChangeLog changeLog = new BinaryOWLOntologyChangeLog();
        try(BufferedInputStream is = new BufferedInputStream(Files.newInputStream(changeDataFile), _100_MEGS)) {
            changeLog.readChanges(is,
                                  new OWLDataFactoryImpl(),
                                  (list, skipSetting, filePosition) -> handleChangeData(list),
                                  SkipSetting.SKIP_DATA);
        }
    }

    public long getChangeLogSizeInBytes() {
        try {
            return Files.size(changeDataFile);
        } catch (IOException e) {
            return 0;
        }
    }

    public int getChangeSetCount() {
        return changeSetCount;
    }

    /**
     * Returns the timestamp of when the change log was created.
     * @return The timestamp.  An empty value if the extractor has not been run or if the change log is corrupt.
     */
    @Nonnull
    public Optional<Long> getCreatedAtTimestamp() {
        if(firstTimeStamp == -1) {
            return Optional.empty();
        }
        else {
            return Optional.of(firstTimeStamp);
        }
    }


    /**
     * Returns the timestamp of when the change log was last modified.
     * @return The timestamp.  An empty value if the extractor has not been run or if the change log is corrupt.
     */
    @Nonnull
    public Optional<Long> getLastModifiedTimestamp() {
        if(lastTimeStamp == -1L) {
            return Optional.empty();
        }
        else {
            return Optional.of(lastTimeStamp);
        }
    }


    /**
     * Returns the user name who made the last change.
     * @return The user name.
     */
    @Nonnull
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    private void handleChangeData(OntologyChangeRecordList list) {
        changeSetCount++;
        long ts = list.getTimestamp();
        if(firstTimeStamp == -1L) {
            firstTimeStamp = ts;
        }
        lastTimeStamp = ts;
        BinaryOWLMetadata metadata  = list.getMetadata();
        String user = metadata.getStringAttribute("username", null);
        if(user != null) {
            lastModifiedBy = user;
        }
    }
}
