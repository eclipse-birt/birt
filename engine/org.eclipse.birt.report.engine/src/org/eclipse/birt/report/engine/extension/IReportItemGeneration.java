/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension;

import java.io.OutputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Represents the extended item generation extension, which processes the report
 * item at report geenration time.
 */
public interface IReportItemGeneration {
	/**
	 * @since BIRT 2.3
	 */
	void init(IReportItemGenerationInfo info);

	/**
	 * passes a handle to the extended report item model to the extension
	 *
	 * @deprecated implement #init(IReportItemGenerationInfo) instead
	 * @param modelHandle a handle to the extended item model object
	 */
	@Deprecated
	void setModelObject(ExtendedItemHandle modelHandle);

	/**
	 * passes the class loader used to load user defined classes.
	 *
	 * @deprecated implement #init(IReportItemGenerationInfo) instead
	 * @param loader class loader used to load the classes
	 */
	@Deprecated
	void setApplicationClassLoader(ClassLoader loader);

	/**
	 * pass the script context to the report item.
	 *
	 * @deprecated implement #init(IReportItemGenerationInfo) instead
	 * @param context report context used by java-based script
	 */
	@Deprecated
	void setScriptContext(IReportContext context);

	/**
	 * pass the prepared query definition to extended item implementation, so that
	 * it can access data.
	 *
	 * @deprecated implement #init(IReportItemGenerationInfo) instead
	 */
	@Deprecated
	void setReportQueries(IDataQueryDefinition[] queries);

	/**
	 * processes the extended item in report generation environment.
	 *
	 * @deprecated since BIRT 2.3
	 * @param rowSets rowSets an array of row sets that is passed to the extension
	 * @return an object that captures the generation-time state information about
	 *         the extended item. Presentation engine guarantees that the same
	 *         object is returned to the extended item instance at presentation
	 *         time. To achieve such a goal, generation engine may uses
	 *         serialization services provided by the IReportItemSerializable
	 *         interface.
	 * @throws BirtException throws exception when there is a problem processing the
	 *                       extended item
	 */
	@Deprecated
	void onRowSets(IRowSet[] rowSets) throws BirtException;

	/**
	 * processes the extended item in report generation environment.
	 *
	 * @param results results is an array of query results which is passed to the
	 *                extended item. The extended item could retrieve data from
	 *                those results.
	 * @return an object that captures the generation-time state information about
	 *         the extended item. Presentation engine guarantees that the same
	 *         object is returned to the extended item instance at presentation
	 *         time. To achieve such a goal, generation engine may uses
	 *         serialization services provided by the IReportItemSerializable
	 *         interface.
	 * @throws BirtException throws exception when there is a problem processing the
	 *                       extended item
	 */
	void onRowSets(IBaseResultSet[] results) throws BirtException;

	/**
	 * returns whether the extended item needs serialization of state information at
	 * generation time
	 *
	 * @return whether the extended item needs to serialize state information at
	 *         generation time
	 */
	boolean needSerialization();

	/**
	 * serializes the extended item generation time state. The application developer
	 * should not assume that the output stream is a file stream. Presentation
	 * engine guarantees that a corresponding input stream is available for
	 * deserializing what is written by this function.
	 *
	 * @param ostream the output stream to write the generation time state of the
	 *                extended item to
	 * @throws BirtException when there is a problem serializing the extended item
	 *                       state
	 */
	void serialize(OutputStream ostream) throws BirtException;

	/**
	 * Get the size of the extended item. The size is a Dimension object. The width
	 * and height can only be in absolute units (inch, mm, etc.) or pixel. It can
	 * not be a relative size such as 150% or 1.2em. Notice that an extended item
	 * can obtain its design-time size information by querying DE. This function is
	 * needed because the actual size may not be the same as the design-time size.
	 *
	 * @return the size of the extended item. Return null if the size does not
	 *         matter or can not be determined.
	 */
	Size getSize();

	/**
	 * Performs clean up work
	 */
	void finish();

	/**
	 * Set the content which is transformed from extended item. Extended item can
	 * process some properties itself, such as bookmark, style etc.
	 *
	 * @deprecated implement #init(IReportItemGenerationInfo) instead
	 * @param content content which is transformed from extended item.
	 */
	@Deprecated
	void setExtendedItemContent(IContent content);

	IReportItemGenerationInfo getGenerationConfig();
}
