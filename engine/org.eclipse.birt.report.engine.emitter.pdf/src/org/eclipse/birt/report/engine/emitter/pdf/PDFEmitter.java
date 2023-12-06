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

package org.eclipse.birt.report.engine.emitter.pdf;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.layout.emitter.PageEmitter;

/**
 * Class of PDFEmitter
 *
 * @since 3.3
 *
 */
public class PDFEmitter extends PageEmitter {

	@Override
	public PageDeviceRender createRender(IEmitterServices services) throws EngineException {
		return new PDFRender(services);
	}
}
