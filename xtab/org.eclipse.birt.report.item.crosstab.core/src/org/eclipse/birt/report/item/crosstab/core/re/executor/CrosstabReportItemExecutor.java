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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * CrosstabReportItemExecutor
 */
public class CrosstabReportItemExecutor extends BaseCrosstabExecutor {

	private static Logger logger = Logger.getLogger(CrosstabReportItemExecutor.class.getName());

	private List children;
	private int currentChild;
	private EdgeCursor rowCursor;
	private List groupCursors;
	private int measureCount;
	private boolean endOfGroups;
	private boolean needRowGroups;

	boolean notifyNextGroupPageBreak;

	public CrosstabReportItemExecutor() {
		super();
	}

	public CrosstabReportItemExecutor(CrosstabReportItemHandle item, IExecutorContext context,
			IReportItemExecutor parentExecutor) {
		super(context, item, parentExecutor);
	}

	@Override
	public void close() {
		super.close();

		closeQuery();

		children = null;
		rowCursor = null;
		groupCursors = null;
	}

	@Override
	public void setModelObject(Object handle) {
		super.setModelObject(handle);

		if (handle instanceof ExtendedItemHandle) {
			ExtendedItemHandle exHandle = (ExtendedItemHandle) handle;
			IReportItem item = null;

			try {
				item = exHandle.getReportItem();
			} catch (ExtendedElementException e) {
				logger.log(Level.SEVERE, Messages.getString("CrosstabReportItemExecutor.error.crosstab.loading"), //$NON-NLS-1$
						e);
			}

			crosstabItem = (CrosstabReportItemHandle) item;
		}
	}

	@Override
	public IContent execute() {
		ITableContent content = context.getReportContent().createTableContent();

		executeQuery(crosstabItem);

		initializeContent(content, crosstabItem);

		processStyle(crosstabItem);
		processVisibility(crosstabItem);
		processBookmark(crosstabItem);
		processAction(crosstabItem);

		// handle table caption
		content.setCaption(crosstabItem.getCaption());
		content.setCaptionKey(crosstabItem.getCaptionKey());
		content.setSummary(crosstabItem.getSummary());

		// check repeate header, the column header in crosstab is mapped to
		// header of table
		content.setHeaderRepeat(crosstabItem.isRepeatColumnHeader());

		// ! we don't check the null cube cursor here anymore, as this is
		// allowed now.
		// if ( getCubeCursor( ) != null )
		{
			// prepare style cache
			styleCache = new HashMap();

			// generate table columns
			try {
				rowGroups = GroupUtil.getGroups(crosstabItem, ROW_AXIS_TYPE);
				columnGroups = GroupUtil.getGroups(crosstabItem, COLUMN_AXIS_TYPE);

				rowLevelPageBreakIntervals = GroupUtil.getLevelPageBreakIntervals(crosstabItem, rowGroups,
						ROW_AXIS_TYPE);

				forcedRowLevelPageBreakInterval = crosstabItem.getRowPageBreakInterval();

				walker = new CachedColumnWalker(crosstabItem, getColumnEdgeCursor());
				new TableColumnGenerator(crosstabItem, walker, getCubeResultSet(), getColumnEdgeCursor(), columnGroups)
						.generateColumns(context.getReportContent(), content);

				prepareChildren();
			} catch (OLAPException e) {
				logger.log(Level.SEVERE, Messages.getString("CrosstabReportItemExecutor.error.generate.columns"), //$NON-NLS-1$
						e);
			}
		}
		// else
		// {
		// logger.log( Level.SEVERE,
		// Messages.getString( "CrosstabReportItemExecutor.error.invalid.cube.result" )
		// ); //$NON-NLS-1$
		// }

		return content;
	}

