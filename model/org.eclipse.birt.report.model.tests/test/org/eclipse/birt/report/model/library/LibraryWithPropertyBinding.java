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

package org.eclipse.birt.report.model.library;

import java.util.List;

import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests property binding in the library and report design.
 */

public class LibraryWithPropertyBinding extends BaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ReportWithPropertyBinding.xml");//$NON-NLS-1$
	}

	/**
	 * Tests get all property bindings method. If current data source and parent has
	 * the same name of property binding, use current data source's value. If only
	 * parent has the property binding, use parent's value. There are three cases in
	 * this test case:
	 * <tr>
	 * <td>Case 1</td>
	 * <td>All property bindings extend from library, there is no binding in design
	 * file</td>
	 * </tr>
	 * <tr>
	 * <td>Case 2</td>
	 * <td>when current data source and parent has the same name of property
	 * binding, all use current data source's value</td>
	 * </tr>
	 * <tr>
	 * <td>Case 3</td>
	 * <td>when only parent has the property binding, use parent's value. else use
	 * current data source's value</td>
	 * </tr>
	 * 
	 * @throws Exception all exception.
	 */

	public void testGetAllPropertyBindings() {
		// Case 1

		DataSourceHandle dsHandle = designHandle.findDataSource("Data Source3");//$NON-NLS-1$
		List propertyBindings = dsHandle.getPropertyBindings();

		assertEquals(4, propertyBindings.size());
		for (int i = 0; i < propertyBindings.size(); ++i) {
			PropertyBinding binding = (PropertyBinding) propertyBindings.get(i);
			switch (i) {
			case 0:
				assertEquals("odaDriverClass", binding.getName()); //$NON-NLS-1$
				assertEquals("driver class", binding.getValue());//$NON-NLS-1$
				break;
			case 1:
				assertEquals("odaURL", binding.getName()); //$NON-NLS-1$
				assertEquals("parent url", binding.getValue());//$NON-NLS-1$
				break;
			case 2:
				assertEquals("odaUser", binding.getName()); //$NON-NLS-1$
				assertEquals("params[\"url\"]", binding.getValue());//$NON-NLS-1$
				break;
			case 3:
				assertEquals("odaPassword", binding.getName()); //$NON-NLS-1$
				assertEquals("params[\"password\"]", binding.getValue()); //$NON-NLS-1$
				break;
			default:
				break;
			}
		}

		// Case 2

		dsHandle = designHandle.findDataSource("Data Source1");//$NON-NLS-1$
		propertyBindings = dsHandle.getPropertyBindings();

		assertEquals(5, propertyBindings.size());
		for (int i = 0; i < propertyBindings.size(); ++i) {
			PropertyBinding binding = (PropertyBinding) propertyBindings.get(i);
			switch (i) {
			case 0:
				assertEquals("odaUser", binding.getName()); //$NON-NLS-1$
				assertEquals("oda user 5", binding.getValue());//$NON-NLS-1$
				break;
			case 1:
				assertEquals("odaDriverClass", binding.getName()); //$NON-NLS-1$
				assertEquals("driver class5", binding.getValue());//$NON-NLS-1$
				break;
			case 2:
				assertEquals("odaURL", binding.getName()); //$NON-NLS-1$
				assertEquals("url 5", binding.getValue());//$NON-NLS-1$
				break;
			case 3:
				assertEquals("odaPassword", binding.getName()); //$NON-NLS-1$
				assertEquals("password 5", binding.getValue()); //$NON-NLS-1$
				break;
			case 4:
				assertEquals("odaJndiName", binding.getName()); //$NON-NLS-1$
				assertEquals("Jndi Name5", binding.getValue()); //$NON-NLS-1$
				break;
			default:
				break;
			}
		}

		// Case 3

		dsHandle = designHandle.findDataSource("Data Source6");//$NON-NLS-1$
		propertyBindings = dsHandle.getPropertyBindings();

		assertEquals(5, propertyBindings.size());
		for (int i = 0; i < propertyBindings.size(); ++i) {
			PropertyBinding binding = (PropertyBinding) propertyBindings.get(i);
			switch (i) {
			case 0:
				assertEquals("odaUser", binding.getName()); //$NON-NLS-1$
				assertEquals("params[\"url\"]", binding.getValue());//$NON-NLS-1$
				break;
			case 1:
				assertEquals("odaJndiName", binding.getName()); //$NON-NLS-1$
				assertEquals("JndiName", binding.getValue()); //$NON-NLS-1$
				break;
			case 2:
				assertEquals("odaDriverClass", binding.getName()); //$NON-NLS-1$
				assertEquals("driver class", binding.getValue()); //$NON-NLS-1$
				break;
			case 3:
				assertEquals("odaURL", binding.getName()); //$NON-NLS-1$
				assertEquals("parent url", binding.getValue()); //$NON-NLS-1$
				break;
			case 4:
				assertEquals("odaPassword", binding.getName()); //$NON-NLS-1$
				assertEquals("params[\"password\"]", binding.getValue()); //$NON-NLS-1$
				break;
			default:
				break;
			}
		}

	}

	/**
	 * Teset getPropertyBinding method. Three cases in this test case:
	 * <tr>
	 * <td>Case 1</td>
	 * <td>only library has such property binding</td>
	 * </tr>
	 * <tr>
	 * <td>Case 2</td>
	 * <td>design file and library have the same property binding</td>
	 * </tr>
	 * <tr>
	 * <td>Case 3</td>
	 * <td>only report has such property binding</td>
	 * </tr>
	 */

	public void testGetPropertyBinding() {
		// Case 1

		DataSourceHandle dsHandle = designHandle.findDataSource("Data Source3");//$NON-NLS-1$
		String value = dsHandle.getPropertyBinding("odaUser");//$NON-NLS-1$
		assertEquals("params[\"url\"]", value);//$NON-NLS-1$

		// Case 2

		dsHandle = designHandle.findDataSource("Data Source1");//$NON-NLS-1$
		value = dsHandle.getPropertyBinding("odaUser");//$NON-NLS-1$
		assertEquals("oda user 5", value);//$NON-NLS-1$

		// Case 3

		dsHandle = designHandle.findDataSource("Data Source6");//$NON-NLS-1$
		value = dsHandle.getPropertyBinding("odaJndiName");//$NON-NLS-1$
		assertEquals("JndiName", value);//$NON-NLS-1$

	}

}
