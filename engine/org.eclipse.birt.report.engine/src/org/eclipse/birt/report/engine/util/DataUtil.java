/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.util;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.ir.DimensionType;

public class DataUtil {

	public static <T> T convertType(Object tempValue, Class<T> type) throws BirtException {
		T value = null;
		if (tempValue == null || type.isAssignableFrom(tempValue.getClass())) {
			value = (T) tempValue;
		} else if (type == DimensionType.class) {
			// todo
		} else {
			value = (T) DataTypeUtil.convert(tempValue, type);
		}
		return value;
	}

}
