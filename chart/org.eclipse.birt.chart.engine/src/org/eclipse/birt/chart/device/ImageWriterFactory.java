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

import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

/**
 *
 */

public class ImageWriterFactory implements IImageWriterFactory {

	private static IImageWriterFactory factory = new ImageWriterFactory();

	public static void initInstance(IImageWriterFactory tFactory) {
		factory = tFactory;
	}

	public static IImageWriterFactory instance() {
		return factory;
	}

	@Override
	public ImageWriter createByFormatName(String formatName) {
		if (formatName != null) {
			Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(formatName);
			if (it.hasNext()) {
				return it.next();
			}
		}

		return null;
	}

	@Override
	public ImageWriter createImageWriter(String formatName, String outputFormat) {
		return createByFormatName(formatName);
	}

}
