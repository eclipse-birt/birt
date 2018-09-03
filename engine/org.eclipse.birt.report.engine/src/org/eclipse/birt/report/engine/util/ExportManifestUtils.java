package org.eclipse.birt.report.engine.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ExportManifestUtils
{

	static final String[] API_JAR_PATTERNS = new String[]{
		"com.ibm.icu.*.jar",
		"org.apache.commons.codec_.*.jar", 
		"flute.jar",
		"js.jar",
		"org.eclipse.emf.common_.*.jar",
		"org.eclipse.emf.ecore.xmi_.*.jar", 
		"org.eclipse.emf.ecore_.*.jar",
		"org.w3c.css.sac_.*.jar", 
		"javax.servlet_.*.jar",
		"javax.servlet.jsp_.*.jar",
		"org.eclipse.birt.report.engine.dataextraction_*.jar",
		"emitterconfig.jar"
	};

	static public void main( String[] args ) throws IOException
	{
		String jarFolder = ".";
		if ( args.length > 0 )
		{
			jarFolder = args[0];
		}
		File[] jarFiles = new File( jarFolder ).listFiles( );
		for ( int i = 0; i < jarFiles.length; i++ )
		{
			if ( isApiJar( jarFiles[i] ) )
			{
				exportPackages( jarFiles[i] );
			}
		}
	}

	static boolean isApiJar( File jarFile )
	{
		String name = jarFile.getName( );
		for ( int i = 0; i < API_JAR_PATTERNS.length; i++ )
		{
			if ( name.matches( API_JAR_PATTERNS[i] ) )
			{
				return true;
			}
		}
		return false;
	}

	static void exportPackages( File jarFile ) throws IOException
	{
		System.out.println( "#" + jarFile.getName( ) );
		JarFile jar = new JarFile( jarFile );
		Entry root = new Entry( );
		Enumeration entries = jar.entries( );
		while ( entries.hasMoreElements( ) )
		{
			JarEntry entry = (JarEntry) entries.nextElement( );
			createEntry( root, entry );
		}
		for ( int i = 0; i < root.children.size( ); i++ )
		{
			outputEntry( (Entry) root.children.get( i ), "" );
		}
	}

	static class Entry
	{

		String name;
		boolean hasFiles;
		ArrayList children = new ArrayList( );
	}

	static void outputEntry( Entry entry, String prefix )
	{
		if ( entry.hasFiles )
		{
			System.out.println( " " + prefix + "." + entry.name + "," );
		}

		if ( prefix != null && prefix.length( ) != 0 )
		{
			prefix = prefix + "." + entry.name;
		}
		else
		{
			prefix = entry.name;
		}
		for ( int i = 0; i < entry.children.size( ); i++ )
		{
			outputEntry( (Entry) entry.children.get( i ), prefix );
		}
	}

	static void createEntry( Entry root, JarEntry entry )
	{
		if ( entry.isDirectory( ) )
		{
			String name = entry.getName( );
			String[] names = name.split( "/" );
			for ( int i = 0; i < names.length; i++ )
			{
				root = createEntry( root, names[i] );
			}
		}
		else
		{
			String name = entry.getName( );
			String[] names = name.split( "/" );
			for ( int i = 0; i < names.length - 1; i++ )
			{
				root = createEntry( root, names[i] );
			}
			root.hasFiles = true;
		}
	}

	static Entry createEntry( Entry parent, String name )
	{

		for ( int i = 0; i < parent.children.size( ); i++ )
		{
			Entry entry = (Entry) parent.children.get( i );
			if ( name.equals( entry.name ) )
			{
				return entry;
			}
		}
		Entry entry = new Entry( );
		entry.name = name;
		parent.children.add( entry );
		return entry;
	}
}
