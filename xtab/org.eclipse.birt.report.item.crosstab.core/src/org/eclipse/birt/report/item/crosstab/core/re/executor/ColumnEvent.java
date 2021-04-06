/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.re.executor;

/**
 * ColumnEvent
 */
abstract class ColumnEvent {

	static final int UNKNOWN_CHANGE = -1;
	static final int ROW_EDGE_CHANGE = 1;
	static final int COLUMN_EDGE_CHANGE = 2;
	static final int COLUMN_TOTAL_CHANGE = 3;
	static final int GRAND_TOTAL_CHANGE = 4;
	static final int MEASURE_HEADER_CHANGE = 5;
	static final int MEASURE_CHANGE = 6;

	int type;
	int dimensionIndex = -1;
	int levelIndex = -1;
	int measureIndex = -1;
	boolean isLocationBefore;

	long dataPosition = -1;

}

class RowEdgeColumnEvent extends ColumnEvent {

	RowEdgeColumnEvent(int dimensionIndex, int levelIndex) {
		type = ROW_EDGE_CHANGE;
		this.dimensionIndex = dimensionIndex;
		this.levelIndex = levelIndex;
	}

	public String toString() {
		return "Type: ROW_EDGE, Dimension: " //$NON-NLS-1$
				+ dimensionIndex + ", Level: " //$NON-NLS-1$
				+ levelIndex;
	}
}

class ColumnEdgeColumnEvent extends ColumnEvent {

	ColumnEdgeColumnEvent(int measureIndex) {
		type = COLUMN_EDGE_CHANGE;
		this.measureIndex = measureIndex;
	}

	public String toString() {
		return "Type: COLUMN_EDGE, Measure: " + measureIndex //$NON-NLS-1$
				+ ", Data Position: " //$NON-NLS-1$
				+ dataPosition;
	}

}

class ColumnTotalColumnEvent extends ColumnEvent {

	ColumnTotalColumnEvent(boolean isLocationBefore, int dimensionIndex, int levelIndex, int measureIndex) {
		type = COLUMN_TOTAL_CHANGE;
		this.dimensionIndex = dimensionIndex;
		this.levelIndex = levelIndex;
		this.measureIndex = measureIndex;
		this.isLocationBefore = isLocationBefore;
	}

	public String toString() {
		return "Type: COLUMN_TOTAL, Dimension: " //$NON-NLS-1$
				+ dimensionIndex + ", Level: " //$NON-NLS-1$
				+ levelIndex + ", Measure: " //$NON-NLS-1$
				+ measureIndex + ", Location: " //$NON-NLS-1$
				+ (isLocationBefore ? "Before" : "After") //$NON-NLS-1$ //$NON-NLS-2$
				+ ", Data Position: " //$NON-NLS-1$
				+ dataPosition;
	}

}

class GrandTotalColumnEvent extends ColumnEvent {

	GrandTotalColumnEvent(int measureIndex) {
		type = GRAND_TOTAL_CHANGE;
		this.measureIndex = measureIndex;
	}

	public String toString() {
		return "Type: GRAND_TOTAL, Measure: " + measureIndex; //$NON-NLS-1$
	}

}

class MeasureHeaderColumnEvent extends ColumnEvent {

	MeasureHeaderColumnEvent() {
		type = MEASURE_HEADER_CHANGE;
	}

	public String toString() {
		return "Type: MEASURE_HEADER"; //$NON-NLS-1$
	}
}

class MeasureColumnEvent extends ColumnEvent {

	MeasureColumnEvent(int measureIndex) {
		type = MEASURE_CHANGE;
		this.measureIndex = measureIndex;
	}

	public String toString() {
		return "Type: MEASURE, Measure: " + measureIndex; //$NON-NLS-1$
	}

}
