package org.protege.notesapi.notes;

import org.protege.notesapi.oc.OntologyComponent;

import java.util.Collection;

/** @author csnyulas */
public interface ProposalChangeHierarchy extends Proposal {

    public static final NoteType Type = NoteType.ProposalForChangeHierarchy;

    public static final String RELATION_TYPE_SUBCLASS_OF = "subclassOf";

    public static final String RELATION_TYPE_PART_OF = "partOf";

    public static final String[] ALLOWED_VALUES_FOR_RELATION_TYPE =
            new String[]{RELATION_TYPE_SUBCLASS_OF, RELATION_TYPE_PART_OF};


    // newParents

    Collection<OntologyComponent> getNewParents();

    boolean hasNewParents();

    void addNewParents(OntologyComponent newNewParents);

    void removeNewParents(OntologyComponent oldNewParents);

    void setNewParents(Collection<? extends OntologyComponent> newNewParents);


    // oldParents

    Collection<OntologyComponent> getOldParents();

    boolean hasOldParents();

    void addOldParents(OntologyComponent newOldParents);

    void removeOldParents(OntologyComponent oldOldParents);

    void setOldParents(Collection<? extends OntologyComponent> newOldParents);


    // relationType

    String getRelationType();

    boolean hasRelationType();

    void setRelationType(String newRelationType);

}
