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
import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewPage;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewTreeViewerPage;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.MultiPageEditorSite;

/**
 * 
 */

public class LibraryLayoutEditorFormPage extends LibraryLayoutEditor implements
		IReportEditorPage
{

	private static final String ID = "org.eclipse.birt.report.designer.ui.editors.library.layout"; //$NON-NLS-1$

	private int index;

	private FormEditor editor;

	private Control control;

	private int staleType;


	private ActivityStackListener commandStackListener = new ActivityStackListener( ) {

		public void stackChanged( ActivityStackEvent event )
		{
			updateStackActions( );
			getEditor( ).editorDirtyStateChanged( );
			staleType = IPageStaleType.MODEL_CHANGED;
		}
	};


	protected void configureGraphicalViewer( )
	{
		super.configureGraphicalViewer( );
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack( );
		if ( stack != null )
		{
			stack.addCommandStackListener( getCommandStackListener( ) );
		}
	}
	/**
	 * returns command stack listener.
	 */
	public ActivityStackListener getCommandStackListener( )
	{
		return commandStackListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#initialize(org.eclipse.ui.forms.editor.FormEditor)
	 */
	public void initialize( FormEditor editor )
	{
		this.editor = editor;
	}

	public FormEditor getEditor( )
	{
		return editor;
	}

	public void init( IEditorSite site, IEditorInput input )
			throws PartInitException
	{
		super.init( site, input );
		initialize( (FormEditor)((MultiPageEditorSite)site).getMultiPageEditor() );
		// Initializes command stack
//		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack( );
//		if ( stack != null )
//		{
//			stack.addCommandStackListener( getCommandStackListener( ) );
//		}
	}

	public IManagedForm getManagedForm( )
	{
		return null;
	}

	public void setActive( boolean active )
	{
	}

	public boolean isActive( )
	{
		return false;
	}

	public boolean canLeaveThePage( )
	{
		return true;
	}

	public Control getPartControl( )
	{
		return control;
	}

	public String getId( )
	{		
		return ID;
	}

	public int getIndex( )
	{
		return index;
	}

	public void setIndex( int index )
	{
		this.index = index;
	}

	public boolean isEditor( )
	{
		return true;
	}

	public boolean selectReveal( Object object )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl( Composite parent )
	{
		super.createPartControl( parent );
		Control[] children = parent.getChildren( );
		control = children[children.length - 1];
	}

	public boolean onBroughtToTop( IReportEditorPage page )
	{
		//the three classes has the logic to rebuild the model, should be refactor. 
		ModuleHandle model = getProvider( ).getReportModuleHandle( getEditorInput( ) );
		
		if ( model != null && getModel( ) != model )
		{
			Object oldModel = getModel( );

			setModel( model );
			
			rebuildReportDesign( oldModel );
			if ( getModel( ) != null )
			{
				this.getGraphicalViewer( ).setContents( getModel( ) );
				hookModelEventManager(getModel( ) );
				markPageStale( IPageStaleType.NONE );
			}
			updateStackActions( );
		}
//		reselect the selection
		GraphicalViewer view = getGraphicalViewer( );

		if(view !=null)
		{
			UIUtil.resetViewSelection( view, true );
		}
		return true;
	}
	
	/**
	 * Rebuild report design model.
	 * 
	 * @param oldModel
	 */
	protected void rebuildReportDesign( Object oldModel )
	{
		// Initializes command stack
		WrapperCommandStack stack = (WrapperCommandStack) getCommandStack( );
		if ( stack != null )
		{
			stack.removeCommandStackListener( getCommandStackListener( ) );
			stack.setActivityStack( getModel( ).getCommandStack( ) );
			stack.addCommandStackListener( getCommandStackListener( ) );
		}

		// Resets the mediator
		SessionHandleAdapter.getInstance( ).resetReportDesign( oldModel,
				getModel( ) );

		SessionHandleAdapter.getInstance( ).setReportDesignHandle(
				getProvider( ).getReportModuleHandle( getEditorInput( ) ) );

	}

	public void dispose( )
	{
		if ( getCommandStack( ) != null )
		{
			WrapperCommandStack stack = (WrapperCommandStack) getCommandStack( );
			stack.removeCommandStackListener( getCommandStackListener( ) );
		}
		super.dispose( );
	}

	public void markPageStale( int type )
	{
		staleType = type;
	}

	public int getStaleType( )
	{
		return staleType;
	}

	public Object getAdapter( Class adapter )
	{
		if ( adapter == DataViewPage.class )
		{
			DataViewTreeViewerPage page = new DataViewTreeViewerPage( getModel( ) );
			return page;
		}
		return super.getAdapter( adapter );
	}
	
	public void setInput(IEditorInput input)
	{
		super.setInput(input);
	}
	
	protected IReportProvider getProvider( )
	{
		IReportProvider provider =  (IReportProvider) editor.getAdapter( IReportProvider.class );
		if(provider == null)
		{
			provider = super.getProvider( );
		}
		
		return provider;
	}	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#firePropertyChange(int)
	 */
	protected void firePropertyChange( int type )
	{
		if ( type == PROP_DIRTY )
		{
			editor.editorDirtyStateChanged( );
		}
		else
		{
			super.firePropertyChange( type );
		}
	}
}
