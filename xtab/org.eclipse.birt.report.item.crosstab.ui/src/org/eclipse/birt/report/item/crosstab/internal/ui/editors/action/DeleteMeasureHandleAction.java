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

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class DeleteMeasureHandleAction extends AbstractCrosstabAction
{
	private MeasureViewHandle measureViewHandle;
	/**
	 * Action displayname
	 */
	private static final String ACTION_MSG_MERGE = "Delete MeasureHandle";

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.DeleteMeasureHandleAction"; //$NON-NLS-1$

	/**
	 * Trans name
	 */
	private static final String NAME = "add grandtotal";

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public DeleteMeasureHandleAction( DesignElementHandle handle )
	{
		super( handle );
		setId( ID );
		setText( ACTION_MSG_MERGE );
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle( handle );
		setHandle( extendedHandle );
		measureViewHandle = CrosstabAdaptUtil.getMeasureViewHandle( extendedHandle );
		
		ISharedImages shareImages = PlatformUI.getWorkbench( )
		.getSharedImages( );
		setImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return true;
	}

	private CrosstabReportItemHandle getCrosstabReportItemHandle(
			Object editpart )
	{
		return measureViewHandle.getCrosstab( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		CrosstabReportItemHandle reportItem = getCrosstabReportItemHandle( getHandle( ) );

		if ( reportItem != null )
		{
			transStar( NAME );
			
			try
			{
				reportItem.removeMeasure( measureViewHandle.getCubeMeasure( ).getQualifiedName( ) );
			}
			catch ( SemanticException e )
			{
				rollBack( );
				return;
			}
			transEnd( );
		}

	}
}
