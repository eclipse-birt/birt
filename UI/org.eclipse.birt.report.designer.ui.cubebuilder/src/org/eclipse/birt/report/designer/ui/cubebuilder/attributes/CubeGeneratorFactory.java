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
