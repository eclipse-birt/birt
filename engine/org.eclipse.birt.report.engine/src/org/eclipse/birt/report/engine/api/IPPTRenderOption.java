/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

public interface IPPTRenderOption extends IRenderOption {

	/**
	 * The option for PPT emitter when exports to office 2010 and 2013.
	 */
	String EXPORT_FILE_FOR_MICROSOFT_OFFICE_2010_2013 = "pptRenderOption.exportFileInMht";
}
