/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
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
public interface IReportProvider {

	/**
	 * Convert input element to ModuleHandle. This will trigger the actual loading
	 * if necessary.
	 * 
	 * @param element The input element
	 * @return
	 */
	ModuleHandle getReportModuleHandle(Object element);

	/**
	 * Convert input element to ModuleHandle. This will trigger the actual loading
	 * if necessary.
	 * 
	 * @param element The input element
	 * @param reset   Indicate if it enforces the loading.
	 * @return
	 */
	ModuleHandle getReportModuleHandle(Object element, boolean reset);

	/**
	 * Retrieves the ModuelHandle associated with current provider if available.
	 * This will not trigger any actuall loading.
	 * 
	 * @return
	 */
	ModuleHandle queryReportModuleHandle();

	/**
	 * Save moduleHandle to the orginal input.
	 * 
	 * @param moduleHandle
	 * @param element      input element.
	 * @param monitor
	 */
	void saveReport(ModuleHandle moduleHandle, Object element, IProgressMonitor monitor);

	/**
	 * Saves moduleHandle to the current input.
	 * 
	 * @param moduleHandle   The specified module handle which provides the common
	 *                       functionalities of report design and library.
	 * @param element        input element.
	 * @param origReportPath The path of original report.
	 * @param monitor        the progress monitor to use to display progress and
	 *                       receive requests for cancelation.
	 */
	void saveReport(ModuleHandle moduleHandle, Object element, IPath origReportPath, IProgressMonitor monitor);

	/**
	 * Get document provider for report.
	 * 
	 * @param element input element.
	 * @return
	 */
	IDocumentProvider getReportDocumentProvider(Object element);

	/**
	 * Save as path provider utility methods.
	 * 
	 * @param element
	 * @return
	 */
	IPath getSaveAsPath(Object element);

	/**
	 * Create a new report editor input.
	 * 
	 * @param path
	 * @return
	 */
	IEditorInput createNewEditorInput(IPath path);

	/**
	 * Get editor input path.
	 * 
	 * @param input
	 * @return
	 */
	IPath getInputPath(IEditorInput input);

	void connect(ModuleHandle handle);

}
