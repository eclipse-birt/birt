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

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class GanttLineAttributesComposite extends Composite implements
		SelectionListener,
		Listener
{

	private transient Composite cmpContent = null;

	private transient Label lblStyle = null;

	private transient Label lblWidth = null;

	private transient Label lblColor = null;

	private transient LineStyleChooserComposite cmbStyle = null;

	private transient IntegerSpinControl iscWidth = null;

	private transient FillChooserComposite cmbColor = null;

	private transient Button cbVisible = null;

	private transient LineAttributes laCurrent = null;

	private transient boolean bEnableWidths = true;

	private transient boolean bEnableStyles = true;

	private transient boolean bEnableVisibility = true;

	private transient Vector vListeners = null;

	public static final int STYLE_CHANGED_EVENT = 1;

	public static final int WIDTH_CHANGED_EVENT = 2;

	public static final int COLOR_CHANGED_EVENT = 3;

	public static final int VISIBILITY_CHANGED_EVENT = 4;

	private transient boolean bEnabled = true;

	private transient boolean bEnableColor = true;

	private transient ChartWizardContext context;

	/**
	 * @param parent
	 * @param style
	 */
	public GanttLineAttributesComposite( Composite parent,
			ChartWizardContext context, int style, LineAttributes laCurrent,
			boolean bEnableWidths, boolean bEnableStyles,
			boolean bEnableVisibility )
	{
		super( parent, style );
		this.context = context;
		this.laCurrent = laCurrent;
		if ( laCurrent == null )
		{
			// Create a default line attributes instance
			this.laCurrent = AttributeFactory.eINSTANCE.createLineAttributes( );
		}
		this.bEnableStyles = bEnableStyles;
		this.bEnableWidths = bEnableWidths;
		this.bEnableVisibility = bEnableVisibility;
		init( );
		placeComponents( );
	}

	public GanttLineAttributesComposite( Composite parent, int style,
			LineAttributes laCurrent, boolean bEnableWidths,
			boolean bEnableStyles, boolean bEnableVisibility,
			boolean bEnableColor )
	{
		super( parent, style );
		this.laCurrent = laCurrent;
		if ( laCurrent == null )
		{
			// Create a default line attributes instance
			this.laCurrent = AttributeFactory.eINSTANCE.createLineAttributes( );
		}
		this.bEnableStyles = bEnableStyles;
		this.bEnableWidths = bEnableWidths;
		this.bEnableVisibility = bEnableVisibility;
		this.bEnableColor = bEnableColor;
		init( );
		placeComponents( );
	}

	/**
	 * 
	 */
	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
		vListeners = new Vector( );
	}

	/**
	 * 
	 */
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

		bEnabled = laCurrent.isVisible( );
		boolean bEnableUI = bEnabled;
		if ( bEnableVisibility )
		{
			cbVisible = new Button( cmpContent, SWT.CHECK );
			GridData gdCBVisible = new GridData( GridData.FILL_HORIZONTAL );
			gdCBVisible.horizontalSpan = 6;
			cbVisible.setLayoutData( gdCBVisible );
			cbVisible.setText( Messages.getString( "LineAttributesComposite.Lbl.IsVisible" ) ); //$NON-NLS-1$
			cbVisible.setSelection( laCurrent.isVisible( ) );
			cbVisible.addSelectionListener( this );
			if ( bEnabled )
			{
				bEnableUI = cbVisible.getSelection( );
			}
		}

		if ( bEnableStyles )
		{
			lblStyle = new Label( cmpContent, SWT.NONE );
			GridData gdLStyle = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
			lblStyle.setLayoutData( gdLStyle );
			lblStyle.setText( Messages.getString( "LineAttributesComposite.Lbl.Style" ) ); //$NON-NLS-1$
			lblStyle.setEnabled( bEnableUI );

			cmbStyle = new LineStyleChooserComposite( cmpContent, SWT.DROP_DOWN
					| SWT.READ_ONLY, getSWTLineStyle( laCurrent.getStyle( ) ) );
			GridData gdCBStyle = new GridData( GridData.FILL_HORIZONTAL );
			gdCBStyle.horizontalSpan = 5;
			cmbStyle.setLayoutData( gdCBStyle );
			cmbStyle.addListener( LineStyleChooserComposite.SELECTION_EVENT,
					this );
			cmbStyle.setEnabled( bEnableUI );
		}

		if ( bEnableWidths )
		{
			lblWidth = new Label( cmpContent, SWT.NONE );
			GridData gdLWidth = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
			lblWidth.setLayoutData( gdLWidth );
			lblWidth.setText( Messages.getString( "LineAttributesComposite.Lbl.Width" ) ); //$NON-NLS-1$
			lblWidth.setEnabled( bEnableUI );

			iscWidth = new IntegerSpinControl( cmpContent,
					SWT.NONE,
					laCurrent.getThickness( ) );
			GridData gdISCWidth = new GridData( GridData.FILL_HORIZONTAL );
			gdISCWidth.horizontalSpan = 5;
			iscWidth.setLayoutData( gdISCWidth );
			iscWidth.setMinimum( 1 );
			iscWidth.setMaximum( 100 );
			iscWidth.addListener( this );
			iscWidth.setEnabled( bEnableUI );
		}

		if ( bEnableColor )
		{
			lblColor = new Label( cmpContent, SWT.NONE );
			GridData gdLColor = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
			lblColor.setLayoutData( gdLColor );
			lblColor.setText( Messages.getString( "LineAttributesComposite.Lbl.Color" ) ); //$NON-NLS-1$
			lblColor.setEnabled( bEnableUI );

			cmbColor = new FillChooserComposite( cmpContent,
					SWT.DROP_DOWN | SWT.READ_ONLY,
					context,
					this.laCurrent.getColor( ),
					false,
					false );
			GridData gdCBColor = new GridData( GridData.FILL_HORIZONTAL );
			gdCBColor.horizontalSpan = 5;
			cmbColor.setLayoutData( gdCBColor );
			cmbColor.addListener( this );
			cmbColor.setEnabled( bEnableUI );
		}
	}

	public Point getPreferredSize( )
	{
		Point ptSize = new Point( 250, 40 );
		if ( bEnableVisibility )
		{
			ptSize.y += 30;
		}
		if ( bEnableStyles )
		{
			ptSize.y += 30;
		}
		if ( bEnableWidths )
		{
			ptSize.y += 30;
		}
		return ptSize;
	}

	public void setEnabled( boolean bState )
	{
		boolean bEnableUI = true;
		if ( this.bEnableVisibility )
		{
			cbVisible.setEnabled( bState );
			bEnableUI = cbVisible.getSelection( );
		}
		if ( this.bEnableStyles )
		{
			lblStyle.setEnabled( bState & bEnableUI );
			cmbStyle.setEnabled( bState & bEnableUI );
		}
		if ( this.bEnableWidths )
		{
			lblWidth.setEnabled( bState & bEnableUI );
			iscWidth.setEnabled( bState & bEnableUI );
		}
		if ( this.bEnableColor )
		{
			lblColor.setEnabled( bState & bEnableUI );
			cmbColor.setEnabled( bState & bEnableUI );
		}
		this.bEnabled = bState;
	}

	public boolean isEnabled( )
	{
		return this.bEnabled;
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	public void setLineAttributes( LineAttributes attributes )
	{
		laCurrent = attributes;
		if ( bEnableVisibility )
		{
			if ( laCurrent == null )
			{
				cbVisible.setSelection( false );
			}
			else
			{
				cbVisible.setSelection( attributes.isVisible( ) );
			}
			boolean bUIEnabled = cbVisible.getSelection( );
			if ( bEnableStyles )
			{
				cmbStyle.setEnabled( bUIEnabled );
				lblStyle.setEnabled( bUIEnabled );
			}
			if ( bEnableWidths )
			{
				iscWidth.setEnabled( bUIEnabled );
				lblWidth.setEnabled( bUIEnabled );
			}
			if ( bEnableColor )
			{
				cmbColor.setEnabled( bUIEnabled );
				lblColor.setEnabled( bUIEnabled );
			}
		}
		if ( bEnableStyles )
		{
			if ( laCurrent == null )
			{
				cmbStyle.setLineStyle( getSWTLineStyle( LineStyle.SOLID_LITERAL ) );
			}
			else
			{
				cmbStyle.setLineStyle( getSWTLineStyle( attributes.getStyle( ) ) );
			}
		}
		if ( this.bEnableWidths )
		{
			if ( laCurrent == null )
			{
				iscWidth.setValue( 1 );
			}
			else
			{
				iscWidth.setValue( attributes.getThickness( ) );
			}
		}
		if ( this.bEnableColor )
		{
			if ( laCurrent == null )
			{
				cmbColor.setFill( null );
			}
			else
			{
				cmbColor.setFill( attributes.getColor( ) );
			}
			cmbColor.redraw( );
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
		Object oSource = e.getSource( );
		if ( oSource.equals( cbVisible ) )
		{
			// Notify Listeners that a change has occurred in the value
			fireValueChangedEvent( GanttLineAttributesComposite.VISIBILITY_CHANGED_EVENT,
					Boolean.valueOf( cbVisible.getSelection( ) ) );
			// Notification may cause this class disposed
			if ( isDisposed( ) )
			{
				return;
			}
			// Enable/Disable UI Elements
			boolean bEnableUI = cbVisible.getSelection( );
			if ( bEnableStyles )
			{
				lblStyle.setEnabled( bEnableUI );
				cmbStyle.setEnabled( bEnableUI );
			}
			if ( bEnableWidths )
			{
				lblWidth.setEnabled( bEnableUI );
				iscWidth.setEnabled( bEnableUI );
			}
			if ( bEnableColor )
			{
				lblColor.setEnabled( bEnableUI );
				cmbColor.setEnabled( bEnableUI );
			}
		}
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

	/**
	 * Converts the specified SWT line style constant to a chart model's
	 * LineStyle object
	 */
	private LineStyle getModelLineStyle( int iStyle )
	{
		switch ( iStyle )
		{
			case SWT.LINE_SOLID :
				return LineStyle.SOLID_LITERAL;
			case SWT.LINE_DASH :
				return LineStyle.DASHED_LITERAL;
			case SWT.LINE_DASHDOT :
				return LineStyle.DASH_DOTTED_LITERAL;
			case SWT.LINE_DOT :
				return LineStyle.DOTTED_LITERAL;
			default :
				return null;
		}
	}

	/**
	 * Converts the specified model line style to an appropriate SWT line style
	 * constant
	 */
	private int getSWTLineStyle( LineStyle style )
	{
		if ( style.equals( LineStyle.DASHED_LITERAL ) )
		{
			return SWT.LINE_DASH;
		}
		else if ( style.equals( LineStyle.DASH_DOTTED_LITERAL ) )
		{
			return SWT.LINE_DASHDOT;
		}
		else if ( style.equals( LineStyle.DOTTED_LITERAL ) )
		{
			return SWT.LINE_DOT;
		}
		else
		{
			return SWT.LINE_SOLID;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( cmbColor != null && cmbColor.equals( event.widget ) )
		{
			fireValueChangedEvent( GanttLineAttributesComposite.COLOR_CHANGED_EVENT,
					cmbColor.getFill( ) );
		}
		else if ( cmbStyle != null && cmbStyle.equals( event.widget ) )
		{
			fireValueChangedEvent( GanttLineAttributesComposite.STYLE_CHANGED_EVENT,
					getModelLineStyle( cmbStyle.getLineStyle( ) ) );
		}
		else if ( iscWidth != null && iscWidth.equals( event.widget ) )
		{
			fireValueChangedEvent( GanttLineAttributesComposite.WIDTH_CHANGED_EVENT,
					Integer.valueOf( iscWidth.getValue( ) ) );
		}
	}
}