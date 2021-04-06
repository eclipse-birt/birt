/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Regression description:</b>
 * <p>
 * Notify resource (Library) changes.
 * <p>
 * Need session level notification because add/remove moduleHandle is kind of
 * resource change.
 * <p>
 * For example, user export a library to resource path. This causes a library
 * add action in SessionHandle. GUI need way to listen on this changes to
 * refresh GUI side accordingly.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test that the following method works fine:
 * <ol>
 * <li>Added IResourceChangeListener, LibraryChangeEvent.
 * <li>Provide fireResourceChange(ResourceChangeEvent ev) on SessionHandle.
 * <li>Added API reloadLibraries( ), reloadLibrary( String path ) on
 * ModuleHandle.
 * </ol>
 * <p>
 * Also, make sure that add/remove module handle on session will fire an event
 * notifying that resource changes.
 */
public class Regression_151661 extends BaseTestCase {

	private final static String LIBRARY = "regression_151661_lib.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( LIBRARY , LIBRARY );
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_151661() throws DesignFileException, SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);

		ResourceChangeListener libExplorer = new ResourceChangeListener();
		session.openLibrary(getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY);
		session.addResourceChangeListener(libExplorer);

		session.fireResourceChange(new LibraryChangeEvent(LIBRARY));

		assertTrue(libExplorer.notified);
		assertEquals(LIBRARY, libExplorer.event.getChangedResourcePath()); // $NON-NLS-1$
	}
}

class ResourceChangeListener implements IResourceChangeListener {

	ModuleHandle module = null;
	ResourceChangeEvent event = null;
	boolean notified = false;

	public void resourceChanged(ModuleHandle module, ResourceChangeEvent event) {
		this.module = module;
		this.event = event;
		this.notified = true;
	}

	void reset() {
		this.module = null;
		this.event = null;
		this.notified = false;
	}
}
