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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * CrosstabGroupExecutor
 */
public class CrosstabGroupExecutor extends BaseCrosstabExecutor {

	private static final Logger logger = Logger.getLogger(CrosstabGroupExecutor.class.getName());

	private int currentGroupIndex;
	private EdgeCursor rowCursor;
	private List groupCursors;

	private LevelViewHandle currentLevel;
	private int currentDimensionIndex, currentLevelIndex;
	private int totalMeasureCount;

	private List elements;
	private int currentElement;
	private boolean endGroup;
	private boolean hasGroup;

	boolean notifyNextGroupPageBreak;

	public CrosstabGroupExecutor(BaseCrosstabExecutor parent, int groupIndex, EdgeCursor rowCursor) {
		super(parent);

		this.currentGroupIndex = groupIndex;
		this.rowCursor = rowCursor;
	}

	@Override
	public void close() {
		if (hasGroup) {
			try {
				handleGroupPageBreakAfter();
			} catch (OLAPException e) {
				logger.log(Level.SEVERE, Messages.getString("CrosstabGroupExecutor.error.close.executor"), //$NON-NLS-1$
						e);
			}
		}

		super.close();

		groupCursors = null;
		currentLevel = null;
		elements = null;
	}

	@Override
	public IContent execute() {
		ITableGroupContent content = context.getReportContent().createTableGroupContent();

		initializeContent(content, null);

		prepareChildren();

		return content;
	}

	private void prepareChildren() {
		hasGroup = rowGroups.size() > 0 && rowCursor != null;

		if (hasGroup) {
			try {
				totalMeasureCount = crosstabItem.getMeasureCount();

				groupCursors = rowCursor.getDimensionCursor();

				EdgeGroup group = (EdgeGroup) rowGroups.get(currentGroupIndex);

				currentDimensionIndex = group.dimensionIndex;
				currentLevelIndex = group.levelIndex;

				if (currentDimensionIndex >= 0 && currentLevelIndex >= 0) {
					currentLevel = crosstabItem.getDimension(ROW_AXIS_TYPE, currentDimensionIndex)
							.getLevel(currentLevelIndex);
				}

				handleGroupPageBreakInside();
				handleGroupPageBreakBefore();

				collectExecutable();
			} catch (OLAPException e) {
				logger.log(Level.SEVERE, Messages.getString("CrosstabGroupExecutor.error.prepare.group"), //$NON-NLS-1$
						e);
			}
		} else {
			// measure only
			elements = new ArrayList();
			currentElement = 0;

			elements.add(new CrosstabMeasureExecutor(this));
		}
	}

	private boolean isCurrentLevelLeafOrDummyGroup() throws OLAPException {
		return GroupUtil.isLeafOrDummyGroup(rowCursor.getDimensionCursor(), currentGroupIndex);
	}

