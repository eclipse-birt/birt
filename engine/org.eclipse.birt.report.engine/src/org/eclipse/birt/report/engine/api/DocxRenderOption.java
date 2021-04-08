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

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.api.RenderOption;

public class DocxRenderOption extends RenderOption {

	public static final String OPTION_COMPRESSION_MODE = "BEST_COMPRESSION"; //$NON-NLS-1$

	public static final String OPTION_EMBED_HTML = "EmbedHtml";

	public static final String OPTION_WORD_VERSION = "WordVersion";

	@SuppressWarnings("unchecked")
	public void setCompressionMode( CompressionMode compressionMode )
	{
		options.put( OPTION_COMPRESSION_MODE, compressionMode );
	}

	public CompressionMode getCompressionMode() {
		Object mode = options.get(OPTION_COMPRESSION_MODE);
		if (mode instanceof CompressionMode) {
			return (CompressionMode) mode;
		}
		return CompressionMode.BEST_COMPRESSION;
	}
}
