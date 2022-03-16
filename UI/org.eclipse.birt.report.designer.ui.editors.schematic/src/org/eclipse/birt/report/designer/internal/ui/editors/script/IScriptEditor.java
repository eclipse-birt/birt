/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import org.eclipse.birt.report.designer.internal.ui.script.JSSyntaxContext;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;

/**
 * An editor for script.
 */
public interface IScriptEditor extends IEditorPart {

	/**
	 * Returns the context about script
	 *
	 * @return the context about script
	 */
	JSSyntaxContext getContext();

	/**
	 * Returns the editor's source viewer. May return <code>null</code> before the
	 * editor's part has been created and after disposal.
	 *
	 * @return the editor's source viewer which may be <code>null</code>
	 */
	ISourceViewer getViewer();

	/**
	 * Returns the current script.
	 *
	 * @return the current script.
	 */
	String getScript();

	/**
	 * Sets the script text to edit.
	 *
	 * @param script the script text
	 */
	void setScript(String text);

	/**
	 * Returns the action registry.
	 *
	 * @return the action registry.
	 */
	ActionRegistry getActionRegistry();
}
