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

package org.eclipse.birt.report.model.validators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Base class for testing validator. It provides the listener class and error
 * pool class. The error pool simulates the problem view in UI.
 * <p>
 * All errors are cached in this error pool, which works like the error list in
 * problem view. When the listener receives a validation event, the errors in
 * this pool will be updated with the errors in the received event. If the event
 * contains a validation ID and element instance, which can identify one
 * specific validation on one element. If new error appears in event, it will be
 * appended to the error pool. If one existing error doesn't appear in this
 * event, it means this error is corrected and the validation doesn't catch such
 * error. So it will be removed from pool.
 */

public abstract class ValidatorTestCase extends BaseTestCase {
	class MyListener implements IValidationListener {

		ErrorPool pool = new ErrorPool();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.validators.IValidationListener#
		 * elementValidated(org.eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.validators.ValidationEvent)
		 */

		public void elementValidated(DesignElementHandle focus, ValidationEvent ev) {
			pool.updateErrorStatus(ev);
		}

		/**
		 * Returns whether the error exists in the error pool. This method is used to
		 * check the errors caught by element validator.
		 * 
		 * @param element       the element to check
		 * @param validatorName the validator name
		 * @param errorCode     the error code to check
		 * @return return true if the given error code is in pool.
		 */

		protected boolean hasError(DesignElementHandle element, String validatorName, String errorCode) {
			return pool.hasErrorFromValidation(element.getElement(), validatorName, errorCode);
		}

		/**
		 * Returns whether the error exists in the error pool. This method is used to
		 * check the errors caught by property validator.
		 * 
		 * @param element       the element to check
		 * @param validatorName the validator name
		 * @param propName      the name of the property on which the validator is
		 *                      applied
		 * @param errorCode     the error code to check
		 * @return return true if the given error code is in pool.
		 */

		protected boolean hasError(DesignElementHandle element, String validatorName, String propName,
				String errorCode) {
			String validationID = validatorName;
			if (!StringUtil.isBlank(propName))
				validationID = validatorName + "." + propName; //$NON-NLS-1$

			return pool.hasErrorFromValidation(element.getElement(), validationID, errorCode);
		}

	}

	class ErrorPool {

		private List errors = new ArrayList();

		/**
		 * Updates the errors with the validation event.
		 * 
		 * @param event the received event
		 */

		void updateErrorStatus(ValidationEvent event) {
			for (int i = errors.size() - 1; i >= 0; i--) {
				ErrorDetail detail = (ErrorDetail) errors.get(i);

				if (event.getTarget() == detail.getElement()
						&& detail.getValidationID().equalsIgnoreCase(event.getValidationID())) {
					errors.remove(i);
				}
			}

			Iterator iter = event.getErrors().iterator();
			while (iter.hasNext()) {
				ErrorDetail detail = (ErrorDetail) iter.next();
				errors.add(detail);
			}
		}

		private boolean hasErrorFromValidation(DesignElement element, String validationID, String errorCode) {
			Iterator iter = errors.iterator();
			while (iter.hasNext()) {
				ErrorDetail detail = (ErrorDetail) iter.next();

				if (detail.getElement() == element && detail.getValidationID().equalsIgnoreCase(validationID)
						&& detail.getErrorCode().equalsIgnoreCase(errorCode))
					return true;
			}

			return false;
		}
	}
}