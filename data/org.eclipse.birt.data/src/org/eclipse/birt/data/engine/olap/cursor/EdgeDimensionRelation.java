/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * This class contains the relation description between dimension and its
 * belonging edge, and some shared information in dimension and edge traverse.
 * 
 * The relation[axisIndex] is an Vector of EdgeInfo objects at the specified
 * axis index. axisIndex is a 0-based index. Example: Dim Country City Product
 * 0: CHINA BEIJING P1 1: CHINA BEIJING P2 2: CHINA BEIJING P3 3: CHINA SHANGHAI
 * P1 4: CHINA SHANGHAI P2 5: CHINA SHANGHAI P3 6: USA CHICAGO P1 7: USA NEW
 * YORK P1 8: USA NEW YORK P2
 * 
 * edgeInfo: (start, end) Country City Product
 * ============================================ 0: -1,0 0,0 0,0 1: -1,2 0,3 0,1
 * 2: 1,6 0,2 3: 1,7 1,3 4: 1,4 5: 1,5 6: 2,6 7: 3,7 8: 3,8
 * 
 * If this edge has mirrored level, the non-mirrored level will be only
 * populated its edgeInfo Example: Product level has been mirrored edgeInfo:
 * (start, end) Country City Product
 * ============================================ 0: -1,0 0,0 [P1,P2,P3] 1: -1,2
 * 0,3 2: 1,6 3: 1,7 The product level's value will be sorted according to its
 * basic sort definition. But in case of aggregation sort, we should try to keep
 * its original sort result.
 * 
 */
class EdgeDimensionRelation {
	List[] currentRelation;
	int traverseLength;
	ResultSetFetcher fetcher;
	private List sectionList;

	EdgeDimensionRelation(RowDataAccessorService service, ResultSetFetcher fetcher, boolean isPage) throws IOException {
		IAggregationResultSet rs = fetcher.getAggrResultSet();
		DimensionAxis[] dimAxis = service.getDimensionAxis();

		this.sectionList = new ArrayList();
		this.fetcher = fetcher;
		int customDimSize = dimAxis.length;

		this.traverseLength = rs.length();

		Object[] preValue = new Object[customDimSize];
		Object[] currValue = new Object[customDimSize];

		Section section = null;
		boolean newSection = true;
		int startId = 0;

		if (this.traverseLength == 0) {
			section = new Section(customDimSize, -1, -1);
			this.sectionList.add(section);
			this.currentRelation = ((Section) this.sectionList.get(0)).getRelation();
		} else {
			for (int rowId = 0; rowId < traverseLength; rowId++) {
				rs.seek(rowId);
				for (int i = 0; i < customDimSize; i++) {
					if (fetcher.getLevelKeyValue(service.getDimensionAxis()[i].getLevelIndex()) == null) {
						currValue[i] = null;
					} else {
						int index = fetcher.getAggrResultSet()
								.getLevelKeyColCount(service.getDimensionAxis()[i].getLevelIndex()) - 1;
						currValue[i] = fetcher.getLevelKeyValue(service.getDimensionAxis()[i].getLevelIndex())[index];
					}
				}
				int breakLevel;
				if (newSection) {
					section = new Section(customDimSize, -1, -1);
					newSection = false;
					this.sectionList.add(section);
					breakLevel = 0;
				} else {
					breakLevel = getBreakLevel(currValue, preValue, section, rowId);

					if (breakLevel <= service.getPagePosition() && !isPage) {
						section.setBaseStart(startId);
						section.setBaseEnd(rowId - 1);
						startId = rowId;
						rowId--;
						newSection = true;
					}
				}

				if (!newSection) {
					for (int level = breakLevel; level < customDimSize; level++) {
						EdgeInfo edge = new EdgeInfo();
						if (currValue[level] == null)
							edge.isNull = 0;

						if (level != 0)
							edge.parent = section.getRelation()[level - 1].size() - 1;
						if (level == section.getRelation().length - 1) {
							edge.firstChild = rowId;
						} else {
							edge.firstChild = section.getRelation()[level + 1].size();
						}
						section.getRelation()[level].add(edge);
					}

					for (int i = 0; i < customDimSize; i++) {
						preValue[i] = currValue[i];
					}
				}
			}

			section.setBaseStart(startId);
			section.setBaseEnd(this.traverseLength - 1);

			this.currentRelation = ((Section) this.sectionList.get(0)).getRelation();
			this.traverseLength = ((Section) this.sectionList.get(0)).getBaseEnd()
					- ((Section) this.sectionList.get(0)).getBaseStart() + 1;
		}
	}

	/**
	 * 
	 * @param currValue
	 * @param preValue
	 * @param rowId
	 * @return
	 */
	private int getBreakLevel(Object[] currValue, Object[] preValue, Section section, int rowId) {
		assert preValue != null && currValue != null;
		int breakLevel = 0;
		for (; breakLevel < currValue.length; breakLevel++) {
			// get the first child of current group in level of breakLevel
			List list = section.getRelation()[breakLevel];
			EdgeInfo edgeInfo = (EdgeInfo) list.get(list.size() - 1);
			int child = edgeInfo.firstChild;

			Object currObjectValue = currValue[breakLevel];
			Object prevObjectValue = preValue[breakLevel];

			for (int level = breakLevel + 1; level < section.getRelation().length; level++) {
				list = section.getRelation()[level];
				edgeInfo = (EdgeInfo) list.get(child);
				child = edgeInfo.firstChild;
			}

			// determines whether next row is in current group
			if (isEqualObject(currObjectValue, prevObjectValue) == false) {
				break;
			}

		}
		return breakLevel;
	}

	/**
	 * 
	 * @param position
	 */
	public void synchronizedWithPage(int position) {
		if (this.sectionList.size() > position) {
			this.currentRelation = ((Section) this.sectionList.get(position)).getRelation();
			{
				this.traverseLength = ((Section) this.sectionList.get(position)).getBaseEnd()
						- ((Section) this.sectionList.get(position)).getBaseStart() + 1;
			}
		}
	}

	/**
	 * 
	 * @param preValue
	 * @param currentValue
	 * @return
	 */
	private boolean isEqualObject(Object preValue, Object currentValue) {
		if (preValue == currentValue) {
			return true;
		}
		if (preValue == null || currentValue == null) {
			return false;
		}
		return preValue.equals(currentValue);
	}
}

class EdgeInfo {
	int parent = -1;
	int firstChild = -1;
	int isNull = -1;
}
