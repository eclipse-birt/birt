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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

public class ColorPropertyDescriptorProvider extends PropertyDescriptorProvider {

	public ColorPropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	public IChoiceSet getElementChoiceSet() {
		return ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty());
	}

}
