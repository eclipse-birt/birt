
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.ITimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;

/**
 *
 */

public class BindingIOUtil {
	/**
	 *
	 * @param dos
	 * @param binding
	 * @throws DataException
	 */
	public static void saveBinding(DataOutputStream dos, IBinding binding, int version) throws DataException {
		int type = binding.getDataType();
		String name = binding.getBindingName();
		String function = binding.getAggrFunction();
		IBaseExpression expr = binding.getExpression();
		IBaseExpression filter = binding.getFilter();
		List arguments = binding.getArguments();
		List aggregateOn = binding.getAggregatOns();
		ITimeFunction timeFunction = binding.getTimeFunction();

		try {
			// First write data type.
			IOUtil.writeInt(dos, type);

			// Then write Name
			IOUtil.writeString(dos, name);

			// Then write function
			IOUtil.writeString(dos, function);

			// Then write base expr
			ExprUtil.saveBaseExpr(dos, expr);

			// Then write filter
			ExprUtil.saveBaseExpr(dos, filter);

			// Then write argument size
			IOUtil.writeInt(dos, arguments.size());

			for (int i = 0; i < arguments.size(); i++) {
				ExprUtil.saveBaseExpr(dos, (IBaseExpression) arguments.get(i));
			}

			IOUtil.writeInt(dos, aggregateOn.size());

			for (int i = 0; i < aggregateOn.size(); i++) {
				IOUtil.writeString(dos, aggregateOn.get(i).toString());
			}

			if (version >= VersionManager.VERSION_2_6_3_1) {
				if (timeFunction != null) {
					// contains time function
					IOUtil.writeBool(dos, true);
					IOUtil.writeString(dos, timeFunction.getTimeDimension());
					IOUtil.writeObject(dos, timeFunction.getReferenceDate().getDate());
					ITimePeriod baseTimePeriod = timeFunction.getBaseTimePeriod();
					ITimePeriod relativeTimePeriod = timeFunction.getRelativeTimePeriod();
					if (baseTimePeriod != null) {
						IOUtil.writeBool(dos, true);
						IOUtil.writeString(dos, baseTimePeriod.getType().toString());
						IOUtil.writeInt(dos, baseTimePeriod.countOfUnit());
						IOUtil.writeBool(dos, baseTimePeriod.isCurrent());
					} else {
						IOUtil.writeBool(dos, false);
					}
					if (relativeTimePeriod != null) {
						IOUtil.writeBool(dos, true);
						IOUtil.writeObject(dos, relativeTimePeriod.getType().toString());
						IOUtil.writeInt(dos, relativeTimePeriod.countOfUnit());
					} else {
						IOUtil.writeBool(dos, false);
					}
				} else {
					IOUtil.writeBool(dos, false);
				}
			}
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	public static IBinding loadBinding(DataInputStream dis, int version) throws IOException, DataException {
		int type = IOUtil.readInt(dis);
		String name = IOUtil.readString(dis);
		String function = IOUtil.readString(dis);
		IBaseExpression expr = ExprUtil.loadBaseExpr(dis);
		IBaseExpression filter = ExprUtil.loadBaseExpr(dis);

		Binding binding = new Binding(name);
		binding.setAggrFunction(function);
		binding.setDataType(type);
		binding.setExpression(expr);
		binding.setFilter(filter);

		int argSize = IOUtil.readInt(dis);
		for (int i = 0; i < argSize; i++) {
			binding.addArgument(ExprUtil.loadBaseExpr(dis));
		}

		int aggrSize = IOUtil.readInt(dis);
		for (int i = 0; i < aggrSize; i++) {
			binding.addAggregateOn(IOUtil.readString(dis));
		}

		if (version >= VersionManager.VERSION_2_6_3_1) {
			boolean hasTimeFunction = IOUtil.readBool(dis);
			if (hasTimeFunction) {
				String timeDimensionName = IOUtil.readString(dis);
				TimeFunction time = new TimeFunction();
				if (timeDimensionName != null) {
					time.setTimeDimension(timeDimensionName);
					Date referenceDate = (Date) IOUtil.readObject(dis);
					time.setReferenceDate(new ReferenceDate(referenceDate));

					boolean containsBasePeriod = IOUtil.readBool(dis);
					if (containsBasePeriod) {
						TimePeriodType periodType = getPeriodType(IOUtil.readString(dis));
						int unit = IOUtil.readInt(dis);
						boolean isCurrent = IOUtil.readBool(dis);
						TimePeriod basedTimePeriod = new TimePeriod(unit, periodType, isCurrent);
						time.setBaseTimePeriod(basedTimePeriod);
					}
					boolean containsRelativePeriod = IOUtil.readBool(dis);
					if (containsRelativePeriod) {
						TimePeriodType periodType = getPeriodType(IOUtil.readString(dis));
						int unit = IOUtil.readInt(dis);
						TimePeriod relativeTimePeriod = new TimePeriod(unit, periodType);
						time.setRelativeTimePeriod(relativeTimePeriod);
					}
				}
				binding.setTimeFunction(time);
			}
		}
		return binding;
	}

	private static TimePeriodType getPeriodType(String type) {
		if (type == null) {
			return null;
		}
		if (type.equals(TimePeriodType.YEAR.toString())) {
			return TimePeriodType.YEAR;
		}
		if (type.equals(TimePeriodType.QUARTER.toString())) {
			return TimePeriodType.QUARTER;
		}
		if (type.equals(TimePeriodType.MONTH.toString())) {
			return TimePeriodType.MONTH;
		}
		if (type.equals(TimePeriodType.WEEK.toString())) {
			return TimePeriodType.WEEK;
		}
		if (type.equals(TimePeriodType.DAY.toString())) {
			return TimePeriodType.DAY;
		}
		return null;
	}
}
