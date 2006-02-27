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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.xml.XMLEditor;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.IPageStaleType;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * XML editor for report source file.
 */
public class ReportXMLSourceEditorFormPage extends XMLEditor implements
		IReportEditorPage
{

	public static final String ID = "BIRT.XMLSourceFormPage"; //$NON-NLS-1$

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
		Object adapter = ( (IEditorPart) this ).getAdapter( IReportProvider.class );
		if ( adapter != null && isValidModelFile( ) )
		{
			IReportProvider provider = (IReportProvider) adapter;
			ModuleHandle model = provider.getReportModuleHandle( getEditorInput( ),
					true );
			SessionHandleAdapter.getInstance( ).setReportDesignHandle( model );
		}
	}

	private boolean isValidModelFile( )
	{
		IEditorInput input = getEditorInput( );

		if ( !( input instanceof IPathEditorInput ) )
		{
			return false;
		}
		boolean validModel = false;
		IPath path = ( (IPathEditorInput) input ).getPath( );
		try
		{
			if ( path.toOSString( )
					.endsWith( IReportEditorContants.LIBRARY_FILE_EXTENTION ) )
			{
				validModel = ModuleUtil.isValidLibrary( SessionHandleAdapter.getInstance( )
						.getSessionHandle( ),
						path.toOSString( ),
						new FileInputStream( new File( path.toOSString( ) ) ) );
			}
			else
			{
				validModel = ModuleUtil.isValidDesign( SessionHandleAdapter.getInstance( )
						.getSessionHandle( ),
						path.toOSString( ),
						new FileInputStream( new File( path.toOSString( ) ) ) );

			}
			return validModel;
		}
		catch ( FileNotFoundException e )
		{
			return validModel;
		}
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
		boolean validModel = false;

		validModel = isValidModelFile( );

		if ( !validModel )
		{
			Display.getCurrent( ).beep( );
			MessageDialog.openError( Display.getCurrent( ).getActiveShell( ),
					Messages.getString( "XMLSourcePage.Error.Dialog.title" ), //$NON-NLS-1$
					Messages.getString( "XMLSourcePage.Error.Dialog.Message.InvalidFile" ) ); //$NON-NLS-1$
			return false;
		}
		return true;
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

	/*
	 * 
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
}
