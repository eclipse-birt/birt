/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

/**
 *
 */

public class RCPReportDocumentEditor extends ReportDocumentEditor {

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		if (input instanceof ReportEditorInput) {
			ReportEditorInput reportInput = (ReportEditorInput) input;
			String fileName = reportInput.getFile().getAbsolutePath();
			setFileName(fileName);

			int index = fileName.lastIndexOf(File.separator);

			setPartName(fileName.substring(index + 1));

		}

	}
}
