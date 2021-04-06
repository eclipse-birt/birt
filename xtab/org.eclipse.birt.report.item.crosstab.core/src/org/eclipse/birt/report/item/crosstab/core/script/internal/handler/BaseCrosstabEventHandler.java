/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal.handler;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;

/**
 * BaseCrosstabEventHandler
 */
public abstract class BaseCrosstabEventHandler {

	BaseCrosstabEventHandler() {
	}

	protected CrosstabScriptHandler createScriptHandler(DesignElementHandle modelHandle, String methodName,
			String script, ClassLoader contextLoader) throws BirtException {
		CrosstabScriptHandler handler = new CrosstabScriptHandler();

		handler.init(null);

		String eventClass = modelHandle.getEventHandlerClass();

		if (eventClass != null && eventClass.trim().length() > 0) {
			// handle java class
			handler.register(null, eventClass.trim(), contextLoader);
		} else {
			// handle java script
			// TODO check debug?
			String id = ModuleUtil.getScriptUID(modelHandle.getPropertyHandle(methodName));

			handler.register(id, script, contextLoader);
		}

		return handler;
	}
}
