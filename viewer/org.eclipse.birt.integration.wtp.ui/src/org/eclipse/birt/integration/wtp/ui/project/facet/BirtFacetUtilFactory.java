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
package org.eclipse.birt.integration.wtp.ui.project.facet;

import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebAppVersionType;

public class BirtFacetUtilFactory {

	public static IBirtFacetUtil getInstance(Object webApp) {
		if (isWebApp25(webApp)) {
			return new BirtFacetUtil25();
		}
		return new BirtFacetUtil();
	}

	public static boolean isWebApp25(final Object webApp) {
		if (webApp instanceof WebApp && ((WebApp) webApp).getVersion() == WebAppVersionType._25_LITERAL) {
			return true;
		}
		return false;
	}

}
