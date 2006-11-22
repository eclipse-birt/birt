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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Exports elements or structures to library. This class contains the handle for
 * target library and encapsulates the main logicas for exporting.
 */

class ElementExporter
{

	private LibraryHandle targetLibraryHandle;

	/**
	 * Constructs the exporter with the handle of target library.
	 * 
	 * @param libraryHandle
	 *            handle of the target library
	 */

	ElementExporter( LibraryHandle libraryHandle )
	{
		this.targetLibraryHandle = libraryHandle;
	}

	/**
	 * Checks whether the given element is suitable for exporting.
	 * <ul>
	 * <li>The element must be in design file.
	 * <li>The element must have name.
	 * </ul>
	 * 
	 * @param elementToExport
	 *            handle of the element to export
	 */

	static void checkElementToExport( DesignElementHandle elementToExport )
	{
		ModuleHandle root = elementToExport.getRoot( );
		if ( !( root instanceof ReportDesignHandle ) )
		{
			throw new IllegalArgumentException(
					"The element to export must be in design file." ); //$NON-NLS-1$
		}

		if ( StringUtil.isBlank( elementToExport.getName( ) ) )
		{
			throw new IllegalArgumentException(
					"The element must have name defined." ); //$NON-NLS-1$
		}
	}

	/**
	 * Checks whether the given structure is suitable for exporting.
	 * <ul>
	 * <li>The structure must be in design file.
	 * <li>The structure must have name property value.
	 * <li>The structure must be one of <code>EmbeddedImage</code>,
	 * <code>CustomColor</code> and <code>ConfigVariable</code>.
	 * </ul>
	 * 
	 * @param structToExport
	 *            handle of the structure to export
	 */

