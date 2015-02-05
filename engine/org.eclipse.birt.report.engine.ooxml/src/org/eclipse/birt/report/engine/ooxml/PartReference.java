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

package org.eclipse.birt.report.engine.ooxml;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.engine.ooxml.util.OOXmlUtil;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;


public class PartReference implements IPart
{

	private PartContainer parentPart;

	private IPart realPart;

	private int relationshipId;

	private String uri;

	PartReference( PartContainer partContainer, IPart realPart,
	        int relationshipId )
	{
		this.parentPart = partContainer;
		this.realPart = realPart;
		this.relationshipId = relationshipId;
		this.uri = OOXmlUtil.getRelativeUri( parentPart.getAbsoluteUri( ),
				getAbsoluteUri( ) );
	}

	public String getAbsoluteUri( )
	{
		return realPart.getAbsoluteUri( );
	}

	public ContentType getContentType( )
	{
		return realPart.getContentType( );
	}

	public OutputStream getOutputStream( ) throws IOException
	{
		return realPart.getOutputStream( );
	}

	public Package getPackage( )
	{
		return realPart.getPackage( );
	}

	public String getRelationshipId( )
	{
		return OOXmlUtil.getRelationShipId( relationshipId );
	}

	public String getRelationshipType( )
	{
		return realPart.getRelationshipType( );
	}

	public String getRelationshipUri( )
	{
		return realPart.getRelationshipUri( );
	}

	public String getRelativeUri( )
	{
		return uri;
	}

	public OOXmlWriter getCacheWriter( ) throws IOException
	{
		return realPart.getCacheWriter( );
	}

	public IPart getPart( String uri, String type, String relationshipType )
	{
		return realPart.getPart( uri, type, relationshipType );
	}

	public IPart createPartReference( IPart part )
	{
		return realPart.createPartReference( part );
	}

	public IPart getPart( String uri, ContentType type, String relationshipType )
	{
		return realPart.getPart( uri, type, relationshipType );
	}

	public IPart getPart( String uri )
	{
		return realPart.getPart( uri );
	}

	public String getHyperlinkId( String url )
	{
		return realPart.getHyperlinkId( url );
	}

	public String getExternalImageId( String url )
	{
		return realPart.getExternalImageId( url );
	}

	public OutputStream getCacheOutputStream( ) throws IOException
	{
		return realPart.getCacheOutputStream( );
	}

	public OOXmlWriter getWriter( ) throws IOException
	{
		return realPart.getWriter( );
	}

	public boolean isCached( )
	{
		return realPart.isCached( );
	}

	public boolean isReference( )
	{
		return true;
	}

	@Override
	public String getBookmarkId( String bmkurl )
	{//req implementation: does not do anything
		return null;
	}
	

}
