/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.nls;

import junit.framework.TestCase;

/**
 * Class of test for Messages
 * 
 */

public class MessagesTest extends TestCase {

	public void testGetString() {

		assertEquals("SUN", Messages.getString("Commom.ShortDateTime.Sun"));
		assertEquals("banian", Messages.getString("banian"));

	}

	public void testGetXMLKey() {
	}

}