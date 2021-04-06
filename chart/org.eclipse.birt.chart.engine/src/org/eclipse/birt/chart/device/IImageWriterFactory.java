/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
