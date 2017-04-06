package edu.stanford.protege.webprotege.migration;

import edu.stanford.smi.protege.server.metaproject.Operation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class OperationConverter_TestCase {

    private OperationConverter converter;

    @Mock
    private Operation operation;

    @Before
    public void setUp() throws Exception {
        converter = new OperationConverter();
    }

    @Test
    public void shouldConvertReadOperationToCanViewRole() {
        when(operation.getName()).thenReturn("Read");
        Set<String> roles = converter.convert(Collections.singleton(operation));
        assertThat(roles, hasItems("CanView"));
    }

    @Test
    public void shouldConvertCommentOperationToCanCommentRole() {
        when(operation.getName()).thenReturn("Comment");
        Set<String> roles = converter.convert(Collections.singleton(operation));
        assertThat(roles, hasItems("CanComment"));
    }

    @Test
    public void shouldConvertWriteOperationToCanEditRole() {
        when(operation.getName()).thenReturn("Write");
        Set<String> roles = converter.convert(Collections.singleton(operation));
        assertThat(roles, hasItems("CanEdit"));
    }

    @Test
    public void shouldNotConvertOtherOperationToRole() {
        when(operation.getName()).thenReturn("DisplayInProjectList");
        Set<String> roles = converter.convert(Collections.singleton(operation));
        assertThat(roles.isEmpty(), is(true));
    }

    @Test
    public void shouldPerformMinimalConversionOfCommentAndRead() {
        when(operation.getName()).thenReturn("Read");
        Operation commentOp = mock(Operation.class);
        when(commentOp.getName()).thenReturn("Comment");
        Set<String> roles = converter.convert(Arrays.asList(operation, commentOp));
        assertThat(roles, hasItems("CanComment"));
    }

    @Test
    public void shouldPerformMinimalConversionOfWriteAndCommentAndRead() {
        when(operation.getName()).thenReturn("Write");
        Operation commentOp = mock(Operation.class);
        when(commentOp.getName()).thenReturn("Comment");
        Operation readOp = mock(Operation.class);
        when(readOp.getName()).thenReturn("Read");
        Set<String> roles = converter.convert(Arrays.asList(operation, commentOp, readOp));
        assertThat(roles, hasItems("CanEdit"));
    }
}
