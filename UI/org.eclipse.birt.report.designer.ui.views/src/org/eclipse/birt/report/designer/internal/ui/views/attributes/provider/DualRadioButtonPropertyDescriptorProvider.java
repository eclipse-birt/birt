/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.Arrays;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

/**
 *
 */

public class DualRadioButtonPropertyDescriptorProvider extends PropertyDescriptorProvider {

	public DualRadioButtonPropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	private IChoiceSet choiceSet;
	private String[] items;

	public String[] getItems() {
		if (choiceSet == null) {
			choiceSet = ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty());
			String[] names = ChoiceSetFactory.getNamefromChoiceSet(choiceSet);
			Arrays.sort(names, new AlphabeticallyComparator());
			this.items = names;
		}
		return this.items;
	}

}
