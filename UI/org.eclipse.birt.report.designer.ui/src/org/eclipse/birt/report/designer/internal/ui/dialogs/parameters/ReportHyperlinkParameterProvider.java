/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.parameters;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * ReportHyperlinkParameterProvider
 */
public class ReportHyperlinkParameterProvider implements IHyperlinkParameterProvider {

	// TODO localize
	public static final String CATEGORY_REPORT = Messages.getString("ReportHyperlinkParameterProvider.Category.Report"); //$NON-NLS-1$

	private static final String[] CATS = new String[] { CATEGORY_REPORT };

	private ReportDesignHandle design;

	ReportHyperlinkParameterProvider(ReportDesignHandle design) {
		this.design = design;
	}

	public String[] getCategories() {
		return CATS;
	}

	public IHyperlinkParameter[] getParameters(String category, String format) {
		if (design != null && CATEGORY_REPORT.equals(category)) {
			ArrayList<IHyperlinkParameter> params = new ArrayList<IHyperlinkParameter>();

			for (Iterator iter = design.getAllParameters().iterator(); iter.hasNext();) {
				Object obj = iter.next();

				if (obj instanceof ParameterHandle) {
					params.add(new ReportHyperlinkParameter((ParameterHandle) obj));
				}
			}

			if (params.size() > 0) {
				return params.toArray(new IHyperlinkParameter[params.size()]);
			}
		}

		return null;
	}

}
