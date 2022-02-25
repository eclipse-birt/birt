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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates whether the included library file is existed or not.
 *
 *
 */
public class IncludedLibraryValidator extends AbstractElementValidator {

	private static IncludedLibraryValidator instance = new IncludedLibraryValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static IncludedLibraryValidator getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.validators.AbstractElementValidator#validate
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */
	@Override
	public List<SemanticException> validate(Module module, DesignElement element) {

		List errors = new ArrayList();
		List libs = module.getAllLibraries();

		for (int i = 0; i < libs.size(); i++) {
			Library lib = (Library) libs.get(i);

			if (!lib.isValid()) {
				errors.add(new LibraryException(module, new String[] { lib.getNamespace() },
						LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND));
			}
		}

		return errors;
	}

}
