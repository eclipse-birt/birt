/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.build;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Update Birt feature.xml during nightly build, specify a directory that
 * contains a "feature.xml" under it, also specify the directory of the plugin
 * home.
 * <p>
 * Firstly, the updater task will parse over the "feature.xml" to collect a set
 * of the including plugins(id(s) of the plugins).
 * <p>
 * Secondly, the task will scan over the plugin directory; for each plugin
 * included in the feature.xml, retrieve its new version number which suffixed
 * with a the time stamp. Build a map that pairs plugin id to its new version
 * number.
 * <p>
 * Finally, the task starts to update the "feature.xml", each tag node
 * <em>&ltplugin&gt</em> will be update, value of <em>version</em> attribute
 * will be replaced with the new one.
 * <p>
 * For example:
 * <p>
 * feature.xml include a plugin described bellow:
 * 
 *
 * <p>
 * From the ${eclipse.home}\plugin we find that new version. After execution the
 * feature.xml will be updated as:
 * 
 *
 * @author Rock Yu
 * 
 */

public class FeatureUpdater extends Task
{

	File featureDir = null;

	File pluginDir = null;

	String timeStamp = null;

	String packageId = null;

	File featureXML = null;

	final static String VERSION_PLACEHOLDER = "0.0.0"; //$NON-NLS-1$

	/**
	 * List of plugin names in the feature xml file, if any item in the list is
	 * a null object, that means the plugin for the index contain wrong if
	 * formation, and will not be treated duing replacement.
	 */

	List pluginsInFeature = new ArrayList( );

	/**
	 * Map that stores the updated version of each plugin.
	 */

	Map versionMap = new HashMap( );

	/**
	 * Set the directory of the feature project, "feature.xml" should be put
	 * under the directory.
	 * 
	 * @param featureDir
	 */

	public void setProjectPath( File featureDir )
	{
		this.featureDir = featureDir;
		this.featureXML = new File( featureDir, "feature.xml" ); //$NON-NLS-1$
	}


	public void setPackageId( String PackageId )
	{
		this.packageId = PackageId;
	}

	/**
	 * Set the plugin directory, this task will update the plugin version in the
	 * feature.xml against the plugins under the "pluginDir".
	 * 
	 * @param pluginDir
	 */

	public void setPluginDir( File pluginDir )
	{
		this.pluginDir = pluginDir;
	}

	/**
	 * Set the timestamp, feature.xml will be updated with the given time stamp.
	 * 
	 * @param timeStamp
	 */

