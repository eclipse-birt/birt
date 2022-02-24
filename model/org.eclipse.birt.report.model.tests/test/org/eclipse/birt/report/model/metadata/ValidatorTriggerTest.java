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

/**
 * Tests the validator definition and semantic validation trigger definition.
 */

public class ValidatorTriggerTest extends AbstractMetaTest {

	/**
	 * Tests the rom parsing for ValueValidator tag.
	 * <ul>
	 * <li>Attribute "name" is missing.
	 * <li>Attribute "class" is missing.
	 * <li>The class can not be instantiated.
	 * </ul>
	 */

	public void testValueValidatorParse() throws Exception {
		// The attribute "name" is missing.

		try {
			loadMetaData(this.getClass().getResourceAsStream("input/ValidatorDefnTest.def")); //$NON-NLS-1$
			fail();
		} catch (MetaDataParserException e) {
		}

		// The attribute "class" is missing.

		try {
			loadMetaData(this.getClass().getResourceAsStream("input/ValidatorDefnTest1.def")); //$NON-NLS-1$
			fail();
		} catch (MetaDataParserException e) {
		}

		// The class can not be instantiated.

		try {
			loadMetaData(this.getClass().getResourceAsStream("input/ValidatorDefnTest2.def")); //$NON-NLS-1$
			fail();
		} catch (MetaDataParserException e) {
		}
	}

	/**
	 * Tests the rom parsing for AbstractSemanticValidator tag.
	 * <ul>
	 * <li>Attribute "name" is missing.
	 * <li>Attribute "class" is missing.
	 * <li>The class can not be instantiated.
	 * </ul>
	 */

	public void testSemanticValidatorParse() throws Exception {
		// The attribute "name" is missing.

		try {
			loadMetaData(this.getClass().getResourceAsStream("input/ValidatorDefnTest5.def")); //$NON-NLS-1$
			fail();
		} catch (MetaDataParserException e) {
		}

		// The attribute "class" is missing.

		try {
			loadMetaData(this.getClass().getResourceAsStream("input/ValidatorDefnTest6.def")); //$NON-NLS-1$
			fail();
		} catch (MetaDataParserException e) {
		}

		// The class can not be instantiated.

		try {
			loadMetaData(this.getClass().getResourceAsStream("input/ValidatorDefnTest7.def")); //$NON-NLS-1$
			fail();
		} catch (MetaDataParserException e) {
		}

	}

	/**
	 * Tests the rom parsing for Trigger tag.
	 * <ul>
	 * <li>Attribute "validator" is missing.
	 * <li>The validator is not found.
	 * </ul>
	 */

	public void testTriggerParse() throws Exception {
		// The attribute "validator" is missing.

		try {
			loadMetaData(this.getClass().getResourceAsStream("input/ValidatorDefnTest3.def")); //$NON-NLS-1$
			fail();
		} catch (MetaDataParserException e) {
		}

		// The validator is not found.

		try {
			loadMetaData(this.getClass().getResourceAsStream("input/ValidatorDefnTest4.def")); //$NON-NLS-1$
			fail();
		} catch (MetaDataParserException e) {
		}
	}
}
