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

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Command to shift library.
 */

public class ShiftLibraryCommand extends AbstractElementCommand {

	/**
	 * Constructs the command with the module containing the changing library.
	 * 
	 * @param module the module containing the changing library
	 */

	public ShiftLibraryCommand(Module module) {
		super(module, module);
		assert module instanceof ReportDesign;
	}

	/**
	 * Shifts the given library forwards or backwards.
	 * 
	 * @param library the library to shift
	 * @param newPosn the new position to shift
	 * @throws SemanticException if failed to shift <code>IncludeLibrary</code>
	 *                           structure
	 */

	public void shiftLibrary(Library library, int newPosn) throws SemanticException {
		List<Library> libraries = module.getLibraries();
		assert !libraries.isEmpty();

		if (!libraries.contains(library))
			throw new LibraryException(library, new String[] { library.getNamespace() },
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND);

		// Move the new position so that it is in range.

		int oldPosn = libraries.indexOf(library);

		int adjustedNewPosn = checkAndAdjustPosition(oldPosn, newPosn, libraries.size());

		if (oldPosn == adjustedNewPosn)
			return;

		ActivityStack stack = getActivityStack();

		ShiftLibraryRecord record = new ShiftLibraryRecord(module, oldPosn, adjustedNewPosn);

		stack.startTrans(record.getLabel());

		getActivityStack().execute(record);

		ComplexPropertyCommand cmd = new ComplexPropertyCommand(module, module);
		ElementPropertyDefn propDefn = module.getPropertyDefn(IModuleModel.LIBRARIES_PROP);
		cmd.moveItem(new StructureContext(module, propDefn, null), oldPosn, newPosn);

		stack.commit();
	}
}
