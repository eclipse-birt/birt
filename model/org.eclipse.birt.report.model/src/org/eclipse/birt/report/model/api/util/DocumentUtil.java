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

package org.eclipse.birt.report.model.api.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.StructureRefUtil;

/**
 * Utility class for the serialize a report design in which all the elements
 * localize their property values from the referred external resources, such as
 * library elements, library embedded images.
 */

public class DocumentUtil
{

	/**
	 * Writes the report design to the given output stream. The caller must call
	 * <code>onSave</code> if the save succeeds.
	 * 
	 * @param designHandle
	 *            the report design to serialize
	 * 
	 * @param out
	 *            the output stream to which the design is written.
	 * @throws IOException
	 *             if the file cannot be written to the output stream
	 *             successfully.
	 */

	public static void serialize( ReportDesignHandle designHandle,
			OutputStream out ) throws IOException
	{
		assert out != null;
		if ( designHandle == null )
			return;

		ReportDesign target = null;
		ReportDesign source = (ReportDesign) designHandle.getModule( );

		// localize element property value

		target = localizeDesign( source );

		assert target != null;

		// clear all the included libraries

		target.setProperty( Module.LIBRARIES_PROP, null );

		target.handle( ).serialize( out );
	}

	/**
	 * Gets a localized report design based on the source design.
	 * 
	 * @param source
	 *            the source design
	 * @return the localized report design based on the source design
	 */

	static ReportDesign localizeDesign( ReportDesign source )
	{
		if ( source == null )
			return null;

		// now the clone method already localizes all the property values

		ReportDesign target = null;

		target = (ReportDesign) ModelUtil.getCopy( source );

		IElementDefn defn = target.getDefn( );
		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			switch ( i )
			{
				// elements in the style slot, data source slot must not
				// have element reference of lib resource except "extends"
				// reference

				case IReportDesignModel.STYLE_SLOT :
				case IModuleModel.DATA_SOURCE_SLOT :
					break;
				case IModuleModel.PARAMETER_SLOT :
				case IModuleModel.DATA_SET_SLOT :
				case IModuleModel.PAGE_SLOT :
				case IModuleModel.COMPONENT_SLOT :
				case IReportDesignModel.BODY_SLOT :
				case IReportDesignModel.SCRATCH_PAD_SLOT :
				case IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT :
					resolveSlot( target, source, i );
					break;
			}
		}

