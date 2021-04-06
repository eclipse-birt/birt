/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * The i18n page for Label
 */

public class LabelI18nPage extends I18nPage {

	public void buildUI(Composite parent) {
		elementName = ReportDesignConstants.LABEL_ITEM;
		propertyName = LabelHandle.TEXT_ID_PROP;
		super.buildUI(parent);
	}
}