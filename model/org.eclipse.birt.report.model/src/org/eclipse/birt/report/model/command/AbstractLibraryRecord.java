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

package org.eclipse.birt.report.model.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * This class is the base class for all library records. The target of records
 * are always module.
 * 
 */

abstract class AbstractLibraryRecord extends SimpleRecord
{

	/**
	 * The target module
	 */

	protected Module module;

	/**
	 * The library to operate
	 */

	protected Library library;

	AbstractLibraryRecord( Module module, Library library )
	{
		this.module = module;
		this.library = library;
	}

	AbstractLibraryRecord( Module module )
	{
		this.module = module;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return module;
	}

	/**
	 * Updates the style reference using the element in the given library list.
	 * 
	 * @param librariesToUpdate
	 *            library list
	 */

	public void updateReferenceableClients( List librariesToUpdate )
	{
		int size = librariesToUpdate.size( );

		List libraries = module.getLibraries( );

		for ( int i = 0; i < size; i++ )
		{
			updateReferenceableClients( (Library) libraries.get( i ) );
		}
	}

	/**
	 * Updates the theme/style element reference which refers to the given
	 * library.
	 * 
	 * @param library
	 *            the library whose element references are updated.
	 */

	public void updateReferenceableClients( Library library )
	{
		updateReferenceableClients( library, ILibraryModel.THEMES_SLOT );

		// update clients of embedded images

		List images = library.getListProperty( library, IModuleModel.IMAGES_PROP );
		if ( images == null || images.isEmpty( ) )
			return;
		boolean sendEvent = false;
		for ( int i = 0; i < images.size( ); i++ )
		{
			EmbeddedImage image = (EmbeddedImage) images.get( i );
			List clients = image.getClientStructures( );
			if ( clients == null || clients.isEmpty( ) )
				continue;
			for ( int j = 0; j < clients.size( ); j++ )
			{
				Structure client = (Structure) clients.get( j );
				StructRefValue value = (StructRefValue) client
						.getLocalProperty( module,
								ReferencableStructure.LIB_REFERENCE_MEMBER );
				assert value != null;
				value.unresolved( value.getName( ) );
				image.dropClientStructure( client );
				sendEvent = true;
			}

			clients = image.getClientList( );
			if ( clients == null || clients.isEmpty( ) )
				continue;
			for ( int j = 0; j < clients.size( ); j++ )
			{
				BackRef client = (BackRef) clients.get( j );
				DesignElement element = client.getElement( );

				StructRefValue value = (StructRefValue) element
						.getLocalProperty( module, client.getPropertyName( ) );
				assert value != null;
				value.unresolved( value.getName( ) );
				image.dropClient( element );
				element.broadcast( new PropertyEvent( module, client
						.getPropertyName( ) ) );
			}
		}

		// send the property event to current module

		if ( sendEvent )
			module.broadcast( new PropertyEvent( module, IModuleModel.IMAGES_PROP ) );
	}

	/**
	 * Updates the element reference which refers to the given library.
	 * 
	 * @param target
	 *            the library whose element references are updated.
	 * @param slotId
	 *            the id of themes/styles slot
	 */

	private void updateReferenceableClients( DesignElement target, int slotId )
	{
		ContainerSlot slot = target.getSlot( slotId );
		Iterator iter = slot.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElement element = (DesignElement) iter.next( );
			assert element instanceof ReferenceableElement;

			ReferenceableElement referenceableElement = (ReferenceableElement) element;

			// first unresolve theme itself first

			referenceableElement.updateClientReferences( );

			// removes references of styles in the theme

			if ( referenceableElement instanceof Theme )
				updateReferenceableClients( referenceableElement,
						IThemeModel.STYLES_SLOT );
		}
	}

}
