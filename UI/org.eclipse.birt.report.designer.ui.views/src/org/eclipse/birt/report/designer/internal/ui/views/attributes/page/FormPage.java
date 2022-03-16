/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.util.SortMap;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FormPage extends AttributePage {

	private int style;
	protected IFormProvider provider;
	private boolean withDialog = false;
	private boolean isTabbed = false;
	private FormSection formSection;
	private Composite composite;

	public FormPage(int style, IFormProvider provider) {
		this.style = style;
		this.provider = provider;
	}

	public FormPage(int style, IFormProvider provider, boolean withDialog) {
		this.style = style;
		this.provider = provider;
		this.withDialog = withDialog;
	}

	public FormPage(int style, IFormProvider provider, boolean withDialog, boolean isTabbed) {
		this.style = style;
		this.provider = provider;
		this.withDialog = withDialog;
		this.isTabbed = isTabbed;
	}

	@Override
	public void buildUI(Composite parent) {
		container = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		((ScrolledComposite) container).setExpandHorizontal(true);
		((ScrolledComposite) container).setExpandVertical(true);
		container.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				computeSize();
			}
		});

		container.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				deRegisterEventManager();
			}
		});

		composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		if (sections == null) {
			sections = new SortMap();
		}
		composite.setLayout(WidgetUtil.createGridLayout(1));

		createFormSection();

		createSections();
		layoutSections();

		((ScrolledComposite) container).setContent(composite);

	}

	protected void createFormSection() {
		formSection = new FormSection(provider.getDisplayName(), composite, true, isTabbed);
		formSection.setProvider(provider);
		formSection.setButtonWithDialog(withDialog);
		formSection.setStyle(style);
		formSection.setHeight(160);
		formSection.setFillForm(true);
		addSection(PageSectionId.FORM_FORM, formSection);
	}

	private void computeSize() {
		Point size = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		((ScrolledComposite) container).setMinSize(size.x, size.y + 10);
		container.layout();

	}

	boolean needRebuild = false;

	@Override
	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {
		if (checkControl(formSection)) {
			if (provider.needRebuilded(ev)) {
				needRebuild = true;
				return;
			}
			formSection.getFormControl().addElementEvent(focus, ev);
		}
	}

	protected void rebuildUI() {
		Control[] children = composite.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
		sections.clear();
		createFormSection();
		createSections();
		layoutSections();
		composite.layout();
		refresh();
	}

	@Override
	public void clear() {
		needRebuild = false;
		if (checkControl(formSection)) {
			formSection.getFormControl().clear();
		}
	}

	private boolean checkControl(FormSection form) {
		return form != null && form.getFormControl() != null && !form.getFormControl().getControl().isDisposed();
	}

	@Override
	public void postElementEvent() {
		if (checkControl(formSection)) {
			if (needRebuild) {
				rebuildUI();
				needRebuild = false;
			}
			formSection.getFormControl().postElementEvent();
		}
	}

}
