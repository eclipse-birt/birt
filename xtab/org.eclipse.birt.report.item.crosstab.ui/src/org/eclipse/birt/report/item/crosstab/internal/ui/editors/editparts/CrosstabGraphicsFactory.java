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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts;

import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * Factory to create the editpart.
 */
public class CrosstabGraphicsFactory implements EditPartFactory
{

	/**
	 * Comment for <code>INSTANCEOF</code>
	 */
	public static final CrosstabGraphicsFactory INSTANCEOF = new CrosstabGraphicsFactory( );

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart( EditPart context, Object model )
	{
		if ( context instanceof CrosstabCellEditPart )
		{
			if ( model instanceof DataItemHandle )
			{
				String position = ( (CrosstabCellEditPart) context ).getCrosstabCellAdapter( )
						.getPositionType( );
				if ( ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals( position ) )
				{
					return new FirstLevelHandleDataItemEditPart( model );
				}
				else if (ICrosstabCellAdapterFactory.CELL_LEVEL_HANDLE.equals( position ))
				{
					//FirstLevelHandleDataItemEditPart
					return new LevelHandleDataItemEditPart( model );
				}
				else if (ICrosstabCellAdapterFactory.CELL_MEASURE.equals( position )
						|| ICrosstabCellAdapterFactory.CELL_MEASURE_HEADER.equals( position ))
				{
					return new MeasureHandleDataItemEditPart(model);
				}
			}
		}
		if ( model instanceof VirtualCrosstabCellAdapter )
		{
			return new VirtualCellEditPart( model );
		}
		if ( model instanceof CrosstabCellAdapter )
		{
			return new CrosstabCellEditPart( model );
		}
		return null;
	}
}
