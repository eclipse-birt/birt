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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishTemplateWizard;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

/**
 *
 */

public class PublishToTemplateHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean retBoolean = true;
		super.execute(event);

		WizardDialog dialog = new BaseWizardDialog(UIUtil.getDefaultShell(), new PublishTemplateWizard(
				(ReportDesignHandle) SessionHandleAdapter.getInstance().getReportDesignHandle()));
		dialog.setPageSize(500, 250);
		dialog.open();

		return Boolean.valueOf(retBoolean);
	}
}
