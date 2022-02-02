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

package org.eclipse.birt.report.item.crosstab.core.script;

import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;

/**
 * ICrosstab
 */
public interface ICrosstab extends IReportItem, ICrosstabConstants {

	List<IMeasure> getMeasures();

	List<ILevel> getRowLevels();

	List<ILevel> getColumnLevels();

	String getCaption();

	void setCaption(String caption) throws SemanticException;

	String getCaptionKey();

	void setCaptionKey(String captionKey) throws SemanticException;

	String getSummary();

	void setSummary(String summary) throws SemanticException;

	boolean isRepeatRowHeader();

	void setRepeatRowHeader(boolean value) throws SemanticException;

	boolean isRepeatColumnHeader();

	void setRepeatColumnHeader(boolean value) throws SemanticException;

	String getMeasureDirection();

	void setMeasureDirection(String direction) throws SemanticException;

	String getEmptyCellValue();

	void setEmptyCellValue(String value) throws SemanticException;

	int getRowPageBreakInterval();

	void setRowPageBreakInterval(int value) throws SemanticException;

	int getColumnPageBreakInterval();

	void setColumnPageBreakInterval(int value) throws SemanticException;
}
