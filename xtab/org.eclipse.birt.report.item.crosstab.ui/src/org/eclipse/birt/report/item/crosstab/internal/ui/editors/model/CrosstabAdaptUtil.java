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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabQueryUtil;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;

/**
 * Util class
 */
public class CrosstabAdaptUtil
{

	/**
	 * Gets the row or column count after the handel, now include the current
	 * handle
	 * 
	 * @param handle
	 * @return
	 */
	public static int getNumberAfterDimensionViewHandle(
			DimensionViewHandle handle )
	{
		CrosstabReportItemHandle reportItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem( handle.getCrosstabHandle( ) );
		int index = handle.getIndex( );
		int type = handle.getAxisType( );
		int count = reportItem.getDimensionCount( type );
		int retValue = 0;
		for ( int i = index; i < count; i++ )
		{
			retValue = retValue
					+ reportItem.getDimension( type, index ).getLevelCount( );
		}
		return retValue;
	}

	/**
	 * @param owner
	 * @param levelHandle
	 * @return
	 */
	public static ComputedColumn createComputedColumn( ReportItemHandle owner,
			LevelHandle levelHandle )
	{
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( owner,
				levelHandle.getName( ) );

		// bindingColumn.setDataType( DesignChoiceConstants.
		// COLUMN_DATA_TYPE_ANY);
		bindingColumn.setDataType( levelHandle.getDataType( ) );
		bindingColumn.setExpression( DEUtil.getExpression( levelHandle ) );

		return bindingColumn;
	}

	/**
	 * @param owner
	 * @param measureHandle
	 * @return
	 */
	public static ComputedColumn createComputedColumn( ReportItemHandle owner,
			MeasureHandle measureHandle )
	{
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( owner,
				measureHandle.getName( ) );

		// bindingColumn.setDataType( DesignChoiceConstants.
		// COLUMN_DATA_TYPE_ANY);
		bindingColumn.setDataType( measureHandle.getDataType( ) );
		bindingColumn.setExpression( DEUtil.getExpression( measureHandle ) );

		return bindingColumn;
	}

	/**
	 * Find the position of the element. If the element is null, the position is
	 * last
	 * 
	 * @param parent
	 * @param element
	 * @return position
	 */
	public static int findInsertPosition( DesignElementHandle parent,
			DesignElementHandle element )
	{
		// if after is null, insert at last
		if ( element == null )
		{
			return parent.getContentCount( DEUtil.getDefaultContentName( parent ) );
		}
		// parent.findContentSlot( )

		return element.getIndex( );
	}

	public static ExtendedItemHandle getExtendedItemHandle(
			DesignElementHandle handle )
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

	/**
	 * @param extendedHandle
	 * @return
	 */
	public static LevelViewHandle getLevelViewHandle(
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

	/**
	 * @param extendedHandle
	 * @return
	 */
	public static DimensionViewHandle getDimensionViewHandle(
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

	public static DimensionHandle getDimensionHandle( LevelHandle levelHandle )
	{
		DesignElementHandle parent = levelHandle;
		while ( parent != null )
		{
			if ( parent instanceof DimensionHandle )
			{
				return (DimensionHandle) parent;
			}
			parent = parent.getContainer( );
		}
		return null;
	}

	public static CubeHandle getCubeHandle( DesignElementHandle levelHandle )
	{
		DesignElementHandle parent = levelHandle;
		while ( parent != null )
		{
			if ( parent instanceof CubeHandle )
			{
				return (CubeHandle) parent;
			}
			parent = parent.getContainer( );
		}
		return null;
	}

	/**
	 * @param extendedHandle
	 * @return
	 */
	public static MeasureViewHandle getMeasureViewHandle(
			ExtendedItemHandle extendedHandle )
	{
		AbstractCrosstabItemHandle handle = (AbstractCrosstabItemHandle) CrosstabUtil.getReportItem( extendedHandle );
		while ( handle != null )
		{
			if ( handle instanceof MeasureViewHandle )
			{
				return (MeasureViewHandle) handle;
			}
			handle = handle.getContainer( );
		}
		return null;
	}

	public static void processInvaildBindings( CrosstabReportItemHandle handle )
	{
		processInvaildBindings( handle, true );
	}

	public static void processInvaildBindings( CrosstabReportItemHandle handle,
			boolean isLevelRemoved )
	{
		if ( CrosstabPlugin.getDefault( )
				.getPluginPreferences( )
				.getBoolean( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS )
				&& isLevelRemoved )
		{
			MessageDialogWithToggle msgDlg = MessageDialogWithToggle.openYesNoQuestion( UIUtil.getDefaultShell( ),
					Messages.getString( "DeleteBindingDialog.Title" ), //$NON-NLS-1$
					Messages.getString( "DeleteBindingDialog.Message" ), //$NON-NLS-1$
					Messages.getString( "DeleteBindingDialog.ToggleMessage" ), //$NON-NLS-1$
					!CrosstabPlugin.getDefault( )
							.getPluginPreferences( )
							.getBoolean( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS ),
					null,
					null );
			if ( msgDlg.getReturnCode( ) == IDialogConstants.YES_ID )
			{
				removeInvalidBindings( handle );
			}
			else if ( msgDlg.getReturnCode( ) == IDialogConstants.NO_ID )
			{
				// dothing
			}
			if ( msgDlg.getReturnCode( ) == IDialogConstants.YES_ID
					|| msgDlg.getReturnCode( ) == IDialogConstants.NO_ID )
			{
				CrosstabPlugin.getDefault( )
						.getPluginPreferences( )
						.setValue( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS,
								!msgDlg.getToggleState( ) );
			}
		}
		else
		{
			removeInvalidBindings( handle );
		}
		// removeInvalidBindings(handle);
	}

	public static void removeInvalidBindings( CrosstabReportItemHandle handle )
	{
		try
		{
			ICubeQueryDefinition definition = CrosstabQueryUtil.createCubeQuery( handle,
					null,
					true,
					true,
					true,
					true,
					false,
					false );
			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			List list = session.getCubeQueryUtil( )
					.getInvalidBindings( definition );
			// for (int i=0; i<list.size( ); i++)
			// {
			// Object obj = list.get( i );
			// if (obj instanceof IBinding)
			// {
			// String name = ((IBinding)obj).getBindingName( );
			// ComputedColumnHandle delhandle =
			// ((ReportItemHandle)handle.getModelHandle( )).findColumnBinding(
			// name );
			// if (delhandle != null)
			// {
			// delhandle.drop( );
			// }
			// }
			// }
			( (ReportItemHandle) handle.getModelHandle( ) ).removedColumnBindings( list );
		}
		catch ( BirtException e )
		{
			// donothing
			e.printStackTrace( );
		}
	}
}
