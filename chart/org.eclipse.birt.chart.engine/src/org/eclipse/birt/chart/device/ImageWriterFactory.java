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

	public ImageWriter createByFormatName(String formatName) {
		if (formatName != null) {
			Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(formatName);
			if (it.hasNext()) {
				return it.next();
			}
		}

		return null;
	}

	public ImageWriter createImageWriter(String formatName, String outputFormat) {
		return createByFormatName(formatName);
	}

}
