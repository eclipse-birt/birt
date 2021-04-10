/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public boolean performFinish() {
		return page.performFinish();
	}
}
