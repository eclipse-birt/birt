/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AxisProcessor {
	/**
	 * Each element of naxis is the start point of each column, indexed by colId.
	 */
	private List<Integer> columnCoordinates = new ArrayList<>();

	public AxisProcessor() {
		addCoordinate(0);
	}

	public void addCoordinates(int[] values) {
		for (int i = 0; i < values.length; i++) {
			addCoordinateWithoutSort(values[i]);
		}
		Collections.sort(columnCoordinates);
	}

	public void addCoordinate(int value) {
		if (addCoordinateWithoutSort(value)) {
			Collections.sort(columnCoordinates);
		}
	}

	public boolean addCoordinateWithoutSort(int value) {
		Integer index = value;

		if (!columnCoordinates.contains(index)) {
			columnCoordinates.add(index);
			return true;
		}
		return false;
	}

	/**
	 * Gets a subset of naxis.
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public int[] getColumnCoordinatesInRange(int start, int end) {
		int startColumnIndex = getColumnIndexByCoordinate(start);
		int endColumnIndex = getColumnIndexByCoordinate(end);

		List<Integer> list = columnCoordinates.subList(startColumnIndex, endColumnIndex + 1);
		int length = list.size();

		int[] columnCoordinates = new int[length];
		for (int i = 0; i < length; i++) {
			columnCoordinates[i] = list.get(i);
		}
		return columnCoordinates;
	}

	/**
	 * It is not going to get the coordinate, but the colId relative to this
	 * coordinate. Gets the colId of the given coordinate point
	 *
	 * @param value the coordinate point
	 * @return the colId
	 */
	public int getColumnIndexByCoordinate(int value) {
		return columnCoordinates.indexOf(value);
	}

	public int[] getColumnWidths() {
		int length = columnCoordinates.size();
		int[] columnWidths = new int[length];

		for (int i = 0; i < length - 1; i++) {
			columnWidths[i] = columnCoordinates.get(i + 1) - columnCoordinates.get(i);
		}
		return columnWidths;
	}
}
