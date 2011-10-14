/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.api.timefunction;

public class TimePeriod implements ITimePeriod
{
	public int countOfUnit =0;
	public TimePeriodType type;
	
	public TimePeriod( int countOfUnit, TimePeriodType type )
	{
		this.countOfUnit = countOfUnit;
		this.type = type;
	}
	
	public int countOfUnit( ) 
	{
		return this.countOfUnit;
	}

	public TimePeriodType getType() {
		return type;
	}

}
