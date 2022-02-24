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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * Resource browser viewer SelectionValidator.
 */

public class ResourceSelectionValidator implements ISelectionStatusValidator {

	private boolean multiSelect;

	private boolean acceptFolders;

	private String[] ext;

	private String extWrongMessage;

	private Status EmptyStatus = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("ResourceSelectionValidator.EmptyError"), //$NON-NLS-1$
			null);
	private Status OKStatus = new Status(IStatus.OK, ReportPlugin.REPORT_UI, IStatus.OK, "", null); //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param multiSelect   allow multi select.
	 * @param acceptFolders allow select folder as result.
	 * @param ext           file extension to filter.
	 */
	public ResourceSelectionValidator(boolean multiSelect, boolean acceptFolders, String[] ext) {
		this.multiSelect = multiSelect;
		this.acceptFolders = acceptFolders;
		this.ext = ext;
		if (ext != null) {
			extWrongMessage = Messages.getString("ResourceSelectionValidator.ExtError"); //$NON-NLS-1$
			for (int i = 0; i < ext.length; i++) {
				if (i > 0) {
					extWrongMessage += Messages.getString("ResourceSelectionValidator.ExtErrorSplitter"); //$NON-NLS-1$
				}
				extWrongMessage += ext[i];
			}
		}
	}

	/**
	 * Constructor. This constructor create a validator which not allow mulit
	 * select, not allow folder selection.
	 */
	public ResourceSelectionValidator() {
		this(false, false, null);
	}

	/**
	 * Constructor. No allow mulit select,
	 * 
	 * @param acceptFolders allow select folder as result.
	 */
	public ResourceSelectionValidator(boolean acceptFolders) {
		this(false, acceptFolders, null);
	}

	/**
	 * Constructor. Not allow mulit select,
	 * 
	 * @param acceptFolders allow select folder as result.
	 * @param ext           file extension to filter.
	 */
	public ResourceSelectionValidator(boolean acceptFolders, String[] ext) {
		this(false, acceptFolders, ext);
	}

	/**
	 * Constructor. Not allow mulit select, Not allow select folder as result.
	 * 
	 * @param ext file extension to filter.
	 */
	public ResourceSelectionValidator(String[] ext) {
		this(false, false, ext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.ISelectionStatusValidator#validate(java.lang.Object[])
	 */
	public IStatus validate(Object[] selection) {
		int nSelected = selection.length;
		String pluginId = ReportPlugin.REPORT_UI;

		if (nSelected == 0 || (nSelected > 1 && multiSelect == false)) {
			return EmptyStatus;
		}
		for (int i = 0; i < selection.length; i++) {
			Object curr = selection[i];
			if (curr instanceof ResourceEntry) {
				ResourceEntry resource = (ResourceEntry) curr;
				if (acceptFolders == false && !resource.isFile()) {
					return EmptyStatus;
				}
				if (ext != null) {
					boolean isCorrectExt = false;
					String fileName = resource.getName().toLowerCase();
					for (int j = 0; j < ext.length; j++) {
						if (fileName.endsWith(ext[j].toLowerCase())) {
							isCorrectExt = true;
							break;
						}
					}
					if (!isCorrectExt) {
						return new Status(IStatus.ERROR, pluginId, IStatus.ERROR, extWrongMessage, null);
					}
				}
			} else {
				return EmptyStatus;
			}
		}
		return OKStatus;
	}
}