	private void handleGroupPageBreakInside() {
		if (currentLevel != null) {
			String pageBreakInside = currentLevel.getPageBreakInside();
			if (DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID.equals(pageBreakInside)) {
				getContent().getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_INSIDE, IStyle.AVOID_VALUE);
			}
		}
	}

	private void handleGroupPageBreakBefore() throws OLAPException {
		if (currentLevel != null) {
			// handle special logic for page_break_before_always_excluding_first
			boolean isFirst = ((DimensionCursor) groupCursors.get(currentGroupIndex)).isFirst();
			// isFirst = rowCursor.isFirst();

			String pageBreakBefore = currentLevel.getPageBreakBefore();
			if (DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS.equals(pageBreakBefore)
					|| (DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST.equals(pageBreakBefore)
							&& !isFirst)) {
				getContent().getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
			}

			String pageBreakAfter = currentLevel.getPageBreakAfter();
			if (DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS.equals(pageBreakAfter)) {
				getContent().getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_AFTER, IStyle.ALWAYS_VALUE);
			}
		}

		// handle special logic for page_break_after_excluding_last
		boolean hasPageBreak = false;
		IReportItemExecutor parentExecutor = getParent();

		// TODO code refactor
		if ((parentExecutor instanceof CrosstabGroupExecutor || parentExecutor instanceof CrosstabReportItemExecutor)) {
			if (parentExecutor instanceof CrosstabGroupExecutor) {
				if (((CrosstabGroupExecutor) parentExecutor).notifyNextGroupPageBreak) {
					((CrosstabGroupExecutor) parentExecutor).notifyNextGroupPageBreak = false;

					hasPageBreak = true;
				}
			} else if (((CrosstabReportItemExecutor) parentExecutor).notifyNextGroupPageBreak) {
				((CrosstabReportItemExecutor) parentExecutor).notifyNextGroupPageBreak = false;

				hasPageBreak = true;
			}

			// parentExecutor = parentExecutor.getParent( );
		}

		if (hasPageBreak) {
			getContent().getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
		}
	}

	private void handleGroupPageBreakAfter() throws OLAPException {
		if (currentLevel != null) {
			// handle page_break_after_excluding_last
			String pageBreakAfter = currentLevel.getPageBreakAfter();
			IReportItemExecutor parentExecutor = getParent();

			if ((parentExecutor instanceof CrosstabGroupExecutor
					|| parentExecutor instanceof CrosstabReportItemExecutor)
					&& DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST.equals(pageBreakAfter)
					&& !rowCursor.isLast()) {
				// TODO code refactor
				if (parentExecutor instanceof CrosstabGroupExecutor
						|| parentExecutor instanceof CrosstabReportItemExecutor) {
					if (parentExecutor instanceof CrosstabGroupExecutor) {
						((CrosstabGroupExecutor) parentExecutor).notifyNextGroupPageBreak = true;
					} else {
						((CrosstabReportItemExecutor) parentExecutor).notifyNextGroupPageBreak = true;
					}

					// parentExecutor = parentExecutor.getParent( );
				}
			}
		}
	}

	private void collectExecutable() throws OLAPException {
		elements = new ArrayList();
		currentElement = 0;
		endGroup = false;

		int startingGroupIndex = GroupUtil.getStartingGroupLevel(rowCursor, groupCursors);

		// check group start on previous group, to show header on
		// previous group
		if (startingGroupIndex <= currentGroupIndex + 1) {
			if (totalMeasureCount > 0 || !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE) {
				if (!isCurrentLevelLeafOrDummyGroup() && currentLevel != null
						&& currentLevel.getAggregationHeader() != null
						&& AGGREGATION_HEADER_LOCATION_BEFORE.equals(currentLevel.getAggregationHeaderLocation())) {
					// header
					CrosstabGroupBandExecutor bandExecutor = new CrosstabGroupBandExecutor(this, currentDimensionIndex,
							currentLevelIndex, IBandContent.BAND_HEADER);
					elements.add(bandExecutor);
				}
			}
		}

		if (currentGroupIndex < rowGroups.size() - 1) {
			// next group
			CrosstabGroupExecutor groupExecutor = new CrosstabGroupExecutor(this, currentGroupIndex + 1, rowCursor);
			elements.add(groupExecutor);
		} else {
			// detail
			CrosstabGroupBandExecutor bandExecutor = new CrosstabGroupBandExecutor(this, currentDimensionIndex,
					currentLevelIndex, IBandContent.BAND_DETAIL);
			elements.add(bandExecutor);
		}

		int endingGroupIndex = GroupUtil.getEndingGroupLevel(rowCursor, groupCursors);

		// check group end on previous group, to show footer on
		// previous group
		if (endingGroupIndex <= currentGroupIndex + 1) {
			if (totalMeasureCount > 0 || !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE) {
				if (!isCurrentLevelLeafOrDummyGroup() && currentLevel != null
						&& currentLevel.getAggregationHeader() != null
						&& AGGREGATION_HEADER_LOCATION_AFTER.equals(currentLevel.getAggregationHeaderLocation())) {
					// footer
					CrosstabGroupBandExecutor bandExecutor = new CrosstabGroupBandExecutor(this, currentDimensionIndex,
							currentLevelIndex, IBandContent.BAND_FOOTER);
					elements.add(bandExecutor);
				}
			}

			endGroup = true;
		}

	}

	@Override
	public IReportItemExecutor getNextChild() {
		if (currentElement < elements.size()) {
			return (IReportItemExecutor) elements.get(currentElement++);
		}

		return null;
	}

	@Override
	public boolean hasNextChild() {
		if (currentElement < elements.size()) {
			return true;
		}

		if (hasGroup) {
			if (endGroup) {
				return false;
			}

			try {
				while (!endGroup) {
					int endingGroupIndex = GroupUtil.getEndingGroupLevel(rowCursor, groupCursors);

					// check group end on previous group, to show footer on
					// previous group
					if (endingGroupIndex <= currentGroupIndex + 1) {
						currentElement = 0;
						elements = new ArrayList();

						if (totalMeasureCount > 0 || !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE) {
							if (!isCurrentLevelLeafOrDummyGroup() && currentLevel != null
									&& currentLevel.getAggregationHeader() != null && AGGREGATION_HEADER_LOCATION_AFTER
											.equals(currentLevel.getAggregationHeaderLocation())) {
								// footer
								CrosstabGroupBandExecutor bandExecutor = new CrosstabGroupBandExecutor(this,
										currentDimensionIndex, currentLevelIndex, IBandContent.BAND_FOOTER);
								elements.add(bandExecutor);
							}
						}

						endGroup = true;

						return currentElement < elements.size();
					}

					if (rowCursor.next()) {
						collectExecutable();

						return currentElement < elements.size();
					}

				}
			} catch (OLAPException e) {
				logger.log(Level.SEVERE, Messages.getString("CrosstabGroupExecutor.error.check.child.executor"), //$NON-NLS-1$
						e);
			}
		}

		return false;
	}
}
