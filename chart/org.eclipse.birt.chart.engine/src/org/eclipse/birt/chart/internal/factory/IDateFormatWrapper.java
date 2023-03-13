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

package org.eclipse.birt.chart.internal.factory;

import java.util.Date;

import org.eclipse.birt.chart.util.CDateTime;

/**
 * This interface defines a DateFormat wrapper. Note this interface is only
 * intended to be used internally.
 */
public interface IDateFormatWrapper {

	String format(Date date);

	String format(CDateTime date);

	/** Returns localized pattern of this format. */
	String toLocalizedPattern();
}
