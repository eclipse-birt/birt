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

import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.LibraryEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;

/**
 * Record for shifting library
 */

public class ShiftLibraryRecord extends AbstractLibraryRecord {

	/**
	 * The old position
	 */

	private int oldPosn = -1;

	/**
	 * The new position
	 */

	private int newPosn = -1;

	/**
	 * Constrcuts the record.
	 *
	 * @param module  the module containing the library to shift
	 * @param oldPosn the old position
	 * @param newPosn the new position
	 */

	ShiftLibraryRecord(Module module, int oldPosn, int newPosn) {
		super(module);
		this.oldPosn = oldPosn;
		this.newPosn = newPosn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	@Override
	protected void perform(boolean undo) {
		library = module.getLibraries().get(undo ? newPosn : oldPosn);
		assert library != null;

		module.dropLibrary(library);
		module.insertLibrary(library, undo ? oldPosn : newPosn);

		List<Library> librariesToUpdate = module.getLibraries().subList(Math.min(oldPosn, newPosn),
				Math.max(oldPosn, newPosn));

		updateReferenceableClients(librariesToUpdate.size());
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	@Override
	public NotificationEvent getEvent() {
		assert library != null;

		return new LibraryEvent(library, LibraryEvent.SHIFT);
	}

}
