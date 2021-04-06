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

package org.eclipse.birt.report.model.adapter.oda;

import org.eclipse.birt.core.exception.BirtException;

/**
 * The factory contains methods that make calls to datatools.oda objects.
 * 
 */

public interface IODAFactory {

	/**
	 * Returns the data type by given nativeCode, ROM data type and choice name.
	 * 
	 * @param dataSourceId the id of the data source
	 * @param dataSetId    the id of the data set
	 * @param nativeCode   the native data type code.
	 * @param romDataType  the ROM defined data type in string
	 * @param choiceName   the ROM choice to map. Can be column data type choice or
	 *                     parameter data type choices.
	 * 
	 * @return the ROM defined data type in string
	 * @throws BirtException
	 */

	public String getUpdatedDataType(String dataSourceId, String dataSetId, int nativeCode, String romDataType,
			String choiceName) throws BirtException;
}
