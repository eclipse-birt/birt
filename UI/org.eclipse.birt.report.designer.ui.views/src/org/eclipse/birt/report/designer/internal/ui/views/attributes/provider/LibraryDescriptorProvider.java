/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.io.File;
import java.net.URL;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class LibraryDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	private Object input;

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public String getDisplayName() {
		return Messages.getString("GeneralPage.Library.Included"); //$NON-NLS-1$
	}

	@Override
	public Object load() {
		if (input == null) {
			return ""; //$NON-NLS-1$
		}
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement(input);
		if (handle.getExtends() == null) {
			return ""; //$NON-NLS-1$
		}
		String filePath = null;
		try {
			filePath = DEUtil.getFilePathFormURL(new URL(handle.getExtends().getRoot().getFileName()));

		} catch (Exception e) {
			filePath = handle.getExtends().getRoot().getFileName();
		}
		if (filePath != null) {
			File libraryFile = new File(filePath);
			if (libraryFile.exists()) {
				return libraryFile.getAbsolutePath();
			}
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	public void save(Object value) throws SemanticException {
	}

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

}
