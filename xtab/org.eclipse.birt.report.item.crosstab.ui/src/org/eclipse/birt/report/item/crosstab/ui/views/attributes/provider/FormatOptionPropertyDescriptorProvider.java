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
/**
 *
 */

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractDescriptorProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * @author Administrator
 *
 */
public class FormatOptionPropertyDescriptorProvider extends AbstractDescriptorProvider {

	protected Object input;

	protected ExtendedItemHandle handle;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#load()
	 */
	@Override
	public Object load() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#save(java.lang.Object)
	 */
	@Override
	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		this.input = input;
		handle = getFirstElementHandle();
	}

	private ExtendedItemHandle getFirstElementHandle() {
		Object obj = DEUtil.getInputFirstElement(input);
		if (obj instanceof ExtendedItemHandle) {
			return (ExtendedItemHandle) obj;
		} else {
			return null;
		}
	}

}
