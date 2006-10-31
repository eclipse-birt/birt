
package org.eclipse.birt.report.model.adapter.oda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.adapter.oda.api.DataSetParameterAdapterTest;
import org.eclipse.birt.report.model.adapter.oda.api.OdaDataSetAdapterTest;
import org.eclipse.birt.report.model.adapter.oda.api.OdaDataSourceAdapterTest;
import org.eclipse.birt.report.model.adapter.oda.api.ReportParameterAdapterTest;
import org.eclipse.birt.report.model.adapter.oda.api.ResultSetColumnAdapterTest;
import org.eclipse.birt.report.model.adapter.oda.util.ParameterValueUtilTest;

/**
 * Tests cases run in the build script.
 */

public class AllTests extends TestCase
{

	/**
	 * @return
	 */
	
	public static Test suite( )
	{
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.birt.report.model.adapter.oda" ); //$NON-NLS-1$
		// $JUnit-BEGIN$

		/* in package: org.eclipse.birt.report.engine.api */
		suite.addTestSuite( DataSetParameterAdapterTest.class );
		suite.addTestSuite( OdaDataSourceAdapterTest.class );
		suite.addTestSuite( OdaDataSetAdapterTest.class );
        suite.addTestSuite( ParameterValueUtilTest.class );
        suite.addTestSuite( ResultSetColumnAdapterTest.class );
        suite.addTestSuite( ReportParameterAdapterTest.class );
		// $JUnit-END$
		return suite;
	}

}
