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

package org.eclipse.birt.chart.tests.engine.model.attribute;

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.Anchor;

public class AnchorTest extends TestCase
{

	public void testConstant( )
	{
		assertEquals( Anchor.NORTH, 0 );
		assertEquals( Anchor.NORTH_EAST, 1 );
		assertEquals( Anchor.EAST, 2 );
		assertEquals( Anchor.SOUTH_EAST, 3 );
		assertEquals( Anchor.SOUTH, 4 );
		assertEquals( Anchor.SOUTH_WEST, 5 );
		assertEquals( Anchor.WEST, 6 );
		assertEquals( Anchor.NORTH_WEST, 7 );
	}

	public void testGet( )
	{
		assertEquals( Anchor.NORTH_LITERAL, Anchor.get( Anchor.NORTH ) );
		assertEquals( Anchor.NORTH_EAST_LITERAL, Anchor.get( Anchor.NORTH_EAST ) );
		assertEquals( Anchor.EAST_LITERAL, Anchor.get( Anchor.EAST ) );
		assertEquals( Anchor.SOUTH_EAST_LITERAL, Anchor.get( Anchor.SOUTH_EAST ) );
		assertEquals( Anchor.SOUTH_LITERAL, Anchor.get( 4 ) );
		assertEquals( Anchor.SOUTH_WEST_LITERAL, Anchor.get( 5 ) );
		assertEquals( Anchor.WEST_LITERAL, Anchor.get( 6 ) );
		assertEquals( Anchor.NORTH_WEST_LITERAL, Anchor.get( 7 ) );
		assertNull( Anchor.get( "8" ) ); //$NON-NLS-1$

		assertEquals( Anchor.NORTH_LITERAL, Anchor.get( "North" ) );//$NON-NLS-1$
		assertEquals( Anchor.NORTH_EAST_LITERAL, Anchor.get( "North_East" ) );//$NON-NLS-1$
		assertEquals( Anchor.EAST_LITERAL, Anchor.get( "East" ) );//$NON-NLS-1$
		assertEquals( Anchor.SOUTH_EAST_LITERAL, Anchor.get( "South_East" ) );//$NON-NLS-1$
		assertEquals( Anchor.SOUTH_LITERAL, Anchor.get( "South" ) );//$NON-NLS-1$
		assertEquals( Anchor.SOUTH_WEST_LITERAL, Anchor.get( "South_West" ) );//$NON-NLS-1$
		assertEquals( Anchor.WEST_LITERAL, Anchor.get( "West" ) );//$NON-NLS-1$
		assertEquals( Anchor.NORTH_WEST_LITERAL, Anchor.get( "North_West" ) );//$NON-NLS-1$
		assertNull( Anchor.get( "No Match" ) );//$NON-NLS-1$
	}

}
