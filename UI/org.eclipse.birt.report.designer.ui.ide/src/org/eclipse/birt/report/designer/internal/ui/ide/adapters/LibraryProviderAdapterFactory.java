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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 *
 */

public class LibraryProviderAdapterFactory implements IAdapterFactory {

	private ILibraryProvider provider;

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (provider == null) {
			provider = new LibraryProvider();
		}
		return provider;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { ILibraryProvider.class };
	}

}
