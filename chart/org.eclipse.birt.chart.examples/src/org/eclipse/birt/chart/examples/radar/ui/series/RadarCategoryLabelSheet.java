/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.radar.ui.series;

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class RadarCategoryLabelSheet extends AbstractPopupSheet implements
		Listener
{

	private final RadarSeries series;

	private Composite cmpContent = null;

	private Button btnCatLabels = null;

	private Button btnCLFormatSpecifier = null;

	private LabelAttributesComposite catLabelAttr = null;

	public RadarCategoryLabelSheet( String title, ChartWizardContext context,
			boolean needRefresh, RadarSeries series )
	{
		super( title, context, needRefresh );
		this.series = series;
	}

	@Override
	protected Composite getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glMain = new GridLayout( );
			glMain.numColumns = 2;
			cmpContent.setLayout( glMain );
		}

		// Category label configuration
		Group grpLine1b = new Group( cmpContent, SWT.NONE );
		GridLayout glLine1b = new GridLayout( 1, false );
		grpLine1b.setLayout( glLine1b );
		grpLine1b.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		grpLine1b.setText( Messages.getString( "RadarSeriesMarkerSheet.Label.CatLabel" ) ); //$NON-NLS-1$

		btnCatLabels = new Button( grpLine1b, SWT.CHECK );
		{
			btnCatLabels.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.ShowCat" ) ); //$NON-NLS-1$
			if ( series.isSetShowCatLabels( ) )
			{
				btnCatLabels.setSelection( series.isShowCatLabels( ) );
			}
			else
			{
				btnCatLabels.setSelection( true );
			}
			btnCatLabels.addListener( SWT.Selection, this );
			GridData gd = new GridData( GridData.FILL_VERTICAL );
			gd.horizontalSpan = 1;
			btnCatLabels.setLayoutData( gd );
		}

		LabelAttributesContext clattributesContext = new LabelAttributesContext( );
		clattributesContext.isPositionEnabled = false;
		clattributesContext.isFontAlignmentEnabled = false;
		clattributesContext.isVisibilityEnabled = false;
		if ( series.getCatLabel( ) == null )
		{
			org.eclipse.birt.chart.model.component.Label lab = LabelImpl.create( );
			series.setCatLabel( lab );
		}

		catLabelAttr = new LabelAttributesComposite( grpLine1b,
				SWT.NONE,
				getContext( ),
				clattributesContext,
				null,
				null,
				series.getCatLabel( ),
				getChart( ).getUnits( ) );
		GridData cla = new GridData( GridData.FILL_HORIZONTAL );
		catLabelAttr.setLayoutData( cla );
		catLabelAttr.addListener( this );

		btnCLFormatSpecifier = new Button( grpLine1b, SWT.PUSH );
		{
			GridData gdBTNFormatSpecifier = new GridData( );
			gdBTNFormatSpecifier.horizontalIndent = -3;
			btnCLFormatSpecifier.setLayoutData( gdBTNFormatSpecifier );
			btnCLFormatSpecifier.setToolTipText( Messages.getString( "CatLabel.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
			btnCLFormatSpecifier.addListener( SWT.Selection, this );
			btnCLFormatSpecifier.setText( Messages.getString( "Format.Button.Cat.Label" ) ); //$NON-NLS-1$
		}

		if ( series.isSetShowCatLabels( ) )
		{
			catLabelAttr.setEnabled( series.isShowCatLabels( ) );
			btnCLFormatSpecifier.setEnabled( series.isShowCatLabels( ) );
		}
		else
		{
			catLabelAttr.setEnabled( true );
			btnCLFormatSpecifier.setEnabled( true );
		}

		return cmpContent;
	}

	public void handleEvent( Event event )
	{
		if ( event.widget.equals( catLabelAttr ) )
		{
			switch ( event.type )
			{
				case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
					series.getCatLabel( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					series.getCatLabel( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					series.getCatLabel( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					series.getCatLabel( ).setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					series.getCatLabel( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					series.getCatLabel( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					series.getCatLabel( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					series.getCatLabel( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					series.getCatLabel( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					series.getCatLabel( ).setInsets( (Insets) event.data );
					break;
			}
		}
		else if ( event.widget.equals( btnCatLabels ) )
		{
			series.setShowCatLabels( btnCatLabels.getSelection( ) );
			catLabelAttr.setEnabled( series.isShowCatLabels( ) );
			btnCLFormatSpecifier.setEnabled( series.isShowCatLabels( ) );
		}
		else if ( event.widget.equals( btnCLFormatSpecifier ) )
		{

			FormatSpecifier formatspecifier = null;
			if ( series.getCatLabelFormatSpecifier( ) != null )
			{
				formatspecifier = series.getCatLabelFormatSpecifier( );
			}
			FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					formatspecifier,
					new AxisType[]{
							AxisType.LINEAR_LITERAL,
							AxisType.TEXT_LITERAL,
							AxisType.DATE_TIME_LITERAL
					},
					Messages.getString( "CatLabel.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
			if ( editor.open( ) == Window.OK )
			{
				series.setCatLabelFormatSpecifier( editor.getFormatSpecifier( ) );
			}
		}
	}
}
