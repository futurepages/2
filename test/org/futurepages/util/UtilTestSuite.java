package org.futurepages.util;

import org.futurepages.util.html.HtmlMapCharsTest;
import org.futurepages.util.html.HtmlRegexTest;
import org.futurepages.util.html.HtmlStripperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    CNPJUtilTest.class,
    CPFUtilTest.class,
    CalendarUtilTest.class,
    CalendarUtil_literalRangeOfDatesTest.class,
    CalendarUtil_literalRangeOfTimesTest.class,
    CollectionUtilTest.class,
    DateUtilTest.class,
    FileUtilTest.class,
    HQLUtilTest.class,
    HtmlMapCharsTest.class,
	HtmlRegexTest.class,
	HtmlStripperTest.class,
    IsTest.class,
    NumberUtilTest.class,
    ProbabilityUtilTest.class,
    SecurityTest.class,
    SEOUtilTest.class,
    StringUtilTest.class,
    TheTest.class    
})
public class UtilTestSuite {
}
