package edu.stanford.protege.webprotege.migration;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Apr 2017
 */
public class NoteBodyTidy_TestCase {

    private NoteBodyTidy tidy;

    @Before
    public void setUp() throws Exception {
        tidy = new NoteBodyTidy();
    }

    @Test
    public void shouldReplaceBreakElement() {
        String tidied = tidy.tidy("Test<br>Test");
        assertThat(tidied, is("Test\nTest"));
    }

    @Test
    public void shouldReplaceBreakElementWithSlash() {
        String tidied = tidy.tidy("Test<br/>Test");
        assertThat(tidied, is("Test\nTest"));
    }

    @Test
    public void shouldRemoveTrailingBreakElement() {
        String tidied = tidy.tidy("Test<br>");
        assertThat(tidied, is("Test"));
    }

    @Test
    public void shouldReplaceClosedParagraphElements() {
        String tidied = tidy.tidy("Test<p>Test</p>Test");
        assertThat(tidied, is("Test\n\nTest\n\nTest"));
    }

    @Test
    public void shouldReplaceUnclosedParagraphElements() {
        String tidied = tidy.tidy("Test<p>Test<p>Test");
        assertThat(tidied, is("Test\n\nTest\n\nTest"));
    }

    @Test
    public void shouldRemoveLeadingParagraphElements() {
        String tidied = tidy.tidy("<p>Test<p>Test<p>Test");
        assertThat(tidied, is("Test\n\nTest\n\nTest"));
    }

    @Test
    public void shouldRemoveTrailingParagraphElements() {
        String tidied = tidy.tidy("<p>Test</p><p>Test</p><p>Test</p>");
        assertThat(tidied, is("Test\n\nTest\n\nTest"));
    }

    @Test
    public void shouldReplaceBoldElements() {
        String tidied = tidy.tidy("<b>Test</b>");
        assertThat(tidied, is("**Test**"));
    }

    @Test
    public void shouldRemoveEmptyDivs() {
        String tidied = tidy.tidy("Test<div></div>");
        assertThat(tidied, is("Test"));
    }

    @Test
    public void shouldRemoveEmptyDivs2() {
        String tidied = tidy.tidy("Test<div/>");
        assertThat(tidied, is("Test"));
    }

    @Test
    public void shouldRemoveDivsContainingWhiteSpace() {
        String tidied = tidy.tidy("Test<div>  \n</div>");
        assertThat(tidied, is("Test"));
    }

    @Test
    public void shouldRemoveOuterMostDivs() {
        String tidied = tidy.tidy("<div>Test<div>Test</div></div>");
        assertThat(tidied, is("Test<div>Test</div>"));
    }



}
