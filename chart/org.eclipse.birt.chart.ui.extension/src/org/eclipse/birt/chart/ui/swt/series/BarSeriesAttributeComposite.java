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

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class BarSeriesAttributeComposite extends Composite
		implements
			SelectionListener,
			Listener
{

	Composite cmpContent = null;

	Combo cmbRiserTypes = null;

	FillChooserComposite fccRiserOutline = null;

	Series series = null;

	ChartWizardContext context;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public BarSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext context, Series series )
	{
		super( parent, style );
		if ( !( series instanceof BarSeries ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"BarSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = series;
		this.context = context;
		init( );
		placeComponents( );
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_YSERIES_BAR );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		// Layout for content composite
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.marginHeight = 2;
		glContent.marginWidth = 2;

		// Main content composite
		this.setLayout( glContent );

		// Riser Type
		Label lblRiserType = new Label( this, SWT.NONE );
		GridData gdLBLRiserType = new GridData( );
		lblRiserType.setLayoutData( gdLBLRiserType );
		lblRiserType.setText( Messages.getString( "BarSeriesAttributeComposite.Lbl.RiserType" ) ); //$NON-NLS-1$

		this.cmbRiserTypes = new Combo( this, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBRiserTypes = new GridData( GridData.FILL_HORIZONTAL );
		cmbRiserTypes.setLayoutData( gdCMBRiserTypes );
		cmbRiserTypes.addSelectionListener( this );

		// Riser Outline
		Label lblRiserOutline = new Label( this, SWT.NONE );
		GridData gdLBLRiserOutline = new GridData( );
		lblRiserOutline.setLayoutData( gdLBLRiserOutline );
		lblRiserOutline.setText( Messages.getString( "BarSeriesAttributeComposite.Lbl.RiserOutline" ) ); //$NON-NLS-1$

		this.fccRiserOutline = new FillChooserComposite( this,
				SWT.NONE,
				context,
				( (BarSeries) series ).getRiserOutline( ),
				false,
				false,
				true,
				true );
		GridData gdFCCRiserOutline = new GridData( GridData.FILL_HORIZONTAL );
		fccRiserOutline.setLayoutData( gdFCCRiserOutline );
		fccRiserOutline.addListener( this );

		populateLists( );
	}

	private void populateLists( )
	{
		NameSet ns = LiteralHelper.riserTypeSet;
		cmbRiserTypes.setItems( ns.getDisplayNames( ) );
		cmbRiserTypes.select( ns.getSafeNameIndex( ( (BarSeries) series ).getRiser( )
				.getName( ) ) );
	}

	public Point getPreferredSize( )
	{
		return new Point( 400, 200 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( cmbRiserTypes ) )
		{
			( (BarSeries) series ).setRiser( RiserType.getByName( LiteralHelper.riserTypeSet.getNameByDisplayName( cmbRiserTypes.getText( ) ) ) );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( fccRiserOutline ) )
		{
			if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
			{
				( (BarSeries) series ).setRiserOutline( (ColorDefinition) event.data );
			}
		}
	}
}