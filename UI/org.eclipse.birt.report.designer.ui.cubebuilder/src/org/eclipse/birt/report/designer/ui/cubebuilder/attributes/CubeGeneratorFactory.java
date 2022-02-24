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

package org.eclipse.birt.report.designer.ui.cubebuilder.attributes;

import java.util.logging.Logger;

import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * 
 */

public class CubeGeneratorFactory implements IAdapterFactory {

	protected static Logger logger = Logger.getLogger(CubeGeneratorFactory.class.getName());

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (!(adaptableObject instanceof TabularCubeHandle)) {
			return null;
		}
		return new CubePageGenerator();
	}

	public Class[] getAdapterList() {
		return new Class[] { IPageGenerator.class };
	}

}
