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

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;

public class BlockStackingExecutorTest extends TestCase {

	public void testBlockStacking() throws BirtException {
		IReportContent report = ContentFactory.createReportContent();
		IContainerContent container = report.createContainerContent();
		ILabelContent label1 = createBlockLabel(report);
		container.getChildren().add(label1);
		ILabelContent label2 = createInlineLabel(report);
		container.getChildren().add(label2);
		ILabelContent label3 = createInlineLabel(report);
		container.getChildren().add(label3);
		ILabelContent label4 = createBlockLabel(report);
		container.getChildren().add(label4);
		ILabelContent label5 = createInlineLabel(report);
		container.getChildren().add(label5);
		ILabelContent label6 = createBlockLabel(report);
		container.getChildren().add(label6);

		DOMReportItemExecutor executor = new DOMReportItemExecutor(container);
		BlockStackingExecutor blockStacking = new BlockStackingExecutor(executor.execute(), executor);
		assertTrue(blockStacking.hasNextChild());
		IReportItemExecutor child = blockStacking.getNextChild();
		assertTrue(child.execute() == label1);

		assertTrue(blockStacking.hasNextChild());
		child = blockStacking.getNextChild();
		assertTrue(child.execute() == null);
		assertTrue(child.hasNextChild());
		assertTrue(child.getNextChild().execute() == label2);
		assertTrue(child.getNextChild().execute() == label3);
		assertFalse(child.hasNextChild());

		assertTrue(blockStacking.hasNextChild());
		child = blockStacking.getNextChild();
		assertTrue(child.execute() == label4);
		assertFalse(child.hasNextChild());

		assertTrue(blockStacking.hasNextChild());
		child = blockStacking.getNextChild();
		assertTrue(child.execute() == null);
		assertTrue(child.hasNextChild());
		assertTrue(child.getNextChild().execute() == label5);
		assertFalse(child.hasNextChild());

		assertTrue(blockStacking.hasNextChild());
		child = blockStacking.getNextChild();
		assertTrue(child.execute() == label6);
		assertFalse(child.hasNextChild());

		assertFalse(blockStacking.hasNextChild());

	}

	protected ILabelContent createBlockLabel(IReportContent report) {
		ILabelContent label = report.createLabelContent();
		label.getStyle().setDisplay("block");
		return label;
	}

	protected ILabelContent createInlineLabel(IReportContent report) {
		ILabelContent label = report.createLabelContent();
		label.getStyle().setDisplay("inline");
		return label;
	}
}
