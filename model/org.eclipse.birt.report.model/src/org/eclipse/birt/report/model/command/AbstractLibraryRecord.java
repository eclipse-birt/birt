/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
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

abstract class AbstractLibraryRecord extends SimpleRecord {

	/**
	 * The target module
	 */

	protected Module module;

	/**
	 * The library to operate
	 */

	protected Library library;

	/**
	 * Constructs the library record.
	 *
	 * @param module
	 * @param library
	 */
	AbstractLibraryRecord(Module module, Library library) {
		this.module = module;
		this.library = library;
	}

	/**
	 * Constructs the library record.
	 *
	 * @param module
	 */
	AbstractLibraryRecord(Module module) {
		this.module = module;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	@Override
	public DesignElement getTarget() {
		return module;
	}

	/**
	 * Updates the style reference using the element in the given library list.
	 *
	 * @param updateSize the size of the library that needs to be updated
	 */

	public void updateReferenceableClients(int updateSize) {
		List<Library> libraries = module.getLibraries();

		for (int i = 0; i < updateSize; i++) {
			updateReferenceableClients(libraries.get(i));
		}
	}

	/**
	 * Updates the theme/style element reference which refers to the given library.
	 *
	 * @param library the library whose element references are updated.
	 */

	public void updateReferenceableClients(Library library) {
		updateReferenceableClients(library, ILibraryModel.THEMES_SLOT);

		// update the clents of data source, data set and cube: in special
		// cases: design element can directly refer element in library rather
		// than "extends" property.
		updateReferenceableClients(library, IModuleModel.DATA_SOURCE_SLOT);
		updateReferenceableClients(library, IModuleModel.DATA_SET_SLOT);
		updateReferenceableClients(library, ILibraryModel.CUBE_SLOT);

		// update clients of embedded images

		List<Object> images = library.getListProperty(library, IModuleModel.IMAGES_PROP);
		if (images == null || images.isEmpty()) {
			return;
		}
		boolean sendEvent = false;
		for (int i = 0; i < images.size(); i++) {
			EmbeddedImage image = (EmbeddedImage) images.get(i);
			List<Structure> clients = image.getClientStructures();
			if (clients == null || clients.isEmpty()) {
				continue;
			}
			for (int j = 0; j < clients.size(); j++) {
				Structure client = clients.get(j);
				StructRefValue value = (StructRefValue) client.getLocalProperty(module,
						ReferencableStructure.LIB_REFERENCE_MEMBER);
				assert value != null;
				value.unresolved(value.getName());
				image.dropClientStructure(client);
				sendEvent = true;
			}

			List<BackRef> clientsRef = image.getClientList();
			if (clientsRef == null || clientsRef.isEmpty()) {
				continue;
			}
			for (int j = 0; j < clientsRef.size(); j++) {
				BackRef client = clientsRef.get(j);
				DesignElement element = client.getElement();

				StructRefValue value = (StructRefValue) element.getLocalProperty(module, client.getPropertyName());
				assert value != null;
				value.unresolved(value.getName());
				image.dropClient(element);
				element.broadcast(new PropertyEvent(module, client.getPropertyName()));
			}
		}

		// send the property event to current module

		if (sendEvent) {
			module.broadcast(new PropertyEvent(module, IModuleModel.IMAGES_PROP));
		}
	}

	/**
	 * Updates the element reference which refers to the given library.
	 *
	 * @param target the library whose element references are updated.
	 * @param slotId the id of themes/styles slot
	 */

	private void updateReferenceableClients(DesignElement target, int slotId) {
		ContainerSlot slot = target.getSlot(slotId);
		Iterator<DesignElement> iter = slot.iterator();
		while (iter.hasNext()) {
			DesignElement element = iter.next();
			assert element instanceof IReferencableElement;

			IReferencableElement referenceableElement = (IReferencableElement) element;

			// first unresolve theme itself first

			referenceableElement.updateClientReferences();

			// removes references of styles in the theme

			if (referenceableElement instanceof Theme) {
				updateReferenceableClients((DesignElement) referenceableElement, IThemeModel.STYLES_SLOT);

				// removes references of css styles in the theme. for bugzilla
				// 192171
				List<CssStyleSheet> csses = ((ICssStyleSheetOperation) referenceableElement).getCsses();
				Iterator<CssStyleSheet> cssIterator = csses.iterator();
				while (cssIterator.hasNext()) {
					CssStyleSheet styleSheet = cssIterator.next();
					List<CssStyle> styles = styleSheet.getStyles();
					Iterator<CssStyle> styleIterator = styles.iterator();
					while (styleIterator.hasNext()) {
						CssStyle cssStyle = styleIterator.next();
						cssStyle.updateClientReferences();
					}
				}

			}
		}
	}
}
