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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.LevelViewDialog;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

/**
 * Add the level handle to the dimension handle.
 */
public class AddLevelHandleAction extends AbstractCrosstabAction
{

	DimensionViewHandle viewHandle = null;
	// private static final String NAME = "Show/Hide Group Levels";//$NON-NLS-1$
	private static final String ID = "add_levelViewhandle";//$NON-NLS-1$
	// private static final String TEXT = "Show/Hide Group Levels";//$NON-NLS-1$
	private static final String NAME = Messages.getString( "AddLevelHandleAction_TransName" );//$NON-NLS-1$
	private static final String TEXT = Messages.getString( "AddLevelHandleAction_Displayname" );//$NON-NLS-1$

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
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle( handle );
		setHandle( extendedHandle );
		viewHandle = CrosstabAdaptUtil.getDimensionViewHandle( extendedHandle );

		Image image = CrosstabUIHelper.getImage( CrosstabUIHelper.SHOW_HIDE_LEVEL );
		setImageDescriptor( ImageDescriptor.createFromImage( image ) );
	}
	
	public boolean isEnabled( )
	{
		return !DEUtil.isReferenceElement( viewHandle.getCrosstabHandle( ) );
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
			LevelViewDialog dialog = new LevelViewDialog( true );
			List showLevels = new ArrayList( );
			List nullLevelHandle = new ArrayList( );
			int viewCount = viewHandle.getLevelCount( );
			for ( int i = 0; i < viewCount; i++ )
			{
				LevelViewHandle levelHandle = viewHandle.getLevel( i );
				if ( levelHandle.getCubeLevel( ) == null )
				{
					nullLevelHandle.add( Integer.valueOf( levelHandle.getIndex( ) ) );
				}
				else
				{
					showLevels.add( levelHandle.getCubeLevel( ) );
				}
			}
			
			ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle( getHandle() );
			LevelViewHandle levelViewHandle = CrosstabAdaptUtil.getLevelViewHandle( extendedHandle );
			
			dialog.setInput( findDimension( levelViewHandle.getCubeLevel( ) ), showLevels );
			if ( dialog.open( ) == Window.OK )
			{
				CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab( );
				
				List result = (List) dialog.getResult( );
				boolean isLevelRemoved = processor( showLevels,
						result,
						nullLevelHandle, false);
				
				if (isLevelRemoved)
				{
					boolean bool = CrosstabAdaptUtil.needRemoveInvaildBindings( reportHandle );
					processor( showLevels,
							result,
							nullLevelHandle, true);
					if (bool)
					{
						CrosstabAdaptUtil.removeInvalidBindings( reportHandle );
					}
				}
				else
				{
					processor( showLevels, result, nullLevelHandle, true);
				}
				
				AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(reportHandle);
				providerWrapper.updateAllAggregationCells( AggregationCellViewAdapter.SWITCH_VIEW_TYPE );
				
				
			}
		}
		catch ( SemanticException e )
		{
			rollBack( );
			ExceptionUtil.handle( e );
			return;
		}
		transEnd( );
	}

	private DimensionHandle findDimension( DesignElementHandle element )
	{
		if(element == null )
		{
			return null;
		}
		
		if(element.getContainer( ) instanceof DimensionHandle )
		{
			return (DimensionHandle) element.getContainer( );
		}
		
		return findDimension(element.getContainer( ));
	}
	
	private boolean processor( List ori, List newList, List nullLevelHandle, boolean doChange )
			throws SemanticException
	{
		boolean isLevelRemoved = false;
		for ( int i = nullLevelHandle.size( ) - 1; i >= 0; i-- )
		{
			int index = ( (Integer) nullLevelHandle.get( i ) ).intValue( );
			if (doChange)
			{
				viewHandle.removeLevel( index );
			}
			isLevelRemoved = true;
		}

		for ( int i = 0; i < ori.size( ); i++ )
		{
			LevelHandle tempHandle = (LevelHandle) ori.get( i );
			if ( !newList.contains( tempHandle ) )
			{
				if (doChange)
				{
					viewHandle.removeLevel( tempHandle.getQualifiedName( ) );
				}
				isLevelRemoved = true;
			}
		}

		Collections.sort( newList, new LevelComparator( ) );
		if (doChange)
		{
			for ( int i = 0; i < newList.size( ); i++ )
			{
				LevelHandle tempHandle = (LevelHandle) newList.get( i );
				if ( viewHandle.getLevel( tempHandle.getQualifiedName( ) ) == null )
				{
					insertLevelHandle( tempHandle, i );
				}
			}
		}
		return isLevelRemoved;
	}

	private static class LevelComparator implements Comparator
	{

		public int compare( Object o1, Object o2 )
		{
			LevelHandle handle1 = (LevelHandle) o1;
			LevelHandle handle2 = (LevelHandle) o2;
			return handle1.getIndex( ) - handle2.getIndex( );
		}

	}

	private void insertLevelHandle( LevelHandle levelHandle, int pos )
			throws SemanticException
	{

		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab( );
		// int viewCount = viewHandle.getLevelCount( );

		DataItemHandle dataHandle = CrosstabAdaptUtil.createColumnBindingAndDataItem( (ExtendedItemHandle) reportHandle.getModelHandle( ),
				levelHandle );

		LevelViewHandle levelViewHandle = viewHandle.insertLevel( levelHandle,
				pos );
		CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

		cellHandle.addContent( dataHandle );
		
		CrosstabUtil.addLabelToHeader( levelViewHandle );

	}

}
