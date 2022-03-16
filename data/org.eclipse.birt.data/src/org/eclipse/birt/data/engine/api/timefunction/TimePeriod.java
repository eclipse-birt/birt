/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.timefunction;

public class TimePeriod implements ITimePeriod {
	public int countOfUnit = 0;
	public TimePeriodType type;
	public boolean isCurrent = false;

	public TimePeriod(int countOfUnit, TimePeriodType type) {
		this.countOfUnit = countOfUnit;
		this.type = type;
	}

	public TimePeriod(int countOfUnit, TimePeriodType type, boolean isCurrent) {
		this.countOfUnit = countOfUnit;
		this.type = type;
		this.isCurrent = isCurrent;
	}

	@Override
	public int countOfUnit() {
		return this.countOfUnit;
	}

	@Override
	public TimePeriodType getType() {
		return type;
	}

	@Override
	public boolean isCurrent() {
		return this.isCurrent;
	}

}
