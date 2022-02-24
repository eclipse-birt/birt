/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 *
 */

public class LibraryAttributePage extends AttributePage {

	@Override
	public void refresh() {
		Section[] sectionArray = getSections();
		for (int i = 0; i < sectionArray.length; i++) {
			Section section = (Section) sectionArray[i];
			section.setInput(input);
			if (checkLibraryReadOnly) {
				section.setReadOnly(isLibraryReadOnly());
			}
			section.load();
		}
		FormWidgetFactory.getInstance().paintFormStyle(container);
		FormWidgetFactory.getInstance().adapt(container);
	}

	protected boolean isLibraryReadOnly() {
		GroupElementHandle elementHandle = null;
		if (input instanceof GroupElementHandle) {
			elementHandle = ((GroupElementHandle) input);

		} else if (input instanceof List) {
			elementHandle = DEUtil.getGroupElementHandle((List) input);
		}
		if (elementHandle != null) {
			if (elementHandle.getModuleHandle() instanceof ReportDesignHandle
					&& DEUtil.getInputFirstElement(elementHandle) instanceof LibraryHandle) {
				return true;
			}
		}
		return false;
	}

	private boolean checkLibraryReadOnly = false;

	protected void needCheckLibraryReadOnly(boolean readOnley) {
		this.checkLibraryReadOnly = readOnley;
	}
}
