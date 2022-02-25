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

package org.eclipse.birt.report.model.api;

import java.io.IOException;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.UserChoice;
import org.eclipse.birt.report.model.metadata.ChoicePropertyType;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * TestCases for UserPropertyHandle.
 *
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testConstructor()}</td>
 * <td>Constructs a UserPropertyHandle with the specified element and the
 * property definition.</td>
 * <td>The object of UserPropertyHandle is created.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Constructs a UserPropertyHandle with the specified element and the
 * property name.</td>
 * <td>The object of UserPropertyHandle is created.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetters()}</td>
 * <td>Tests to get element, design, the property name, the value and the
 * display name by a UserPropertyHandle.</td>
 * <td>Values can be retrieved correctly.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSetPropDefn()}</td>
 * <td>Sets UserPropertyDefn has no name.</td>
 * <td>Throws UserPropertyException with the error code NAME_REQUIRED.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Sets a valid UserPropertyDefn with a name.</td>
 * <td>The property definition is set correctly.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Sets a UserPropertyDefn with choices.</td>
 * <td>Property type is ChoiceType and throws an exception for the invalid
 * value.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Sets a valid value for a UserPropertyDefn with choices..</td>
 * <td>The value is set correctly.</td>
 * </tr>
 *
 * </table>
 *
 *
 */

public class UserPropertyHandleTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		openDesign("UserPropertyHandleTest.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests to construct the UserPropertyHandle.
	 *
	 * @throws Exception
	 *
	 */

	public void testConstructor() throws Exception {

		UserPropertyDefn upd = new UserPropertyDefn();
		UserPropertyDefnHandle handle1 = new UserPropertyDefnHandle(designHandle.findMasterPage("My Page"), upd); //$NON-NLS-1$
		assertNotNull(handle1);

		upd.setName("newName"); //$NON-NLS-1$
		designHandle.findMasterPage("My Page").addUserPropertyDefn(upd); //$NON-NLS-1$
		UserPropertyDefnHandle handle2 = new UserPropertyDefnHandle(designHandle.findMasterPage("My Page"), "newName"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(handle2);

	}

	/**
	 * Test cases to set UserPropertyDefn.
	 *
	 * <ul>
	 * <li>Sets UserPropertyDefn has no name.
	 * <li>Sets a valid UserPropertyDefn with a name.
	 * <li>Sets a valid UserPropertyDefn with choices.
	 * <li>Sets the value for a UserPropertyDefn with choices.
	 * </ul>
	 *
	 * @throws SemanticException
	 * @throws IOException
	 * @throws UserPropertyException
	 * @throws PropertyValueException
	 * @throws MetaDataException
	 */

	public void testSetPropDefn()
			throws SemanticException, IOException, UserPropertyException, PropertyValueException, MetaDataException {

		MasterPageHandle pageHandle = designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		UserPropertyDefnHandle updHandle = pageHandle.getUserPropertyDefnHandle("myProp1"); //$NON-NLS-1$
		assertNotNull(updHandle);

		UserPropertyDefn upd = new UserPropertyDefn();

		// set a new upd without providing the upd name. Exception expected
		// here.
		try {
			updHandle.setUserPropertyDefn(upd);
			fail();
		} catch (UserPropertyException e) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_NAME_REQUIRED, e.getErrorCode());
		}

		// set the name to this upd. Set the upd with name but without type. the
		// type expected here is the default value.
		upd.setName("new upd"); //$NON-NLS-1$
		assertEquals(0, upd.getTypeCode());
		updHandle.setUserPropertyDefn(upd);

		// undo the abve set propDefn operation to do another test.
		designHandle.getCommandStack().undo();

		// set the upd type to structList which is not allowed for userProperty.
		PropertyType propType = new StructPropertyType();
		upd.setType(propType);

		try {
			updHandle.setUserPropertyDefn(upd);
			fail();
		} catch (UserPropertyException upe) {
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE, upe.getErrorCode());
		}

		// new a choice type and set a choice type propDefn
		UserChoice[] set = new UserChoice[3];
		set[0] = new UserChoice("newOne", "one resourcekey"); //$NON-NLS-1$ //$NON-NLS-2$
		set[0].setValue("one"); //$NON-NLS-1$
		set[1] = new UserChoice("newTwo", "two resourcekey"); //$NON-NLS-1$ //$NON-NLS-2$
		set[1].setValue("two"); //$NON-NLS-1$
		set[2] = new UserChoice("newThree", "three resourcekey"); //$NON-NLS-1$ //$NON-NLS-2$
		set[2].setValue("three"); //$NON-NLS-1$

		upd.setChoices(set);

		propType = new ChoicePropertyType();
		upd.setType(propType);

		// TODO now we not support complex type, like choice

//		try
//		{
//			updHandle.setUserPropertyDefn( upd );
//			fail( );
//		}
//		catch ( PropertyValueException pve )
//		{
//			assertEquals(
//					PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND,
//					pve.getErrorCode( ) );
//		}

	}

	/**
	 * Tests to get element, design, the property name, the value and the display
	 * name.
	 *
	 * @throws SemanticException if the value cannot be set correctly.
	 */

	public void testGetters() throws SemanticException {

		MasterPageHandle pageHandle = designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		UserPropertyDefnHandle updHandle = pageHandle.getUserPropertyDefnHandle("myProp1"); //$NON-NLS-1$
		assertNotNull(updHandle);

		assertEquals(pageHandle.getElement(), updHandle.getElement());
		assertEquals(design, updHandle.getModule());
		assertEquals("myProp1", updHandle.getName()); //$NON-NLS-1$

		UserPropertyDefn copy = updHandle.getCopy();
		assertEquals(updHandle.getDisplayName(), copy.getDisplayName());
		assertEquals(updHandle.getDefn().getName(), copy.getName());
		assertEquals(updHandle.getDefn().getTypeCode(), copy.getType().getTypeCode());

	}

}
