/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

/**
 * A URL class loader with close API.
 * 
 * Java's URL class loader locks the JAR file it loaded until the JVM exit. It
 * may cause some problem when we need remove the JARs if the class loader are
 * not used any more.
 * 
 * BIRT's URL class loader add a new close() method to close the JAR files
 * explicitly. Once the close() is called, the user can't use the class and the
 * loaded classes any more.
 * 
 */
public class URLClassLoader extends java.net.URLClassLoader
{

	private static Logger logger = Logger.getLogger( URLClassLoader.class
			.getName( ) );

	private List<URL> urls = new LinkedList<URL>( );
	private ArrayList<Loader> loaders;

	public URLClassLoader( URL[] urls )
	{
		super( new URL[]{} );

		initURLs( urls );

		loaders = new ArrayList<Loader>( urls.length );
		for ( int i = 0; i < urls.length; i++ )
		{
			Loader loader = createLoader( urls[i] );
			if ( loader != null )
			{
				loaders.add( loader );
			}
		}
	}

	private void initURLs( URL[] urls )
	{
		for ( URL url : urls )
		{
			this.urls.add( url );
		}
	}

	public URLClassLoader( URL[] urls, ClassLoader parent )
	{
		super( new URL[]{}, parent );

		initURLs( urls );

		loaders = new ArrayList<Loader>( urls.length );
		for ( int i = 0; i < urls.length; i++ )
		{
			Loader loader = createLoader( urls[i] );
			if ( loader != null )
			{
				loaders.add( loader );
			}
		}
	}

	public void close( )
	{
		if ( loaders != null )
		{
			for ( Loader loader : loaders )
			{
				try
				{
					loader.close( );
				}
				catch ( IOException ex )
				{
				}
			}
			loaders = null;
		}
	}

	public void addURL( URL url )
	{
		if ( url == null || this.urls.contains( url ) )
			return;
		this.urls.add( url );
		Loader loader = createLoader( url );
		if ( loader != null )
		{
			loaders.add( loader );
		}
	}

	public URL[] getURLs( )
	{
		return this.urls.toArray( new URL[0] );
	}

	protected Class<?> findClass( final String name )
			throws ClassNotFoundException
	{
		if ( loaders == null )
		{
			throw new ClassNotFoundException( name );
		}
		String path = name.replace( '.', '/' ).concat( ".class" );
		try
		{
			Resource res = loadResource( path );
			if ( res != null )
			{
				CodeSource cs = res.getCodeSource( );
				byte[] b = res.getBytes( );
				return defineClass( name, b, 0, b.length, cs );
			}
		}
		catch ( IOException e )
		{
			throw new ClassNotFoundException( name, e );
		}
		throw new ClassNotFoundException( name );
	}

	public URL findResource( final String name )
	{
		if ( loaders != null )
		{
			for ( Loader loader : loaders )
			{
				try
				{
					URL url = loader.findResource( name );
					if ( url != null )
					{
						return url;
					}
				}
				catch ( IOException ex )
				{
				}
			}
		}
		return null;
	}

	public Enumeration<URL> findResources( final String name )
			throws IOException
	{
		Vector<URL> urls = new Vector<URL>( );
		if ( loaders != null )
		{
			for ( Loader loader : loaders )
			{
				try
				{
					URL url = loader.findResource( name );
					if ( url != null )
					{
						urls.add( url );
					}
				}
				catch ( IOException ex )
				{

				}
			}
		}
		return urls.elements( );
	}

	private Resource loadResource( final String name ) throws IOException
	{
		for ( Loader loader : loaders )
		{
			Resource resource = loader.loadResource( name );
			if ( resource != null )
			{
				return resource;
			}
		}
		return null;
	}

	abstract static class Resource
	{

		abstract CodeSource getCodeSource( );

		abstract byte[] getBytes( ) throws IOException;
	}

	static abstract class Loader
	{

		abstract URL findResource( String name ) throws IOException;

		abstract Resource loadResource( String name ) throws IOException;

		abstract void close( ) throws IOException;
	}

	static class UrlLoader extends Loader
	{

		CodeSource codeSource;
		URL baseUrl;

		UrlLoader( URL url )
		{
			baseUrl = url;
			codeSource = new CodeSource( url, (CodeSigner[]) null );
		}

		void close( ) throws IOException
		{
		}

		URL findResource( String name ) throws IOException
		{
			URL url = new URL( baseUrl, name );
			URLConnection conn = url.openConnection( );
			if ( conn instanceof HttpURLConnection )
			{
				HttpURLConnection hconn = (HttpURLConnection) conn;
				hconn.setRequestMethod( "HEAD" );
				if ( hconn.getResponseCode( ) >= HttpURLConnection.HTTP_BAD_REQUEST )
				{
					return null;
				}
			}
			else
			{
				// our best guess for the other cases
				InputStream is = url.openStream( );
				is.close( );
			}
			return url;
		}

