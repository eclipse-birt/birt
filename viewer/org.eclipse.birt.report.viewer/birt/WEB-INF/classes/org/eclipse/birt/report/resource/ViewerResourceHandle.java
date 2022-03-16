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

package org.eclipse.birt.report.resource;

import java.util.Locale;

import org.eclipse.birt.core.i18n.ResourceHandle;

public class ViewerResourceHandle extends ResourceHandle {

	/**
	 * Constructs the resource handle with a specific resource bundle, which is
	 * associated with locale.
	 *
	 * @param locale the locale of <code>ULocale</code> type
	 */

	public ViewerResourceHandle(Locale locale) {
		super(locale);
	}

}
