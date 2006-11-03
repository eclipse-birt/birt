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

package org.eclipse.birt.report.model;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.extension.PeerExtensionTest;
import org.eclipse.birt.report.model.extension.ReportItemExtensionTest;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests cases run in the build script.
 */

public class AllTests extends BaseTestCase
{

	/**
	 * @return
	 */

	public static Test suite( )
	{
		AllTests creator = new AllTests( );
		List tmpClasses = creator.createCases( );

		TestSuite test = new TestSuite( );
		for ( int i = 0; i < tmpClasses.size( ); i++ )
		{
			try
			{
				String className = (String) tmpClasses.get( i );
				if ( className.endsWith( "AllTests" ) ) //$NON-NLS-1$
					continue;

				Class clazz = Class.forName( className );

				int modifier = clazz.getModifiers( );

				if ( Modifier.isAbstract( modifier )
						|| !Modifier.isPublic( modifier ) )
					continue;
				
				test.addTestSuite( clazz );
			}
			catch ( ClassNotFoundException e )
			{
				assert false;
			}
		}
		
		// add peerextensiontest and reportitemextension test
		test.addTestSuite( PeerExtensionTest.class );
		test.addTestSuite( ReportItemExtensionTest.class );

		return test;
	}

	/**
	 * Returns all class names in the test directories.
	 * 
	 * @return a list containing all cases.
	 */

	private List createCases( )
	{
		String pkgPrefix = "org.eclipse.birt.report.model"; //$NON-NLS-1$

		List tmpClasses = new ArrayList( );
		tmpClasses.addAll( getClasses( "activity", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "api", pkgPrefix ) ); //$NON-NLS-1$ 
		tmpClasses.addAll( getClasses( "command", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "core", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "css", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "element", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "i18n", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "library", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "metadata", pkgPrefix ) ); //$NON-NLS-1$		
		tmpClasses.addAll( getClasses( "parser", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "script", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "util", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "validators", pkgPrefix ) ); //$NON-NLS-1$
		tmpClasses.addAll( getClasses( "writer", pkgPrefix ) ); //$NON-NLS-1$

		return tmpClasses;

	}

	private List getClasses( String pckgname, String pkgPrefix )
	{
		List classes = new ArrayList( );

		// Get a File object for the package
		File directory = null;

		String path = pckgname.replace( '.', '/' );

		String pkgFolder = getClassFolder( );
		directory = new File( pkgFolder, path );

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

					classes.add( pkgPrefix + '.' + pckgname + '.'
							+ files[i].substring( 0, files[i].length( ) - 5 ) );
				}
			}
		}

		return classes;
	}
}
