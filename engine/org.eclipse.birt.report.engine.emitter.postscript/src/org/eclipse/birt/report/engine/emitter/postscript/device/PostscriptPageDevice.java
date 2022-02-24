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

package org.eclipse.birt.report.engine.emitter.postscript.device;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.engine.api.IPostscriptRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.emitter.postscript.PostscriptRenderOption;
import org.eclipse.birt.report.engine.emitter.postscript.PostscriptWriter;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;

/**
 * Represents a postscript device, which will manage postscript pages.
 */
public class PostscriptPageDevice implements IPageDevice {

	private PostscriptWriter writer;
	private PostscriptPage currentPage;
	private String orientation;

	public PostscriptPageDevice(RenderOption renderOption, OutputStream output, String title, String author,
			String description) throws Exception {
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(output);
		writer = new PostscriptWriter(bufferedOutputStream, title);

		String paperSize = renderOption.getStringOption(PostscriptRenderOption.OPTION_PAPER_SIZE);
		String paperTray = renderOption.getStringOption(PostscriptRenderOption.OPTION_PAPER_TRAY);
		Object duplex = renderOption.getOption(PostscriptRenderOption.OPTION_DUPLEX);
		int copies = renderOption.getIntOption(PostscriptRenderOption.OPTION_COPIES, 1);
		boolean collate = renderOption.getBooleanOption(PostscriptRenderOption.OPTION_COLLATE, false);
		String resolution = renderOption.getStringOption(IPostscriptRenderOption.OPTION_RESOLUTION);
		boolean color = renderOption.getBooleanOption(IPostscriptRenderOption.OPTION_COLOR, true);
		int scale = renderOption.getIntOption(IPostscriptRenderOption.OPTION_SCALE, 100);
		boolean autoPaperSizeSelection = renderOption
				.getBooleanOption(IPostscriptRenderOption.OPTION_AUTO_PAPER_SIZE_SELECTION, false);
		boolean fitToPaper = renderOption.getBooleanOption(IPostscriptRenderOption.OPTION_FIT_TO_PAPER, false);
		writer.startRenderer(author, description, paperSize, paperTray, duplex, copies, collate, resolution, color,
				scale, autoPaperSizeSelection, fitToPaper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.postscript.page.IPagableDevice#close()
	 */
	public void close() throws IOException {
		writer.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.postscript.page.IPagableDevice#newPage
	 * (float, float)
	 */
	public IPage newPage(int width, int height, Color backgroundColor) {
		if (currentPage != null) {
			currentPage.dispose();
		}
		currentPage = new PostscriptPage(width, height, backgroundColor, writer, orientation);
		return currentPage;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
}
