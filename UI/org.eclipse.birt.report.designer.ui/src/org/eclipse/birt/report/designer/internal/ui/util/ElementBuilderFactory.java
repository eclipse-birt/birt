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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.ImageBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.TextEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

public class ElementBuilderFactory {

	static private ElementBuilderFactory instance;

	private ElementBuilderFactory() {
	}

	/**
	 * @return instance of factory.
	 */
	static public ElementBuilderFactory getInstance() {
		if (instance == null) {
			instance = new ElementBuilderFactory();
		}
		return instance;
	}

	/**
	 * Creates builder for given element
	 *
	 * @param handle
	 * @return
	 */
	public Object createBuilder(DesignElementHandle handle) {
		if (handle instanceof TextItemHandle) {
			return new TextEditor(UIUtil.getDefaultShell(), TextEditor.DLG_TITLE_NEW, (TextItemHandle) handle);
		}
		if (handle instanceof TextDataHandle) {
			ExpressionBuilder dialog = new ExpressionBuilder(UIUtil.getDefaultShell(),
					((TextDataHandle) handle).getValueExpr());

			dialog.setExpressionProvier(new ExpressionProvider(handle));

			return (dialog);
		}
		if (handle instanceof DataItemHandle) {
			ColumnBindingDialog dialog = new ColumnBindingDialog((ReportItemHandle) handle, UIUtil.getDefaultShell(),
					true);
			dialog.setGroupList(DEUtil.getGroups(handle));
			return (dialog);
		}
		if (handle instanceof ImageHandle) {
			ImageBuilder dialog = new ImageBuilder(UIUtil.getDefaultShell(), ImageBuilder.DLG_TITLE_NEW);
			dialog.setInput(handle);
			return dialog;
		}

		return null;
	}
}
