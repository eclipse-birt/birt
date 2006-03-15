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

package org.eclipse.birt.report.designer.ui.preview.editors;

import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * Preview page.
 */
public class ReportPreviewFormPage extends ReportPreviewEditor implements
		IReportEditorPage
{

	public static final String ID = "BIRT.Preivew"; //$NON-NLS-1$

	private Control control;

	private int staleType;


	private FormEditor editor;


	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#onBroughtToTop(org.eclipse.birt.report.designer.ui.editors.IReportEditorPage)
	 */
	public boolean onBroughtToTop( IReportEditorPage prePage )
	{
		if ( getEditorInput( ) != prePage.getEditorInput( ) )
		{
			setInput( prePage.getEditorInput( ) );
		}
		if ( prePage.isDirty( ) )
		{
			prePage.doSave( null );
		}
		if ( getBrowser( ) != null )
		{
			display( );
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	public Control getPartControl( )
	{
		return control;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.IFormPage#getId()
	 */
	public String getId( )
	{
		return ID;
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#markPageStale(int)
	 */
	public void markPageStale( int type )
	{
		staleType = type;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#getStaleType()
	 */
	public int getStaleType( )
	{
		return staleType;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty( )
	{
		return false;
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

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class adapter )
	{
		if ( adapter.equals( ActionRegistry.class ) )
		{
			return new ActionRegistry( );
		}
		return super.getAdapter( adapter );
	}

	public void initialize( FormEditor editor )
	{
		this.editor = editor;
		
	}

	public FormEditor getEditor( )
	{
		return editor;
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

	public int getIndex( )
	{
		return 0;
	}

	public void setIndex( int index )
	{
		
	}

	public boolean isEditor( )
	{
		return true;
	}

	public boolean selectReveal( Object object )
	{
		return false;
	}
	
	public void setInput(IEditorInput input)
	{
		super.setInput( input );
	}
}
