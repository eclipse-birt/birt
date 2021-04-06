/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public String getCaption() {
		return crh.getCaption();
	}

	public void setCaption(String caption) throws SemanticException {
		crh.setCaption(caption);
	}

	public String getCaptionKey() {
		return crh.getCaptionKey();
	}

	public void setCaptionKey(String captionKey) throws SemanticException {
		crh.setCaptionKey(captionKey);
	}

	public String getSummary() {
		return crh.getSummary();
	}

	public void setSummary(String summary) throws SemanticException {
		crh.setSummary(summary);
	}

	public List<ILevel> getColumnLevels() {
		List<ILevel> ms = new ArrayList<ILevel>();

		for (int i = 0; i < crh.getDimensionCount(COLUMN_AXIS_TYPE); i++) {
			DimensionViewHandle dv = crh.getDimension(COLUMN_AXIS_TYPE, i);

			for (int j = 0; j < dv.getLevelCount(); j++) {
				ms.add(new LevelImpl(dv.getLevel(j)));
			}
		}

		return Collections.unmodifiableList(ms);
	}

	public String getEmptyCellValue() {
		return crh.getEmptyCellValue();
	}

	public String getMeasureDirection() {
		return crh.getMeasureDirection();
	}

	public List<IMeasure> getMeasures() {
		List<IMeasure> ms = new ArrayList<IMeasure>();

		for (int i = 0; i < crh.getMeasureCount(); i++) {
			ms.add(new MeasureImpl(crh.getMeasure(i)));
		}

		return Collections.unmodifiableList(ms);
	}

	public List<ILevel> getRowLevels() {
		List<ILevel> ms = new ArrayList<ILevel>();

		for (int i = 0; i < crh.getDimensionCount(ROW_AXIS_TYPE); i++) {
			DimensionViewHandle dv = crh.getDimension(ROW_AXIS_TYPE, i);

			for (int j = 0; j < dv.getLevelCount(); j++) {
				ms.add(new LevelImpl(dv.getLevel(j)));
			}
		}

		return Collections.unmodifiableList(ms);
	}

	public boolean isRepeatColumnHeader() {
		return crh.isRepeatColumnHeader();
	}

	public boolean isRepeatRowHeader() {
		return crh.isRepeatRowHeader();
	}

	public void setEmptyCellValue(String value) throws SemanticException {
		crh.setEmptyCellValue(value);
	}

	public void setMeasureDirection(String direction) throws SemanticException {
		crh.setMeasureDirection(direction);
	}

	public void setRepeatColumnHeader(boolean value) throws SemanticException {
		crh.setRepeatColumnHeader(value);
	}

	public void setRepeatRowHeader(boolean value) throws SemanticException {
		crh.setRepeatRowHeader(value);
	}

	public int getRowPageBreakInterval() {
		return crh.getRowPageBreakInterval();
	}

	public void setRowPageBreakInterval(int value) throws SemanticException {
		crh.setRowPageBreakInterval(value);
	}

	public int getColumnPageBreakInterval() {
		return crh.getColumnPageBreakInterval();
	}

	public void setColumnPageBreakInterval(int value) throws SemanticException {
		crh.setColumnPageBreakInterval(value);
	}

}
