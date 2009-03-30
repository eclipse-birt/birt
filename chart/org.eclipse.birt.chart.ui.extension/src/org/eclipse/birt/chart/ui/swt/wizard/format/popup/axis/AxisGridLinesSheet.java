/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
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
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.GridAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * Gridlines popup sheet
 */

public class AxisGridLinesSheet extends AbstractPopupSheet implements
		SelectionListener,
		Listener
{

	private Composite cmpContent;

	private Composite cmpGeneral = null;

	// private Composite cmpGapWidth = null;

	private FillChooserComposite fccLine = null;

	private Label cmbOrientation = null;

	// private IntegerSpinControl iscGapWidth = null;

	private Button cbHidden = null;

	private Button cbTickBetweenCategory = null;

	private Group grpMajor = null;

	private Group grpMinor = null;

	private GridAttributesComposite gacMajor = null;

	private GridAttributesComposite gacMinor = null;

	private Label lblGridCount = null;

	private Spinner iscGridCount = null;

	private Axis axis;

	private int angleType;

	private Spinner majGridStNum;

	private Label lblGridStepNum;

	/**
	 * @param title
	 *            popup title
	 * @param context
	 *            wizard context
	 * @param axis
	 *            axis model
	 * @param angleType
	 *            indicate axis type, value is AngleType.X or AngleType.Y or
	 *            AngleType.Z
	 */
	public AxisGridLinesSheet( String title, ChartWizardContext context,
			Axis axis, int angleType )
	{
		super( title, context, true );
		this.axis = axis;
		this.angleType = angleType;
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_AXIS_GRIDLINES );

		// Layout for the content composite
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		// Layout for the general composite
		GridLayout glGeneral = new GridLayout( );
		glGeneral.numColumns = 10;
		glGeneral.horizontalSpacing = 5;
		glGeneral.verticalSpacing = 5;
		glGeneral.marginHeight = 4;
		glGeneral.marginWidth = 4;

		// Layout for the Major Grid group
		FillLayout flMajor = new FillLayout( );

		// Main content composite
		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		// Choices composite
		Composite choiceComposite = new Composite( cmpContent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		choiceComposite.setLayoutData( gd );
		choiceComposite.setLayout( new GridLayout( 2, true ) );

		// Axis Visibility
		cbHidden = new Button( choiceComposite, SWT.CHECK );
		cbHidden.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		cbHidden.setText( Messages.getString( "BaseAxisAttributeSheetImpl.Lbl.HideAxisLine" ) ); //$NON-NLS-1$
		cbHidden.setSelection( !axis.getLineAttributes( ).isVisible( ) );
		cbHidden.addSelectionListener( this );

		// Axis as Category / Value type
		cbTickBetweenCategory = new Button( choiceComposite, SWT.CHECK );
		cbTickBetweenCategory.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
		cbTickBetweenCategory.setText( Messages.getString( "BaseAxisAttributeSheetImpl.Lbl.IsTickBetweenCategories" ) ); //$NON-NLS-1$
		cbTickBetweenCategory.setSelection( axis.getScale( )
				.isTickBetweenCategories( ) );
		cbTickBetweenCategory.addSelectionListener( this );
		cbTickBetweenCategory.setEnabled( axis.isCategoryAxis( ) );
		cbTickBetweenCategory.setVisible( (angleType == AngleType.X ) && axis.isCategoryAxis( ) );

		// General attributes composite
		cmpGeneral = new Composite( cmpContent, SWT.NONE );
		GridData gdCMPGeneral = new GridData( GridData.FILL_BOTH );
		gdCMPGeneral.horizontalSpan = 2;
		gdCMPGeneral.grabExcessVerticalSpace = false;
		cmpGeneral.setLayoutData( gdCMPGeneral );
		cmpGeneral.setLayout( glGeneral );

		// Axis Line Color
		Label lblColor = new Label( cmpGeneral, SWT.NONE );
		GridData gdLBLColor = new GridData( GridData.FILL );
		lblColor.setLayoutData( gdLBLColor );
		lblColor.setText( Messages.getString( "BaseAxisAttributeSheetImpl.Lbl.AxisLineColor" ) ); //$NON-NLS-1$

		ColorDefinition clrCurrent = null;
		if ( axis.eIsSet( ComponentPackage.eINSTANCE.getAxis_LineAttributes( ) ) )
		{
			clrCurrent = axis.getLineAttributes( ).getColor( );
		}
		fccLine = new FillChooserComposite( cmpGeneral,
				SWT.NONE,
				getContext( ),
				clrCurrent,
				false,
				false );
		GridData gdFCCLine = new GridData( GridData.FILL_BOTH );
		gdFCCLine.horizontalSpan = 9;
		gdFCCLine.heightHint = fccLine.getPreferredSize( ).y;
		gdFCCLine.grabExcessVerticalSpace = false;
		fccLine.setLayoutData( gdFCCLine );
		fccLine.addListener( this );

		lblGridStepNum = new Label( cmpGeneral, SWT.NONE );
		GridData gdLblGridStepNum = new GridData( GridData.FILL );
		lblGridStepNum.setLayoutData( gdLblGridStepNum );
		lblGridStepNum.setText( Messages.getString("BaseAxisDataSheetImpl.Lbl.MajorGridStepNum") ); //$NON-NLS-1$
		
		majGridStNum = new Spinner( cmpGeneral, SWT.BORDER );
		{
			gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 4;
			majGridStNum.setLayoutData( gd );
			majGridStNum.setMinimum( 1 );
			majGridStNum.setSelection( getAxisForProcessing( ).getScale( )
					.getMajorGridsStepNumber( ) );
			majGridStNum.addSelectionListener( this );
		}
		
		lblGridCount = new Label( cmpGeneral, SWT.NONE );
		GridData gdLBLGridCount = new GridData( );
		lblGridCount.setLayoutData( gdLBLGridCount );
		lblGridCount.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.MinorGridCount" ) ); //$NON-NLS-1$

		iscGridCount = new Spinner( cmpGeneral, SWT.BORDER );
		{
			gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 4;
			iscGridCount.setLayoutData( gd );
			iscGridCount.setMinimum( 1 );
			iscGridCount.setSelection( getAxisForProcessing( ).getScale( )
					.getMinorGridsPerUnit( ) );
			iscGridCount.addSelectionListener( this );
		}

		// Comments out for deficient support
		// Axis gap width
		// cmpGapWidth = new Composite( cmpGeneral, SWT.NONE );
		// GridData gdCMPGapWidth = new GridData( GridData.FILL_BOTH );
		// gdCMPGapWidth.horizontalSpan = 5;
		// cmpGapWidth.setLayoutData( gdCMPGapWidth );
		// cmpGapWidth.setLayout( glGapWidth );
		//
		// Label lblSpacing = new Label( cmpGapWidth, SWT.NONE );
		// GridData gdLBLSpacing = new GridData( GridData.FILL );
		// gdLBLSpacing.heightHint = 20;
		// lblSpacing.setLayoutData( gdLBLSpacing );
		// lblSpacing.setText( Messages.getString(
		// "BaseAxisAttributeSheetImpl.Lbl.GapWidth" ) ); //$NON-NLS-1$
		//
		// iscGapWidth = new IntegerSpinControl( cmpGapWidth,
		// SWT.NONE,
		// (int) axis.getGapWidth( ) );
		// GridData gdISCSpacing = new GridData( GridData.FILL_BOTH
		// | GridData.GRAB_HORIZONTAL );
		// gdISCSpacing.horizontalSpan = 6;
		// gdISCSpacing.widthHint = 100;
		// gdISCSpacing.heightHint = iscGapWidth.getPreferredSize( ).y;
		// iscGapWidth.setLayoutData( gdISCSpacing );
		// iscGapWidth.addListener( this );
		//
		// Label lblSpacingUnit = new Label( cmpGapWidth, SWT.NONE );
		// GridData gdLBLSpacingUnit = new GridData( GridData.FILL );
		// gdLBLSpacingUnit.widthHint = 20;
		// gdLBLSpacingUnit.grabExcessHorizontalSpace = false;
		// lblSpacingUnit.setLayoutData( gdLBLSpacingUnit );
		// lblSpacingUnit.setText( Messages.getString(
		// "BaseAxisAttributeSheetImpl.Lbl.PercentSign" ) ); //$NON-NLS-1$

		// Major Grid
		grpMajor = new Group( cmpContent, SWT.NONE );
		GridData gdGRPMajor = new GridData( GridData.FILL_HORIZONTAL );
		grpMajor.setLayoutData( gdGRPMajor );
		grpMajor.setText( Messages.getString( "BaseAxisAttributeSheetImpl.Lbl.MajorGrid" ) ); //$NON-NLS-1$
		grpMajor.setLayout( flMajor );

		gacMajor = new GridAttributesComposite( grpMajor,
				SWT.NONE,
				getContext( ),
				axis.getMajorGrid( ),
				axis.getOrientation( ).getValue( ) );
		gacMajor.addListener( this );

		// Minor Grid
		grpMinor = new Group( cmpContent, SWT.NONE );
		{
			grpMinor.setLayout( new FillLayout( ) );
			GridData gdGRPMinor = new GridData( GridData.FILL_HORIZONTAL );
			grpMinor.setLayoutData( gdGRPMinor );
			grpMinor.setText( Messages.getString( "BaseAxisAttributeSheetImpl.Lbl.MinorGrid" ) ); //$NON-NLS-1$
		}

		gacMinor = new GridAttributesComposite( grpMinor,
				SWT.NONE,
				getContext( ),
				axis.getMinorGrid( ),
				axis.getOrientation( ).getValue( ) );
		gacMinor.addListener( this );

		setStateOfMajorGrid( );
		setStateOfMinorGrid( );

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		// if ( this.iscGapWidth.equals( event.widget ) )
		// {
		// if ( event.type == IntegerSpinControl.VALUE_CHANGED_EVENT )
		// {
		// getAxisForProcessing( ).setGapWidth( iscGapWidth.getValue( ) );
		// }
		// }
		if ( this.fccLine.equals( event.widget ) )
		{
			if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
			{
				getAxisForProcessing( ).getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( this.gacMajor.equals( event.widget ) )
		{
			switch ( event.type )
			{
				case GridAttributesComposite.LINE_STYLE_CHANGED_EVENT :
					getAxisForProcessing( ).getMajorGrid( )
							.getLineAttributes( )
							.setStyle( (LineStyle) event.data );
					break;
				case GridAttributesComposite.LINE_WIDTH_CHANGED_EVENT :
					getAxisForProcessing( ).getMajorGrid( )
							.getLineAttributes( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case GridAttributesComposite.LINE_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getMajorGrid( )
							.getLineAttributes( )
							.setColor( (ColorDefinition) event.data );
					break;
				case GridAttributesComposite.LINE_VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getMajorGrid( )
							.getLineAttributes( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					setStateOfMajorGrid( );
					break;
				case GridAttributesComposite.TICK_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getMajorGrid( )
							.getTickAttributes( )
							.setColor( (ColorDefinition) event.data );
					break;
				case GridAttributesComposite.TICK_STYLE_CHANGED_EVENT :
					getAxisForProcessing( ).getMajorGrid( )
							.setTickStyle( (TickStyle) event.data );
					break;
				case GridAttributesComposite.TICK_VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getMajorGrid( )
							.getTickAttributes( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if ( this.gacMinor.equals( event.widget ) )
		{
			switch ( event.type )
			{
				case GridAttributesComposite.LINE_STYLE_CHANGED_EVENT :
					getAxisForProcessing( ).getMinorGrid( )
							.getLineAttributes( )
							.setStyle( (LineStyle) event.data );
					break;
				case GridAttributesComposite.LINE_WIDTH_CHANGED_EVENT :
					getAxisForProcessing( ).getMinorGrid( )
							.getLineAttributes( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case GridAttributesComposite.LINE_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getMinorGrid( )
							.getLineAttributes( )
							.setColor( (ColorDefinition) event.data );
					break;
				case GridAttributesComposite.LINE_VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getMinorGrid( )
							.getLineAttributes( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					setStateOfMinorGrid( );
					break;
				case GridAttributesComposite.TICK_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getMinorGrid( )
							.getTickAttributes( )
							.setColor( (ColorDefinition) event.data );
					break;
				case GridAttributesComposite.TICK_STYLE_CHANGED_EVENT :
					getAxisForProcessing( ).getMinorGrid( )
							.setTickStyle( (TickStyle) event.data );
					break;
				case GridAttributesComposite.TICK_VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getMinorGrid( )
							.getTickAttributes( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					setStateOfMinorGrid( );
					break;
			}
		}
	}

	private void setStateOfMinorGrid( )
	{
		boolean enabled;
		if ( ChartUIUtil.is3DWallFloorSet( getChart( ) ) )
		{
			enabled = getAxisForProcessing( ).getMinorGrid( )
				.getLineAttributes( )
				.isVisible( )
				;
			if ( !ChartUIUtil.is3DType( getChart( ) ) )
			{
				enabled = enabled
						|| getAxisForProcessing( ).getMinorGrid( )
								.getTickAttributes( )
								.isVisible( );
			}
		}
		else
		{
			enabled = false;
		}
		lblGridCount.setEnabled( enabled );
		iscGridCount.setEnabled( enabled );
	}
	
	private void setStateOfMajorGrid( )
	{
		boolean enabled;
		if ( ChartUIUtil.is3DWallFloorSet( getChart( ) ) )
		{
			enabled = getAxisForProcessing( ).getMajorGrid( )
					.getLineAttributes( )
					.isVisible( );
		}
		else
		{
			enabled = false;
		}
		lblGridStepNum.setEnabled( enabled );
		majGridStNum.setEnabled( enabled );
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Object oSource = e.getSource( );
		if ( oSource.equals( cbHidden ) )
		{
			// Process hiding showing of axis
			getAxisForProcessing( ).getLineAttributes( )
					.setVisible( !cbHidden.getSelection( ) );
		}
		else if ( oSource.equals( cbTickBetweenCategory ) )
		{
			// Process setting of cross categories
			getAxisForProcessing( ).getScale( )
					.setTickBetweenCategories( cbTickBetweenCategory.getSelection( ) );
		}
		if ( oSource.equals( iscGridCount ) )
		{
			getAxisForProcessing( ).getScale( )
					.setMinorGridsPerUnit( iscGridCount.getSelection( ) );
		}
		else if ( oSource.equals( majGridStNum ) )
		{
			getAxisForProcessing( ).getScale( )
					.setMajorGridsStepNumber( majGridStNum.getSelection( ) );
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

}
