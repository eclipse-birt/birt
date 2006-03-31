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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.internal.ui.dialogs.NewSectionDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.TemplateReportItemPropertiesDialog;
import org.eclipse.birt.report.designer.internal.ui.processor.ElementProcessorFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CutAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertInLayoutAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PasteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RenameAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Default node provider. This class is the base class for other providers
 */

public class DefaultNodeProvider implements INodeProvider
{

	public static final String BODY = Messages.getString( "DefaultNodeProvider.Tree.Body" ); //$NON-NLS-1$

	public static final String COMPONENTS = Messages.getString( "DefaultNodeProvider.Tree.Components" ); //$NON-NLS-1$

	public static final String PAGESETUP = Messages.getString( "DefaultNodeProvider.Tree.PageSetup" ); //$NON-NLS-1$

	public static final String DATASOURCES = Messages.getString( "DefaultNodeProvider.Tree.DataSources" ); //$NON-NLS-1$

	public static final String DATASETS = Messages.getString( "DefaultNodeProvider.Tree.DataSets" ); //$NON-NLS-1$

	public static final String STYLES = Messages.getString( "DefaultNodeProvider.Tree.Styles" ); //$NON-NLS-1$

	public static final String THEMES = Messages.getString( "DefaultNodeProvider.Tree.Themes" ); //$NON-NLS-1$

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

	private Comparator comparator;

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
		{// if can rename,add to menu
			menu.add( renameAction );
		}
		// Delete action
		DeleteAction deleteAction = new DeleteAction( object );
		if ( deleteAction.isEnabled( ) )
		{// if can delete,add to menu
			menu.add( deleteAction );
		}

		CutAction cutAction = new CutAction( object );
		if ( cutAction.isEnabled( ) )
			menu.add( cutAction );

		CopyAction copyAction = new CopyAction( object );
		if ( copyAction.isEnabled( ) )
			menu.add( copyAction );

		if ( !( object instanceof DataSetItemModel ) )
		{
			menu.add( new PasteAction( object ) );
		}

