package utility;
import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

public class BaseTestCase extends TestCase
{

	protected static final String TEST_FOLDER = "src/"; //$NON-NLS-1$

	protected String getTestFolder( )
	{
		return TEST_FOLDER;
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder( )
	{

		String pathBase = null;

		ProtectionDomain domain = this.getClass( ).getProtectionDomain( );
		if ( domain != null )
		{
			CodeSource source = domain.getCodeSource( );
			if ( source != null )
			{
				URL url = source.getLocation( );
				pathBase = url.getPath( );

				if ( pathBase.endsWith( "bin/" ) ) //$NON-NLS-1$
					pathBase = pathBase.substring( 0, pathBase.length( ) - 4 );
				if ( pathBase.endsWith( "bin" ) ) //$NON-NLS-1$
					pathBase = pathBase.substring( 0, pathBase.length( ) - 3 );
			}
		}

		pathBase = pathBase + getTestFolder( );
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = pathBase + className.replace( '.', '/' );

		return className;
	}

	/**
	 * Returns a list of class objects under the given package.
	 * 
	 * @param packageName
	 *            a given Java package
	 * @return a list of class objects under the given package.
	 */

	protected List getClasses( String packageName )
	{
		List classes = new ArrayList( );

		String pkgFolder = getClassFolder( )
				+ "/" + packageName.substring( packageName.lastIndexOf( '.' ) + 1 ); //$NON-NLS-1$
		File directory  = new File( pkgFolder );

		if ( directory.exists( ) )
		{
			// Get the list of the files contained in the package
			String[] files = directory.list( );
			for ( int i = 0; i < files.length; i++ )
			{
				// we are only interested in .class files
				if ( files[i].endsWith( ".java" ) ) //$NON-NLS-1$
				{
					// removes the .class extension

					classes.add( packageName + "."
							+ files[i].substring( 0, files[i].length( ) - 5 ) );
				}
			}
		}

		return classes;
	}
	
	/*
	 * A null-test for suppressing warnings in the build
	 */
	public void testNullTest( )
	{
		assertTrue( true );
	}
	
}
