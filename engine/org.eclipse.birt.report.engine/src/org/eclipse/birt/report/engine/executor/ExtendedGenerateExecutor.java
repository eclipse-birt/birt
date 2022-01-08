/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.SingleCubeResultSet;
import org.eclipse.birt.report.engine.data.dte.SingleQueryResultSet;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.internal.ReportItemGenerationInfo;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

public class ExtendedGenerateExecutor extends QueryItemExecutor {

	IReportItemGeneration itemGeneration;
	IBaseResultSet[] rsets;

	public ExtendedGenerateExecutor(ExecutorManager manager, IReportItemGeneration itemGeneration) {
		super(manager, -1);
		this.itemGeneration = itemGeneration;
	}

	public void close() throws BirtException {
		closeQueries();
		super.close();
	}

	public IContent execute() {
		ExtendedItemDesign extDesign = (ExtendedItemDesign) design;

		IForeignContent extContent = report.createForeignContent();
		setContent(extContent);

		try {
			executeQueries();
		} catch (BirtException ex) {
			getLogger().log(Level.SEVERE, ex.getMessage(), ex);
			context.addException(design.getHandle(), new EngineException(ex));
			return null;
		}

		initializeContent(extDesign, extContent);

		processAction(extDesign, extContent);
		processBookmark(extDesign, extContent);
		processStyle(extDesign, extContent);
		processVisibility(extDesign, extContent);
		processUserProperties(extDesign, extContent);

		generateContent(extDesign, extContent);

		return extContent;
	}

	protected void generateContent(ExtendedItemDesign item, IForeignContent content) {
		// create user-defined generation-time helper object
		ExtendedItemHandle handle = (ExtendedItemHandle) item.getHandle();
		String name = item.getName();

		byte[] generationStatus = null;
		if (itemGeneration != null) {
			IDataQueryDefinition[] queries = ((ExtendedItemDesign) item).getQueries();

			ReportItemGenerationInfo info = new ReportItemGenerationInfo();
			info.setModelObject(handle);
			info.setApplicationClassLoader(context.getApplicationClassLoader());
			info.setReportContext(context.getReportContext());
			info.setReportQueries(queries);
			info.setExtendedItemContent(content);
			itemGeneration.init(info);

			try {
				IBaseResultSet[] resultSets = rsets;
				if (resultSets == null) {
					IBaseResultSet prset = getParentResultSet();
					if (prset != null) {
						int rsetType = prset.getType();
						if (rsetType == IBaseResultSet.QUERY_RESULTSET) {
							resultSets = new IBaseResultSet[1];
							resultSets[0] = new SingleQueryResultSet((IQueryResultSet) prset);
						} else if (rsetType == IBaseResultSet.CUBE_RESULTSET) {
							resultSets = new IBaseResultSet[1];
							resultSets[0] = new SingleCubeResultSet((ICubeResultSet) prset);
						}
					}
				}
				itemGeneration.onRowSets(resultSets);
				if (itemGeneration.needSerialization()) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					itemGeneration.serialize(out);
					generationStatus = out.toByteArray();
				}
				itemGeneration.finish();
			} catch (BirtException ex) {
				getLogger().log(Level.SEVERE, ex.getMessage(), ex);
				context.addException(handle, new EngineException(MessageConstants.EXTENDED_ITEM_GENERATION_ERROR,
						handle.getExtensionName() + (name != null ? " " + name : ""), ex));//$NON-NLS-1$
			}
		} else {
			// TODO: review. If itemGeneration is null. we should create a text
			// item for it. and set the alttext as its text.
		}
		content.setRawType(IForeignContent.EXTERNAL_TYPE);
		content.setRawValue(generationStatus);
	}

	/**
	 * read the content of input stream.
	 * 
	 * @param in input content
	 * @return content in the stream.
	 */
	static protected byte[] readContent(InputStream in) throws IOException {
		BufferedInputStream bin = in instanceof BufferedInputStream ? (BufferedInputStream) in
				: new BufferedInputStream(in);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int readSize = 0;
		readSize = bin.read(buffer);
		while (readSize != -1) {
			out.write(buffer, 0, readSize);
			readSize = bin.read(buffer);
		}
		return out.toByteArray();
	}

	protected void executeQueries() throws BirtException {
		ExtendedItemDesign extItem = (ExtendedItemDesign) design;
		IDataEngine dataEngine = context.getDataEngine();

		boolean useCache = extItem.useCachedResult();

		IDataQueryDefinition[] queries = extItem.getQueries();
		if (queries != null) {
			IBaseResultSet parent = getParentResultSet();
			rsets = new IBaseResultSet[queries.length];
			for (int i = 0; i < rsets.length; i++) {
				if (queries[i] == null) {
					rsets[i] = null;
				} else {
					rsets[i] = dataEngine.execute(parent, queries[i], extItem.getHandle(), useCache);
				}
			}
			if (rsets[0] instanceof IQueryResultSet) {
				rset = (IQueryResultSet) rsets[0];
			}
			context.setResultSets(rsets);
		}
	}

	protected void closeQueries() {
		if (rsets != null) {
			for (int i = 0; i < rsets.length; i++) {
				if (rsets[i] != null) {
					rsets[i].close();
				}
			}
		}
		rsets = null;
		rset = null;
	}
}
