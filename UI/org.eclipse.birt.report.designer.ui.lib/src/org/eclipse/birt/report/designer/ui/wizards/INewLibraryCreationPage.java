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

package org.eclipse.birt.report.designer.ui.wizards;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * INewLibraryCreationPage
 */
public interface INewLibraryCreationPage extends IWizardPage {

	void setContainerFullPath(IPath initPath);

	void setFileName(String initFileName);

	IPath getContainerFullPath();

	String getFileName();

	boolean performFinish();

	void updatePerspective(IConfigurationElement configElement);
}
