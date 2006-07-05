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

package org.eclipse.birt.chart.internal.factory;

import java.util.Date;

/**
 * This interface defines a DateFormat wrapper. Note this interface is only
 * intended to be used internally.
 */
public interface IDateFormatWrapper
{

	String format( Date date );
}
