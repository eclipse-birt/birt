/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup;

import java.text.ParseException;
import java.util.Date;

import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
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

public abstract class AbstractScaleSheet extends AbstractPopupSheet
		implements
			Listener
{

	private static final SimpleDateFormat _sdf = new SimpleDateFormat( "MM-dd-yyyy HH:mm:ss" ); //$NON-NLS-1$

	private transient Composite cmpContent;

	protected transient Label lblMin = null;

	protected transient TextEditorComposite txtScaleMin = null;

	protected transient Label lblMax = null;

	protected transient TextEditorComposite txtScaleMax = null;

	protected transient Label lblStep = null;

	protected transient TextEditorComposite txtScaleStep = null;

	// Not support now
	// private transient Label lblUnit = null;
	//
	// private transient Combo cmbScaleUnit = null;

	public AbstractScaleSheet( String title, ChartWizardContext context )
	{
		super( title, context, true );
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
				| SWT.SINGLE, getValueType( ) );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 100;
			txtScaleMin.setLayoutData( gd );
			txtScaleMin.setText( getValue( getScale( ).getMin( ) ) );
			txtScaleMin.addListener( this );
			txtScaleMin.setDefaultValue( "" ); //$NON-NLS-1$
		}

		lblMax = new Label( grpScale, SWT.NONE );
		lblMax.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Maximum" ) ); //$NON-NLS-1$

		txtScaleMax = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE, getValueType( ) );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 100;
			txtScaleMax.setLayoutData( gd );
			txtScaleMax.setText( getValue( getScale( ).getMax( ) ) );
			txtScaleMax.addListener( this );
			txtScaleMax.setDefaultValue( "" ); //$NON-NLS-1$
		}

		lblStep = new Label( grpScale, SWT.NONE );
		lblStep.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Step" ) ); //$NON-NLS-1$

		txtScaleStep = new TextEditorComposite( grpScale, SWT.BORDER
				| SWT.SINGLE, TextEditorComposite.TYPE_NUMBERIC );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 100;
			txtScaleStep.setLayoutData( gd );
			String str = ""; //$NON-NLS-1$
			if ( getScale( ).eIsSet( ComponentPackage.eINSTANCE.getScale_Step( ) ) )
			{
				str = String.valueOf( getScale( ).getStep( ) );
			}
			txtScaleStep.setText( str );
			txtScaleStep.addListener( this );
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

		return cmpContent;
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
				getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Min( ) );
			}
			else
			{
				DataElement de = getTypedDataElement( txtScaleMin.getText( ) );
				if ( de != null )
				{
					getScale( ).setMin( de );
				}
			}
		}
		else if ( event.widget.equals( txtScaleMax ) )
		{
			if ( txtScaleMax.getText( ).length( ) == 0 )
			{
				getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Max( ) );
			}
			else
			{
				DataElement de = getTypedDataElement( txtScaleMax.getText( ) );
				if ( de != null )
				{
					getScale( ).setMax( de );
				}
			}
		}
		else if ( event.widget.equals( txtScaleStep ) )
		{
			try
			{
				if ( txtScaleStep.getText( ).length( ) == 0 )
				{
					getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Step( ) );
				}
				else
				{
					double dbl = Double.valueOf( txtScaleStep.getText( ) )
							.doubleValue( );
					if ( dbl == 0 )
					{
						getScale( ).eUnset( ComponentPackage.eINSTANCE.getScale_Step( ) );
					}
					else
					{
						getScale( ).setStep( dbl );
					}
				}
			}
			catch ( NumberFormatException e1 )
			{
				txtScaleStep.setText( String.valueOf( getScale( ).getStep( ) ) );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	// public void widgetSelected( SelectionEvent e )
	// {
	// if ( e.getSource( ).equals( cmbScaleUnit ) )
	// {
	// getScale( )
	// .setUnit( ScaleUnitType.getByName(
	// LiteralHelper.scaleUnitTypeSet.getNameByDisplayName(
	// cmbScaleUnit.getText( ) ) ) );
	// }
	// }
	private String getValue( DataElement de )
	{
		if ( de instanceof DateTimeDataElement )
		{
			Date dt = ( (DateTimeDataElement) de ).getValueAsCalendar( )
					.getTime( );
			return _sdf.format( dt );
		}
		else if ( de instanceof NumberDataElement )
		{
			return ChartUIUtil.getDefaultNumberFormatInstance( )
					.format( ( (NumberDataElement) de ).getValue( ) );
		}
		return ""; //$NON-NLS-1$
	}

	private DataElement getTypedDataElement( String strDataElement )
	{
		NumberFormat nf = ChartUIUtil.getDefaultNumberFormatInstance( );
		try
		{
			// First try Date
			Date dateElement = _sdf.parse( strDataElement );
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

	protected abstract Scale getScale( );

	/**
	 * Returns the type of scale value
	 * 
	 * @return Constant value defined in <code>TextEditorComposite</code>
	 */
	protected abstract int getValueType( );

}