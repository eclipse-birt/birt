/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
