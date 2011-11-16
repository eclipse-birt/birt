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
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
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

public class AxisTitleSheet extends AbstractPopupSheet implements Listener
{

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacTitle = null;

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
			attributesContext.isVisibilityEnabled = !ChartUIExtensionUtil.isSetInvisible( axis.getTitle( ) );
			lacTitle = new LabelAttributesComposite( cmpContent,
					SWT.NONE,
					getContext( ),
					attributesContext,
					Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Title" ),//$NON-NLS-1$
					getAxisForProcessing( ).isSetTitlePosition( ) ? getAxisForProcessing( ).getTitlePosition( ) : null,
					getAxisForProcessing( ).getTitle( ),
					getChart( ).getUnits( ) );
			lacTitle.setDefaultLabelValue( DefaultValueProvider.defAncillaryAxis( ).getTitle( ) );
		}
		else
		{
			LabelAttributesContext attributesContext = new LabelAttributesContext( );
			attributesContext.isVisibilityEnabled = !ChartUIExtensionUtil.isSetInvisible( axis.getTitle( ) );
			lacTitle = new LabelAttributesComposite( cmpContent,
					SWT.NONE,
					getContext( ),
					attributesContext,
					Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Title" ),//$NON-NLS-1$
					getAxisForProcessing( ).isSetTitlePosition( ) ? getAxisForProcessing( ).getTitlePosition( ) : null,
					getAxisForProcessing( ).getTitle( ),
					getChart( ).getUnits( ),
					getPositionScope( ) );
			if ( axisType == AngleType.X )
			{
				lacTitle.setDefaultLabelValue( DefaultValueProvider.defBaseAxis( ).getTitle( ) );
			}
			else
			{
				lacTitle.setDefaultLabelValue( DefaultValueProvider.defOrthogonalAxis( ).getTitle( ) );
			}

		}
		GridData gdLACTitle = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
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
			boolean isUnset = ( event.detail == ChartUIExtensionUtil.PROPERTY_UNSET );
			switch ( event.type )
			{
				case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.POSITION_CHANGED_EVENT :
					ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ),
							"titlePosition", //$NON-NLS-1$
							event.data,
							isUnset );
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
					ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getTitle( )
							.getOutline( ),
							"style", //$NON-NLS-1$
							event.data,
							isUnset );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getTitle( )
							.getOutline( ),
							"thickness", //$NON-NLS-1$
							( (Integer) event.data ).intValue( ),
							isUnset );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getTitle( )
							.getOutline( ),
							"visible", //$NON-NLS-1$
							( (Boolean) event.data ).booleanValue( ),
							isUnset );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setInsets( (Insets) event.data );
					break;
			}
		}
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
