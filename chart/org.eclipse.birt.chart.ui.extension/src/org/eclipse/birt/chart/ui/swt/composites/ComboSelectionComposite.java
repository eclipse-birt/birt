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

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * This composite wraps a combo with specified items.
 */

public class ComboSelectionComposite extends Composite implements
		SelectionListener
{

	protected Label lblSubject;

	protected Combo cmbItems;

	protected String label;

	protected String[] items;

	protected Object[] dataArray;

	private Vector<SelectionListener> vListeners = new Vector<SelectionListener>( );

	/**
	 * @param parent
	 * @param styles
	 * @param label
	 * @param items
	 */
	public ComboSelectionComposite( Composite parent, int styles, String label,
			String[] items )
	{
		this( parent, styles, label, items, items );
	}

	/**
	 * @param parent
	 * @param styles
	 * @param label
	 * @param items
	 * @param dataArray
	 */
	public ComboSelectionComposite( Composite parent, int styles, String label,
			String[] items, Object[] dataArray )
	{
		super( parent, styles );
		this.label = label;
		this.items = items;
		this.dataArray = dataArray;
		createComponents( this );
	}

	protected void createComponents( Composite parent )
	{
		GridLayout gl = new GridLayout( );
		gl.numColumns = 2;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		this.setLayout( gl );
		createLabel( );
		createCombo( );
	}

	protected void createLabel( )
	{
		lblSubject = new Label( this, SWT.NONE );
		lblSubject.setText( label );
	}

	protected void createCombo( )
	{
		cmbItems = new Combo( this, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gd = new GridData( GridData.FILL_BOTH );
		cmbItems.setLayoutData( gd );
		cmbItems.setItems( items );
		cmbItems.addSelectionListener( this );
	}

	public void addSelectionListener( SelectionListener listener )
	{
		vListeners.add( 0, listener );
	}

	public void removeSelctionListener( SelectionListener listener )
	{
		vListeners.remove( listener );
	}

	public void select( int index )
	{
		cmbItems.select( index );
	}

	public int getSelectionIndex( )
	{
		return cmbItems.getSelectionIndex( );
	}

	public Object getSelectedValue( )
	{
		int index = getSelectionIndex( );
		if ( index >= 0 )
		{
			return dataArray[index];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent arg0 )
	{
		// Nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent event )
	{
		event.widget = this;
		for ( SelectionListener l : vListeners )
		{
			l.widgetSelected( event );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		lblSubject.setEnabled( enabled );
		cmbItems.setEnabled( enabled );
	}
}
