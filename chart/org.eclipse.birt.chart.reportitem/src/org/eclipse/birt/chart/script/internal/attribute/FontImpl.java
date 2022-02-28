/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.script.internal.attribute;

import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.report.model.api.extension.IFont;

/**
 *
 */

public class FontImpl implements IFont {

	private FontDefinition fd = null;

	public FontImpl(FontDefinition fd) {
		this.fd = fd;
	}

	@Override
	public String getName() {
		return fd.getName();
	}

	@Override
	public float getSize() {
		return fd.getSize();
	}

	@Override
	public boolean isBold() {
		return fd.isBold();
	}

	@Override
	public boolean isItalic() {
		return fd.isItalic();
	}

	@Override
	public boolean isStrikeThrough() {
		return fd.isStrikethrough();
	}

	@Override
	public boolean isUnderline() {
		return fd.isUnderline();
	}

	@Override
	public void setBold(boolean isBold) {
		fd.setBold(isBold);
	}

	@Override
	public void setItalic(boolean isItalic) {
		fd.setItalic(isItalic);
	}

	@Override
	public void setStrikeThrough(boolean isStrikeThrough) {
		fd.setStrikethrough(isStrikeThrough);
	}

	@Override
	public void setUnderline(boolean isUnderline) {
		fd.setUnderline(isUnderline);
	}

	@Override
	public void setName(String name) {
		fd.setName(name);
	}

	@Override
	public void setSize(float size) {
		fd.setSize(size);
	}

}
