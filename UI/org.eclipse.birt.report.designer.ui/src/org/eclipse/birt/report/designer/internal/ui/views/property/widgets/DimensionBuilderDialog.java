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

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

	private String unitName;

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
		try
		{
			measureData = StringUtil.parseInput( measure.getText( ),
					ThreadResources.getLocale( ) );
		}
		catch ( PropertyValueException e )
		{
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent,IHelpContextIds.DIMENSION_BUILDER_DIALOG_DIALOG );  
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
		if ( measureData != null && measureData != "" )
		{
			measure.setText( StringUtil.doubleToString( ( (Double) measureData ).doubleValue( ),
					3,
					ThreadResources.getLocale( ) ) );
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

		IChoiceSet choiceSet = DesignEngine.getMetaDataDictionary( )
				.getChoiceSet( DesignChoiceConstants.CHOICE_UNITS );

		units = new Button[unitNames.length];
		for ( int i = 0; i < units.length; i++ )
		{
			units[i] = new Button( composite, SWT.RADIO );
			IChoice choice = choiceSet.findChoice( unitNames[i] );
			if ( choice != null )
			{
				units[i].setData( choice.getName( ) );
				units[i].setText( choice.getDisplayName( ) );
			}
			else
			{
				units[i].setData( unitNames[i] );
				units[i].setText( unitNames[i] );
			}

			if ( units[i].getData( ).equals( getUnitName( ) ) )
			{
				units[i].setSelection( true );
			}
			
			final int currentUnitData = i;
			units[i].addSelectionListener( new SelectionListener( ) {

				public void widgetSelected( SelectionEvent e )
				{
					setUnitName( units[currentUnitData].getData( ).toString( ) );
				}

				public void widgetDefaultSelected( SelectionEvent e )
				{
				}
			} );
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
	 * @return Returns the measureData.
	 */
	public Object getMeasureData( )
	{
		return measureData;
	}

	/**
	 * @param the
	 *            unit name
	 */
	public void setUnitName( String unitName )
	{
		this.unitName = unitName;
	}
	
	/**
	 * @return the selected unit name
	 */
	public String getUnitName( )
	{
		return unitName;
	}
}