package com.yaison.cerebro;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.yaison.cerebro.algorithms.GradientDescentTest;
import com.yaison.cerebro.algorithms.KMeansTest;
import com.yaison.cerebro.algorithms.RegularizationTest;
import com.yaison.cerebro.algorithms.StochasticGradientDescentTest;
import com.yaison.cerebro.learning.DataConverterTest;
import com.yaison.cerebro.learning.LearningTest;
import com.yaison.cerebro.parsers.TextFileReaderTest;
import com.yaison.cerebro.structs.DataImplTest;
import com.yaison.cerebro.structs.SubMatrixTest;

@RunWith(Suite.class)
@SuiteClasses({ TextFileReaderTest.class, GradientDescentTest.class,
		StochasticGradientDescentTest.class, DataImplTest.class,
		SubMatrixTest.class, DataConverterTest.class, RegularizationTest.class,
		KMeansTest.class, LearningTest.class })
public class AllTests {
	
}
