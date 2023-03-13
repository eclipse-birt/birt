/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 * MultiLine Item Executor
 *
 */
public class DynamicTextItemExecutor extends QueryItemExecutor {

	/**
	 * constructor
	 *
	 * @param context the executor context
	 * @param visitor the report executor visitor
	 */
	DynamicTextItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.DYNAMICTEXTITEM);
	}

	/**
	 *
	 * execute the multiline text.
	 *
	 * multiline text have two expressions define the value and type. If the value
	 * type is HTML, the value returns string in HTML. If the value type is
	 * PLAIN_TEXT, the value returns string in plain text.
	 *
	 * the handling process is:
	 * <li>create forign object
	 * <li>push it into the context
	 * <li>execute the dataset if any
	 * <li>seek to the first record
	 * <li>intialize the content
	 * <li>process style, action, bookmark, visiblity
	 * <li>evaluate the type and value
	 * <li>set the rawType to html or text.
	 * <li>call the onCreate if necessary
	 * <li>pass it to emitter
	 * <li>close the data set
	 * <li>pop the context.
	 *
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#excute(org.eclipse.birt.report.engine.ir.ReportItemDesign,
	 *      org.eclipse.birt.report.engine.emitter.IReportEmitter)
	 */
	@Override
	public IContent execute() {
		DynamicTextItemDesign textDesign = (DynamicTextItemDesign) getDesign();

		IForeignContent textContent = report.createForeignContent();
		setContent(textContent);

		executeQuery();

		initializeContent(textDesign, textContent);

		processStyle(textDesign, textContent);
		processBookmark(textDesign, textContent);
		processVisibility(textDesign, textContent);
		processAction(textDesign, textContent);
		processUserProperties(textDesign, textContent);

		// strValue = getMapVal( strValue, multiLineItem );
		String contentType = textDesign.getContentType();
		if (contentType == null) {
			contentType = TextItemDesign.AUTO_TEXT;
		}

		Object content = evaluate(textDesign.getContent());
		if (content != null) {
			if (!isSupportedType(content)) {
				content = content.toString();
			}
		}

		String rawType = ForeignContent.getTextRawType(contentType, content);
		if (IForeignContent.TEXT_TYPE.equals(rawType)) {
			rawType = IForeignContent.VALUE_TYPE;
		}

		textContent.setRawType(rawType);
		textContent.setRawValue(content);
		textContent.setJTidy(textDesign.isJTidy());

		if (context.isInFactory()) {
			handleOnCreate(textContent);
		}

		startTOCEntry(textContent);

		return textContent;
	}

	private boolean isSupportedType(Object obValue) {
		if (obValue instanceof Scriptable) {
			if (obValue instanceof IdScriptableObject) {
				IdScriptableObject jsObject = ((IdScriptableObject) obValue);
				if (jsObject.getClassName().equals("Date")) {
					return true;
				}
				return false;
			} else if (obValue instanceof NativeJavaObject) {
				return true;
			}
			return false;
		}
		return IOUtil.getTypeIndex(obValue) != -1;
	}

	@Override
	public void close() throws BirtException {
		finishTOCEntry();
		closeQuery();
		super.close();
	}

}
