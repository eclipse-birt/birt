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

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishLibraryWizard;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.wizard.WizardDialog;

/**
 *
 */

public class PublishToLibraryHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		super.execute(event);

//		String filePath = SessionHandleAdapter.getInstance( )
//				.getReportDesignHandle( )
//				.getFileName( );
//		String fileName = filePath.substring( filePath.lastIndexOf( File.separator ) + 1 );

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		String fileName = (String) UIUtil.getVariableFromContext(context,
				ICommandParameterNameContants.PUBLISH_LIBRARY_FILENAME);
		LibraryHandle libHandle = (LibraryHandle) UIUtil.getVariableFromContext(context,
				ICommandParameterNameContants.PUBLISH_LIBRARY_LIBRARY_HANDLE);

		PublishLibraryWizard publishLibrary = new PublishLibraryWizard(libHandle, fileName,
				ReportPlugin.getDefault().getResourceFolder());

		WizardDialog dialog = new BaseWizardDialog(UIUtil.getDefaultShell(), publishLibrary);

		dialog.setPageSize(500, 250);
		dialog.open();

		return Boolean.TRUE;
	}

}
