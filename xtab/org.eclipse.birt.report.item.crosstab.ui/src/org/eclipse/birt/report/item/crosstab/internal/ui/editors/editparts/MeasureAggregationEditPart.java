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

import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.util.bidi.BidiUIUtils;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;

/**
 * The data item in the measure aggregation.
 */

public class MeasureAggregationEditPart extends DataEditPart
{

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public MeasureAggregationEditPart( Object model )
	{
		super( model );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart#getText()
	 */
	protected String getText( )
	{
		// 223034
		// if (!hasBindingFunction( ))
		// {
		// return super.getText( );
		// }
		// 223034

		DataItemHandle handle = (DataItemHandle) getModel( );
		ComputedColumnHandle bindingColumn = DEUtil.getInputBinding( handle,
				handle.getResultSetColumn( ) );
		
		if ( bindingColumn == null )
		{
			return super.getText( );
		}

		String retValue = null;
		String displayName = getDisplayName();
		if(displayName != null && displayName.length( ) > 0)
		{
			retValue = displayName;
		}
		if(retValue == null)
		{
			retValue = getMeasureName( bindingColumn );
		}
		
		if ( retValue == null )
		{
			return super.getText( );
		}
		( (LabelFigure) getFigure( ) ).setSpecialPREFIX( PREFIX );
		if ( retValue.length( ) > TRUNCATE_LENGTH )
		{
			retValue = retValue.substring( 0, TRUNCATE_LENGTH - 2 ) + ELLIPSIS;
		}
		
		if ( BidiUIUtils.INSTANCE.isDirectionRTL( getModel( ) ) )
			retValue =  BidiUIUtils.LRE + "[" + BidiUIUtils.RLE + retValue + //$NON-NLS-1$
				BidiUIUtils.PDF + "]" + BidiUIUtils.PDF + BidiUIUtils.LRE + PREFIX; //$NON-NLS-1$
		else
		// bidi_hcg end
			retValue = PREFIX + "[" + retValue + "]"; //$NON-NLS-1$//$NON-NLS-2$
		return retValue;
		//return PREFIX + "[" + retValue + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private String getMeasureName( ComputedColumnHandle bindingColumn )
	{
		DataRequestSession session = null;
		try
		{
			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			return session.getCubeQueryUtil( )
					.getReferencedMeasureName( DataUtil.getAggregationExpression( bindingColumn ) );
		}
		catch ( Exception e )
		{
			return null;
		}
		finally
		{
			if (session != null)
			{
				session.shutdown( );
			}
		}
	}
}
