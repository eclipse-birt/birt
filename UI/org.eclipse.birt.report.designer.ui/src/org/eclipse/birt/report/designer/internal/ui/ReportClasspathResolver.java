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

package org.eclipse.birt.report.designer.internal.ui;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.preferences.ClassPathBlock;
import org.eclipse.birt.report.designer.ui.IReportClasspathResolver;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;

/**
 * ReportClasspathResolver
 */
public class ReportClasspathResolver implements IReportClasspathResolver {

	public String[] resolveClasspath(Object adaptable) {
		String value = PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault())
				.getString(ReportPlugin.CLASSPATH_PREFERENCE);

		List<String> strs = ClassPathBlock.getEntries(value);

		return strs.toArray(new String[strs.size()]);
	}

}
