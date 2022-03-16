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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BindingGroupSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SortingFormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DataSetColumnBindingsFormDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
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

/**
 * The Binding attribute page of DE element. Note: Binding Not support
 * multi-selection.
 */
public class BindingPage extends AttributePage {
	// private ComboAndButtonSection dataSetSection;

	private DataSetColumnBindingsFormHandleProvider dataSetFormProvider;

	private SortingFormSection dataSetFormSection;

	private Composite composite;

	private boolean dataSetSectionVisible = true;

	private BindingGroupDescriptorProvider bindingProvider;

	public void setDataSetSectionVisible(boolean bool) {
		dataSetSectionVisible = bool;
	}

	protected Composite getSectionContainer() {
		return composite;
	}

	@Override
	public void buildUI(Composite parent) {
		container = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
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

		composite.setLayout(WidgetUtil.createGridLayout(6));

		if (dataSetSectionVisible) {
			BindingGroupSection bindingGroupSection = new BindingGroupSection(composite, true);
			bindingProvider = new BindingGroupDescriptorProvider();
			bindingProvider.setRefrenceSection(bindingGroupSection);
			bindingGroupSection.setProvider(bindingProvider);
			bindingGroupSection.setGridPlaceholder(2, true);
//			bindingGroupSection.setWidth( 550 );
			addSection(PageSectionId.BINDING_GROUP, bindingGroupSection);
		}

		dataSetFormProvider = new DataSetColumnBindingsFormHandleProvider();
		dataSetFormSection = new SortingFormSection(dataSetFormProvider.getDisplayName(), composite, true);
		dataSetFormSection.setCustomForm(new DataSetColumnBindingsFormDescriptor(true));
		dataSetFormSection.setProvider(dataSetFormProvider);
		dataSetFormSection.showDisplayLabel(true);
		dataSetFormSection.setButtonWithDialog(true);
		dataSetFormSection.setStyle(FormPropertyDescriptor.FULL_FUNCTION);
		dataSetFormSection.setHeight(0);
		dataSetFormSection.setFillForm(true);
		dataSetFormSection.setGridPlaceholder(1, true);
		addSection(PageSectionId.BINDING_DATASET_FORM, dataSetFormSection);

		if (bindingProvider != null) {
			bindingProvider.setDependedProvider(dataSetFormProvider);
		}

		createSections();
		layoutSections();

		((ScrolledComposite) container).setContent(composite);
	}

	private void computeSize() {
		Point size = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		((ScrolledComposite) container).setMinSize(size.x, size.y + 10);
		container.layout();

	}

	@Override
	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {
		if (checkControl(dataSetFormSection)) {
			dataSetFormSection.getFormControl().addElementEvent(focus, ev);
		}
	}

	@Override
	public void clear() {
		if (checkControl(dataSetFormSection)) {
			dataSetFormSection.getFormControl().clear();
		}
	}

	@Override
	public void postElementEvent() {

		if (checkControl(dataSetFormSection)) {
			dataSetFormSection.getFormControl().postElementEvent();
		}

	}

	@Override
	public void setInput(Object input) {
		super.setInput(input);
	}

	private boolean checkControl(SortingFormSection form) {
		return form != null && form.getFormControl() != null && !form.getFormControl().getControl().isDisposed();
	}

}
