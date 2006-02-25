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

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * IReportProvider
 */
public interface IReportProvider
{

	/**
	 * Convert input element to ModuleHandle.
	 * @param element
	 * @return
	 */
	ModuleHandle getReportModuleHandle( Object element );


	/**
	 * Convert input element to ModuleHandle.
	 * @param element
	 * @return
	 */
	ModuleHandle getReportModuleHandle( Object element,boolean reset );

	
	/**
	 * Save moduleHandle to the orginal input.
	 * @param moduleHandle
	 * @param element input element.
	 * @param monitor
	 */
	void saveReport( ModuleHandle moduleHandle, Object element,
			IProgressMonitor monitor );

	/**
	 * Get document provider for report.
	 * @param element input element.
	 * @return
	 */
	IDocumentProvider getReportDocumentProvider( Object element );
	
	/**
	 * Save as path provider utility methods.
	 * @param element
	 * @return
	 */
	IPath getSaveAsPath( Object element );
	
	/**
	 * Create a new report editor input.
	 * @param path
	 * @return
	 */
	IEditorInput createNewEditorInput( IPath path );
	
	/**
	 * Get editor input path.
	 * @param input
	 * @return
	 */
	IPath getInputPath(IEditorInput input);

}
