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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.UserPropertyEvent;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.FloatPropertyType;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Unit test case for class UserPropertyCommand.
 * 
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
 * collapse" bordercolor="#111111" width="100%" id="AutoNumber2">
 * <tr>
 * <td width="33%"></td>
 * <td width="33%"></td>
 * <td width="34%"></td>
 * </tr>
 * <tr>
 * <td width="33%"><b>Method </b></td>
 * <td width="33%"><b>Test Case </b></td>
 * <td width="34%"><b>Expected Result </b></td>
 * </tr>
 * <tr>
 * <td width="33%">testAddUserProperty( )</td>
 * <td width="33%">reate a user property.Set name, Type, Default value, choice
 * for it. Add it into masterPage. Then set property value for it.</td>
 * <td width="34%">Operations can be done.</td>
 * </tr>
 * <td width="33%">testUserPropertyFromAPILevel()</td>
 * <td width="33%">Parse an test xml file, get the user property value defined
 * in it.</td>
 * <td width="34%">correct value returned.</td>
 * </tr>
 * <tr>
 * <td width="33%"></td>
 * <td width="33%">retrieve a not existed user property value</td>
 * <td width="34%">Exception expected.</td>
 * </tr>
 * <tr>
 * <td width="33%"></td>
 * <td width="33%">drop a user property from an element which did not defined
 * it.</td>
 * <td width="34%">Exception expected.</td>
 * </tr>
 * <tr>
 * <td width="33%"></td>
 * <td width="33%">Add a user property on an element which can not hold a user
 * property.</td>
 * <td width="34%">Exception expected.</td>
 * </tr>
 * <tr>
 * <td width="33%"></td>
 * <td width="33%">test if the value of all user property written in the xml
 * file can be parsed correctly</td>
 * <td width="34%">correct value returned.</td>
 * </tr>
 * <tr>
 * <td width="33%"></td>
 * <td width="33%"></td>
 * <td width="34%"></td>
 * </tr>
 * <tr>
 * <td width="33%">testSetUserPropertyDefn( )</td>
 * <td width="33%">reset a user property definition.The two definition may have
 * different name, type, display name or choices.</td>
 * <td width="34%">Operations can be done and check the exception.</td>
 * </tr>
 * </table>
 * 
 */

