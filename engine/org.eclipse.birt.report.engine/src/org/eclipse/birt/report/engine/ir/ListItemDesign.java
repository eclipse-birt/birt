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

package org.eclipse.birt.report.engine.ir;

/**
 * List Item IR. Listing is a base element in report design. it has a query,
 * many groups (corresponds to the query), header, footer and detail.
 * 
 * In creating, a listing will be replace by one header, one footer, several
 * details (surround by groups, each row in dataset will create a detail).
 * 
 */
public class ListItemDesign extends ListingDesign {
	/**
	 * default constructor.
	 */
	public ListItemDesign() {
	}

	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitListItem(this, value);
	}
}
