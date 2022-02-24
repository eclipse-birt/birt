/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended Item.
 * 
 */
public class ExtendedItemDesign extends ReportItemDesign {

	/**
	 * children of this extended item.
	 */
	protected List children = new ArrayList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.
	 * report.engine.ir.ReportItemVisitor)
	 */
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitExtendedItem(this, value);
	}

	public List getChildren() {
		return children;
	}
}
