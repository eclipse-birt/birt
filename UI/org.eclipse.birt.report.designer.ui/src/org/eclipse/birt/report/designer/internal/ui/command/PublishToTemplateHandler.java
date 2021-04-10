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
