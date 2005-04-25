/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ImageBuilderDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.NewSectionDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.TableOptionDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDUtil;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CutAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertInLayoutAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PasteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RenameAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.PageSetAction.CodePageAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchImages;

/**
 * Default node provider. This class is the base class for other providers
 * 
 *  
 */
public class DefaultNodeProvider implements INodeProvider
{

	public static final String BODY = Messages.getString( "DefaultNodeProvider.Tree.Body" ); //$NON-NLS-1$

	public static final String PAGESETUP = Messages.getString( "DefaultNodeProvider.Tree.PageSetup" ); //$NON-NLS-1$

	public static final String DATASOURCES = Messages.getString( "DefaultNodeProvider.Tree.DataSources" ); //$NON-NLS-1$

	public static final String DATASETS = Messages.getString( "DefaultNodeProvider.Tree.DataSets" ); //$NON-NLS-1$

	public static final String STYLES = Messages.getString( "DefaultNodeProvider.Tree.Styles" ); //$NON-NLS-1$
	
	public static final String IMAGES = Messages.getString( "DefaultNodeProvider.Tree.Images" ); //$NON-NLS-1$

	public static final String PARAMETERS = Messages.getString( "DefaultNodeProvider.Tree.Parameters" ); //$NON-NLS-1$

	public static final String SCRATCHPAD = Messages.getString( "DefaultNodeProvider.Tree.Scratch" ); //$NON-NLS-1$

	public static final String MASTERPAGE = Messages.getString( "DefaultNodeProvider.Tree.MasterPages" ); //$NON-NLS-1$

	public static final String COLUMNHEADING_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.ColumnHedings" ); //$NON-NLS-1$

	public static final String DETAIL_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.Detail" ); //$NON-NLS-1$

	public static final String HEADER_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.Header" ); //$NON-NLS-1$

	public static final String FOOTER_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.Footer" ); //$NON-NLS-1$

	public static final String GROUPS_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.Groups" ); //$NON-NLS-1$

	public static final String MISSINGNAME = Messages.getString( "DefaultNodeProvider.Tree.Invalid" ); //$NON-NLS-1$

	/**
	 * Creates the context menu
	 * 
	 * @param sourceViewer
	 *            the source viewer
	 * @param object
	 *            the object
	 * @param menu
	 *            the menu
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		menu.add( new Separator( ) );

		InsertInLayoutAction insertAction = new InsertInLayoutAction( object );
		if ( insertAction.isTypeAvailable( ) )
		{
			menu.add( insertAction );
		}

		menu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );
		// Rename action
		RenameAction renameAction = new RenameAction( sourceViewer );
		if ( renameAction.isEnabled( ) )
		{//if can rename,add to menu
			menu.add( renameAction );
		}
		// Delete action
		DeleteAction deleteAction = new DeleteAction( object );
		if ( deleteAction.isEnabled( ) )
		{//if can delete,add to menu
			menu.add( deleteAction );
		}

		CutAction cutAction = new CutAction( object );
		if ( cutAction.isEnabled( ) )
			menu.add( cutAction );

		CopyAction copyAction = new CopyAction( object );
		if ( copyAction.isEnabled( ) )
			menu.add( copyAction );

		menu.add( new PasteAction( object ) );

		//Insert point for refresh action
		menu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS
				+ "-refresh" ) );//$NON-NLS-1$

		Action action = new CodePageAction( object );
		if ( action.isEnabled( ) )
			menu.add( action );
			
		menu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS
				+ "-end" ) );//$NON-NLS-1$		
	}

	/**
	 * Gets the display name of the node.
	 * 
	 * @param model
	 *            the object
	 */
	public String getNodeDisplayName( Object model )
	{
		return DEUtil.getDisplayLabel( model );
	}

	/**
	 * Gets the children element of the given model using visitor.
	 * 
	 * @param model
	 *            the model
	 */
	public Object[] getChildren( Object model )
	{

		if ( model instanceof ReportElementModel )
		{
			if ( ( (ReportElementModel) model ).getSlotHandle( ) != null )
			{
				return this.getChildrenBySlotHandle( ( (ReportElementModel) model ).getSlotHandle( ) );
			}
		}
		return new Object[]{};
	}

	public Object getParent( Object model )
	{
		if ( model instanceof ReportElementModel )
		{
			return ( (ReportElementModel) model ).getElementHandle( );
		}
		else if ( model instanceof ReportElementHandle )
		{
			ReportElementHandle handle = (ReportElementHandle) model;
			if ( handle instanceof CellHandle
					|| handle.getContainer( ) instanceof CellHandle
					|| handle.getContainer( ) instanceof ParameterGroupHandle
					|| ( handle instanceof RowHandle && handle.getContainer( ) instanceof GridHandle ) )
			{
				return handle.getContainer( );
			}
			if ( handle.getContainerSlotHandle( ) != null )
			{
				return new ReportElementModel( handle.getContainerSlotHandle( ) );
			}
		}
		return null;
	}

