
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.script.bre;

import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import junit.framework.TestCase;

/**
 * 
 */

public class BirtDurationTest extends TestCase
{
	private Context cx;
	private Scriptable scope;
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void setUp() throws Exception
	{
		/*
		 * Creates and enters a Context. The Context stores information about
		 * the execution environment of a script.
		 */

		cx = Context.enter( );
		/*
		 * Initialize the standard objects (Object, Function, etc.) This must be
		 * done before scripts can be executed. Returns a scope object that we
		 * use in later calls.
		 */
		scope = cx.initStandardObjects( );

		new CoreJavaScriptInitializer().initialize( cx, scope );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
    public void tearDown()
	{
		Context.exit( );
	}
	@Test
    public void testAdd() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.add( \"P1Y2M1D\", \"P1Y2M2D\" )";
		String script2 = "BirtDuration.add( \"P1Y2M1DT3S\", \"P1Y2M2D\" )";
		
		assertEquals( toDuration( ((String)cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null )) ), toDuration( "P2Y4M3D" ) );
		assertEquals( toDuration(((String)cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ))) ,toDuration("P2Y4M3DT3S") );
	}
	@Test
    public void testAddTo() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.addTo( \"P1Y2M1D\", new Date(53,11,11) )";
		String script2 = "BirtDuration.addTo( \"P1Y2M1DT3S\", new Date(53,11,11) )";
		
		assertEquals(  cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ), new Date( 55,1,12 ) );
		assertEquals( cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ,new Date( 55, 1, 12, 0, 0, 3 )  );
	}
	@Test
    public void testTimeInMills() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.timeInMills( \"PT1S\", new Date(53,11,11) )";
		String script2 = "BirtDuration.timeInMills( \"PT3S\", new Date(53,11,11) )";
		
		assertEquals(  cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ), Long.valueOf( 1000 ) );
		assertEquals( cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ,Long.valueOf( 3000 ) );
	}
	@Test
    public void testSubstract() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.subtract( \"P1Y2M1D\", \"P1Y2M2D\" )";
		String script2 = "BirtDuration.subtract( \"P1Y2M2DT3S\", \"P1Y2M2D\" )";
		
		assertEquals( toDuration(((String)cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ))) , toDuration("-P1D") );
		assertEquals( toDuration(((String)cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ))) ,toDuration("PT3S") );
	}
	@Test
    public void testMultiply() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.multiply( \"P1Y2M1D\", 1 )";
		String script2 = "BirtDuration.multiply( \"P1Y2M2DT3S\", 2 )";
		
		assertEquals( toDuration(((String)cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ))) ,toDuration("P1Y2M1D") );
		assertEquals( toDuration(((String)cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ))) ,toDuration("P2Y4M4DT6S") );
	}
	@Test
    public void testCompare() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.compare( \"P1Y2M1D\", \"P1Y2M2D\" )";
		String script2 = "BirtDuration.compare( \"P1Y2M2DT3S\", \"P1Y2M2DT3S\" )";
		
		assertEquals( cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ,Integer.valueOf( -1 ) );
		assertEquals( cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ,Integer.valueOf( 0 ) );
	}
	@Test
    public void testIsLongerThan() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.isLongerThan( \"P1Y2M3D\", \"P1Y2M2D\" )";
		String script2 = "BirtDuration.isLongerThan( \"P1Y2M2DT2S\", \"P1Y2M2DT3S\" )";
		
		assertEquals( cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ,Boolean.valueOf( true ) );
		assertEquals( cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ,Boolean.valueOf( false ) );
	}
	@Test
    public void testIsShorterThan() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.isShorterThan( \"P1Y2M3D\", \"P1Y2M2D\" )";
		String script2 = "BirtDuration.isShorterThan( \"P1Y2M2DT2S\", \"P1Y2M2DT3S\" )";
		
		assertEquals( cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ,Boolean.valueOf( false ) );
		assertEquals( cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ,Boolean.valueOf( true ) );
	}
	@Test
    public void testGetSign() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.getSign( \"-P1Y2M1D\" )";
		String script2 = "BirtDuration.getSign( \"P1Y2M2DT3S\" )";
		
		assertEquals( cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ,Integer.valueOf( -1 ) );
		assertEquals( cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ,Integer.valueOf( 1 ) );
	}
	@Test
    public void testNegate() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.negate( \"-P1Y2M1D\" )";
		String script2 = "BirtDuration.negate( \"P1Y2M2DT3S\" )";
		
		assertEquals( toDuration( ((String)cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null )) ), toDuration( "P1Y2M1D" ) );
		assertEquals( toDuration(((String)cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ))) ,toDuration("-P1Y2M2DT3S") );
	}
	@Test
    public void testYear() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.year( \"P1Y2M1D\" )";
		String script2 = "BirtDuration.year( \"P2M2DT3S\" )";
		
		assertEquals( (cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null )) ,Integer.valueOf( 1 ) );
		assertEquals( (cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null )) ,Integer.valueOf( 0 ) );
	}
	@Test
    public void testMonth() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.month( \"P1Y2M1D\" )";
		String script2 = "BirtDuration.month( \"P2DT3S\" )";
		
		assertEquals( (cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null )) ,Integer.valueOf( 2 ) );
		assertEquals( (cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null )) ,Integer.valueOf( 0 ) );
	}
	@Test
    public void testDay() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.day( \"P1Y2M1D\" )";
		String script2 = "BirtDuration.day( \"P2M2DT3S\" )";
		
		assertEquals( (cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null )) ,Integer.valueOf( 1 ) );
		assertEquals( (cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null )) ,Integer.valueOf( 2 ) );
	}
	@Test
    public void testHour() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.hour( \"P1Y2M1D\" )";
		String script2 = "BirtDuration.hour( \"P2M2DT4H\" )";
		
		assertEquals( (cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null )) ,Integer.valueOf( 0 ) );
		assertEquals( (cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null )) ,Integer.valueOf( 4 ) );
	}
	@Test
    public void testMinute() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.minute( \"P1Y2M1D\" )";
		String script2 = "BirtDuration.minute( \"P2M2DT4M\" )";
		
		assertEquals( (cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null )) ,Integer.valueOf( 0 ) );
		assertEquals( (cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null )) ,Integer.valueOf( 4 ) );
	}
	@Test
    public void testSecond() throws DatatypeConfigurationException
	{
		String script1 = "BirtDuration.second( \"P1Y2M1D\" )";
		String script2 = "BirtDuration.second( \"P2M2DT4S\" )";
		
		assertEquals( (cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null )) ,Integer.valueOf( 0 ) );
		assertEquals( (cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null )) ,Integer.valueOf( 4 ) );
	}
	
	private static Duration toDuration( String arg ) throws DatatypeConfigurationException 
	{
		Duration duration;
		duration = DatatypeFactory.newInstance( )
					.newDuration( arg );
		return duration;
	}
}
