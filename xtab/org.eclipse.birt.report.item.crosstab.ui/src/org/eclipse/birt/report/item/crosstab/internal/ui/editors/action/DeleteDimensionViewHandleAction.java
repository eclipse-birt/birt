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

import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Delete the dimension view handle
 */

public class DeleteDimensionViewHandleAction extends AbstractCrosstabAction
{

	//LevelViewHandle viewHandle = null;
	DimensionViewHandle dimensionHandle;
	private static final String NAME = "delete test dimensionviewhandle";
	private static final String ID = "delete_test_dimensionviewhandle";
	private static final String TEXT = "Remove";
	
	private static final String DISPALY_NAME = "dimensionviewhandle";
	
	/**Constructor
	 * @param handle
	 * @param index
	 */
	public DeleteDimensionViewHandleAction( DesignElementHandle handle)
	{
		super( handle );
		setId( ID );

		setText( TEXT );
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle( handle );
		setHandle( extendedHandle );
		dimensionHandle = CrosstabAdaptUtil.getDimensionViewHandle( extendedHandle );
		//viewHandle = dimensionHandle.getLevel( getLevelIndex( ) );
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		if (dimensionHandle == null)
		{
			return;
		}
		transStar( NAME );
		try
		{
			dimensionHandle.getCrosstab( ).removeDimension( dimensionHandle.getAxisType( ), dimensionHandle.getIndex( ) );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			return;
		}
		transEnd( );
	}
}
