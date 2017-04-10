package edu.stanford.protege.webprotege.migration;

import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Apr 2017
 */
public class NoteBodyTidy {

    private Pattern brPattern = Pattern.compile("<br/?>");

    private Pattern pPattern = Pattern.compile("</?p>");

    private Pattern bPattern = Pattern.compile("</?b>");

    private Pattern divPattern = Pattern.compile("<div>\\s*</div>|<div/>", Pattern.MULTILINE);


    public NoteBodyTidy() {
    }

    @Nonnull
    public String tidy(@Nonnull String body) {
        String replacedPElements = replaceParagraphElements(body);
        String replacedBElements = replaceBoldElements(replacedPElements);
        String replacedBrElements = replaceBreakElements(replacedBElements);
        String replacedDivs = replaceEmptyDivElements(replacedBrElements);
        return removeOutermostDiv(replacedDivs);
    }

    private String removeOutermostDiv(String replacedDivs) {
        if(replacedDivs.startsWith("<div>") && replacedDivs.endsWith("</div>")) {
            return replacedDivs.substring(5, replacedDivs.length() - 6);
        }
        else {
            return replacedDivs;
        }
    }

    private String replaceEmptyDivElements(String replacedBrElements) {
        return divPattern.matcher(replacedBrElements).replaceAll("");
    }

    private String replaceBreakElements(String replacedBElements) {
        return brPattern.matcher(replacedBElements).replaceAll("\n").trim();
    }

    private String replaceBoldElements(String replacedPElements) {
        return bPattern.matcher(replacedPElements).replaceAll("**");
    }

    private String replaceParagraphElements(@Nonnull String body) {
        return pPattern.matcher(body).replaceAll("\n\n").replace("\n\n\n\n", "\n\n").trim();
    }


}
