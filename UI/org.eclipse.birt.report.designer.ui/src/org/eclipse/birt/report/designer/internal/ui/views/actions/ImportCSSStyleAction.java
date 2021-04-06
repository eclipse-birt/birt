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

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.SelectCssStyleWizard;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

/**
 * Imports CSS Style action.
 */

public class ImportCSSStyleAction extends AbstractViewAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction"; //$NON-NLS-1$

	public static final String ACTION_TEXT = Messages.getString("ImportCSSStyleAction.text"); //$NON-NLS-1$

	public ImportCSSStyleAction(Object selectedObject) {
		this(selectedObject, ACTION_TEXT);
	}

	public ImportCSSStyleAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	public void run() {
		Dialog dialog = new BaseWizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				new SelectCssStyleWizard(getSelection()));
		dialog.open();

		// FileDialog fd = new FileDialog( PlatformUI.getWorkbench( )
		// .getDisplay( )
		// .getActiveShell( ), SWT.OPEN );
		// fd.setFilterExtensions( new String[]{
		// "*.css"} );//$NON-NLS-1$
		// fd.setFilterNames( new String[]{
		// "CSS Style file" + " (css)" //$NON-NLS-1$ //$NON-NLS-2$
		// } );
		//
		// String file = fd.open( );
		// if ( file != null )
		// {
		// Dialog dialog = new ImportCSSStyleDialog( "Import CSS Style", file );
		// //$NON-NLS-1$
		// dialog.open( );
		// }
		//
	}

	public boolean isEnabled() {
		return true;
	}
}
