/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.chart.integration.wtp.ui.internal.dialogs;

import org.eclipse.birt.chart.integration.wtp.ui.internal.wizards.BirtWebProjectWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jst.j2ee.internal.actions.AbstractOpenWizardWorkbenchAction;

/**
 * Action for new Birt Web Module
 * 
 */
public class NewBirtWebComponentAction extends AbstractOpenWizardWorkbenchAction {

	/**
	 * Create Wizard
	 * 
	 * @see org.eclipse.jst.j2ee.internal.actions.AbstractOpenWizardAction#createWizard()
	 */
	protected Wizard createWizard() {
		return new BirtWebProjectWizard();
	}

	protected boolean shouldAcceptElement(Object obj) {
		return true;
	}
}
