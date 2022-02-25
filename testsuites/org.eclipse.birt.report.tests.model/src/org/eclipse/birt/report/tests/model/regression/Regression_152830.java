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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScriptLibHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Need model's support to add jar files
 * <p>
 * Test description:
 * <p>
 * Make sure Model support the followings: Add 'scriptLibs' property in module
 * which detaitype is 'ScriptLib'. In moduleHandle supports API 'addScriptLib' ,
 * 'dropScriptLib' , ''dropAllScriptLibs' , 'getAllScriptLibs','findScriptLib'
 * ,'scriptLibsIterator' 'shiftScriptLib' method.
 * </p>
 */
public class Regression_152830 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */
	public void test_regression_152830() throws SemanticException {
		ReportDesignHandle designHandle = this.createDesign();

		ScriptLib lib = StructureFactory.createScriptLib();
		lib.setName("xcers.jar"); //$NON-NLS-1$
		designHandle.addScriptLib(lib);

		Iterator iter = designHandle.scriptLibsIterator();
		assertEquals("xcers.jar", ((ScriptLibHandle) iter.next()).getName()); //$NON-NLS-1$

		designHandle.dropScriptLib(lib);
		assertFalse(designHandle.scriptLibsIterator().hasNext());

	}
}
