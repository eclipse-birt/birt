/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.engine;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.tests.engine.smoke.sampleReport.SampleReportTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * All test cases for tests Engine
 */
public class AllTests extends utility.BaseTestCase
{
	public static void main( String[] args )
	{
		Test t = AllTests.suite( );
		System.out.println( t );
	}
	
	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.report.tests.engine" ); //$NON-NLS-1$

		List classes = new ArrayList( );
		AllTests allTests = new AllTests( );

		classes.addAll( allTests.getClasses( "org.eclipse.birt.report.tests.engine.api" ) ); //$NON-NLS-1$
		classes.addAll( allTests.getClasses( "org.eclipse.birt.report.tests.engine.regression" ) ); //$NON-NLS-1$
		classes.add( "org.eclipse.birt.report.tests.engine.smoke.sampleReport.SampleReportTest" ); //$NON-NLS-1$
		
		Iterator iter = classes.iterator( );
		while ( iter.hasNext( ) )
		{
			String next = ( String ) iter.next( );
			
			try
			{
				Class c = Class.forName( next );
				int modifier = c.getModifiers( );
				if ( Modifier.isPublic( modifier ) && !Modifier.isStatic( modifier ) )
				{
					suite.addTestSuite( c );
				}
			}
			catch ( ClassNotFoundException e )
			{
				assert false;
			}
		}

		return suite;
	}
}