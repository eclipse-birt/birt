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

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.LevelViewDialog;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.jface.window.Window;

/**
 * This is a test action.Add next level handle to the dimension handle.
 */
public class AddLevelHandleAction extends AbstractCrosstabAction
{

	DimensionViewHandle viewHandle = null;
	private static final String NAME = "add levelview handle";
	private static final String ID = "add_levelViewhandle";
	private static final String TEXT = "add levelview handle";

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public AddLevelHandleAction( DesignElementHandle handle )
	{
		super( handle );
		setId( ID );
		setText( TEXT );
		ExtendedItemHandle extendedHandle = getExtendedItemHandle( handle );
		setHandle( extendedHandle );
		viewHandle = getDimensionViewHandle( extendedHandle );
	}

	private DimensionViewHandle getDimensionViewHandle(
			ExtendedItemHandle extendedHandle )
	{
		AbstractCrosstabItemHandle handle = (AbstractCrosstabItemHandle) CrosstabUtil.getReportItem( extendedHandle );
		while ( handle != null )
		{

			if ( handle instanceof DimensionViewHandle )
			{
				return (DimensionViewHandle) handle;
			}
			handle = handle.getContainer( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		transStar( NAME );
		try
		{
			LevelViewDialog dialog = new LevelViewDialog(UIUtil.getDefaultShell( ));
			List showLevels = new ArrayList();
			int viewCount = viewHandle.getLevelCount( );
			for (int i=0; i<viewCount; i++)
			{
				LevelViewHandle levelHandle = viewHandle.getLevel( i );
				showLevels.add( levelHandle.getCubeLevel( ) );
			}
			dialog.setInput( viewHandle.getCubeDimension( ), showLevels );
			if ( dialog.open( ) == Window.OK )
			{
				List result = dialog.getResult( );
				if (!isDifferent( showLevels, result ))
				{
					return;
				}
				for (int i=viewCount-1; i>=0; i--)
				{
					viewHandle.removeLevel( i );
				}
				for (int i=0; i<result.size( ); i++)
				{
					LevelHandle tempHandle = (LevelHandle)result.get(i );
					insertLevelHandle( viewHandle, tempHandle );
				}
			}
		}
		catch ( SemanticException e )
		{
			rollBack( );
			return;
		}
		transEnd( );
	}

	private boolean isDifferent(List list1, List list2)
	{
		if (list1.size( ) != list2.size( ))
		{
			return true;
		}
		for (int i=0; i<list1.size( ); i++)
		{
			if (list1.get( i ) != list2.get( i ))
			{
				return true;
			}
		}
		return false;
	}
	
	private void insertLevelHandle( DimensionViewHandle handle,
			LevelHandle levelHandle ) throws SemanticException
	{

		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab( );
		int viewCount = viewHandle.getLevelCount( );
		LevelViewHandle levelViewHandle = viewHandle.insertLevel( levelHandle,
				viewCount );
		ComputedColumn bindingColumn = CrosstabAdaptUtil.createComputedColumn( (ExtendedItemHandle) reportHandle.getModelHandle( ),
				levelHandle );

		CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

		// TODO create a data bingding dataitem
		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( levelHandle.getName( ) );
		dataHandle.setResultSetColumn( bindingColumn.getName( ) );

		cellHandle.addContent( dataHandle );

	}

	private ExtendedItemHandle getExtendedItemHandle( DesignElementHandle handle )
	{
		// DesignElementHandle temp = handle;
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
}
