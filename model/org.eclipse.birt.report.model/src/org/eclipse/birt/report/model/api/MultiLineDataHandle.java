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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * @deprecated by {@link org.eclipse.birt.report.model.api.TextDataHandle}
 */

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
