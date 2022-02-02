/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import javax.imageio.ImageWriter;

/**
 * This interface defines the ability of creating an ImageWriter.
 */

public interface IImageWriterFactory {

	ImageWriter createByFormatName(String formatName);

	ImageWriter createImageWriter(String formatName, String outputFormat);

}
