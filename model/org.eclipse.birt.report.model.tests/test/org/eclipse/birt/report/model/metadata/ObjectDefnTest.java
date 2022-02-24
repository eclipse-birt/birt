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

import java.util.Iterator;

import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * 
 * Unit test for Class ObjectDefn.
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testAddThreeProperty()}</td>
 * <td>Adds three properties, iterate them and find them.</td>
 * <td>Properties are set properly. And the value of a non-existed property can
 * not be found.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testAddSameProperty()}</td>
 * <td>Adds the property whose name exists in object definition</td>
 * <td>Throws a <code>MetaDataException</code>.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetterAndSetter()}</td>
 * <td>Test getting/setting name/display name.</td>
 * <td>Values are set and returned properly.</td>
 * </tr>
 * 
 * </table>
 */

public class ObjectDefnTest extends BaseTestCase {

	private ObjectDefn obj = new ObjectDefn("TestObject"); //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ThreadResources.setLocale(TEST_LOCALE);
		MetadataTestUtil.setDisplayNameKey(obj, "Element.ReportDesign"); //$NON-NLS-1$
	}

	/**
	 * test getters and setters.
	 * 
	 */
	public void testGetterAndSetter() {
		assertEquals("TestObject", obj.getName()); //$NON-NLS-1$
		MetadataTestUtil.setName(obj, "New Object Name"); //$NON-NLS-1$

		assertEquals("\u62a5\u8868", obj.getDisplayName()); //$NON-NLS-1$
		assertEquals("New Object Name", obj.getName()); //$NON-NLS-1$

	}

	/**
	 * add three properties, iterate them and find them.
	 * 
	 */
	public void testAddThreeProperty() {
		PropertyDefnFake propertyA = new PropertyDefnFake();
		PropertyDefnFake propertyB = new PropertyDefnFake();
		PropertyDefnFake propertyC = new PropertyDefnFake();

		propertyA.setName("property A"); //$NON-NLS-1$
		propertyB.setName("property B"); //$NON-NLS-1$
		propertyC.setName("property C"); //$NON-NLS-1$

		// add three properties

		try {
			MetadataTestUtil.addPropertyDefn(obj, propertyA);
			MetadataTestUtil.addPropertyDefn(obj, propertyB);
			MetadataTestUtil.addPropertyDefn(obj, propertyC);
		} catch (MetaDataException e) {
			fail();
		}

		// iterate all properties

		Iterator iter = obj.getPropertyIterator();
		while (iter.hasNext()) {
			PropertyDefn property = (PropertyDefn) iter.next();

			assertTrue(property.getName().equals("property A") || //$NON-NLS-1$
					property.getName().equals("property B") || //$NON-NLS-1$
					property.getName().equals("property C")); //$NON-NLS-1$
		}

		// find property via name

		assertSame(propertyA, obj.findProperty(propertyA.getName()));
		assertSame(propertyB, obj.findProperty(propertyB.getName()));
		assertSame(propertyC, obj.findProperty(propertyC.getName()));

		// find an unexisting property

		assertNull(obj.findProperty("Unexisting")); //$NON-NLS-1$
	}

	/**
	 * add the property whose name exists in object definition.
	 * 
	 */
	public void testAddSameProperty() {
		PropertyDefnFake propertyA = new PropertyDefnFake();

		propertyA.setName("property A"); //$NON-NLS-1$

		// add the property whose name exists in object definition

		try {
			MetadataTestUtil.addPropertyDefn(obj, propertyA);
			MetadataTestUtil.addPropertyDefn(obj, propertyA);

			fail("MetaDataException should be thrown because the name exists !"); //$NON-NLS-1$
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_PROPERTY, e.getErrorCode());
		}
	}
}
