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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>StyledItemExecutor</code> that manipulates label/text items.
 *
 */
public class TextItemExecutor extends QueryItemExecutor {

	/**
	 * constructor
	 *
	 * @param context the executor context
	 * @param visitor the report executor visitor
	 */
	public TextItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.TEXTITEM);
	}

	/**
	 * Text item create an foreign object. The process is:
	 * <li>create the foreign object
	 * <li>push it into the context
	 * <li>execute the query and seek to the first record if any.
	 * <li>process the style, visiblity, bookmark, actions
	 * <li>evaluate the expressions in the text.
	 * <li>call onCreate if needed.
	 * <li>pass it to emitter
	 * <li>close the query
	 * <li>pop up.
	 *
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute(IContentEmitter)
	 */
	@Override
	public IContent execute() {
		TextItemDesign textDesign = (TextItemDesign) getDesign();
		String textType = textDesign.getTextType();
		String text = textDesign.getText();
		String contentType = ForeignContent.getTextRawType(textType, text);

		if (IForeignContent.HTML_TYPE.equals(contentType)) {
			return executeHtmlText();
		} else {
			return executePlainText();
		}
	}

	@Override
	public void close() throws BirtException {
		finishTOCEntry();
		closeQuery();
		super.close();
	}

	/**
	 * execute the html text.
	 *
	 * @param design
	 * @param emitter
	 */
	protected IContent executeHtmlText() {
		TextItemDesign textDesign = (TextItemDesign) getDesign();
		IForeignContent textContent = report.createForeignContent();
		setContent(textContent);

		executeQuery();
		// accessQuery( );

		initializeContent(textDesign, textContent);

		processAction(textDesign, textContent);
		processBookmark(textDesign, textContent);
		processStyle(textDesign, textContent);
		processVisibility(textDesign, textContent);
		processUserProperties(textDesign, textContent);

		HashMap<String, Expression> exprs = textDesign.getExpressions();
		if (exprs != null && !exprs.isEmpty()) {
			HashMap<String, Object> results = new HashMap<>();
			for (Map.Entry<String, Expression> entry : exprs.entrySet()) {
				Expression expr = entry.getValue();
				Object value = evaluate(expr);
				results.put(entry.getKey(), value);
			}
			Object[] value = new Object[2];
			value[0] = null;
			value[1] = results;
			textContent.setRawValue(value);
		} else {
			textContent.setRawValue(new Object[] { null, null });
		}
		textContent.setRawType(IForeignContent.TEMPLATE_TYPE);
		textContent.setJTidy(textDesign.isJTidy());

		if (context.isInFactory()) {
			handleOnCreate(textContent);
		}

		startTOCEntry(content);

		return textContent;
	}

	/**
	 * execute the plain text.
	 *
	 * @param design
	 * @param emitter
	 */
	protected IContent executePlainText() {
		TextItemDesign textDesign = (TextItemDesign) getDesign();

		ILabelContent textContent = report.createLabelContent();
		setContent(textContent);

		executeQuery();
		// accessQuery( design, emitter );

		initializeContent(textDesign, textContent);
		textContent.setLabelText(textDesign.getText());
		textContent.setLabelKey(textDesign.getTextKey());

		processAction(textDesign, textContent);
		processBookmark(textDesign, textContent);
		processStyle(textDesign, textContent);
		processVisibility(textDesign, textContent);
		processUserProperties(textDesign, textContent);

		if (context.isInFactory()) {
			handleOnCreate(textContent);
		}

		startTOCEntry(content);

		return textContent;
	}
}
