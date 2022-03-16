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

	@Override
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitListItem(this, value);
	}
}
