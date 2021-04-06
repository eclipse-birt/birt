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

package org.eclipse.birt.report.designer.internal.ui.util;

import javax.swing.text.AttributeSet;

/**
 * 
 */

public interface RTFDocumentHandler {

	void startElement(String name, AttributeSet attributeSet);

	void endElement(String name);

	void content(String content);

}
