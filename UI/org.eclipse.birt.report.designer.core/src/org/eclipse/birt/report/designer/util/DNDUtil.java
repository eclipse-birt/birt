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

package org.eclipse.birt.report.designer.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.core.commands.PasteCommand;
import org.eclipse.birt.report.designer.core.commands.PasteStructureCommand;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.CellHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnBandData;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Useful utility in Drag and Drop or Copy and Paste.
 */

public class DNDUtil
{

	public static final String TYPE_CUT = "CUT"; //$NON-NLS-1$

	public static final String TYPE_COPY = "COPY"; //$NON-NLS-1$

	/** Target can't contain source */
	public static final int CONTAIN_NO = 0;

	/** Target can contain source */
	public static final int CONTAIN_THIS = 1;

	/** Target's parent can contain source */
	public static final int CONTAIN_PARENT = 2;

	/**
	 * Moves elements. Like paste, but transfer data will be deleted.Includes
	 * transaction.
	 * 
	 * @param transferData
	 *            single or multiple transfer data, every data must be an
	 *            instance of <code>DesignElementHandle</code> or
	 *            <code>DesignElement</code>
	 * @param container
	 *            container, class type could be
	 *            <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *            or <code>ReportElementModel</code>
	 * @param position
	 *            the position will be added
	 * @return move result
	 */
	public static boolean moveHandles( Object transferData, Object container,
			int position )
	{
		return operateHandles( transferData,
				container,
				position,
				Messages.getString( "DNDUtil.trans.cut" ), TYPE_CUT ); //$NON-NLS-1$
	}

	/**
	 * Pastes elements. Includes transaction.
	 * 
	 * @param transferData
	 *            single or multiple transfer data, every data must be an
	 *            instance of <code>DesignElementHandle</code> or
	 *            <code>DesignElement</code>
	 * @param container
	 *            container elements, class type could be
	 *            <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *            or <code>ReportElementModel</code>
	 * @param position
	 *            the position will be added
	 * @return paste result
	 */
	public static boolean copyHandles( Object transferData, Object container,
			int position )
	{
		return operateHandles( transferData,
				container,
				position,
				Messages.getString( "DNDUtil.trans.copy" ), TYPE_COPY ); //$NON-NLS-1$
	}

	/**
	 * Pastes elements. Includes transaction.
	 * 
	 * @param transferData
	 *            single or multiple transfer data, every data must be an
	 *            instance of <code>DesignElementHandle</code> or
	 *            <code>DesignElement</code>
	 * @param targetObj
	 *            container or sibling. Copy position is after the sibling or
	 *            the last in the container
	 * @return paste result
	 */
	public static boolean copyHandles( Object transferData, Object targetObj )
	{
		if ( getColumnHandle( transferData ) != null )
		{
			return copyColumn( getColumnHandle( transferData ),
					targetObj,
					false );
		}
		int canContain = handleValidateTargetCanContain( targetObj,
				transferData,
				true );
		int position = calculateNextPosition( targetObj, canContain );
		if ( position > -1 )
		{
			targetObj = getDesignElementHandle( targetObj ).getContainerSlotHandle( );
		}
		return copyHandles( transferData, targetObj, position );
	}

	/**
	 * Does insert and paste column to table/grid
	 * 
	 * @param transferData
	 *            copy data of column
	 * @param targetObj
	 *            target column of table/grid
	 * @return paste result
	 */
	public static boolean insertPasteColumn( Object transferData,
			Object targetObj )
	{
		if ( getColumnHandle( transferData ) != null )
		{
			return copyColumn( getColumnHandle( transferData ), targetObj, true );
		}
		return false;
	}

	protected static ColumnBandData getColumnHandle( Object transferData )
	{
		if ( transferData instanceof ColumnBandData )
		{
			return (ColumnBandData) transferData;
		}
		if ( transferData instanceof Object[]
				&& ( (Object[]) transferData ).length == 1 )
		{
			return getColumnHandle( ( (Object[]) transferData )[0] );
		}
		return null;
	}

