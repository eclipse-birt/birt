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

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;

/**
 *
 */

public class DataSetDesignComparator {
	public static boolean isEqualDataSetDesign(IBaseDataSetDesign dataSetDesign, IBaseDataSetDesign dataSetDesign2) {
		if (!OSDataSetDesignComparator.isEqualBaseDataSetDesign(dataSetDesign, dataSetDesign2)) {
			return false;
		}
		return OSDataSetDesignComparator.isEqualOSDataSetDesign(dataSetDesign, dataSetDesign2);
	}
}
