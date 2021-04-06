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