	/**
	 * Pastes table or grid columns to target
	 * 
	 * @param transferData
	 *            column data
	 * @param targetObj
	 *            table
	 * @param isNew
	 *            true: insert and paste; false: override and paste
	 * @return paste succeed or fail
	 */
	protected static boolean copyColumn( ColumnBandData transferData,
			Object targetObj, boolean isNew )
	{
		try
		{
			int columnNumber = HandleAdapterFactory.getInstance( )
					.getColumnHandleAdapter( targetObj )
					.getColumnNumber( );
			Object parent = ( (ColumnHandle) targetObj ).getContainer( );
			if ( parent instanceof TableHandle )
			{
				if ( isNew )
				{
					( (TableHandle) parent ).insertAndPasteColumn( transferData.copy( ),
							columnNumber );
				}
				else
				{
					( (TableHandle) parent ).pasteColumn( transferData.copy( ),
							columnNumber,
							true );
				}
			}

			else if ( parent instanceof GridHandle )
			{
				if ( isNew )
				{
					( (GridHandle) parent ).insertAndPasteColumn( transferData.copy( ),
							columnNumber );
				}
				else
				{
					( (GridHandle) parent ).pasteColumn( transferData.copy( ),
							columnNumber,
							true );
				}
			}
		}
		catch ( SemanticException e )
		{
			return false;
		}
		return true;
	}

	/**
	 * Operates elements. Operation type includes move or copy. Includes
	 * transaction.
	 * 
	 * @param transferData
	 *            single or multiple transfer data, every data must be an
	 *            instance of <code>DesignElementHandle</code> or
	 *            <code>DesignElement</code>
	 * @param targetObj
	 *            target elements, class type could be
	 *            <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *            or <code>ReportElementModel</code>
	 * @param position
	 *            the position will be added
	 * @param commandName
	 * @param commandType
	 *            TYPE_CUT or TYPE_COPY
	 * @return if succeeding in operating data
	 */
	protected static boolean operateHandles( Object transferData,
			Object targetObj, int position, String commandName,
			String commandType )
	{
		ModuleHandle designHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
		CommandStack stack = designHandle.getCommandStack( );
		try
		{
			stack.startTrans( commandName );
			CompoundCommand commands = new CompoundCommand( );

			if ( transferData instanceof StructuredSelection )
			{
				transferData = ( (StructuredSelection) transferData ).toArray( );
			}

			addCommandToCompound( transferData,
					targetObj,
					position,
					commandName,
					commandType,
					commands );

			commands.execute( );
			stack.commit( );
		}
		catch ( Exception e )
		{
			stack.rollbackAll( );
			return false;
		}
		return true;
	}

	protected static void addCommandToCompound( Object transferData,
			Object targetObj, int position, String commandName,
			String commandType, CompoundCommand commands )
	{
		if ( transferData instanceof SlotHandle )
		{
			transferData = transferSlotHandle( commandType, transferData );
		}

		if ( transferData instanceof Object[] )
		{
			Object[] array = (Object[]) transferData;
			for ( int i = 0; i < array.length; i++ )
			{
				addCommandToCompound( array[i],
						targetObj,
						position,
						commandName,
						commandType,
						commands );
				if ( position > -1 )
				{
					position++;
				}
			}
		}

		else if ( transferData instanceof DesignElementHandle
				|| transferData instanceof IDesignElement )
		{
			if ( targetObj instanceof ReportElementModel
					|| targetObj instanceof DesignElementHandle
					|| targetObj instanceof SlotHandle )
			{
				commands.add( getNewCommand( commandType,
						transferData,
						targetObj,
						position ) );
			}
		}
		else if ( transferData instanceof IStructure )
		{
			commands.add( new PasteStructureCommand( (IStructure) transferData,
					targetObj ) );
		}
	}

