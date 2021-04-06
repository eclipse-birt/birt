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
