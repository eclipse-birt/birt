package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ObjectContent extends ImageContent
{
	/*
	  Attributes discribled in W3C recommendation.
 	  declare     (declare)      #IMPLIED  -- declare but don't instantiate flag --
	  classid     %URI;          #IMPLIED  -- identifies an implementation --
	  codebase    %URI;          #IMPLIED  -- base URI for classid, data, archive--
      data        %URI;          #IMPLIED  -- reference to object's data --
	  type        %ContentType;  #IMPLIED  -- content type for data --
	  codetype    %ContentType;  #IMPLIED  -- content type for code --
	  archive     CDATA          #IMPLIED  -- space-separated list of URIs --
	  standby     %Text;         #IMPLIED  -- message to show while loading --
	  height      %Length;       #IMPLIED  -- override height --
	  width       %Length;       #IMPLIED  -- override width --
	  usemap      %URI;          #IMPLIED  -- use client-side image map --
  	  name        CDATA          #IMPLIED  -- submit as part of form --
	  tabindex    NUMBER         #IMPLIED  -- position in tabbing order --
	 */

	private HashMap<String, Param> params = new HashMap<String, Param>( );

	ObjectContent( ReportContent report )
	{
		super( report );
	}

	public void addParam( String name, String value )
	{
		addParam( null, name, value, null, null );
	}

	public void addParam( String id, String name, String value,
			String valueType, String type )
	{
		if ( null != name )
		{
			Param p = new Param( name, value );
			p.setId( id );
			p.setValueType( valueType );
			p.setType( type );
			params.put( name, p );
		}
	}

	public String getParamValueByName( String name )
	{
		Param p = params.get( name );
		if ( p == null )
		{
			return null;
		}
		else
		{
			return p.getValue( );
		}
	}

	public String getParamValueTypeByName( String name )
	{
		Param p = params.get( name );
		if ( p == null )
		{
			return null;
		}
		else
		{
			return p.getValueType( );
		}

	}

	public String getParamIdByName( String name )
	{
		Param p = params.get( name );
		if ( p == null )
		{
			return null;
		}
		else
		{
			return p.getId( );
		}

	}

	public String getParamTypeByName( String name )
	{
		Param p = params.get( name );
		if ( p == null )
		{
			return null;
		}
		else
		{
			return p.getType( );
		}
	}

	public void readContent( DataInputStream in, ClassLoader loader )
			throws IOException
	{
		throw new IOException(
				"Unsupported operation: Object content can not be serialized" );
	}

	public void writeContent( DataOutputStream out ) throws IOException
	{
		throw new IOException(
				"Unsupported operation: Object content can not be serialized" );
	}
}

class Param
{

	/**
	 * unique id
	 */
	String id;
	/**
	 * property name
	 */
	String name;
	/**
	 * property value
	 */
	String value;
	/**
	 * How to interpret value (DATA|REF|OBJECT) DATA
	 */
	String valueType;
	/**
	 * content type for value when valuetype = ref
	 */
	String type;

	Param( String name, String value )
	{
		setName( name );
		setValue( value );
	}

	public String getId( )
	{
		return id;
	}

	public void setId( String id )
	{
		this.id = id;
	}

	public String getName( )
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getValue( )
	{
		return value;
	}

	public void setValue( String value )
	{
		this.value = value;
	}

	public String getValueType( )
	{
		return valueType;
	}

	public void setValueType( String valueType )
	{
		this.valueType = valueType;
	}

	public String getType( )
	{
		return type;
	}

	public void setType( String type )
	{
		this.type = type;
	}

}