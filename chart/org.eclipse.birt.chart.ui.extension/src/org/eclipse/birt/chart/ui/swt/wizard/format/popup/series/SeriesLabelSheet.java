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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class SeriesLabelSheet extends AbstractPopupSheet implements Listener
{

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacTitle = null;

	private transient LabelAttributesComposite lacLabel = null;

	private transient SeriesDefinition seriesDefn = null;

	public SeriesLabelSheet( Composite parent, Chart chart,
			SeriesDefinition seriesDefn )
	{
		super( parent, chart, false );
		this.seriesDefn = seriesDefn;
		cmpTop = getComponent( parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getComponent( Composite parent )
	{
		// Layout for the content composite
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		// The axis from the model for convenient access
		Series series = null;
		try
		{
			series = getSeriesForProcessing( );
		}
		catch ( ClassCastException cce )
		{
			cce.printStackTrace( );
		}

		lacLabel = new LabelAttributesComposite( cmpContent,
				SWT.NONE,
				Messages.getString( "OrthogonalSeriesLabelAttributeSheetImpl.Lbl.Label" ), series.getLabelPosition( ), series //$NON-NLS-1$
						.getLabel( ),
				chart.getUnits( ),
				true,
				true,
				serviceprovider,
				( series instanceof PieSeries || series instanceof BarSeries )
						? LabelAttributesComposite.ALLOW_INOUT_POSITION
						: ( LabelAttributesComposite.ALLOW_HORIZONTAL_POSITION | LabelAttributesComposite.ALLOW_VERTICAL_POSITION ) );
		GridData gdLACLabel = new GridData( GridData.FILL_HORIZONTAL );
		gdLACLabel.widthHint = 200;
		lacLabel.setLayoutData( gdLACLabel );
		lacLabel.addListener( this );
		// StockSeries don't draw the label
		lacLabel.setEnabled( !( series instanceof StockSeries ) );

		if ( series instanceof PieSeries )
		{
			lacTitle = new LabelAttributesComposite( cmpContent,
					SWT.NONE,
					Messages.getString( "OrthogonalSeriesLabelAttributeSheetImpl.Lbl.Title" ), ( (PieSeries) series ) //$NON-NLS-1$
							.getTitlePosition( ),
					( (PieSeries) series ).getTitle( ),
					chart.getUnits( ),
					true,
					true,
					serviceprovider,
					LabelAttributesComposite.ALLOW_HORIZONTAL_POSITION
							| LabelAttributesComposite.ALLOW_VERTICAL_POSITION );
			GridData gdLACTitle = new GridData( GridData.FILL_HORIZONTAL );
			gdLACTitle.widthHint = 200;
			lacTitle.setLayoutData( gdLACTitle );
			lacTitle.addListener( this );
		}

		return cmpContent;
	}

	private Series getSeriesForProcessing( )
	{
		return seriesDefn.getDesignTimeSeries( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( lacTitle ) )
		{
			switch ( event.type )
			{
				case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.POSITION_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).setTitlePosition( (Position) event.data );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					( (PieSeries) getSeriesForProcessing( ) ).getTitle( )
							.setInsets( (Insets) event.data );
					break;
			}
		}
		else if ( event.widget.equals( lacLabel ) )
		{
			switch ( event.type )
			{
				case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.POSITION_CHANGED_EVENT :
					getSeriesForProcessing( ).setLabelPosition( (Position) event.data );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					getSeriesForProcessing( ).getLabel( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.setInsets( (Insets) event.data );
					break;
			}
		}
	}
}