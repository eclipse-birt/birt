/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;

/**
 * 
 */

public class ElementUtil {

	/**
	 * Returns the simple api element for the given <code>element</code>.
	 * 
	 * @param element the element handle
	 * @return the simple api element instance
	 */

	public static IDesignElement getElement(DesignElementHandle element) {
		if (element == null)
			return null;

		if (element instanceof ReportDesignHandle)
			return new ReportDesign((ReportDesignHandle) element);

		if (element instanceof DataItemHandle)
			return new DataItem((DataItemHandle) element);

		if (element instanceof GridHandle)
			return new Grid((GridHandle) element);

		if (element instanceof ImageHandle)
			return new Image((ImageHandle) element);

		if (element instanceof LabelHandle)
			return new Label((LabelHandle) element);

		if (element instanceof ListHandle)
			return new List((ListHandle) element);

		if (element instanceof ListGroupHandle)
			return new ListGroup((ListGroupHandle) element);

		if (element instanceof TableHandle)
			return new Table((TableHandle) element);

		if (element instanceof TableGroupHandle)
			return new TableGroup((TableGroupHandle) element);

		if (element instanceof TextDataHandle)
			return new DynamicText((TextDataHandle) element);

		if (element instanceof TextItemHandle)
			return new TextItem((TextItemHandle) element);

		if (element instanceof CellHandle)
			return new Cell((CellHandle) element);

		if (element instanceof RowHandle)
			return new Row((RowHandle) element);

		if (element instanceof ColumnHandle)
			return new Column((ColumnHandle) element);

		if (element instanceof MasterPageHandle) {
			return new MasterPage((MasterPageHandle) element);
		}

		if (element instanceof FilterConditionElementHandle) {
			return new FilterConditionElement((FilterConditionElementHandle) element);
		}

		if (element instanceof SortElementHandle) {
			return new SortElement((SortElementHandle) element);
		}

		if (element instanceof ExtendedItemHandle) {
			org.eclipse.birt.report.model.api.simpleapi.IReportItem item = null;
			try {
				IReportItem extensionItem = ((ExtendedItemHandle) element).getReportItem();

				if (extensionItem != null)
					item = extensionItem.getSimpleElement();
			} catch (ExtendedElementException e) {
				// do thing.
			}

			if (item == null)
				item = new ExtendedItem((ExtendedItemHandle) element);

			return item;
		}

		if (!(element instanceof ReportElementHandle))
			return null;

		return new ReportElement((ReportElementHandle) element);
	}

}
