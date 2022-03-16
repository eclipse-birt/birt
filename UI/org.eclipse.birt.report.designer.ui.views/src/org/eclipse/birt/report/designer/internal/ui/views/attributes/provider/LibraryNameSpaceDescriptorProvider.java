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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class LibraryNameSpaceDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public String getDisplayName() {
		return Messages.getString("LibraryPage.Label.Namespace"); //$NON-NLS-1$
	}

	@Override
	public Object load() {
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof LibraryHandle) {
			LibraryHandle handle = (LibraryHandle) DEUtil.getInputFirstElement(input);
			if (DEUtil.isIncluded(handle)) {
				return handle.getNamespace();
			}
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private Object input;

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

}
