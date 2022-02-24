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

package org.eclipse.birt.report.model.extension;

import java.util.List;

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.EncryptionException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SimpleEncryptionHelper;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the extension pointer of
 * org.eclipse.birt.report.model.encryptionHelper.
 */

public class EncryptionHelperExtensionTest extends BaseTestCase {

	private final static String propName = "pswd"; //$NON-NLS-1$
	private final static String FILE_NAME = "EncryptionExtensionTest.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests the encryption helper from extension.
	 */

	public void testMetaData() {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		List helpers = dd.getEncryptionHelpers();
		assertTrue(helpers.size() >= 4);

		IEncryptionHelper helper = dd.getEncryptionHelper("oneEncryptionHelper"); //$NON-NLS-1$
		assertEquals("org.eclipse.birt.report.model.tests.encryptionHelper.EncryptionHelperImpl", //$NON-NLS-1$
				helper.getClass().getName());

		String testString = "something"; //$NON-NLS-1$
		assertEquals("_ab_something", helper.encrypt(testString)); //$NON-NLS-1$
		assertEquals(testString, helper.decrypt(helper.encrypt(testString)));

		helper = dd.getEncryptionHelper(SimpleEncryptionHelper.ENCRYPTION_ID);
		assertEquals(helper, SimpleEncryptionHelper.getInstance());

		assertNull(dd.getEncryptionHelper("wrong_en")); //$NON-NLS-1$

		// test default encryption
		assertEquals("oneEncryptionHelper", dd.getDefaultEncryptionHelperID()); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private ElementPropertyDefn getPropertyDefn(DesignElementHandle item) {
		return (ElementPropertyDefn) item.getPropertyDefn(propName);
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testParser() throws Exception {
		openDesign(FILE_NAME);

		// test: getProperty returned rare value; value in the hash-map is
		// encrypted; the encryption is right
		DesignElementHandle item = designHandle.findElement("item_1"); //$NON-NLS-1$
		assertEquals("New Password", item.getStringProperty(propName)); //$NON-NLS-1$
		assertEquals(SimpleEncryptionHelper.ENCRYPTION_ID,
				item.getElement().getLocalEncryptionID(getPropertyDefn(item)));
		assertEquals("TmV3IFBhc3N3b3Jk", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));
		assertEquals(SimpleEncryptionHelper.ENCRYPTION_ID,
				item.getElement().getLocalEncryptionID(getPropertyDefn(item)));
		assertEquals("New Password", SimpleEncryptionHelper.getInstance().decrypt("TmV3IFBhc3N3b3Jk")); //$NON-NLS-1$//$NON-NLS-2$

		// item3 extends form a library one
		item = designHandle.findElement("item_3"); //$NON-NLS-1$
		assertEquals("myown", item.getStringProperty(propName)); //$NON-NLS-1$
		assertNull(item.getElement().getLocalProperty(design, propName));
		assertEquals("_b_myown", ExtensionTestUtil //$NON-NLS-1$
				.getLocalExtensionMapValue((ExtendedItem) item.getElement().getExtendsElement(), propName));
		assertNull(item.getElement().getLocalEncryptionID(getPropertyDefn(item)));
		assertEquals("encryption_b", item.getElement().getEncryptionID(getPropertyDefn(item))); //$NON-NLS-1$

		// item2 has local value and no parent, however not specify local
		// encryption: use default encryption to encrypt-decrypt property
		item = designHandle.findElement("item_2"); //$NON-NLS-1$
		assertEquals("mypswd", item.getStringProperty(propName)); //$NON-NLS-1$
		assertNull(item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		assertEquals("_ab_mypswd", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));
		assertEquals("oneEncryptionHelper", item.getElement().getEncryptionID(getPropertyDefn(item))); //$NON-NLS-1$

		// item4 has local value and it has library parent, however not specify
		// local encryption
		item = designHandle.findElement("item_4"); //$NON-NLS-1$
		assertEquals("mypswd", item.getStringProperty(propName)); //$NON-NLS-1$
		assertNull(item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		assertEquals("encryption_b", item.getElement().getEncryptionID(getPropertyDefn(item))); //$NON-NLS-1$
		assertEquals("_b_mypswd", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));

		// item5 has local value and local encryption, however its encryption
		// does not exist
		item = designHandle.findElement("item_5"); //$NON-NLS-1$
		assertEquals("_ab_mypswd", item.getStringProperty(propName)); //$NON-NLS-1$
		assertEquals("no_encryption", item.getElement().getLocalEncryptionID( //$NON-NLS-1$
				(ElementPropertyDefn) item.getPropertyDefn(propName)));
		assertEquals("_ab_mypswd", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));

	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testCompatibility() throws Exception {
		openDesign("EncryptionExtensionTest_1.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("EncryptionExtensionTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testCommand() throws Exception {
		openDesign(FILE_NAME);
		DesignElementHandle item = designHandle.findElement("item_1"); //$NON-NLS-1$

		// set encryption for not encryptable property
		try {
			item.setEncryption(IReportItemModel.DATA_SET_PROP, "e"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(EncryptionException.DESIGN_EXCEPTION_INVALID_ENCRYPTABLE_PROPERTY, e.getErrorCode());
		}

		// set non-existing encryption
		try {
			item.setEncryption(propName, "no_encryption"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(EncryptionException.DESIGN_EXCEPTION_INVALID_ENCRYPTION, e.getErrorCode());
		}

		CommandStack stack = designHandle.getCommandStack();

		// set encryption, this element has local value
		item.setEncryption(propName, "encryption_a"); //$NON-NLS-1$
		assertEquals("New Password", item.getStringProperty(propName)); //$NON-NLS-1$
		assertEquals("encryption_a", item.getElement() //$NON-NLS-1$
				.getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		assertEquals("_a_New Password", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));

		// undo
		stack.undo();
		assertEquals("New Password", item.getStringProperty(propName)); //$NON-NLS-1$
		assertEquals(SimpleEncryptionHelper.ENCRYPTION_ID,
				item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		assertEquals("TmV3IFBhc3N3b3Jk", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));

		// redo
		stack.redo();
		assertEquals("New Password", item.getStringProperty(propName)); //$NON-NLS-1$
		assertEquals("encryption_a", item.getElement() //$NON-NLS-1$
				.getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		assertEquals("_a_New Password", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));

		// item2 has local value, however not specify local encryption
		item = designHandle.findElement("item_2"); //$NON-NLS-1$
		assertEquals("mypswd", item.getStringProperty(propName)); //$NON-NLS-1$
		assertNull(item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		assertEquals("_ab_mypswd", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));

		// change encryption
		item.setEncryption(propName, "encryption_b"); //$NON-NLS-1$
		assertEquals("mypswd", item.getStringProperty(propName)); //$NON-NLS-1$
		assertEquals("encryption_b", item.getElement().getLocalEncryptionID( //$NON-NLS-1$
				(ElementPropertyDefn) item.getPropertyDefn(propName)));
		assertEquals("_b_mypswd", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));

		// test setEncryption for item3: its value and encryption all extend
		// from parent
		item = designHandle.findElement("item_3"); //$NON-NLS-1$
		assertEquals("myown", item.getStringProperty(propName)); //$NON-NLS-1$
		assertNull(item.getElement().getLocalProperty(design, propName));
		assertEquals("_b_myown", ExtensionTestUtil //$NON-NLS-1$
				.getLocalExtensionMapValue((ExtendedItem) item.getElement().getExtendsElement(), propName));
		assertNull(item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));

		item.setEncryption(propName, "encryption_a"); //$NON-NLS-1$
		assertEquals("myown", item.getStringProperty(propName)); //$NON-NLS-1$
		assertEquals("_a_myown", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));
		assertEquals("encryption_a", item.getElement().getLocalEncryptionID( //$NON-NLS-1$
				(ElementPropertyDefn) item.getPropertyDefn(propName)));
		// undo
		stack.undo();
		assertEquals("myown", item.getStringProperty(propName)); //$NON-NLS-1$
		assertNull(item.getElement().getLocalProperty(design, propName));
		assertEquals("_b_myown", ExtensionTestUtil //$NON-NLS-1$
				.getLocalExtensionMapValue((ExtendedItem) item.getElement().getExtendsElement(), propName));
		assertNull(item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		// redo
		stack.redo();
		assertEquals("myown", item.getStringProperty(propName)); //$NON-NLS-1$
		assertEquals("_a_myown", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));
		assertEquals("encryption_a", item.getElement().getLocalEncryptionID( //$NON-NLS-1$
				(ElementPropertyDefn) item.getPropertyDefn(propName)));
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testSetProperty() throws Exception {
		openDesign(FILE_NAME);

		CommandStack stack = designHandle.getCommandStack();
		String value = "newValue"; //$NON-NLS-1$

		// item1 has local value and encryption
		DesignElementHandle item = designHandle.findElement("item_1"); //$NON-NLS-1$
		assertEquals("New Password", item.getElement().getLocalProperty(design, propName)); //$NON-NLS-1$
		assertEquals(SimpleEncryptionHelper.ENCRYPTION_ID,
				item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		// set property
		item.setProperty(propName, value);
		assertEquals(value, item.getElement().getLocalProperty(design, propName));
		assertEquals(SimpleEncryptionHelper.ENCRYPTION_ID,
				item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		// undo
		stack.undo();
		assertEquals("New Password", item.getElement().getLocalProperty(design, propName)); //$NON-NLS-1$
		assertEquals(SimpleEncryptionHelper.ENCRYPTION_ID,
				item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));

		// item2 has local value and no local encryption
		item = designHandle.findElement("item_2"); //$NON-NLS-1$
		assertEquals("mypswd", item.getElement().getLocalProperty(design, propName)); //$NON-NLS-1$
		assertEquals("_ab_mypswd", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));
		assertNull(item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		// set property
		item.setProperty(propName, value);
		assertEquals(value, item.getElement().getLocalProperty(design, propName));
		assertEquals("_ab_" + value, ExtensionTestUtil.getLocalExtensionMapValue( //$NON-NLS-1$
				(ExtendedItem) item.getElement(), propName));
		assertEquals("oneEncryptionHelper", item.getElement().getLocalEncryptionID( //$NON-NLS-1$
				(ElementPropertyDefn) item.getPropertyDefn(propName)));
		// undo
		stack.undo();
		assertEquals("mypswd", item.getElement().getLocalProperty(design, propName)); //$NON-NLS-1$
		assertEquals("_ab_mypswd", //$NON-NLS-1$
				ExtensionTestUtil.getLocalExtensionMapValue((ExtendedItem) item.getElement(), propName));
		assertEquals("oneEncryptionHelper", item.getElement().getLocalEncryptionID( //$NON-NLS-1$
				(ElementPropertyDefn) item.getPropertyDefn(propName)));

		// item3: all value and encryption extend from parent
		item = designHandle.findElement("item_3"); //$NON-NLS-1$
		assertEquals("myown", item.getStringProperty(propName)); //$NON-NLS-1$
		assertNull(item.getElement().getLocalProperty(design, propName));
		assertEquals("_b_myown", ExtensionTestUtil //$NON-NLS-1$
				.getLocalExtensionMapValue((ExtendedItem) item.getElement().getExtendsElement(), propName));
		assertNull(item.getElement().getLocalEncryptionID((ElementPropertyDefn) item.getPropertyDefn(propName)));
		// set property
		item.setProperty(propName, value);
		assertEquals(value, item.getElement().getLocalProperty(design, propName));
		assertEquals("_b_" + value, ExtensionTestUtil.getLocalExtensionMapValue( //$NON-NLS-1$
				(ExtendedItem) item.getElement(), propName));
		assertEquals("encryption_b", item.getElement().getLocalEncryptionID( //$NON-NLS-1$
				(ElementPropertyDefn) item.getPropertyDefn(propName)));
		// undo
		stack.undo();
		assertEquals("myown", item.getStringProperty(propName)); //$NON-NLS-1$
		assertNull(item.getElement().getLocalProperty(design, propName));
		assertEquals("_b_myown", ExtensionTestUtil //$NON-NLS-1$
				.getLocalExtensionMapValue((ExtendedItem) item.getElement().getExtendsElement(), propName));
		assertEquals("encryption_b", item.getElement().getLocalEncryptionID( //$NON-NLS-1$
				(ElementPropertyDefn) item.getPropertyDefn(propName)));
	}

	private void copyAndInsertItem(DesignElementHandle item) throws Exception {
		String prefix = "copyOf"; //$NON-NLS-1$
		SlotHandle bodyHandle = designHandle.getBody();

		IDesignElement copiedItem = item.copy();
		copiedItem.getHandle(design).setName(prefix + item.getName());
		bodyHandle.add(copiedItem.getHandle(design));
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testClone() throws Exception {
		openDesign(FILE_NAME);

		// copy and insert item1, item2, item3 and item4
		copyAndInsertItem(designHandle.findElement("item_1")); //$NON-NLS-1$
		copyAndInsertItem(designHandle.findElement("item_2")); //$NON-NLS-1$
		copyAndInsertItem(designHandle.findElement("item_3")); //$NON-NLS-1$
		copyAndInsertItem(designHandle.findElement("item_4")); //$NON-NLS-1$
		copyAndInsertItem(designHandle.findElement("item_5")); //$NON-NLS-1$

		save();
		assertTrue(compareFile("EncryptionExtensionTest_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the encryption in the generated document.
	 * 
	 * @throws Exception
	 */
	public void testDocumentSerialize() throws Exception {
		openDesign(FILE_NAME);
		serializeDocument();
		save();

		assertTrue(compareFile("EncryptionExtensionTest_golden_2.xml")); //$NON-NLS-1$
	}
}
