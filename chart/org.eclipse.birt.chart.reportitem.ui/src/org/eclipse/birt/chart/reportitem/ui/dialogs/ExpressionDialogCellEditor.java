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

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ExpressionDialogCellEditor contains a Label and a Button control for
 * presenting an Expression builder UI.
 */
public class ExpressionDialogCellEditor extends DialogCellEditor
{

	private List dataSetList;

	/**
	 * 
	 */
	public ExpressionDialogCellEditor( )
	{
		super( );
	}

	/**
	 * @param parent
	 */
	public ExpressionDialogCellEditor( Composite parent )
	{
		super( parent );
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ExpressionDialogCellEditor( Composite parent, int style )
	{
		super( parent, style );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox( Control cellEditorWindow )
	{
		String oldValue = (String) getValue( );
		ExpressionBuilder dialog = new ExpressionBuilder( cellEditorWindow.getShell( ),
				oldValue );
		dialog.setExpressionProvier( new ExpressionProvider( dataSetList ) );

		if ( dialog.open( ) == Dialog.OK )
		{
			String newValue = dialog.getResult( );
			if ( !newValue.equals( oldValue ) )
			{
				return newValue;
			}
		}
		return null;
	}

	public void setDataSetList( List list )
	{
		dataSetList = list;
	}
}