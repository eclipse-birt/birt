
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionDivider.CombinedPositionContructor;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionDivider.DimensionPositionSeeker.DimensionInfo;

/**
 * This class describes a fact table.
 */

public class FactTable {
	private String name;
	private DimensionInfo[] dimensionInfo;
	private MeasureInfo[] measureInfo, calculatedMeasureInfo;
	private int segmentCount;
	private DimensionDivision[] dimensionDivision;

	private IDocumentManager documentManager;
	private CombinedPositionContructor combinedPositionCalculator;
	private static Logger logger = Logger.getLogger(FactTable.class.getName());

	/**
	 * 
	 * @param name
	 * @param documentManager
	 * @param dimensionInfo
	 * @param measureInfo
	 * @param segmentCount
	 * @param dimensionDivision
	 */
	FactTable(String name, IDocumentManager documentManager, DimensionInfo[] dimensionInfo, MeasureInfo[] measureInfo,
			MeasureInfo[] calculatedMeasureInfo, int segmentCount, DimensionDivision[] dimensionDivision) {
		Object[] params = { name, documentManager, dimensionInfo, measureInfo, Integer.valueOf(segmentCount),
				dimensionDivision };
		logger.entering(FactTable.class.getName(), "FactTable", params);
		this.name = name;
		this.dimensionInfo = dimensionInfo;
		this.measureInfo = measureInfo;
		this.calculatedMeasureInfo = calculatedMeasureInfo;
		this.segmentCount = segmentCount;
		this.dimensionDivision = dimensionDivision;
		this.documentManager = documentManager;
		this.combinedPositionCalculator = new CombinedPositionContructor(dimensionDivision);
		logger.exiting(FactTable.class.getName(), "FactTable");
	}

	/**
	 * 
	 * @return
	 */
	public DimensionDivision[] getDimensionDivision() {
		return dimensionDivision;
	}

	/**
	 * 
	 * @return
	 */
	public CombinedPositionContructor getCombinedPositionCalculator() {
		return combinedPositionCalculator;
	}

	/**
	 * 
	 * @return
	 */
	public DimensionInfo[] getDimensionInfo() {
		return dimensionInfo;
	}

	/**
	 * 
	 * @return
	 */
	public MeasureInfo[] getMeasureInfo() {
		return measureInfo;
	}

	/**
	 * 
	 * @return
	 */
	public MeasureInfo[] getCalcualtedMeasureInfo() {
		return this.calculatedMeasureInfo;
	}

	/**
	 * 
	 * @return
	 */
	public String[] getMeasureNames() {
		String[] measureNames = new String[measureInfo.length];
		for (int i = 0; i < measureNames.length; i++) {
			measureNames[i] = measureInfo[i].getMeasureName();
		}
		return measureNames;
	}

	/**
	 * 
	 * @return
	 */
	public int getSegmentCount() {
		return segmentCount;
	}

	/**
	 * 
	 * @return
	 */
	public DimensionDivision[] getSubDimensions() {
		return dimensionDivision;
	}

	/**
	 * 
	 * @return
	 */
	public IDocumentManager getDocumentManager() {
		return documentManager;
	}

	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	public int getDimensionIndex(String dimensionName) {
		for (int i = 0; i < dimensionInfo.length; i++) {
			if (dimensionInfo[i].dimensionName.equals(dimensionName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param measureName
	 * @return
	 */
	public int getMeasureIndex(String measureName) {
		for (int i = 0; i < measureInfo.length; i++) {
			if (measureInfo[i].getMeasureName().equals(measureName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

}
