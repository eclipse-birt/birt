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
package org.eclipse.birt.report.model.plugin;

import org.eclipse.birt.report.model.extension.oda.IOdaExtensionLoader;
import org.eclipse.birt.report.model.extension.oda.IOdaExtensionLoaderFactory;

public class OdaBaseExtensionLoaderFactory implements IOdaExtensionLoaderFactory {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.oda.IOdaExtensionLoaderFactory#
	 * createOdaExtensionLoader()
	 */
	@Override
	public IOdaExtensionLoader createOdaExtensionLoader() {

		return new OdaExtensibilityExtensionLoader();
	}

}
