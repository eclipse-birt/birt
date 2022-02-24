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

package org.eclipse.birt.integration.wtp.ui.internal.wizards;

import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.servlet.ui.project.facet.WebProjectWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * Implement a wizard for creating a new BIRT Web Project. This wizard extends
 * "Dynamic Web Project" wizard.
 * 
 */
public class BirtWebProjectWizard extends WebProjectWizard implements IBirtWizardConstants {

	/**
	 * Configuration Element of birt wizard
	 */
	private IConfigurationElement wizardConfigElement;

	/**
	 * Constructor
	 * 
	 */
	public BirtWebProjectWizard() {
		super();
		setWindowTitle(BirtWTPMessages.BIRTProjectCreationWizard_title);
		setNeedsProgressMonitor(true);
	}

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public BirtWebProjectWizard(IDataModel model) {
		super(model);
		setWindowTitle(BirtWTPMessages.BIRTProjectCreationWizard_title);
		setNeedsProgressMonitor(true);
	}

	/**
	 * Get template for project facets selection
	 */
	protected IFacetedProjectTemplate getTemplate() {
		return ProjectFacetsManager.getTemplate("template.birt.runtime"); //$NON-NLS-1$
	}

	/**
	 * Initialize wizard
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);

		// find configuration element of new wizard
		this.wizardConfigElement = BirtWizardUtil.findConfigurationElementById(NEW_WIZARDS_EXTENSION_POINT,
				BIRT_WIZARD_ID);

		// set window title
		String title = wizardConfigElement.getAttribute("name"); //$NON-NLS-1$
		if (title != null)
			setWindowTitle(title);

	}
}
