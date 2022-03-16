/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

	private static final String[] CATS = { CATEGORY_RENDER };

	private IHyperlinkParameter htmlPaginationParameter = new SimpleHyperlinkParameter("__" //$NON-NLS-1$
			+ IRenderOption.HTML_PAGINATION, DesignChoiceConstants.PARAM_TYPE_BOOLEAN);

	@Override
	public String[] getCategories() {
		return CATS;
	}

	@Override
	public IHyperlinkParameter[] getParameters(String category, String format) {
		if (CATEGORY_RENDER.equals(category)) {
			return new IHyperlinkParameter[] { htmlPaginationParameter };
		}
		return null;
	}

}
