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

import java.util.Date;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * A cell editor that manages a date-time property.
 */
public class DateTimeCellEditor extends DialogCellEditor
{

	/**
	 * Creates a new date-time cell editor parented under the given control.
	 * 
	 * @param parent
	 *            the parent control
	 */
	public DateTimeCellEditor( Composite parent )
	{
		super( parent );
	}

	/**
	 * Creates a new date-time cell editor parented under the given control.
	 * 
	 * @param parent
	 *            the parent control
	 * @param style
	 *            the style bits
	 */
	public DateTimeCellEditor( Composite parent, int style )
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
		TimeOptionDialog dialog = new TimeOptionDialog( cellEditorWindow.getShell( ) );
		Date value = (Date) getValue( );

		if ( value != null )
		{
			TimeDialogInfo time = new TimeDialogInfo( );
			time.setTime( value.getTime( ) );
			dialog.setInfo( time );
		}
		dialog.open( );
		if ( dialog.getReturnCode( ) == SelectionDialog.OK )
		{
			TimeDialogInfo result = (TimeDialogInfo) dialog.getInfo( );
			return new Date( result.getTime( ) );
		}

		return value;
	}

}