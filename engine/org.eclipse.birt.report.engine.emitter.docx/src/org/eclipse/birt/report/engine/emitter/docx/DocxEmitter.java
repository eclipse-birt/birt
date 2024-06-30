/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.docx;

import org.eclipse.birt.report.engine.emitter.wpml.DocEmitter;

public class DocxEmitter extends DocEmitter {

	@Override
	protected void createEmitterImplement() {
		emitterImplement = new DocxEmitterImpl(contentVisitor);
	}
}
