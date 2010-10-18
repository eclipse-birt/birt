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

public class RadarWebLabelSheet extends AbstractPopupSheet implements Listener
{

	private final RadarSeries series;

	private Composite cmpContent = null;

	private Button btnWebLabels = null;

	private LabelAttributesComposite webLabelAttr = null;

	private Button btnWLFormatSpecifier = null;

	public RadarWebLabelSheet( String title, ChartWizardContext context,
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

		Group grpLine1a = new Group( cmpContent, SWT.NONE );
		GridLayout glLine1a = new GridLayout( 1, false );
		grpLine1a.setLayout( glLine1a );
		grpLine1a.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		grpLine1a.setText( Messages.getString( "RadarSeriesMarkerSheet.Label.WebLabel" ) ); //$NON-NLS-1$

		btnWebLabels = new Button( grpLine1a, SWT.CHECK );
		{
			btnWebLabels.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.ShowWeb" ) ); //$NON-NLS-1$
			btnWebLabels.setSelection( series.isShowWebLabels( ) );
			btnWebLabels.addListener( SWT.Selection, this );
			GridData gd = new GridData( GridData.FILL_VERTICAL );
			gd.horizontalSpan = 1;
			btnWebLabels.setLayoutData( gd );
		}
		// Web Label Configuration
		LabelAttributesContext attributesContext = new LabelAttributesContext( );
		attributesContext.isPositionEnabled = false;
		attributesContext.isFontAlignmentEnabled = false;
		attributesContext.isVisibilityEnabled = false;
		if ( series.getWebLabel( ) == null )
		{
			org.eclipse.birt.chart.model.component.Label lab = LabelImpl.create( );
			series.setWebLabel( lab );
		}

		webLabelAttr = new LabelAttributesComposite( grpLine1a,
				SWT.NONE,
				getContext( ),
				attributesContext,
				null,
				null,
				series.getWebLabel( ),
				getChart( ).getUnits( ) );
		webLabelAttr.setEnabled( series.isShowWebLabels( ) );
		GridData wla = new GridData( GridData.FILL_HORIZONTAL );
		webLabelAttr.setLayoutData( wla );
		webLabelAttr.addListener( this );

		btnWLFormatSpecifier = new Button( grpLine1a, SWT.PUSH );
		{
			GridData gdBTNFormatSpecifier = new GridData( );
			gdBTNFormatSpecifier.horizontalIndent = -3;
			btnWLFormatSpecifier.setLayoutData( gdBTNFormatSpecifier );
			btnWLFormatSpecifier.setToolTipText( Messages.getString( "WebLabel.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
			btnWLFormatSpecifier.addListener( SWT.Selection, this );
			btnWLFormatSpecifier.setText( Messages.getString( "Format.Button.Web.Label" ) ); //$NON-NLS-1$
		}

		webLabelAttr.setEnabled( series.isShowWebLabels( ) );
		btnWLFormatSpecifier.setEnabled( series.isShowWebLabels( ) );

		return cmpContent;
	}

	public void handleEvent( Event event )
	{
		if ( event.widget.equals( webLabelAttr ) )
		{
			switch ( event.type )
			{
				case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
					series.getWebLabel( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					series.getWebLabel( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					series.getWebLabel( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					series.getWebLabel( ).setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					series.getWebLabel( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					series.getWebLabel( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					series.getWebLabel( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					series.getWebLabel( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					series.getWebLabel( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					series.getWebLabel( ).setInsets( (Insets) event.data );
					break;
			}
		}
		else if ( event.widget.equals( btnWebLabels ) )
		{
			series.setShowWebLabels( btnWebLabels.getSelection( ) );
			webLabelAttr.setEnabled( series.isShowWebLabels( ) );
			btnWLFormatSpecifier.setEnabled( series.isShowWebLabels( ) );
		}
		else if ( event.widget.equals( btnWLFormatSpecifier ) )
		{

			FormatSpecifier formatspecifier = null;
			if ( series.getWebLabelFormatSpecifier( ) != null )
			{
				formatspecifier = series.getWebLabelFormatSpecifier( );
			}
			FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					formatspecifier,
					AxisType.LINEAR_LITERAL,
					Messages.getString( "WebLabel.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
			if ( editor.open( ) == Window.OK )
			{
				series.setWebLabelFormatSpecifier( editor.getFormatSpecifier( ) );
			}
		}
	}
}