	/**
	 * Returns specified command
	 * 
	 * @param commandType
	 *            command type, value is <code>TYPE_CUT</code> or
	 *            <code>TYPE_COPY</code>
	 * @param transferSource
	 *            transfer source
	 * @param newContainer
	 *            the new container
	 * @param position
	 *            the position will be added
	 * @return command
	 */
	protected static Command getNewCommand( String commandType,
			Object transferSource, Object newContainer, int position )
	{
		boolean isCut = TYPE_CUT.equals( commandType );

		if ( newContainer instanceof ParameterGroupHandle
				&& ( transferSource instanceof ParameterGroupHandle || DEUtil.isParameterGroup( transferSource ) ) )
		{
			return pasteParameterGroup( commandType,
					transferSource,
					(ParameterGroupHandle) newContainer );
		}
		else if ( transferSource instanceof DesignElementHandle )
		{
			return new PasteCommand( (DesignElementHandle) transferSource,
					newContainer,
					position,
					isCut );
		}
		else if ( transferSource instanceof IDesignElement )
		{
			return new PasteCommand( (IDesignElement) transferSource,
					newContainer,
					position );
		}
		return null;
	}

	protected static Command pasteParameterGroup( String commandType,
			Object childGroup, ParameterGroupHandle targetGroup )
	{
		CompoundCommand commands = new CompoundCommand( );
		ParameterGroupHandle childHandle = null;
		if ( childGroup instanceof ParameterGroupHandle )
		{
			childHandle = (ParameterGroupHandle) childGroup;
		}
		else if ( DEUtil.isParameterGroup( childGroup ) )
		{
			childHandle = (ParameterGroupHandle) ( (IDesignElement) childGroup ).getHandle( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getModule( ) );
		}

		if ( childHandle != null )
		{
			SlotHandle transferSlot = childHandle.getParameters( );
			for ( Iterator i = transferSlot.iterator( ); i.hasNext( ); )
			{
				commands.add( getNewCommand( commandType,
						i.next( ),
						targetGroup,
						-1 ) );
			}
		}
		return commands;
	}

	protected static Object transferSlotHandle( String commandType,
			Object handle )
	{
		Object cloneObj = cloneSource( handle );
		if ( TYPE_CUT.equals( commandType ) )
		{
			dropSource( handle );
		}
		return cloneObj;
	}

	/**
	 * Validates selection can be dragged, cut or copied
	 * 
	 * @param selection
	 *            selected object, support single or multiple selection
	 * @return if selection can be dragged, cut or copied
	 */
	public static boolean handleValidateDragInOutline( Object selection )
	{
		if ( selection instanceof StructuredSelection )
		{
			return handleValidateDragInOutline( ( (StructuredSelection) selection ).toArray( ) );
		}
		if ( selection instanceof Object[] )
		{
			Object[] array = (Object[]) selection;
			if ( array.length == 0 )
			{
				return false;
			}

			if ( array[0] instanceof ColumnHandle )
			{
				boolean bool = false;
				int columnNumber = HandleAdapterFactory.getInstance( )
						.getColumnHandleAdapter( array[0] )
						.getColumnNumber( );
				Object parent = ( (ColumnHandle) array[0] ).getContainer( );
				if ( parent instanceof TableHandle )
				{
					bool = ( (TableHandle) parent ).canCopyColumn( columnNumber );
				}
				else if ( parent instanceof GridHandle )
				{
					bool = ( (GridHandle) parent ).canCopyColumn( columnNumber );
				}
				if ( bool && array.length == 1 )
				{
					return true;
				}
				if ( bool && array[1] instanceof CellHandle )
				{
					return true;
				}
				return false;
			}

			for ( int i = 0; i < array.length; i++ )
			{
				if ( checkContainerExists( array[i], array ) )
					continue;
				if ( !handleValidateDragInOutline( array[i] ) )
					return false;
			}
			return true;
		}
		if ( selection instanceof ReportElementModel )
		{
			return handleValidateDragInOutline( ( (ReportElementModel) selection ).getSlotHandle( ) );
		}
		if ( selection instanceof SlotHandle )
		{
			SlotHandle slot = (SlotHandle) selection;
			DesignElementHandle handle = slot.getElementHandle( );
			return slot.getContents( ).size( ) > 0
					&& ( handle instanceof ListHandle || handle instanceof ListGroupHandle );
		}
		if ( selection instanceof ColumnHandle )
		{
			int columnNumber = HandleAdapterFactory.getInstance( )
					.getColumnHandleAdapter( selection )
					.getColumnNumber( );
			Object parent = ( (ColumnHandle) selection ).getContainer( );
			if ( parent instanceof TableHandle )
			{
				return ( (TableHandle) parent ).canCopyColumn( columnNumber );
			}
			else if ( parent instanceof GridHandle )
			{
				return ( (GridHandle) parent ).canCopyColumn( columnNumber );
			}
		}
		return selection instanceof ReportItemHandle
				|| selection instanceof DataSetHandle
				|| selection instanceof DataSourceHandle
				|| selection instanceof ScalarParameterHandle
				|| selection instanceof ParameterGroupHandle
				// || selection instanceof RowHandle
				|| selection instanceof GroupHandle
				|| selection instanceof StyleHandle
				|| selection instanceof EmbeddedImageHandle;
	}

