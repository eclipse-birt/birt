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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * the base class for all crosstab element executor
 */
public abstract class BaseCrosstabExecutor implements ICrosstabConstants, IReportItemExecutor {

	private static Logger logger = Logger.getLogger(BaseCrosstabExecutor.class.getName());

	protected IExecutorContext context;
	protected CrosstabReportItemHandle crosstabItem;
	protected int[] rowCounter;
	protected IColumnWalker walker;

	private IContent content;
	private long[][] lastRowLevelState;
	private long[][] checkedRowLevelState;

	protected ICubeResultSet cubeRset;
	protected CubeCursor cubeCursor;

	protected Map styleCache;
	protected List rowGroups, columnGroups;
	protected int[] rowLevelPageBreakIntervals;

	protected int forcedRowLevelPageBreakInterval;
	protected int[] forcedRowCounter;

	private Object modelHandle;
	private IReportItemExecutor parentExecutor;

	protected BaseCrosstabExecutor() {
		this.rowCounter = new int[1];
		this.lastRowLevelState = new long[1][];
		this.checkedRowLevelState = new long[1][];
		this.forcedRowCounter = new int[] { -1 };
	}

	protected BaseCrosstabExecutor(IExecutorContext context, CrosstabReportItemHandle item,
			IReportItemExecutor parentExecutor) {
		this();

		this.context = context;
		this.crosstabItem = item;
		this.parentExecutor = parentExecutor;
	}

	protected BaseCrosstabExecutor(BaseCrosstabExecutor parent) {
		this(parent.context, parent.crosstabItem, parent);
		this.rowCounter = parent.rowCounter;
		this.walker = parent.walker;

		this.columnGroups = parent.columnGroups;
		this.rowGroups = parent.rowGroups;
		this.styleCache = parent.styleCache;

		this.rowLevelPageBreakIntervals = parent.rowLevelPageBreakIntervals;
		this.forcedRowLevelPageBreakInterval = parent.forcedRowLevelPageBreakInterval;
		this.forcedRowCounter = parent.forcedRowCounter;
		this.lastRowLevelState = parent.lastRowLevelState;
		this.checkedRowLevelState = parent.checkedRowLevelState;
	}

	protected void executeQuery(AbstractCrosstabItemHandle handle) {
		DesignElementHandle elementHandle = crosstabItem.getModelHandle();

		IDataQueryDefinition query = context.getQueries(elementHandle)[0];

		IBaseResultSet rset = context.executeQuery(getParentResultSet(), query, elementHandle);

		if (rset instanceof ICubeResultSet) {
			cubeRset = (ICubeResultSet) rset;
			cubeCursor = cubeRset.getCubeCursor();
		}
	}

	protected void closeQuery() {
		if (cubeRset != null) {
			cubeRset.close();
			cubeRset = null;
			cubeCursor = null;
		}
	}

	protected CrosstabReportItemHandle getCrosstabItemHandle() {
		return crosstabItem;
	}

	protected void processStyle(AbstractCrosstabItemHandle handle) {
		try {
			ContentUtil.processStyle(context, content, handle, getCubeResultSet(), styleCache);
		} catch (BirtException e) {
			logger.log(Level.SEVERE, Messages.getString("BaseCrosstabExecutor.error.process.style"), //$NON-NLS-1$
					e);
		}
	}

	protected void processVisibility(AbstractCrosstabItemHandle handle) {
		try {
			ContentUtil.processVisibility(context, content, handle, getCubeResultSet());
		} catch (BirtException e) {
			logger.log(Level.SEVERE, Messages.getString("BaseCrosstabExecutor.error.process.visibility"), //$NON-NLS-1$
					e);
		}
	}

	protected void processScopeAndHeaders(CrosstabCellHandle handle) {
		if (!(content instanceof ICellContent)) {
			return;
		}

		ICellContent cellContent = (ICellContent) content;

		try {
			ContentUtil.processScope(context, cellContent, handle, getCubeResultSet());

			ContentUtil.processHeaders(context, cellContent, handle, getCubeResultSet());
		} catch (BirtException e) {
			logger.log(Level.SEVERE, Messages.getString("BaseCrosstabExecutor.error.process.headers"), //$NON-NLS-1$
					e);
		}

	}

	protected void processBookmark(AbstractCrosstabItemHandle handle) {
		try {
			ContentUtil.processBookmark(context, content, handle, getCubeResultSet());
		} catch (BirtException e) {
			logger.log(Level.SEVERE, Messages.getString("BaseCrosstabExecutor.error.process.bookmark"), //$NON-NLS-1$
					e);
		}
	}

