/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.InsetsComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class LegendTextSheet extends AbstractPopupSheet
		implements
			SelectionListener,
			Listener
{

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacTitle = null;

	private transient FontDefinitionComposite fdcFont = null;

	private transient FillChooserComposite fccShadow;

	private transient LineAttributesComposite outlineText;

	private transient InsetsComposite icText;

	public LegendTextSheet( Composite parent, ChartWizardContext context )
	{
		super( parent, context, true );
		cmpTop = getComponent( parent );
	}

	protected Composite getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glMain = new GridLayout( 2, false );
			glMain.horizontalSpacing = 5;
			glMain.verticalSpacing = 5;
			glMain.marginHeight = 7;
			glMain.marginWidth = 7;
			cmpContent.setLayout( glMain );
		}

		lacTitle = new LabelAttributesComposite( cmpContent,
				SWT.NONE,
				Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Title" ),//$NON-NLS-1$
				getLegend( ).getTitlePosition( ),
				getLegend( ).getTitle( ),
				chart.getUnits( ),
				true,
				false,
				serviceprovider,
				LabelAttributesComposite.ALLOW_ALL_POSITION );
		{
			GridData gdLACTitle = new GridData( GridData.FILL_BOTH );
			lacTitle.setLayoutData( gdLACTitle );
			lacTitle.addListener( this );
			lacTitle.setEnabled( getLegend( ).getTitle( ).isVisible( ) );
		}

		Group grpTxtArea = new Group( cmpContent, SWT.NONE );
		{
			GridLayout layout = new GridLayout( 2, false );
			layout.marginHeight = 7;
			layout.marginWidth = 7;
			grpTxtArea.setLayout( layout );
			grpTxtArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			grpTxtArea.setText( Messages.getString( "MoreOptionsChartLegendSheet.Label.TextArea" ) ); //$NON-NLS-1$
		}

		new Label( grpTxtArea, SWT.NONE ).setText( Messages.getString( "LegendTextSheet.Label.Font" ) ); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite( grpTxtArea,
				SWT.NONE,
				getLegend( ).getText( ).getFont( ),
				getLegend( ).getText( ).getColor( ) );
		GridData gdFDCFont = new GridData( GridData.FILL_HORIZONTAL );
		gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
		gdFDCFont.grabExcessVerticalSpace = false;
		fdcFont.setLayoutData( gdFDCFont );
		fdcFont.addListener( this );

		Label lblShadow = new Label( grpTxtArea, SWT.NONE );
		GridData gdLBLShadow = new GridData( );
		lblShadow.setLayoutData( gdLBLShadow );
		lblShadow.setText( Messages.getString( "ClientAreaAttributeComposite.Lbl.Shadow" ) ); //$NON-NLS-1$

		fccShadow = new FillChooserComposite( grpTxtArea,
				SWT.NONE,
				getLegend( ).getClientArea( ).getShadowColor( ),
				false,
				false );
		GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
		fccShadow.setLayoutData( gdFCCShadow );
		fccShadow.setEnabled( getLegend( ).isVisible( ) );
		fccShadow.addListener( this );

		new Label( grpTxtArea, SWT.NONE ).setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Group grpOutline = new Group( grpTxtArea, SWT.NONE );
		GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPOutline.horizontalSpan = 2;
		grpOutline.setLayoutData( gdGRPOutline );
		grpOutline.setLayout( new FillLayout( ) );
		grpOutline.setText( Messages.getString( "MoreOptionsChartLegendSheet.Label.Outline" ) ); //$NON-NLS-1$

		outlineText = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getLegend( ).getClientArea( ).getOutline( ),
				true,
				true,
				true );
		outlineText.addListener( this );

		icText = new InsetsComposite( grpTxtArea,
				SWT.NONE,
				getLegend( ).getClientArea( ).getInsets( ),
				chart.getUnits( ),
				serviceprovider );
		GridData gdInsets = new GridData( GridData.FILL_HORIZONTAL );
		gdInsets.horizontalSpan = 2;
		icText.setLayoutData( gdInsets );
		icText.addListener( this );

		return cmpContent;
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
					getLegend( ).getTitle( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.POSITION_CHANGED_EVENT :
					getLegend( ).setTitlePosition( (Position) event.data );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					getLegend( ).getTitle( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					getLegend( ).getTitle( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					getLegend( ).getTitle( ).setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					getLegend( ).getTitle( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					getLegend( ).getTitle( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					getLegend( ).getTitle( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getLegend( ).getTitle( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					getLegend( ).getTitle( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getLegend( ).getTitle( ).setInsets( (Insets) event.data );
					break;
			}
		}
		else if ( event.widget.equals( fdcFont ) )
		{
			getLegend( ).getText( )
					.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
			getLegend( ).getText( )
					.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			getLegend( ).getClientArea( )
					.setShadowColor( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( icText ) )
		{
			getLegend( ).getClientArea( ).setInsets( (Insets) event.data );
		}
		else if ( event.widget.equals( outlineText ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getLegend( ).getClientArea( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getLegend( ).getClientArea( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getLegend( ).getClientArea( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getLegend( ).getClientArea( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
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
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	private Legend getLegend( )
	{
		return chart.getLegend( );
	}

}
