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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;

/**
 * ColumnTracker
 */
public class ColumnTracker extends TableSelectionGuideTracker
{

	IContainer container;

	/**
	 * Constructor
	 * @param sourceEditPart
	 */
	public ColumnTracker( TableEditPart sourceEditPart, int column,
			IContainer container )
	{
		super( sourceEditPart, column );

		this.container = container;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableSelectionGuideTracker#select()
	 */
	public void select( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		part.selectColumn( new int[]{
			getNumber( )
		} );
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
}