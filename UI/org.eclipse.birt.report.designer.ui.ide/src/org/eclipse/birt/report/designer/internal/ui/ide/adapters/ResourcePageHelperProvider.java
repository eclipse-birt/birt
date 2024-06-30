/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.ui.preferences.ResourceConfigurationBlock;
import org.eclipse.birt.report.designer.ui.preferences.TemplateConfigurationBlock;

/**
 *
 */

public class ResourcePageHelperProvider implements IDialogHelperProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.internal.ui.dialogs.helper.
	 * IDialogHelperProvider#createHelper(java.lang.Object, java.lang.String)
	 */
	@Override
	public IDialogHelper createHelper(Object container, String helperKey) {
		if (container instanceof ResourceConfigurationBlock) {
			if (ResourceConfigurationBlock.BUTTON_KEY.equals(helperKey)) {
				return new IDEResourcePageHelper();
			}
		} else if (container instanceof TemplateConfigurationBlock) {
			if (TemplateConfigurationBlock.BUTTON_KEY.equals(helperKey)) {
				IDEResourcePageHelper helper = new IDEResourcePageHelper();
				helper.setButtonLabels(new String[] { IDEResourcePageHelper.FILESYSTEM_BUTTON,
						IDEResourcePageHelper.VARIABLES_BUTTON });
				// helper.setButonAlignment( SWT.BEGINNING );
				return helper;
			}
		}

		return null;
	}
}