public class UserPropertyCommandTest extends BaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Unit test for method addUserProperty.
	 * 
	 * Test Case:Normal case
	 * <ul>
	 * <li>create a user property.Set name, Type, Default value, choice for it. Add
	 * it into masterPage. Then set property value for it.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testAddUserProperty() throws Exception {
		openDesign("UserPropertyCommandTest.xml", TEST_LOCALE);//$NON-NLS-1$
		assertEquals(0, design.getErrorList().size());

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("My Data"); //$NON-NLS-1$

		String propValue = (String) dataHandle.getProperty("myProp2"); //$NON-NLS-1$
		assertEquals("choiceOne", propValue); //$NON-NLS-1$

		dataHandle.setProperty("myProp2", "\u4e00"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("choiceOne", dataHandle.getStringProperty("myProp2")); //$NON-NLS-1$ //$NON-NLS-2$

		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		PropertyType intType = dd.getPropertyType(PropertyType.INTEGER_TYPE);

		UserPropertyDefn newPropDefn = new UserPropertyDefn();
		newPropDefn.setName("myProp4"); //$NON-NLS-1$
		newPropDefn.setType(intType);

		// Add one new user-defined property definition

		dataHandle.addUserPropertyDefn(newPropDefn);
		UserPropertyDefnHandle newPropDefnHandle = dataHandle.getUserPropertyDefnHandle("myProp4"); //$NON-NLS-1$
		assertNotNull(newPropDefnHandle);
		assertEquals(intType.getTypeCode(), newPropDefnHandle.getType());

		// The property name is duplicated.

		try {
			dataHandle.addUserPropertyDefn(newPropDefn);
			fail();
		} catch (UserPropertyException e) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_DUPLICATE_NAME, e.getErrorCode());
		}

		// Add new user-defined property to the element that doesn't allow
		// user-defined property.

		UserPropertyDefn upd = (UserPropertyDefn) dataHandle.getPropertyHandle("myProp1") //$NON-NLS-1$
				.getDefn();

		try {
			designHandle.addUserPropertyDefn(upd);
			fail();
		} catch (UserPropertyException upe) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_INVALID_DISPLAY_ID, upe.getErrorCode());
		}

		// the type is not supported

		newPropDefn = new UserPropertyDefn();
		newPropDefn.setName("uProperty"); //$NON-NLS-1$
		newPropDefn.setType(dd.getPropertyType(PropertyType.EXTENDS_TYPE));
		try {
			dataHandle.addUserPropertyDefn(newPropDefn);
			fail();
		} catch (UserPropertyException e) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE, e.getErrorCode());
		}

		// the default value is invalid

		newPropDefn = new UserPropertyDefn();
		newPropDefn.setName("uProperty"); //$NON-NLS-1$
		newPropDefn.setType(dd.getPropertyType(PropertyType.INTEGER_TYPE));
		newPropDefn.setDefault("wInteger"); //$NON-NLS-1$
		try {
			dataHandle.addUserPropertyDefn(newPropDefn);
			fail();
		} catch (UserPropertyException e) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE, e.getErrorCode());
		}
	}

	/**
	 * Unit test for user property from API.
	 * 
	 * @throws Exception if any exception
	 * 
	 */

	public void testDropUserProperty() throws Exception {
		openDesign("UserPropertyCommandTest.xml");//$NON-NLS-1$
		assertEquals(0, design.getErrorList().size());

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("My Data"); //$NON-NLS-1$

		assertNotNull(dataHandle);
		assertEquals("choiceOne", //$NON-NLS-1$
				dataHandle.getStringProperty("myProp2")); //$NON-NLS-1$

		// Drop one user-defined property.

		dataHandle.dropUserPropertyDefn("myProp2"); //$NON-NLS-1$
		UserPropertyDefnHandle userPropHandle = dataHandle.getUserPropertyDefnHandle("myProp2"); //$NON-NLS-1$
		assertNull(userPropHandle);

		// Drop the user-defined property which doesn't exist.

		try {
			dataHandle.dropUserPropertyDefn("prop"); //$NON-NLS-1$
			fail();
		} catch (UserPropertyException e) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_NOT_FOUND, e.getErrorCode());
		}

		// Drop the user-defined property from the element which doesn't allow
		// user-defined property.

		try {
			designHandle.dropUserPropertyDefn("prop"); //$NON-NLS-1$
			fail();
		} catch (UserPropertyException e) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_NOT_FOUND, e.getErrorCode());
		}

		// Drop the user-defined property from parent. All value in children
		// should be removed.

		DataItemHandle parentDataHandle = (DataItemHandle) designHandle.findElement("base data"); //$NON-NLS-1$
		DataItemHandle childDataHandle = (DataItemHandle) designHandle.findElement("child data"); //$NON-NLS-1$
		assertNotNull(parentDataHandle.getUserPropertyDefnHandle("stringProp")); //$NON-NLS-1$
		assertNotNull(childDataHandle.getUserPropertyDefnHandle("stringProp")); //$NON-NLS-1$
		assertNotNull(childDataHandle.getProperty("stringProp")); //$NON-NLS-1$

		parentDataHandle.dropUserPropertyDefn("stringProp"); //$NON-NLS-1$

		assertNull(parentDataHandle.getUserPropertyDefnHandle("stringProp")); //$NON-NLS-1$
		assertNull(childDataHandle.getUserPropertyDefnHandle("stringProp")); //$NON-NLS-1$
		assertNull(childDataHandle.getProperty("stringProp")); //$NON-NLS-1$
	}

	/**
	 * Unit test for set the new definition.
	 * 
	 * @throws Exception
	 */

	public void testSetUserPropertyDefn() throws Exception {
		openDesign("UserPropertyCommandTest.xml");//$NON-NLS-1$
		assertEquals(0, design.getErrorList().size());

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("My Data"); //$NON-NLS-1$

		assertNotNull(dataHandle);
		UserPropertyDefn old = (UserPropertyDefn) dataHandle.getPropertyHandle("myProp1").getDefn(); //$NON-NLS-1$

		UserPropertyCommand updCommand = new UserPropertyCommand(design, dataHandle.getElement());
		UserPropertyDefn newProp = new UserPropertyDefn();
		newProp.setName("upd"); //$NON-NLS-1$
		PropertyType type = new FloatPropertyType();
		newProp.setType(type);
		try {
			updCommand.setPropertyDefn(old, newProp);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
		old = (UserPropertyDefn) dataHandle.getPropertyHandle("myProp2").getDefn(); //$NON-NLS-1$
		double value = dataHandle.getFloatProperty("myProp2"); //$NON-NLS-1$
		newProp.setName("updNew"); //$NON-NLS-1$

		// The new user-defined property is inconsistent with old user-defined
		// property type.

		try {
			updCommand.setPropertyDefn(old, newProp);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// The new user-defined property has no name.

		assertEquals(new Double(value).toString(), new Double(dataHandle.getFloatProperty("updNew")).toString()); //$NON-NLS-1$
		newProp = new UserPropertyDefn();
		try {
			updCommand.setPropertyDefn(old, newProp);
			fail();
		} catch (UserPropertyException e) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_NAME_REQUIRED, e.getErrorCode());
		}

		// The old user-defined property definition is not found.

		old.setName("notExists"); //$NON-NLS-1$
		try {
			updCommand.setPropertyDefn(old, newProp);
			fail();
		} catch (UserPropertyException e) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * Test <code>UserPropertyEvent</code> notification.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testNotification() throws DesignFileException, SemanticException {
		openDesign("UserPropertyCommandTest.xml", ULocale.CHINA);//$NON-NLS-1$

		MyUserPropertyListener listener = new MyUserPropertyListener();
		MyPropertyListener propertyListener = new MyPropertyListener();

		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		PropertyType stringType = dd.getPropertyType(PropertyType.STRING_TYPE);

		UserPropertyDefn userPropDefn = new UserPropertyDefn();
		userPropDefn.setName("UP1");//$NON-NLS-1$
		userPropDefn.setType(stringType);
		userPropDefn.setDisplayName("Property 1");//$NON-NLS-1$

		FreeFormHandle freeForm = designHandle.getElementFactory().newFreeForm("F1"); //$NON-NLS-1$
		designHandle.getBody().add(freeForm);
		freeForm.addListener(listener);
		freeForm.addListener(propertyListener);

		// Add user property definition

		listener.action = MyUserPropertyListener.NA;
		propertyListener.propertyChanged = false;
		freeForm.addUserPropertyDefn(userPropDefn);
		assertEquals(MyUserPropertyListener.ADDED, listener.action);
		assertFalse(propertyListener.propertyChanged);

		// Change user property type
		// Note: this test is commented for there is inconsistency in user
		// property changing.
		// PropertyType intType = dd.getPropertyType( PropertyType.INTEGER_TYPE
		// );
		// UserPropertyDefnHandle userPropHandle =
		// freeForm.getUserPropertyDefnHandle( userPropDefn.getName());
		// UserPropertyDefn newPropDefn = userPropHandle.getCopy();
		// newPropDefn.setType( intType );
		// userPropHandle.setUserPropertyDefn( newPropDefn );
		// assertEquals( MyUserPropertyListener.CHANGED, listener.action );
		// assertTrue( propertyListener.propertyChanged);

		// Change user property value

		listener.action = MyUserPropertyListener.NA;
		propertyListener.propertyChanged = false;
		freeForm.setProperty(userPropDefn.getName(), "123"); //$NON-NLS-1$
		assertEquals(MyUserPropertyListener.NA, listener.action);
		assertTrue(propertyListener.propertyChanged);

		// Remove user property definition

		listener.action = MyUserPropertyListener.NA;
		propertyListener.propertyChanged = false;
		freeForm.dropUserPropertyDefn(userPropDefn.getName());
		assertEquals(MyUserPropertyListener.REMOVED, listener.action);
		assertTrue(propertyListener.propertyChanged);
	}

	class MyUserPropertyListener implements Listener {

		static final int NA = 0;
		static final int ADDED = 1;
		static final int REMOVED = 2;
		static final int CHANGED = 3;

		UserPropertyEvent event = null;

		int action = 0;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt.report.
		 * model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev.getEventType() == NotificationEvent.USER_PROP_EVENT) {
				event = (UserPropertyEvent) ev;

				if (action == ADDED && event.getAction() == UserPropertyEvent.DROP)
					action = CHANGED;
				else {
					switch (event.getAction()) {
					case UserPropertyEvent.ADD:
						action = ADDED;
						break;
					case UserPropertyEvent.DROP:
						action = REMOVED;
						break;
					}
				}
			}
		}
	}

	class MyPropertyListener implements Listener {

		boolean propertyChanged = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt.report.
		 * model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev.getEventType() == NotificationEvent.PROPERTY_EVENT) {
				propertyChanged = true;
			}
		}

	}

}
