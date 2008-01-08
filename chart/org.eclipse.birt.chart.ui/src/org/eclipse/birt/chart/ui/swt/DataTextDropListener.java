/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * This class implements supporting for Drop of selected table column header
 */
public class DataTextDropListener extends DropTargetAdapter
{

	private Control txtDataDefn = null;

	public DataTextDropListener( Control txtDataDefn )
	{
		super( );
		this.txtDataDefn = txtDataDefn;
		assert txtDataDefn instanceof Text || txtDataDefn instanceof Combo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter( DropTargetEvent event )
	{
		// always indicate a copy
		event.detail = DND.DROP_COPY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged( DropTargetEvent event )
	{
		// always indicate a copy
		event.detail = DND.DROP_COPY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver( DropTargetEvent event )
	{
		// TODO Auto-generated method stub
		event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop( DropTargetEvent event )
	{
		String expression = (String) event.data;
		// If it's last element, remove color binding
		if ( !expression.equals( getText( txtDataDefn ) )
				&& DataDefinitionTextManager.getInstance( )
						.getNumberOfSameDataDefinition( getText( txtDataDefn ) ) == 1 )
		{
			ColorPalette.getInstance( ).retrieveColor( getText( txtDataDefn ) );
		}
		setText( txtDataDefn, expression );

		DataDefinitionTextManager.getInstance( ).updateQuery( txtDataDefn );

		// Refresh all data definition text
		DataDefinitionTextManager.getInstance( ).refreshAll( );
	}

	private String getText( Control control )
	{
		if ( control instanceof Text )
		{
			return ( (Text) control ).getText( );
		}
		if ( control instanceof Combo )
		{
			return ( (Combo) control ).getText( );
		}
		return ""; //$NON-NLS-1$
	}

	private void setText( Control control, String text )
	{
		if ( control instanceof Text )
		{
			( (Text) control ).setText( text );
		}
		else if ( control instanceof Combo )
		{
			( (Combo) control ).setText( text );
		}
	}

}
