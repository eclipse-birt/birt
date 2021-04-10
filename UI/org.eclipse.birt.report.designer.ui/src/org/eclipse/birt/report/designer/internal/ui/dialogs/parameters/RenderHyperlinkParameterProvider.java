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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * RenderHyperlinkParameterProvider
 */
public class RenderHyperlinkParameterProvider implements IHyperlinkParameterProvider {

	// TODO localize
	public static final String CATEGORY_RENDER = Messages.getString("RenderHyperlinkParameterProvider.Category.Render"); //$NON-NLS-1$

	private static final String[] CATS = new String[] { CATEGORY_RENDER };

	private IHyperlinkParameter htmlPaginationParameter = new SimpleHyperlinkParameter("__" //$NON-NLS-1$
			+ IRenderOption.HTML_PAGINATION, DesignChoiceConstants.PARAM_TYPE_BOOLEAN);

	public String[] getCategories() {
		return CATS;
	}

	public IHyperlinkParameter[] getParameters(String category, String format) {
		if (CATEGORY_RENDER.equals(category)) {
			return new IHyperlinkParameter[] { htmlPaginationParameter };
		}
		return null;
	}

}
