/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

/**
 * Use this class to activate RCP plug-in.
 */

/**
 * RCPMultiPageReportEditor
 */
public class RCPMultiPageReportEditor extends MultiPageReportEditor {

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
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor#init(org.
	 * eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor#dispose()
	 */
	public void dispose() {
		super.dispose();
		getSite().getWorkbenchWindow().getPartService().removePartListener(this);
	}

	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		try {
			refreshMarkers(getEditorInput());
		} catch (CoreException e) {
		}
	}

	public void doSaveAs() {
		// TODO Auto-generated method stub
		super.doSaveAs();

		String resource = new Path(getModel().getFileName()).removeLastSegments(1).toOSString();
		getModel().setResourceFolder(resource);
		SessionHandleAdapter.getInstance().getSessionHandle()
				.fireResourceChange(new LibraryChangeEvent(getModel().getFileName()));
	}

	@Override
	public void refreshMarkers(IEditorInput input) throws CoreException {
		ModuleHandle reportDesignHandle = getModel();
		if (reportDesignHandle != null) {
			reportDesignHandle.checkReport();
		}
	}

}
