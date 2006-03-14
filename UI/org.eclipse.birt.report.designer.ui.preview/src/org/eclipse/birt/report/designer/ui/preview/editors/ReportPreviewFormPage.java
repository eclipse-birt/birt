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
import org.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

/**
 * Preview page.
 */
public class ReportPreviewFormPage extends ReportFormPage implements
		IReportEditorPage
{

	public static final String ID = "BIRT.Preivew"; //$NON-NLS-1$

	private Control control;

	private int staleType;

	private ReportPreviewEditor previewEditor = new ReportPreviewEditor( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void init( IEditorSite site, IEditorInput input )
			throws PartInitException
	{
		super.init( site, input );
		previewEditor.init( site, input );
	}

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
		if ( previewEditor.getBrowser( ) != null )
		{
			previewEditor.display( );
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
		// Control[] children = parent.getChildren();
		control = new Composite( parent, SWT.NONE );
		previewEditor.createPartControl( (Composite) control );
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
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave( IProgressMonitor monitor )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs( )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty( )
	{
		return false;
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
	
}
