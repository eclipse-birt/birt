/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor;

import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;

/**
 * DataSetDesign Comparator for open source data sets
 */

public class OSDataSourceDesignComparator {
	public static boolean isEqualOSDataSourceDesign(IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSourceDesign dataSourceDesign2) {
		if (dataSourceDesign instanceof IOdaDataSourceDesign && dataSourceDesign2 instanceof IOdaDataSourceDesign) {
			IOdaDataSourceDesign dataSource = (IOdaDataSourceDesign) dataSourceDesign;
			IOdaDataSourceDesign dataSource2 = (IOdaDataSourceDesign) dataSourceDesign2;

			if (ComparatorUtil.isEqualString(dataSource.getExtensionID(), dataSource2.getExtensionID()) == false)
				return false;

			if (ComparatorUtil.isEqualProps(dataSource.getPublicProperties(),
					dataSource2.getPublicProperties()) == false
					|| ComparatorUtil.isEqualProps(dataSource.getPrivateProperties(),
							dataSource2.getPrivateProperties()) == false)
				return false;
			return true;
		} else if (dataSourceDesign instanceof IScriptDataSourceDesign
				&& dataSourceDesign2 instanceof IScriptDataSourceDesign) {
			IScriptDataSourceDesign dataSource = (IScriptDataSourceDesign) dataSourceDesign;
			IScriptDataSourceDesign dataSource2 = (IScriptDataSourceDesign) dataSourceDesign2;

			if (ComparatorUtil.isEqualString(dataSource.getOpenScript(), dataSource2.getOpenScript()) == false
					|| ComparatorUtil.isEqualString(dataSource.getCloseScript(), dataSource2.getCloseScript()) == false)
				return false;
			return true;
		}
		return false;
	}

	public static boolean isEqualBaseDataSourceDesign(IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSourceDesign dataSourceDesign2) {
		if (dataSourceDesign == dataSourceDesign2)
			return true;

		if (dataSourceDesign == null || dataSourceDesign2 == null)
			return false;

		if (!ComparatorUtil.isEqualString(dataSourceDesign.getName(), dataSourceDesign2.getName()))
			return false;

		// Two data source designs may be not equal to each other if only their
		// share the same script text, because their evaluated results may be
		// different. Here below we define they are different data source designs
		// if both of them have scripts. So that cache will not to be used in
		// script data source designs.
		if (dataSourceDesign.getBeforeOpenScript() != null || dataSourceDesign.getAfterOpenScript() != null
				|| dataSourceDesign.getBeforeCloseScript() != null || dataSourceDesign.getAfterCloseScript() != null)
			return false;
		return true;
	}

}
