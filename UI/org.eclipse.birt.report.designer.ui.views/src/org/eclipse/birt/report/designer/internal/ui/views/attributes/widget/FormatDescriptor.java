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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatLayoutPeer;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FormatDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ibm.icu.util.ULocale;

/**
 * FormatDescriptor
 */
public abstract class FormatDescriptor extends PropertyDescriptor implements IFormatPage {

	protected FormatLayoutPeer layoutPeer;

	private FormatDescriptorProvider provider;

	public void addFormatChangeListener(IFormatChangeListener listener) {
		layoutPeer.addFormatChangeListener(listener);
	}

	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);

		if (provider instanceof FormatDescriptorProvider) {
			this.provider = (FormatDescriptorProvider) provider;
		}
	}

	public void load() {
		layoutPeer.setEnabled(true);
		String[] result = (String[]) provider.load();

		if (result == null || result.length == 0) {
			layoutPeer.setEnabled(false);
		} else if (result.length == 1) {
			setInput(result[0]);
		} else if (result.length == 2) {
			layoutPeer.setInput(result[0], result[1]);
		} else if (result.length == 3) {
			setInput(result[0], result[1], FormatAdapter.getLocaleByDisplayName(result[2]));
		}
	}

	public void save(Object obj) throws SemanticException {
		provider.save(obj);
	}

	public String getCategory() {
		return layoutPeer.getCategory();
	}

	public String getFormatString() {
		return layoutPeer.getFormatString();
	}

	public ULocale getLocale() {
		return layoutPeer.getLocale();
	}

	public String getPattern() {
		return layoutPeer.getPattern();
	}

	public boolean isDirty() {
		return layoutPeer.isDirty();
	}

	public boolean isFormatModified() {
		return layoutPeer.isFormatModified();
	}

	public void setInput(String category, String pattern, ULocale formatLocale) {
		layoutPeer.setInput(category, pattern, formatLocale);
	}

	@Override
	public void setInput(Object object) {
		super.setInput(object);
		getDescriptorProvider().setInput(object);
	}

	public void setInput(String formatString) {
		layoutPeer.setInput(formatString);
	}

	public void setPreviewText(String text) {
		layoutPeer.setPreviewText(text);
	}

	public Control createControl(Composite parent) {
		return layoutPeer.createLayout(parent);
	}

	@Override
	public Control getControl() {
		return layoutPeer.getControl();
	}
}
