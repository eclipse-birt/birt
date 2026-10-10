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

package org.eclipse.birt.core.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.eclipse.birt.core.internal.util.ImageConversionUtil;

import junit.framework.TestCase;

/**
 * Tests the WebP to PNG conversion of {@link ImageConversionUtil} with a 16x8
 * lossless image whose left half is transparent.
 */
public class ImageConversionUtilTest extends TestCase {

	private byte[] readWebp() throws IOException {
		try (InputStream in = getClass().getResourceAsStream("lossless-alpha.webp")) { //$NON-NLS-1$
			return in.readAllBytes();
		}
	}

	public void testToPngKeepsAlpha() throws IOException {
		byte[] png = ImageConversionUtil.convertUnsupportedFormat(readWebp());
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(png));
		assertNotNull(image);
		assertEquals(16, image.getWidth());
		assertEquals(8, image.getHeight());
		assertEquals(0, image.getRGB(0, 0) >>> 24);
		assertEquals(0xff0000ff, image.getRGB(15, 0));
	}

	public void testOtherDataIsReturnedUnchanged() throws IOException {
		byte[] png = { (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1a, '\n' };
		assertSame(png, ImageConversionUtil.convertUnsupportedFormat(png));
		byte[] corrupt = Arrays.copyOf(readWebp(), 20);
		assertSame(corrupt, ImageConversionUtil.convertUnsupportedFormat(corrupt));
	}
}
