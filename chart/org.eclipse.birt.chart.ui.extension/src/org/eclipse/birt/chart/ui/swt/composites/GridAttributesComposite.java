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

import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class GridAttributesComposite extends Composite
		implements
			SelectionListener,
			Listener
{

	private transient LineAttributesComposite liacLines = null;

	private transient LineAttributesComposite liacTicks = null;

	private transient Composite cmpContent = null;

	private transient Composite cmpLines = null;

	private transient Label lblStyle = null;

	private transient Combo cmbTickStyle = null;

	private transient Grid grid = null;

	private transient Vector vListeners = null;

	private int orientation;

	private transient boolean bLineGroupEnabled = true;

	private transient ChartWizardContext context;

	// Grid Attribute Change Events
	public static final int LINE_STYLE_CHANGED_EVENT = 1;

	public static final int LINE_WIDTH_CHANGED_EVENT = 2;

	public static final int LINE_COLOR_CHANGED_EVENT = 3;

	public static final int LINE_VISIBILITY_CHANGED_EVENT = 4;

	public static final int TICK_STYLE_CHANGED_EVENT = 5;

	public static final int TICK_COLOR_CHANGED_EVENT = 6;

	public static final int TICK_VISIBILITY_CHANGED_EVENT = 7;

	/**
	 * @param parent
	 * @param style
	 */
	public GridAttributesComposite( Composite parent, int style,
			ChartWizardContext context, Grid grid, int orientation )
	{
		super( parent, style );
		this.orientation = orientation;
		this.context = context;
		init( grid );
		placeComponents( );
	}

	public GridAttributesComposite( Composite parent, int style,
			ChartWizardContext context, Grid grid, boolean bLineGroupEnabled )
	{
		super( parent, style );
		this.bLineGroupEnabled = bLineGroupEnabled;
		this.context = context;
		init( grid );
		placeComponents( );
	}

	/**
	 * 
	 */
	private void init( Grid grid )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
		this.grid = grid;
		this.vListeners = new Vector( );
	}

	/**
	 * 
	 */
	private void placeComponents( )
	{
		// Layout for entire composite
		FillLayout flMain = new FillLayout( );
		flMain.marginHeight = 4;
		flMain.marginWidth = 2;

		// Layout for content composite
		GridLayout glContent = new GridLayout( );
		glContent.verticalSpacing = 5;
		glContent.horizontalSpacing = 5;
		glContent.marginHeight = 0;
		glContent.marginWidth = 0;
		glContent.numColumns = 2;

		// Layout for Ticks group
		GridLayout glTicks = new GridLayout( );
		glTicks.marginHeight = 4;
		glTicks.marginWidth = 4;
		glTicks.verticalSpacing = 2;
		glTicks.horizontalSpacing = 5;
		glTicks.numColumns = 2;

		this.setLayout( flMain );

		// Content Composite
		cmpContent = new Composite( this, SWT.NONE );
		cmpContent.setLayout( glContent );

		// Grid Lines group

		if ( bLineGroupEnabled )
		{
			// Layout for Grid Lines group
			FillLayout flLines = new FillLayout( );
			flLines.marginHeight = 1;
			flLines.marginWidth = 1;

			cmpLines = new Composite( cmpContent, SWT.NONE );
			GridData gdCMPLines = new GridData( GridData.FILL_BOTH );
			gdCMPLines.horizontalSpan = 2;
			cmpLines.setLayoutData( gdCMPLines );
			cmpLines.setLayout( flLines );

			// Line Attributes for Grid Lines
			liacLines = new LineAttributesComposite( cmpLines,
					SWT.NONE,
					context,
					grid.getLineAttributes( ),
					true,
					true,
					true );
			liacLines.addListener( this );
		}

		// Ticks group (unsupported in 3D)
		boolean bTicksEnabled = this.context.getModel( )
				.getDimension( )
				.getValue( ) != ChartDimension.THREE_DIMENSIONAL;

		Group grpTicks = new Group( cmpContent, SWT.NONE );
		{
			GridData gdGRPTicks = new GridData( GridData.FILL_BOTH );
			gdGRPTicks.horizontalSpan = 2;
			grpTicks.setLayoutData( gdGRPTicks );
			grpTicks.setLayout( glTicks );
			grpTicks.setText( Messages.getString( "GridAttributesComposite.Lbl.Ticks" ) ); //$NON-NLS-1$
			grpTicks.setEnabled( bTicksEnabled );
		}

		// Line Attributes for Ticks
		liacTicks = new LineAttributesComposite( grpTicks,
				SWT.NONE,
				context,
				grid.getTickAttributes( ),
				false,
				false,
				true );
		{
			GridData gdLIACTicks = new GridData( GridData.FILL_HORIZONTAL );
			gdLIACTicks.horizontalSpan = 2;
			liacTicks.setLayoutData( gdLIACTicks );
			liacTicks.addListener( this );
			liacTicks.setEnabled( bTicksEnabled );
		}

		// Tick Styles
		lblStyle = new Label( grpTicks, SWT.NONE );
		{
			GridData gdLBLStyle = new GridData( );
			gdLBLStyle.horizontalIndent = 4;
			lblStyle.setLayoutData( gdLBLStyle );
			lblStyle.setText( Messages.getString( "GridAttributesComposite.Lbl.Style" ) ); //$NON-NLS-1$
			lblStyle.setEnabled( bTicksEnabled && grid.getTickAttributes( ).isVisible( ) );
		}

		cmbTickStyle = new Combo( grpTicks, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gdCMBTickStyle = new GridData( GridData.FILL_HORIZONTAL );
			cmbTickStyle.setLayoutData( gdCMBTickStyle );
			cmbTickStyle.addSelectionListener( this );
			cmbTickStyle.setEnabled( bTicksEnabled && grid.getTickAttributes( ).isVisible( ) );
		}

		populateLists( );
		setDefaultSelections( );
	}

	private void populateLists( )
	{
		if ( orientation == Orientation.HORIZONTAL )
		{
			cmbTickStyle.setItems( LiteralHelper.horizontalTickStyleSet.getDisplayNames( ) );
		}
		else if ( orientation == Orientation.VERTICAL )
		{
			cmbTickStyle.setItems( LiteralHelper.verticalTickStyleSet.getDisplayNames( ) );
		}
	}

	private void setDefaultSelections( )
	{
		if ( grid.getTickStyle( ) == null )
		{
			cmbTickStyle.select( 0 );
			return;
		}

		if ( orientation == Orientation.HORIZONTAL )
		{
			cmbTickStyle.select( LiteralHelper.horizontalTickStyleSet.getSafeNameIndex( grid.getTickStyle( )
					.getName( ) ) );
		}
		else if ( orientation == Orientation.VERTICAL )
		{
			cmbTickStyle.select( LiteralHelper.verticalTickStyleSet.getSafeNameIndex( grid.getTickStyle( )
					.getName( ) ) );
		}
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( cmbTickStyle ) )
		{
			Event eGrid = new Event( );
			eGrid.widget = this;
			eGrid.type = TICK_STYLE_CHANGED_EVENT;
			TickStyle tsGrid = TickStyle.getByName( LiteralHelper.fullTickStyleSet.getNameByDisplayName( cmbTickStyle.getText( ) ) );
			eGrid.data = tsGrid;
			fireEvent( eGrid );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	public Point getPreferredSize( )
	{
		return new Point( 230, 240 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		Event eGrid = new Event( );
		eGrid.widget = this;
		if ( event.widget.equals( liacLines ) )
		{
			if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				eGrid.type = LINE_STYLE_CHANGED_EVENT;
				eGrid.data = event.data;
				// grid.getStyle().setStyle((LineStyle) event.data);
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				eGrid.type = LINE_WIDTH_CHANGED_EVENT;
				eGrid.data = event.data;
				// grid.getStyle().setThickness(((Integer)
				// event.data).intValue());
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				eGrid.type = LINE_COLOR_CHANGED_EVENT;
				eGrid.data = event.data;
				// grid.getStyle().setColor((ColorDefinition) event.data);
			}
			else
			{
				eGrid.type = LINE_VISIBILITY_CHANGED_EVENT;
				eGrid.data = event.data;
				// grid.getStyle().setVisible(((Boolean)
				// event.data).booleanValue());
			}
		}
		else if ( event.widget.equals( liacTicks ) )
		{
			if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				eGrid.type = TICK_COLOR_CHANGED_EVENT;
				eGrid.data = event.data;
				grid.getTickAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
			else
			{
				eGrid.type = TICK_VISIBILITY_CHANGED_EVENT;
				eGrid.data = event.data;
				grid.getTickAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
				lblStyle.setEnabled( ( (Boolean) event.data ).booleanValue( ) );
				cmbTickStyle.setEnabled( ( (Boolean) event.data ).booleanValue( ) );
			}
		}
		else
		{
			// Unknown event
			return;
		}
		fireEvent( eGrid );
	}

	private void fireEvent( Event event )
	{
		for ( int i = 0; i < vListeners.size( ); i++ )
		{
			( (Listener) vListeners.get( i ) ).handleEvent( event );
		}
	}
}