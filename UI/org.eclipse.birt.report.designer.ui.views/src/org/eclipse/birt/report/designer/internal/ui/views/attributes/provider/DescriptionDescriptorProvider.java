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
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class DescriptionDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public String getDisplayName() {
		return Messages.getString("TemplateReportItemPage.description.Label.Instructions"); //$NON-NLS-1$
	}

	@Override
	public Object load() {
		String result = null;
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof TemplateReportItemHandle) {
			TemplateReportItemHandle handle = (TemplateReportItemHandle) DEUtil.getInputFirstElement(input);
			if (handle != null) {
				result = handle.getDescription();
			}
		}
		if (result == null) {
			return ""; //$NON-NLS-1$
		} else {
			return result.trim();
		}
	}

	@Override
	public void save(Object value) throws SemanticException {
		if (value != null && DEUtil.getInputSize(input) == 1
				&& DEUtil.getInputFirstElement(input) instanceof TemplateReportItemHandle) {
			TemplateReportItemHandle handle = (TemplateReportItemHandle) DEUtil.getInputFirstElement(input);
			try {
				String desc = value.toString().trim();
				handle.setDescription(desc);
			} catch (SemanticException e1) {
				e1.printStackTrace();
			}
		}
	}

	private Object input;

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

}
