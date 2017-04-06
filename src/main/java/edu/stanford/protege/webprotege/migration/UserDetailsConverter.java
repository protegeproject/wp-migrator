package edu.stanford.protege.webprotege.migration;

import edu.stanford.smi.protege.server.metaproject.User;
import org.bson.Document;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2017
 */
public class UserDetailsConverter {


    private static final String ID = "_id";

    private static final String REAL_NAME = "realName";

    private static final String EMAIL_ADDRESS = "emailAddress";

    private static final String SALT = "salt";

    private static final String SALTED_PASSWORD_DIGEST = "saltedPasswordDigest";

    private final User user;

    public UserDetailsConverter(@Nonnull User user) {
        this.user = checkNotNull(user);
    }

    public Optional<Document> convert() {
        /*
            "_id" : "<UserId>",
            "realName" : "<Real Name>",
            "emailAddress" : "<Email>",
            "salt" : "<Salt>",
            "saltedPasswordDigest" : "<SaltedPasswordDigest>"
         */
        String userId = user.getName();
        if(userId == null) {
            return Optional.empty();
        }
        String emailAddress = Optional.ofNullable(user.getEmail()).orElse("");
        String salt = Optional.ofNullable(user.getSalt()).orElse("");
        String saltedPasswordDigest = Optional.ofNullable(user.getDigestedPassword()).orElse("");
        Document document = new Document();
        document.append(ID, userId);
        document.append(REAL_NAME, userId);
        document.append(EMAIL_ADDRESS, emailAddress);
        document.append(SALT, salt);
        document.append(SALTED_PASSWORD_DIGEST, saltedPasswordDigest);
        return Optional.of(document);
    }
}
