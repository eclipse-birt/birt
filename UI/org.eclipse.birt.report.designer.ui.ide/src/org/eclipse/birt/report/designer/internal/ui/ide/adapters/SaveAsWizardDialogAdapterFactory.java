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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ide.wizards.SaveReportAsWizard;
import org.eclipse.birt.report.designer.ui.ide.wizards.SaveReportAsWizardDialog;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

/**
 * 
 */

public class SaveAsWizardDialogAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof IEditorPart) {
			IEditorPart part = (IEditorPart) adaptableObject;
			ModuleHandle handle = SessionHandleAdapter.getInstance().creatReportDesign();
			return new SaveReportAsWizardDialog(UIUtil.getDefaultShell(),
					new SaveReportAsWizard(handle, ((IFileEditorInput) part.getEditorInput()).getFile()));

		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { WizardDialog.class };
	}

}
