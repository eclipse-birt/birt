/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * TristateCheckbox
 */

public class TristateCheckbox extends Composite implements Listener, SelectionListener
{

	public static final int STATE_GRAYED = 0;
	
	public static final int STATE_SELECTED = 1;
	
	public static final int STATE_UNSELECTED = 2;
	
	private Button button;
	private List<SelectionListener> selectListenerList = new ArrayList<SelectionListener>(2);

	/**
	 * Constructor.
	 * 
	 * @param container
	 * @param styles
	 */
	public TristateCheckbox( Composite container, int styles )
	{
		super( container, SWT.NONE );

		GridLayout gl = new GridLayout( );
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.marginTop = 0;
		gl.marginBottom = 0;
		setLayout( gl );
		button = new Button( this, SWT.CHECK | styles );
		GridData gd = new GridData( GridData.FILL_BOTH );
		button.setLayoutData( gd );

		button.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event e )
			{
				if ( button.getSelection( ) )
				{
					if ( !button.getGrayed( ) )
					{
						button.setGrayed( true );
					}
				}
				else
				{
					if ( button.getGrayed( ) )
					{
						button.setGrayed( false );
						button.setSelection( true );
					}
				}
			}
		} );
	}

	/**
	 * Set button text.
	 * 
	 * @param text
	 */
	public void setText( String text )
	{
		button.setText( text );
	}

	/**
	 * Returns grayed state of checkbox.
	 * 
	 * @return
	 */
	public boolean getGrayed( )
	{
		return button.getGrayed( );
	}

	/**
	 * Sets grayed state of checkbox.
	 * 
	 * @param grayed
	 */
	public void setGrayed( boolean grayed )
	{
		button.setGrayed( grayed );
	}

	/**
	 * Returns checkbox state, 0 means grayed state, 1 means checked state, 2
	 * means unchecked state.
	 * 
	 * @return
	 */
	public int getSelectionState( )
	{
		if ( button.getGrayed( ) )
		{
			return STATE_GRAYED;
		}
		else if ( button.getSelection( ) )
		{
			return STATE_SELECTED;
		}
		else
		{
			return STATE_UNSELECTED;
		}
	}

	/**
	 * Sets checkbox state.
	 * 
	 * @param state
	 *            the state value, 0 means grayed state, 1 means checked state,
	 *            2 means unchecked state.
	 */
	public void setSelectionState( int state )
	{
		switch ( state )
		{
			case STATE_GRAYED :
				button.setGrayed( true );
				button.setSelection( true );
				break;
			case STATE_SELECTED :
				button.setGrayed( false );
				button.setSelection( true );
				break;
			case STATE_UNSELECTED :
				button.setGrayed( false );
				button.setSelection( false );
				break;
		}
	}

	public boolean isNoSelection( )
	{
		return button.getGrayed( );
	}

	public boolean isChecked( )
	{
		return !isNoSelection( ) && button.getSelection( );
	}

	public boolean isUnchecked( )
	{
		return !isNoSelection( ) && !button.getSelection( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener(int,
	 * org.eclipse.swt.widgets.Listener)
	 */
	@Override
	public void addListener( int eventType, Listener listener )
	{
		super.addListener( eventType, listener );
		button.addListener( eventType, this );
	}
	
	/**
	 * Adds selection listener for button.
	 * 
	 * @param listener
	 */
	public void addSelectionListener( SelectionListener listener )
	{
		selectListenerList.add( listener );
		button.addSelectionListener( this );
	}
	
	/**
	 * Returns actual button object.
	 * 
	 * @return
	 */
	public Button getButton( )
	{
		return button;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget == button )
		{
			event.widget = this;
			Listener[] lis = this.getListeners( event.type );
			for ( int i = ( lis.length - 1 ); i >= 0 ; i-- )
			{
				lis[i].handleEvent( event );
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent event )
	{
		// Do nothing.
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent event )
	{
		if ( event.widget == button )
		{
			Event e = new Event();
			e.detail = event.detail;
			e.data = event.data;
			e.display = event.display;
			e.doit = event.doit;
			e.height = event.height;
			e.item = event.item;
			e.stateMask = event.stateMask;
			e.text = event.text;
			e.time = event.time;
			e.width = event.width;
			e.widget = this;
			e.x = event.x;
			e.y = event.y;
			SelectionEvent se = new SelectionEvent( e );
			
			for ( int i = ( selectListenerList.size( ) - 1 ); i >= 0; i-- )
			{
				selectListenerList.get( i ).widgetSelected( se );
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		button.setEnabled( enabled );
	}
}
