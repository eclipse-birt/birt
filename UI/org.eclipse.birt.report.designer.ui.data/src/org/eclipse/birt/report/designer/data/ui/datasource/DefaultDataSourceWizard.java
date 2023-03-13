/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.datasource;

import org.eclipse.jface.wizard.Wizard;

/**
 * TODO: Please document
 *
 * @version $Revision: 1.2 $ $Date: 2006/07/19 07:06:03 $
 */
public class DefaultDataSourceWizard extends Wizard {

	private DataSourceSelectionPage page = new DataSourceSelectionPage("datasourceselection"); //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public DefaultDataSourceWizard() {
		super();
		addPage(page);
		setForcePreviousAndNextButtons(true);
	}

	/*
	 *
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return page.performFinish();
	}
}
