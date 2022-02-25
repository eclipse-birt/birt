/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.image;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;

/**
 * GifRendererImpl
 *
 * @deprecated GIF rendering is deprecated, use PNG renderer instead
 * @see PngRendererImpl
 */
@Deprecated
public final class GifRendererImpl extends PngRendererImpl {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/image"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
	 */
	@Override
	public void after() throws ChartException {
		logger.log(ILogger.WARNING,
				"Chart GIF device renderer is deprecated, reverting to PNG renderer. Please update your code to use the PNG device renderer instead." //$NON-NLS-1$
		);
		super.after();

	}

}
