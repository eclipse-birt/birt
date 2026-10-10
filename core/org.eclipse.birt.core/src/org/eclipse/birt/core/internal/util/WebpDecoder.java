/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.core.internal.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.twelvemonkeys.imageio.plugins.webp.WebPImageReaderSpi;

/**
 * The only class that refers to the optional TwelveMonkeys WebP plugin. It is
 * reached through {@link ImageConversionUtil}, which catches the linkage error
 * thrown when the plugin is missing.
 *
 * The reader is created directly instead of being looked up in the global
 * ImageIO registry, which does not see OSGi bundles, and the streams are kept
 * in memory whatever {@link ImageIO#getUseCache()} says.
 */
final class WebpDecoder {

	private static final ImageReaderSpi READER_SPI = new WebPImageReaderSpi();

	private WebpDecoder() {
	}

	static byte[] toPng(byte[] data) throws IOException {
		ImageReader reader = READER_SPI.createReaderInstance();
		try (ImageInputStream in = new MemoryCacheImageInputStream(new ByteArrayInputStream(data))) {
			reader.setInput(in, true, true);
			return writePng(reader.read(0));
		} finally {
			reader.dispose();
		}
	}

	static int[] getSize(byte[] data) throws IOException {
		ImageReader reader = READER_SPI.createReaderInstance();
		try (ImageInputStream in = new MemoryCacheImageInputStream(new ByteArrayInputStream(data))) {
			reader.setInput(in, true, true);
			return new int[] { reader.getWidth(0), reader.getHeight(0) };
		} finally {
			reader.dispose();
		}
	}

	private static byte[] writePng(BufferedImage image) throws IOException {
		ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next(); //$NON-NLS-1$
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ImageOutputStream ios = new MemoryCacheImageOutputStream(out)) {
			writer.setOutput(ios);
			writer.write(image);
		} finally {
			writer.dispose();
		}
		return out.toByteArray();
	}
}
