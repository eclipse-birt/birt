/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.ColumnHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.RowHandle;
import org.eclipse.gef.Handle;

/**
 * RowTracker
 */
public class RowTracker extends TableSelectionGuideTracker
{

	IContainer container;

	/**
	 * Constructor
	 * @param sourceEditPart
	 */
	public RowTracker( TableEditPart sourceEditPart, int row,
			IContainer container )
	{
		super( sourceEditPart, row );

		this.container = container;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableSelectionGuideTracker#select()
	 */
	public void select( )
	{
		if (container.isSelect() && getCurrentInput().isMouseButtonDown(3) )
		{
			return ;
		}
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		part.selectRow( new int[]{
			getNumber( )
		} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#handleMove()
	 */
	protected boolean handleMove( )
	{
		// TODO Auto-generated method stub
		return super.handleMove( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableSelectionGuideTracker#handleButtonUp(int)
	 */
	protected boolean handleButtonUp( int button )
	{
		boolean rlt = super.handleButtonUp( button );

		if ( button == 1
				&& container != null
				&& container.contains( getLocation( ) ) )
		{
			getSourceEditPart( ).getViewer( )
					.getContextMenu( )
					.getMenu( )
					.setVisible( true );
		}

		return rlt;
	}
	
	public boolean isDealwithDrag()
	{
		Handle handle = getHandleUnderMouse();
		if (handle instanceof RowHandle)
		{
			return ((RowHandle)handle).getOwner() == getSourceEditPart();
		}
		return false;
		//EditPart part = getEditPartUnderMouse();
		//return part instanceof TableEditPart.DummyColumnEditPart || isSameTable();
	}
	
	public void selectDrag( )
	{
		RowHandle handle = (RowHandle)getHandleUnderMouse();
		
		int rowNumber = handle.getRowNumber();
		int number = getNumber();
		int[] rows = new int[]{};
		for (int i=number; i<=number + Math.abs(number - rowNumber); i++)
		{
			int lenegth = rows.length;
			int[] temp = new int[lenegth + 1];

			System.arraycopy( rows, 0, temp, 0, lenegth );
			temp[lenegth] = number > rowNumber ? number - (i - number): i;
			rows = temp;
		}
		if (rows.length > 0)
		{
			TableEditPart tableEditpart = (TableEditPart) getSourceEditPart( );
			tableEditpart.selectRow( rows);
		}
	}
}