	/**
	 * Gets the icon image for the given model.
	 * 
	 * @param model
	 *            the model of the node
	 * 
	 * @return Returns the icon name for the model,or null if no proper one
	 *         available for the given model
	 */
	public Image getNodeIcon( Object model )
	{
		Image icon = null;
		String iconName = getIconName( model );

		if(model instanceof DesignElementHandle &&
				((DesignElementHandle)model).getValidationErrors().size()>0)
		{
			return WorkbenchImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );
		}
		if ( iconName != null )
		{//if the getIconName is defined
			icon = ReportPlatformUIImages.getImage( iconName );
		}
		if ( icon == null )
		{
			if ( model instanceof DesignElementHandle )
			{
				icon = ReportPlatformUIImages.getImage( model );
			}
		}
		return icon;
	}

	/**
	 * Gets the icon name for the given model. The default implementation does
	 * nothing.The subclasses may override it if necessary
	 * 
	 * @param model
	 *            the model of the node
	 * 
	 * @return Returns the icon name for the model,or null if no proper one
	 *         available for the given model
	 */
	public String getIconName( Object model )
	{//Do nothing
		return null;
	}

	protected Object[] getChildrenBySlotHandle( SlotHandle slotHandle )
	{
		ArrayList list = new ArrayList( );
		Iterator itor = slotHandle.iterator( );
		while ( itor.hasNext( ) )
		{
			Object obj = itor.next( );
			if ( obj instanceof DesignElementHandle )
			{
				DesignElementHandle eleHandle = (DesignElementHandle) obj;
				list.add( eleHandle );
			}
		}

		return list.toArray( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getCommand(org.eclipse.gef.Request)
	 */
	public boolean performRequest( Object model, Request request )
			throws Exception
	{
		if ( request.getType( ).equals( IRequestConstants.REQUEST_TYPE_INSERT ) )
		{
			Map extendsData = request.getExtendedData( );
			SlotHandle slotHandle = (SlotHandle) extendsData.get( IRequestConstants.REQUEST_KEY_INSERT_SLOT );
			String type = (String) extendsData.get( IRequestConstants.REQUEST_KEY_INSERT_TYPE );
			String position = (String) extendsData.get( IRequestConstants.REQUEST_KEY_INSERT_POSITION );
			return performInsert( model,
					slotHandle,
					type,
					position,
					extendsData );
		}
		if ( request.getType( ).equals( IRequestConstants.REQUEST_TYPE_EDIT ) )
		{
			return performEdit( (ReportElementHandle) model );
		}
		if ( request.getType( ).equals( IRequestConstants.REQUEST_TYPE_DELETE ) )
		{
			return false;
		}
		return false;
	}

	protected DesignElementHandle createElement( String type ) throws Exception
	{
		ElementFactory factory = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getElementFactory( );
		if ( ReportDesignConstants.TABLE_ITEM.equals( type ) )
		{
			TableOptionDialog dlg = new TableOptionDialog( UIUtil.getDefaultShell( ),
					true );
			if ( dlg.open( ) == Window.OK && dlg.getResult( ) instanceof int[] )
			{
				int[] data = (int[]) dlg.getResult( );
				TableHandle table = factory.newTableItem( null,
						data[1],
						1,
						data[0],
						1 );
				InsertInLayoutUtil.setInitWidth( table );
				return table;
			}
			return null;
		}
		else if ( ReportDesignConstants.GRID_ITEM.equals( type ) )
		{
			TableOptionDialog dlg = new TableOptionDialog( UIUtil.getDefaultShell( ),
					false );
			if ( dlg.open( ) == Window.OK && dlg.getResult( ) instanceof int[] )
			{
				int[] data = (int[]) dlg.getResult( );
				GridHandle grid = factory.newGridItem( null, data[1], data[0] );
				InsertInLayoutUtil.setInitWidth( grid );
				return grid;
			}
			return null;
		}
		else if ( ReportDesignConstants.IMAGE_ITEM.equals( type ) )
		{
			ImageBuilderDialog dialog = new ImageBuilderDialog( UIUtil.getDefaultShell( ) );
			if ( dialog.open( ) == Dialog.OK )
			{
				return (DesignElementHandle) dialog.getResult( );
			}
			return null;
		}
		return factory.newElement( type, null );
	}

	protected DesignElementHandle createElement( SlotHandle slotHandle,
			String type ) throws Exception
	{
		if ( type == null )
		{
			List supportList = DEUtil.getElementSupportList( slotHandle );
			if ( supportList.size( ) == 1 )
			{
				type = ( (IElementDefn) supportList.get( 0 ) ).getName( );
			}
			else
			{
				NewSectionDialog dialog = new NewSectionDialog( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						supportList );
				if ( dialog.open( ) == Dialog.CANCEL )
				{
					return null;
				}
				type = (String) dialog.getResult( )[0];
			}
		}
		return createElement( type );
	}

	protected boolean performInsert( Object model, SlotHandle slotHandle,
			String type, String position, Map extendData ) throws Exception
	{
		DesignElementHandle elementHandle = createElement( slotHandle, type );

		if ( extendData != null )
		{
			extendData.put( IRequestConstants.REQUEST_KEY_RESULT, elementHandle );
		}

		if ( elementHandle == null )
		{
			return false;
		}
		if ( position == InsertAction.CURRENT )
		{
			slotHandle.add( elementHandle );
		}
		else
		{
			int pos = DNDUtil.calculateNextPosition( model,
					DNDUtil.handleValidateTargetCanContain( model,
							elementHandle,
							true ) );
			if ( pos > 0 && position == InsertAction.ABOVE )
			{
				pos--;
			}

			if ( pos == -1 )
			{
				slotHandle.add( elementHandle );
			}
			else
			{
				slotHandle.add( elementHandle, pos );
			}
		}
		return true;
	}

	protected boolean performEdit( ReportElementHandle handle )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren( Object object )
	{
		return getChildren( object ).length > 0;
	}
}

