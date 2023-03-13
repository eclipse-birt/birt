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

package org.eclipse.birt.report.engine.executor;

import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * Defines execution logic for a List report item.
 * <p>
 * Currently table header and footer do not support data items
 *
 * <p>
 * if the table contains any drop cells, we need buffer the cell contents unitl
 * we resolved all the drop cells. we resovles the drop cells at the end of each
 * group as the drop cells can only start from the group header and terminate in
 * the group footer.
 *
 */
public class TableItemExecutor extends ListingElementExecutor {
	protected static Logger logger = Logger.getLogger(TableItemExecutor.class.getName());

	int rowId = 0;

	/**
	 * @param context execution context
	 * @param visitor visitor object for driving the execution
	 */
	protected TableItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.TABLEITEM);
	}

	@Override
	public IContent execute() {
		TableItemDesign tableDesign = (TableItemDesign) getDesign();

		ITableContent tableContent = report.createTableContent();
		setContent(tableContent);

		executeQuery();

		initializeContent(tableDesign, tableContent);
		processStyle(tableDesign, tableContent);
		processVisibility(tableDesign, tableContent);
		processBookmark(tableDesign, tableContent);
		processAction(tableDesign, tableContent);
		processUserProperties(tableDesign, tableContent);

		for (int i = 0; i < tableDesign.getColumnCount(); i++) {
			ColumnDesign columnDesign = tableDesign.getColumn(i);

			Column column = new Column(report);
			column.setGenerateBy(columnDesign);

			InstanceID iid = new InstanceID(null, columnDesign.getID(), null);
			column.setInstanceID(iid);

			processColumnVisibility(columnDesign, column);

			tableContent.addColumn(column);
		}
		if (context.isInFactory()) {
			handleOnCreate(tableContent);
		}

		startTOCEntry(tableContent);

		// create an empty result set to handle the showIfBlank
		boolean showIfBlank = "true".equalsIgnoreCase(content.getStyle().getShowIfBlank());
		if (showIfBlank && rsetEmpty) {
			createQueryForShowIfBlank();
		}

		// prepare to execute the children
		prepareToExecuteChildren();
		return tableContent;
	}

	@Override
	public void close() throws BirtException {
		finishTOCEntry();
		closeQuery();
		rowId = 0;
		super.close();
	}

	@Override
	public IReportItemExecutor getNextChild() {
		IReportItemExecutor executor = super.getNextChild();
		if (executor instanceof TableBandExecutor) {
			TableBandExecutor bandExecutor = (TableBandExecutor) executor;
			bandExecutor.setTableExecutor(this);
		}
		return executor;
	}

}
