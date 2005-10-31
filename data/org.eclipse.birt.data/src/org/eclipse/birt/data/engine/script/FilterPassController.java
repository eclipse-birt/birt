
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.script;

/**
 * This class control the current pass level of filter.This
 * Class would be inherited by other classes to do the actual
 * filter evaluation job.
 */

public class FilterPassController
{
	public static final int FIRST_PASS = 1;
	public static final int SECOND_PASS = 2;
	public static final int DEFAULT_PASS = 0;
	private static int passLevel = DEFAULT_PASS;
	
	public static int getPassLevel()
	{
		return passLevel;
	}
	
	public static void setPassLevel( int i )
	{
		passLevel = i;
	}
}