		return target;
	}

	/**
	 * Resolves element references of all the elements in any level of the slot.
	 * 
	 * @param targetDesign
	 *            the target design to resolve the element references
	 * @param sourceDesign
	 *            the source design
	 * @param slot
	 *            the slot id of the report design
	 */

	static void resolveSlot( ReportDesign targetDesign,
			ReportDesign sourceDesign, int slot )
	{
		if ( targetDesign == null || sourceDesign == null )
			return;

		ContentIterator iter = new ContentIterator( targetDesign, slot );
		while ( iter.hasNext( ) )
		{
			DesignElement target = (DesignElement) iter.next( );
			DesignElement source = sourceDesign
					.getElementByID( target.getID( ) );

			if ( source != null )
				resolveElementReference( targetDesign, sourceDesign, target,
						source );
		}
	}

	/**
	 * Resolves all the element references in the target element. This method
	 * will copy the resolved element reference from the source element to the
	 * target design.
	 * 
	 * @param targetDesign
	 *            the target design to add the copied element
	 * @param sourceDesign
	 *            the source design from which to get the copy
	 * @param target
	 *            the target element to resolve the element references
	 * @param source
	 *            the source element
	 */

	static void resolveElementReference( ReportDesign targetDesign,
			ReportDesign sourceDesign, DesignElement target,
			DesignElement source )
	{
		if ( targetDesign == null || target == null || source == null )
			return;

		assert target.getDefn( ) == source.getDefn( );

		if ( source instanceof CascadingParameterGroup
				|| source instanceof ScalarParameter )
		{
			resolveDataSet( targetDesign, sourceDesign, target, source );
		}
		else if ( source instanceof SimpleDataSet )
		{
			resolveDataSource( targetDesign, sourceDesign, target, source );
		}
		else if ( source instanceof JointDataSet )
		{
			resolveDataSet( targetDesign, sourceDesign, target, source );
		}
		else if ( source instanceof MasterPage )
		{
			resolveStyle( targetDesign, target, source );
		}
		else if ( source instanceof ReportItem )
		{
			if ( source instanceof ImageItem )
			{
				resolveImageItem( targetDesign, sourceDesign, target, source );
			}

			resolveDataSet( targetDesign, sourceDesign, target, source );
			resolveStyle( targetDesign, target, source );
		}
		else if ( source instanceof StyledElement )
		{
			resolveStyle( targetDesign, target, source );
		}

	}

	/**
	 * if the image item reference a library image, which uses a library
	 * embedded image, copy the embedded image into the target design.
	 * 
	 * @param targetDesign
	 *            the target design to add the copied embedded image
	 * @param sourceDesign
	 *            the source design from which to get the copy
	 * @param target
	 *            the target element to resolve the embedded image
	 * @param source
	 *            the source element
	 */

	static void resolveImageItem( ReportDesign targetDesign,
			ReportDesign sourceDesign, DesignElement target,
			DesignElement source )
	{
		assert targetDesign != null && target != null && source != null;
		assert source instanceof ImageItem;
		assert target instanceof ImageItem;

		StructRefValue embeddedImage = (StructRefValue) source.getProperty(
				sourceDesign, ImageItem.IMAGE_NAME_PROP );

		if ( embeddedImage == null || !embeddedImage.isResolved( ) )
			return;

		if ( !isLocalImage( embeddedImage.getName( ), sourceDesign ) )
		{

			EmbeddedImage targetEmbeddedImage = (EmbeddedImage) embeddedImage
					.getTargetStructure( ).copy( );

			targetDesign.rename( targetEmbeddedImage );

			List images = targetDesign.getListProperty(
					targetDesign.getRoot( ), ReportDesign.IMAGES_PROP );

			if ( images == null )
			{
				images = new ArrayList( );
				targetDesign.setProperty( ReportDesign.IMAGES_PROP, images );
			}
			// add the embedded image into the target report deisgn.
			images.add( targetEmbeddedImage );

			StructRefValue structRef = new StructRefValue( null,
					targetEmbeddedImage.getName( ) );

			target.setProperty( ImageItem.IMAGE_NAME_PROP, structRef );
		}
	}

	/**
	 * 
	 * 
	 * @param imageName
	 * @param sourceDesign
	 * @return
	 */
	private static boolean isLocalImage( String imageName,
			ReportDesign sourceDesign )
	{
		StructureDefn defn = (StructureDefn) MetaDataDictionary.getInstance( )
				.getStructure( EmbeddedImage.EMBEDDED_IMAGE_STRUCT );

		if ( StructureRefUtil.findNativeStructure( sourceDesign, defn,
				imageName ) != null )
			return true;

		return false;

	}

	/**
	 * Resolves data source reference in the target element. This method will
	 * copy the resolved element reference from the source element to the target
	 * design.
	 * 
	 * @param targetDesign
	 *            the target design to add the copied data source
	 * @param sourceDesign
	 *            the source design from which to get the copy
	 * @param target
	 *            the target element to resolve the data source
	 * @param source
	 *            the source element
	 */

	static void resolveDataSource( ReportDesign targetDesign,
			ReportDesign sourceDesign, DesignElement target,
			DesignElement source )
	{
		assert targetDesign != null && target != null && source != null;
		assert target instanceof SimpleDataSet;
		assert source instanceof SimpleDataSet;

		Module sourceModule = source.getRoot( );
		assert sourceModule != null;
		DesignElement dataSource = source.getReferenceProperty( sourceModule,
				SimpleDataSet.DATA_SOURCE_PROP );

		// handle only when the data source is not local but a library resource

		if ( dataSource != null && sourceDesign != dataSource.getRoot( ) )
		{

			DesignElement copied = ModelUtil.getCopy( dataSource );
			addElement( targetDesign, copied, ReportDesign.DATA_SOURCE_SLOT );
			target.setProperty( SimpleDataSet.DATA_SOURCE_PROP,
					new ElementRefValue( null, copied ) );

			// recursively resolve the copied element

			resolveElementReference( targetDesign, sourceDesign, copied,
					dataSource );

		}
	}

	/**
	 * Resolves data set reference in the target element. This method will copy
	 * the resolved element reference from the source element to the target
	 * design.
	 * 
	 * @param targetDesign
	 *            the target design to add the copied data set
	 * @param sourceDesign
	 *            the source design from which to get the copy
	 * @param target
	 *            the target element to resolve the data set
	 * @param source
	 *            the source element
	 */

	static void resolveDataSet( ReportDesign targetDesign,
			ReportDesign sourceDesign, DesignElement target,
			DesignElement source )
	{
		assert targetDesign != null && target != null && source != null;

		if ( source instanceof JointDataSet )
		{
			Module sourceModule = source.getRoot( );
			assert sourceModule != null;

			List dataSets = source.getListProperty( sourceModule,
					JointDataSet.DATA_SETS_PROP );
			if ( dataSets != null )
			{
				List values = new ArrayList( );
				for ( int i = 0; i < dataSets.size( ); i++ )
				{
					ElementRefValue value = (ElementRefValue) dataSets.get( i );

					// handle only when the data set is not local but a library
					// resource

					if ( value.isResolved( )
							&& value.getElement( ).getRoot( ) != sourceDesign )
					{

						DesignElement copied = ModelUtil.getCopy( value
								.getElement( ) );
						addElement( targetDesign, copied,
								ReportDesign.DATA_SET_SLOT );

						// recursively resolve the copied element

						resolveElementReference( targetDesign, sourceDesign,
								copied, value.getElement( ) );

						// TODO: handle back-ref in another place, then
						// remove this patch

						values.add( new ElementRefValue( null, copied ) );
						( (ReferenceableElement) copied ).addClient( target,
								JointDataSet.DATA_SETS_PROP );

					}
					else
						values.add( value.copy( ) );
				}
				target.setProperty( JointDataSet.DATA_SETS_PROP, values );
			}
		}
		else
		{
			String propName = null;
			if ( source instanceof CascadingParameterGroup )
			{
				propName = CascadingParameterGroup.DATA_SET_PROP;
			}
			else if ( source instanceof ScalarParameter )
			{
				propName = ScalarParameter.DATASET_NAME_PROP;
			}
			else if ( source instanceof ReportItem )
			{
				propName = ReportItem.DATA_SET_PROP;
			}
			else
			{
				assert false;
			}

			Module sourceModule = source.getRoot( );
			assert sourceModule != null;
			DesignElement dataSet = source.getReferenceProperty( sourceModule,
					propName );

			// handle only when the data set is not local but library resource

			if ( dataSet != null && dataSet.getRoot( ) != sourceDesign )
			{

				DesignElement copied = ModelUtil.getCopy( dataSet );
				addElement( targetDesign, copied, ReportDesign.DATA_SET_SLOT );
				target.setProperty( propName,
						new ElementRefValue( null, copied ) );

				// recursively resolve the copied element

				resolveElementReference( targetDesign, sourceDesign, copied,
						dataSet );

			}
		}
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

	static void resolveStyle( ReportDesign targetDesign, DesignElement target,
			DesignElement source )
	{
		assert targetDesign != null && target != null && source != null;
		assert target instanceof StyledElement;
		assert source instanceof StyledElement;

		Module sourceModule = source.getRoot( );
		assert sourceModule != null;
		DesignElement style = ( (StyledElement) source )
				.getStyle( sourceModule );

		// handle only when the style is not local one but a library resource

		if ( style != null && style.getRoot( ) != sourceModule )
		{
			// clear the style reference of the target element

			( (StyledElement) target ).setStyle( null );

			localizeStyleProperties( target, style, targetDesign );

		}

		// handle only when the style is not local one but a library resource

		String selector = ( (ElementDefn) target.getDefn( ) ).getSelector( );
		if ( selector != null )
		{
			style = sourceModule.resolveElement( selector,
					Module.STYLE_NAME_SPACE, null );
			if ( style != null && style.getRoot( ) != sourceModule )
			{
				localizeStyleProperties( target, style, targetDesign );
			}
		}
	}

	/**
	 * Copies properties from <code>style</code> to the <code>target</code>.
	 * 
	 * @param target
	 *            the target element
	 * @param style
	 *            the style element
	 * @param targetDesign
	 *            the module of the target element
	 */

	private static void localizeStyleProperties( DesignElement target,
			DesignElement style, Module targetDesign )
	{
		Module root = style.getRoot( );
		assert root != null;

		// copy all the local values in the style

		IElementDefn defn = style.getDefn( );
		Iterator iter = defn.getPropertyIterator( );
		while ( iter.hasNext( ) )
		{
			PropertyDefn prop = (PropertyDefn) iter.next( );
			Object value = style.getLocalProperty( root, prop.getName( ) );
			if ( value != null )
			{
				// only handle values that not set in the target element

				if ( target.getPropertyDefn( prop.getName( ) ) != null
						&& target.getLocalProperty( targetDesign,
								(ElementPropertyDefn) prop ) == null )
					target.setProperty( prop.getName( ), value );
			}
		}
	}

	/**
	 * Adds an element to the slot of the design. Add the name to the name space
	 * and element id to idMap.
	 * 
	 * @param root
	 *            the report design to add
	 * @param element
	 *            the element to add
	 * @param slot
	 *            the slot id of report design
	 */

	static private void addElement( ReportDesign root, DesignElement element,
			int slot )
	{
		assert root != null;
		assert element != null;

		root.getSlot( slot ).add( element );
		element.setContainer( root, slot );

		// add element to idMap

		root.manageId( element, true );

		// add name to namespace, all the reference elements must have a name

		root.makeUniqueName( element );
		assert !StringUtil.isBlank( element.getName( ) );
		int ns = ( (ElementDefn) element.getDefn( ) ).getNameSpaceID( );
		root.getNameSpace( ns ).insert( element );

	}

}
