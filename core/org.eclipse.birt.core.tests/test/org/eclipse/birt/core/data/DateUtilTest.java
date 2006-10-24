/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.data;


import java.text.ParseException;
import java.util.Date;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;


/**
 *
 */
public class DateUtilTest extends TestCase
{
	/**
	 * Test DataTypeUtil#checkValid
	 */
	public void testCheckValid( )
	{
		ULocale locale;
		DateFormat df;	
		String dateStr;
		boolean isValid;
		
		// ------------test of Locale.UK
		locale = ULocale.UK; //dd/MM/yy
		
		df = DateFormat.getDateInstance( DateFormat.SHORT, locale );		
		dateStr = "25/11/16 ";
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertTrue( isValid );
		
		dateStr = "25/11/6 ";
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertTrue( isValid );
		
		dateStr = "2005/11/16 "; // invalid dd
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertFalse( isValid );
		
		// ------------test of Locale.US
		locale = ULocale.US; //MM/dd/yy
		df = DateFormat.getDateInstance( DateFormat.SHORT, locale );		
		dateStr = "11/25/16";
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertTrue( isValid );
		
		dateStr = "21/11/6"; // invalid MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertFalse( isValid );
		
		dateStr = "2005/11/6"; // invalid MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertFalse( isValid );
		
		dateStr = "11/44/16"; // invalid dd
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertFalse( isValid );
		
		dateStr = "11/31/1990"; // invalid dd to MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertFalse( isValid );
		
		dateStr = "02/29/1990"; // invalid dd to MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertFalse( isValid );
		
		dateStr = "02/28/1990"; // invalid dd to MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertTrue( isValid );
		
		// ------------test of Locale.CHINA
		locale = ULocale.CHINA; //yy-M-d
		df = DateFormat.getDateInstance( DateFormat.SHORT, locale );		
		dateStr = "2005-3-3";
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertTrue( isValid );
		
		dateStr = "2005-13-6"; // invalid MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertFalse( isValid );
		
		dateStr = "2005-11-36"; // invalid dd
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		assertFalse( isValid );
		
		dateStr = "5-13-2005"; // invalid dd
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );		
		assertFalse( isValid );
		
		dateStr = "2005-11-31"; // invalid dd to MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );
		
		dateStr = "2005-2-29"; // invalid dd to MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );		
		assertFalse( isValid );
		
		dateStr = "2005-2-28"; // invalid dd to MM
		try
		{
			Date date = df.parse( dateStr );
		}
		catch ( ParseException e )
		{
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid( df, dateStr );		
		assertTrue( isValid );	
	}

}
