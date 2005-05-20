/*
 * Created on May 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DocProperty extends DocObject
{
	PropertyDefn defn;
	HashMap notes = null;
	ArrayList choices = null;
	
	public DocProperty( PropertyDefn propDefn )
	{
		defn = propDefn;
		IChoiceSet choiceSet = defn.getChoices( );
		if ( choiceSet == null )
			return;
		choices = new ArrayList( );
		IChoice set[] = choiceSet.getChoices( );
		for ( int i = 0;  i < set.length;  i++ )
		{
			choices.add( new DocChoice( set[i] ) );
		}
	}

	public String getName( )
	{
		return defn.getName( );
	}

	public String getType( )
	{
		String type;
		if ( defn.getTypeCode( ) == PropertyType.STRUCT_TYPE )
		{
			type = makeStructureLink( defn.getStructDefn( ), "element" ) +
			       " Structure";
		}
		else if ( defn.getTypeCode( ) == PropertyType.ELEMENT_REF_TYPE )
		{
			type = makeElementLink( defn.getTargetElementType( ).getName( ), "element" ) +
				   " Reference";
		}
		else if ( defn.getTypeCode( ) == PropertyType.CHOICE_TYPE )
		{
			type = makeTypeLink( defn.getType( ), "element" ) +
			       " (" + defn.getChoices( ).getName( ) + ")";
		}
		else
			type = makeTypeLink( defn.getType( ), "element" );
		if ( defn.isList( ) )
			type = "List of " + type + "s";
		return type;
	}

	public String getSince( )
	{
		// Style is special
		
		if ( defn.getName( ).equals( "style" ) )
			return "1.0";
		
		return defn.getSince( );
	}

	public String getRequired( )
	{
		return yesNo( defn.isValueRequired( ) );
	}

	public String getDisplayName( )
	{
		return defn.getDisplayName( );
	}

	public String getJSType( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getDefaultValue( )
	{
		String note = getNote( "Default value" );
		if ( note != null )
			return note;
		Object value = defn.getDefault( );
		if ( value != null )
			return value.toString( );
		return "None";
	}

	public String getInherited( )
	{
		return yesNo( ((ElementPropertyDefn) defn).canInherit( ) );
	}

	public String getRuntimeSettable( )
	{
		return yesNo( defn.isRuntimeSettable( ) );
	}

	public boolean hasChoices( )
	{
		return defn.getChoices( ) != null;
	}

	public String getVisibility( DocElement element )
	{
		if ( element.getElementDefn( ).isPropertyReadOnly( defn.getName( ) ) )
			return "Read-only";
		if ( element.getElementDefn( ).isPropertyVisible( defn.getName( ) ) )
			return "Visible";
		return "Hidden";
	}

	public String getGroup( )
	{
		String group = ((ElementPropertyDefn) defn).getGroupName( );
		if ( group == null )
			return "Top";
		return group;
	}
	
	public void addNote( String key, String note )
	{
		if( notes == null )
			notes = new HashMap( );
		notes.put( key.toLowerCase( ), note );
	}
	
	public String getNote( String key )
	{
		if ( notes == null )
			return null;
		return (String) notes.get( key.toLowerCase( ) );
	}

	public DocChoice findChoice( String name )
	{
		if ( choices == null )
			return null;
		for ( int i = 0;  i < choices.size( );  i++ )
		{
			DocChoice choice = (DocChoice) choices.get( i );
			if ( choice.getName( ).equals( name ) )
				return choice;
		}
		return null;
	}

	public AbstractList getChoices( )
	{
		return choices;
	}

	public boolean isExpression( )
	{
		return defn.getTypeCode( ) == PropertyType.EXPRESSION_TYPE;
	}

	public String getContext( )
	{
		return defn.getContext( );
	}

	public String getReturnType( )
	{
		if ( defn.getReturnType( ) == null )
			return "None";
		return defn.getReturnType( );
	}
}
