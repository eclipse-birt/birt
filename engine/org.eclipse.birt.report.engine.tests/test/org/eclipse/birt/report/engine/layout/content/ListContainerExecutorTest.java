/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.layout.content;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;

import junit.framework.TestCase;

public class ListContainerExecutorTest extends TestCase {

	public void testListStacking() throws BirtException {
		IReportContent report = ContentFactory.createReportContent();
		IListContent list = report.createListContent();

		IListBandContent listHeader = report.createListBandContent();
		list.getChildren().add(listHeader);
		ILabelContent headerLabel = createBlockLabel(report);
		listHeader.getChildren().add(headerLabel);

		IListGroupContent group1 = report.createListGroupContent();
		list.getChildren().add(group1);
		IListBandContent groupHeader = report.createListBandContent();
		group1.getChildren().add(groupHeader);
		ILabelContent groupHeaderLabel = createBlockLabel(report);
		groupHeader.getChildren().add(groupHeaderLabel);

		IListBandContent detail = report.createListBandContent();
		group1.getChildren().add(detail);
		ILabelContent detailLabel = createBlockLabel(report);
		detail.getChildren().add(detailLabel);

		IListBandContent listFooter = report.createListBandContent();
		list.getChildren().add(listFooter);
		ILabelContent footerLabel = createBlockLabel(report);
		listFooter.getChildren().add(footerLabel);

		DOMReportItemExecutor executor = new DOMReportItemExecutor(list);
		ListContainerExecutor listExecutor = new ListContainerExecutor(executor.execute(), executor);
		assertTrue(listExecutor.hasNextChild());
		IReportItemExecutor child = listExecutor.getNextChild();
		assertTrue(child.execute() == headerLabel);

		assertTrue(listExecutor.hasNextChild());
		child = listExecutor.getNextChild();
		assertTrue(child.execute() == group1);
		assertTrue(child.hasNextChild());
		child = new ListContainerExecutor(group1, child);
		assertTrue(child.hasNextChild());
		assertTrue(child.getNextChild().execute() == groupHeaderLabel);
		assertTrue(child.hasNextChild());
		assertTrue(child.getNextChild().execute() == detailLabel);
		assertFalse(child.hasNextChild());

		assertTrue(listExecutor.hasNextChild());
		child = listExecutor.getNextChild();
		assertTrue(child.execute() == footerLabel);
		assertFalse(listExecutor.hasNextChild());

	}

	protected ILabelContent createBlockLabel(IReportContent report) {
		ILabelContent label = report.createLabelContent();
		label.getStyle().setDisplay("block"); //$NON-NLS-1$
		return label;
	}

	protected ILabelContent createInlineLabel(IReportContent report) {
		ILabelContent label = report.createLabelContent();
		label.getStyle().setDisplay("inline"); //$NON-NLS-1$
		return label;
	}
}
