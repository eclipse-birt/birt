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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ColorPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;

/**
 * The general attribute page of MasterPage element.
 */
public class MasterPageGeneralPage extends GeneralPage {

	private ComplexUnitSection heightSection;
	private ComboPropertyDescriptorProvider typeProvider;
	private ComplexUnitSection widthSection;
	private ComboSection orientationSection;

	protected void buildContent() {
		container.setLayout(WidgetUtil.createGridLayout(6, 15));

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(MasterPageHandle.NAME_PROP,
				ReportDesignConstants.MASTER_PAGE_ELEMENT);
		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setWidth(200);
		nameSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.MASTER_PAGE_NAME, nameSection);

		SeperatorSection seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.MASTER_PAGE_SEPERATOR, seperatorSection);

		UnitPropertyDescriptorProvider headHeightProvider = new UnitPropertyDescriptorProvider(
				SimpleMasterPageHandle.HEADER_HEIGHT_PROP, ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT);
		ComplexUnitSection headHeightSection = new ComplexUnitSection(headHeightProvider.getDisplayName(), container,
				true);
		headHeightSection.setProvider(headHeightProvider);
		headHeightSection.setWidth(200);
		headHeightSection.setLayoutNum(2);
		addSection(PageSectionId.MASTER_PAGE_HEAD_HEIGHT, headHeightSection);

		ColorPropertyDescriptorProvider colorProvider = new ColorPropertyDescriptorProvider(
				StyleHandle.BACKGROUND_COLOR_PROP, ReportDesignConstants.STYLE_ELEMENT);
		colorProvider.enableReset(true);
		ColorSection colorSection = new ColorSection(colorProvider.getDisplayName(), container, true);
		colorSection.setProvider(colorProvider);
		colorSection.setWidth(200);
		colorSection.setLayoutNum(4);
		colorSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.MASTER_PAGE_COLOR, colorSection);

		UnitPropertyDescriptorProvider footHeightProvider = new UnitPropertyDescriptorProvider(
				SimpleMasterPageHandle.FOOTER_HEIGHT_PROP, ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT);
		ComplexUnitSection footHeightSection = new ComplexUnitSection(footHeightProvider.getDisplayName(), container,
				true);
		footHeightSection.setProvider(footHeightProvider);
		footHeightSection.setWidth(200);
		footHeightSection.setLayoutNum(2);
		addSection(PageSectionId.MASTER_PAGE_FOOT_HEIGHT, footHeightSection);

		ComboPropertyDescriptorProvider orientationProvider = new ComboPropertyDescriptorProvider(
				MasterPageHandle.ORIENTATION_PROP, ReportDesignConstants.MASTER_PAGE_ELEMENT);
		orientationSection = new ComboSection(orientationProvider.getDisplayName(), container, true);
		orientationSection.setProvider(orientationProvider);
		orientationSection.setLayoutNum(4);
		orientationSection.setGridPlaceholder(2, true);
		orientationSection.setWidth(200);
		addSection(PageSectionId.MASTER_PAGE_ORIENTATION, orientationSection);

		SeperatorSection seperatorSection1 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.MASTER_PAGE_SEPERATOR_1, seperatorSection1);

		final UnitPropertyDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider(
				MasterPageHandle.WIDTH_PROP, ReportDesignConstants.MASTER_PAGE_ELEMENT);
		widthSection = new ComplexUnitSection(widthProvider.getDisplayName(), container, true);
		widthSection.setProvider(widthProvider);
		widthSection.setWidth(200);
		widthSection.setLayoutNum(2);
		addSection(PageSectionId.MASTER_PAGE_WIDTH, widthSection);

		final UnitPropertyDescriptorProvider heightProvider = new UnitPropertyDescriptorProvider(
				MasterPageHandle.HEIGHT_PROP, ReportDesignConstants.MASTER_PAGE_ELEMENT);

		typeProvider = new ComboPropertyDescriptorProvider(MasterPageHandle.TYPE_PROP,
				ReportDesignConstants.MASTER_PAGE_ELEMENT) {

			public void save(Object value) throws SemanticException {
				if (DesignChoiceConstants.PAGE_SIZE_CUSTOM.equals(getSaveValue(value))) {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(Messages.getString("MasterPageGeneralPage.Trans.SetType")); //$NON-NLS-1$
					Object width = widthProvider.load();
					Object height = heightProvider.load();
					super.save(value);
					widthProvider.save(width);
					heightProvider.save(height);
					stack.commit();
				} else {
					super.save(value);
				}
			}
		};
		ComboSection typeSection = new ComboSection(typeProvider.getDisplayName(), container, true);
		typeSection.setProvider(typeProvider);
		typeSection.setGridPlaceholder(2, true);
		typeSection.setLayoutNum(4);
		typeSection.setWidth(200);
		addSection(PageSectionId.MASTER_PAGE_TYPE, typeSection);

		heightSection = new ComplexUnitSection(heightProvider.getDisplayName(), container, true);
		heightSection.setProvider(heightProvider);
		heightSection.setWidth(200);
		heightSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.MASTER_PAGE_HEIGHT, heightSection);

		// WidgetUtil.buildGridControl( container, propertiesMap,
		// ReportDesignConstants.MASTER_PAGE_ELEMENT,
		// MasterPageHandle.NAME_PROP, 1, false );

		// WidgetUtil.buildGridControl( container, propertiesMap,
		// ReportDesignConstants.STYLE_ELEMENT,
		// StyleHandle.BACKGROUND_COLOR_PROP, 1, false );
		//
		// WidgetUtil.createGridPlaceholder( container, 1, true );

		/*
		 * WidgetUtil.buildGridControl( container, propertiesMap,
		 * ReportDesignConstants.MASTER_PAGE_ELEMENT, MasterPageHandle.ORIENTATION_PROP,
		 * 1, false );
		 * 
		 * Label separator = new Label( container, SWT.SEPARATOR | SWT.HORIZONTAL );
		 * GridData data = new GridData( ); data.horizontalSpan = 5;
		 * data.grabExcessHorizontalSpace = false; data.horizontalAlignment =
		 * GridData.FILL; separator.setLayoutData( data );
		 * 
		 * WidgetUtil.buildGridControl( container, propertiesMap,
		 * ReportDesignConstants.MASTER_PAGE_ELEMENT, MasterPageHandle.TYPE_PROP, 1,
		 * false ); pageSizeDescriptor = (IPropertyDescriptor) propertiesMap.get(
		 * MasterPageHandle.TYPE_PROP );
		 * 
		 * WidgetUtil.createGridPlaceholder( container, 3, false );
		 * 
		 * widthPane = (Composite) WidgetUtil.buildGridControl( container,
		 * propertiesMap, ReportDesignConstants.MASTER_PAGE_ELEMENT,
		 * MasterPageHandle.WIDTH_PROP, 1, false );
		 * 
		 * heightPane = (Composite) WidgetUtil.buildGridControl( container,
		 * propertiesMap, ReportDesignConstants.MASTER_PAGE_ELEMENT,
		 * MasterPageHandle.HEIGHT_PROP, 1, false );
		 */
	}

	public void refresh() {
		super.refresh();
		resetCustomStyle();
	}

	private boolean checkControl() {
		return widthSection != null && widthSection.getUnitComboControl() != null
				&& !widthSection.getUnitComboControl().getControl().isDisposed();
	}

	private void resetCustomStyle() {
		if (checkControl()) {
			if (!typeProvider.load().equals(DesignChoiceConstants.PAGE_SIZE_CUSTOM)) {
				widthSection.getUnitComboControl().setReadOnly(true);
				heightSection.getUnitComboControl().setReadOnly(true);
				orientationSection.getComboControl().getControl().setEnabled(true);
			} else {
				widthSection.getUnitComboControl().setReadOnly(false);
				heightSection.getUnitComboControl().setReadOnly(false);
				orientationSection.getComboControl().getControl().setEnabled(false);
			}
		}
	}

	public void postElementEvent() {
		super.postElementEvent();
		resetCustomStyle();
	}

}