	private void prepareChildren() throws OLAPException {
		needRowGroups = false;
		measureCount = crosstabItem.getMeasureCount();
		rowCursor = getRowEdgeCursor();

		// check if need recursive groups
		if (rowGroups.size() > 0 && rowCursor != null) {
			rowCursor.beforeFirst();

			if (rowCursor.next()) {
				groupCursors = rowCursor.getDimensionCursor();

				needRowGroups = true;
			}
		}

		if (needRowGroups) {
			// collect recursive group executable
			collectExecutable();
		} else {
			// collect simple header/detial(measure)/footer sturcture
			currentChild = 0;
			children = new ArrayList();

			// prepare header
			if (columnGroups.size() > 0 || GroupUtil.hasMeasureHeader(crosstabItem, COLUMN_AXIS_TYPE)
					|| crosstabItem.getHeader() != null || needRowGrandTotal(GRAND_TOTAL_LOCATION_BEFORE)) {
				CrosstabHeaderExecutor headerExecutor = new CrosstabHeaderExecutor(this);
				children.add(headerExecutor);
			}

			// prepare body (measure only), this will skip the case if the
			// rowCursor is avaiable but has zero data
			if (measureCount > 0 && rowCursor == null) {
				CrosstabMeasureExecutor measureExecutor = new CrosstabMeasureExecutor(this);
				children.add(measureExecutor);
			}

			// prepare footer, if the rowCursor is avaiable but has zero data,
			// we still need the footer
			if (needRowGrandTotal(GRAND_TOTAL_LOCATION_AFTER)) {
				CrosstabFooterExecutor totalExecutor = new CrosstabFooterExecutor(this);
				children.add(totalExecutor);
			}
		}
	}

	private void collectExecutable() throws OLAPException {
		endOfGroups = false;
		currentChild = 0;
		children = new ArrayList();

		// prepare header
		int startingGroupIndex = GroupUtil.getStartingGroupLevel(rowCursor, groupCursors);

		// this is the start of the entire edge
		if (startingGroupIndex <= 0) {
			if (columnGroups.size() > 0 || GroupUtil.hasMeasureHeader(crosstabItem, COLUMN_AXIS_TYPE)
					|| crosstabItem.getHeader() != null || needRowGrandTotal(GRAND_TOTAL_LOCATION_BEFORE)) {
				CrosstabHeaderExecutor headerExecutor = new CrosstabHeaderExecutor(this);
				children.add(headerExecutor);
			}
		}

		// prepare groups, always generate a top level group
		{
			CrosstabGroupExecutor groupExecutor = new CrosstabGroupExecutor(this, 0, rowCursor);
			children.add(groupExecutor);
		}

		// prepare footer
		int endingGroupIndex = GroupUtil.getEndingGroupLevel(rowCursor, groupCursors);

		// this is the end of entire edge
		if (endingGroupIndex <= 0) {
			if (needRowGrandTotal(GRAND_TOTAL_LOCATION_AFTER)) {
				CrosstabFooterExecutor totalExecutor = new CrosstabFooterExecutor(this);
				children.add(totalExecutor);
			}

			endOfGroups = true;
		}
	}

	@Override
	public boolean hasNextChild() {
		if (children == null) {
			return false;
		}

		if (currentChild < children.size()) {
			return true;
		}

		if (needRowGroups) {
			if (endOfGroups) {
				return false;
			}

			try {
				while (!endOfGroups) {
					int endingGroupIndex = GroupUtil.getEndingGroupLevel(rowCursor, groupCursors);

					// check end on entire edge
					if (endingGroupIndex <= 0) {
						currentChild = 0;
						children = new ArrayList();

						if (needRowGrandTotal(GRAND_TOTAL_LOCATION_AFTER)) {
							CrosstabFooterExecutor totalExecutor = new CrosstabFooterExecutor(this);
							children.add(totalExecutor);
						}

						endOfGroups = true;

						return currentChild < children.size();
					}

					if (rowCursor.next()) {
						collectExecutable();

						return currentChild < children.size();
					}
				}
			} catch (OLAPException e) {
				logger.log(Level.SEVERE, Messages.getString("CrosstabReportItemExecutor.error.generate.columns"), //$NON-NLS-1$
						e);
			}
		}

		return false;
	}

	@Override
	public IReportItemExecutor getNextChild() {
		if (hasNextChild()) {
			return (IReportItemExecutor) children.get(currentChild++);
		}
		return null;
	}

}
