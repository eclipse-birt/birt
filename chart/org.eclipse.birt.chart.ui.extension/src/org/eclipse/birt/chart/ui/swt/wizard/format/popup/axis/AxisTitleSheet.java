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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis;

import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * 
 */

public class AxisTitleSheet extends AbstractPopupSheet
		implements
			SelectionListener,
			Listener
{

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacTitle = null;

	private transient Spinner iscInterval;

	private transient Axis axis;

	private transient int axisType;

	public AxisTitleSheet( String title, ChartWizardContext context, Axis axis,
			int axisType )
	{
		super( title, context, true );
		this.axis = axis;
		this.axisType = axisType;
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_TEXT_FORMAT );

		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glMain = new GridLayout( );
			glMain.marginHeight = 7;
			glMain.marginWidth = 7;
			cmpContent.setLayout( glMain );
		}

		if ( axisType == AngleType.Z )
		{
			LabelAttributesContext attributesContext = new LabelAttributesContext( );
			attributesContext.isPositionEnabled = false;
			attributesContext.isVisibilityEnabled = false;
			lacTitle = new LabelAttributesComposite( cmpContent,
					SWT.NONE,
					getContext( ),
					attributesContext,
					Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Title" ),//$NON-NLS-1$
					getAxisForProcessing( ).getTitlePosition( ),
					getAxisForProcessing( ).getTitle( ),
					getChart( ).getUnits( ) );
		}
		else
		{
			LabelAttributesContext attributesContext = new LabelAttributesContext( );
			attributesContext.isVisibilityEnabled = false;
			lacTitle = new LabelAttributesComposite( cmpContent,
					SWT.NONE,
					getContext( ),
					attributesContext,
					Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Title" ),//$NON-NLS-1$
					getAxisForProcessing( ).getTitlePosition( ),
					getAxisForProcessing( ).getTitle( ),
					getChart( ).getUnits( ),
					getPositionScope( ) );

		}
		GridData gdLACTitle = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		gdLACTitle.widthHint = 200;
		lacTitle.setLayoutData( gdLACTitle );
		lacTitle.addListener( this );

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
					getAxisForProcessing( ).getTitle( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.POSITION_CHANGED_EVENT :
					getAxisForProcessing( ).setTitlePosition( (Position) event.data );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					getAxisForProcessing( ).getTitle( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setInsets( (Insets) event.data );
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
		if ( e.getSource( ).equals( iscInterval ) )
		{
			getAxisForProcessing( ).setInterval( iscInterval.getSelection( ) );
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

	private Axis getAxisForProcessing( )
	{
		return axis;
	}

	private int getPositionScope( )
	{
		// Vertical position for X axis
		if ( axisType == AngleType.X )
		{
			return LabelAttributesComposite.ALLOW_VERTICAL_POSITION;
		}
		return LabelAttributesComposite.ALLOW_HORIZONTAL_POSITION;
	}

}