	protected void processAction(AbstractCrosstabItemHandle handle) {
		ContentUtil.processAction(context, content, handle);
	}

	protected void processRowHeight(CrosstabCellHandle cell) {
		if (cell != null) {
			try {
				DimensionType height = ContentUtil.createDimension(crosstabItem.getRowHeight(cell));

				if (height != null) {
					content.setHeight(height);
				}
			} catch (CrosstabException e) {
				logger.log(Level.SEVERE, Messages.getString("BaseCrosstabExecutor.error.process.row.height"), //$NON-NLS-1$
						e);
			}
		}
	}

	protected void processRowLevelPageBreak(IRowContent rowContent, boolean forceCheckOnly) {
		if (rowContent == null) {
			// if invalid content, just return
			return;
		}

		if (forcedRowLevelPageBreakInterval > 0) {
			// handle forced page break interval
			if (forcedRowCounter[0] == -1) {
				// record the current position only, note this position is (real
				// position + 1);
				forcedRowCounter[0] = rowCounter[0];
			} else if (rowCounter[0] - forcedRowCounter[0] >= forcedRowLevelPageBreakInterval) {
				rowContent.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);

				forcedRowCounter[0] = rowCounter[0];
			}
		}

		if (rowLevelPageBreakIntervals == null || forceCheckOnly) {
			// if no effective row level page break interval
			// setting, just return
			return;
		}

		try {
			if (lastRowLevelState[0] == null) {
				// this is the first access, store the initial state only
				lastRowLevelState[0] = getRowLevelCursorState();

				// need use diffrernt state instance for checked state and last
				// state, must not use
				// "checkedRowLevelState = lastRowLevelState;"
				checkedRowLevelState[0] = getRowLevelCursorState();
				return;
			}

			long[] currentRowLevelState = getRowLevelCursorState();

			for (int i = 0; i < rowLevelPageBreakIntervals.length; i++) {
				long currentPos = currentRowLevelState[i];
				long lastPos = lastRowLevelState[0][i];

				if (currentPos == lastPos) {
					continue;
				}

				if (rowLevelPageBreakIntervals[i] > 0) {
					// TODO check dummy group?

					long lastCheckedPos = checkedRowLevelState[0][i];

					if (currentPos - lastCheckedPos >= rowLevelPageBreakIntervals[i]) {
						// if step length larger than interval setting, then
						// break
						rowContent.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);

						// after break, need reset checked level state to
						// current state
						System.arraycopy(currentRowLevelState, 0, checkedRowLevelState[0], 0,
								currentRowLevelState.length);
					}
				}

				// also revalidate subsequent checked level state since
				// parent level position change will reset all sub level
				// positions
				for (int j = i + 1; j < rowLevelPageBreakIntervals.length; j++) {
					checkedRowLevelState[0][j] = 0;
				}
			}

