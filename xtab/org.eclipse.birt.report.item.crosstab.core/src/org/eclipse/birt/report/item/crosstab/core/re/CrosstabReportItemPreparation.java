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

package org.eclipse.birt.report.item.crosstab.core.re;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.ReportItemPreparationBase;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.script.internal.handler.CrosstabPreparationHandler;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * CrosstabReportItemPreparation
 */
public class CrosstabReportItemPreparation extends ReportItemPreparationBase {

	@Override
	public void prepare() throws BirtException {
		if (!(handle instanceof ExtendedItemHandle)) {
			return;
		}

		CrosstabReportItemHandle crosstab = (CrosstabReportItemHandle) ((ExtendedItemHandle) handle).getReportItem();

		if (crosstab == null) {
			return;
		}

		// Hide detail measure row or column
		Object hideDetail = handle.getProperty(ICrosstabConstants.HIDE_DETAIL_PROP);
		if (hideDetail != null) {
			if (hideDetail.toString().toLowerCase().equals(ICrosstabConstants.HIDE_DETAIL_ROW)) {
				hideDetail(crosstab, true);
			} else if (hideDetail.toString().toLowerCase().equals(ICrosstabConstants.HIDE_DETAIL_COLUMN)) {
				hideDetail(crosstab, false);
			} else {
				throw new CrosstabException(
						Messages.getString("CrosstabReportItemPreparation.Exception.HideDetailPropertyValueIsWrong", //$NON-NLS-1$
								new String[] { ICrosstabConstants.HIDE_DETAIL_PROP, ICrosstabConstants.HIDE_DETAIL_ROW,
										ICrosstabConstants.HIDE_DETAIL_COLUMN }));
			}
		}

		ExtendedItemHandle modelHandle = (ExtendedItemHandle) crosstab.getModelHandle();
		String javaClass = modelHandle.getEventHandlerClass();
		String script = modelHandle.getOnPrepare();

		if ((javaClass != null && javaClass.trim().length() > 0) || (script != null && script.trim().length() > 0)) {
			// fix bug 235947, ensure engine script context is initialized at
			// this moment
			context.evaluate("1"); //$NON-NLS-1$
		}

		new CrosstabPreparationHandler(crosstab, context).handle();
	}

	private void hideDetail(CrosstabReportItemHandle crosstab, boolean hideRow) throws BirtException {
		// Zero width/height in measure cell
		final String ZERO = "0in"; //$NON-NLS-1$
		for (MeasureViewHandle mv : crosstab.getAllMeasures()) {
			AggregationCellHandle cell = mv.getCell();

			if (hideRow) {
				// Remove all items in the measure cell and grand total cell
				clearCellContents(cell);
				clearCellContents(mv.getAggregationCell(cell.getDimensionName(ICrosstabConstants.ROW_AXIS_TYPE),
						cell.getLevelName(ICrosstabConstants.ROW_AXIS_TYPE), null, null));
			} else {
				cell.getModelHandle().setProperty(IStyleModel.WIDTH_PROP, ZERO);
			}
		}

		if (hideRow) {
			// Remove all items in the dimension cell
			int dimCount = crosstab.getDimensionCount(ICrosstabConstants.ROW_AXIS_TYPE);
			for (int i = 0; i < dimCount; i++) {
				DimensionViewHandle dim = crosstab.getDimension(ICrosstabConstants.ROW_AXIS_TYPE, i);
				for (int j = 0; j < dim.getLevelCount(); j++) {
					LevelViewHandle level = dim.getLevel(j);
					clearCellContents(level.getCell());
				}
			}

			// Hide measure header to avoid meaningless header
			handle.setProperty(ICrosstabReportItemConstants.HIDE_MEASURE_HEADER_PROP, true);
		}

		// Set big pageBreakInterval to avoid page break
		final int NO_PAGE_BREAK = 10000;
		crosstab.getModelHandle().setProperty(hideRow ? ICrosstabConstants.ROW_PAGE_BREAK_INTERVAL_PROP
				: ICrosstabConstants.COLUMN_PAGE_BREAK_INTERVAL_PROP, NO_PAGE_BREAK);

		// Fixed Layout to avoid white space
		((ReportDesignHandle) crosstab.getModuleHandle())
				.setLayoutPreference(DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT);

	}

	private void clearCellContents(CrosstabCellHandle cell) throws BirtException {
		if (cell != null) {
			// No outline
			cell.getModelHandle().setProperty(IStyleModel.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_NONE);
			cell.getModelHandle().setProperty(IStyleModel.BORDER_BOTTOM_STYLE_PROP,
					DesignChoiceConstants.LINE_STYLE_NONE);
			cell.getModelHandle().setProperty(IStyleModel.BORDER_LEFT_STYLE_PROP,
					DesignChoiceConstants.LINE_STYLE_NONE);
			cell.getModelHandle().setProperty(IStyleModel.BORDER_RIGHT_STYLE_PROP,
					DesignChoiceConstants.LINE_STYLE_NONE);

			// No padding
			final String ZERO = "0pt"; //$NON-NLS-1$
			cell.getModelHandle().setProperty(IStyleModel.PADDING_TOP_PROP, ZERO);
			cell.getModelHandle().setProperty(IStyleModel.PADDING_BOTTOM_PROP, ZERO);
			cell.getModelHandle().setProperty(IStyleModel.PADDING_LEFT_PROP, ZERO);
			cell.getModelHandle().setProperty(IStyleModel.PADDING_RIGHT_PROP, ZERO);
			// No contents
			for (Object child : cell.getContents()) {
				if (child instanceof DesignElementHandle) {
					DesignElementHandle designElement = (DesignElementHandle) child;
					designElement.setProperty(IStyleModel.DISPLAY_PROP, DesignChoiceConstants.DISPLAY_NONE);
				}
			}
		}
	}

}
