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
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class TitleTextSheet extends AbstractPopupSheet implements Listener
{

	private transient Composite cmpContent;

	private transient Composite cmpLabel;

	private transient LabelAttributesComposite lacLabel;

	public TitleTextSheet( String title, ChartWizardContext context )
	{
		super( title, context, false );
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent,
				ChartHelpContextIds.POPUP_CHART_TITLE_FORMAT );

		// Layout for the content composite
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		// Sheet content composite
		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		// Layout for general composite
		GridLayout glLabel = new GridLayout( );
		glLabel.numColumns = 2;
		glLabel.horizontalSpacing = 5;
		glLabel.verticalSpacing = 5;
		glLabel.marginHeight = 0;
		glLabel.marginWidth = 0;

		cmpLabel = new Composite( cmpContent, SWT.NONE );
		GridData gdCMPLabel = new GridData( GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL );
		cmpLabel.setLayoutData( gdCMPLabel );
		cmpLabel.setLayout( glLabel );

		LabelAttributesContext attributesContext = new LabelAttributesContext( );
		attributesContext.isVisibilityEnabled = false;
		attributesContext.isPositionEnabled = false;
		attributesContext.isFontEnabled = false;
		lacLabel = new LabelAttributesComposite( cmpLabel,
				SWT.NONE,
				getContext( ),
				attributesContext,
				Messages.getString( "TitlePropertiesSheet.Label.Text" ), //$NON-NLS-1$
				Position.INSIDE_LITERAL,
				getBlockForProcessing( ).getLabel( ),
				getChart( ).getUnits( ) );
		GridData gdLACLabel = new GridData( GridData.FILL_HORIZONTAL );
		gdLACLabel.horizontalSpan = 2;
		lacLabel.setLayoutData( gdLACLabel );
		lacLabel.addListener( this );

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( lacLabel ) )
		{
			switch ( event.type )
			{
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					getBlockForProcessing( ).getLabel( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					getBlockForProcessing( ).getLabel( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					getBlockForProcessing( ).getLabel( )
							.setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					getBlockForProcessing( ).getLabel( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					getBlockForProcessing( ).getLabel( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					getBlockForProcessing( ).getLabel( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getBlockForProcessing( ).getLabel( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					getBlockForProcessing( ).getLabel( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getBlockForProcessing( ).getLabel( )
							.setInsets( (Insets) event.data );
					break;
			}
		}
	}

	private LabelBlock getBlockForProcessing( )
	{
		return getChart( ).getTitle( );
	}
}
