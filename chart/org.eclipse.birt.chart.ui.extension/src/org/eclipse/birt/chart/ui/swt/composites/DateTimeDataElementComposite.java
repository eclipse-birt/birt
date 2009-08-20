/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataElementComposite;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.ibm.icu.util.Calendar;

/**
 * Composite for inputing DataTimeDataElement
 */

public class DateTimeDataElementComposite extends Composite implements
		IDataElementComposite,
		Listener
{

	private Button btnDate;
	private Button btnTime;
	private DateTime pickerDate;
	private DateTime pickerTime;
	private Vector<Listener> vListeners = null;
	private final boolean isNullAllowed;

	public DateTimeDataElementComposite( Composite parent, int style,
			DateTimeDataElement data, boolean isNullAllowed )
	{
		super( parent, SWT.NONE );
		this.isNullAllowed = isNullAllowed;

		GridLayout layout = new GridLayout( 4, false );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout( layout );
		this.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		btnDate = new Button( this, SWT.CHECK );
		btnDate.addListener( SWT.Selection, this );

		pickerDate = new DateTime( this, SWT.DATE | style );
		pickerDate.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		pickerDate.addListener( SWT.Selection, this );

		btnTime = new Button( this, SWT.CHECK );
		btnTime.addListener( SWT.Selection, this );

		pickerTime = new DateTime( this, SWT.TIME | style );
		pickerTime.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		pickerTime.addListener( SWT.Selection, this );

		vListeners = new Vector<Listener>( );

		setDataElement( data );
	}

	public void setEnabled( boolean enabled )
	{
		btnDate.setEnabled( enabled );
		btnTime.setEnabled( enabled );
		pickerDate.setEnabled( enabled && btnDate.getSelection( ) );
		pickerTime.setEnabled( enabled && btnTime.getSelection( ) );
	}

	public DataElement getDataElement( )
	{
		if ( isNullAllowed
				&& !btnDate.getSelection( )
				&& !btnTime.getSelection( ) )
		{
			return null;
		}
		Calendar calendar = Calendar.getInstance( );
		int year = 1970;
		int month = 0;
		int day = 1;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		if ( btnDate.getSelection( ) )
		{
			year = pickerDate.getYear( );
			month = pickerDate.getMonth( );
			day = pickerDate.getDay( );
		}
		if ( btnTime.getSelection( ) )
		{
			hours = pickerTime.getHours( );
			minutes = pickerTime.getMinutes( );
			seconds = pickerTime.getSeconds( );
		}
		calendar.set( year, month, day, hours, minutes, seconds );
		calendar.set( Calendar.MILLISECOND, 0 );
		return DateTimeDataElementImpl.create( calendar );
	}

	public void handleEvent( Event event )
	{
		if ( event.widget == btnDate )
		{
			pickerDate.setEnabled( btnDate.getSelection( ) );
		}
		else if ( event.widget == btnTime )
		{
			pickerTime.setEnabled( btnTime.getSelection( ) );
		}
		event.widget = this;

		// Notify events to listeners
		for ( int i = 0; i < vListeners.size( ); i++ )
		{
			Event e = new Event( );
			e.data = getDataElement( );
			e.widget = this;
			e.type = DATA_MODIFIED;
			vListeners.get( i ).handleEvent( e );
		}
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	public void setDataElement( DataElement data )
	{
		if ( !( data == null || data instanceof DateTimeDataElement ) )
		{
			return;
		}

		CDateTime calendar = null;
		if ( data != null )
		{
			calendar = ( (DateTimeDataElement) data ).getValueAsCDateTime( );
		}
		else
		{
			// Default null date
			calendar = new CDateTime( 1970, 1, 1, 0, 0, 0 );
		}

		if ( calendar.getYear( ) == 1970
				&& calendar.getMonth( ) == 0
				&& calendar.getDay( ) == 1 )
		{
			btnDate.setSelection( false );
			pickerDate.setEnabled( false );
		}
		else
		{
			btnDate.setSelection( true );
			pickerDate.setEnabled( true );
		}
		if ( calendar.getHour( ) == 0
				&& calendar.getMinute( ) == 0
				&& calendar.getSecond( ) == 0 )
		{
			btnTime.setSelection( false );
			pickerTime.setEnabled( false );
		}
		else
		{
			btnTime.setSelection( true );
			pickerTime.setEnabled( true );
		}

		pickerDate.setYear( calendar.getYear( ) );
		pickerDate.setMonth( calendar.getMonth( ) );
		pickerDate.setDay( calendar.getDay( ) );
		pickerTime.setHours( calendar.getHour( ) );
		pickerTime.setMinutes( calendar.getMinute( ) );
		pickerTime.setSeconds( calendar.getSecond( ) );
	}
}
