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

import java.util.Arrays;

import org.eclipse.birt.report.model.metadata.DimensionValue;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A cell editor that manages a dimension field.
 */
public class DimensionCellEditor extends DialogCellEditor
{

	private String[] units;
	private String unitName;

	public DimensionCellEditor( Composite parent, String[] unitNames )
	{
		super( parent );
		this.units = unitNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox( Control cellEditorWindow )
	{
		DimensionBuilderDialog dialog = new DimensionBuilderDialog( cellEditorWindow.getShell( ) );

		DimensionValue value;
		try
		{
			value = DimensionValue.parse( (String) this.getValue( ) );
		}
		catch ( PropertyValueException e )
		{
			value = null;
		}
		
		dialog.setUnitNames( units );
		dialog.setUnitData(Arrays.asList(units).indexOf(unitName));

		if ( value != null )
		{
			dialog.setMeasureData( new Double( value.getMeasure( ) ) );
		}
		
		dialog.open( );
		deactivate( );

		return dialog.getMeasureData( ).toString( ) + dialog.getUnitName( );
	}
	
	/**
	 * Set current units
	 * @param units
	 */
	public void setUnits(String units)
	{
		this.unitName = units; 
	}

}