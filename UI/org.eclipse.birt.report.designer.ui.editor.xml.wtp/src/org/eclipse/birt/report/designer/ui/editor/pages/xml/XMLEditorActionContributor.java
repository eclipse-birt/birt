/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.editor.pages.xml;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * 
 */

public class XMLEditorActionContributor extends TextEditorActionContributor {
	public void setActiveEditor(IEditorPart part) {
		if (part.getAdapter(ITextEditor.class) != null) {
			super.setActiveEditor((ITextEditor) part.getAdapter(ITextEditor.class));
		} else {
			super.setActiveEditor(part);
		}
	}
}