	static void checkStructureToExport( StructureHandle structToExport )
	{
		String memberName = null;
		String propName = null;

		// Check whether the structure is allowed to export.

		String structName = structToExport.getDefn( ).getName( );
		if ( EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equals( structName ) )
		{
			propName = IModuleModel.IMAGES_PROP;
			memberName = EmbeddedImage.NAME_MEMBER;
		}
		else if ( CustomColor.CUSTOM_COLOR_STRUCT.equals( structName ) )
		{
			propName = IModuleModel.COLOR_PALETTE_PROP;
			memberName = CustomColor.NAME_MEMBER;
		}
		else if ( ConfigVariable.CONFIG_VAR_STRUCT.equals( structName ) )
		{
			propName = IModuleModel.CONFIG_VARS_PROP;
			memberName = ConfigVariable.NAME_MEMBER;
		}
		else
		{
			throw new IllegalArgumentException( "The structure \"" //$NON-NLS-1$
					+ structName + "\" is not allowed to export." ); //$NON-NLS-1$
		}

		// Check whether the name property value is defined.

		Object value = structToExport.getMember( memberName ).getValue( );
		if ( StringUtil.isBlank( (String) value ) )
		{
			throw new IllegalArgumentException( "The structure \"" //$NON-NLS-1$
					+ structName
					+ "\" must have member \"" + memberName + "\" defined." ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Check whether this structure is in design file.

		boolean found = false;
		ModuleHandle moduleHandle = structToExport.getElementHandle( )
				.getModuleHandle( );
		PropertyHandle propertyHandle = moduleHandle
				.getPropertyHandle( propName );

		List list = propertyHandle.getListValue( );
		if ( list != null )
		{
			Iterator iter = list.iterator( );
			while ( iter.hasNext( ) )
			{
				Structure struct = (Structure) iter.next( );
				if ( struct == structToExport.getStructure( ) )
				{
					found = true;
					break;
				}
			}
		}

		if ( !found )
		{
			throw new IllegalArgumentException(
					"The structure to export must be in design file." ); //$NON-NLS-1$
		}
	}

	/**
	 * Exports the given element.
	 * 
	 * @param structToExport
	 *            handle of the structure to export.
	 * @param canOverride
	 *            indicates whether the structure with the same name in target
	 *            library will be overriden.
	 * 
	 * @throws SemanticException
	 *             if error encountered when adding this structure to target
	 *             library or duplicating member value from the given structure.
	 */

	void exportStructure( StructureHandle structToExport, boolean canOverride )
			throws SemanticException
	{
		String structName = structToExport.getDefn( ).getName( );
		String nameMemberName = null;

		Structure newStruct = null;
		PropertyHandle newPropertyHandle = null;

		if ( EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equals( structName ) )
		{
			nameMemberName = EmbeddedImage.NAME_MEMBER;
			newStruct = StructureFactory.createEmbeddedImage( );
			newPropertyHandle = targetLibraryHandle
					.getPropertyHandle( IModuleModel.IMAGES_PROP );
		}
		else if ( CustomColor.CUSTOM_COLOR_STRUCT.equals( structName ) )
		{
			nameMemberName = CustomColor.NAME_MEMBER;
			newStruct = StructureFactory.createCustomColor( );
			newPropertyHandle = targetLibraryHandle
					.getPropertyHandle( IModuleModel.COLOR_PALETTE_PROP );
		}
		else if ( ConfigVariable.CONFIG_VAR_STRUCT.equals( structName ) )
		{
			nameMemberName = ConfigVariable.NAME_MEMBER;
			newStruct = StructureFactory.createConfigVar( );
			newPropertyHandle = targetLibraryHandle
					.getPropertyHandle( IModuleModel.CONFIG_VARS_PROP );
		}
		else
		{
			throw new IllegalArgumentException( "The structure \"" //$NON-NLS-1$
					+ structName + "\" is not allowed to export." ); //$NON-NLS-1$
		}

		assert newStruct != null;
		assert newPropertyHandle != null;

		if ( canOverride )
		{
			Object nameValue = structToExport.getMember( nameMemberName )
					.getValue( );
			if ( nameValue != null )
			{
				Iterator iter = newPropertyHandle.iterator( );
				while ( iter.hasNext( ) )
				{
					StructureHandle structureHandle = (StructureHandle) iter
							.next( );
					Object value = structureHandle.getMember( nameMemberName )
							.getValue( );

					if ( nameValue.equals( value ) )
					{
						IStructure struct = structureHandle.getStructure( );
						newPropertyHandle.removeItem( struct );
						break;
					}
				}
			}
		}

		Iterator iter = structToExport.getDefn( ).propertiesIterator( );
		while ( iter.hasNext( ) )
		{
			PropertyDefn memberDefn = (PropertyDefn) iter.next( );
			String memberName = memberDefn.getName( );

			if ( ReferencableStructure.LIB_REFERENCE_MEMBER.equals( memberName ) )
				continue;

			Object value = structToExport.getMember( memberName ).getValue( );
			Object valueToSet = ModelUtil.copyValue( memberDefn, value );

			newStruct.setProperty( memberName, valueToSet );
		}

		newPropertyHandle.addItem( newStruct );
	}

	/**
	 * Exports the given element.
	 * 
	 * @param elementToExport
	 *            handle of the element to export.
	 * @param canOverride
	 *            indicates whether the element with the same name in target
	 *            library will be overriden.
	 * 
	 * @throws SemanticException
	 *             if error encountered when adding this element to target
	 *             library or duplicating property value from the given element.
	 */

	void exportElement( DesignElementHandle elementToExport, boolean canOverride )
			throws SemanticException
	{
		if ( elementToExport instanceof StyleHandle )
		{
			exportStyle( (StyleHandle) elementToExport, canOverride );
			return;
		}

		if ( canOverride )
		{
			int nameSpaceID = ( (ElementDefn) elementToExport.getDefn( ) )
					.getNameSpaceID( );
			NameSpace nameSpace = targetLibraryHandle.getModule( )
					.getNameSpace( nameSpaceID );

			DesignElement duplicateElement = nameSpace
					.getElement( elementToExport.getName( ) );
			if ( duplicateElement != null )
				duplicateElement.getHandle( targetLibraryHandle.getModule( ) )
						.drop( );
		}

		int slotID = getTopContainerSlot( elementToExport.getElement( ) );

		// The element in body slot should be added into components slot.

		if ( slotID == IReportDesignModel.BODY_SLOT )
			slotID = IModuleModel.COMPONENT_SLOT;
		else if ( slotID == IModuleModel.PAGE_SLOT
				&& elementToExport.getContainer( ) != elementToExport
						.getModuleHandle( ) )
			slotID = IModuleModel.COMPONENT_SLOT;

		// if the element only exist in the report design such as template
		// element definition, do not export it.

		if ( slotID >= ILibraryModel.SLOT_COUNT )
			return;

		DesignElementHandle newElementHandle = duplicateElement(
				elementToExport, false );

		SlotHandle slotHandle = targetLibraryHandle.getSlot( slotID );
		addToSlot( slotHandle, newElementHandle );
	}

	/**
	 * Export the given style to the target library.
	 * 
	 * @param elementToExport
	 *            the style to export
	 * @param canOverride
	 *            <code>true</code> indicates the element with the same name
	 *            in target library will be overriden. Otherwise
	 *            <code>false</code>.
	 * @throws SemanticException
	 */

	void exportStyle( StyleHandle elementToExport, boolean canOverride )
			throws SemanticException
	{
		SlotHandle themes = targetLibraryHandle.getThemes( );
		String defaultThemeName = ModelMessages
				.getMessage( IThemeModel.DEFAULT_THEME_NAME );

		// find the default theme

		NameSpace nameSpace = targetLibraryHandle.getModule( ).getNameSpace(
				Module.THEME_NAME_SPACE );

		Theme theme = (Theme) nameSpace.getElement( defaultThemeName );
		ThemeHandle themeHandle = null;

		// if no default theme, create it in the themes slot.

		if ( theme == null )
		{
			themeHandle = targetLibraryHandle.getElementFactory( ).newTheme(
					defaultThemeName );
			themes.add( themeHandle );
		}
		else
			themeHandle = (ThemeHandle) theme.getHandle( targetLibraryHandle
					.getModule( ) );

		exportStyle( elementToExport, themeHandle, canOverride );
	}

	/**
	 * Export the given style to the target library.
	 * 
	 * @param elementToExport
	 *            the style to export
	 * @param theme
	 *            the theme where the style exports.
	 * @param canOverride
	 *            <code>true</code> indicates the element with the same name
	 *            in target library will be overriden. Otherwise
	 *            <code>false</code>.
	 * @throws SemanticException
	 */

	void exportStyle( StyleHandle elementToExport, ThemeHandle theme,
			boolean canOverride ) throws SemanticException
	{
		assert theme != null;

		if ( canOverride )
		{
			StyleHandle style = theme.findStyle( elementToExport.getName( ) );
			if ( style != null )
				style.drop( );
		}

		DesignElementHandle newElementHandle = duplicateElement(
				elementToExport, false );
		addToSlot( theme.getStyles( ), newElementHandle );
	}

	/**
	 * Exports the given design. The following rules are applied on exporting.
	 * <ul>
	 * <li>Only properties supported by library are exported.
	 * <li>Only top-level element with name are exported.
	 * </ul>
	 * 
	 * @param designToExport
	 *            handle of the report design to export.
	 * @param canOverride
	 *            indicates whether the element with the same name in target
	 *            library will be overriden.
	 * @param genDefaultName
	 *            if true, a default name will be generated if an element
	 *            doesn't has a name. if false, an exception will be throwed
	 *            indicate that the element to export must has a name
	 * @throws SemanticException
	 *             if error encountered when adding this element to target
	 *             library or duplicating property value from the given element.
	 */

	void exportDesign( ReportDesignHandle designToExport, boolean canOverride,
			boolean genDefaultName ) throws SemanticException
	{
		ModelUtil.duplicateProperties( designToExport, targetLibraryHandle,
				false );

		// Copy the contents in design file.

		int slotCount = designToExport.getDefn( ).getSlotCount( );
		for ( int i = 0; i < slotCount; i++ )
		{
			SlotHandle sourceSlotHandle = designToExport.getSlot( i );
			Iterator iter = sourceSlotHandle.iterator( );
			
			//First export element which has name.
			
			List noNameList = new ArrayList( );
			while ( iter.hasNext( ) )
			{
				DesignElementHandle contentHandle = (DesignElementHandle) iter
						.next( );

				if ( StringUtil.isBlank( contentHandle.getName( ) ) )
				{
					noNameList.add( contentHandle );
				}
				else
				{
					exportElement( contentHandle, canOverride );
				}
			}

			//Second export element which has no name.
			
			iter = noNameList.iterator( );
			while ( iter.hasNext( ) )
			{
				DesignElementHandle contentHandle = (DesignElementHandle) iter
						.next( );
				if ( !genDefaultName )
				{
					String typeName = contentHandle.getDefn( ).getDisplayName( );
					String location = contentHandle.getElement( )
							.getIdentifier( );

					throw new IllegalArgumentException(
							"The element [type=\"" + typeName + "\"," //$NON-NLS-1$//$NON-NLS-2$ 
									+ "location=\"" + location + "\"] must have name defined." ); //$NON-NLS-1$ //$NON-NLS-2$
				}

				targetLibraryHandle.getModule( ).makeUniqueName(
						contentHandle.getElement( ) );

				exportElement( contentHandle, canOverride );
			}
		}
	}

	private int getTopContainerSlot( DesignElement element )
	{
		int slotID = element.getContainerSlot( );

		DesignElement container = element.getContainer( );
		while ( !( container instanceof Module ) )
		{
			slotID = container.getContainerSlot( );
			container = container.getContainer( );
			assert container != null;
		}

		return slotID;
	}

	/**
	 * Duplicates the given element in target module, including properties and
	 * contents.
	 * 
	 * @param elementHandle
	 *            handle of the element to duplicate
	 * @param onlyFactoryProperty
	 *            indicate whether only factory property values are duplicated.
	 * 
	 * @return the handle of the duplicated element
	 * @throws SemanticException
	 *             if error encountered when setting property or adding content
	 *             into slot.
	 */

	private DesignElementHandle duplicateElement(
			DesignElementHandle elementHandle, boolean onlyFactoryProperty )
			throws SemanticException
	{
		String elementName = elementHandle.getDefn( ).getName( );
		String name = elementHandle.getName( );

		// Create one element which has the same name as the one to export.

		DesignElementHandle newElementHandle = targetLibraryHandle
				.getElementFactory( ).newElement( elementName, name );

		// Copy all properties from the original one to new element.

		ModelUtil.duplicateProperties( elementHandle, newElementHandle,
				onlyFactoryProperty );

		// Duplicate all contents in the original element to new one.

		duplicateSlots( elementHandle, newElementHandle );

		return newElementHandle;
	}

	/**
	 * Duplicates the content elements from source element to destination
	 * element.
	 * 
	 * @param source
	 *            handle of the element to duplicate
	 * @param destination
	 *            handle of of the destination element
	 * @throws SemanticException
	 *             if error encountered when adding contents into slot.
	 */

	private void duplicateSlots( DesignElementHandle source,
			DesignElementHandle destination ) throws SemanticException
	{
		int slotCount = source.getDefn( ).getSlotCount( );
		for ( int i = 0; i < slotCount; i++ )
		{
			SlotHandle sourceSlotHandle = source.getSlot( i );
			SlotHandle destinationSlotHandle = destination.getSlot( i );

			Iterator iter = sourceSlotHandle.iterator( );
			while ( iter.hasNext( ) )
			{
				DesignElementHandle contentHandle = (DesignElementHandle) iter
						.next( );

				DesignElementHandle newContentHandle = duplicateElement(
						contentHandle, true );

				addToSlot( destinationSlotHandle, newContentHandle );
			}
		}
	}

	private void addToSlot( SlotHandle slotHandle,
			DesignElementHandle contentHandle ) throws SemanticException
	{
		slotHandle.add( contentHandle );
	}
}