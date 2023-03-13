/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.adapter.oda.ODAFactory;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * The utility to convert between native data type in integer and ROM data type
 * in string.
 */

class NativeDataTypeUtil {

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
	 * @throws OdaException
	 * @throws BirtException
	 */

	public static String getUpdatedDataType(String dataSourceId, String dataSetId, int nativeCode, String romDataType,
			String choiceName) throws BirtException {
		return ODAFactory.getFactory().getUpdatedDataType(dataSourceId, dataSetId, nativeCode, romDataType, choiceName);
	}
}
