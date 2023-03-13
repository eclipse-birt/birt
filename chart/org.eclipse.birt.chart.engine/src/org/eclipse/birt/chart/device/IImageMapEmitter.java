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

package org.eclipse.birt.chart.device;

/**
 * This interface defines the capability for implementors to provide an html
 * image map string.
 */
public interface IImageMapEmitter {

	/**
	 * Returns the image map string by current emitter implementation.
	 */
	String getImageMap();

}
