/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

public interface IReportItemPresentationInfo {

	public ExtendedItemHandle getModelObject();

	public ClassLoader getApplicationClassLoader();

	public IReportContext getReportContext();

	public IDataQueryDefinition[] getReportQueries();

	public IContent getExtendedItemContent();

	public int getResolution();

	public String getOutputFormat();

	public IHTMLActionHandler getActionHandler();

	public String getSupportedImageFormats();
}
