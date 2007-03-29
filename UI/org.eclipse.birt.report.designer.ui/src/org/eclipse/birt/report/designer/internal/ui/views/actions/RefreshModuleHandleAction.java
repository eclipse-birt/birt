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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.ui.IEditorPart;

/**
 * 
 */

public class RefreshModuleHandleAction extends AbstractViewAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshModuleHandleAction"; //$NON-NLS-1$

	public static final String ACTION_TEXT = Messages
			.getString("RefreshModuleHandleAction.Action.Text"); //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public RefreshModuleHandleAction(Object selectedObject) {
		super(selectedObject, ACTION_TEXT);
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public RefreshModuleHandleAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled() {
		if (getSelection() instanceof ReportDesignHandle
				|| getSelection() instanceof LibraryHandle) {
			return true;
		} else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Object obj = getSelection();

		if ((obj instanceof LibraryHandle)
				&& (((LibraryHandle) obj).getHostHandle() != obj)) {
			ModuleHandle host = ((LibraryHandle) obj).getHostHandle();
			if ((host == null) || (!host.isInclude((LibraryHandle) obj))) {
				return;
			}
			try {
				host.reloadLibrary((LibraryHandle) obj);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				return;
			} catch (DesignFileException e) {
				ExceptionHandler.handle(e);
				return;
			}

		} else {
			reloadAllLibraries(obj);
		}

	}

	private Boolean reloadAllLibraries(Object obj) {
		boolean retBoolean = true;
		if (obj instanceof ReportDesignHandle || obj instanceof LibraryHandle) {
			ModuleHandle moduleHandle = (ModuleHandle) obj;

			try {
				moduleHandle.reloadLibraries();
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				retBoolean = false;
			} catch (DesignFileException e) {
				ExceptionHandler.handle(e);
				retBoolean = false;
			}

		}
		return new Boolean(retBoolean);
	}
}
