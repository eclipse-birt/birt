
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.document.stream;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * Class DummyOutputStream is deprecated
 */

@Ignore("Class DummyOutputStream is deprecated")
public class DummyOutputStreamTest {

	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGetContent() throws IOException {
		byte[] b = new byte[12345];
		Arrays.fill(b, (byte) 3);
		DummyOutputStream stream = new DummyOutputStream(null, null, 0);
		stream.write(b);
		assertEquals(b.length, stream.toByteArray().length);

		assertTrue(Arrays.equals(b, stream.toByteArray()));
	}

}
