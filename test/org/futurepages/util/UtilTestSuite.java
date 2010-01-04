package org.futurepages.util;

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
    IsTest.class,
    NumberUtilTest.class,
    ProbabilityUtilTest.class,
    SecurityTest.class,
    SEOUtilTest.class,
    TheTest.class    
})
public class UtilTestSuite {
}
