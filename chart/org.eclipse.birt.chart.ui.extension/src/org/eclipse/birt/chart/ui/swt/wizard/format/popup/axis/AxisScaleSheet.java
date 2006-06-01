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

import java.text.ParseException;
import java.util.Date;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
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

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;

/**
 * 
 */

public class AxisScaleSheet extends AbstractPopupSheet
		implements
			Listener,
			SelectionListener
{

	private transient Composite cmpContent;

	private transient Label lblMin = null;

	private transient TextEditorComposite txtScaleMin = null;

	private transient Label lblMax = null;

	private transient TextEditorComposite txtScaleMax = null;

	private transient Label lblStep = null;

	private transient TextEditorComposite txtScaleStep = null;

	// Not support now
	// private transient Label lblUnit = null;
	//
	// private transient Combo cmbScaleUnit = null;

	private transient Axis axis;

	public AxisScaleSheet( String title, ChartWizardContext context, Axis axis )
	{
		super( title, context, true );
		this.axis = axis;
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_AXIS_SCALE );
		
		GridLayout glContent = new GridLayout( 2, true );
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;
		glContent.horizontalSpacing = 2;
		glContent.verticalSpacing = 5;

		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		Group grpScale = new Group( cmpContent, SWT.NONE );
		{
			GridData gdGRPScale = new GridData( GridData.FILL_BOTH );
			gdGRPScale.horizontalSpan = 4;
			grpScale.setLayoutData( gdGRPScale );
			GridLayout glScale = new GridLayout( );
			glScale.numColumns = 4;
			glScale.horizontalSpacing = 5;
			glScale.verticalSpacing = 5;
			glScale.marginHeight = 2;
			glScale.marginWidth = 7;
			grpScale.setLayout( glScale );
			grpScale.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Scale" ) ); //$NON-NLS-1$
		}

		lblMin = new Label( grpScale, SWT.NONE );
		GridData gdLBLMin = new GridData( );
		lblMin.setLayoutData( gdLBLMin );
		lblMin.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Minimum" ) ); //$NON-NLS-1$

		txtScaleMin = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE, true );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 100;
			txtScaleMin.setLayoutData( gd );
			txtScaleMin.setText( getValue( getAxisForProcessing( ).getScale( )
					.getMin( ) ) );
			txtScaleMin.addListener( this );
			txtScaleMin.setDefaultValue( "" ); //$NON-NLS-1$
		}

		lblMax = new Label( grpScale, SWT.NONE );
		lblMax.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Maximum" ) ); //$NON-NLS-1$

		txtScaleMax = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE, true );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 100;
			txtScaleMax.setLayoutData( gd );
			txtScaleMax.setText( getValue( getAxisForProcessing( ).getScale( )
					.getMax( ) ) );
			txtScaleMax.addListener( this );
			txtScaleMax.setDefaultValue( "" ); //$NON-NLS-1$
		}

		boolean bDateTimeAxisType = getAxisForProcessing( ).getType( )
				.equals( AxisType.DATE_TIME_LITERAL );

		lblStep = new Label( grpScale, SWT.NONE );
		lblStep.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Step" ) ); //$NON-NLS-1$
		lblStep.setEnabled( !bDateTimeAxisType );

		txtScaleStep = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE, true );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 100;
			txtScaleStep.setLayoutData( gd );
			String str = ""; //$NON-NLS-1$
			if ( getAxisForProcessing( ).getScale( )
					.eIsSet( ComponentPackage.eINSTANCE.getScale_Step( ) ) )
			{
				str = String.valueOf( getAxisForProcessing( ).getScale( )
						.getStep( ) );
			}
			txtScaleStep.setText( str );
			txtScaleStep.addListener( this );
			txtScaleStep.setEnabled( !bDateTimeAxisType );
			txtScaleStep.setDefaultValue( "" ); //$NON-NLS-1$
		}

		// lblUnit = new Label( grpScale, SWT.NONE );
		// GridData gdLBLUnit = new GridData( );
		// lblUnit.setLayoutData( gdLBLUnit );
		// lblUnit.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Unit"
		// ) ); //$NON-NLS-1$
		// lblUnit.setEnabled( false );
		//
		// cmbScaleUnit = new Combo( grpScale, SWT.DROP_DOWN | SWT.READ_ONLY );
		// GridData gdCMBScaleUnit = new GridData( GridData.FILL_HORIZONTAL );
		// cmbScaleUnit.setLayoutData( gdCMBScaleUnit );
		// cmbScaleUnit.addSelectionListener( this );
		// cmbScaleUnit.setEnabled( false );

		GridLayout glGridCount = new GridLayout( );
		glGridCount.numColumns = 2;
		glGridCount.horizontalSpacing = 5;
		glGridCount.marginHeight = 0;
		glGridCount.marginWidth = 0;

		populateLists( );

		return cmpContent;
	}

	private void populateLists( )
	{
		setState( getAxisForProcessing( ).getType( ).getName( ) );

		// Populate unit types combo
		// NameSet ns = LiteralHelper.scaleUnitTypeSet;
		// cmbScaleUnit.setItems( ns.getDisplayNames( ) );
		// cmbScaleUnit.select( ns.getSafeNameIndex( getAxisForProcessing(
		// ).getScale( )
		// .getUnit( )
		// .getName( ) ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( txtScaleMin ) )
		{
			if ( txtScaleMin.getText( ).length( ) == 0 )
			{
				getAxisForProcessing( ).getScale( )
						.eUnset( ComponentPackage.eINSTANCE.getScale_Min( ) );
			}
			else
			{
				DataElement de = getTypedDataElement( txtScaleMin.getText( ) );
				if ( de != null )
				{
					getAxisForProcessing( ).getScale( ).setMin( de );
				}
			}
		}
		else if ( event.widget.equals( txtScaleMax ) )
		{
			if ( txtScaleMax.getText( ).length( ) == 0 )
			{
				getAxisForProcessing( ).getScale( )
						.eUnset( ComponentPackage.eINSTANCE.getScale_Max( ) );
			}
			else
			{
				DataElement de = getTypedDataElement( txtScaleMax.getText( ) );
				if ( de != null )
				{
					getAxisForProcessing( ).getScale( ).setMax( de );
				}
			}
		}
		else if ( event.widget.equals( txtScaleStep ) )
		{
			try
			{
				if ( txtScaleStep.getText( ).length( ) == 0 )
				{
					getAxisForProcessing( ).getScale( )
							.eUnset( ComponentPackage.eINSTANCE.getScale_Step( ) );
				}
				else
				{
					double dbl = Double.valueOf( txtScaleStep.getText( ) )
							.doubleValue( );
					if ( dbl == 0 )
					{
						getAxisForProcessing( ).getScale( )
								.eUnset( ComponentPackage.eINSTANCE.getScale_Step( ) );
					}
					else
					{
						getAxisForProcessing( ).getScale( ).setStep( dbl );
					}
				}
			}
			catch ( NumberFormatException e1 )
			{
				txtScaleStep.setText( String.valueOf( getAxisForProcessing( ).getScale( )
						.getStep( ) ) );
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		// if ( e.getSource( ).equals( cmbScaleUnit ) )
		// {
		// getAxisForProcessing( ).getScale( )
		// .setUnit( ScaleUnitType.getByName(
		// LiteralHelper.scaleUnitTypeSet.getNameByDisplayName(
		// cmbScaleUnit.getText( ) ) ) );
		// }
	}

	private void setState( String sType )
	{
		// Bugzilla#103961 Marker line and range only work for non-category
		// style X-axis,
		boolean bEnabled = !( getAxisForProcessing( ).isCategoryAxis( ) || sType.equals( AxisType.TEXT_LITERAL.getName( ) ) );
		lblMin.setEnabled( bEnabled );
		txtScaleMin.setEnabled( bEnabled );
		lblMax.setEnabled( bEnabled );
		txtScaleMax.setEnabled( bEnabled );
		lblStep.setEnabled( bEnabled );
		txtScaleStep.setEnabled( bEnabled );

		// lblUnit.setEnabled( sType.equals( "DateTime" ) ); //$NON-NLS-1$
		// cmbScaleUnit.setEnabled( sType.equals( "DateTime" ) ); //$NON-NLS-1$
		lblStep.setEnabled( bEnabled
				&& !sType.equals( AxisType.DATE_TIME_LITERAL.getName( ) ) );
		txtScaleStep.setEnabled( bEnabled
				&& !sType.equals( AxisType.DATE_TIME_LITERAL.getName( ) ) );
	}

	private String getValue( DataElement de )
	{
		if ( de instanceof DateTimeDataElement )
		{
			Date dt = ( (DateTimeDataElement) de ).getValueAsCalendar( )
					.getTime( );
			SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" ); //$NON-NLS-1$
			return sdf.format( dt );
		}
		else if ( de instanceof NumberDataElement )
		{
			return NumberFormat.getInstance( ).format( ( (NumberDataElement) de ).getValue( ) );
		}
		return ""; //$NON-NLS-1$
	}

	private DataElement getTypedDataElement( String strDataElement )
	{
		SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" ); //$NON-NLS-1$
		NumberFormat nf = NumberFormat.getInstance( );
		try
		{
			// First try Date
			Date dateElement = sdf.parse( strDataElement );
			Calendar cal = Calendar.getInstance( TimeZone.getDefault( ) );
			cal.setTime( dateElement );
			return DateTimeDataElementImpl.create( cal );
		}
		catch ( ParseException e )
		{
			// Next try double
			try
			{
				Number numberElement = nf.parse( strDataElement );
				return NumberDataElementImpl.create( numberElement.doubleValue( ) );
			}
			catch ( ParseException e1 )
			{
				return null;
			}
		}
	}

	private Axis getAxisForProcessing( )
	{
		return axis;
	}

}