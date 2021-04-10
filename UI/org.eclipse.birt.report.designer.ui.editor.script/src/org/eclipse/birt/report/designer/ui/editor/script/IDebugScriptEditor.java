/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.editor.script;

import org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor;
import org.eclipse.birt.report.model.api.PropertyHandle;

/**
 * Advance interface to supprt script debugger.
 */

public interface IDebugScriptEditor extends IScriptEditor {

	/**
	 * Before set the script text.
	 */
	void beforeChangeContents(PropertyHandle handle);

	/**
	 * Update the script ID.
	 * 
	 * @param id
	 */
	void updateScipt(PropertyHandle handle);

	/**
	 * update the marke
	 */
	void saveDocument();
}
