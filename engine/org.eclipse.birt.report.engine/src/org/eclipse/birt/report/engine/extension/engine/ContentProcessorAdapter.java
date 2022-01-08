/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension.engine;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;

public class ContentProcessorAdapter implements IContentProcessor {

	public void end(IReportContent report) throws EngineException {
	}

	public void endContent(IContent content) throws EngineException {
	}

	public void start(IReportContent report) throws EngineException {
	}

	public void startContent(IContent content) throws EngineException {
	}
}
