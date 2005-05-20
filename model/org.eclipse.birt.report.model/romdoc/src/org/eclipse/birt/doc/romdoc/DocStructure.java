/*
 * Created on May 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DocStructure extends DocComposite
{
	public DocStructure( StructureDefn struct )
	{
		super( struct );

		Iterator iter = struct.propertiesIterator( );
		while ( iter.hasNext( ) )
		{
			PropertyDefn propDefn = (PropertyDefn) iter.next( );
			properties.add( new DocProperty( propDefn ) );
		}
		Collections.sort( properties, new DocComparator( ) );
	}

	public boolean isElement( )
	{
		return false;
	}

}
