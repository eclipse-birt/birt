/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area;

import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

public interface ITextArea extends IArea {

	String getText();

	String getLogicalOrderText();

	TextStyle getTextStyle();

	boolean needClip();
}
