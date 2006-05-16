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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.xml.XMLEditor;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.IPageStaleType;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * XML editor for report source file.
 */
public class ReportXMLSourceEditorFormPage extends XMLEditor implements
		IReportEditorPage
{

	public static final String ID = "org.eclipse.birt.report.designer.ui.editors.xmlsource"; //$NON-NLS-1$

	private ActionRegistry registry;

	private FormEditor editor;
	private Control control;
	private int staleType;

	private int index;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#initialize(org.eclipse.ui.forms.editor.FormEditor)
	 */
	public void initialize( FormEditor editor )
	{
		this.editor = editor;
		setRangeIndicator( new Annotation(){} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getEditor()
	 */
	public FormEditor getEditor( )
	{
		return editor;
	}

	public void doSave( IProgressMonitor progressMonitor )
	{
		super.doSave( progressMonitor );
		IReportProvider provider = getProvider();
		if ( provider != null && getErrorLIine( ) == -1 )
		{
			ModuleHandle model = provider.getReportModuleHandle( getEditorInput( ),
					true );
			SessionHandleAdapter.getInstance( ).setReportDesignHandle( model );
		}
	}

	private int getErrorLIine( )
	{
		IEditorInput input = getEditorInput( );

		if ( !( input instanceof IPathEditorInput ) )
		{
			return 0;
		}
		IPath path = ( (IPathEditorInput) input ).getPath( );
		try
		{
			if ( path.toOSString( )
					.endsWith( IReportEditorContants.LIBRARY_FILE_EXTENTION ) )
			{
				try
				{
					SessionHandleAdapter.getInstance( )
							.getSessionHandle( )
							.openLibrary( path.toOSString( ) );
				}
				catch ( DesignFileException e )
				{
					return getExpetionErrorLine( e );
				}
			}
			else
			{
				try
				{
					SessionHandleAdapter.getInstance( )
							.getSessionHandle( )
							.openDesign( path.toOSString( ),
									new FileInputStream( path.toFile( ) ) );
				}
				catch ( DesignFileException e )
				{
					return getExpetionErrorLine( e );
				}
			}
		}
		catch ( FileNotFoundException e )
		{
			return 0;
		}
		return -1;
	}

	private int getExpetionErrorLine( DesignFileException e )
	{
		List errorList = e.getErrorList( );
		for ( Iterator iter = errorList.iterator( ); iter.hasNext( ); )
		{
			Object element = iter.next( );
			if ( element instanceof ErrorDetail )
			{
				return ( (ErrorDetail) element ).getLineNo( );
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getManagedForm()
	 */
	public IManagedForm getManagedForm( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#setActive(boolean)
	 */
	public void setActive( boolean active )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#isActive()
	 */
	public boolean isActive( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#canLeaveThePage()
	 */
	public boolean canLeaveThePage( )
	{
		if ( isDirty( ) )
		{
			MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
					Messages.getString( "XMLSourcePage.Error.Dialog.title" ),//$NON-NLS-1$
					null,
					Messages.getString( "XMLSourcePage.Error.Dialog.Message.PromptMsg" ),//$NON-NLS-1$
					MessageDialog.INFORMATION,
					new String[]{
							Messages.getString( "XMLSourcePage.Error.Dialog.Message.Yes" ),//$NON-NLS-1$
							Messages.getString( "XMLSourcePage.Error.Dialog.Message.No" ),//$NON-NLS-1$
							Messages.getString( "XMLSourcePage.Error.Dialog.Message.Cancel" )}, 0 );//$NON-NLS-1$

			int ret = prefDialog.open( );
			switch ( ret )
			{
				case 0 :
					doSave( null );
					break;
				case 1 :
					if ( getEditorInput( ) != null )
					{
						this.setInput( getEditorInput( ) );
					}
					break;
				case 2 :
					return false;
			}
		}

		int errorLine = getErrorLIine( );

		if ( errorLine > -1 )
		{
//			Display.getCurrent( ).beep( );
			MessageDialog.openError( Display.getCurrent( ).getActiveShell( ),
					Messages.getString( "XMLSourcePage.Error.Dialog.title" ), //$NON-NLS-1$
					Messages.getString( "XMLSourcePage.Error.Dialog.Message.InvalidFile" ) ); //$NON-NLS-1$
			setFocus( );
			setHighlightLine( errorLine );

			return false;
		}
		return true;
	}

	private void setHighlightLine( int errorLine )
	{
		try
		{
			IRegion region = getDocumentProvider( ).getDocument( getEditorInput( ) )
					.getLineInformation( errorLine );
			setHighlightRange( region.getOffset( ), region.getLength( ), true );
		}
		catch ( BadLocationException e )
		{
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	public Control getPartControl( )
	{
		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getId()
	 */
	public String getId( )
	{
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getIndex()
	 */
	public int getIndex( )
	{
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#setIndex(int)
	 */
	public void setIndex( int index )
	{
		this.index = index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#isEditor()
	 */
	public boolean isEditor( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#selectReveal(java.lang.Object)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#firePropertyChange(int)
	 */
	// protected void firePropertyChange( int type )
	// {
	// if ( type == PROP_DIRTY )
	// {
	// markPageStale( IPageStaleType.CODE_CHANGED );
	// getEditor( ).editorDirtyStateChanged( );
	// }
	// else
	// super.firePropertyChange( type );
	// }
	public boolean onBroughtToTop( IReportEditorPage prePage )
	{
		if ( getEditorInput( ) != prePage.getEditorInput( ) )
		{
			setInput( prePage.getEditorInput( ) );
		}
		if ( prePage.isDirty( )
				|| prePage.getStaleType( ) != IPageStaleType.NONE )
		{
			prePage.doSave( null );
			prePage.markPageStale( IPageStaleType.NONE );
			refreshDocument( );
			markPageStale( IPageStaleType.NONE );
		}
		//ser the attribute view disedit.
		ReportRequest request = new ReportRequest( ReportXMLSourceEditorFormPage.this );
		List list = new ArrayList( );
		
		request.setSelectionObject( list );
		request.setType( ReportRequest.SELECTION );

		// SessionHandleAdapter.getInstance().getMediator().pushState();
		SessionHandleAdapter.getInstance( )
				.getMediator( )
				.notifyRequest( request );
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#markPageStale(int)
	 */
	public void markPageStale( int type )
	{
		staleType = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#getStaleType()
	 */
	public int getStaleType( )
	{
		return staleType;
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
			if ( editor != null )
			{
				markPageStale( IPageStaleType.CODE_CHANGED );
				editor.editorDirtyStateChanged( );
			}
		}
		else
		{
			super.firePropertyChange( type );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class required )
	{
		if ( required.equals( ActionRegistry.class ) )
		{
			if ( registry == null )
			{
				registry = new ActionRegistry( );
			}
			return registry;
		}
		return super.getAdapter( required );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.xml.XMLEditor#getProvider()
	 */
	protected IReportProvider getProvider( )
	{
		IReportProvider provider =  (IReportProvider) editor.getAdapter( IReportProvider.class );
		if(provider == null)
		{
			provider = super.getProvider( );
		}
		
		return provider;
	}
}
