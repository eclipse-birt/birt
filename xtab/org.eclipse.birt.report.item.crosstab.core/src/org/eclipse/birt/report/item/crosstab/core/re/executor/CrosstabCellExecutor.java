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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabCellExecutor
 */
public class CrosstabCellExecutor extends BaseCrosstabExecutor {

	private static final Logger logger = Logger.getLogger(CrosstabCellExecutor.class.getName());

	private CrosstabCellHandle cellHandle;
	private int rowSpan, colSpan, colIndex;
	private List contents;
	private int currentChild;

	private long position = -1;
	private boolean isForceEmpty;

	public CrosstabCellExecutor(BaseCrosstabExecutor parent, CrosstabCellHandle handle, int rowSpan, int colSpan,
			int colIndex) {
		super(parent);
		this.cellHandle = handle;
		if (cellHandle != null) {
			contents = cellHandle.getContents();
		}
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
		this.colIndex = colIndex;
	}

	public void setPosition(long pos) {
		this.position = pos;
	}

	public void setForceEmpty(boolean isEmpty) {
		this.isForceEmpty = isEmpty;
	}

	@Override
	public IContent execute() {
		ICellContent content = context.getReportContent().createCellContent();

		initializeContent(content, cellHandle);

		content.setRowSpan(rowSpan);
		content.setColSpan(colSpan);
		content.setColumn(colIndex);

		// set repeat content property for level view cells
		if (cellHandle != null && cellHandle.getContainer() instanceof LevelViewHandle) {
			content.setRepeatContent(true);
		}

		// reset data position before style processing
		try {
			EdgeCursor columnEdgeCursor = getColumnEdgeCursor();

			if (columnEdgeCursor != null) {
				columnEdgeCursor.setPosition(position);
			}
		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabCellExecutor.error.restor.data.position"), //$NON-NLS-1$
					e);
		}

		// user crosstab style for blank cells
		processStyle(cellHandle);
		processVisibility(cellHandle);
		processBookmark(cellHandle);
		processScopeAndHeaders(cellHandle);
		processAction(cellHandle);

		currentChild = 0;

		ICubeResultSet cubeRset = getCubeResultSet();

		DataID di = cubeRset == null ? null : new DataID(cubeRset.getID(), cubeRset.getCellIndex());

		InstanceID iid = new InstanceID(null, cellHandle == null ? -1 : cellHandle.getModelHandle().getID(), di);

		content.setInstanceID(iid);

		return content;
	}

	@Override
	public IReportItemExecutor getNextChild() {
		if (isForceEmpty) {
			return null;
		}

		// must reset data position
		try {
			EdgeCursor columnEdgeCursor = getColumnEdgeCursor();

			if (columnEdgeCursor != null) {
				columnEdgeCursor.setPosition(position);
			}
		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabCellExecutor.error.restor.data.position"), //$NON-NLS-1$
					e);
		}

		IReportItemExecutor executor = context.createExecutor(this, contents.get(currentChild++));

		return executor;
	}

	@Override
	public boolean hasNextChild() {
		if (isForceEmpty) {
			return false;
		}

		if (contents != null) {
			return currentChild < contents.size();
		}

		return false;
	}
}
