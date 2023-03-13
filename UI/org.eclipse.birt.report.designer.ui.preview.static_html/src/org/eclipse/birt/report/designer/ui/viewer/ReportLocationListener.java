/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.viewer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.widgets.Display;

import com.ibm.icu.util.StringTokenizer;

/**
 * Listener for static html viewer to deal with hyperlink in report output
 */
public class ReportLocationListener implements LocationListener {

	private StaticHTMLViewer viewer;

	public ReportLocationListener(StaticHTMLViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void changed(LocationEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changing(LocationEvent event) {
		if (event.location.startsWith("birt://")) //$NON-NLS-1$
		{
			try {
				String urlstr = URLDecoder.decode(event.location, "UTF-8"); //$NON-NLS-1$
				urlstr = urlstr.substring(urlstr.indexOf("?") + 1); //$NON-NLS-1$
				StringTokenizer st = new StringTokenizer(urlstr, "&"); //$NON-NLS-1$

				final Map options = new HashMap();
				while (st.hasMoreTokens()) {
					String option = st.nextToken();
					int index = option.indexOf("="); //$NON-NLS-1$
					if (index > 0) {
						options.put(option.substring(0, index),
								URLDecoder.decode(option.substring(index + 1), "UTF-8")); //$NON-NLS-1$
					} else {
						options.put(option, ""); //$NON-NLS-1$
					}
				}
				event.doit = false;
				Display.getCurrent().asyncExec(new Runnable() {

					@Override
					public void run() {
						viewer.setReportDesignFile((String) options.get("__report")); //$NON-NLS-1$
						viewer.setParamValues(options);
						viewer.setCurrentPage(1);
						viewer.render();
					}
				});
			} catch (UnsupportedEncodingException e) {
			}
		}

	}
}
