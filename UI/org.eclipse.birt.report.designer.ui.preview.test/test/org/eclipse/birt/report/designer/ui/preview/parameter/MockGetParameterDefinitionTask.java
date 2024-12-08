/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IStatusHandler;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Mock getparameter definition task
 *
 */
public class MockGetParameterDefinitionTask implements IGetParameterDefinitionTask {

	@Override
	public void evaluateDefaults() throws EngineException {

	}

	@Override
	public void evaluateQuery(String parameterGroupName) {
	}

	@Override
	public Object getDefaultValue(IParameterDefnBase param) {
		return null;
	}

	@Override
	public Object getDefaultValue(String name) {
		return null;
	}

	@Override
	public HashMap getDefaultValues() {
		return null;
	}

	@Override
	public ParameterHandle getParameter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IParameterDefnBase getParameterDefn(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getParameterDefns(boolean includeParameterGroups) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SlotHandle getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getSelectionList(String name) {
		List selects = new ArrayList();

		IParameterSelectionChoice choice = new MockParameterSelectionChoice(name, name);
		selects.add(choice);

		return selects;
	}

	@Override
	public Collection getSelectionListForCascadingGroup(String parameterGroupName, Object[] groupKeyValues) {
		List selects = new ArrayList();

		for (int i = 0; i < groupKeyValues.length; ++i) {
			Object groupKey = groupKeyValues[i];
			IParameterSelectionChoice choice = new MockParameterSelectionChoice(groupKey.toString(),
					groupKey.toString());
			selects.add(choice);
		}
		return selects;
	}

	@Override
	public void setValue(String name, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addScriptableJavaObject(String jsName, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

	public void cancel(Object signal) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map getAppContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getCancelFlag() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IReportEngine getEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDisplayText(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParameterValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap getParameterValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IReportRunnable getReportRunnable() {
		return new MockReportRunnable();
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ULocale getULocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAppContext(Map context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataSource(IDocArchiveReader dataSource) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocale(Locale locale) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocale(ULocale locale) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParameter(String name, Object value, String displayText) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParameterDisplayText(String name, String displayText) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParameterValue(String name, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParameterValues(Map params) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateParameters() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setErrorHandlingOption(int option) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getTaskType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogger(Logger logger) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTimeZone(TimeZone timeZone) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParameterValue(String name, Object[] values) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParameter(String name, Object[] values, String[] displayText) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParameterDisplayText(String name, String[] text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataSource(IDocArchiveReader dataSource, String reportlet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUserACL(String[] acl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatusHandler(IStatusHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection getSelectionTreeForCascadingGroup(String parameterGroupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancel(String reason) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isNormalFlowModeEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setNormalFlowModeEnabled(boolean normalFlowModeEnabled) {
		// TODO Auto-generated method stub

	}

}
