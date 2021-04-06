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

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Show properties of selected element.
 */

public class ShowPropertyAction extends Action {

	private Object model;

	public ShowPropertyAction(Object model) {
		setText(Messages.getString("ShowPropertyAction.text")); //$NON-NLS-1$
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		return model instanceof DesignElementHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Show property action >> Run ..."); //$NON-NLS-1$
		}
		showView();
		// ReportEditor editor = UIUtil.getActiveReportEditor( );
		// IViewReference[] viewReference = editor.getSite( )
		// .getPage( )
		// .getViewReferences( );
		// for ( int i = 0; i < viewReference.length; i++ )
		// {
		// IViewPart viewPart = viewReference[i].getView( false );
		// if ( viewPart instanceof PropertySheet )
		// {
		// ( (PropertySheet) viewPart ).selectionChanged( editor,
		// new StructuredSelection( model ) );
		// ( (PropertySheet) viewPart ).partActivated( editor );
		// }
		// }
	}

	private void showView() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			try {
				page.showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
			} catch (PartInitException e) {
				ExceptionHandler.handle(e);
			}
		}
	}
}