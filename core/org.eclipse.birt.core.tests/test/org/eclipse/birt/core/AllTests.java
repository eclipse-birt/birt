
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
package org.eclipse.birt.core;

import org.eclipse.birt.core.template.TemplateParserTest;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * 
 */

public class AllTests
{

	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.core" );
		//$JUnit-BEGIN$
		
		/* in package: org.eclipse.birt.core.archive */
		suite.addTestSuite( org.eclipse.birt.core.archive.DocumentArchiveTest.class );
		
		/* in package: org.eclipse.birt.core.config */
		suite.addTestSuite( org.eclipse.birt.core.config.FileConfigVarManagerTest.class );
		
		/* in package: org.eclipse.birt.core.data */
		suite.addTestSuite( org.eclipse.birt.core.data.DataTypeUtilTest.class );			
		suite.addTestSuite( org.eclipse.birt.core.data.DateUtilTest.class );				
		suite.addTestSuite( org.eclipse.birt.core.data.ExpressionParserUtilityTest.class );
		suite.addTestSuite( org.eclipse.birt.core.data.ExpressionUtilTest.class );			//
		
		/* in package: org.eclipse.birt.core.exception */
		suite.addTestSuite( org.eclipse.birt.core.exception.BirtExceptionTest.class );
		
		/* in package: org.eclipse.birt.core.format */
		suite.addTestSuite( org.eclipse.birt.core.format.DateFormatterTest.class );			
		suite.addTestSuite( org.eclipse.birt.core.format.NumberFormatterTest.class );
		suite.addTestSuite( org.eclipse.birt.core.format.StringFormatterTest.class );
		
		/* in package: org.eclipse.birt.core.script */
		suite.addTestSuite( org.eclipse.birt.core.script.ScriptContextTest.class );
		suite.addTestSuite( org.eclipse.birt.core.script.NativeNamedListTest.class );
		suite.addTestSuite( org.eclipse.birt.core.script.NativeDateTimeSpanTest.class );
		suite.addTestSuite( org.eclipse.birt.core.script.NativeFinanceTest.class );
		suite.addTestSuite( org.eclipse.birt.core.script.FinanceTest.class );
		
		/* in package: org.eclipse.birt.core.template */
		suite.addTestSuite( TemplateParserTest.class );
		
		/* in package: org.eclipse.birt.core.util */
		suite.addTestSuite( org.eclipse.birt.core.util.IOUtilTest.class );
		
		//$JUnit-END$
		return suite;
	}

}
