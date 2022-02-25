/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;

public class FontSizePropertyDescriptorProvider extends PropertyDescriptorProvider {

	public FontSizePropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	public String getDefaultUnit() {

		String unit = null;
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement(input);
		if (handle != null) {
			unit = handle.getPropertyHandle(getProperty()).getDefaultUnit();
		}
		return unit;
	}

}
