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

import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.LibraryEvent;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.ElementStructureUtil;

/**
 * Records to add/drop library.
 */

class LibraryRecord extends AbstractLibraryRecord {

	/**
	 * The position of the library
	 */

	protected int position = -1;

	/**
	 * Whether to add or remove the library.
	 */

	protected boolean add = true;

	/**
	 * The cached overridden values when removing one library.
	 */
	protected Map<Long, Map<Long, List<Object>>> overriddenValues = null;

	/**
	 * Constructs the library record.
	 * 
	 * @param module  the module
	 * @param library the library to add/drop
	 * @param add     whether the given library is for adding
	 */

	LibraryRecord(Module module, Library library, boolean add) {
		super(module, library);

		this.add = add;
	}

	/**
	 * Constructs the library record. Only for adding library.
	 * 
	 * @param module  the module
	 * @param library the library to add/drop
	 * @param values  the cached overridden values when removing a library
	 */

	LibraryRecord(Module module, Library library, Map<Long, Map<Long, List<Object>>> values) {
		this(module, library, true);

		overriddenValues = values;
		assert overriddenValues != null;
	}

	/**
	 * Constructs the library record. Only for adding library.
	 * 
	 * @param module  the module
	 * @param library the library to add/drop
	 * @param posn    the position to insert the library
	 * @param values  the cached overridden values when removing a library
	 */

	LibraryRecord(Module module, Library library, Map<Long, Map<Long, List<Object>>> values, int posn) {
		this(module, library, true);

		this.position = posn;

		overriddenValues = values;
		assert overriddenValues != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		if (add && !undo || !add && undo) {
			int toUpdateLibraryCount;
			if (position == -1) {
				module.addLibrary(library);
				toUpdateLibraryCount = module.getLibraries().size() - 1;
			} else {
				module.insertLibrary(library, position);
				toUpdateLibraryCount = position;
			}

			// first resolve the extends and apply overridden values to virtual
			// elements. Only for the add & do case. For remove & undo, it is
			// supported by ContentCommand. See LibraryCommand.reloadLibrary for
			// details.

			if (add && !undo)
				resolveAllElementDescendants();

			// One library is added, and the style in it can override the
			// previous one.

			updateReferenceableClients(toUpdateLibraryCount);
		} else {
			position = module.dropLibrary(library);

			// The update is performed only on the referred elements in the
			// dropped library.

			updateReferenceableClients(library);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget() {
		return module;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent() {
		if (add && state != UNDONE_STATE || !add && state == UNDONE_STATE)
			return new LibraryEvent(library, LibraryEvent.ADD);

		return new LibraryEvent(library, LibraryEvent.DROP);
	}

	/**
	 * Resolves extends references for elements in the <code>module</code>. During
	 * the resolving procedure, cached overridden values are also distributed.
	 */

	protected void resolveAllElementDescendants() {
		for (int i = 0; i < module.getDefn().getSlotCount(); i++) {
			int slotId = i;
			if (module instanceof ReportDesign && (slotId == IReportDesignModel.STYLE_SLOT
					|| slotId == IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT))
				continue;
			else if (module instanceof Library && slotId == ILibraryModel.THEMES_SLOT)
				continue;

			resolveElementDescendantsInSlot(slotId);
		}
	}

	/**
	 * Resolves extends references for elements in the given slot. During the
	 * resolving procedure, cached overridden values are also distributed.
	 * 
	 * @param slotId the slot id
	 */

	private void resolveElementDescendantsInSlot(int slotId) {
		ContentIterator contentIter = new ContentIterator(module, new ContainerContext(module, slotId));

		while (contentIter.hasNext()) {
			DesignElement tmpElement = contentIter.next();
			ElementDefn elementDefn = (ElementDefn) tmpElement.getDefn();
			if (!elementDefn.canExtend() || !elementDefn.isContainer())
				continue;

			String name = tmpElement.getExtendsName();
			if (StringUtil.isBlank(name))
				continue;

			// only handle the added library related inheritance

			ElementRefValue extendRef = (ElementRefValue) tmpElement.getLocalProperty(module,
					IDesignElementModel.EXTENDS_PROP);
			if (!library.getNamespace().equalsIgnoreCase(extendRef.getLibraryNamespace()))
				continue;

			// refresh the structure and add children to name space and id-map
			ElementStructureUtil.refreshStructureFromParent(module, tmpElement);
			ElementStructureUtil.addTheVirualElementsToNamesapce(tmpElement, module);
			module.manageId(tmpElement, true);

			if (overriddenValues == null)
				continue;

			Long idObj = Long.valueOf(tmpElement.getID());
			Map<Long, List<Object>> values = overriddenValues.get(idObj);
			ElementStructureUtil.distributeValues(module, tmpElement, values);
		}
	}

}
