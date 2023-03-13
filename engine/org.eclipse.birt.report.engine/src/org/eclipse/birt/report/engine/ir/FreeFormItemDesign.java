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

import java.util.ArrayList;

/**
 * Container Object.
 *
 * the content of container should be ReportItem also.
 *
 */
public class FreeFormItemDesign extends ReportItemDesign {

	/**
	 * items in this container.
	 */
	protected ArrayList items = new ArrayList();

	/**
	 * get items in this container.
	 *
	 * @return items in this container.
	 */
	public ArrayList getItems() {
		return this.items;
	}

	/**
	 * add item into the container.
	 *
	 * @param item item to be added.
	 */
	public void addItem(ReportItemDesign item) {
		this.items.add(item);
	}

	/**
	 * get total items in the container.
	 *
	 * @return item count
	 */
	public int getItemCount() {
		return this.items.size();
	}

	/**
	 * get item at index index.
	 *
	 * @param index item index. the index must >= 0 and < item.count.
	 * @return item
	 */
	public ReportItemDesign getItem(int index) {
		assert (index >= 0 && index < this.items.size());
		return (ReportItemDesign) this.items.get(index);
	}

	@Override
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitFreeFormItem(this, value);
	}
}