	/**
	 * Gets a copy of source data. If copy multi-selection, skip all children's
	 * clone
	 * 
	 * @param source
	 *            source to clone
	 * @return copy of source
	 */
	public static Object cloneSource( Object source )
	{
		source = unwrapToModel( source );
		if ( source instanceof Object[] )
		{
			Object[] array = (Object[]) source;
			ArrayList list = new ArrayList( );
			for ( int i = 0; i < array.length; i++ )
			{
				if ( array[i] instanceof ColumnHandle )
				{
					list.add( cloneSource( array[i] ) );
				}
			}
			if ( !list.isEmpty( ) )
			{
				return list.toArray( );
			}
			for ( int i = 0; i < array.length; i++ )
			{
				// Skips child's clone
				if ( !checkContainerExists( array[i], array ) )
				{
					list.add( cloneSource( array[i] ) );
				}
			}
			return list.toArray( );
		}
		if ( source instanceof StructuredSelection )
		{
			return cloneSource( ( (StructuredSelection) source ).toArray( ) );
		}
		if ( source instanceof ColumnHandle )
		{
			try
			{
				int columnNumber = HandleAdapterFactory.getInstance( )
						.getColumnHandleAdapter( source )
						.getColumnNumber( );
				Object parent = ( (ColumnHandle) source ).getContainer( );
				if ( parent instanceof TableHandle )
				{

					return ( (TableHandle) parent ).copyColumn( columnNumber );
				}
				else if ( parent instanceof GridHandle )
				{
					return ( (GridHandle) parent ).copyColumn( columnNumber );
				}
			}
			catch ( SemanticException e )
			{
				e.printStackTrace( );
			}
			return null;
		}
		if ( source instanceof DesignElementHandle )
		{
			IDesignElement copyElement = ( (DesignElementHandle) source ).copy( );
			return copyElement;
		}
		if ( source instanceof IDesignElement )
		{
			try
			{
				return ( (IDesignElement) source ).clone( );
			}
			catch ( CloneNotSupportedException e )
			{
				e.printStackTrace( );
			}
		}
		if ( source instanceof SlotHandle )
		{
			SlotHandle slot = (SlotHandle) source;
			Object[] array = new Object[slot.getCount( )];
			for ( int i = 0; i < array.length; i++ )
			{
				array[i] = slot.get( i );
			}
			return cloneSource( array );
		}
		if ( source instanceof StructureHandle )
		{
			return ( (StructureHandle) source ).getStructure( ).copy( );
		}
		return null;
	}

	/**
	 * Drops source data
	 * 
	 * @param source
	 *            source to drop
	 */
	public static void dropSource( Object source )
	{
		DeleteCommand command = new DeleteCommand( source );
		if ( command.canExecute( ) )
			command.execute( );
	}

	/**
	 * Gets handle of target
	 * 
	 * @param target
	 * @return handle of target
	 */
	public static DesignElementHandle getDesignElementHandle( Object target )
	{
		target = unwrapToModel( target );
		if ( target instanceof DesignElementHandle )
		{
			return (DesignElementHandle) target;
		}
		else if ( target instanceof SlotHandle )
		{
			return ( (SlotHandle) target ).getElementHandle( );
		}
		return null;
	}

