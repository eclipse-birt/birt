/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.util;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.data.oda.pojo.input.forclasslister.C1;
import org.eclipse.birt.data.oda.pojo.util.ClassLister;
import org.eclipse.core.runtime.FileLocator;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;


/**
 * 
 */

public class ClassListerTest {
	@SuppressWarnings("nls")
	@Test
    public void testListPublicClasses( ) throws IOException
	{
		URL classFolderURL = C1.class.getResource( "." );
		if ( !classFolderURL.getProtocol( ).equals( "file" ))
		{
			classFolderURL = FileLocator.resolve( classFolderURL );
		}
		String[] classes = ClassLister.listClasses( new URL[]{classFolderURL} );
		Set<String> cs = new HashSet<String>( Arrays.asList( classes ) );
		assertEquals( cs.size( ), 3 );
		assertTrue( cs.contains( "C1" ));
		assertTrue( cs.contains( "inner.C1" ));
		assertTrue( cs.contains( "inner.C1.C2" ));

	}

}
