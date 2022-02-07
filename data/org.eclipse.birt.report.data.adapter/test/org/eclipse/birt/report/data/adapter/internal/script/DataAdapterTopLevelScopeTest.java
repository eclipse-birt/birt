/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.report.data.adapter.internal.script;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.report.data.adapter.api.script.DataAdapterTopLevelScope;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.mozilla.javascript.Context;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

public class DataAdapterTopLevelScopeTest extends TestCase
{
	private SessionHandle session;
	private ReportDesignHandle design;
	private DataAdapterTopLevelScope scope;
	private Context cx;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		
		// Create report design with several report parameters
		session = DesignEngine.newSession( ULocale.getDefault());
		design = session.createDesign();
		design.setFileName( "ABC.rptdesign" );
		setUpParam( "string_1", true, true, DesignChoiceConstants.PARAM_TYPE_STRING, "1_default");
		setUpParam( "string_2", true, true, DesignChoiceConstants.PARAM_TYPE_STRING, null);
		setUpParam( "string_3", false, true, DesignChoiceConstants.PARAM_TYPE_STRING, "");
		setUpParam( "string_4", false, false, DesignChoiceConstants.PARAM_TYPE_STRING, " ");
		
		setUpParam( "date_1", true, false, DesignChoiceConstants.PARAM_TYPE_DATETIME, "1/20/2006");
		setUpParam( "date_2", true, false, DesignChoiceConstants.PARAM_TYPE_DATETIME, null);
		setUpParam( "date_3", false, false, DesignChoiceConstants.PARAM_TYPE_DATETIME, null);
		
		setUpParam( "dec_1", true, false, DesignChoiceConstants.PARAM_TYPE_DECIMAL, "1234");
		setUpParam( "dec_2", false, false, DesignChoiceConstants.PARAM_TYPE_DECIMAL, null);
		
		setUpParam( "float_1", true, false, DesignChoiceConstants.PARAM_TYPE_FLOAT, "1234");
		setUpParam( "float_2", false, false, DesignChoiceConstants.PARAM_TYPE_FLOAT, null);
		
