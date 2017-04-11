package org.protege.notesapi.notes;

import org.protege.notesapi.oc.OntologyProperty;

/** @author csnyulas */
public interface ProposalChangePropertyValue extends Proposal {

    public static final NoteType Type = NoteType.ProposalForChangePropertyValue;


    // newValue

    String getNewValue();

    boolean hasNewValue();

    void setNewValue(String newNewValue);


    // oldValue

    String getOldValue();

    boolean hasOldValue();

    void setOldValue(String newOldValue);


    // property

    OntologyProperty getProperty();

    boolean hasProperty();

    void setProperty(OntologyProperty newProperty);

}
