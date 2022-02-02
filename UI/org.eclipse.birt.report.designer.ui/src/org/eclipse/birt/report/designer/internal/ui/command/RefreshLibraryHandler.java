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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * 
 */

public class RefreshLibraryHandler extends SelectionHandler {

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

		Object obj = getElementHandles();
		// if ( obj instanceof List )
		{
			obj = ((List) obj).get(0);
		}

		if ((obj instanceof LibraryHandle) && ((LibraryHandle) obj).getHostHandle() != null) {
			ModuleHandle host = ((LibraryHandle) obj).getHostHandle();

			try {
				host.reloadLibrary((LibraryHandle) obj);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				retBoolean = false;
			} catch (DesignFileException e) {
				ExceptionHandler.handle(e);
				retBoolean = false;
			}

		} else {
			return reloadAllLibraries(obj);
		}

		UIUtil.refreshCurrentEditorMarkers();

		return Boolean.valueOf(retBoolean);
	}

	private Boolean reloadAllLibraries(Object obj) {
		boolean retBoolean = true;
		if (obj instanceof ReportDesignHandle || obj instanceof LibraryHandle) {
			retBoolean = UIUtil.reloadModuleHandleLibraries((ModuleHandle) obj);
		}
		UIUtil.refreshCurrentEditorMarkers();
		return Boolean.valueOf(retBoolean);
	}
}
