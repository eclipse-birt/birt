/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleGroupElementHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Some properties for report items in master page may need to be disabled,
 * include: TOC, Bookmark, Page break;
 * <p>
 * Test description:
 * <p>
 * Add a label in masterpage header, ensure that TOC, Bookmark and Page break
 * properties are read-only.
 * <p>
 */
public class Regression_121276 extends BaseTestCase {

	/**
	 * @throws ContentException
	 * @throws NameException
	 */
	public void test_regression_121276() throws ContentException, NameException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$

		SimpleMasterPageHandle page = factory.newSimpleMasterPage("newpage"); //$NON-NLS-1$
		designHandle.getMasterPages().add(page);

		page.getPageHeader().add(label);

		List elements = new ArrayList();
		elements.add(label);
		SimpleGroupElementHandle group = new SimpleGroupElementHandle(designHandle, elements);

		// ensure that TOC, Bookmark and Page break properties are read-only.

		GroupPropertyHandle bookmark = group.getPropertyHandle(LabelHandle.BOOKMARK_PROP);
		assertEquals(true, bookmark.isReadOnly());

		GroupPropertyHandle toc = group.getPropertyHandle(LabelHandle.TOC_PROP);
		assertEquals(true, toc.isReadOnly());

		GroupPropertyHandle pagebreak = group.getPropertyHandle(StyleHandle.PAGE_BREAK_AFTER_PROP);
		assertEquals(true, pagebreak.isReadOnly());

	}
}
