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