		Resource loadResource( String name ) throws IOException
		{
			URL url = new URL( baseUrl, name );
			InputStream in = url.openStream( );
			try
			{
				final byte[] bytes = loadStream( in );
				return new Resource( ) {

					byte[] getBytes( )
					{
						return bytes;
					};

					CodeSource getCodeSource( )
					{
						return codeSource;
					}
				};
			}
			finally
			{
				in.close( );
			}
		}
	}

	static class JarLoader extends Loader
	{

		URL baseUrl;
		URL jarUrl;
		JarFile jarFile;

		JarLoader( URL url ) throws IOException
		{
			baseUrl = url;
			jarUrl = new URL( "jar", "", -1, baseUrl + "!/" );
			if ( baseUrl.getProtocol( ).equalsIgnoreCase( "file" ) )
			{
				String filePath = getFilePath( baseUrl );
				jarFile = new JarFile( filePath );
			}
			else
			{
				JarURLConnection jarConn = (JarURLConnection) jarUrl
						.openConnection( );
				jarFile = jarConn.getJarFile( );
			}
		}

		private String getFilePath( URL url )
		{
			String path = url.getFile( ).replace( '/', '\\' );
			try
			{
				return URLDecoder.decode( path, "utf-8" );
			}
			catch ( UnsupportedEncodingException ex )
			{
				return path;
			}
		}

		public void close( ) throws IOException
		{
			if ( jarFile != null )
			{
				jarFile.close( );
				jarFile = null;
			}
		}

		URL findResource( String name ) throws IOException
		{
			if ( jarFile != null )
			{
				ZipEntry entry = jarFile.getEntry( name );
				if ( entry != null )
				{
					return new URL( jarUrl, name, new JarEntryHandler( entry ) );
				}
			}
			return null;
		}

		Resource loadResource( String name ) throws IOException
		{
			// first test if the jar file exist
			if ( jarFile != null )
			{
				final JarEntry entry = jarFile.getJarEntry( name );
				if ( entry != null )
				{
					InputStream in = jarFile.getInputStream( entry );
					try
					{
						final byte[] bytes = loadStream( in );
						return new Resource( ) {

							byte[] getBytes( )
							{
								return bytes;
							};

							CodeSource getCodeSource( )
							{
								return new CodeSource( baseUrl, entry
										.getCodeSigners( ) );
							}
						};
					}
					finally
					{
						in.close( );
					}
				}
			}
			return null;
		}

		private class JarEntryHandler extends URLStreamHandler
		{

			private ZipEntry entry;

			JarEntryHandler( ZipEntry entry )
			{
				this.entry = entry;
			}

			protected URLConnection openConnection( URL u ) throws IOException
			{
				return new URLConnection( u ) {

					public void connect( ) throws IOException
					{
					}

					public int getContentLength( )
					{
						return (int) entry.getSize( );
					}

					public InputStream getInputStream( ) throws IOException
					{
						if ( jarFile != null )
						{
							return jarFile.getInputStream( entry );
						}
						throw new IOException( "ClassLoader has been closed" );
					}
				};
			}
		}
	}

	static class FileLoader extends Loader
	{

		URL baseUrl;
		File baseDir;
		CodeSource codeSource;

		FileLoader( URL url )
		{
			baseUrl = url;
			baseDir = new File( url.getFile( ) );
			codeSource = new CodeSource( baseUrl, (CodeSigner[]) null );
		}

		void close( ) throws IOException
		{

		}

		URL findResource( String name ) throws IOException
		{
			File file = new File( baseDir, name.replace( '/',
					File.separatorChar ) );
			if ( file.exists( ) && file.isFile( ) )
			{
				return file.toURI( ).toURL( );
			}
			return null;
		}

		Resource loadResource( String name ) throws IOException
		{
			File file = new File( baseDir, name.replace( '/',
					File.separatorChar ) );
			if ( file.exists( ) )
			{
				FileInputStream in = new FileInputStream( file );
				try
				{
					final byte[] bytes = loadStream( in );
					return new Resource( ) {

						public byte[] getBytes( )
						{
							return bytes;
						};

						CodeSource getCodeSource( )
						{
							return codeSource;
						}
					};
				}
				finally
				{
					in.close( );
				}
			}
			return null;
		}
	}

	static Loader createLoader( URL url )
	{
		try
		{

			String file = url.getFile( );
			if ( file != null && file.endsWith( "/" ) )
			{
				if ( "file".equals( url.getProtocol( ) ) )
				{
					return new FileLoader( url );
				}
				return new UrlLoader( url );
			}
			return new JarLoader( url );
		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, "can't load the class from " + url, ex );
			return null;
		}
	}

	static byte[] loadStream( InputStream in ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( in.available( ) );
		byte[] bytes = new byte[1024];
		int readSize = in.read( bytes );
		while ( readSize != -1 )
		{
			out.write( bytes, 0, readSize );
			readSize = in.read( bytes );
		}
		return out.toByteArray( );
	}
}