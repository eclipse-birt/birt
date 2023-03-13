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

package org.eclipse.birt.report.debug.internal.ui.script.outline.node;

import java.io.File;

import org.eclipse.birt.report.debug.internal.ui.script.editor.DebugJsEditor;
import org.eclipse.birt.report.debug.internal.ui.script.editor.DebugJsInput;
import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ScriptTreeNodeProvider;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.gef.Request;

/**
 * All script element provider.
 */

public class ScriptDebugTreeNodeProvider extends ScriptTreeNodeProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * ScriptTreeNodeProvider#performRequest(java.lang.Object,
	 * org.eclipse.gef.Request)
	 */
	@Override
	public boolean performRequest(Object model, Request request) throws Exception {
		if (request.getType().equals(IRequestConstants.REQUEST_TYPE_EDIT)) {
			return performEdit(model);
		}
		return false;
	}

	private boolean performEdit(Object handle) {
		if (!(handle instanceof DebugScriptObjectNode)) {
			return false;
		}
		DebugScriptObjectNode nodeObject = (DebugScriptObjectNode) handle;
		PropertyHandle propertyHandle = nodeObject.getPropertyHandle();
		String id = ModuleUtil.getScriptUID(propertyHandle);

		DebugJsInput input = new DebugJsInput(
				new File(propertyHandle.getElementHandle().getModuleHandle().getFileName()), id);

		DebugJsEditor editor = ScriptDebugUtil.getActiveJsEditor();

		if (editor != null) {
			editor.setInput(input);
		}

		return true;
	}
}
