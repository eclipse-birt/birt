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

public class DateTimeDataElementComposite extends Composite
		implements
			IDataElementComposite,
			Listener
{

	private Button btnCheck;
	private DateTime pickerDate;
	private DateTime pickerTime;
	private Vector vListeners = null;

	public DateTimeDataElementComposite( Composite parent,
			DateTimeDataElement data )
	{
		super( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 3, false );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout( layout );
		this.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		btnCheck = new Button( this, SWT.CHECK );
		btnCheck.addListener( SWT.Selection, this );

		pickerDate = new DateTime( this, SWT.DATE );
		pickerTime = new DateTime( this, SWT.TIME );
		pickerDate.addListener( SWT.Selection, this );
		pickerTime.addListener( SWT.Selection, this );

		vListeners = new Vector( );

		setDataElement( data );
	}

	public void setEnabled( boolean enabled )
	{
		btnCheck.setEnabled( enabled );
		pickerDate.setEnabled( enabled && btnCheck.getSelection( ) );
		pickerTime.setEnabled( enabled && btnCheck.getSelection( ) );
	}

	public DataElement getDataElement( )
	{
		if ( !btnCheck.getSelection( ) )
		{
			return null;
		}
		Calendar calendar = Calendar.getInstance( );
		calendar.set( pickerDate.getYear( ),
				pickerDate.getMonth( ),
				pickerDate.getDay( ),
				pickerTime.getHours( ),
				pickerTime.getMinutes( ),
				pickerTime.getSeconds( ) );
		return DateTimeDataElementImpl.create( calendar );
	}

	public void handleEvent( Event event )
	{
		if ( event.widget == btnCheck )
		{
			pickerDate.setEnabled( btnCheck.getSelection( ) );
			pickerTime.setEnabled( btnCheck.getSelection( ) );
		}
		event.widget = this;

		// Notify events to listeners
		for ( int i = 0; i < vListeners.size( ); i++ )
		{
			Event e = new Event( );
			e.data = getDataElement( );
			e.widget = this;
			e.type = DATA_MODIFIED;
			( (Listener) vListeners.get( i ) ).handleEvent( e );
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

		btnCheck.setSelection( data != null );

		Calendar calendar = null;
		if ( data == null )
		{
			// Clear time for default date
			calendar = Calendar.getInstance( );
			calendar.set( Calendar.HOUR, 0 );
			calendar.set( Calendar.MINUTE, 0 );
			calendar.set( Calendar.SECOND, 0 );
		}
		else
		{
			calendar = ( (DateTimeDataElement) data ).getValueAsCalendar( );
		}

		pickerDate.setYear( calendar.get( Calendar.YEAR ) );
		pickerDate.setMonth( calendar.get( Calendar.MONTH ) );
		pickerDate.setDay( calendar.get( Calendar.DATE ) );

		pickerTime.setHours( calendar.get( Calendar.HOUR ) );
		pickerTime.setMinutes( calendar.get( Calendar.MINUTE ) );
		pickerTime.setSeconds( calendar.get( Calendar.SECOND ) );
		setEnabled( data != null );
	}
}
