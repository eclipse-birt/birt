/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.odf;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public class TableInfo {

	private Hashtable<Integer, List<SpanInfo>> spans = new Hashtable<Integer, List<SpanInfo>>();

	private double[] cols;

	private int crow = 0;

	private IStyle style = null;

	private Map<DimensionType, StyleEntry> rowHeightStyles;

	public TableInfo(double[] cols, IStyle style) {
		this.cols = cols;
		this.style = style;
		rowHeightStyles = new HashMap<DimensionType, StyleEntry>();
	}

	void newRow() {
		this.crow++;
	}

	void addSpan(int columnId, int columnSpan, int rowSpan, StyleEntry style) {
		for (int i = 1; i < rowSpan; i++) {
			Integer key = crow + i;

			if (spans.containsKey(key)) {
				List<SpanInfo> rSpan = spans.get(key);
				rSpan.add(new SpanInfo(columnId, columnSpan, 1, false, style));
				Collections.sort(rSpan, new Comparator<SpanInfo>() {

					public int compare(SpanInfo o1, SpanInfo o2) {
						SpanInfo r1 = o1;
						SpanInfo r2 = o2;
						return r1.getColumnId() - r2.getColumnId();
					}
				});
			} else {
				Vector<SpanInfo> rSpan = new Vector<SpanInfo>();
				rSpan.add(new SpanInfo(columnId, columnSpan, 1, false, style));
				spans.put(key, rSpan);
			}
		}
	}

	List<SpanInfo> getSpans(int end) {
		List<SpanInfo> cSpans = spans.get(crow);

		if (cSpans == null) {
			return null;
		}

		Vector<SpanInfo> cList = new Vector<SpanInfo>();

		int pos = -1;

		for (int i = 0; i < cSpans.size(); i++) {
			SpanInfo r = cSpans.get(i);

			if ((r.getColumnId() + r.getColumnSpan() - 1) <= end) {

				cList.add(r);

				pos = i;
			} else {
				break;
			}
		}

		for (int i = 0; i <= pos; i++) {
			cSpans.remove(0);
		}

		if (cSpans.size() == 0) {
			removeSpan();
		}

		return cList.size() == 0 ? null : cList;
	}

	public void removeSpan() {
		spans.remove(crow);
	}

	public double[] getColumnWidths() {
		return cols;
	}

	int getRow() {
		return crow;
	}

	public IStyle getTableStyle() {
		return this.style;
	}

	public StyleEntry getRowHeightStyle(DimensionType rowHeight) {
		return rowHeightStyles.get(rowHeight);
	}

	public void addRowHeightStyle(DimensionType rowHeight, StyleEntry style) {
		rowHeightStyles.put(rowHeight, style);
	}
}
