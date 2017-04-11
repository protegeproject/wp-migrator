package org.protege.notesapi.notes;

/** @author csnyulas */
public interface Vote extends Annotation {

    public static final NoteType Type = NoteType.Vote;

    public static final String[] ALLOWED_VALUES_FOR_VOTE_VALUE = new String[]{};


    // voteValue

    String getVoteValue();

    boolean hasVoteValue();

    void setVoteValue(String newVoteValue);

}
