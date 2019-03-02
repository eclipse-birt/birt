
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
    public void testGetContent( ) throws IOException
	{
		byte[] b = new byte[12345];
		Arrays.fill( b, (byte)3 );
		DummyOutputStream stream = new DummyOutputStream( null, null ,0);
		stream.write( b );
		assertEquals( b.length, stream.toByteArray( ).length);
		
		assertTrue( Arrays.equals( b, stream.toByteArray( ) ) );
	}

}
