
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 */

public class OdaConsumerTests
{
	/**
	 * @return
	 */
	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.data.engine.odaconsumer" );

		/* in package org.eclipse.birt.data.engine.odaconsumer */
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.AppContextTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.ConnectionManagerTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.ConnectionTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.DataSetCapabilitiesTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.DriverManagerTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.LargeObjectTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.ManifestExplorerTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.OdaconsumerTestCase.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.OutputParametersTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.ParameterHintTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.PreparedStatementTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.ProjectedColumnsTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.QueryTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.ResultSetMetaDataTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.odaconsumer.ResultSetTest.class);
		return suite;
	}
	
}
