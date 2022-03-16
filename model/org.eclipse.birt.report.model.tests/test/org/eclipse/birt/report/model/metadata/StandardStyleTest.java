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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for StandardStyle.
 *
 */
public class StandardStyleTest extends BaseTestCase {

	private PredefinedStyle standardStyle = null;

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// MetaDataDictionary.reset();
		super.setUp();
		standardStyle = new PredefinedStyle();

		// Setup.setup();
	}

	/**
	 * test getters and setters.
	 *
	 */
	public void testGetterSetters() {
		MetadataTestUtil.setPredefinedStyleDisplayNameKey(standardStyle, "DisplayNameID"); //$NON-NLS-1$
		MetadataTestUtil.setPredefinedStyleName(standardStyle, "Name"); //$NON-NLS-1$

		assertEquals("DisplayNameID", standardStyle.getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("Name", standardStyle.getName()); //$NON-NLS-1$
	}

	/**
	 * test adding two derived style to one style.
	 *
	 */
	public void testAddTwoDerivedStyles() {
		/*
		 * <Style name="$BH" displayNameID="BASE_HDR_STYLE_DISP_NAME"
		 * reference="Base Header" extends="$N"/> <Style name="$LGH1"
		 * displayNameID="LIST_GRP1_HDR_STYLE_DISP_NAME" reference="List Group 1
		 * Header" extends="$BH"/> <Style name="$LGH2"
		 * displayNameID="LIST_GRP2_HDR_STYLE_DISP_NAME" reference="List Group 2
		 * Header" extends="$BH"/>
		 */

		PredefinedStyle bh = new PredefinedStyle();
		PredefinedStyle lgh1 = new PredefinedStyle();
		PredefinedStyle lgh2 = new PredefinedStyle();

		MetadataTestUtil.setPredefinedStyleName(bh, "$BH"); //$NON-NLS-1$
		MetadataTestUtil.setPredefinedStyleDisplayNameKey(bh, "BASE_HDR_STYLE_DISP_NAME"); //$NON-NLS-1$
		MetadataTestUtil.setPredefinedStyleName(lgh1, "@LGH1"); //$NON-NLS-1$
		MetadataTestUtil.setPredefinedStyleDisplayNameKey(lgh1, "LIST_GRP1_HDR_STYLE_DISP_NAME"); //$NON-NLS-1$
		MetadataTestUtil.setPredefinedStyleName(lgh2, "@LGH2"); //$NON-NLS-1$
		MetadataTestUtil.setPredefinedStyleDisplayNameKey(lgh2, "LIST_GRP2_HDR_STYLE_DISP_NAME"); //$NON-NLS-1$
	}
}
