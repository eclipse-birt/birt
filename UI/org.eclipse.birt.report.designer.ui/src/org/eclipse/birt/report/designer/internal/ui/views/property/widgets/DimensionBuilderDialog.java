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

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

/**
 * A dialog for dimension builder in the property view.
 *  
 */
public class DimensionBuilderDialog extends SelectionStatusDialog
{

	private static String TITLE = Messages
			.getString( "DimensionBuilderDialog.Title" ); //$NON-NLS-1$

	private static String LABEL_MEASURE = Messages
			.getString( "DimensionBuilderDialog.LabelMeasure" ); //$NON-NLS-1$

	private static String LABEL_UNIT = Messages
			.getString( "DimensionBuilderDialog.LabelUnit" ); //$NON-NLS-1$

	private Button[] units = new Button[]{};

	private String[] unitNames;

	private Text measure = null;

	private Object measureData = ""; //$NON-NLS-1$

	private int unitData;

	/**
	 * @param parent
	 */
	public DimensionBuilderDialog( Shell parent )
	{
		super( parent );
		this.setTitle( TITLE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#computeResult()
	 */
	protected void computeResult( )
	{
		measureData = measure.getText( );
		for ( int i = 0; i < units.length; i++ )
		{
			if ( units[i].getSelection( ) == true )
			{
				unitData = i;
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );

		GridLayout gridLayout = new GridLayout( );
		gridLayout.numColumns = 2;
		composite.setLayout( gridLayout );

		createMeasureField( composite );

		createUnitGroup( composite );

		return composite;
	}

	/**
	 * @param composite
	 */
	private void createMeasureField( Composite composite )
	{
		new Label( composite, SWT.NONE ).setText( LABEL_MEASURE );

		measure = new Text( composite, SWT.SINGLE | SWT.BORDER );
		GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
		gridData.horizontalSpan = 2;
		measure.setLayoutData( gridData );
		measure.setFont( composite.getFont( ) );
		if ( measureData != null )
		{
			measure.setText( measureData.toString( ) );

		}
	}

	/**
	 * @param composite
	 */
	private void createUnitGroup( Composite composite )
	{
		Label unitLabel = new Label( composite, SWT.NONE );
		unitLabel.setText( LABEL_UNIT );
		GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
		gridData.horizontalSpan = 2;
		unitLabel.setLayoutData( gridData );

		units = new Button[unitNames.length];

		for ( int i = 0; i < units.length; i++ )
		{
			units[i] = new Button( composite, SWT.RADIO );
			units[i].setText( unitNames[i] );
			if ( i == unitData )
			{
				units[i].setSelection( true );
			}
			else
			{
				units[i].setSelection( false );
			}
		}
	}

	/**
	 * @param measureData
	 *            The measureData to set.
	 */
	public void setMeasureData( Object measureData )
	{
		if ( measureData != null )
		{
			this.measureData = measureData;
		}
	}

	/**
	 * @param unitNames
	 *            The unitNames to set.
	 */
	public void setUnitNames( String[] unitNames )
	{
		this.unitNames = unitNames;
	}

	/**
	 * @param unit
	 */
	public void setUnitData( int unit )
	{
		unitData = unit;

	}

	/**
	 * @return Returns the measureData.
	 */
	public Object getMeasureData( )
	{
		return measureData;
	}

	/**
	 * @return Returns the unitData.
	 */
	public String getUnitName( )
	{
		if ( unitData == 0 || unitData > ( unitNames.length - 1 )
				|| unitData < 0 )
			return ""; //$NON-NLS-1$
		return unitNames[unitData];
	}
}