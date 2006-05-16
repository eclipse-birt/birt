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

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

/**
 * Use this class to activate RCP plug-in.
 */

public class RCPMultiPageReportEditor extends MultiPageReportEditor
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void init( IEditorSite site, IEditorInput input )
			throws PartInitException
	{
		super.init( site, input );
		getSite( ).getWorkbenchWindow( )
				.getPartService( )
				.addPartListener( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor#dispose()
	 */
	public void dispose( )
	{
		super.dispose( );
		getSite( ).getWorkbenchWindow( )
				.getPartService( )
				.removePartListener( this );
	}

}
