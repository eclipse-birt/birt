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

package org.eclipse.birt.report.model.metadata;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.extension.IChoiceDefinition;
import org.eclipse.birt.report.model.extension.IPropertyDefinition;


/**
 * Represents the extension model property definition. It is used for
 * only those property defined by BIRT extension element. The instance of this
 * class is read-only, which means that all the "set" methods is forbidden to call.
 */

public class ExtensionModelPropertyDefn extends ElementPropertyDefn
{
	/**
	 * The effective extension property definition.
	 */
	
	private IPropertyDefinition extProperty = null;
	
	/**
	 * Constructs the extension element property definition with an effective
	 * extension property.
	 * @param extPropertyDefn
	 *  the effective extension property definition
	 */
	
	public ExtensionModelPropertyDefn( IPropertyDefinition extPropertyDefn )
	{
		extProperty = extPropertyDefn;
		assert extProperty != null;
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		type = dd.getPropertyType( extProperty.getType( ) );	
		name = extProperty.getName( );
		defaultValue = extProperty.getDefaultValue( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getValueType()
	 */
	
	public int getValueType( )
	{
		return EXTENSION_PROPERTY;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#canInherit()
	 */
	
	public boolean canInherit( )
	{
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getGroupName()
	 */
	
	public String getGroupName( )
	{
		return extProperty.getGroupName( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getGroupNameKey()
	 */
	
	public String getGroupNameKey( )
	{
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getMethodInfo()
	 */
	
	public MethodInfo getMethodInfo( )
	{
		// TODO: till now, we will support the scripting type?
		// if so, we will handle this in IPropertyDefinition
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#isStyleProperty()
	 */
	
	public boolean isStyleProperty( )
	{
		// TODO: if the extension elements can define their own style properties
		// then the interface IPropertyDefinition should handle this?
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#setCanInherit(boolean)
	 */
	
	void setCanInherit( boolean flag )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#setGroupNameKey(java.lang.String)
	 */
	
	void setGroupNameKey( String id )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getAllowedChoices()
	 */
	
	public ChoiceSet getAllowedChoices( )
	{
		return getChoices( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getChoices()
	 */
	
	public ChoiceSet getChoices( )
	{
		if ( details instanceof ChoiceSet )
			return (ChoiceSet) details;
		Collection choices = extProperty.getChoices( );
		if ( choices == null )
			return null;
		if ( choices.size( ) == 0 )
			return null;
		Choice[] choiceArray = new Choice[ choices.size( ) ];
		Iterator iter = choices.iterator( );
		int i = 0;
		while( iter.hasNext( ) )
		{
			IChoiceDefinition choice = (IChoiceDefinition)iter.next( );
			ExtensionChoice extChoice = new ExtensionChoice( choice );
			choiceArray[ i ] = extChoice;
			i++;
		}
		assert i == choices.size( );
		
		ChoiceSet set = new ChoiceSet( );
		set.setChoices( choiceArray );
		details = set;
		return set;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDefault()
	 */
	
	public Object getDefault( )
	{
		return extProperty.getDefaultValue( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDisplayName()
	 */
	
	public String getDisplayName( )
	{
		return extProperty.getDisplayName( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDisplayNameID()
	 */
	
	public String getDisplayNameID( )
	{
		return null;
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getName()
	 */
	
	public String getName( )
	{
		return extProperty.getName( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getStructDefn()
	 */
	
	public StructureDefn getStructDefn( )
	{
		if ( details instanceof StructureDefn )
			return (StructureDefn) details;
		List members = extProperty.getMembers( );
		if ( members == null )
			return null;
		StructureDefn struct = new StructureDefn( ); 
		Iterator iter = members.iterator( );
		while( iter.hasNext( ) )
		{
			IPropertyDefinition prop = (IPropertyDefinition) iter.next( );
			try
			{
			struct.addProperty( new ExtensionStructPropertyDefn( prop ) );
			}
			catch( MetaDataException e )
			{
				assert false;
				return null;
			}
		}
		details = struct;
		return struct;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getTargetElementType()
	 */
	
	public ElementDefn getTargetElementType( )
	{
		// till now, the extension elements do not support
		// element reference type?
		// TODO: if support element reference type, we should handle
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getType()
	 */
	
	public PropertyType getType( )
	{
		int typeCode = extProperty.getType( );
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		return dd.getPropertyType( typeCode );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getTypeCode()
	 */
	
	public int getTypeCode( )
	{
		PropertyType type = getType( );
		assert type != null;
		return type.getTypeCode( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#isIntrinsic()
	 */
	
	public boolean isIntrinsic( )
	{
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#isList()
	 */
	
	public boolean isList( )
	{
		return extProperty.isList( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setAllowedChoices(org.eclipse.birt.report.model.metadata.ChoiceSet)
	 */
	
	void setAllowedChoices( ChoiceSet allowedChoices )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setDefault(java.lang.Object)
	 */
	
	protected void setDefault( Object value )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setDetails(java.lang.Object)
	 */
	
	public void setDetails( Object obj )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setDisplayNameID(java.lang.String)
	 */
	
	public void setDisplayNameID( String id )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setIntrinsic(boolean)
	 */
	
	void setIntrinsic( boolean flag )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setIsList(boolean)
	 */
	
	protected void setIsList( boolean isList )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setName(java.lang.String)
	 */
	
	public void setName( String theName )
	{
		assert false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setType(org.eclipse.birt.report.model.metadata.PropertyType)
	 */
	
	public void setType( PropertyType typeDefn )
	{
		assert false;
	}		
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#isExtended()
	 */
	
	public boolean isExtended( )
	{
		return true;
	}
}
