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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.metadata.StructureDefn;

/**
 * This class implemented visitor pattern, to flatten the report deisgn.
 */

public class ReportDesignSerializer extends ElementVisitor
{

	/**
	 * The created report design.
	 */

	private ReportDesign targetDesign = null;

	/**
	 * The source design tree for the visitor pattern.
	 */

	private ReportDesign sourceDesign = null;

	/**
	 * The stack that contains element in the targetDesign. It is used to set up
	 * container/content relationship.
	 */

	private Stack elements = new Stack( );

	/**
	 * Elements are not direcly in source design. Hence, it should be created
	 * with new names and added to the target design.
	 */

	private Map externalElements = new LinkedHashMap( );

	/**
	 * Structures are not direcly in source design. Hence, it should be created
	 * with new names and added to the target design. Currently, only have cases
	 * with embedded images.
	 */

	private Map externalStructs = new LinkedHashMap( );

	/**
	 * The element is on process.
	 */

	private DesignElement currentNewElement = null;

	/**
	 * Returns the newly created report design.
	 * 
	 * @return the newly created report design.
	 */

	public ReportDesign getTarget( )
	{
		return targetDesign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDesignElement(org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void visitReportDesign( ReportDesign obj )
	{
		sourceDesign = obj;
		targetDesign = localizeDesign( obj );

		visitSlots( obj, targetDesign, IReportDesignModel.SLOT_COUNT );

		addExternalElements( );
		addExternalStructures( );

		// do some memory release
		release( );

		// copy version number from source to the target

		targetDesign.getVersionManager( ).setVersion(
				sourceDesign.getVersionManager( ).getVersion( ) );
	}

	/**
	 * Releases some memory instantly.
	 */

	private void release( )
	{
		elements = null;
		externalElements = null;
		externalStructs = null;
		currentNewElement = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDesignElement(org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void visitDesignElement( DesignElement obj )
	{
		DesignElement newElement = localize( obj );
		ElementDefn elementDefn = (ElementDefn) obj.getDefn( );

		if ( elementDefn.isContainer( ) )
		{
			int slotCount = elementDefn.getSlotCount( );
			if ( slotCount > 0 )
				visitSlots( obj, newElement, slotCount );
			List properties = elementDefn.getContainmentProperties( );
			if ( properties.size( ) > 0 )
				visitContainerProperties( obj, newElement, properties );
		}

		currentNewElement = newElement;
	}

	/**
	 * Adds structure values to the target. Currently only has cases with
	 * embedded image.
	 */

	private void addExternalStructures( )
	{
		List images = (List) targetDesign.getLocalProperty( targetDesign,
				IModuleModel.IMAGES_PROP );
		if ( images == null )
		{
			images = new ArrayList( );
			targetDesign.setProperty( IModuleModel.IMAGES_PROP, images );
		}

		Iterator embeddedImages = externalStructs.values( ).iterator( );
		while ( embeddedImages.hasNext( ) )
		{
			EmbeddedImage image = (EmbeddedImage) embeddedImages.next( );

			targetDesign.rename( image );
			images.add( image );
		}

	}

	/**
	 * Adds elements values to the target. These elements are not directly in
	 * source design. May resides in libraries of source design.
	 */

	private void addExternalElements( )
	{
		DesignElement[] elementArray = new DesignElement[externalElements
				.size( )];
		externalElements.keySet( ).toArray( elementArray );

		for ( int i = 0; i < elementArray.length; i++ )
		{
			DesignElement originalElement = elementArray[i];

			int slotId = originalElement.getContainerInfo( ).getSlotID( );

			DesignElement tmpElement = (DesignElement) externalElements
					.get( originalElement );

			DesignElement tmpContainer = getTargetContainer( originalElement,
					tmpElement );

			assert tmpContainer != null;
			tmpContainer.add( tmpElement, slotId );

			// work on unique name and name space.

			targetDesign.manageId( tmpElement, true );
			ModelUtil.addElement2NameSpace( targetDesign, tmpElement );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitStyle(org.eclipse.birt.report.model.elements.Style)
	 */

	public void visitStyle( Style obj )
	{
		visitDesignElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTheme(org.eclipse.birt.report.model.elements.Theme)
	 */

	public void visitTheme( Theme obj )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitReportItem(org.eclipse.birt.report.model.elements.ReportItem)
	 */

	public void visitReportItem( ReportItem obj )
	{
		visitStyledElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitScalarParameter(org.eclipse.birt.report.model.elements.ScalarParameter)
	 */

	public void visitScalarParameter( ScalarParameter obj )
	{
		visitParameter( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitStyledElement(org.eclipse.birt.report.model.core.StyledElement)
	 */

	public void visitStyledElement( StyledElement obj )
	{
		visitDesignElement( obj );

		localizeStyle( (StyledElement) currentNewElement, obj );
	}

	/**
	 * Resolves style reference in the target element. This method will copy the
	 * resolved element reference from the source element to the target design.
	 * 
	 * @param targetDesign
	 *            the target design to add the copied style
	 * @param target
	 *            the target element to resolve the style
	 * @param source
	 *            the source element
	 */

	private void localizeStyle( StyledElement target, StyledElement source )
	{
		// to localize properties values. The algorithm must match with the
		// property search path. 1. for element private value; 2. for shared
		// style value; 3. for values defined on extends parent, 4. for selector

		Set notEmptyProperties = new HashSet( );

		// first step to localize private style properties.
		// handle values defined on virtual/extends parents.

		localizeSelfStyleProperties( target, source, notEmptyProperties );

		// handle only when the style is not local one but a library resource

		String selector = ( (ElementDefn) target.getDefn( ) ).getSelector( );
		if ( selector != null )
		{
			Style style = (Style) sourceDesign.resolveElement( selector,
					Module.STYLE_NAME_SPACE, null );

			if ( style != null )
			{
				Module tmpRoot = style.getRoot( );
				if ( tmpRoot != sourceDesign )
					localizePrivateStyleProperties( target, style, tmpRoot,
							notEmptyProperties );

			}
		}

		notEmptyProperties.clear( );
	}

	/**
	 * Copies style values from source to the target if corresponding values of
	 * target are null. This method follows the same algorithm that is defined
	 * in PropertySearchAlgorithm.
	 * <ul>
	 * <li>localize private style properties.
	 * <li>localize style properties on extends/virtual parents.
	 * </ul>
	 * 
	 * @param target
	 *            the target element
	 * @param source
	 *            the source element
	 * @param notEmptyProperties
	 */

	private void localizeSelfStyleProperties( StyledElement target,
			StyledElement source, Set notEmptyProperties )
	{
		StyledElement tmpElement = source;

		while ( tmpElement != null )
		{
			Module root = tmpElement.getRoot( );
			localizePrivateStyleProperties( target, tmpElement, root,
					notEmptyProperties );

			Style style = (Style) tmpElement.getStyle( );

			// handle only when the style is not local one but a library
			// resource

			if ( style != null )
			{
				Module styleRoot = style.getRoot( );
				if ( styleRoot != sourceDesign )
				{
					localizePrivateStyleProperties( target, style, styleRoot,
							notEmptyProperties );
				}
				else
				{
					target.setStyleName( tmpElement.getStyleName( ) );
				}
			}

			if ( tmpElement.isVirtualElement( ) )
				tmpElement = (StyledElement) tmpElement.getVirtualParent( );
			else
				tmpElement = (StyledElement) tmpElement.getExtendsElement( );
		}

	}

	/**
	 * Copies local properties from <code>style</code> to the
	 * <code>target</code>.
	 * 
	 * @param target
	 *            the target element
	 * @param style
	 *            the style element
	 * @param targetDesign
	 *            the module of the target element
	 */

	private void localizePrivateStyleProperties( DesignElement target,
			DesignElement source, Module root, Set notEmptyProperties )
	{
		if ( !source.hasLocalPropertyValues( ) )
			return;

		// copy all the local values in the style

		IElementDefn defn = source.getDefn( );
		Iterator iter = source.propertyWithLocalValueIterator( );

		while ( iter.hasNext( ) )
		{
			ElementPropertyDefn prop = (ElementPropertyDefn) defn
					.getProperty( (String) iter.next( ) );

			// the property may be user-defined property. So, the value may be
			// null.

			if ( prop == null || !prop.isStyleProperty( ) )
				continue;

			String propName = prop.getName( );
			if ( notEmptyProperties.contains( propName ) )
				continue;

			ElementPropertyDefn targetProp = target.getPropertyDefn( propName );

			if ( targetProp == null )
				continue;

			if ( target.getLocalProperty( targetDesign, prop ) != null )
			{
				notEmptyProperties.add( propName );
				continue;
			}

			Object value = source.getLocalProperty( root, prop );

			// only handle values that not set in the target element

			if ( value == null )
				continue;

			switch ( targetProp.getTypeCode( ) )
			{
				case IPropertyType.LIST_TYPE :
					target.setProperty( targetProp, ModelUtil.copyValue(
							targetProp, value ) );
					break;
				case IPropertyType.STRUCT_TYPE :
					handleStructureValue( target, targetProp, value );
					break;
				default :
					target.setProperty( targetProp, ModelUtil.copyValue(
							targetProp, value ) );
			}

			notEmptyProperties.add( propName );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitMasterPage(org.eclipse.birt.report.model.elements.MasterPage)
	 */

	public void visitMasterPage( MasterPage obj )
	{
		visitStyledElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitGroup(org.eclipse.birt.report.model.elements.GroupElement)
	 */

	public void visitGroup( GroupElement obj )
	{
		visitDesignElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitRow(org.eclipse.birt.report.model.elements.TableRow)
	 */

	public void visitRow( TableRow obj )
	{
		visitStyledElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitCell(org.eclipse.birt.report.model.elements.Cell)
	 */

	public void visitCell( Cell obj )
	{
		visitStyledElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitColumn(org.eclipse.birt.report.model.elements.TableColumn)
	 */

	public void visitColumn( TableColumn obj )
	{
		visitStyledElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDataSet(org.eclipse.birt.report.model.elements.DataSet)
	 */

	public void visitDataSet( DataSet obj )
	{
		visitReferenceableElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDataSource(org.eclipse.birt.report.model.elements.DataSource)
	 */

	public void visitDataSource( DataSource obj )
	{
		visitReferenceableElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTemplateParameterDefinition(org.eclipse.birt.report.model.elements.TemplateParameterDefinition)
	 */

	public void visitTemplateParameterDefinition(
			TemplateParameterDefinition obj )
	{
		visitReferenceableElement( obj );
	}

	/**
	 * Visits the referenceable element.
	 * 
	 * @param obj
	 *            the element to traverse
	 */

	void visitReferenceableElement( ReferenceableElement obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the slots of the element.
	 * 
	 * @param obj
	 *            the element to traverse
	 */

	private void visitSlots( DesignElement obj, DesignElement newElement,
			int slotCount )
	{
		elements.push( newElement );
		for ( int i = 0; i < slotCount; i++ )
			visitContents( obj.getSlot( i ) );
		elements.pop( );
	}

	/**
	 * Visits the container properties of the given element.
	 * 
	 * @param obj
	 * @param newElement
	 * @param properties
	 */
	private void visitContainerProperties( DesignElement obj,
			DesignElement newElement, List properties )
	{
		elements.push( newElement );
		for ( int i = 0; i < properties.size( ); i++ )
		{
			PropertyDefn propDefn = (PropertyDefn) properties.get( i );
			List contents = new ContainerContext( obj, propDefn.getName( ) )
					.getContents( sourceDesign );
			Iterator iter = contents.iterator( );
			while ( iter.hasNext( ) )
				( (DesignElement) iter.next( ) ).apply( this );
		}
		elements.pop( );
	}

	/**
	 * Returns a newly created element by given elements. Do not copies values
	 * here. It only concerns names, container/content relationship.
	 * 
	 * @param element
	 *            the design element
	 * @return a newly created element
	 */

	private DesignElement createNewElement( DesignElement element )
	{
		ElementFactory factory = new ElementFactory( targetDesign );
		DesignElement newElement = factory.newElement(
				element.getDefn( ).getName( ), element.getName( ) )
				.getElement( );

		// if the element is an external element. do not add to the design now.
		// should be added in the end by addExternalElements.

		Set externalOriginalElements = externalElements.keySet( );
		if ( externalOriginalElements.contains( element ) )
			return newElement;

		// setup container relationship

		if ( element instanceof ReportDesign )
			return newElement;

		DesignElement container = (DesignElement) elements.peek( );

		newElement.setID( element.getID( ) );
		int slotId = element.getContainerInfo( ).getSlotID( );
		container.add( newElement, slotId );

		if ( newElement.getName( ) != null )
		{
			int ns = ( (ElementDefn) newElement.getDefn( ) ).getNameSpaceID( );
			if ( ns >= 0 )
				targetDesign.getNameSpace( ns ).insert( newElement );
		}

		targetDesign.addElementID( newElement );

		return newElement;
	}

	/**
	 * Creates a structure by the given structure. The given structure must be a
	 * structure that is not directly defined in the source design.
	 * 
	 * @param struct
	 *            the source structure
	 * @return the new structure
	 */

	private IStructure visitExternalStruct( IStructure struct )
	{
		IStructure newStrcut = struct.copy( );
		cacheMapping( struct, newStrcut );
		return newStrcut;
	}

	/**
	 * Localizes the element, including "extends" relationship, and reference
	 * relationship.
	 * 
	 * @param element
	 *            the element to localize
	 */

	private DesignElement localize( DesignElement element )
	{
		DesignElement newElement = createNewElement( element );
		localizePropertyValues( element, newElement );
		return newElement;
	}

	/**
	 * 
	 * @param source
	 * @return the localized design
	 */
	private ReportDesign localizeDesign( ReportDesign source )
	{
		ReportDesign design = new ReportDesign( source.getSession( ) );
		localizePropertyValues( source, design );
		return design;
	}

	/**
	 * Creates am element by the given element. The given element must be the
	 * one that is not directly defined in the source design.
	 * 
	 * @param struct
	 *            the source element
	 * @return the new element
	 */

	private DesignElement visitExternalElement( DesignElement element )
	{
		if ( element instanceof Theme )
			return null;

		ElementFactory factory = new ElementFactory( sourceDesign );
		DesignElement newElement = factory.newElement(
				element.getDefn( ).getName( ), element.getName( ) )
				.getElement( );

		localizePropertyValues( element, newElement );

		cacheMapping( element, newElement );
		return newElement;
	}

	/**
	 * Copies user property definitions from element to newElement.
	 * 
	 * @param element
	 *            the source element
	 * @param newElement
	 *            the target element
	 */

	private void localizeUserPropDefn( DesignElement element,
			DesignElement newElement )
	{
		Iterator iter = null;
		DesignElement current = null;
		if ( !element.isVirtualElement( ) )
		{
			current = element.getExtendsElement( );
			while ( current != null )
			{
				if ( current.hasUserProperties( ) )
				{
					iter = current.getLocalUserProperties( ).iterator( );
					while ( iter.hasNext( ) )
					{
						UserPropertyDefn uDefn = (UserPropertyDefn) iter.next( );
						if ( element
								.getLocalUserPropertyDefn( uDefn.getName( ) ) != null )
							continue;
						newElement
								.addUserPropertyDefn( (UserPropertyDefn) uDefn
										.copy( ) );
					}
				}

				current = current.getExtendsElement( );
			}
		}

	}

	/**
	 * Copies all values from element to newElement. Structure, element
	 * reference values, etc. are dumped as a new copy.
	 * 
	 * @param element
	 *            the source element
	 * @param newElement
	 *            the target element
	 */

	private void localizePropertyValues( DesignElement element,
			DesignElement newElement )
	{
		// copy user property definitions first, otherwise definition will
		// not be found when copying property values

		localizeUserPropDefn( element, newElement );

		if ( element instanceof IExtendableElement )
			ModelUtil.duplicateExtensionIdentifier( element, newElement,
					element.getRoot( ) );

		// get proerties from ascendants.

		Iterator iter = element.getPropertyDefns( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next( );

			String propName = propDefn.getName( );

			// Style property and extends property will be removed.
			// The properties inherited from style or parent will be
			// flatten to new element.

			if ( IDesignElementModel.EXTENDS_PROP.equals( propName )
					|| IDesignElementModel.USER_PROPERTIES_PROP
							.equals( propName )
					|| IModuleModel.THEME_PROP.equals( propName )
					|| IModuleModel.LIBRARIES_PROP.equals( propName ) )
				continue;

			// style properties are handled in styledElement.

			if ( ( propDefn.isStyleProperty( ) && !( element instanceof Style ) )
					|| IStyledElementModel.STYLE_PROP.equals( propName ) )
				continue;

			Object value = element.getStrategy( ).getPropertyFromElement(
					element.getRoot( ), element, propDefn );

			if ( value == null )
				continue;

			switch ( propDefn.getTypeCode( ) )
			{
				case IPropertyType.ELEMENT_REF_TYPE :
					handleElementRefValue( newElement, propDefn,
							(ElementRefValue) value );
					break;
				case IPropertyType.STRUCT_REF_TYPE :
					handleStructureRefValue( newElement, propDefn,
							(StructRefValue) value );
					break;
				case IPropertyType.LIST_TYPE :
					if ( propDefn.getSubTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
					{
						handleElementRefValueList( newElement, propDefn,
								(List) value );
					}
					else if ( newElement.getLocalProperty( null, propDefn ) == null )
						newElement.setProperty( propDefn, ModelUtil.copyValue(
								propDefn, value ) );
					break;
				case IPropertyType.STRUCT_TYPE :
					handleStructureValue( newElement, propDefn, value );
					break;
				default :
					if ( newElement.getLocalProperty( null, propDefn ) == null )
						newElement.setProperty( propDefn, value );
			}
		}
	}

	/**
	 * Localize values if the property type is structure or structure list.
	 * 
	 * @param newElement
	 *            the target element
	 * @param propDefn
	 *            the property definition
	 * @param valueList
	 *            the original property value
	 */

	private void handleStructureValue( DesignElement newElement,
			PropertyDefn propDefn, Object valueList )
	{
		if ( propDefn.isList( )
				&& IModuleModel.IMAGES_PROP.equalsIgnoreCase( propDefn
						.getName( ) ) )
		{
			List images = newElement.getListProperty( targetDesign,
					IModuleModel.IMAGES_PROP );
			if ( images == null )
			{
				images = new ArrayList( );
				newElement.setProperty( propDefn, images );
			}

			localizeEmbeddedImage( (List) valueList, images );
		}
		else
		{
			newElement.setProperty( propDefn, createNewStructureValue(
					propDefn, valueList ) );
		}
	}

	/**
	 * Returns the copy of <code>value</code>. Structure, structure list,
	 * element reference values, etc. are dumped as a new copy.
	 * 
	 * @param propDefn
	 *            the property/member definition
	 * @param value
	 *            the source value
	 * @return the copy of <code>value</code>
	 * 
	 */

	private Object createNewStructureValue( PropertyDefn propDefn, Object value )
	{
		Object newValue = null;

		if ( propDefn.isList( ) )
		{
			List sourceValue = (List) value;

			newValue = new ArrayList( );

			for ( int i = 0; i < sourceValue.size( ); i++ )
			{
				Structure newStruct = doCreateNewStructureValue( (Structure) sourceValue
						.get( i ) );
				( (List) newValue ).add( newStruct );
			}
		}
		else
			newValue = doCreateNewStructureValue( (Structure) value );

		return newValue;
	}

	/**
	 * Returns the copy of <code>value</code>. Structure, structure list,
	 * element reference values, etc. are dumped as a new copy.
	 * 
	 * @param propDefn
	 *            the property/member definition
	 * @param value
	 *            the source value
	 * @return the copy of <code>value</code>
	 * 
	 */

	private Structure doCreateNewStructureValue( Structure struct )
	{
		Structure newStruct = (Structure) struct.copy( );

		Iterator iter = struct.getObjectDefn( ).propertiesIterator( );
		while ( iter.hasNext( ) )
		{
			StructPropertyDefn memberDefn = (StructPropertyDefn) iter.next( );
			Object value = struct.getLocalProperty( sourceDesign, memberDefn );

			if ( value == null )
				continue;

			switch ( memberDefn.getTypeCode( ) )
			{
				case IPropertyType.ELEMENT_REF_TYPE :
					handleElementRefValue( newStruct, memberDefn,
							(ElementRefValue) value );
					break;
				case IPropertyType.STRUCT_TYPE :
					newStruct.setProperty( memberDefn, createNewStructureValue(
							memberDefn, value ) );
					break;
				default :
					newStruct.setProperty( memberDefn, ModelUtil.copyValue(
							memberDefn, value ) );
			}
		}

		return newStruct;
	}

	/**
	 * Localize values if the property type is element reference value.
	 * 
	 * @param newElement
	 *            the target element
	 * @param propDefn
	 *            the property definition
	 * @param value
	 *            the original property value
	 */

	private void handleElementRefValue( Structure structure,
			PropertyDefn propDefn, ElementRefValue value )
	{
		DesignElement refElement = value.getElement( );

		// handle only when the data set is not local but
		// library resource

		if ( refElement != null && refElement.getRoot( ) != sourceDesign )
		{
			DesignElement newRefEelement = getCache( refElement );
			if ( newRefEelement == null )
			{
				newRefEelement = visitExternalElement( refElement );
				refElement.apply( this );
			}

			assert newRefEelement != null;

			structure.setProperty( propDefn, new ElementRefValue( null,
					newRefEelement ) );
		}
		else
			structure.setProperty( propDefn, new ElementRefValue( value
					.getLibraryNamespace( ), value.getName( ) ) );
	}

	/**
	 * Localize values if the property type is element reference list.
	 * 
	 * @param newElement
	 *            the target element
	 * @param propDefn
	 *            the property definition
	 * @param valueList
	 *            the original property value
	 */

	private void handleElementRefValueList( DesignElement newElement,
			PropertyDefn propDefn, List valueList )
	{
		List values = new ArrayList( );
		for ( int i = 0; i < valueList.size( ); i++ )
		{
			// try to resolve every

			ElementRefValue item = (ElementRefValue) valueList.get( i );
			DesignElement refElement = item.getElement( );
			if ( refElement != null && refElement.getRoot( ) != sourceDesign )
			{
				DesignElement newRefEelement = getCache( refElement );
				if ( newRefEelement == null )
				{
					newRefEelement = visitExternalElement( refElement );
					refElement.apply( this );
				}
				values.add( new ElementRefValue( null, newRefEelement ) );
			}
			else
				newElement.setProperty( propDefn, item );
		}

		newElement.setProperty( propDefn, values );

	}

	/**
	 * Localize values if the property type is structure reference value.
	 * 
	 * @param newElement
	 *            the target element
	 * @param propDefn
	 *            the property definition
	 * @param value
	 *            the original property value
	 */

	private void handleStructureRefValue( DesignElement newElement,
			PropertyDefn propDefn, StructRefValue value )
	{
		if ( !isLocalImage( ( value ).getQualifiedReference( ) ) )
		{
			EmbeddedImage targetEmbeddedImage = (EmbeddedImage) value
					.getTargetStructure( );

			EmbeddedImage newEmbeddedIamge = localizeExternalEmbeddedImage( targetEmbeddedImage );

			newElement.setProperty( propDefn, new StructRefValue( null,
					newEmbeddedIamge ) );
		}
		else
			newElement.setProperty( propDefn, ModelUtil.copyValue( propDefn,
					value ) );
	}

	/**
	 * Localize values if the property type is element reference value.
	 * 
	 * @param newElement
	 *            the target element
	 * @param propDefn
	 *            the property definition
	 * @param value
	 *            the original property value
	 */

	private void handleElementRefValue( DesignElement newElement,
			PropertyDefn propDefn, ElementRefValue value )
	{
		DesignElement refElement = value.getElement( );

		// handle only when the data set is not local but
		// library resource

		if ( refElement != null && refElement.getRoot( ) != sourceDesign )
		{
			DesignElement newRefEelement = getCache( refElement );
			if ( newRefEelement == null )
			{
				newRefEelement = visitExternalElement( refElement );
				refElement.apply( this );
			}

			// if it is theme, newRefElement can be null.

			if ( newRefEelement != null )
				newElement.setProperty( propDefn, new ElementRefValue( null,
						newRefEelement ) );
		}
		else
			newElement.setProperty( propDefn, new ElementRefValue( value
					.getLibraryNamespace( ), value.getName( ) ) );
	}

	/**
	 * Localizs embedded images in sourceEmbeddedImage to the new list of
	 * targetEmbeddedImage.
	 * 
	 * @param sourceEmbeddedImage
	 *            the source images
	 * @param targetEmeddedImage
	 *            the target images
	 */

	private void localizeEmbeddedImage( List sourceEmbeddedImage,
			List targetEmeddedImage )
	{

		for ( int i = 0; i < sourceEmbeddedImage.size( ); i++ )
		{
			EmbeddedImage sourceImage = (EmbeddedImage) sourceEmbeddedImage
					.get( i );

			if ( !targetEmeddedImage.contains( sourceImage ) )
			{
				EmbeddedImage newEmeddedImage = (EmbeddedImage) sourceImage
						.copy( );

				localizeEmbeddedImageValues( sourceImage, newEmeddedImage );

				targetEmeddedImage.add( newEmeddedImage );
			}
		}
	}

	/**
	 * Localizs member values of embedded images in sourceEmbeddedImage to the
	 * targetEmbeddedImage.
	 * 
	 * @param sourceEmbeddedImage
	 *            the source images
	 * @param targetEmeddedImage
	 *            the target images
	 */

	private void localizeEmbeddedImageValues(
			EmbeddedImage sourceEmbeddedImage, EmbeddedImage targetEmeddedImage )
	{
		EmbeddedImage tmpEmeddedImage = sourceEmbeddedImage;
		while ( tmpEmeddedImage != null
				&& ( targetEmeddedImage.getData( null ) == null || targetEmeddedImage
						.getType( null ) == null ) )
		{
			targetEmeddedImage
					.setData( tmpEmeddedImage.getData( sourceDesign ) );
			targetEmeddedImage
					.setType( tmpEmeddedImage.getType( sourceDesign ) );

			StructRefValue refValue = (StructRefValue) tmpEmeddedImage
					.getProperty( sourceDesign,
							ReferencableStructure.LIB_REFERENCE_MEMBER );
			if ( refValue == null )
				break;

			tmpEmeddedImage = (EmbeddedImage) refValue.getTargetStructure( );
		}

		targetEmeddedImage.setProperty(
				ReferencableStructure.LIB_REFERENCE_MEMBER, null );

	}

	/**
	 * Localizes an embedded image to the cached map when the
	 * sourceEmbeddedImage is not directly in the source design.
	 * 
	 * @param sourceEmbeddedImage
	 *            the source embedded image
	 * @return the new embedded image
	 */

	private EmbeddedImage localizeExternalEmbeddedImage(
			EmbeddedImage sourceEmbeddedImage )
	{
		EmbeddedImage newEmeddedImage = (EmbeddedImage) getCache( sourceEmbeddedImage );

		if ( newEmeddedImage != null )
			return newEmeddedImage;

		newEmeddedImage = (EmbeddedImage) visitExternalStruct( sourceEmbeddedImage );
		localizeEmbeddedImageValues( sourceEmbeddedImage, newEmeddedImage );

		return newEmeddedImage;
	}

	private void cacheMapping( DesignElement sourceElement,
			DesignElement targetElement )
	{
		externalElements.put( sourceElement, targetElement );
	}

	private void cacheMapping( IStructure sourceStruct, IStructure targetStruct )
	{
		externalStructs.put( sourceStruct, targetStruct );
	}

	private IStructure getCache( IStructure sourceStruct )
	{
		return (IStructure) externalStructs.get( sourceStruct );
	}

	private DesignElement getCache( DesignElement sourceElement )
	{
		return (DesignElement) externalElements.get( sourceElement );
	}

	/**
	 * Determines whether the given name embedded image is a local one.
	 * 
	 * @param imageName
	 * @param sourceDesign
	 * @return true if the embedded image is a local one, otherwise false
	 */

	private boolean isLocalImage( String imageName )
	{
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance( )
				.getStructure( EmbeddedImage.EMBEDDED_IMAGE_STRUCT );

		if ( StructureRefUtil.findNativeStructure( targetDesign, defn,
				imageName ) != null )
			return true;

		return false;
	}

	/**
	 * Returns the container for the target element.
	 * 
	 * @param sourceElement
	 *            the source element
	 * @param target
	 *            the target element
	 * @return the container of <code>target</code>
	 */

	private DesignElement getTargetContainer( DesignElement sourceElement,
			DesignElement target )
	{
		DesignElement sourceContainer = sourceElement.getContainer( );
		long containerId = sourceContainer.getID( );

		DesignElement tmpContainer = targetDesign.getElementByID( containerId );

		if ( sourceContainer.getElementName( ).equalsIgnoreCase(
				tmpContainer.getElementName( ) ) )
			return tmpContainer;

		if ( sourceContainer instanceof Module && tmpContainer == targetDesign )
			return tmpContainer;

		if ( sourceContainer instanceof Theme )
			return targetDesign;

		return tmpContainer;
	}
}
