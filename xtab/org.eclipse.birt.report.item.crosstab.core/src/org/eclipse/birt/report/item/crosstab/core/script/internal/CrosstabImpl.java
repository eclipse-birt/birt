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

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstab;
import org.eclipse.birt.report.item.crosstab.core.script.ILevel;
import org.eclipse.birt.report.item.crosstab.core.script.IMeasure;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.SimpleRowItem;

/**
 * CrosstabImpl
 */
public class CrosstabImpl extends SimpleRowItem implements ICrosstab {

	private CrosstabReportItemHandle crh;

	public CrosstabImpl(CrosstabReportItemHandle crh) {
		super((ExtendedItemHandle) crh.getModelHandle());

		this.crh = crh;
	}

	@Override
	public String getCaption() {
		return crh.getCaption();
	}

	@Override
	public void setCaption(String caption) throws SemanticException {
		crh.setCaption(caption);
	}

	@Override
	public String getCaptionKey() {
		return crh.getCaptionKey();
	}

	@Override
	public void setCaptionKey(String captionKey) throws SemanticException {
		crh.setCaptionKey(captionKey);
	}

	@Override
	public String getSummary() {
		return crh.getSummary();
	}

	@Override
	public void setSummary(String summary) throws SemanticException {
		crh.setSummary(summary);
	}

	@Override
	public List<ILevel> getColumnLevels() {
		List<ILevel> ms = new ArrayList<>();

		for (int i = 0; i < crh.getDimensionCount(COLUMN_AXIS_TYPE); i++) {
			DimensionViewHandle dv = crh.getDimension(COLUMN_AXIS_TYPE, i);

			for (int j = 0; j < dv.getLevelCount(); j++) {
				ms.add(new LevelImpl(dv.getLevel(j)));
			}
		}

		return Collections.unmodifiableList(ms);
	}

	@Override
	public String getEmptyCellValue() {
		return crh.getEmptyCellValue();
	}

	@Override
	public String getMeasureDirection() {
		return crh.getMeasureDirection();
	}

	@Override
	public List<IMeasure> getMeasures() {
		List<IMeasure> ms = new ArrayList<>();

		for (int i = 0; i < crh.getMeasureCount(); i++) {
			ms.add(new MeasureImpl(crh.getMeasure(i)));
		}

		return Collections.unmodifiableList(ms);
	}

	@Override
	public List<ILevel> getRowLevels() {
		List<ILevel> ms = new ArrayList<>();

		for (int i = 0; i < crh.getDimensionCount(ROW_AXIS_TYPE); i++) {
			DimensionViewHandle dv = crh.getDimension(ROW_AXIS_TYPE, i);

			for (int j = 0; j < dv.getLevelCount(); j++) {
				ms.add(new LevelImpl(dv.getLevel(j)));
			}
		}

		return Collections.unmodifiableList(ms);
	}

	@Override
	public boolean isRepeatColumnHeader() {
		return crh.isRepeatColumnHeader();
	}

	@Override
	public boolean isRepeatRowHeader() {
		return crh.isRepeatRowHeader();
	}

	@Override
	public void setEmptyCellValue(String value) throws SemanticException {
		crh.setEmptyCellValue(value);
	}

	@Override
	public void setMeasureDirection(String direction) throws SemanticException {
		crh.setMeasureDirection(direction);
	}

	@Override
	public void setRepeatColumnHeader(boolean value) throws SemanticException {
		crh.setRepeatColumnHeader(value);
	}

	@Override
	public void setRepeatRowHeader(boolean value) throws SemanticException {
		crh.setRepeatRowHeader(value);
	}

	@Override
	public int getRowPageBreakInterval() {
		return crh.getRowPageBreakInterval();
	}

	@Override
	public void setRowPageBreakInterval(int value) throws SemanticException {
		crh.setRowPageBreakInterval(value);
	}

	@Override
	public int getColumnPageBreakInterval() {
		return crh.getColumnPageBreakInterval();
	}

	@Override
	public void setColumnPageBreakInterval(int value) throws SemanticException {
		crh.setColumnPageBreakInterval(value);
	}

}
