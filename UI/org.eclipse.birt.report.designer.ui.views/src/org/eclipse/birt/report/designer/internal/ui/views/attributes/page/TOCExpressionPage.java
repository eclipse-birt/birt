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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TocExpressionPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TocStylePropertyDescriptiorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ExpressionSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TocSimpleComboSection;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * TOC expresion page.
 * 
 */
public class TOCExpressionPage extends AttributePage {

	TocSimpleComboSection styleSection;
	ExpressionSection tocSection;

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(2, 15));

		TocExpressionPropertyDescriptorProvider tocProvider = new TocExpressionPropertyDescriptorProvider(
				IReportItemModel.TOC_PROP, ReportDesignConstants.REPORT_ITEM);
		tocSection = new ExpressionSection(tocProvider.getDisplayName(), container, true);
		tocSection.setProvider(tocProvider);
		tocSection.setWidth(500);
		addSection(PageSectionId.TOC_EXPRESSION_TOC, tocSection);

		TocStylePropertyDescriptiorProvider styleProvider = new TocStylePropertyDescriptiorProvider(
				IReportItemModel.TOC_PROP, ReportDesignConstants.REPORT_ITEM);
		styleSection = new TocSimpleComboSection(styleProvider.getDisplayName(), container, true);
		styleSection.setProvider(styleProvider);
		styleSection.setWidth(200);
		addSection(PageSectionId.TOC_EXPRESSION_TOC_STYLE, styleSection);

		createSections();
		layoutSections();

		final Text text = tocSection.getExpressionControl().getTextControl();
		text.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (text.getText().length() == 0) {
					styleSection.getSimpleComboControl().getControl().setEnabled(false);
				} else {
					styleSection.getSimpleComboControl().getControl().setEnabled(true);
				}
			}

		});
	}
	/*
	 * protected void refreshValues( Set propertiesSet ) { if ( DEUtil.getInputSize(
	 * input) > 0 ) { ReportElementHandle handle = (ReportElementHandle)
	 * DEUtil.getInputFirstElement(input ); GroupPropertyHandle propertyHandle =
	 * GroupElementFactory.newGroupElement( handle.getModuleHandle( ),
	 * DEUtil.getInputElements(input )) .getPropertyHandle(
	 * IReportItemModel.TOC_PROP ); Control[] children = tocArea.getChildren( ); for
	 * ( int i = 0; i < children.length; i++ ) { children[i].setEnabled(
	 * !propertyHandle.isReadOnly( ) ); } } super.refresh( ); }
	 */

	public void refresh() {
		super.refresh();
		checkTocStyleEnable();
	}

	private void checkTocStyleEnable() {
		Text text = tocSection.getExpressionControl().getTextControl();
		if (text.getText().length() == 0) {
			styleSection.getSimpleComboControl().getControl().setEnabled(false);
		} else {
			styleSection.getSimpleComboControl().getControl().setEnabled(true);
		}
	}
}
