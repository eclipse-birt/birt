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
import org.eclipse.birt.report.designer.internal.ui.dialogs.DeleteWarningDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ImageBuilderDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.NewSectionDialog;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CutAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PasteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RenameAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.PageSetAction.CodePageAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Default node provider. This class is the base class for other providers
 * 
 *  
 */
public class DefaultNodeProvider implements INodeProvider
{

	private static final String DLG_CONFIRM_MSG = Messages.getString( "DefaultNodeProvider.Dlg.Confirm" ); //$NON-NLS-1$

	private static final String DLG_HAS_FOLLOWING_CLIENTS_MSG = Messages.getString( "DefaultNodeProvider.Tree.Clients" ); //$NON-NLS-1$

	private static final String DLG_REFERENCE_FOUND_TITLE = Messages.getString( "DefaultNodeProvider.Tree.Reference" ); //$NON-NLS-1$

	public static final String BODY = Messages.getString( "DefaultNodeProvider.Tree.Body" ); //$NON-NLS-1$

	public static final String PAGESETUP = Messages.getString( "DefaultNodeProvider.Tree.PageSetup" ); //$NON-NLS-1$

	public static final String DATASOURCES = Messages.getString( "DefaultNodeProvider.Tree.DataSources" ); //$NON-NLS-1$

	public static final String DATASETS = Messages.getString( "DefaultNodeProvider.Tree.DataSets" ); //$NON-NLS-1$

	public static final String STYLES = Messages.getString( "DefaultNodeProvider.Tree.Styles" ); //$NON-NLS-1$

	public static final String PARAMETERS = Messages.getString( "DefaultNodeProvider.Tree.Parameters" ); //$NON-NLS-1$

	public static final String SCRATCHPAD = Messages.getString( "DefaultNodeProvider.Tree.Scratch" ); //$NON-NLS-1$

	public static final String MASTERPAGE = Messages.getString( "DefaultNodeProvider.Tree.MasterPages" ); //$NON-NLS-1$

	public static final String COLUMNHEADING_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.ColumnHedings" ); //$NON-NLS-1$

	public static final String DETAIL_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.Detail" ); //$NON-NLS-1$

	public static final String HEADER_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.Header" ); //$NON-NLS-1$

	public static final String FOOTER_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.Footer" ); //$NON-NLS-1$

	public static final String GROUPS_DISPALYNAME = Messages.getString( "DefaultNodeProvider.Tree.Groups" ); //$NON-NLS-1$

	public static final String MISSINGNAME = Messages.getString( "DefaultNodeProvider.Tree.Invalid" ); //$NON-NLS-1$

	public static final String CONFIRM_PARAM_DELETE_TITLE = Messages.getString( "DefaultNodeProvider.ParameterGroup.ConfirmTitle" ); //$NON-NLS-1$

	public static final String CONFIRM_PARAM_DELETE_MESSAGE = Messages.getString( "DefaultNodeProvider.ParameterGroup.ConfirmMessage" ); //$NON-NLS-1$

	/**
	 * Creates the context menu
	 * 
	 * @param sourceViewer
	 *            TODO
	 * @param object
	 *            the object
	 * @param menu
	 *            the menu
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		menu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );
		// Rename action
		RenameAction renameAction = new RenameAction( sourceViewer );
		if ( renameAction.isEnabled( ) )
		{//if can rename,add to menu
			menu.add( renameAction );
		}
		// Delete action
		DeleteAction deleteAction = deleteAction = new DeleteAction( object );
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

		menu.add( new Separator( ) );

		Action pageAction = new CodePageAction( object );
		if ( pageAction.isEnabled( ) )
			menu.add( pageAction );

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
			if ( model instanceof IStructuredSelection )
			{
				boolean retValue = false;
				for ( Iterator itor = ( (IStructuredSelection) model ).iterator( ); itor.hasNext( ); )
				{
					Object obj = itor.next( );
					retValue |= ProviderFactory.createProvider( obj )
							.performRequest( obj, request );
				}
				return retValue;
			}
			DesignElementHandle handle = (DesignElementHandle) model;
			if ( handle.getContainer( ) == null )
			{//has been deleted
				return false;
			}
			return performDelete( handle );
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
			TableHandle table = factory.newTableItem( null, 3 );
			BasePaletteFactory.setInitWidth( table );
			return table;
		}
		else if ( ReportDesignConstants.GRID_ITEM.equals( type ) )
		{
			GridHandle grid = factory.newGridItem( null, 3, 3 );
			BasePaletteFactory.setInitWidth( grid );
			return grid;
		}
		else if ( ReportDesignConstants.IMAGE_ITEM.equals( type ) )
		{
			ImageBuilderDialog dialog = new ImageBuilderDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ) );
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
				type = ( (ElementDefn) supportList.get( 0 ) ).getName( );
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
			if ( model instanceof DesignElementHandle )
			{
				DesignElementHandle handle = (DesignElementHandle) model;
				int pos = slotHandle.findPosn( handle.getElement( ) );
				if ( position == InsertAction.ABOVE )
				{
					if ( pos > 0 )
					{
						pos--;
					}
					else
					{
						pos = 0;
					}
				}
				else if ( position == InsertAction.BELOW )
				{
					if ( pos < slotHandle.getCount( ) )
					{
						pos++;
					}
					else
					{
						pos = -1;
					}
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
		}

		return true;
	}

	protected boolean performEdit( ReportElementHandle handle )
	{
		return false;
	}

	protected boolean performDelete( DesignElementHandle handle )
			throws SemanticException
	{
		if ( handle instanceof ParameterGroupHandle )
		{
			if ( ( (ParameterGroupHandle) handle ).getParameters( ).getCount( ) > 0 )
			{
				if ( !MessageDialog.openQuestion( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						CONFIRM_PARAM_DELETE_TITLE,
						CONFIRM_PARAM_DELETE_MESSAGE ) )
				{
					return false;
				}
			}
		}
		ArrayList referenceList = new ArrayList( );
		for ( Iterator itor = handle.clientsIterator( ); itor.hasNext( ); )
		{
			referenceList.add( itor.next( ) );
		}
		if ( !referenceList.isEmpty( ) )
		{
			DeleteWarningDialog dialog = new DeleteWarningDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					DLG_REFERENCE_FOUND_TITLE,
					referenceList );
			dialog.setPreString( DEUtil.getDisplayLabel( handle )
					+ DLG_HAS_FOLLOWING_CLIENTS_MSG );
			dialog.setSufString( DLG_CONFIRM_MSG );
			if ( dialog.open( ) == Dialog.CANCEL )
			{
				return false;
			}
		}
		handle.drop( );
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren( Object object )
	{
		return getChildren( object ).length > 0;
	}
}

