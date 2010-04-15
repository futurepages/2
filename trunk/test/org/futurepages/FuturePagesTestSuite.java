package org.futurepages;

import org.futurepages.core.persistence.HQLFieldTest;
import org.futurepages.core.persistence.HQLProviderTest;
import org.futurepages.core.persistence.HibernateFilterTest_isTransactional;
import org.futurepages.enums.EnumTestSuite;
import org.futurepages.filters.FilterTestSuite;
import org.futurepages.tags.TagsTestSuite;
import org.futurepages.util.UtilTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	EnumTestSuite.class,
	FilterTestSuite.class,
	UtilTestSuite.class,
	TagsTestSuite.class,
	HQLProviderTest.class,
	HQLFieldTest.class,
	HibernateFilterTest_isTransactional.class
})
public class FuturePagesTestSuite {

}