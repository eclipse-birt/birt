/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.olap.OLAPException;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabCornerHeaderRowExecutor
 */
public class CrosstabCornerHeaderRowExecutor extends BaseCrosstabExecutor {

	private static Logger logger = Logger.getLogger(CrosstabCornerHeaderRowExecutor.class.getName());

	private int rowSpan, colSpan;
	private int currentChangeType;
	private int currentColIndex;

	private long currentEdgePosition;

	private boolean blankStarted;
	private boolean emptyStarted;
	private boolean hasLast;

	public CrosstabCornerHeaderRowExecutor(BaseCrosstabExecutor parent) {
		super(parent);
	}

	@Override
	public IContent execute() {
		IRowContent content = context.getReportContent().createRowContent();

		initializeContent(content, null);

		processRowHeight(crosstabItem.getHeader());

		prepareChildren();

		return content;
	}

	private void prepareChildren() {
		currentChangeType = ColumnEvent.UNKNOWN_CHANGE;
		currentColIndex = -1;

		currentEdgePosition = -1;

		blankStarted = false;
		emptyStarted = false;

		rowSpan = 1;
		colSpan = 0;

		hasLast = false;

		walker.reload();
	}

	@Override
	public IReportItemExecutor getNextChild() {
		IReportItemExecutor nextExecutor = null;

		try {
			while (walker.hasNext()) {
				ColumnEvent ev = walker.next();

				switch (currentChangeType) {
				case ColumnEvent.ROW_EDGE_CHANGE:
				case ColumnEvent.MEASURE_HEADER_CHANGE:

					if (blankStarted) {
						int headerCount = crosstabItem.getHeaderCount();

						if (headerCount > 1 || (ev.type != ColumnEvent.ROW_EDGE_CHANGE
								&& ev.type != ColumnEvent.MEASURE_HEADER_CHANGE)) {
							CrosstabCellHandle headerCell = null;

							int colIndex = currentColIndex - colSpan + 1;

							if (colIndex < headerCount) {
								headerCell = crosstabItem.getHeader(colIndex);
							}

							nextExecutor = new CrosstabCellExecutor(this, headerCell, rowSpan, colSpan,
									currentColIndex - colSpan + 1);

							((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

							blankStarted = false;
							hasLast = false;
						}
					}
					break;
				}

				if (!blankStarted
						&& (ev.type == ColumnEvent.ROW_EDGE_CHANGE || ev.type == ColumnEvent.MEASURE_HEADER_CHANGE)) {
					blankStarted = true;
					rowSpan = 1;
					colSpan = 0;
					hasLast = true;
				} else if (!emptyStarted && ev.type != ColumnEvent.ROW_EDGE_CHANGE
						&& ev.type != ColumnEvent.MEASURE_HEADER_CHANGE) {
					emptyStarted = true;
					rowSpan = 1;
					colSpan = 0;
					hasLast = true;
				}

				currentEdgePosition = ev.dataPosition;

				currentChangeType = ev.type;
				colSpan++;
				currentColIndex++;

				if (nextExecutor != null) {
					return nextExecutor;
				}
			}

		} catch (OLAPException e) {
			logger.log(Level.SEVERE,
					Messages.getString("CrosstabMeasureHeaderRowExecutor.error.generate.child.executor"), //$NON-NLS-1$
					e);
		}

		if (hasLast) {
			hasLast = false;

			// handle last column
			if (blankStarted) {
				CrosstabCellHandle headerCell = null;

				int colIndex = currentColIndex - colSpan + 1;

				if (colIndex < crosstabItem.getHeaderCount()) {
					headerCell = crosstabItem.getHeader(colIndex);
				}

				nextExecutor = new CrosstabCellExecutor(this, headerCell, rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				blankStarted = false;
			} else if (emptyStarted) {
				nextExecutor = new CrosstabCellExecutor(this, null, rowSpan, colSpan, currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				emptyStarted = false;
			}
		}

		return nextExecutor;
	}

	@Override
	public boolean hasNextChild() {
		try {
			return walker.hasNext() || hasLast;
		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabMeasureHeaderRowExecutor.error.check.child.executor"), //$NON-NLS-1$
					e);
		}
		return false;
	}
}
