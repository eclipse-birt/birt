package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents a the design of a multi line text item in the scripting
 * environment
 */
public interface IDynamicText extends IReportItem {

	/**
	 * Returns the expression that gives the text that the multi-line data item
	 * displays.
	 * 
	 * @return the value expression
	 */

	String getValueExpr();

	/**
	 * Sets the expression that gives the text that this multi-line data item
	 * displays.
	 * 
	 * @param expr the new expression for the value expression
	 * @throws SemanticException if the expression contains errors, or the property
	 *                           is locked.
	 */

	void setValueExpr(String expr) throws SemanticException;

	/**
	 * Returns the expression that that defines the type of text the multi-line data
	 * item holds. The content type can be one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_AUTO</code> (default)
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_PLAIN</code>: Plain
	 * text;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML</code>: HTML
	 * format;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF</code>: Rich Text
	 * format;
	 * </ul>
	 * 
	 * @return the text type
	 */

	String getContentType();

	/**
	 * Sets the expression that defines the text type this multi-line data item
	 * holds. The content type can be one of
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_AUTO</code> (default)
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_PLAIN</code>: Plain
	 * text;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML</code>: HTML
	 * format;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF</code>: Rich Text
	 * format;
	 * </ul>
	 * 
	 * @param contentType the new text type
	 * @throws SemanticException if the property is locked or the
	 *                           <code>contentType</code> is not one of the above.
	 */

	void setContentType(String contentType) throws SemanticException;

}