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

package org.eclipse.birt.report.designer.ui.internal.rcp.adapters;

import org.eclipse.birt.report.designer.ui.internal.rcp.wizards.WizardNewLibraryCreationPage;
import org.eclipse.birt.report.designer.ui.wizards.INewLibraryCreationPage;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * Add INewLibraryCreationPage adaptable to NewLibraryWizard.
 */

public class NewLibraryWizardAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return new WizardNewLibraryCreationPage(""); //$NON-NLS-1$
	}

	public Class[] getAdapterList() {
		return new Class[] { INewLibraryCreationPage.class };
	}

}