			lastRowLevelState[0] = currentRowLevelState;
		} catch (OLAPException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	private long[] getRowLevelCursorState() throws OLAPException {
		return GroupUtil.getLevelCursorState(getRowEdgeCursor());
	}

	protected CrosstabCellHandle findHeaderRowCell(int dimIndex, int levelIndex) {
		return crosstabItem.getDimension(COLUMN_AXIS_TYPE, dimIndex).getLevel(levelIndex).getCell();
	}

	protected CrosstabCellHandle findMeasureHeaderCell() {
		for (int i = 0; i < crosstabItem.getMeasureCount(); i++) {
			CrosstabCellHandle headerCell = crosstabItem.getMeasure(i).getHeader();

			if (headerCell != null) {
				return headerCell;
			}
		}

		return null;
	}

	protected CrosstabCellHandle findMeasureRowCell(int rowIndex) {
		return crosstabItem.getMeasure(rowIndex).getCell();
	}

	protected CrosstabCellHandle findDetailRowCell(int rowIndex) {
		if (crosstabItem.getMeasureCount() > 0) {
			return crosstabItem.getMeasure(rowIndex).getCell();
		}

		// if no measure returns the innerest level cell
		int rdCount = crosstabItem.getDimensionCount(ROW_AXIS_TYPE);
		DimensionViewHandle dv = crosstabItem.getDimension(ROW_AXIS_TYPE, rdCount - 1);
		LevelViewHandle lv = dv.getLevel(dv.getLevelCount() - 1);

		return lv.getCell();
	}

	protected CrosstabCellHandle findSubTotalRowCell(int dimIndex, int levelIndex, int rowIndex) {
		MeasureViewHandle mv = crosstabItem.getMeasure(rowIndex);
		int count = mv.getAggregationCount();

		LevelHandle lh = crosstabItem.getDimension(ROW_AXIS_TYPE, dimIndex).getLevel(levelIndex).getCubeLevel();

		for (int i = 0; i < count; i++) {
			AggregationCellHandle cell = mv.getAggregationCell(i);

			if (cell.getAggregationOnRow() == lh) {
				return cell;
			}
		}

		return null;
	}

	protected CrosstabCellHandle findGrandTotalRowCell(int rowIndex) {
		MeasureViewHandle mv = crosstabItem.getMeasure(rowIndex);
		int count = mv.getAggregationCount();

		for (int i = 0; i < count; i++) {
			AggregationCellHandle cell = mv.getAggregationCell(i);

			if (cell.getAggregationOnRow() == null) {
				return cell;
			}
		}

		return null;
	}

	protected void initializeContent(IContent content, AbstractCrosstabItemHandle handle) {
		this.content = content;

		// increase row index
		if (content instanceof IRowContent) {
			((IRowContent) content).setRowID(rowCounter[0]++);
		}

		IContent parent = getParentContent();
		if (parent != null) {
			content.setParent(parent);
		}
	}

	private IContent getParentContent() {
		IReportItemExecutor re = parentExecutor;

		while (re != null) {
			IContent cont = re.getContent();
			if (cont != null) {
				return cont;
			}
			re = re.getParent();
		}
		return null;
	}

	private IBaseResultSet getParentResultSet() {
		IReportItemExecutor re = parentExecutor;

		while (re != null) {
			IBaseResultSet[] rsa = re.getQueryResults();
			if (rsa != null && rsa.length > 0) {
				return rsa[0];
			}
			re = re.getParent();
		}
		return null;
	}

	@Override
	public IContent getContent() {
		return content;
	}

	protected ICubeResultSet getCubeResultSet() {
		if (cubeRset != null) {
			return cubeRset;
		} else if (parentExecutor instanceof BaseCrosstabExecutor && !(this instanceof CrosstabReportItemExecutor)) {
			// for top level crosstab item executor, it needn't check the
			// parent, in case it's nested in another Crosstab.
			return ((BaseCrosstabExecutor) parentExecutor).getCubeResultSet();
		}

		return null;
	}

	protected CubeCursor getCubeCursor() {
		if (cubeCursor != null) {
			return cubeCursor;
		} else if (parentExecutor instanceof BaseCrosstabExecutor && !(this instanceof CrosstabReportItemExecutor)) {
			// for top level crosstab item executor, it needn't check the
			// parent, in case it's nested in another Crosstab.
			return ((BaseCrosstabExecutor) parentExecutor).getCubeCursor();
		}

		return null;
	}

	protected EdgeCursor getColumnEdgeCursor() throws OLAPException {
		CubeCursor cs = getCubeCursor();

		if (cs != null) {
			List ordinates = cs.getOrdinateEdge();

			if (columnGroups != null && columnGroups.size() > 0 && ordinates.size() > 0) {
				// the first is always column edge if has column definition
				return (EdgeCursor) ordinates.get(0);
			}
		}
		return null;
	}

	protected EdgeCursor getRowEdgeCursor() throws OLAPException {
		CubeCursor cs = getCubeCursor();

		if (cs != null) {
			List ordinates = cs.getOrdinateEdge();

			if (rowGroups != null && rowGroups.size() > 0 && ordinates.size() > 0) {
				// the last is always row edge if has row definition
				return (EdgeCursor) ordinates.get(ordinates.size() - 1);
			}
		}
		return null;
	}

	protected boolean needRowGrandTotal(String position) throws OLAPException {
		if (rowGroups.size() > 0 && getRowEdgeCursor() != null
				&& (crosstabItem.getMeasureCount() > 0 || !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE)
				&& crosstabItem.getGrandTotal(ROW_AXIS_TYPE) != null) {
			return position.equals(crosstabItem.getCrosstabView(ROW_AXIS_TYPE).getGrandTotalLocation());
		}
		return false;
	}

	@Override
	public void close() {
		// TODO clean up
	}

	@Override
	public Object getModelObject() {
		return modelHandle;
	}

	@Override
	public void setModelObject(Object handle) {
		modelHandle = handle;
	}

	@Override
	public IReportItemExecutor getParent() {
		return parentExecutor;
	}

	@Override
	public void setParent(IReportItemExecutor parent) {
		parentExecutor = parent;
	}

	@Override
	public IBaseResultSet[] getQueryResults() {
		if (cubeRset == null) {
			return null;
		}

		return new IBaseResultSet[] { cubeRset };
	}

	@Override
	public IExecutorContext getContext() {
		return context;
	}

	@Override
	public void setContext(IExecutorContext context) {
		this.context = context;
	}

}
