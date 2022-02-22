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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * @deprecated by {@link org.eclipse.birt.report.model.api.TextDataHandle}
 */

@Deprecated
public class MultiLineDataHandle extends TextDataHandle {

	/**
	 * Constructs the handle with the report design and the element it holds. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 *
	 * @param design  the report design
	 * @param element the model representation of the element
	 */

	public MultiLineDataHandle(ReportDesign design, DesignElement element) {
		super(design, element);
	}
}
