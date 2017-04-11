package org.protege.notesapi.notes;

/** @author csnyulas */
public interface VoteAgreeDisagree extends Vote {

    public static final NoteType Type = NoteType.VoteAgreeDisagree;

    public static final String VOTE_VALUE_I_AGREE = "I_agree";

    public static final String VOTE_VALUE_I_DISAGREE = "I_disagree";

    public static final String VOTE_VALUE_I_DONT_KNOW = "I_don't_know";

    public static final String[] ALLOWED_VALUES_FOR_VOTE_VALUE =
            new String[]{VOTE_VALUE_I_AGREE, VOTE_VALUE_I_DISAGREE, VOTE_VALUE_I_DONT_KNOW};

}
