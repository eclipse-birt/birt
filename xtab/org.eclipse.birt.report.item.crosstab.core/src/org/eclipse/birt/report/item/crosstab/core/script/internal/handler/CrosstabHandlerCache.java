/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.script.internal.handler;

import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * CrosstabHandlerCache
 */
public class CrosstabHandlerCache {

	private HashMap<DesignElementHandle, DesignElementHandle> cell2crosstabCache;
	private HashMap<DesignElementHandle, String> cell2createScriptCache;
	private HashMap<DesignElementHandle, CrosstabCreationHandler> crosstab2createHandlerCache;
	private HashMap<DesignElementHandle, String> cell2renderScriptCache;
	private HashMap<DesignElementHandle, CrosstabRenderingHandler> crosstab2renderHandlerCache;

	public CrosstabHandlerCache() {
		cell2crosstabCache = new HashMap<DesignElementHandle, DesignElementHandle>();
		cell2createScriptCache = new HashMap<DesignElementHandle, String>();
		crosstab2createHandlerCache = new HashMap<DesignElementHandle, CrosstabCreationHandler>();
		cell2renderScriptCache = new HashMap<DesignElementHandle, String>();
		crosstab2renderHandlerCache = new HashMap<DesignElementHandle, CrosstabRenderingHandler>();
	}

	private DesignElementHandle getCrosstabHandle(DesignElementHandle cellHandle) {
		DesignElementHandle crosstab = cell2crosstabCache.get(cellHandle);

		if (crosstab == null) {
			DesignElementHandle e = cellHandle;
			while (e != null) {
				if (ICrosstabConstants.CROSSTAB_EXTENSION_NAME
						.equals(e.getStringProperty(ExtendedItemHandle.EXTENSION_NAME_PROP))) {
					crosstab = e;
					cell2crosstabCache.put(cellHandle, crosstab);
					break;
				}
				e = e.getContainer();
			}
		}

		return crosstab;
	}

	public String getOnCreateScript(DesignElementHandle cellHandle) {
		String onCreate = cell2createScriptCache.get(cellHandle);

		if (onCreate == null) {
			ExtendedItemHandle crosstabHandle = (ExtendedItemHandle) getCrosstabHandle(cellHandle);

			if (crosstabHandle != null) {
				onCreate = crosstabHandle.getEventHandlerClass();

				if (onCreate == null || onCreate.trim().length() == 0) {
					onCreate = crosstabHandle.getOnCreate();
				}

				onCreate = onCreate == null ? "" //$NON-NLS-1$
						: onCreate.trim();

				cell2createScriptCache.put(cellHandle, onCreate);
			}
		}

		return onCreate;
	}

	public String getOnRenderScript(DesignElementHandle cellHandle) {
		String onRender = cell2renderScriptCache.get(cellHandle);

		if (onRender == null) {
			ExtendedItemHandle crosstabHandle = (ExtendedItemHandle) getCrosstabHandle(cellHandle);

			if (crosstabHandle != null) {
				onRender = crosstabHandle.getEventHandlerClass();

				if (onRender == null || onRender.trim().length() == 0) {
					onRender = crosstabHandle.getOnRender();
				}

				onRender = onRender == null ? "" //$NON-NLS-1$
						: onRender.trim();

				cell2renderScriptCache.put(cellHandle, onRender);
			}
		}

		return onRender;
	}

	public CrosstabCreationHandler getCreateHandler(DesignElementHandle cellHandle, ClassLoader contextLoader)
			throws BirtException {
		ExtendedItemHandle crosstabModelHandle = (ExtendedItemHandle) getCrosstabHandle(cellHandle);

		CrosstabCreationHandler handler = crosstab2createHandlerCache.get(crosstabModelHandle);

		if (handler == null) {
			handler = new CrosstabCreationHandler(crosstabModelHandle, contextLoader);

			crosstab2createHandlerCache.put(crosstabModelHandle, handler);
		}

		return handler;
	}

	public CrosstabRenderingHandler getRenderHandler(DesignElementHandle cellHandle, ClassLoader contextLoader)
			throws BirtException {
		ExtendedItemHandle crosstabModelHandle = (ExtendedItemHandle) getCrosstabHandle(cellHandle);

		CrosstabRenderingHandler handler = crosstab2renderHandlerCache.get(crosstabModelHandle);

		if (handler == null) {
			handler = new CrosstabRenderingHandler(crosstabModelHandle, contextLoader);

			crosstab2renderHandlerCache.put(crosstabModelHandle, handler);
		}

		return handler;
	}

	public void dispose() {
		cell2crosstabCache.clear();
		cell2createScriptCache.clear();
		crosstab2createHandlerCache.clear();
		cell2renderScriptCache.clear();
		crosstab2renderHandlerCache.clear();

		cell2crosstabCache = null;
		cell2createScriptCache = null;
		crosstab2createHandlerCache = null;
		cell2renderScriptCache = null;
		crosstab2renderHandlerCache = null;
	}

}
