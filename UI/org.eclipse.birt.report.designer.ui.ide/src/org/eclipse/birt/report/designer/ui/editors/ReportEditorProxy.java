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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.internal.ui.editors.util.EditorUtil;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.PageBookView;

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

	MultiPageReportEditor instance;
	private String title = "";

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
		}
		else
		{
			instance = new MultiPageReportEditor( );
		}
		instance.init( site, input );
		instance.addPropertyListener( this );
		getSite( ).getWorkbenchWindow( )
				.getPartService( )
				.addPartListener( this );

		setPartName( getEditorInput( ).getName( ) );

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

	public Image getTitleImage( )
	{
		return instance.getTitleImage( );
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
		IReportProvider provider = EditorUtil.getReportProvider( this,
				getEditorInput( ) );
		if ( provider != null )
		{
			setPartName( provider.getInputPath( getEditorInput( ) )
					.lastSegment( ) );
		}
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
		instance.partActivated( part );
		if ( part != this )
		{
			if ( part instanceof PageBookView )
			{
				PageBookView view = (PageBookView) part;
				if ( view.getCurrentPage( ) instanceof DesignerOutlinePage )
				{
					ISelectionProvider provider = (ISelectionProvider) view.getCurrentPage( );
					ReportRequest request = new ReportRequest( view.getCurrentPage( ) );
					List list = new ArrayList( );
					if ( provider.getSelection( ) instanceof IStructuredSelection )
					{
						list = ( (IStructuredSelection) provider.getSelection( ) ).toList( );
					}
					request.setSelectionObject( list );
					request.setType( ReportRequest.SELECTION );
					// no convert
					// request.setRequestConvert(new
					// EditorReportRequestConvert());
					SessionHandleAdapter.getInstance( )
							.getMediator( )
							.notifyRequest( request );
					SessionHandleAdapter.getInstance( )
							.getMediator( )
							.pushState( );
				}
			}
			if ( instance.getActiveEditor( ) instanceof GraphicalEditorWithFlyoutPalette )
			{
				if ( ( (GraphicalEditorWithFlyoutPalette) instance.getActiveEditor( ) ).getGraphicalViewer( )
						.getEditDomain( )
						.getPaletteViewer( ) != null )
				{
					GraphicalEditorWithFlyoutPalette editor = (GraphicalEditorWithFlyoutPalette) instance.getActiveEditor( );
					GraphicalViewer view = editor.getGraphicalViewer( );
					view.getEditDomain( ).loadDefaultTool( );
				}

			}
			return;
		}

		if ( part == this )
		{
			// use the asynchronized execution to ensure correct active page
			// index.
			Display.getCurrent( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					if ( instance.getActivePageInstance( ) instanceof GraphicalEditorWithFlyoutPalette )
					{
						GraphicalEditorWithFlyoutPalette editor = (GraphicalEditorWithFlyoutPalette) instance.getActivePageInstance( );
						GraphicalViewer view = editor.getGraphicalViewer( );

						UIUtil.resetViewSelection( view, true );
					}
				};

			} );

			if ( getEditorInput( ).exists( ) )
			{
				instance.handleActivation( );

				SessionHandleAdapter.getInstance( )
						.setReportDesignHandle( instance.getModel( ) );
				DataSetManager.initCurrentInstance( getEditorInput( ) );
			}
		}
	}

	public void partBroughtToTop( IWorkbenchPart part )
	{
		instance.partBroughtToTop( part );
	}

	public void partClosed( IWorkbenchPart part )
	{
		// instance.partClosed( part );
		// FIXME ugly code
		if ( part == this )
		{
			SessionHandleAdapter.getInstance( ).clear( instance.getModel( ) );
		}
	}

	public void partDeactivated( IWorkbenchPart part )
	{
		instance.partDeactivated( part );
	}

	public void partOpened( IWorkbenchPart part )
	{
		instance.partOpened( part );
	}

	public void propertyChanged( Object source, int propId )
	{
		firePropertyChange( propId );
	}

	public IEditorPart getEditorPart( )
	{
		return instance;
	}

}
