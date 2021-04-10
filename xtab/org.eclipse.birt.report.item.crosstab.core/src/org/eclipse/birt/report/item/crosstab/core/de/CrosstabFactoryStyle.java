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

package org.eclipse.birt.report.item.crosstab.core.de;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.util.ColorUtil;

/**
 * CrosstabFactoryStyle
 */
class CrosstabFactoryStyle implements IStyleDeclaration {

	private String name;

	CrosstabFactoryStyle(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getProperty(String name) {
		if (BORDER_BOTTOM_STYLE_PROP.equals(name) || BORDER_TOP_STYLE_PROP.equals(name)
				|| BORDER_LEFT_STYLE_PROP.equals(name) || BORDER_RIGHT_STYLE_PROP.equals(name)) {
			return DesignChoiceConstants.LINE_STYLE_SOLID;
		}

		if (BORDER_BOTTOM_WIDTH_PROP.equals(name) || BORDER_TOP_WIDTH_PROP.equals(name)
				|| BORDER_LEFT_WIDTH_PROP.equals(name) || BORDER_RIGHT_WIDTH_PROP.equals(name)) {
			return "1pt"; //$NON-NLS-1$
		}

		if (BORDER_BOTTOM_COLOR_PROP.equals(name) || BORDER_TOP_COLOR_PROP.equals(name)
				|| BORDER_LEFT_COLOR_PROP.equals(name) || BORDER_RIGHT_COLOR_PROP.equals(name)) {
			return Integer.valueOf(ColorUtil.formRGB(0xcc, 0xcc, 0xcc));
		}
		return null;
	}
}
