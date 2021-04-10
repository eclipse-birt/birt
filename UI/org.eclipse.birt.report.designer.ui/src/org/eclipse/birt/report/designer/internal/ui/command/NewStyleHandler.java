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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.StyleBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class NewStyleHandler extends SelectionHandler {

	private static final String STACK_MSG_ADD_STYLE = Messages.getString("NewStyleHandler.transaction.label"); //$NON-NLS-1$

	private AbstractThemeHandle themeHandle;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);
		boolean retBoolean = true;

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Add Style rule action >> Run ..."); //$NON-NLS-1$
		}
		CommandStack stack = getActiveCommandStack();
		stack.startTrans(STACK_MSG_ADD_STYLE);

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.NEW_STYLE_THEME_HANDLE_NAME);
		if (obj instanceof AbstractThemeHandle) {
			themeHandle = (AbstractThemeHandle) obj;
		} else {
			themeHandle = null;
		}

		ModuleHandle reportDesignHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		StyleHandle styleHandle = themeHandle != null
				? DesignElementFactory.getInstance(reportDesignHandle).newStyle(themeHandle, null)
				: DesignElementFactory.getInstance(reportDesignHandle).newStyle(null);

		try {
			StyleBuilder dialog = new StyleBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(), styleHandle,
					themeHandle, StyleBuilder.DLG_TITLE_NEW);
			if (dialog.open() == Window.OK) {
				if (themeHandle != null) {
					themeHandle.getStyles().add(styleHandle);
				} else {
					reportDesignHandle.getStyles().add(styleHandle);
					if (!styleHandle.isPredefined()) {
						applyStyle((SharedStyleHandle) styleHandle);
					}
				}
				stack.commit();
			} else {
				stack.rollbackAll();
				retBoolean = false;
			}
		} catch (Exception e) {
			stack.rollbackAll();
			ExceptionHandler.handle(e);
			retBoolean = false;
		}

		return Boolean.valueOf(retBoolean);
	}

	/**
	 * Applys style to selected elements.
	 * 
	 * @param styleHandle
	 */
	private void applyStyle(SharedStyleHandle styleHandle) {
		Object elements = getElementHandles();
		List handles = new ArrayList();
//		if ( elements instanceof List )
//		{
		handles = (List) elements;
//		}
//		else
//		{
//			handles.add( elements );
//		}
		for (int i = 0; i < handles.size(); i++) {
			try {
				if (handles.get(i) instanceof ReportElementHandle) {
					// set style
					((DesignElementHandle) handles.get(i)).setStyle(styleHandle);
				}
			} catch (StyleException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}
