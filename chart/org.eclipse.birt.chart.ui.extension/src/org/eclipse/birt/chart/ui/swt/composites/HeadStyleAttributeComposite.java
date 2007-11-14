/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Contains HeadStyleChooser
 * 
 */
public class HeadStyleAttributeComposite extends Composite implements
		SelectionListener,
		Listener
{

	private transient Composite cmpContent = null;

	private transient Label lblHeadStyle = null;

	private transient HeadStyleChooserComposite cmbHeadStyle = null;

	private transient LineDecorator laCurrent = null;

	private transient Vector vListeners = null;

	public static final int STYLE_CHANGED_EVENT = 1;

	/**
	 * @param parent
	 * @param style
	 */
	public HeadStyleAttributeComposite( Composite parent, int style,
			LineDecorator laCurrent )
	{
		super( parent, style );
		this.laCurrent = laCurrent;
		init( );
		placeComponents( );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
		vListeners = new Vector( );
	}

	private void placeComponents( )
	{
		FillLayout flMain = new FillLayout( );
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;

		GridLayout glContent = new GridLayout( );
		glContent.verticalSpacing = 5;
		glContent.horizontalSpacing = 5;
		glContent.marginHeight = 4;
		glContent.marginWidth = 4;
		glContent.numColumns = 6;

		this.setLayout( flMain );

		cmpContent = new Composite( this, SWT.NONE );
		cmpContent.setLayout( glContent );

		lblHeadStyle = new Label( cmpContent, SWT.NONE );
		GridData gdLHeadStyle = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
		lblHeadStyle.setLayoutData( gdLHeadStyle );
		lblHeadStyle.setText( Messages.getString( "HeadStyleAttributeComposite.Lbl.HeadStyle" ) ); //$NON-NLS-1$

		cmbHeadStyle = new HeadStyleChooserComposite( cmpContent, SWT.DROP_DOWN
				| SWT.READ_ONLY, laCurrent.getValue( ) );
		GridData gdCBHeadStyle = new GridData( GridData.FILL_HORIZONTAL );
		gdCBHeadStyle.horizontalSpan = 5;
		cmbHeadStyle.setLayoutData( gdCBHeadStyle );
		cmbHeadStyle.addListener( HeadStyleChooserComposite.SELECTION_EVENT,
				this );
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	public void setLineDecorator( LineDecorator attributes )
	{
		laCurrent = attributes;

		if ( laCurrent == null )
		{
			cmbHeadStyle.setHeadStyle( LineDecorator.ARROW );
		}
		else
		{
			cmbHeadStyle.setHeadStyle( attributes.getValue( ) );
		}
		redraw( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	private void fireValueChangedEvent( int iEventType, Object data )
	{
		for ( int iL = 0; iL < vListeners.size( ); iL++ )
		{
			Event se = new Event( );
			se.widget = this;
			se.data = data;
			se.type = iEventType;
			( (Listener) vListeners.get( iL ) ).handleEvent( se );
		}
	}

	private LineDecorator getModelHeadStyle( int iStyle )
	{
		switch ( iStyle )
		{
			case 0 :
				return LineDecorator.ARROW_LITERAL;
			case 1 :
				return LineDecorator.CIRCLE_LITERAL;
			case 2 :
				return LineDecorator.NONE_LITERAL;
			default :
				return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( cmbHeadStyle != null && cmbHeadStyle.equals( event.widget ) )
		{
			fireValueChangedEvent( HeadStyleAttributeComposite.STYLE_CHANGED_EVENT,
					getModelHeadStyle( cmbHeadStyle.getHeadStyle( ) ) );
		}
	}
}
