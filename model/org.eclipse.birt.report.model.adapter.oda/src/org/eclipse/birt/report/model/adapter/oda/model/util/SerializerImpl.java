/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.model.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.adapter.oda.IConstants;
import org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.Serializer;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * SerializerImpl
 */
public class SerializerImpl implements Serializer
{

	public static final String ODA_VALUES_START_MARKER = "<!-- ODA values Starts Here. -->"; //$NON-NLS-1$

	public static final String ODA_VALUES_END_MARKER = "<!-- ODA values Ends Here. -->"; //$NON-NLS-1$

	private static Serializer sz = null;

	static
	{
		EPackage.Registry.INSTANCE
				.put(
						"http://www.eclipse.org/birt/report/model/adapter/odaModel", ModelFactory.eINSTANCE ); //$NON-NLS-1$
	}

	/**
	 * Cannot invoke constructor; use instance() instead
	 */
	private SerializerImpl( )
	{

	}

	/**
	 * 
	 * @return A singleton instance of the chart serializer
	 */
	public static synchronized final Serializer instance( )
	{
		if ( sz == null )
		{
			sz = new SerializerImpl( );
		}
		return sz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ISerialization#write(org.eclipse.birt.chart.model.Chart,
	 *      java.io.OutputStream)
	 */

	public void write( DesignValues cModel, OutputStream os )
			throws IOException
	{

		// Create and setup local ResourceSet

		ResourceSet rsOdaValues = new ResourceSetImpl( );
		rsOdaValues.getResourceFactoryRegistry( ).getExtensionToFactoryMap( )
				.put( "designValues", new ModelResourceFactoryImpl( ) ); //$NON-NLS-1$

		// Create resources to represent the disk files to be used to store the
		// models
		Resource rOdaValue = rsOdaValues.createResource( URI
				.createFileURI( "test.designValues" ) ); //$NON-NLS-1$

		cModel.setVersion( IConstants.DESINGER_VALUES_VERSION );

		// Add the chart to the resource
		rOdaValue.getContents( ).add( cModel );

		Map options = new HashMap( );
		options.put( XMLResource.OPTION_ENCODING, "UTF-8" ); //$NON-NLS-1$

		// Save the resource to disk
		rOdaValue.save( os, options );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ISerialization#write(org.eclipse.birt.chart.model.Chart,
	 *      org.eclipse.emf.common.util.URI)
	 */

	public ByteArrayOutputStream asXml( DesignValues cModel,
			boolean bStripHeaders ) throws IOException
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream( );

		// Create and setup local ResourceSet
		ResourceSet rsChart = new ResourceSetImpl( );
		rsChart.getResourceFactoryRegistry( ).getExtensionToFactoryMap( ).put(
				"designValues", new ModelResourceFactoryImpl( ) ); //$NON-NLS-1$

		// Create resources to represent the disk files to be used to store the
		// models
		Resource rChart = rsChart.createResource( URI
				.createFileURI( "test.designValues" ) ); //$NON-NLS-1$

		// Add the chart to the resource
		rChart.getContents( ).add( cModel );

		Map options = new HashMap( );
		options.put( XMLResource.OPTION_ENCODING, "UTF-8" ); //$NON-NLS-1$
		if ( bStripHeaders )
		{
			options.put( XMLResource.OPTION_DECLARE_XML, Boolean.FALSE );
		}

		// Save the resource to disk
		rChart.save( baos, options );
		return baos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ISerialization#read(java.io.InputStream)
	 */
	public DesignValues read( InputStream is ) throws IOException
	{
		// Create and setup local ResourceSet
		ResourceSet rsChart = new ResourceSetImpl( );
		rsChart.getResourceFactoryRegistry( ).getExtensionToFactoryMap( ).put(
				"designValue", new ModelResourceFactoryImpl( ) ); //$NON-NLS-1$

		// Create resources to represent the disk files to be used to store the
		// models
		Resource rChart = rsChart.createResource( URI
				.createFileURI( "test.designValue" ) ); //$NON-NLS-1$

		Map options = new HashMap( );
		options.put( XMLResource.OPTION_ENCODING, "UTF-8" ); //$NON-NLS-1$

		rChart.load( is, options );
		
		DocumentRoot docRoot = (DocumentRoot) rChart.getContents( ).get( 0 );
		return docRoot.getDesignValues( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ISerialization#read(java.io.InputStream)
	 */

	public DesignValues read( String values ) throws IOException
	{
		if ( values == null )
			return null;

		ByteArrayInputStream bis = new ByteArrayInputStream( values
				.getBytes( IConstants.CHAR_ENCODING ) );
		
		DesignValues retValues = read( bis );
		
		bis.close( );
		
		return retValues;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ISerialization#read(java.io.InputStream)
	 */

	public String write( DesignValues values ) throws IOException
	{
		if ( values == null )
			return null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream(  );		
		write( values, bos );
		
		
		String retValue = bos.toString( IConstants.CHAR_ENCODING );		
		bos.close( );
		
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ISerialization#fromXml(byte[], boolean)
	 */
	public DesignValues fromXml( ByteArrayInputStream byais,
			boolean bStripHeaders ) throws IOException
	{
		// Create and setup local ResourceSet
		ResourceSet rsChart = new ResourceSetImpl( );
		rsChart.getResourceFactoryRegistry( ).getExtensionToFactoryMap( ).put(
				"designValues", new ModelResourceFactoryImpl( ) ); //$NON-NLS-1$

		// Create resources to represent the disk files to be used to store the
		// models
		Resource rChart = rsChart.createResource( URI
				.createFileURI( "test.designValues" ) ); //$NON-NLS-1$

		Map options = new HashMap( );
		options.put( XMLResource.OPTION_ENCODING, "UTF-8" ); //$NON-NLS-1$
		if ( bStripHeaders )
		{
			options.put( XMLResource.OPTION_DECLARE_XML, Boolean.FALSE );
		}
		rChart.load( byais, options );
		return (DesignValues) rChart.getContents( ).get( 0 );
	}
}