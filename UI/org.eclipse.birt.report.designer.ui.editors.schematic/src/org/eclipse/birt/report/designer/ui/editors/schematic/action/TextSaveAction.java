/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editors.schematic.action;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

/**
 * Action for saving recent changes made in the text editor.
 */
public class TextSaveAction extends TextEditorAction {

	/**
	 * Creates a new action for the given text editor. The action configures its
	 * visual representation from the given resource bundle.
	 * 
	 * @param editor the text editor
	 */
	public TextSaveAction(ITextEditor editor) {
		super(Messages.getReportResourceBundle(), "Editor.Save.", editor); //$NON-NLS-1$
		setActionDefinitionId("org.eclipse.ui.file.save"); //$NON-NLS-1$
		// the action key is no longer work, so we set the action text.
		setText(Messages.getString("MultiPageReportEditor.SaveButton")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		getTextEditor().getSite().getPage().saveEditor(getTextEditor(), false);
	}

	@Override
	public void update() {
		setEnabled(getTextEditor().isDirty());
	}
}
