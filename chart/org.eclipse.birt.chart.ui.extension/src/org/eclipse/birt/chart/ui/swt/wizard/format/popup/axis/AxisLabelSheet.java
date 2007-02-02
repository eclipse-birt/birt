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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * 
 */

public class AxisLabelSheet extends AbstractPopupSheet
		implements
			SelectionListener,
			Listener
{

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacLabel = null;

	private transient Spinner iscInterval;

	private transient Axis axis;

	private transient int axisType;

	public AxisLabelSheet( String title, ChartWizardContext context, Axis axis,
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

		boolean isLabelEnabled = getAxisForProcessing( ).getLabel( )
				.isVisible( );

		Group grpLabel = new Group( cmpContent, SWT.NONE );
		{
			GridLayout layout = new GridLayout( );
			layout.numColumns = 2;
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			grpLabel.setLayout( layout );
			grpLabel.setText( Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Label" ) ); //$NON-NLS-1$
			grpLabel.setEnabled( isLabelEnabled );
		}

		if ( axisType == AngleType.Z )
		{
			LabelAttributesContext attributesContext = new LabelAttributesContext( );
			attributesContext.isPositionEnabled = false;
			attributesContext.isVisibilityEnabled = false;
			attributesContext.isFontEnabled = false;
			attributesContext.isFontAlignmentEnabled = false;
			lacLabel = new LabelAttributesComposite( grpLabel,
					SWT.NONE,
					getContext( ),
					attributesContext,
					null,
					getAxisForProcessing( ).getLabelPosition( ),
					getAxisForProcessing( ).getLabel( ),
					getChart( ).getUnits( ) );
		}
		else
		{
			LabelAttributesContext attributesContext = new LabelAttributesContext( );
			attributesContext.isVisibilityEnabled = false;
			attributesContext.isFontEnabled = false;
			attributesContext.isFontAlignmentEnabled = false;
			lacLabel = new LabelAttributesComposite( grpLabel,
					SWT.NONE,
					getContext( ),
					attributesContext,
					null,
					getAxisForProcessing( ).getLabelPosition( ),
					getAxisForProcessing( ).getLabel( ),
					getChart( ).getUnits( ),
					getPositionScope( ) );
		}
		GridData gdLACLabel = new GridData( GridData.FILL_HORIZONTAL );
		gdLACLabel.horizontalSpan = 2;
		lacLabel.setLayoutData( gdLACLabel );
		lacLabel.addListener( this );
		lacLabel.setEnabled( isLabelEnabled );

		Label lblInterval = new Label( grpLabel, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalIndent = 10;
			lblInterval.setLayoutData( gd );
			lblInterval.setText( Messages.getString( "AxisTextSheet.Label.Interval" ) ); //$NON-NLS-1$
			lblInterval.setEnabled( isLabelEnabled );
		}

		iscInterval = new Spinner( grpLabel, SWT.BORDER );
		{
			iscInterval.setMinimum( 1 );
			iscInterval.setSelection( getAxisForProcessing( ).getInterval( ) );
			GridData gd = new GridData( );
			gd.widthHint = 135;
			iscInterval.setLayoutData( gd );
			iscInterval.addSelectionListener( this );
			iscInterval.setEnabled( isLabelEnabled );
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
		if ( event.widget.equals( lacLabel ) )
		{
			switch ( event.type )
			{
				case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.POSITION_CHANGED_EVENT :
					getAxisForProcessing( ).setLabelPosition( (Position) event.data );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					getAxisForProcessing( ).getLabel( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
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
