package org.protege.notesapi.notes;

/** @author csnyulas */
public interface VoteFiveStars extends Vote {

    public static final NoteType Type = NoteType.VoteFiveStars;

    public static final String VOTE_VALUE_ONE_STAR = "*";

    public static final String VOTE_VALUE_TWO_STARS = "**";

    public static final String VOTE_VALUE_THREE_STARS = "***";

    public static final String VOTE_VALUE_FOUR_STARS = "****";

    public static final String VOTE_VALUE_FIVE_STARS = "*****";

    public static final String VOTE_VALUE_NO_VOTE = "No_Vote";

    public static final String[] ALLOWED_VALUES_FOR_VOTE_VALUE =
            new String[]{VOTE_VALUE_ONE_STAR, VOTE_VALUE_TWO_STARS, VOTE_VALUE_THREE_STARS,
                    VOTE_VALUE_FOUR_STARS, VOTE_VALUE_FIVE_STARS, VOTE_VALUE_NO_VOTE};

}
