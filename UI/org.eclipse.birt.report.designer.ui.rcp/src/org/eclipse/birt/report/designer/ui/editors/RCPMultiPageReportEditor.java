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
