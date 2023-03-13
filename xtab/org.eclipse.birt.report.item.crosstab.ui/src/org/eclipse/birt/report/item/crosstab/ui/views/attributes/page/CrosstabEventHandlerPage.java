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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.page;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.ide.util.ClassFinder;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndButtonSection;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Administrator
 *
 */
public class CrosstabEventHandlerPage extends AttributePage {

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		TextPropertyDescriptorProvider eventProvider = new TextPropertyDescriptorProvider(
				ReportDesignHandle.EVENT_HANDLER_CLASS_PROP, ReportDesignConstants.REPORT_DESIGN_ELEMENT);
		TextAndButtonSection eventSection = new TextAndButtonSection(eventProvider.getDisplayName(), container, true);
		eventSection.setProvider(eventProvider);
		eventSection.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ClassFinder finder = new ClassFinder();
				String className = null;
				if (input != null && ((List) input).size() > 0) {
					if (((List) input).get(0) instanceof DesignElementHandle) {
						className = getEventHandlerClassName((DesignElementHandle) ((List) input).get(0));

					}
				}
				if (className != null) {
					finder.setParentClassName(className);
					GroupPropertyHandle handle = DEUtil.getMultiSelectionHandle((List) input)
							.getPropertyHandle(ReportDesignHandle.EVENT_HANDLER_CLASS_PROP);
					try {
						String finderClassName = finder.getFinderClassName();
						if (finderClassName != null && finderClassName.trim().length() > 0) {
							handle.setStringValue(finderClassName.trim());
						}
					} catch (SemanticException e1) {
						ExceptionUtil.handle(e1);
					}
				}
			}

		});
		eventSection.setWidth(400);
		eventSection.setGridPlaceholder(1, true);
		eventSection.setButtonText(Messages.getString("CrosstabEventHandlerPage.dialog.Browse")); //$NON-NLS-1$
		addSection(CrosstabPageSectionId.HANDLER_EVENT, eventSection);

		createSections();
		layoutSections();
	}

	private String getEventHandlerClassName(DesignElementHandle handle) {
		return "org.eclipse.birt.report.item.crosstab.core.script.ICrosstabEventHandler"; //$NON-NLS-1$
	}
}