	public void setTimeStamp( String timeStamp )
	{
		this.timeStamp = timeStamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */

	public void execute( ) throws BuildException
	{
		if ( featureDir == null )
			throw new BuildException( "Specify the manatory \"dir\" attribute." ); //$NON-NLS-1$

		if ( this.pluginDir == null )
			throw new BuildException(
					"Specify the manatory \"pluginDir\" attribute." ); //$NON-NLS-1$

		if ( !featureXML.exists( ) )
		{
			throw new BuildException(
					"Missing \"feature.xml\" under " + featureDir.getAbsolutePath( ) ); //$NON-NLS-1$
		}

		// parse the feature.xml, get the plugin list.

		this.parseFeature( featureXML );

		// build a map that maps plugin id to the new version number.

		this.buildVersions( );

		// finally, we use the new version map to update the feature.xml.

		this.updateFeature( );
		this.sanityCheck( );
	}

	/**
	 * Parse the feature xml, collect the plugin ids in the feature.xml, this
	 * method will store the plugin ids in a list <code>pluginsInFeature</code>,
	 * plugin that matches is tagged with <plugin> or <includes> .
	 * 
	 * @param feature
	 *            feature file.
	 */

	void parseFeature( File feature )
	{

		DocumentBuilder builder = null;
		Document doc = null;

		try
		{
			builder = DocumentBuilderFactory.newInstance( )
					.newDocumentBuilder( );
			doc = builder.parse( feature );
		}
		catch ( Exception e )
		{
			handleErrorOutput( "Error occur when parsing feature file: " + feature ); //$NON-NLS-1$
			e.printStackTrace( );

			throw new BuildException( e );
		}

		handleOutput( "Start parsing feature.xml[" + feature.getAbsolutePath( ) + "]....... " ); //$NON-NLS-1$ //$NON-NLS-2$

		List matchingNodes = new ArrayList( );
		NodeList features = doc.getElementsByTagName( "feature" ); //$NON-NLS-1$
		if ( null == features || features.getLength( ) != 1 )
		{
			handleOutput( "Wrong feature.xml files, not feature tag or more than one feature tag includes." ); //$NON-NLS-1$
		}

		if ( features == null )
			return;

		Node featureNode = features.item( 0 ); // get the first node.

		NodeList pluginNodes = featureNode.getOwnerDocument( )
				.getElementsByTagName( "plugin" ); //$NON-NLS-1$
		NodeList includesNodes = featureNode.getOwnerDocument( )
				.getElementsByTagName( "includes" ); //$NON-NLS-1$

		for ( int j = 0; j < pluginNodes.getLength( ); j++ )
			matchingNodes.add( pluginNodes.item( j ) );

		for ( int j = 0; j < includesNodes.getLength( ); j++ )
			matchingNodes.add( includesNodes.item( j ) );

		for ( int j = 0, pluginCounter = 0, includesCounter = 0; j < matchingNodes.size( ); j++ )
		{
			Node node = (Node) matchingNodes.get( j );
			
			int counter;
			String tag = node.getNodeName( );
			if( "plugin".equals( StringUtil.trimString( tag ) )) //$NON-NLS-1$
			{
				pluginCounter++;
				counter = pluginCounter;
			}
			else
			{
				includesCounter++;
				counter = includesCounter;
			}
			
			
			String id = node.getAttributes( )
					.getNamedItem( "id" ).getNodeValue( ); //$NON-NLS-1$
			String version = node.getAttributes( )
					.getNamedItem( "version" ).getNodeValue( ); //$NON-NLS-1$

			handleOutput( "Mathcing <" + tag + "[" + counter + "]: id=\"" + id + "\" " + "version=\"" + version + "\"" ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$ //$NON-NLS-5$

			// Version is needed, we use "0.0.0" as placeholder.

			if ( StringUtil.isBlank( version ) )
			{
				handleErrorOutput( "Wrong feature.xml. Plugin :[" + id + "] doesn't specify version value" ); //$NON-NLS-1$//$NON-NLS-2$
				handleErrorOutput( "This plugin node is ignored...." ); //$NON-NLS-1$

				this.pluginsInFeature.add( null );
				continue;
			}

			if ( !VERSION_PLACEHOLDER.equals( version ) )
			{
				handleErrorOutput( "feature.xml may be incorrect. Plugin node [" //$NON-NLS-1$
						+ id + "] should have \"" //$NON-NLS-1$
						+ VERSION_PLACEHOLDER + "\" for its version number." ); //$NON-NLS-1$
				handleErrorOutput( "This plugin node is ignored...." ); //$NON-NLS-1$
				this.pluginsInFeature.add( null );
				continue;
			}

			this.pluginsInFeature.add( id );

		}
	}

	/**
	 * Go over the plugin list in the feature.xml, build a map that maps plugin
	 * id to its new version number.
	 * 
	 */

	private void buildVersions( )
	{
		// list all the plugin folders.

		File[] pluginDirs = this.pluginDir.listFiles( new FileFilter( ) {

			public boolean accept( File pathname )
			{
				return pathname.isDirectory( );
			}
		} );

		// List all plugins in "plugin" folder, map the plugin name to the
		// plugin folder.

		Map pluginIdToFile = new HashMap( );

		for ( int i = 0; i < pluginDirs.length; i++ )
			pluginIdToFile.put( pluginDirs[i].getName( ), pluginDirs[i] );

		// for each plugin node in feature.xml, we locate the plugin directory
		// and retrieve its plugin version.

		for ( Iterator iter = this.pluginsInFeature.iterator( ); iter.hasNext( ); )
		{
			String id = (String) iter.next( );

			// find its corresponding plugin folder.

			File pluginDir = (File) pluginIdToFile.get( id );
			if ( pluginDir == null )
			{
				this.handleErrorOutput( "Plugin [" + id //$NON-NLS-1$
						+ "] in the feature.xml doesn't exist in folder \"" //$NON-NLS-1$
						+ this.pluginDir + "\"" ); //$NON-NLS-1$
				continue;
			}

			String newVersion = BuildUtil.getPluginVersion( pluginDir );
			if ( StringUtil.isBlank( newVersion ) )
				throw new BuildException(
						"We can not identify the plugin version for plugin \"" //$NON-NLS-1$
								+ pluginDir.getAbsolutePath( ) + "\"" ); //$NON-NLS-1$

			this.versionMap.put( id, newVersion );
		}

	}

	void updateFeature( )
	{
		StringBuffer sb = new StringBuffer( );
		BufferedReader reader = null;
		String line = null;
		try
		{
			reader = new BufferedReader( new FileReader( featureXML ) );
			while ( ( line = reader.readLine( ) ) != null )
			{
				sb.append( line );
				sb.append( "\n" ); //$NON-NLS-1$
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
		finally
		{
			try
			{
				reader.close( );
			}
			catch ( IOException e )
			{
				e.printStackTrace( );
				// ignore.
			}
		}

		Matcher qualifierMatcher = Pattern.compile( ".qualifier" ).matcher( sb );
		sb = new StringBuffer( );
		while ( qualifierMatcher.find( ) )
		{
			qualifierMatcher.appendReplacement( sb, this.timeStamp );
		}
		qualifierMatcher.appendTail( sb );

		Matcher pluginMatcher = Pattern.compile( "<includes[\\s]|<plugin[\\s]" )
				.matcher( sb );
		Matcher idMatcher = Pattern.compile( ".*id[\\s]*=[\\s]*\".+\".*" )
				.matcher( sb );
		Matcher versionMatcher = Pattern.compile(
				"version[\\s]*=[\\s]*\"0.0.0\"" ).matcher( sb );
		Matcher xmlEndingMatcher = Pattern.compile( "/>" ).matcher( sb );

		while ( pluginMatcher.find( ) )
		{
            String value = pluginMatcher.group();
			handleOutput( "Find another matching tag: [" + pluginMatcher.group( ) + "]" ); //$NON-NLS-1$//$NON-NLS-2$

			int include_end = pluginMatcher.end( );
			int include_start = pluginMatcher.start( );

			boolean endXML = xmlEndingMatcher.find( include_end );
			assert endXML == true;
			int endXML_pos = xmlEndingMatcher.start( );

			if ( !idMatcher.find( include_start ) )
			{
				handleErrorOutput( "No match for id=\"XX.XX.XX\"... continue with next match" ); //$NON-NLS-1$
				continue;
			}

			int id_start = idMatcher.start( );
			if ( id_start > endXML_pos )
			{
				// wrong id possition, belong to the next plugin tag.
				// continue with the next matcher tag.

				handleErrorOutput( "Wrong id possition, this plugin tag doesn't contain a id attribute..." ); //$NON-NLS-1$
				continue;
			}

			String idStr = idMatcher.group( ).trim( );
			String id = idStr.substring( idStr.indexOf( '"' ) + 1, idStr
					.lastIndexOf( '"' ) );
			String version = (String) versionMap.get( id );

			handleOutput( "The new version for : [" + id + "] is version:[" //$NON-NLS-1$//$NON-NLS-2$
					+ version + "]" ); //$NON-NLS-1$

			if ( !versionMatcher.find( include_end ) )
			{
				handleOutput( "Can not find a matching token version=\"0.0.0\",  [" + id + "]" ); //$NON-NLS-1$ //$NON-NLS-2$
				continue;
			}
			int version_start = versionMatcher.start( );
			int version_end = versionMatcher.end( );

			if ( version_start > endXML_pos )
			{
				handleErrorOutput( "Wrong version possition, this plugin tag doesn't contain a version attribute..." ); //$NON-NLS-1$
				continue;
			}

			String tmpPrefix = this.packageId;
			String tmpTimeStamp = tmpPrefix.concat( timeStamp);
			
			if( value.indexOf("<includes")!=-1){
					sb.replace( version_start, version_end, "version=\"" + tmpTimeStamp
					+ "\"" );
			} else{
					sb.replace( version_start, version_end, "version=\"" + version
					+ "\"" );

			}
			handleOutput( "Update [" + id + "]  with: " + "version=\""
					+ version + "\"" );
		}

		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter( new FileWriter( featureXML ) );
			writer.write( sb.toString( ) );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
		finally
		{
			try
			{
				writer.close( );
			}
			catch ( IOException e )
			{
				e.printStackTrace( );
			}
		}

		System.out.println( sb );
	}

	/**
	 * Sanity check after the updation is finished. For debug purpose.
	 * 
	 */

	private void sanityCheck( )
	{
		DocumentBuilder builder = null;
		Document doc = null;

		try
		{
			builder = DocumentBuilderFactory.newInstance( )
					.newDocumentBuilder( );
			doc = builder.parse( this.featureXML );
		}
		catch ( Exception e )
		{
			handleErrorOutput( "[Sanity]Error occur when parsing feature file: " + featureXML ); //$NON-NLS-1$
		}

		List matchingNodes = new ArrayList( );
		List matchingNodesIncludes = new ArrayList( );

		NodeList features = doc.getElementsByTagName( "feature" ); //$NON-NLS-1$
		if ( null == features || features.getLength( ) != 1 )
		{
			handleErrorOutput( "[Sanity]failed to to incorrect feature file, missing feature tag." ); //$NON-NLS-1$
			return;
		}

		if ( features == null )
			return;

		Node featureNode = features.item( 0 ); // get the first node.

		NodeList pluginsNodes = featureNode.getOwnerDocument( )
				.getElementsByTagName( "plugin" ); //$NON-NLS-1$
		NodeList includesNodes = featureNode.getOwnerDocument( )
				.getElementsByTagName( "includes" ); //$NON-NLS-1$

		for ( int j = 0; j < pluginsNodes.getLength( ); j++ )
			matchingNodes.add( pluginsNodes.item( j ) );
		for ( int j = 0; j < includesNodes.getLength( ); j++ )
			matchingNodesIncludes.add( includesNodes.item( j ) );

		
		boolean success = true;
		final String DOTS = ".................."; //$NON-NLS-1$
		List errorids = new ArrayList();
		handleOutput( "\nSanity check report for \"" + this.featureXML.getAbsolutePath( ) + "\""); //$NON-NLS-1$
		for ( int j = 0; j < matchingNodes.size( ); j++ )
		{
			Node pluginNode = (Node) matchingNodes.get( j );

			String id = pluginNode.getAttributes( )
					.getNamedItem( "id" ).getNodeValue( ); //$NON-NLS-1$
			String version = pluginNode.getAttributes( ).getNamedItem(
					"version" ).getNodeValue( ); //$NON-NLS-1$

			String newVersion = (String) this.versionMap.get( id );

			if ( StringUtil.isEqual( version, newVersion ) )
			{
				handleOutput( "Check id[" + id + "]" + DOTS + " success, updated to ["
						+ version + "]" );
			}
			else
			{
				handleErrorOutput( "Check id[" + id + "]" + DOTS + "fail, old version is: [" + version
						+ "],  plugin.version is: [" + newVersion + "]" );
				errorids.add( id );
				success = false;
			}
		}
		
		if( success )
			handleOutput( "Sanity check for plugins........ all success." );
		else
			handleErrorOutput( "Sanity check for plugins ........ has failure in: " + errorids );
		

		for ( int j = 0; j < matchingNodesIncludes.size( ); j++ )
		{
			Node pluginNode2 = (Node) matchingNodesIncludes.get( j );

			String version2 = pluginNode2.getAttributes( ).getNamedItem(
					"version" ).getNodeValue( ); //$NON-NLS-1$
			String id2 = pluginNode2.getAttributes( )
					.getNamedItem( "id" ).getNodeValue( ); //$NON-NLS-1$

			String tmpPrefix2 = "2.1.0";
			String newVersion2 = tmpPrefix2.concat( timeStamp);

			if ( StringUtil.isEqual( version2, newVersion2 ) )
			{
				handleOutput( "Check id[" + id2 + "]" + DOTS + " success, updated to ["
						+ version2 + "]" );
				success = true;
			}
			else
			{
				handleErrorOutput( "Check id[" + id2 + "]" + DOTS + "fail, old version is: [" + version2
						+ "],  plugin.version is: [" + newVersion2 + "]" );
				errorids.add( id2 );
				success = false;
			}
		}

		if( success )
			handleOutput( "Sanity check for includes features........ all success." );
		else
			handleErrorOutput( "Sanity check for included features ........ has failure in: " + errorids );

		handleOutput( "End of sanity check ["
				+ this.featureXML.getAbsolutePath( ) + "]\n" );

	}
}
