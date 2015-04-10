/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.computation;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

public class MonthDateFormatTest extends TestCase
{

	public void testFormat( )
	{
		Calendar calendar = Calendar.getInstance( );
		calendar.set( Calendar.YEAR, 2013 );
		calendar.set( Calendar.MONTH, 5 );
		calendar.set( Calendar.DATE, 8 );
		Date date = calendar.getTime( );
		Hashtable<ULocale, String> locales = new Hashtable<ULocale, String>( );
		locales.put( ULocale.CANADA, "Jun 2013" ); //$NON-NLS-1$
		locales.put( ULocale.CHINA, "2013年6月" ); //$NON-NLS-1$
		locales.put( ULocale.ENGLISH, "Jun 2013" ); //$NON-NLS-1$
		locales.put( ULocale.FRANCE, "juin 2013" ); //$NON-NLS-1$
		locales.put( ULocale.GERMAN, "06.2013" ); //$NON-NLS-1$
		locales.put( ULocale.ITALY, "giu 2013" ); //$NON-NLS-1$
		locales.put( ULocale.JAPAN, "2013/06" ); //$NON-NLS-1$
		locales.put( ULocale.KOREA, "2013. 6" ); //$NON-NLS-1$
		locales.put( ULocale.SIMPLIFIED_CHINESE, "2013年6月" ); //$NON-NLS-1$
		locales.put( ULocale.TAIWAN, "2013年6月" ); //$NON-NLS-1$
		locales.put( ULocale.TRADITIONAL_CHINESE, "2013年6月" ); //$NON-NLS-1$
		locales.put( ULocale.UK, "Jun 2013" ); //$NON-NLS-1$

		for ( Iterator<ULocale> itr = locales.keySet( ).iterator( ); itr.hasNext( ); )
		{
			ULocale locale = itr.next( );
			IDateFormatWrapper formatter = DateFormatWrapperFactory.getPreferredDateFormat( Calendar.MONTH,
					locale,
					true );
			assertEquals( locales.get( locale ), formatter.format( date ) );
		}
	}
}
