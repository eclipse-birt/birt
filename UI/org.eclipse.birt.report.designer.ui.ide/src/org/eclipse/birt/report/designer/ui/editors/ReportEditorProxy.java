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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * ReportEditorProxy is a editor proxy, which use in eclipse IDE enviroment.
 * 
 * ReportEditorProxy determines editor input, then create a proper editor
 * instance to represents the editor behaivors.
 */

public class ReportEditorProxy extends EditorPart implements
		IPartListener,
		IPropertyListener,
		IReportEditor
{

	/**
	 * The ID of the Report Editor
	 */
	public static final String REPROT_EDITOR_ID = "org.eclipse.birt.report.designer.ui.editors.ReportEditor"; //$NON-NLS-1$
	/**
	 * The ID of the Template Editor
	 */
	public static final String TEMPLATE_EDITOR_ID = "org.eclipse.birt.report.designer.ui.editors.TemplateEditor"; //$NON-NLS-1$
	/**
	 * The ID of the Library Editor
	 */
	public static final String LIBRARY_EDITOR_ID = "org.eclipse.birt.report.designer.ui.editors.LibraryEditor"; //$NON-NLS-1$

	MultiPageReportEditor instance;
	private String title = ""; //$NON-NLS-1$

	public IEditorInput getEditorInput( )
	{
		return instance.getEditorInput( );
	}

	public IEditorSite getEditorSite( )
	{
		return instance.getEditorSite( );
	}

	public void init( IEditorSite site, IEditorInput input )
			throws PartInitException
	{
		if ( instance != null )
		{
			getSite( ).getWorkbenchWindow( )
					.getPartService( )
					.removePartListener( instance );
			instance.dispose( );
		}

		if ( input instanceof IFileEditorInput )
		{
			instance = new IDEMultiPageReportEditor( );
			instance.addPropertyListener( this );
		}
		else
		{
			instance = new MultiPageReportEditor( );
			instance.addPropertyListener( this );
		}
		instance.init( site, input );
		instance.addPropertyListener( this );
		getSite( ).getWorkbenchWindow( )
				.getPartService( )
				.addPartListener( this );

	}

	public void createPartControl( Composite parent )
	{
		instance.createPartControl( parent );
	}

	public void dispose( )
	{
		if ( instance != null )
		{
			instance.dispose( );
			getSite( ).getWorkbenchWindow( )
					.getPartService( )
					.removePartListener( this );
			instance.removePropertyListener( this );
		}
		instance = null;
	}

	public IWorkbenchPartSite getSite( )
	{
		return instance.getSite( );
	}

	public String getTitle( )
	{
		return this.title;
	}

	public String getTitleToolTip( )
	{
		return instance.getTitleToolTip( );
	}

	public void setFocus( )
	{
		instance.setFocus( );
	}

	public Object getAdapter( Class adapter )
	{
		return instance.getAdapter( adapter );
	}

	public void doSave( IProgressMonitor monitor )
	{
		instance.doSave( monitor );
		firePropertyChange( PROP_DIRTY );
	}

	public void doSaveAs( )
	{
		instance.doSaveAs( );

		firePropertyChange( PROP_DIRTY );
	}

	public boolean isDirty( )
	{
		return instance.isDirty( );
	}

	public boolean isSaveAsAllowed( )
	{
		return instance.isSaveAsAllowed( );
	}

	public boolean isSaveOnCloseNeeded( )
	{
		return instance.isSaveOnCloseNeeded( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput( IEditorInput input )
	{
		// TODO Auto-generated method stub
		super.setInput( input );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setPartName(java.lang.String)
	 */
	protected void setPartName( String partName )
	{
		this.title = partName;
		super.setPartName( partName );
	}

	public void partActivated( IWorkbenchPart part )
	{
		if ( part instanceof ReportEditorProxy )
		{
			instance.partActivated( ( (ReportEditorProxy) part ).getEditorPart( ) );
		}
		else
		{
			instance.partActivated( part );
		}
		// if ( part != this )
		// {
		// if ( part instanceof PageBookView )
		// {
		// PageBookView view = (PageBookView) part;
		// if ( view.getCurrentPage( ) instanceof DesignerOutlinePage )
		// {
		// ISelectionProvider provider = (ISelectionProvider)
		// view.getCurrentPage( );
		// ReportRequest request = new ReportRequest( view.getCurrentPage( ) );
		// List list = new ArrayList( );
		// if ( provider.getSelection( ) instanceof IStructuredSelection )
		// {
		// list = ( (IStructuredSelection) provider.getSelection( ) ).toList( );
		// }
		// request.setSelectionObject( list );
		// request.setType( ReportRequest.SELECTION );
		// // no convert
		// // request.setRequestConvert(new
		// // EditorReportRequestConvert());
		// SessionHandleAdapter.getInstance( )
		// .getMediator( )
		// .notifyRequest( request );
		// SessionHandleAdapter.getInstance( )
		// .getMediator( )
		// .pushState( );
		// }
		// }
		// if ( instance.getActiveEditor( ) instanceof
		// GraphicalEditorWithFlyoutPalette )
		// {
		// if ( ( (GraphicalEditorWithFlyoutPalette) instance.getActiveEditor( )
		// ).getGraphicalViewer( )
		// .getEditDomain( )
		// .getPaletteViewer( ) != null )
		// {
		// GraphicalEditorWithFlyoutPalette editor =
		// (GraphicalEditorWithFlyoutPalette) instance.getActiveEditor( );
		// GraphicalViewer view = editor.getGraphicalViewer( );
		// view.getEditDomain( ).loadDefaultTool( );
		// }
		//
		// }
		// return;
		// }
		//
		// if ( part == this )
		// {
		// // use the asynchronized execution to ensure correct active page
		// // index.
		// Display.getCurrent( ).asyncExec( new Runnable( ) {
		//
		// public void run( )
		// {
		// // if ( instance.getActivePageInstance( ) instanceof
		// GraphicalEditorWithFlyoutPalette )
		// // {
		// // GraphicalEditorWithFlyoutPalette editor =
		// (GraphicalEditorWithFlyoutPalette) instance.getActivePageInstance( );
		// // GraphicalViewer view = editor.getGraphicalViewer( );
		// //
		// // UIUtil.resetViewSelection( view, true );
		// // }
		// };
		//
		// } );
		//
		// if ( getEditorInput( ).exists( ) )
		// {
		// instance.handleActivation( );
		//
		// SessionHandleAdapter.getInstance( )
		// .setReportDesignHandle( instance.getModel( ) );
		// DataSetManager.initCurrentInstance( getEditorInput( ) );
		// }
		// }
	}

	public void partBroughtToTop( IWorkbenchPart part )
	{
		if ( part instanceof ReportEditorProxy )
		{
			instance.partBroughtToTop( ( (ReportEditorProxy) part ).getEditorPart( ) );
		}
		else
		{
			instance.partBroughtToTop( part );
		}
	}

	public void partClosed( IWorkbenchPart part )
	{
		if ( part instanceof ReportEditorProxy )
		{
			instance.partClosed( ( (ReportEditorProxy) part ).getEditorPart( ) );
		}
		else
		{
			instance.partClosed( part );
		}
		// instance.partClosed( part );
		// FIXME ugly code
		if ( part == this )
		{
			SessionHandleAdapter.getInstance( ).clear( instance.getModel( ) );
		}
	}

	public void partDeactivated( IWorkbenchPart part )
	{
		if ( part instanceof ReportEditorProxy )
		{
			instance.partDeactivated( ( (ReportEditorProxy) part ).getEditorPart( ) );
		}
		else
		{
			instance.partDeactivated( part );
		}
	}

	public void partOpened( IWorkbenchPart part )
	{
		if ( part instanceof ReportEditorProxy )
		{
			instance.partOpened( ( (ReportEditorProxy) part ).getEditorPart( ) );
		}
		else
		{
			instance.partOpened( part );
		}
	}

	public void propertyChanged( Object source, int propId )
	{
		if ( propId == IWorkbenchPartConstants.PROP_PART_NAME )
		{
			setPartName( instance.getPartName( ) );
		}

		firePropertyChange( propId );
	}

	public IEditorPart getEditorPart( )
	{
		return instance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if (obj == instance)
		{
			return true;
		}
		return super.equals( obj );
	}
}
