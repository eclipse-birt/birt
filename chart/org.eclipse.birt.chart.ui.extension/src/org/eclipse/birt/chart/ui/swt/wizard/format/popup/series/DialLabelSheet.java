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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierPreview;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class DialLabelSheet extends AbstractPopupSheet
		implements
			Listener,
			SelectionListener
{

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacTitle = null;

	private transient Button btnFormatSpecifier;

	private transient SeriesDefinition seriesDefn = null;

	private FormatSpecifierPreview fsp;

	public DialLabelSheet( String title, ChartWizardContext context,
			SeriesDefinition seriesDefn )
	{
		super( title, context, false );
		this.seriesDefn = seriesDefn;
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_SERIES_LABEL );

		// Layout for the content composite
		GridLayout glContent = new GridLayout( 2, false );
		glContent.verticalSpacing = 0;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		LabelAttributesContext attributesContext = new LabelAttributesContext( );
		attributesContext.isPositionEnabled = false;
		attributesContext.isFontAlignmentEnabled = false;
		lacTitle = new LabelAttributesComposite( cmpContent,
				SWT.NONE,
				getContext( ),
				attributesContext,
				null,
				null,
				getSeriesForProcessing( ).getDial( ).getLabel( ),
				getChart( ).getUnits( ) );
		GridData gdLACTitle = new GridData( GridData.FILL_HORIZONTAL );
		gdLACTitle.horizontalSpan = 2;
		lacTitle.setLayoutData( gdLACTitle );
		lacTitle.addListener( this );

		Label label = new Label( cmpContent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalIndent = 10;
			label.setLayoutData( gd );
			label.setText( Messages.getString( "DialLabelSheet.Label.Format" ) ); //$NON-NLS-1$
		}

		Composite cmpFormat = new Composite( cmpContent, SWT.BORDER );
		{
			GridLayout layout = new GridLayout( 2, false );
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			layout.horizontalSpacing = 0;
			cmpFormat.setLayout( layout );
			cmpFormat.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			cmpFormat.setBackground( cmpFormat.getDisplay( )
					.getSystemColor( SWT.COLOR_WHITE ) );
		}

		fsp = new FormatSpecifierPreview( cmpFormat, SWT.NONE, false );
		{
			GridData gd = new GridData( );
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.CENTER;
			fsp.setLayoutData( gd );
			fsp.updatePreview( getSeriesForProcessing( ).getDial( )
					.getFormatSpecifier( ) );
		}

		btnFormatSpecifier = new Button( cmpFormat, SWT.PUSH );
		{
			GridData gd = new GridData( );
			gd.widthHint = 20;
			gd.heightHint = 20;
			btnFormatSpecifier.setLayoutData( gd );
			btnFormatSpecifier.setToolTipText( Messages.getString( "BaseDataDefinitionComponent.Text.EditFormat" ) ); //$NON-NLS-1$
			btnFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
			btnFormatSpecifier.getImage( )
					.setBackground( btnFormatSpecifier.getBackground( ) );
			btnFormatSpecifier.addSelectionListener( this );
		}

		return cmpContent;
	}

	private DialSeries getSeriesForProcessing( )
	{
		return (DialSeries) seriesDefn.getDesignTimeSeries( );
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
					getLabel( ).setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					getLabel( ).getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					getLabel( ).getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					getLabel( ).setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					getLabel( ).setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					getLabel( ).getOutline( ).setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					getLabel( ).getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getLabel( ).getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					getLabel( ).getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getLabel( ).setInsets( (Insets) event.data );
					break;
			}
		}
	}

	private org.eclipse.birt.chart.model.component.Label getLabel( )
	{
		return getSeriesForProcessing( ).getDial( ).getLabel( );
	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnFormatSpecifier ) )
		{
			FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					getSeriesForProcessing( ).getDial( ).getFormatSpecifier( ),
					Messages.getString( "BaseDataDefinitionComponent.Text.EditFormat" ) ); //$NON-NLS-1$
			if ( editor.open( ) == Window.OK )
			{
				getSeriesForProcessing( ).getDial( )
						.setFormatSpecifier( editor.getFormatSpecifier( ) );
				fsp.updatePreview( editor.getFormatSpecifier( ) );
			}
		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}
}