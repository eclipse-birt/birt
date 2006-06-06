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

package org.eclipse.birt.report.designer.internal.ui.editors.xml;

import org.eclipse.birt.report.designer.internal.ui.editors.FileReportProvider;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.StatusTextEditor;

public class XMLEditor extends StatusTextEditor
{

	private ColorManager colorManager;
	private IReportProvider provider;

	public XMLEditor( )
	{
		super( );
		colorManager = new ColorManager( );
		setSourceViewerConfiguration( new XMLConfiguration( colorManager ) );
	}

	public void dispose( )
	{
		colorManager.dispose( );
		super.dispose( );
	}

	public void refreshDocument( )
	{
		setInput( getEditorInput( ) );
	}

	protected void firePropertyChange( int property )
	{
		// TODO Auto-generated method stub
		super.firePropertyChange( property );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init( IEditorSite site, IEditorInput input ) throws PartInitException
	{
		IReportProvider provider = getProvider();
		if ( provider != null )
		{
			setDocumentProvider( provider.getReportDocumentProvider( null ) );
		}
		super.init( site, input );
	}

	protected IReportProvider getProvider( )
	{
		if(provider == null)
		{
			provider = new FileReportProvider();
		}
		
		return provider;
		
	}
}
