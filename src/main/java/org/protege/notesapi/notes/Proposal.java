package org.protege.notesapi.notes;

/** @author csnyulas */
public interface Proposal extends Annotation {

    public static final NoteType Type = NoteType.Proposal;


    // contactInformation

    String getContactInformation();

    boolean hasContactInformation();

    void setContactInformation(String newContactInformation);


    // reasonForChange

    String getReasonForChange();

    boolean hasReasonForChange();

    void setReasonForChange(String newReasonForChange);

}
