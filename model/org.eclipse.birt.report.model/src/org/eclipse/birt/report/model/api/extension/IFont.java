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

package org.eclipse.birt.report.model.api.extension;

public interface IFont {

	boolean isBold();

	void setBold(boolean isBold);

	boolean isItalic();

	void setItalic(boolean isItalic);

	boolean isUnderline();

	void setUnderline(boolean isUnderline);

	boolean isStrikeThrough();

	void setStrikeThrough(boolean isStrikeThrough);

	String getName();

	void setName(String name);

	float getSize();

	void setSize(float size);
}