		setUpParam( "bool_1", true, false, DesignChoiceConstants.PARAM_TYPE_BOOLEAN, "true");
		setUpParam( "bool_2", false, false, DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null);
		cx = Context.enter();
		
	}

	protected void tearDown() throws Exception
	{
		session.closeAll(false);
		Context.exit();
		super.tearDown();
	}
	
	
	private void setUpParam( String name, boolean allowNull, boolean allowBlank, 
			String dataType, String defaultValue ) throws Exception
	{
		ElementFactory ef = design.getElementFactory();
		ScalarParameterHandle param = ef.newScalarParameter( name );
		param.setAllowNull(allowNull);
		param.setAllowBlank(allowBlank);
		param.setDataType(dataType);
		if ( defaultValue != null )
			param.setDefaultValue(defaultValue);
		design.getParameters().paste( param);
	}
	
	private Object evaluateScript( String scriptText ) throws Exception
	{
		Object obj = JavascriptEvalUtil.evaluateScript(cx, scope, scriptText, "inline", 0);
		return obj;
	}

	/**
	 * Test access to "params" with report handle
	 */
	public void testParams() throws Exception
	{
		scope = new DataAdapterTopLevelScope( cx, design);
		
		// test params.length
		Object len = evaluateScript("params.length");
		assertEquals( len.getClass(), Integer.class);
		assertEquals( ((Integer) len).intValue(), 13 );
		 
		// Test string parameters
		Object r = evaluateScript("params[\"string_1\"]");
		assertEquals( r.getClass(), String.class);
		assertEquals( r, "1_default" );
		
		r = evaluateScript("params[\"string_2\"]");
		assertNull( r );
		
		r = evaluateScript("params[\"string_3\"]");
		assertEquals( r.getClass(), String.class);
		assertEquals( r, "" );
		
		r = evaluateScript("params[\"string_4\"]");
		assertEquals( r.getClass(), String.class);
		assertTrue( r.toString().length() > 0 );
		
		// Test date params
		r = evaluateScript("params[\"date_1\"]");
		assertEquals( r.getClass(), Date.class);
	/*	r = evaluateScript("params[\"date_2\"]");
		assertNull( r);
		r = evaluateScript("params[\"date_3\"]");
		assertEquals( r.getClass(), Date.class);*/
		
		// Test float params
		r = evaluateScript("params[\"float_1\"]");
		assertEquals( r.getClass(), Double.class);
		assertEquals( ((Double)r).intValue(), 1234 );
		r = evaluateScript("params[\"float_2\"]");
		assertEquals( r.getClass(), Double.class);
		assertEquals( ((Double)r).intValue(), 0 );
		
		// Test decimal params
		r = evaluateScript("params[\"dec_1\"]");
		assertEquals( r.getClass(), BigDecimal.class);
		assertEquals( ((BigDecimal)r).intValue(), 1234 );
		r = evaluateScript("params[\"dec_2\"]");
		assertEquals( r.getClass(), BigDecimal.class);
		assertEquals( ((BigDecimal)r).intValue(), 0 );
		
		// Test boolean params
		r = evaluateScript("params[\"bool_1\"]");
		assertEquals( r.getClass(), Boolean.class);
		assertTrue( ((Boolean)r).booleanValue() );
		r = evaluateScript("params[\"bool_2\"]");
		assertEquals( r.getClass(), Boolean.class);
		assertFalse( ((Boolean)r).booleanValue() );
		
		// Test invalid param
		r = evaluateScript("params[\"bad_name\"]");
		assertNull(r);
		
		// Make sure that BIRT objects like "Finance" are accessible 
		// in this top scope
		r = evaluateScript("Finance");
		assertNotNull(r);
	}
	
	/**
	 * Test access to "params's value" with report handle
	 */
	public void testParamsWithItAttr() throws Exception
	{
		scope = new DataAdapterTopLevelScope( cx, design);
		 
		// Test string parameters
		Object r = evaluateScript("params[\"string_1\"].value");
		assertEquals( r.getClass(), String.class);
		assertEquals( r, "1_default" );
		
		r = evaluateScript("params[\"string_2\"].value");
		assertNull( r );
		
		r = evaluateScript("params[\"string_3\"].value");
		assertEquals( r.getClass(), String.class);
		assertEquals( r, "" );
		
		r = evaluateScript("params[\"string_4\"].value");
		assertEquals( r.getClass(), String.class);
		assertTrue( r.toString().length() > 0 );
		
		// Test date params
		r = evaluateScript("params[\"date_1\"].value");
		assertEquals( r.getClass(), Date.class);
		/*r = evaluateScript("params[\"date_2\"].value");
		assertNull( r);
		r = evaluateScript("params[\"date_3\"].value");
		assertEquals( r.getClass(), Date.class);*/
		
		// Test float params
		r = evaluateScript("params[\"float_1\"].value");
		assertEquals( r.getClass(), Double.class);
		assertEquals( ((Double)r).intValue(), 1234 );
		r = evaluateScript("params[\"float_2\"].value");
		assertEquals( r.getClass(), Double.class);
		assertEquals( ((Double)r).intValue(), 0 );
		
		// Test decimal params
		r = evaluateScript("params[\"dec_1\"].value");
		assertEquals( r.getClass(), BigDecimal.class);
		assertEquals( ((BigDecimal)r).intValue(), 1234 );
		r = evaluateScript("params[\"dec_2\"].value");
		assertEquals( r.getClass(), BigDecimal.class);
		assertEquals( ((BigDecimal)r).intValue(), 0 );
		
		// Test boolean params
		r = evaluateScript("params[\"bool_1\"].value");
		assertEquals( r.getClass(), Boolean.class);
		assertTrue( ((Boolean)r).booleanValue() );
		r = evaluateScript("params[\"bool_2\"].value");
		assertEquals( r.getClass(), Boolean.class);
		assertFalse( ((Boolean)r).booleanValue() );
		
		// Test put params value
		r = evaluateScript("params[\"string_1\"].value =\"param1\"");
		assertEquals( r.getClass(), String.class);
		assertEquals( r, "param1" );

		// Test dispayText params value
		r = evaluateScript("params[\"string_1\"].displayText =\"displayText\";params[\"string_1\"].displayText");
		assertEquals( r.getClass(), String.class);
		assertEquals( r, "displayText" );

		// Test error attribute name
		r = evaluateScript("params[\"string_1\"].displyText =\"displayText\";params[\"string_1\"].displyText");
		assertEquals( r, null );

		r = evaluateScript("params[\"string_2\"]=params[\"string_1\"]");
		assertEquals( r.getClass(), String.class );
		assertEquals( r, "param1" );
	}

	/**
	 * Test access to "params" w/o report handle
	 */
	public void testParamsNoReportHandle() throws Exception
	{
		scope = new DataAdapterTopLevelScope( cx, null);
		try
		{
			evaluateScript("params[\"string_1\"]");
			fail("Exception expected");
		}
		catch( BirtException e)
		{
			//ok
		}
	}
}
