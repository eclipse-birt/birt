/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor;

import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.impl.DataSetAdapter;

/**
 * DataSetDesign Comparator for open source data sets
 */

public class OSDataSetDesignComparator {
	public static boolean isEqualOSDataSetDesign(IBaseDataSetDesign dataSetDesign, IBaseDataSetDesign dataSetDesign2) {
		if (dataSetDesign instanceof IOdaDataSetDesign && dataSetDesign2 instanceof IOdaDataSetDesign) {
			IOdaDataSetDesign dataSet = (IOdaDataSetDesign) dataSetDesign;
			IOdaDataSetDesign dataSet2 = (IOdaDataSetDesign) dataSetDesign2;

			if (ComparatorUtil.isEqualString(dataSet.getQueryText(), dataSet2.getQueryText()) == false
					|| ComparatorUtil.isEqualString(dataSet.getExtensionID(), dataSet2.getExtensionID()) == false
					|| ComparatorUtil.isEqualString(dataSet.getPrimaryResultSetName(),
							dataSet2.getPrimaryResultSetName()) == false
					|| ComparatorUtil.isEqualProps(dataSet.getPublicProperties(),
							dataSet2.getPublicProperties()) == false
					|| ComparatorUtil.isEqualProps(dataSet.getPrivateProperties(),
							dataSet2.getPrivateProperties()) == false)
				return false;
		}

		if (dataSetDesign instanceof IScriptDataSetDesign && dataSetDesign2 instanceof IScriptDataSetDesign) {
			IScriptDataSetDesign dataSet = (IScriptDataSetDesign) dataSetDesign;
			IScriptDataSetDesign dataSet2 = (IScriptDataSetDesign) dataSetDesign2;

			if (ComparatorUtil.isEqualString(dataSet.getOpenScript(), dataSet2.getOpenScript()) == false
					|| ComparatorUtil.isEqualString(dataSet.getFetchScript(), dataSet2.getFetchScript()) == false
					|| ComparatorUtil.isEqualString(dataSet.getCloseScript(), dataSet2.getCloseScript()) == false
					|| ComparatorUtil.isEqualString(dataSet.getDescribeScript(), dataSet2.getDescribeScript()) == false)
				return false;
		}

		if (dataSetDesign instanceof IJointDataSetDesign && dataSetDesign2 instanceof IJointDataSetDesign) {
			IJointDataSetDesign design1 = (IJointDataSetDesign) dataSetDesign;
			IJointDataSetDesign design2 = (IJointDataSetDesign) dataSetDesign2;
			if (ComparatorUtil.isEqualString(design1.getLeftDataSetDesignName(),
					design2.getLeftDataSetDesignName()) == false
					|| ComparatorUtil.isEqualString(design1.getLeftDataSetDesignQulifiedName(),
							design2.getLeftDataSetDesignQulifiedName()) == false
					|| ComparatorUtil.isEqualString(design1.getRightDataSetDesignName(),
							design2.getRightDataSetDesignName()) == false
					|| ComparatorUtil.isEqualString(design1.getRightDataSetDesignQulifiedName(),
							design2.getRightDataSetDesignQulifiedName()) == false
					|| design1.getJoinType() != design2.getJoinType() || ComparatorUtil
							.isEqualJointCondition(design1.getJoinConditions(), design2.getJoinConditions()) == false)
				return false;
		}

		// Since no difference is found, just return true for now.
		// If more checks are needed should be determined by outside caller.
		return true;
	}

	public static boolean isEqualBaseDataSetDesign(IBaseDataSetDesign dataSetDesign,
			IBaseDataSetDesign dataSetDesign2) {
		if (dataSetDesign == dataSetDesign2)
			return true;

		if (dataSetDesign == null || dataSetDesign2 == null)
			return false;

		if (!ComparatorUtil.isEqualString(dataSetDesign.getName(), dataSetDesign2.getName()))
			return false;

		if (dataSetDesign.getRowFetchLimit() != dataSetDesign2.getRowFetchLimit()) {
			return false;
		}

		if (ComparatorUtil.isEqualString(dataSetDesign.getBeforeOpenScript(),
				dataSetDesign2.getBeforeOpenScript()) == false
				|| ComparatorUtil.isEqualString(dataSetDesign.getAfterOpenScript(),
						dataSetDesign2.getAfterOpenScript()) == false
				|| ComparatorUtil.isEqualString(dataSetDesign.getBeforeCloseScript(),
						dataSetDesign2.getBeforeCloseScript()) == false
				|| ComparatorUtil.isEqualString(dataSetDesign.getAfterCloseScript(),
						dataSetDesign2.getAfterCloseScript()) == false
				|| ComparatorUtil.isEqualString(dataSetDesign.getOnFetchScript(),
						dataSetDesign2.getOnFetchScript()) == false)
			return false;

		if (ComparatorUtil.isEqualComputedColumns(dataSetDesign.getComputedColumns(),
				dataSetDesign2.getComputedColumns()) == false
				|| ComparatorUtil.isEqualParameters(dataSetDesign.getParameters(),
						dataSetDesign2.getParameters()) == false
				|| ComparatorUtil.isEqualResultHints(dataSetDesign.getResultSetHints(),
						dataSetDesign2.getResultSetHints()) == false)
			return false;

		List filter1 = getFilter(dataSetDesign);
		List filter2 = getFilter(dataSetDesign2);

		if (ComparatorUtil.isEqualFilters(filter1, filter2) == false)
			return false;

		if (dataSetDesign.getCacheRowCount() != dataSetDesign2.getCacheRowCount())
			return false;
		return true;
	}

	private static List getFilter(IBaseDataSetDesign dataSetDesign) {
		return (dataSetDesign instanceof DataSetAdapter) ? ((DataSetAdapter) dataSetDesign).getSource().getFilters()
				: dataSetDesign.getFilters();
	}

}
