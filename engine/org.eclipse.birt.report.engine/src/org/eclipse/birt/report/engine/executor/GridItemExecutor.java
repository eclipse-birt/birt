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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;

/**
 * the gridItem excutor
 *
 */
public class GridItemExecutor extends QueryItemExecutor {

	/**
	 * constructor
	 *
	 * @param context the executor context
	 * @param visitor the report executor visitor
	 */
	public GridItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.GRIDITEM);
	}

	/**
	 * execute a grid. The execution process is:
	 * <li>create a Table content
	 * <li>push it into the stack
	 * <li>execute the query and seek to the first record
	 * <li>process the table style, visiblity, action, bookmark
	 * <li>execute the onCreate if necessary.
	 * <li>call emitter to start the grid.
	 * <li>for each row, execute the row.
	 * <li>call emitter to close the grid.
	 * <li>close the query
	 * <li>popup the table.
	 *
	 * @see org.eclipse.birt.report.engine.excutor.ReportItemExcutor#excute()
	 */
	@Override
	public IContent execute() {
		GridItemDesign gridDesign = (GridItemDesign) getDesign();
		ITableContent tableContent = report.createTableContent();
		setContent(tableContent);

		executeQuery();

		initializeContent(gridDesign, tableContent);

		processAction(gridDesign, tableContent);
		processBookmark(gridDesign, tableContent);
		processStyle(gridDesign, tableContent);
		processVisibility(gridDesign, tableContent);
		processUserProperties(gridDesign, tableContent);

		for (int i = 0; i < gridDesign.getColumnCount(); i++) {
			ColumnDesign columnDesign = gridDesign.getColumn(i);
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

		// prepare to execute the children
		curRowDesign = 0;
		curRowContent = 0;

		return tableContent;
	}

	@Override
	public void close() throws BirtException {
		finishTOCEntry();
		closeQuery();
		super.close();
	}

	int curRowDesign;
	int curRowContent;

	@Override
	public boolean hasNextChild() {
		GridItemDesign gridDesign = (GridItemDesign) getDesign();
		return curRowDesign < gridDesign.getRowCount();
	}

	@Override
	public IReportItemExecutor getNextChild() {
		GridItemDesign gridDesign = (GridItemDesign) getDesign();

		if (curRowDesign < gridDesign.getRowCount()) {
			RowDesign rowDesign = gridDesign.getRow(curRowDesign++);
			ReportItemExecutor childExecutor = manager.createExecutor(this, rowDesign);
			if (childExecutor instanceof RowExecutor) {
				RowExecutor rowExecutor = (RowExecutor) childExecutor;
				rowExecutor.setRowId(curRowContent++);
			} else {
				curRowContent++;
			}
			return childExecutor;
		}
		return null;
	}
}
