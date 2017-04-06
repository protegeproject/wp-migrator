package edu.stanford.protege.webprotege.migration;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.HasGetEntitiesInSignature;
import org.semanticweb.owlapi.model.HasSignature;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 *
 * Provides properties in the signature of the change log.
 */
public class HasGetEntitiesInSignatureProperties implements HasGetEntitiesInSignature {

    @Nonnull
    private final Path changeLogPath;

    @Nullable
    private SetMultimap<IRI, OWLEntity> signatureMap = null;

    public HasGetEntitiesInSignatureProperties(@Nonnull Path changeLogPath) {
        this.changeLogPath = checkNotNull(changeLogPath);
    }

    /**
     * Gets the entities in the signature of this change log that are properties.
     * @param propertyIRI The property IRI.
     * @return A set of entities that are properties with the specified IRI.
     */
    @Nonnull
    @Override
    public Set<OWLEntity> getEntitiesInSignature(@Nonnull IRI propertyIRI) {
        if (signatureMap == null) {
            loadSignature();
        }
        return signatureMap.get(propertyIRI);
    }


    private void loadSignature() {
        this.signatureMap = HashMultimap.create();
        BinaryOWLOntologyChangeLog log = new BinaryOWLOntologyChangeLog();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(changeLogPath))) {
            log.readChanges(bufferedInputStream, new OWLDataFactoryImpl(), (list, skipSetting, filePosition) -> {
                list.getChangeRecords().stream()
                    .map(OWLOntologyChangeRecord::getData)
                    .map(HasSignature::getSignature)
                    .flatMap(Collection::stream)
                    .filter(entity ->
                                    entity.isOWLObjectProperty()
                                            || entity.isOWLDataProperty()
                                            || entity.isOWLAnnotationProperty())
                    .forEach(entity -> signatureMap.put(entity.getIRI(), entity));
            });
        } catch (IOException e) {
            System.out.printf("An error occurred reading the change log: %s\n", e.getMessage());
        }
    }


}