		// Insert point for refresh action
		menu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS
				+ "-refresh" ) );//$NON-NLS-1$

		// Action action = new CodePageAction( object );
		// if ( action.isEnabled( ) )
		// menu.add( action );
		//
		// menu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS
		// + "-end" ) );//$NON-NLS-1$

		// if ( object instanceof ReportDesignHandle
		// || ( object instanceof ReportElementHandle && !( (
		// (ReportElementHandle) object ).getRoot( ) instanceof LibraryHandle )
		// ) )

		// action = new CreatePlaceHolderAction( object );
		// if ( action.isEnabled( ) )
		// {
		// menu.add( action );
		// }
		// action = new TransferPlaceHolderAction( object );
		// if ( action.isEnabled( ) )
		// {
		// menu.add( action );
		// }

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
				Object[] children = this.getChildrenBySlotHandle( ( (ReportElementModel) model ).getSlotHandle( ) );
				if ( comparator != null )
				{
					Arrays.sort( children, comparator );
				}
				return children;
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
		else if ( model instanceof SlotHandle )
		{
			return ( (SlotHandle) model ).getElementHandle( );
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

		if ( model instanceof DesignElementHandle
				&& ( (DesignElementHandle) model ).getSemanticErrors( ).size( ) > 0 )
		{
			return ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );
		}
		if ( iconName != null )
		{// if the getIconName is defined
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
	 * Gets the tooltip of the node
	 * 
	 * @param model
	 *            the model of the node
	 * @return Returns the tooltip name for the node, or null if no tooltip is
	 *         needed.
	 */
	public String getNodeTooltip( Object model )
	{
		return null;
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
	{// Do nothing
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
		if ( request.getType( )
				.equals( IRequestConstants.REQUEST_CREATE_PLACEHOLDER ) )
		{
			return performCreatePlaceHolder( (ReportElementHandle) model );
		}
		if ( request.getType( )
				.equals( IRequestConstants.REQUEST_TRANSFER_PLACEHOLDER ) )
		{
			return performTransferPlaceHolder( (TemplateElementHandle) model );
		}
		if ( request.getType( )
				.equals( IRequestConstants.REQUST_REVERT_TO_REPORTITEM ) )
		{
			return performRevertToReportItem( (DesignElementHandle) model );
		}
		if ( request.getType( )
				.equals( IRequestConstants.REQUST_REVERT_TO_TEMPLATEITEM ) )
		{
			return performRevertToTemplateItem( (DesignElementHandle) model );
		}

		return false;
	}

	private boolean performRevertToTemplateItem( DesignElementHandle handle )
	{
		try
		{
			handle.revertToTemplate( "" ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
		return true;
	}

	private boolean performTransferPlaceHolder( TemplateElementHandle handle )
	{
		DesignElementHandle copiedHandle = handle.getDefaultElement( )
				.copy( )
				.getHandle( SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getModule( ) );
		try
		{
			( (TemplateReportItemHandle) handle ).transformToReportItem( (ReportItemHandle) copiedHandle );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
		return true;
	}

	private boolean performRevertToReportItem( DesignElementHandle handle )
	{
		try
		{
			handle.revertToReportItem( );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
		return true;
	}

	private boolean checkNameExist( DesignElement element, String name )
	{
		if ( name == null || name.length( ) == 0 )
			return false;
		ElementDefn metaData = (ElementDefn) element.getDefn( );
		int ns = metaData.getNameSpaceID( );

		// first found the element with the given name. Since the library
		// has it own namespace -- prefix, the range of name check should be
		// in the current module.

		DesignElement existedElement = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getModule( )
				.getNameSpace( ns )
				.getElement( name );

		return ( existedElement == null ) ? false : true;

	}

	private boolean performCreatePlaceHolder( ReportElementHandle handle )
	{
		boolean bIsNameExist = false;
		TemplateElementHandle template = null;
		String name = null;
		String desc = null;

		do
		{
			try
			{
				if ( template == null )
				{
					template = handle.createTemplateElement( null );
				}

				if ( name == null )
				{
					name = template.getName( );
					name = ( name == null ) ? "" : name; //$NON-NLS-1$					
				}

				if ( desc == null )
				{
					desc = template.getDescription( );
					desc = ( desc == null ) ? "" : desc; //$NON-NLS-1$
				}

				TemplateReportItemPropertiesDialog dialog = new TemplateReportItemPropertiesDialog( template.getDefaultElement( )
						.getDefn( )
						.getDisplayName( ),
						name,
						desc );
				if ( dialog.open( ) == Window.OK )
				{
					name = (String) dialog.getName( ).trim( );
					desc = (String) dialog.getResult( );

					bIsNameExist = checkNameExist( template.getElement( ), name );
					if ( bIsNameExist == false )
					{
						template.setDescription( desc );
						template.setName( name );
					}
					else
					{

						ExceptionHandler.openErrorMessageBox( Messages.getString( "performCreatePlaceHolder.errorMessage.title" ), //$NON-NLS-1$
								Messages.getString( "performCreatePlaceHolder.errorMessage.content" ) ); //$NON-NLS-1$
					}
				}
				else
				{
					return false;
				}
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
				return false;
			}
		} while ( bIsNameExist == true );

		return true;
	}

	protected DesignElementHandle createElement( String type ) throws Exception
	{
		return ElementProcessorFactory.createProcessor( type )
				.createElement( null );
	}

	protected DesignElementHandle createElement( SlotHandle slotHandle,
			String type ) throws Exception
	{
		if ( type == null )
		{
			List supportList = DEUtil.getElementSupportList( slotHandle );
			if ( supportList.size( ) == 1 || supportList.get( 0 ) instanceof SimpleMasterPageHandle)
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
		if ( elementHandle instanceof ExtendedItemHandle )
		{
			if ( !ElementProcessorFactory.createProcessor( elementHandle )
					.editElement( elementHandle ) )
			{
				return false;
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

	/**
	 * Set comparator to control the order of children.
	 * 
	 * @param comparator
	 */
	public void setSorter( Comparator comparator )
	{
		this.comparator = comparator;
	}
}
