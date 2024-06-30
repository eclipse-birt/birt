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

package org.eclipse.birt.report.designer.internal.ui.editors;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 *
 */

public class ReportTextEditorActionContributor extends BasicTextEditorActionContributor {
	@Override
	public void setActiveEditor(IEditorPart part) {
		if (part.getAdapter(ITextEditor.class) != null) {
			super.setActiveEditor((ITextEditor) part.getAdapter(ITextEditor.class));
		} else {
			super.setActiveEditor(part);
		}
	}
}
