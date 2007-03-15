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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Add the sub total to the level handle.
 */
//NOTE maybe this is a temp class because the SPEC
//TODO i18n the string
//TODO binding the data 
public class AddSubTotalAction extends AbstractCrosstabAction
{

	LevelViewHandle viewHandle = null;
	private static final String NAME = "add subtotal";
	private static final String ID = "add_subtotal";
	private static final String TEXT = "add subtotal";
	
	/**
	 * The name of the label into the sub total cell.
	 */
	private static final String DISPALY_NAME = "TOTAL";

	/**Constructor
	 * @param handle
	 */
	public AddSubTotalAction( DesignElementHandle handle )
	{
		super( handle );
		setId( ID );
		setText( TEXT );
		ExtendedItemHandle extendedHandle = getExtendedItemHandle( handle );
		setHandle( extendedHandle );
		viewHandle = getLevelViewHandle( extendedHandle );
	}

	private ExtendedItemHandle getExtendedItemHandle( DesignElementHandle handle )
	{
		while ( handle != null )
		{
			if ( handle instanceof ExtendedItemHandle )
			{
				return (ExtendedItemHandle) handle;
			}
			handle = handle.getContainer( );

		}
		return null;
	}

	private LevelViewHandle getLevelViewHandle(
			ExtendedItemHandle extendedHandle )
	{
		AbstractCrosstabItemHandle handle = (AbstractCrosstabItemHandle) CrosstabUtil.getReportItem( extendedHandle );
		while ( handle != null )
		{
			if ( handle instanceof LevelViewHandle )
			{
				return (LevelViewHandle) handle;
			}
			handle = handle.getContainer( );
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		transStar( NAME );
		try
		{
			String funString = DesignChoiceConstants.MEASURE_FUNCTION_SUM;
			CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab( );
			List list = new ArrayList();
			int measureCount = reportHandle.getMeasureCount( );
			for (int i=0; i<measureCount; i++)
			{
				MeasureViewHandle measureHandle = reportHandle.getMeasure( i );
				list.add( measureHandle );
			}
			
			CrosstabCellHandle cellHandle = CrosstabUtil.addAggregationHeader( viewHandle, funString, list );
			if (cellHandle == null)
			{
				return;
			}
			LabelHandle dataHandle = DesignElementFactory.getInstance( )
			.newLabel(null );
			//Label name is a compand name.
			dataHandle.setText( "[" + viewHandle.getCubeLevelName( )+ "]" + DISPALY_NAME);
			
			cellHandle.addContent( dataHandle );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			return;
		}
		transEnd( );
	}
}
