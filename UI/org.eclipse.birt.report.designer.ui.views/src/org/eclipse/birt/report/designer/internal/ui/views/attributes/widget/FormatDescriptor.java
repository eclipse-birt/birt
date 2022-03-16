/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

	@Override
	public void addFormatChangeListener(IFormatChangeListener listener) {
		layoutPeer.addFormatChangeListener(listener);
	}

	@Override
	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);

		if (provider instanceof FormatDescriptorProvider) {
			this.provider = (FormatDescriptorProvider) provider;
		}
	}

	@Override
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

	@Override
	public void save(Object obj) throws SemanticException {
		provider.save(obj);
	}

	@Override
	public String getCategory() {
		return layoutPeer.getCategory();
	}

	@Override
	public String getFormatString() {
		return layoutPeer.getFormatString();
	}

	@Override
	public ULocale getLocale() {
		return layoutPeer.getLocale();
	}

	@Override
	public String getPattern() {
		return layoutPeer.getPattern();
	}

	@Override
	public boolean isDirty() {
		return layoutPeer.isDirty();
	}

	@Override
	public boolean isFormatModified() {
		return layoutPeer.isFormatModified();
	}

	@Override
	public void setInput(String category, String pattern, ULocale formatLocale) {
		layoutPeer.setInput(category, pattern, formatLocale);
	}

	@Override
	public void setInput(Object object) {
		super.setInput(object);
		getDescriptorProvider().setInput(object);
	}

	@Override
	public void setInput(String formatString) {
		layoutPeer.setInput(formatString);
	}

	@Override
	public void setPreviewText(String text) {
		layoutPeer.setPreviewText(text);
	}

	@Override
	public Control createControl(Composite parent) {
		return layoutPeer.createLayout(parent);
	}

	@Override
	public Control getControl() {
		return layoutPeer.getControl();
	}
}
