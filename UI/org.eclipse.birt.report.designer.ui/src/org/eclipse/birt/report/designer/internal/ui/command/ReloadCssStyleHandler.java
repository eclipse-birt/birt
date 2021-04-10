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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * 
 */

public class ReloadCssStyleHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean retBoolean = true;
		super.execute(event);
		Object container = null;

		Object obj = getElementHandles();

		// if ( obj instanceof List )
		{
			List tmpList = (List) obj;
			if (tmpList.size() < 1)
				return Boolean.TRUE;
			obj = tmpList.get(0);
		}
		if (obj instanceof CssStyleSheetHandle) {
			container = ((CssStyleSheetHandle) obj).getContainerHandle();
		} else if (obj instanceof ReportDesignHandle || obj instanceof AbstractThemeHandle) {
			container = obj;
			obj = null;
		}

		retBoolean = reloadCss((CssStyleSheetHandle) obj, container);
		return Boolean.valueOf(retBoolean);
	}

	private boolean reloadCss(CssStyleSheetHandle cssCtyleSheetHandle, Object container) {
		if (cssCtyleSheetHandle == null) {
			List cssStyleSheet = null;
			if (container instanceof ReportDesignHandle) {
				cssStyleSheet = ((ReportDesignHandle) container).getAllCssStyleSheets();
				if (cssStyleSheet == null) {
					return true;
				}
				for (int i = 0; i < cssStyleSheet.size(); i++) {
					try {
						((ReportDesignHandle) container).reloadCss((CssStyleSheetHandle) cssStyleSheet.get(i));
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
						return false;
					}
				}

			} else if (container instanceof AbstractThemeHandle) {
				cssStyleSheet = ((AbstractThemeHandle) container).getAllCssStyleSheets();
				if (cssStyleSheet == null) {
					return true;
				}
				for (int i = 0; i < cssStyleSheet.size(); i++) {
					try {
						((AbstractThemeHandle) container).reloadCss((CssStyleSheetHandle) cssStyleSheet.get(i));
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
						return false;
					}
				}
			}

		} else {
			if (container instanceof ReportDesignHandle) {
				try {
					((ReportDesignHandle) container).reloadCss(cssCtyleSheetHandle);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
					return false;
				}
			} else if (container instanceof AbstractThemeHandle) {
				try {
					((AbstractThemeHandle) container).reloadCss(cssCtyleSheetHandle);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
					return false;
				}
			}
		}

		UIUtil.refreshCurrentEditorMarkers();

		return true;
	}
}
