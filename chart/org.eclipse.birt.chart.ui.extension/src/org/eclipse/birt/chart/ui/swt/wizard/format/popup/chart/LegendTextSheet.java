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
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
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

public class LegendTextSheet extends AbstractPopupSheet implements Listener
{

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacTitle = null;

	private transient FontDefinitionComposite fdcFont = null;

	private transient LineAttributesComposite lineSeparator;

	private transient FillChooserComposite fccShadow;

	private transient LineAttributesComposite outlineText;

	private transient InsetsComposite icText;

	public LegendTextSheet( String title, ChartWizardContext context )
	{
		super( title, context, true );
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_TEXT_FORMAT );
		
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
				getChart( ).getUnits( ),
				true,
				false,
				getContext( ),
				LabelAttributesComposite.ALLOW_VERTICAL_POSITION
						| LabelAttributesComposite.ALLOW_HORIZONTAL_POSITION,
				false );
		{
			GridData gdLACTitle = new GridData( GridData.FILL_BOTH );
			gdLACTitle.verticalSpan = 2;
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
				getContext( ),
				getLegend( ).getText( ).getFont( ),
				getLegend( ).getText( ).getColor( ),
				false );
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
				getContext( ),
				getLegend( ).getClientArea( ).getShadowColor( ),
				false,
				false );
		GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
		fccShadow.setLayoutData( gdFCCShadow );
		fccShadow.setEnabled( getLegend( ).isVisible( ) );
		fccShadow.addListener( this );

		Group grpOutline = new Group( grpTxtArea, SWT.NONE );
		GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPOutline.horizontalSpan = 2;
		grpOutline.setLayoutData( gdGRPOutline );
		grpOutline.setLayout( new FillLayout( ) );
		grpOutline.setText( Messages.getString( "MoreOptionsChartLegendSheet.Label.Outline" ) ); //$NON-NLS-1$

		outlineText = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getLegend( ).getClientArea( ).getOutline( ),
				true,
				true,
				true );
		outlineText.addListener( this );
		outlineText.setEnabled( true );

		icText = new InsetsComposite( grpTxtArea,
				SWT.NONE,
				getLegend( ).getClientArea( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		GridData gdInsets = new GridData( GridData.FILL_HORIZONTAL );
		gdInsets.horizontalSpan = 2;
		icText.setLayoutData( gdInsets );
		icText.addListener( this );

		Group grpSeparator = new Group( cmpContent, SWT.NONE );
		{
			GridLayout layout = new GridLayout( );
			layout.marginHeight = 0;
			layout.marginWidth = 5;
			grpSeparator.setLayout( layout );
			grpSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			grpSeparator.setText( Messages.getString( "LegendTextSheet.Label.Separator" ) ); //$NON-NLS-1$
		}

		lineSeparator = new LineAttributesComposite( grpSeparator,
				SWT.NONE,
				getContext( ),
				getLegend( ).getSeparator( ),
				true,
				true,
				true );
		{
			lineSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			lineSeparator.addListener( this );
			lineSeparator.setEnabled( true );
		}

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
		else if ( event.widget.equals( lineSeparator ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getLegend( ).getSeparator( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getLegend( ).getSeparator( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getLegend( ).getSeparator( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getLegend( ).getSeparator( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
	}

	private Legend getLegend( )
	{
		return getChart( ).getLegend( );
	}

}
