/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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