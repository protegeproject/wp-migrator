package edu.stanford.protege.webprotege.migration;

import edu.stanford.smi.protege.server.metaproject.Operation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 *
 * Converts Protege 3 operations to actions
 */
public class OperationConverter {

    private static final String CAN_VIEW = "CanView";

    private static final String CAN_COMMENT = "CanComment";

    private static final String CAN_EDIT = "CanEdit";


    private static final String READ_PERMISSION = "Read";

    private static final String COMMENT_PERMISSION = "Comment";

    private static final String WRITE_PERMISSION = "Write";


    /**
     * Convert the specified operation to a set of roles.
     * @param operations The operation to be converted.
     * @return The set of roles
     */
    @Nonnull
    public Set<String> convert(@Nonnull Collection<Operation> operations) {
        Set<String> result = new TreeSet<>();
        for (Operation operation : operations) {
            toRoles(operation).ifPresent(result::add);
        }
        if(result.contains(CAN_EDIT)) {
            result.remove(CAN_COMMENT);
            result.remove(CAN_VIEW);
        }
        else if(result.contains(CAN_COMMENT)) {
            result.remove(CAN_VIEW);
        }
        return result;
    }


    @Nonnull
    private static Optional<String> toRoles(@Nonnull Operation operation) {
        // Only map to CanView, CanComment, CanEdit
        String operationName = operation.getName();
        switch (operationName) {
            case READ_PERMISSION:
                return Optional.of(CAN_VIEW);
            case COMMENT_PERMISSION:
                return Optional.of(CAN_COMMENT);
            case WRITE_PERMISSION:
                return Optional.of(CAN_EDIT);
            default:
                return Optional.empty();
        }
    }
}
