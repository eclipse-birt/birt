/*******************************************************************************
 * Copyright (c) 2022 Henning von Bargen
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Henning von Bargen - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.qrcode.views;

import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * QRCodePageGeneratorFactory
 */
public class QRCodePageGeneratorFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof ExtendedItemHandle && IPageGenerator.class.isInstance(adapterType)) {
			return new QRCodePageGenerator();
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { IPageGenerator.class };
	}

}