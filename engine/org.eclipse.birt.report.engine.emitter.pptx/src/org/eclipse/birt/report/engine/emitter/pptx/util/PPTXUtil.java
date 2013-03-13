/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx.util;

import org.eclipse.birt.report.engine.ooxml.util.OOXmlUtil;

public class PPTXUtil
{
	public static int convertToPointer(int milliPointerValue)
	{
		return milliPointerValue / 1000;
	}

	public static int convertToEnums( double milliPointerValue )
	{
		return (int) OOXmlUtil.convertPointerToEmus( milliPointerValue / 1000 );
	}
}