	/**
	 * Unwraps the object to model
	 * 
	 * @param obj
	 *            object which may be wrapped, such as
	 *            <code>ReportElementModel</code>, <code>ListBandProxy</code>
	 * @return model object
	 */
	public static Object unwrapToModel( Object obj )
	{
		if ( obj instanceof ReportElementModel )
		{
			return ( (ReportElementModel) obj ).getSlotHandle( );
		}
		if ( obj instanceof ListBandProxy )
		{
			return ( (ListBandProxy) obj ).getSlotHandle( );
		}
		return obj;
	}

	/**
	 * Gets the length of elements in object
	 * 
	 * @param obj
	 * @return the length of elements in object
	 */
	public static int getObjectLength( Object obj )
	{
		if ( obj == null )
		{
			return 0;
		}
		else if ( obj instanceof StructuredSelection )
		{
			return getObjectLength( ( (StructuredSelection) obj ).toArray( ) );
		}
		else if ( obj instanceof Object[] )
		{
			return ( (Object[]) obj ).length;
		}
		return 1;
	}

	/**
	 * Checks whether child's container exists in handle array
	 * 
	 * @param content
	 *            child handle
	 * @param handles
	 *            handle array
	 * @return if exists
	 */
	public static boolean checkContainerExists( Object content, Object[] handles )
	{
		content = unwrapToModel( content );
		DesignElementHandle child = null;
		if ( content instanceof SlotHandle )
		{
			child = ( (SlotHandle) content ).getElementHandle( );
		}
		else if ( content instanceof DesignElementHandle )
		{
			child = (DesignElementHandle) content;
		}
		if ( child != null )
		{
			for ( int i = 0; i < handles.length; i++ )
			{
				if ( content == handles[i] )
				{
					continue;
				}

				// Test slot's elementhandle is the container
				if ( child == handles[i] )
				{
					return true;
				}
				if ( handles[i] instanceof DesignElementHandle )
				{
					// Consider special case: columnhandle
					if ( child instanceof CellHandle
							&& handles[i] instanceof ColumnHandle )
					{
						if ( isInSameColumn( new Object[]{
								child, handles[i]
						} ) )
						{
							return true;
						}
						continue;
					}

					// Test parent or grandparent is some elementhandle
					DesignElementHandle container = child.getContainer( );
					while ( container != null )
					{
						if ( container.equals( handles[i] ) )
						{
							return true;
						}
						container = container.getContainer( );
					}
				}
				else if ( handles[i] instanceof SlotHandle )
				{
					// Test container is slothandle
					if ( child.getContainerSlotHandle( ) == handles[i] )
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets the position after the sibling in the container
	 * 
	 * @param targetObj
	 *            container or sibling
	 * @param canContain
	 *            <code>CONTAIN_PARENT</code> as sibling, others as container
	 * @return the position: after the sibling in the same container, or -1 as
	 *         the last position in the container
	 */
	public static int calculateNextPosition( Object targetObj, int canContain )
	{
		int position = -1;
		if ( canContain == CONTAIN_PARENT )
		{
			DesignElementHandle afterHandle = getDesignElementHandle( targetObj );
			if ( afterHandle != null )
			{
				position = afterHandle.getContainerSlotHandle( )
						.findPosn( afterHandle );
				position++;
			}
		}
		return position;
	}

	/**
	 * Adds new object to container
	 * 
	 * @param container
	 *            container, not null
	 * @param handle
	 *            new object. If new object is null, create nothing
	 * @throws SemanticException
	 */
	public static void addElementHandle( Object container,
			DesignElementHandle handle ) throws SemanticException
	{
		assert ( container != null );
		if ( handle == null )
		{
			return;
		}
		container = unwrapToModel( container );
		if ( container instanceof DesignElementHandle )
		{
			( (DesignElementHandle) container ).addElement( handle,
					DEUtil.getDefaultSlotID( container ) );
		}
		else if ( container instanceof SlotHandle )
		{
			( (SlotHandle) container ).add( handle );
		}
	}

	/**
	 * Validates target elements can contain transfer data
	 * 
	 * @param targetObj
	 *            target elements
	 * @param transferData
	 *            transfer data,single object or array are permitted
	 * @return if target elements can be dropped
	 */
	public static boolean handleValidateTargetCanContain( Object targetObj,
			Object transferData )
	{
		return handleValidateTargetCanContain( targetObj, transferData, true ) != CONTAIN_NO;
	}

	/**
	 * Validates target elements can contain transfer data.
	 * <p>
	 * If transfer data is single element, validate target's container also
	 * 
	 * @param targetObj
	 *            target elements
	 * @param transferData
	 *            transfer data,single object or array are permitted
	 * @param validateContainer
	 *            validate target's container can contain
	 * @return If target elements can't be dropped, return CONTAIN_NO.
	 *         <p>
	 *         If target elements can be dropped, return CONTAIN_THIS.
	 *         <p>
	 *         If target's container can be dropped, return CONTAIN_PARENT
	 */
	public static int handleValidateTargetCanContain( Object targetObj,
			Object transferData, boolean validateContainer )
	{
		if ( targetObj == null || transferData == null )
			return CONTAIN_NO;

		if ( transferData instanceof StructuredSelection )
		{
			return handleValidateTargetCanContain( targetObj,
					( (StructuredSelection) transferData ).toArray( ),
					validateContainer );
		}
		else if ( transferData instanceof Object[] )
		{
			Object[] array = (Object[]) transferData;
			if ( array.length == 1 )
			{
				return handleValidateTargetCanContain( targetObj,
						array[0],
						validateContainer );
			}
			int canContainAll = CONTAIN_NO;
			for ( int i = 0; i < array.length; i++ )
			{
				int canContain = handleValidateTargetCanContain( targetObj,
						array[i],
						validateContainer );
				if ( i == 0 )
				{
					canContainAll = canContain;
				}
				if ( canContain == CONTAIN_NO || canContain != canContainAll )
				{
					return CONTAIN_NO;
				}
			}
			return canContainAll;
		}
		else
		{
			// Gets handle to test if can contain
			if ( transferData instanceof DesignElementHandle )
			{
				return handleValidateTargetCanContainByContainer( targetObj,
						(DesignElementHandle) transferData,
						validateContainer );
			}
			else if ( transferData instanceof ColumnBandData )
			{
				if ( targetObj instanceof ColumnHandle )
				{
					return handleValidateContainColumnPaste( (ColumnHandle) targetObj,
							(ColumnBandData) transferData,
							false ) ? CONTAIN_PARENT : CONTAIN_NO;
				}
				return CONTAIN_NO;
			}
			else if ( transferData instanceof IDesignElement )
			{
				DesignElementHandle childHandle = ( (IDesignElement) transferData ).getHandle( SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getModule( ) );
				return handleValidateTargetCanContainByContainer( targetObj,
						childHandle,
						validateContainer );
			}
			else if ( transferData instanceof SlotHandle )
			{
				SlotHandle slot = (SlotHandle) transferData;
				Object[] childHandles = slot.getContents( ).toArray( );
				return handleValidateTargetCanContainByContainer( targetObj,
						childHandles,
						validateContainer );
			}
			else if ( transferData instanceof IStructure )
			{
				return handleValidateTargetCanContainStructure( targetObj,
						(IStructure) transferData ) ? CONTAIN_THIS : CONTAIN_NO;
			}
			else
			{
				return CONTAIN_NO;
			}
		}
	}

	public static boolean handleValidateTargetCanContainStructure(
			Object targetObj, IStructure transferData )
	{
		if ( targetObj instanceof EmbeddedImageNode )
		{
			targetObj = ( (EmbeddedImageNode) targetObj ).getReportDesignHandle( );
		}
		if ( transferData instanceof EmbeddedImage
				&& targetObj instanceof ModuleHandle )
		{
			// return ( (ModuleHandle) targetObj ).findImage( ( (EmbeddedImage)
			// transferData ).getName( ) ) == null;
			return true;
		}
		return false;
	}

	/**
	 * Validates target column can paste another column.
	 * 
	 * @param targetObj
	 *            target table/grid column
	 * @param transferData
	 *            copy data of table/grid column
	 * @param isNew
	 *            true: insert and paste; false: override and paste
	 * @return can paste
	 */
	public static boolean handleValidateContainColumnPaste(
			ColumnHandle targetObj, ColumnBandData transferData, boolean isNew )
	{
		int columnNumber = HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( targetObj )
				.getColumnNumber( );
		Object parent = targetObj.getContainer( );
		if ( parent instanceof TableHandle )
		{
			if ( isNew )
			{
				return ( (TableHandle) parent ).canInsertAndPasteColumn( transferData,
						columnNumber );
			}
			return ( (TableHandle) parent ).canPasteColumn( transferData,
					columnNumber,
					true );
		}
		else if ( parent instanceof GridHandle )
		{
			if ( isNew )
			{
				return ( (GridHandle) parent ).canInsertAndPasteColumn( transferData,
						columnNumber );
			}
			return ( (GridHandle) parent ).canPasteColumn( transferData,
					columnNumber,
					true );
		}
		return false;
	}

	protected static int handleValidateTargetCanContainByContainer(
			Object targetObj, DesignElementHandle childHandle,
			boolean validateContainer )
	{
		targetObj = unwrapToModel( targetObj );
		if ( targetObj instanceof DesignElementHandle )
		{
			return handleValidateTargetCanContainElementHandle( (DesignElementHandle) targetObj,
					childHandle,
					validateContainer );
		}
		if ( targetObj instanceof SlotHandle )
		{
			SlotHandle targetHandle = (SlotHandle) targetObj;
			return targetHandle.getElementHandle( )
					.canContain( targetHandle.getSlotID( ), childHandle )
					? CONTAIN_THIS : CONTAIN_NO;
		}
		return CONTAIN_NO;
	}

	protected static int handleValidateTargetCanContainByContainer(
			Object targetObj, Object[] childHandles, boolean validateContainer )
	{
		if ( childHandles.length == 0 )
		{
			return CONTAIN_NO;
		}
		for ( int i = 0; i < childHandles.length; i++ )
		{
			if ( !( childHandles[i] instanceof DesignElementHandle )
					|| handleValidateTargetCanContainByContainer( targetObj,
							(DesignElementHandle) childHandles[i],
							validateContainer ) == CONTAIN_NO )
			{
				return CONTAIN_NO;
			}
		}
		return CONTAIN_THIS;
	}

	protected static int handleValidateTargetCanContainElementHandle(
			DesignElementHandle targetHandle, DesignElementHandle childHandle,
			boolean validateContainer )
	{
		if ( targetHandle instanceof CascadingParameterGroupHandle )
		{
			return childHandle.getContainer( ) == targetHandle ? CONTAIN_THIS
					: CONTAIN_NO;
		}
		else if ( targetHandle.canContain( DEUtil.getDefaultSlotID( targetHandle ),
				childHandle ) )
		{
			return CONTAIN_THIS;
		}
		else if ( targetHandle instanceof ParameterGroupHandle
				&& childHandle instanceof ParameterGroupHandle )
		{
			return CONTAIN_THIS;
		}
		// else if ( targetHandle instanceof RowHandle
		// && childHandle instanceof RowHandle )
		// {
		// RowHandleAdapter adapter = HandleAdapterFactory.getInstance( )
		// .getRowHandleAdapter( childHandle );
		// return adapter.canPaste( targetHandle ) ? CONTAIN_PARENT
		// : CONTAIN_NO;
		// }
		else if ( validateContainer )
		// Validates target's container
		{
			if ( targetHandle.getContainer( ) == null )
			{
				return CONTAIN_NO;
			}
			if ( !targetHandle.getContainer( )
					.getDefn( )
					.getSlot( targetHandle.getContainerSlotHandle( )
							.getSlotID( ) )
					.isMultipleCardinality( ) )
			{
				// If only can contain single
				return CONTAIN_NO;
			}
			if ( targetHandle.getClass( ).equals( childHandle.getClass( ) ) )
			{
				// If class type is same
				return CONTAIN_PARENT;
			}
			return targetHandle.getContainer( )
					.canContain( targetHandle.getContainerSlotHandle( )
							.getSlotID( ),
							childHandle ) ? CONTAIN_PARENT : CONTAIN_NO;
		}
		return CONTAIN_NO;
	}

	/**
	 * Validates target can contain more elements
	 * 
	 * @param targetObj
	 *            target
	 * @param length
	 *            the length of elements in source.If do not add to target, set
	 *            zero
	 * @return whether target can contain more elements
	 */
	public static boolean handleValidateTargetCanContainMore( Object targetObj,
			int length )
	{
		if ( targetObj == null || length < 0 )
		{
			return false;
		}
		if ( targetObj instanceof StructuredSelection )
		{
			return handleValidateTargetCanContainMore( ( (StructuredSelection) targetObj ).toArray( ),
					length );
		}
		if ( targetObj instanceof Object[] )
		{
			Object[] array = (Object[]) targetObj;
			for ( int i = 0; i < array.length; i++ )
			{
				if ( !handleValidateTargetCanContainMore( array[i], length ) )
				{
					return false;
				}
			}
			return true;
		}
		targetObj = unwrapToModel( targetObj );
		if ( targetObj instanceof SlotHandle )
		{
			SlotHandle slot = (SlotHandle) targetObj;
			return slot.getElementHandle( )
					.getDefn( )
					.getSlot( slot.getSlotID( ) )
					.isMultipleCardinality( )
					|| slot.getCount( ) < 1 && length <= 1;
		}
		return targetObj instanceof DesignElementHandle
				|| targetObj instanceof EmbeddedImageNode;
	}

	/**
	 * Validates target elements can contain specified type of transfer data
	 * 
	 * @param targetObj
	 *            target elements
	 * @param dragObjType
	 *            specified type of transfer data. Type should get from
	 *            <code>ReportDesignConstants</code>
	 * @see ReportDesignConstants
	 * @return if target elements can be dropped
	 */
	public static boolean handleValidateTargetCanContainType( Object targetObj,
			String dragObjType )
	{
		DesignElementHandle targetHandle = null;
		int slotId = 0;
		targetObj = unwrapToModel( targetObj );
		if ( targetObj instanceof DesignElementHandle )
		{
			targetHandle = (DesignElementHandle) targetObj;
			slotId = DEUtil.getDefaultSlotID( targetObj );
		}
		else if ( targetObj instanceof SlotHandle )
		{
			targetHandle = ( (SlotHandle) targetObj ).getElementHandle( );
			slotId = ( (SlotHandle) targetObj ).getSlotID( );
		}
		else
			return false;

		return targetHandle.canContain( slotId, dragObjType );
	}

	/**
	 * Returns the table or grid parent
	 * 
	 * @param handle
	 *            the child of the table or grid
	 */
	public static Object getTableParent( DesignElementHandle handle )
	{
		while ( handle != null )
		{
			if ( handle instanceof TableHandle )
			{
				return ( (TableHandle) handle );
			}
			if ( handle instanceof GridHandle )
			{
				return ( (GridHandle) handle );
			}
			handle = handle.getContainer( );
		}
		return null;
	}

	/**
	 * Returns if all objects are in the same column
	 * 
	 * @param objs
	 *            the array of the object
	 */
	public static boolean isInSameColumn( Object[] objs )
	{
		assert objs != null && objs.length > 1;
		final class ColumnPosition
		{

			int columnNumber;
			Object parent;

			ColumnPosition( Object obj )
			{
				if ( obj instanceof ColumnHandle )
				{
					ColumnHandleAdapter columnAdapter = HandleAdapterFactory.getInstance( )
							.getColumnHandleAdapter( obj );
					columnNumber = columnAdapter.getColumnNumber( );
					parent = getTableParent( (DesignElementHandle) obj );
				}
				else if ( obj instanceof CellHandle )
				{
					CellHandleAdapter cellAdapter = HandleAdapterFactory.getInstance( )
							.getCellHandleAdapter( obj );
					columnNumber = cellAdapter.getColumnNumber( );
					parent = getTableParent( (DesignElementHandle) obj );
				}
			}
		}
		ColumnPosition position = null;
		for ( int i = 0; i < objs.length; i++ )
		{
			ColumnPosition newPosi = new ColumnPosition( objs[i] );
			if ( position == null )
			{
				position = newPosi;
			}
			else if ( position.columnNumber != newPosi.columnNumber
					|| position.parent != newPosi.parent )
			{
				return false;
			}
		}
		return true;
	}
}