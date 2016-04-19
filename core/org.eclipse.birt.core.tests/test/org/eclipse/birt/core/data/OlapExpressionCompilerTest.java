
package org.eclipse.birt.core.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.eclipse.birt.core.exception.CoreException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class OlapExpressionCompilerTest
{
	@Test
    public void testGetDimLevels( ) throws CoreException
    {
        //assertEquals( "dim1", getDimLevels( "dimension[\"dim1\"]" ) );
        assertEquals( "dim1/level1", getDimLevels( "dimension['dim1']['level1']" ) );
        assertEquals( "dim1/level1/attr1", getDimLevels( "dimension['dim1']['level1']['attr1']" ) );
        assertEquals( "dim1/level1,dim2/level2", getDimLevels( "dimension['dim1']['level1'] * dimension['dim2']['level2']" ) );
        assertEquals( "dim1/level1", getDimLevels( "func(dimension['dim1']['level1'])" ) );
    }
	@Test
    public void testGetReferencedMeasures( )
    {
        assertEquals( "m1", getMeasureNames( "measure.m1" ) );
        assertEquals( "m1", getMeasureNames( "measure[\"m1\"]" ) );
        assertEquals( "m1", getMeasureNames( "measure['m1']" ) );
        assertEquals( "m1,m2", getMeasureNames( "measure[\"m1\"] + measure[\"m2\"]" ) );
        assertEquals( "m1,m2", getMeasureNames( "measure[\"m1\"] + measure[\"m2\"] //measure[\"m3\"] " ) );
        assertEquals( "m1", getMeasureNames( "global.test(measure[\"m1\"]) " ) );
    }

    private String getDimLevels( String expr ) throws CoreException
    {
        Set<IDimLevel> levels = OlapExpressionCompiler.getReferencedDimLevel( expr );
        return toString( levels );
    }

    private String getMeasureNames( String expr )
    {
        Set<String> measures = OlapExpressionCompiler.getReferencedMeasure( expr );

        return toString( measures );

    }

    private String toString( Collection<String> values )
    {
        StringBuilder sb = new StringBuilder( );
        if ( values.size( ) > 0 )
        {
            String[] lines = values.toArray( new String[values.size( )] );
            Arrays.sort( lines );
            for ( String line : lines )
            {
                sb.append( line );
                sb.append( "," );
            }
            sb.setLength( sb.length( ) - 1 );
        }
        return sb.toString( );
    }

    private String toString( IDimLevel dim )
    {
        StringBuilder sb = new StringBuilder( );
        sb.append( dim.getDimensionName( ) );
        if ( dim.getLevelName( ) != null )
        {
            sb.append( "/" );
            sb.append( dim.getLevelName( ) );
        }

        if ( dim.getAttrName( ) != null )
        {
            sb.append( "/" );
            sb.append( dim.getAttrName( ) );
        }
        return sb.toString( );
    }

    private String toString( Set<IDimLevel> dims )
    {
        ArrayList<String> lines = new ArrayList<String>( );
        for ( IDimLevel dim : dims )
        {
            lines.add( toString( dim ) );
        }
        return toString( lines );
    }

}
