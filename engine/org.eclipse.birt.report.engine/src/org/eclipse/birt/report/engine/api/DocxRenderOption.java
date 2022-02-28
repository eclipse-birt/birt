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

package org.eclipse.birt.report.engine.api;

public class DocxRenderOption extends RenderOption {

	public static final String OPTION_COMPRESSION_MODE = "BEST_COMPRESSION"; //$NON-NLS-1$

	public static final String OPTION_EMBED_HTML = "EmbedHtml";

	public static final String OPTION_WORD_VERSION = "WordVersion";

	@SuppressWarnings("unchecked")
	public void setCompressionMode(CompressionMode compressionMode) {
		options.put(OPTION_COMPRESSION_MODE, compressionMode);
	}

	public CompressionMode getCompressionMode() {
		Object mode = options.get(OPTION_COMPRESSION_MODE);
		if (mode instanceof CompressionMode) {
			return (CompressionMode) mode;
		}
		return CompressionMode.BEST_COMPRESSION;
	}
}
