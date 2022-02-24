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

package org.eclipse.birt.report.engine.emitter.postscript;

import org.eclipse.birt.report.engine.api.IPostscriptRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

public class PostscriptRenderOption extends RenderOption implements IPostscriptRenderOption {

	/**
	 * Sets postscript level.
	 * 
	 * @param level
	 */
	public void setPostscriptLevel(int level) {
		options.put(PS_LEVEL, level);
	}

	/**
	 * Gets postscript level.
	 */
	public int getPostscriptLevel() {
		return getIntOption(PS_LEVEL, 1);
	}

	public void setPaperSize(String paperSize) {
		options.put(OPTION_PAPER_SIZE, paperSize);
	}

	public String getPaperSize() {
		return getStringOption(OPTION_PAPER_SIZE);
	}

	public void setPaperTray(String paperTray) {
		options.put(OPTION_PAPER_TRAY, paperTray);
	}

	public String getPaperTray() {
		return getStringOption(OPTION_PAPER_TRAY);
	}

	public void setDuplex(int duplex) {
		options.put(OPTION_DUPLEX, duplex);
	}

	public int getDuplex() {
		return getIntOption(OPTION_DUPLEX, DUPLEX_SIMPLEX);
	}

	public void setCopies(int copies) {
		options.put(OPTION_COPIES, copies);
	}

	public int getCopies() {
		return getIntOption(OPTION_COPIES, 1);
	}

	public void setCollate(boolean collate) {
		options.put(OPTION_COLLATE, collate);
	}

	public boolean getCollate() {
		return getBooleanOption(OPTION_COLLATE, false);
	}

	public void setResolution(String resolution) {
		options.put(OPTION_RESOLUTION, resolution);
	}

	public String getResolution() {
		return getStringOption(OPTION_RESOLUTION);
	}

	public void setColor(boolean color) {
		options.put(OPTION_COLOR, color);
	}

	public boolean getColor() {
		return getBooleanOption(OPTION_COLOR, true);
	}

	public void setScale(int scale) {
		options.put(OPTION_SCALE, scale);
	}

	public int getScale() {
		return getIntOption(OPTION_SCALE, 100);
	}

	public void setAutoPaperSizeSelection(boolean autoPaperSizeSelection) {
		options.put(OPTION_AUTO_PAPER_SIZE_SELECTION, autoPaperSizeSelection);
	}

	public boolean getAutoPaperSizeSelection() {
		return getBooleanOption(OPTION_AUTO_PAPER_SIZE_SELECTION, false);
	}

	public void setFitToPaper(boolean fitToPaper) {
		options.put(OPTION_FIT_TO_PAPER, fitToPaper);
	}

	public boolean getFitToPaper() {
		return getBooleanOption(OPTION_FIT_TO_PAPER, true);
	}
}
