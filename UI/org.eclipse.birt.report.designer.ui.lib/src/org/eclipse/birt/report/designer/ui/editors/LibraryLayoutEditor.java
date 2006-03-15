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

package org.eclipse.birt.report.designer.ui.editors;


import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.lib.commands.SetCurrentEditModelCommand;
import org.eclipse.birt.report.designer.internal.lib.editparts.LibraryGraphicalPartFactory;
import org.eclipse.birt.report.designer.internal.lib.palette.LibraryTemplateTransferDropTargetListener;
import org.eclipse.birt.report.designer.internal.lib.views.outline.LibraryOutlinePage;
import org.eclipse.birt.report.designer.internal.ui.editors.notification.DeferredRefreshManager;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.layout.AbstractReportGraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.internal.ui.palette.DesignerPaletteFactory;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewPage;
import org.eclipse.birt.report.designer.internal.ui.views.property.ReportPropertySheetPage;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * <p>
 * Report design graphical editor. This editor is the main editor of JRP ERD.
 * </p>
 * 
 * 
 */
public class LibraryLayoutEditor extends AbstractReportGraphicalEditorWithFlyoutPalette
{

	private IEditorPart parentEditorPart;

	public LibraryLayoutEditor( )
	{
		super( );
	}

	/**
	 * @param parent
	 */
	public LibraryLayoutEditor( IEditorPart parent )
	{
		super( parent );
		this.parentEditorPart = parent;
	}

	public boolean isSaveAsAllowed( )
	{
		return true;
	}

	public void performRequest( ReportRequest request )
	{
		if ( ReportRequest.OPEN_EDITOR.equals( request.getType( ) )
				&& ( request.getSelectionModelList( ).size( ) == 1 )
				&& request.getSelectionModelList( ).get( 0 ) instanceof SlotHandle )
		{
			SlotHandle slt = (SlotHandle) request.getSelectionModelList( )
					.get( 0 );
			if ( slt.getSlotID( ) == ReportDesignHandle.BODY_SLOT )
			{
				handleOpenDesigner( request );
			}
			return;
		}

		super.performRequest( request );
	}

	public void selectionChanged( IWorkbenchPart part, ISelection selection )
	{
		super.selectionChanged( part, selection );

		IEditorPart report = getSite( ).getPage( ).getActiveEditor( );
		if ( report != null )
		{
			updateActions( getSelectionActions( ) );
		}
	}

	protected void handleSelectionChange( ReportRequest request )
	{
		List list = request.getSelectionModelList( );
		// should be change the reuqest.getSource() interface, recode the source
		// type.added by gao
		if ( ( request.getSource( ) instanceof LibraryOutlinePage || request.getSource( ) instanceof TableEditPart )
				&& !isInContainer( list ) )
		{
			int size = list.size( );
			Object obj = null;
			if ( size != 0 )
			{
				obj = list.get( size - 1 );
			}
			SetCurrentEditModelCommand command = new SetCurrentEditModelCommand( obj );
			command.execute( );
			return;
		}
		super.handleSelectionChange( request );
	}
	
	private boolean isInContainer( List list )
	{
		boolean retValue = false;
		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof RowHandle || obj instanceof CellHandle )
			{
				retValue = true;
			}
			else
			{
				retValue = false;
				break;
			}
		}
		return retValue;
	}
	
	protected TemplateTransferDropTargetListener createTemplateTransferDropTargetListener( EditPartViewer viewer)
	{
		return new LibraryTemplateTransferDropTargetListener( viewer );
	}

	/**
	 * @param request
	 */
	private void handleOpenDesigner( ReportRequest request )
	{
		// if ( ( (LayoutEditor) editingDomainEditor ).isVisible( ) )
		// {
		// ( (LayoutEditor) editingDomainEditor ).setActivePage( 0 );
		// ( (LayoutEditor) editingDomainEditor ).pageChange( 0 );
		// }
	}

	/**
	 * Returns an object which is an instance of the given class associated with
	 * this object. Returns <code>null</code> if no such object can be found.
	 * 
	 * @param adapter
	 *            the adapter class to look up
	 * @return a object castable to the given class, or <code>null</code> if
	 *         this object does not have an adapter for the given class
	 */
	public Object getAdapter( Class adapter )
	{

		if ( adapter == IContentOutlinePage.class )
		{

			// ( (NonGEFSynchronizerWithMutiPageEditor)
			// getSelectionSynchronizer( ) ).add( (NonGEFSynchronizer)
			// outlinePage.getAdapter( NonGEFSynchronizer.class ) );

			// Add JS Editor as a selection listener to Outline view selections.
			// outlinePage.addSelectionChangedListener( jsEditor );
			return new LibraryOutlinePage( getModel( ) );
		}

		if ( adapter == DataViewPage.class )
		{
			// TODO garbage code
			// important: this code is for fixing a bug in emergency.
			// Must shift to mediator structure after R1
			DataViewPage page = (DataViewPage) super.getAdapter( adapter );
			if ( page == null )
			{
				return null;
			}
			return page;
		}

		// return the property sheet page
		if ( adapter == IPropertySheetPage.class )
		{
			ReportPropertySheetPage sheetPage = new ReportPropertySheetPage( );
			return sheetPage;
		}

		if ( adapter == DeferredRefreshManager.class )
			return getRefreshManager( );

		return super.getAdapter( adapter );
	}

	protected PaletteRoot getPaletteRoot( )
	{
		if ( paletteRoot == null )
		{
			paletteRoot = DesignerPaletteFactory.createPalette( );
		}
		return paletteRoot;

	}

	protected IEditorPart getMultiPageEditor( )
	{
		return parentEditorPart;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.schematic.layout.AbstractReportGraphicalEditorWithFlyoutPalette#getFileType()
	 */
	protected int getFileType( )
	{
		return SessionHandleAdapter.LIBRARYFILE;
	}

	protected EditPartFactory getEditPartFactory( )
	{
		return new LibraryGraphicalPartFactory( );
	}
}