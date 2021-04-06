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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;

/**
 * @author Administrator
 * 
 */
public class TocStylePropertyDescriptiorProvider extends SimpleComboPropertyDescriptorProvider {

	protected TOCHandle tocHandle;

	public TocStylePropertyDescriptiorProvider(String property, String element) {
		super(property, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IDescriptorProvider#getDisplayName()
	 */
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return Messages.getString("Element.Toc.Sytle"); //$NON-NLS-1$
	}

	public TOCHandle getTocHandle() {
		return tocHandle;
	}

	public String[] getItems() {
		String[] items = null;

		items = ChoiceSetFactory.getStyles();
		items = filterPreStyles(items);

		return items;
	}

	private String[] filterPreStyles(String items[]) {
		List preStyles = DesignEngine.getMetaDataDictionary().getPredefinedStyles();
		List preStyleNames = new ArrayList();

		for (int i = 0; i < preStyles.size(); i++) {
			preStyleNames.add(((IPredefinedStyle) preStyles.get(i)).getName());
		}

		List sytleNames = new ArrayList();
		for (int i = 0; i < items.length; i++) {
			if (preStyleNames.indexOf(items[i]) == -1) {
				sytleNames.add(items[i]);
			}
		}

		return (String[]) (sytleNames.toArray(new String[] {}));

	}

	public Object load() {
		String value = null;
		if (input instanceof ReportItemHandle) {
			tocHandle = ((ReportItemHandle) input).getTOC();
		} else if (input instanceof List) {
			tocHandle = ((ReportItemHandle) DEUtil.getGroupElementHandle((List) input).getElements().get(0)).getTOC();
		}

		if (tocHandle != null) {
			value = tocHandle.getStyleName();
		}

		return value == null ? "" : value; //$NON-NLS-1$
	}

	public void save(Object value) throws SemanticException {
		if (tocHandle == null) {
			if (input instanceof ReportItemHandle) {
				tocHandle = ((ReportItemHandle) input).getTOC();
			} else if (input instanceof List) {
				tocHandle = ((ReportItemHandle) DEUtil.getGroupElementHandle((List) input).getElements().get(0))
						.getTOC();
			}
		}

		if (tocHandle != null) {
			tocHandle.setStyleName((String) value);
		}

	}

	public void setInput(Object input) {
		super.setInput(input);
		if (tocHandle == null) {
			if (input instanceof ReportItemHandle) {
				tocHandle = ((ReportItemHandle) input).getTOC();
			} else if (input instanceof List) {
				tocHandle = ((ReportItemHandle) DEUtil.getGroupElementHandle((List) input).getElements().get(0))
						.getTOC();
			}
		}
	}

	public boolean isSpecialProperty() {
		return true;
	}

}
