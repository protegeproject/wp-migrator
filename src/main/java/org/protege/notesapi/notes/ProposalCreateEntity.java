package org.protege.notesapi.notes;

import org.protege.notesapi.NotesException;
import org.protege.notesapi.oc.OntologyClass;
import org.protege.notesapi.oc.OntologyComponent;
import org.protege.notesapi.oc.OntologyProperty;

import java.util.Collection;

/** @author csnyulas */
public interface ProposalCreateEntity extends Proposal {

    public static final NoteType Type = NoteType.ProposalForCreateEntity;


    // definition

    LinguisticEntity getDefinition();

    boolean hasDefinition();

    void setDefinition(LinguisticEntity newDefinition);


    // entityId

    String getEntityId();

    boolean hasEntityId();

    void setEntityId(String newEntityId);


    // parents

    Collection<OntologyComponent> getParents();

    boolean hasParents();

    void addParents(OntologyClass newParent);

    void addParents(OntologyProperty newParent);

    void removeParents(OntologyClass oldParent);

    void removeParents(OntologyProperty oldParent);

    /**
     * @param newParents collection of instances of one of the following types:
     *                   {@link OntologyClass} or {@link OntologyProperty}.
     * @throws NotesException thrown if the argument contains instances of invalid type.
     */
    void setParents(Collection<? extends OntologyComponent> newParents) throws NotesException;


    // preferredName

    LinguisticEntity getPreferredName();

    boolean hasPreferredName();

    void setPreferredName(LinguisticEntity newPreferredName);


    // synonym

    Collection<LinguisticEntity> getSynonym();

    boolean hasSynonym();

    void addSynonym(LinguisticEntity newSynonym);

    void removeSynonym(LinguisticEntity oldSynonym);

    void setSynonym(Collection<? extends LinguisticEntity> newSynonym);


    // createdEntity

    OntologyComponent getCreatedEntity();

    boolean hasCreatedEntity();

    void setCreatedEntity(OntologyComponent newCreatedEntity);

}
