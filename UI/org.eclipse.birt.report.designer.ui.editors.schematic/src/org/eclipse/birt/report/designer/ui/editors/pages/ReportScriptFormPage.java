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

package org.eclipse.birt.report.designer.ui.editors.pages;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.script.JSEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.util.EditorUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.ui.editors.IPageStaleType;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class ReportScriptFormPage extends ReportFormPage
{

	public static final String ID = "report.script"; //$NON-NLS-1$

	private JSEditor jsEditor;

	private Control control;

	private int staleType;

	private ModuleHandle model;

	private IReportEditorPage previouPage;

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init( IEditorSite site, IEditorInput input )
			throws PartInitException
	{
		super.init( site, input );
		try
		{
			jsEditor = new JSEditor( this );
			jsEditor.init( site, input );
		}
		catch ( Exception e )
		{
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#onBroughtToTop(org.eclipse.birt.report.designer.ui.editors.IReportEditorPage)
	 */
	public boolean onBroughtToTop( IReportEditorPage prePage )
	{
		// FIXME
		if ( getEditorInput( ) != prePage.getEditorInput( ) )
		{
			setInput( prePage.getEditorInput( ) );
		}
		previouPage = prePage;
		if ( prePage != null && jsEditor != null )
		{
			jsEditor.setIsModified( prePage.isDirty( ) );
		}

		ISelection selection = new StructuredSelection( SessionHandleAdapter.getInstance( )
				.getMediator( )
				.getCurrentState( )
				.getSelectionObject( ) );

		jsEditor.handleSelectionChanged( selection );

		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#markPageStale(int)
	 */
	public void markPageStale( int type )
	{
		this.staleType = type;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#getStaleType()
	 */
	public int getStaleType( )
	{
		return staleType;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	public Control getPartControl( )
	{
		return this.control;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.IFormPage#getId()
	 */
	public String getId( )
	{
		return ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl( Composite parent )
	{
		try
		{
			jsEditor.createPartControl( parent );
			Control[] children = parent.getChildren( );
			control = children[children.length - 1];
			//
			if ( previouPage != null )
			{
				onBroughtToTop( previouPage );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave( IProgressMonitor monitor )
	{
		jsEditor.saveModel( );
		IReportProvider provider = EditorUtil.getReportProvider( this, getEditorInput( ) );
		if ( provider != null )
		{
			provider.saveReport( getReportModel( ), getEditorInput( ), monitor );
			firePropertyChange( PROP_DIRTY );
		}
		markPageStale( IPageStaleType.NONE );
		getEditor( ).editorDirtyStateChanged( );
	}

	/**
	 * @return
	 */
	protected ModuleHandle getReportModel( )
	{
		if ( model == null )
		{
			IReportProvider provider = EditorUtil.getReportProvider( this, getEditorInput( ) );
			if ( provider != null )
			{
				model = provider.getReportModuleHandle( getEditorInput( ) );
			}
		}
		return model;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs( )
	{
		IReportProvider provider = EditorUtil.getReportProvider( this, getEditorInput( ) );
		if ( provider != null )
		{
			IPath path = provider.getSaveAsPath( getEditorInput( ) );

			final IEditorInput input = provider.createNewEditorInput( path );

			setInput( input );

			IRunnableWithProgress op = new IRunnableWithProgress( ) {

				public synchronized final void run( IProgressMonitor monitor )
						throws InvocationTargetException, InterruptedException
				{
					final InvocationTargetException[] iteHolder = new InvocationTargetException[1];
					try
					{
						IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable( ) {

							public void run( IProgressMonitor pm )
									throws CoreException
							{
								try
								{
									execute( pm );
								}
								catch ( InvocationTargetException e )
								{
									// Pass it outside the workspace runnable
									iteHolder[0] = e;
								}
								catch ( InterruptedException e )
								{
									// Re-throw as OperationCanceledException,
									// which
									// will be
									// caught and re-thrown as
									// InterruptedException
									// below.
									throw new OperationCanceledException( e.getMessage( ) );
								}
								// CoreException and OperationCanceledException
								// are
								// propagated
							}
						};

						ResourcesPlugin.getWorkspace( ).run( workspaceRunnable,
								ResourcesPlugin.getWorkspace( ).getRoot( ),
								IResource.NONE,
								monitor );
					}
					catch ( CoreException e )
					{
						throw new InvocationTargetException( e );
					}
					catch ( OperationCanceledException e )
					{
						throw new InterruptedException( e.getMessage( ) );
					}
					// Re-throw the InvocationTargetException, if any occurred
					if ( iteHolder[0] != null )
					{
						throw iteHolder[0];
					}
				}

				public void execute( final IProgressMonitor monitor )
						throws CoreException, InvocationTargetException,
						InterruptedException
				{

					try
					{
						doSave( monitor );
					}

					catch ( Exception e )
					{
						ExceptionHandler.handle( e );
					}
				}
			};

			try
			{
				new ProgressMonitorDialog( getSite( ).getWorkbenchWindow( )
						.getShell( ) ).run( false, true, op );
			}

			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty( )
	{
		return jsEditor.isDirty( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose( )
	{
		super.dispose( );
		jsEditor = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class adapter )
	{
		if ( adapter == ActionRegistry.class )
		{
			return jsEditor.getActionRegistry( );
		}
		if ( adapter == PalettePage.class )
		{
			return jsEditor.getAdapter( PalettePage.class );
		}
		if ( adapter == IContentOutlinePage.class )
		{

			// ( (NonGEFSynchronizerWithMutiPageEditor)
			// getSelectionSynchronizer( ) ).add( (NonGEFSynchronizer)
			// outlinePage.getAdapter( NonGEFSynchronizer.class ) );

			// Add JS Editor as a selection listener to Outline view selections.
			// outlinePage.addSelectionChangedListener( jsEditor );
			DesignerOutlinePage outlinePage = new DesignerOutlinePage( getModel( ) );

			return outlinePage;
		}
		return jsEditor.getAdapter( adapter );
	}

	/**
	 * Get JS Script editor.
	 * @return
	 */
	public IEditorPart getScriptEditor( )
	{
		return jsEditor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage#canLeaveThePage()
	 */
	public boolean canLeaveThePage( )
	{
		jsEditor.saveModelIfNeeds( );
		return super.canLeaveThePage( );
	}
	
	
}
