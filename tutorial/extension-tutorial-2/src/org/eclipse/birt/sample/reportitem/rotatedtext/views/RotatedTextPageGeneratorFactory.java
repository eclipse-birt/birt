/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.sample.reportitem.rotatedtext.views;

import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * RotatedTextPageGeneratorFactory
 */
public class RotatedTextPageGeneratorFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return new RotatedTextPageGenerator();
	}

	public Class[] getAdapterList() {
		return new Class[] { IPageGenerator.class };
	}

}
