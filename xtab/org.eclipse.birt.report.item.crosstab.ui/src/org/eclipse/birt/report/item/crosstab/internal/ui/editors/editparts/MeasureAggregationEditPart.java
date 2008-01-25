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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;

/**
 *  The data item in the measure aggregation.
 */

public class MeasureAggregationEditPart extends DataEditPart
{
	
	/**Constructor
	 * @param model
	 */
	public MeasureAggregationEditPart( Object model )
	{
		super( model );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart#getText()
	 */
	protected String getText( )
	{
		if (!hasBindingFunction( ))
		{
			return super.getText( );
		}
		String retValue =  getMeasureName( );
		if (retValue == null)
		{
			return super.getText( );
		}
		((LabelFigure)getFigure( )).setSpecialPREFIX( PREFIX );
		return PREFIX+ "[" + retValue + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private String getMeasureName()
	{
		try
		{
			DataItemHandle handle = (DataItemHandle) getModel( );
			
			ComputedColumnHandle bindingColumn = DEUtil.getInputBinding( handle, handle.getResultSetColumn( ) );
			
			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION) );
			return session.getCubeQueryUtil( ).getReferencedMeasureName( bindingColumn.getExpression( ) );
		}
		catch ( BirtException e )
		{
			return null;
		}
	}
}
