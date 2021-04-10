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

package org.eclipse.birt.report.engine.emitter.postscript;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.layout.emitter.PageEmitter;

public class PostscriptEmitter extends PageEmitter {

	public PageDeviceRender createRender(IEmitterServices services) throws EngineException {
		return new PostscriptRender(services);
	}
}
