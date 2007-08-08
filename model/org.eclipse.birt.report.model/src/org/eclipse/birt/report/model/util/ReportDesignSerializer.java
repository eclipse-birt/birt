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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.elements.AccessControl;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.FilterConditionElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.MemberValue;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.SortElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.ValueAccessControl;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PeerExtensionLoader;
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
	 * Cubes that need to build the dimension condition. It stores
	 * newCube/oldCube pair.
	 */
	private Map cubes = new LinkedHashMap( );

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

		localizeExternalSelectors( );

		addExternalElements( );
		addExternalStructures( );

		// handle dimension conditions
		localizeDimensionConditions( );

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
		cubes = null;
	}

	/**
	 * By calling this method directly/indirectly, the obj must in the source
	 * design directly. Must not be in the included libraries of source design.
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
			List properties = elementDefn.getContents( );
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
		List tmpElements = new ArrayList( );
		tmpElements.addAll( externalElements.keySet( ) );

		List processedElements = new ArrayList( );
		int index = 0;
		while ( processedElements.size( ) < externalElements.size( ) )
		{
			DesignElement originalElement = (DesignElement) tmpElements
					.get( index++ );
			addExternalElement( tmpElements, processedElements, originalElement );
		}
	}

	/**
	 * @param elements
	 * @param processedElements
	 * @param originalElement
	 */

	private void addExternalElement( List elements, List processedElements,
			DesignElement originalElement )
	{
		if ( processedElements.contains( originalElement ) )
			return;

		DesignElement originalContainer = originalElement.getContainer( );
		if ( elements.contains( originalContainer ) )
		{
			addExternalElement( elements, processedElements, originalContainer );
		}

		processedElements.add( originalElement );

		DesignElement tmpElement = (DesignElement) externalElements
				.get( originalElement );

		DesignElement tmpContainer = getTargetContainer( originalElement,
				tmpElement, processedElements );

		ContainerContext context = null;

		if ( tmpContainer != null )
			context = originalElement.getContainerInfo( ).createContext(
					tmpContainer );

		if ( context == null && originalContainer instanceof Theme )
		{
			int newId = IReportDesignModel.STYLE_SLOT;
			context = new ContainerContext( targetDesign, newId );
		}

		// if so, it can only be ReportDesign and Library case.

		if ( context == null && originalContainer instanceof Library )
		{
			context = originalElement.getContainerInfo( );
			int newId = DesignElement.NO_SLOT;
			switch ( context.getSlotID( ) )
			{
				case ILibraryModel.THEMES_SLOT :
					newId = IDesignElementModel.NO_SLOT;
					assert false;
					break;
				case ILibraryModel.CUBE_SLOT :
					newId = IReportDesignModel.CUBE_SLOT;
					break;
				default :
					newId = context.getSlotID( );
			}
			context = new ContainerContext( tmpContainer, newId );
		}

		assert context != null;

		if ( context.contains( targetDesign, tmpElement ) )
			return;

		addElement( targetDesign, context, tmpElement );
		targetDesign.manageId( tmpElement, true );
	}

	/**
	 * Adds an element to the context. This method will handle container
	 * relationship, element name and element id for the inserted content and
	 * all its children.
	 * 
	 * @param module
	 * @param context
	 * @param content
	 */
	private void addElement( Module module, ContainerContext context,
			DesignElement content )
	{
		assert context != null;
		assert content != null;

		// first construct the container relationship
		context.add( module, content );

		// manage element name: the inserted content and all its children
		if ( context.isManagedByNameSpace( ) )
			module.rename( context.getElement( ), content );
		addElement2NameSpace( content );
		ContentIterator iter = new ContentIterator( module, content );
		while ( iter.hasNext( ) )
		{
			DesignElement child = (DesignElement) iter.next( );
			addElement2NameSpace( child );
		}
	}

	/**
	 * Adds an element to the name space. If the module is null, or element is
	 * null, or element is not in the tree of module, then do nothing.
	 * 
	 * @param module
	 * @param element
	 */

	private void addElement2NameSpace( DesignElement element )
	{
		if ( element == null || !element.isManagedByNameSpace( ) )
			return;

		int ns = ( (ElementDefn) element.getDefn( ) ).getNameSpaceID( );
		if ( element.getName( ) != null
				&& ns != MetaDataConstants.NO_NAME_SPACE )
		{
			NameSpace namespace = new NameExecutor( element )
					.getNameSpace( targetDesign );
			if ( namespace != null )
			{
				if ( namespace.contains( element.getName( ) ) )
					throw new RuntimeException( "element name is not unique" ); //$NON-NLS-1$
				namespace.insert( element );
			}
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

		notEmptyProperties.clear( );
	}

	/**
	 * Copies values of selector in the library into the target design. If the
	 * selector doesn't exist, localize to the report design. Otherwise, merge
	 * values in external and local selectors.
	 */

	private void localizeExternalSelectors( )
	{
		assert elements.isEmpty( );

		elements.push( targetDesign );

		Theme theme = sourceDesign.getTheme( sourceDesign );
		if ( theme == null )
		{
			elements.pop( );
			return;
		}

		Module tmpRoot = theme.getRoot( );
		List styles = theme.getAllStyles( );
		for ( int i = 0; i < styles.size( ); i++ )
		{
			Style tmpStyle = (Style) styles.get( i );
			visitExternalSelector( tmpStyle, tmpRoot );
		}

		elements.pop( );
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
		visitDesignElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDataSource(org.eclipse.birt.report.model.elements.DataSource)
	 */

	public void visitDataSource( DataSource obj )
	{
		visitDesignElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTemplateParameterDefinition(org.eclipse.birt.report.model.elements.TemplateParameterDefinition)
	 */

	public void visitTemplateParameterDefinition(
			TemplateParameterDefinition obj )
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
			visitContents( sourceDesign, new ContainerContext( obj, i ) );
		elements.pop( );
	}

	/**
	 * Visits the slots of the element.
	 * 
	 * @param obj
	 *            the element to traverse
	 */

	private void visitExternalSlots( DesignElement obj,
			DesignElement newElement, int slotCount )
	{
		elements.push( newElement );
		for ( int i = 0; i < slotCount; i++ )
			visitExternalContents( sourceDesign, new ContainerContext( obj, i ) );
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
			visitContents( sourceDesign, new ContainerContext( obj, propDefn
					.getName( ) ) );
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
		ContainerContext sourceContainment = element.getContainerInfo( );

		ContainerContext containment = null;
		DesignElement container = (DesignElement) elements.peek( );
		String containmentProp = sourceContainment.getPropertyName( );
		if ( containmentProp != null )
			containment = new ContainerContext( container, containmentProp );
		else
			containment = new ContainerContext( container, sourceContainment
					.getSlotID( ) );
		DesignElement newElement = newElement( element.getDefn( ).getName( ),
				element.getName( ), containment ).getElement( );

		// if the element is an external element. do not add to the design now.
		// should be added in the end by addExternalElements.

		Set externalOriginalElements = externalElements.keySet( );
		if ( externalOriginalElements.contains( element ) )
			return newElement;

		// setup container relationship

		if ( element instanceof ReportDesign )
			return newElement;

		newElement.setID( element.getID( ) );
		addElement( targetDesign, containment, newElement );
		if ( !( newElement instanceof ContentElement ) )
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
	 * <p>
	 * By calling this method directly/indirectly, the obj must in the source
	 * design directly. Must not be in the included libraries of source design.
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

		design.setFileName( source.getFileName( ) );
		design.setSystemId( source.getSystemId( ) );

		ModuleOption options = source.getOptions( );
		try
		{
			if ( options != null )
				design.setOptions( ( (ModuleOption) options.copy( ) ) );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
		}

		localizePropertyValues( source, design );

		// css style sheet must be treated here. It is different from other
		// elements and property values.

		visitCssStyleSheets( source, design );
		return design;
	}

	// css style related methods.

	/**
	 * Localizes the css style sheets from the source to the target.
	 * 
	 * @param source
	 *            the source module
	 * @param target
	 *            the target module
	 */

	private void visitCssStyleSheets( ReportDesign source, ReportDesign target )
	{
		List sheets = source.getCsses( );
		for ( int i = 0; i < sheets.size( ); i++ )
		{
			CssStyleSheet sheet = (CssStyleSheet) sheets.get( i );
			CssStyleSheet newSheet = visitCssStyleSheet( sheet );

			newSheet.setContainer( target );
			target.addCss( newSheet );
		}

	}

	/**
	 * Localizes a css style sheets.
	 * 
	 */

	private CssStyleSheet visitCssStyleSheet( CssStyleSheet sheet )
	{
		CssStyleSheet newSheet = new CssStyleSheet( );

		newSheet.setFileName( sheet.getFileName( ) );
		List styles = sheet.getStyles( );
		for ( int i = 0; i < styles.size( ); i++ )
		{
			CssStyle style = (CssStyle) styles.get( i );
			CssStyle newStyle = visitCssStyle( style );
			newStyle.setCssStyleSheet( newSheet );

			newSheet.addStyle( newStyle );
		}

		return newSheet;
	}

	/**
	 * Localizes a css style.
	 * 
	 */

	private CssStyle visitCssStyle( CssStyle style )
	{
		CssStyle newStyle = new CssStyle( style.getName( ) );
		localizePrivateStyleProperties( newStyle, style, (Module) style
				.getContainer( ), new HashSet( ) );

		return newStyle;
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

		ElementFactory factory = new ElementFactory( targetDesign );
		DesignElement newElement = factory.newElement(
				element.getDefn( ).getName( ), element.getName( ) )
				.getElement( );

		IElementDefn elementDefn = newElement.getDefn( );
		if ( elementDefn.isContainer( ) )
		{
			int slotCount = elementDefn.getSlotCount( );
			if ( slotCount > 0 )
				visitExternalSlots( element, newElement, slotCount );
			List properties = elementDefn.getContents( );
			if ( properties.size( ) > 0 )
				visitExternalContainerProperties( element, newElement,
						properties );
		}

		localizePropertyValues( element, newElement );
		cacheMapping( element, newElement );

		return newElement;
	}

	/**
	 * Visits the container properties of the given element.
	 * 
	 * @param obj
	 * @param newElement
	 * @param properties
	 */

	private void visitExternalContainerProperties( DesignElement obj,
			DesignElement newElement, List properties )
	{
		elements.push( newElement );
		for ( int i = 0; i < properties.size( ); i++ )
		{
			PropertyDefn propDefn = (PropertyDefn) properties.get( i );
			visitExternalContents( sourceDesign, new ContainerContext( obj,
					propDefn.getName( ) ) );
		}
		elements.pop( );
	}

	/**
	 * Visits the contents of the given context. Allows a derived class to
	 * traverse downward though the design tree.
	 * 
	 * @param module
	 *            the module where the contents reside
	 * @param context
	 *            the container context where the contents reside
	 */

	private void visitExternalContents( Module module, ContainerContext context )
	{
		List contents = context.getContents( module );
		Iterator iter = contents.iterator( );

		DesignElement tmpContainer = (DesignElement) elements.peek( );
		while ( iter.hasNext( ) )
		{
			DesignElement tmpElement = (DesignElement) iter.next( );
			DesignElement cachedExternalElement = getCache( tmpElement );
			if ( cachedExternalElement != null )
			{
				ContainerContext newContext = context
						.createContext( tmpContainer );
				addElement( module, newContext, cachedExternalElement );
			}
			else
			{
				visitExternalElement( tmpElement );
			}
		}
	}

	/**
	 * Creates am element by the given element. The given element must be the
	 * one that is not directly defined in the source design.
	 * 
	 * @param element
	 *            the source element
	 * @param elementRoot
	 *            the root of the element
	 */

	private void visitExternalSelector( Style element, Module elementRoot )
	{
		String tmpStyleName = element.getName( ).toLowerCase( );

		if ( MetaDataDictionary.getInstance( )
				.getPredefinedStyle( tmpStyleName ) == null )
			return;

		Style sourceDesignStyle = (Style) targetDesign
				.findNativeStyle( tmpStyleName );

		if ( sourceDesignStyle == null )
		{
			ElementFactory factory = new ElementFactory( targetDesign );
			DesignElement newElement = factory.newElement(
					element.getDefn( ).getName( ), element.getName( ) )
					.getElement( );

			localizePropertyValues( element, newElement );
			cacheMapping( element, newElement );
			newElement.setName( tmpStyleName );

			return;
		}

		Iterator iter1 = element.propertyWithLocalValueIterator( );
		while ( iter1.hasNext( ) )
		{
			String elem = (String) iter1.next( );

			if ( sourceDesignStyle.getLocalProperty( targetDesign, elem ) != null )
				continue;

			Object value = element.getLocalProperty( elementRoot, elem );
			sourceDesignStyle.setProperty( elem, value );
		}
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

		Module root = element.getRoot( );
		if ( element instanceof IExtendableElement )
			ModelUtil.duplicateExtensionIdentifier( element, newElement, root );

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

			Object value = element.getStrategy( ).getPropertyFromElement( root,
					element, propDefn );

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

					// handle dimension conditions in cube: 1) if the cube
					// resides directly in the design, then handle it in special
					// method 2) if the cube is not in the design but referred
					// by some elements in the design, such as design x-tab
					// extends a library x-tab, and the library x-tab refers a
					// library cube; in this case, no need special handle for
					// dimension condition, so call handleStructureValue is ok.
					if ( newElement instanceof Cube
							&& ITabularCubeModel.DIMENSION_CONDITIONS_PROP
									.equals( propDefn.getName( ) )
							&& element.getRoot( ) == sourceDesign )
						handleDimensionConditions( (Cube) newElement,
								(Cube) element );
					else
						handleStructureValue( newElement, propDefn, value );
					break;
				case IPropertyType.ELEMENT_TYPE :
					break;
				case IPropertyType.CONTENT_ELEMENT_TYPE :
					handleContentElementValue( newElement, propDefn, value );
					break;
				default :
					if ( newElement.getLocalProperty( null, propDefn ) == null )
						newElement.setProperty( propDefn, value );
			}
		}
	}

	/**
	 * 
	 * @param newCube
	 * @param cube
	 */
	private void handleDimensionConditions( Cube newCube, Cube cube )
	{
		if ( cubes.get( cube ) == null )
			cubes.put( newCube, cube );
	}

	/**
	 * 
	 */
	private void localizeDimensionConditions( )
	{
		if ( cubes.isEmpty( ) )
			return;
		Iterator iter = cubes.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Cube newCube = (Cube) iter.next( );
			Cube srcCube = (Cube) cubes.get( newCube );
			List dimensionConditionList = (List) srcCube.getProperty(
					sourceDesign, ITabularCubeModel.DIMENSION_CONDITIONS_PROP );
			List newValueList = new ArrayList( );
			newCube.setProperty( ITabularCubeModel.DIMENSION_CONDITIONS_PROP,
					newValueList );

			// one by one handle the dimension condition
			if ( dimensionConditionList != null )
			{
				for ( int i = 0; i < dimensionConditionList.size( ); i++ )
				{
					DimensionCondition dimensionCond = (DimensionCondition) dimensionConditionList
							.get( i );
					DimensionCondition newDimensionCond = (DimensionCondition) dimensionCond
							.copy( );
					newValueList.add( newDimensionCond );

					// handle hierarchy reference
					ElementRefValue hierarchyRef = (ElementRefValue) dimensionCond
							.getLocalProperty( sourceDesign,
									DimensionCondition.HIERARCHY_MEMBER );
					// if hierarchy is not resolved, do nothing
					if ( hierarchyRef == null || !hierarchyRef.isResolved( ) )
						continue;

					DesignElement hierarchy = hierarchyRef.getElement( );
					assert hierarchy != null;
					int hierarchyIndex = hierarchy.getIndex( sourceDesign );
					DesignElement dimension = hierarchy.getContainer( );
					assert dimension != null;
					int dimensionIndex = dimension.getIndex( sourceDesign );
					// according to the hierarchy index, set the referred
					// hierarchy in the new dimension condition
					DesignElement newDimension = getContent( targetDesign,
							newCube, CubeHandle.DIMENSIONS_PROP, dimensionIndex );
					assert newDimension != null;
					DesignElement newHierarchy = getContent( targetDesign,
							newDimension, DimensionHandle.HIERARCHIES_PROP,
							hierarchyIndex );
					assert newHierarchy != null;
					newDimensionCond.setProperty(
							DimensionCondition.HIERARCHY_MEMBER,
							new ElementRefValue( null, newHierarchy ) );

					// handle all the join conditions
					List joinConditionList = (List) dimensionCond.getProperty(
							sourceDesign,
							DimensionCondition.JOIN_CONDITIONS_MEMBER );
					if ( joinConditionList == null
							|| joinConditionList.isEmpty( ) )
						continue;
					List newJoinConditionList = (List) newDimensionCond
							.getProperty( targetDesign,
									DimensionCondition.JOIN_CONDITIONS_MEMBER );
					for ( int j = 0; j < joinConditionList.size( ); j++ )
					{
						DimensionJoinCondition joinCond = (DimensionJoinCondition) joinConditionList
								.get( j );
						DimensionJoinCondition newJoinCond = (DimensionJoinCondition) newJoinConditionList
								.get( j );
						ElementRefValue levelRef = (ElementRefValue) joinCond
								.getLocalProperty( sourceDesign,
										DimensionJoinCondition.LEVEL_MEMBER );
						if ( levelRef == null || !levelRef.isResolved( ) )
							continue;
						DesignElement level = levelRef.getElement( );
						assert level != null;
						int levelIndex = level.getIndex( sourceDesign );

						// if level is not in the hierarchy that referred by
						// dimension condition, then this dimension condition is
						// invalid, no need to do validation
						if ( level.getContainer( ) != hierarchy )
							continue;

						// according to the level index and set the referred
						// level
						DesignElement newLevel = getContent( targetDesign,
								newHierarchy, HierarchyHandle.LEVELS_PROP,
								levelIndex );
						assert newLevel != null;
						newJoinCond.setProperty(
								DimensionJoinCondition.LEVEL_MEMBER,
								new ElementRefValue( null, newLevel ) );
					}
				}
			}
		}
	}

	private DesignElement getContent( Module module, DesignElement element,
			String propName, int index )
	{
		Object value = element.getProperty( module, propName );
		if ( value == null )
			return null;
		if ( value instanceof List )
		{
			List valueList = (List) value;
			if ( index >= 0 && index < valueList.size( ) )
				return (DesignElement) valueList.get( index );
		}
		else if ( value instanceof DesignElement && index == 0 )
			return (DesignElement) value;

		return null;

	}

	/**
	 * Localize values if the property type is content element or content
	 * element list.
	 * 
	 * @param newElement
	 *            the target element
	 * @param propDefn
	 *            the property definition
	 * @param valueList
	 *            the original property value
	 */

	private void handleContentElementValue( DesignElement newElement,
			PropertyDefn propDefn, Object value )
	{
		elements.push( newElement );
		if ( propDefn.isListType( ) )
		{
			List tmplist = (List) value;
			for ( int i = 0; i < tmplist.size( ); i++ )
			{
				DesignElement tmpElement = (DesignElement) tmplist.get( i );
				tmpElement.apply( this );
			}

			elements.pop( );
			return;
		}

		( (DesignElement) value ).apply( this );
		elements.pop( );

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
			DesignElement target, List processedElement )
	{
		DesignElement sourceContainer = sourceElement.getContainer( );
		long containerId = sourceContainer.getID( );

		DesignElement tmpContainer = null;

		tmpContainer = (DesignElement) externalElements.get( sourceContainer );
		if ( tmpContainer == null )
			tmpContainer = targetDesign.getElementByID( containerId );

		if ( tmpContainer == null )
			return null;

		if ( sourceContainer.getElementName( ).equalsIgnoreCase(
				tmpContainer.getElementName( ) ) )
			return tmpContainer;

		if ( sourceContainer instanceof Module && tmpContainer == targetDesign )
			return tmpContainer;

		if ( sourceContainer instanceof Theme )
			return targetDesign;

		return tmpContainer;
	}

	/**
	 * Visits the member value.
	 * 
	 * @param obj
	 *            the member value to traverse
	 */

	public void visitMemberValue( MemberValue obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the filter condition element.
	 * 
	 * @param obj
	 *            the filter condition element to traverse
	 */

	public void visitFilterConditionElement( FilterConditionElement obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Visits the sort element.
	 * 
	 * @param obj
	 *            the sort element to traverse
	 */

	public void visitSortElement( SortElement obj )
	{
		visitDesignElement( obj );
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by
	 * the meta-data system.
	 * 
	 * @param elementTypeName
	 *            the element type name
	 * @param name
	 *            the optional element name
	 * 
	 * @return design element, <code>null</code> returned if the element
	 *         definition name is not a valid element type name.
	 */

	public DesignElementHandle newElement( String elementTypeName, String name )
	{

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance( )
				.getExtension( elementTypeName );

		// try extension first
		if ( elemDefn != null )
		{
			return newExtensionElement( elementTypeName, name );
		}

		// try other system definitions
		elemDefn = (ElementDefn) MetaDataDictionary.getInstance( ).getElement(
				elementTypeName );
		if ( elemDefn != null )
		{
			DesignElement element = ModelUtil.newElement( targetDesign,
					elementTypeName, name );
			if ( element == null )
				return null;
			return element.getHandle( targetDesign );
		}
		return null;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by
	 * the meta-data system.
	 * 
	 * @param elementTypeName
	 *            the element type name
	 * @param name
	 *            the optional element name
	 * @param targetContext
	 *            the contain context where the created element will be inserted
	 * 
	 * @return design element, <code>null</code> returned if the element
	 *         definition name is not a valid element type name.
	 */

	public DesignElementHandle newElement( String elementTypeName, String name,
			ContainerContext targetContext )
	{

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance( )
				.getExtension( elementTypeName );

		// try extension first
		if ( elemDefn != null )
		{
			return newExtensionElement( elementTypeName, name );
		}

		// try other system definitions
		elemDefn = (ElementDefn) MetaDataDictionary.getInstance( ).getElement(
				elementTypeName );
		if ( elemDefn != null )
		{
			DesignElement element = newElement( targetDesign, targetContext,
					elementTypeName, name );
			if ( element == null )
				return null;
			return element.getHandle( targetDesign );
		}
		return null;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by
	 * the meta-data system.
	 * 
	 * @param module
	 *            the module to create an element
	 * @param targetContainment
	 *            the container context where the created element will be
	 *            inserted
	 * @param elementTypeName
	 *            the element type name
	 * @param name
	 *            the optional element name
	 * 
	 * @return design element, <code>null</code> returned if the element
	 *         definition name is not a valid element type name.
	 */

	public static DesignElement newElement( Module module,
			ContainerContext targetContainment, String elementTypeName,
			String name )
	{

		DesignElement element = ModelUtil.newElement( elementTypeName, name );
		if ( targetContainment.isManagedByNameSpace( ) )
			module.rename( targetContainment.getElement( ), element );
		return element;
	}

	/**
	 * Creates an extension element specified by the extension type name.
	 * 
	 * @param elementTypeName
	 *            the element type name
	 * @param name
	 *            the optional element name
	 * 
	 * @return design element, <code>null</code> returned if the extension
	 *         with the given type name is not found
	 */

	private DesignElementHandle newExtensionElement( String elementTypeName,
			String name )
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd
				.getExtension( elementTypeName );
		if ( extDefn == null )
			return null;
		String extensionPoint = extDefn.getExtensionPoint( );
		if ( PeerExtensionLoader.EXTENSION_POINT
				.equalsIgnoreCase( extensionPoint ) )
			return newExtendedItem( name, elementTypeName );

		return null;
	}

	/**
	 * Creates a new extended item.
	 * 
	 * @param name
	 *            the optional extended item name. Can be <code>null</code>.
	 * @param extensionName
	 *            the required extension name
	 * @return a handle to extended item, return <code>null</code> if the
	 *         definition with the given extension name is not found
	 */

	public ExtendedItemHandle newExtendedItem( String name, String extensionName )
	{
		try
		{
			return newExtendedItem( name, extensionName, null );
		}
		catch ( ExtendsException e )
		{
			assert false;
			return null;
		}
	}

	/**
	 * Creates a new extended item which extends from a given parent.
	 * 
	 * @param name
	 *            the optional extended item name. Can be <code>null</code>.
	 * @param extensionName
	 *            the required extension name
	 * @param parent
	 *            a given parent element.
	 * @return a handle to extended item, return <code>null</code> if the
	 *         definition with the given extension name is not found
	 * @throws ExtendsException
	 */

	private ExtendedItemHandle newExtendedItem( String name,
			String extensionName, ExtendedItemHandle parent )
			throws ExtendsException
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd
				.getExtension( extensionName );
		if ( extDefn == null )
			return null;

		if ( parent != null )
			assert ( (ExtendedItem) parent.getElement( ) ).getExtDefn( ) == extDefn;

		if ( !( extDefn instanceof PeerExtensionElementDefn ) )
			throw new IllegalOperationException(
					"Only report item extension can be created through this method." ); //$NON-NLS-1$

		ExtendedItem element = new ExtendedItem( name );

		// init provider.

		element.setProperty( IExtendedItemModel.EXTENSION_NAME_PROP,
				extensionName );

		if ( parent != null )
		{
			element.getHandle( targetDesign ).setExtends( parent );
		}

		targetDesign.makeUniqueName( element );
		ExtendedItemHandle handle = element.handle( targetDesign );
		try
		{
			handle.loadExtendedElement( );
		}
		catch ( ExtendedElementException e )
		{
			// It's impossible to fail when deserializing.

			assert false;
		}
		return handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitAccessControl(org.eclipse.birt.report.model.elements.AccessControl)
	 */

	public void visitAccessControl( AccessControl obj )
	{
		visitDesignElement( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitValueAccessControl(org.eclipse.birt.report.model.elements.ValueAccessControl)
	 */

	public void visitValueAccessControl( ValueAccessControl obj )
	{
		visitDesignElement( obj );
	}
}
