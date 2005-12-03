package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public interface IReportItem extends IReportElement
{

	/**
	 * Returns the data set of the report item.
	 * 
	 * @return the handle to the data set
	 */

	DataSetHandle getDataSet( );

	/**
	 * Sets the data set of the report item.
	 * 
	 * @param handle
	 *            the handle of the data set
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	void setDataSet( DataSetHandle dataSet ) throws SemanticException;

	/**
	 * Gets a handle to deal with the item's x (horizontal) position.
	 * 
	 * @return a DimensionHandle for the item's x position.
	 */

	DimensionHandle getX( );

	/**
	 * Gets a handle to deal with the item's y (vertical) position.
	 * 
	 * @return a DimensionHandle for the item's y position.
	 */

	DimensionHandle getY( );

	/**
	 * Sets the item's x position using a dimension string with optional unit
	 * suffix such as "10" or "10pt". If no suffix is provided, then the units
	 * are assumed to be in the design's default units. Call this method to set
	 * a string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	void setX( String dimension ) throws SemanticException;

	/**
	 * Sets the item's x position to a value in default units. The default unit
	 * may be defined by the property in BIRT or the application unit defined in
	 * the design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	void setX( double dimension ) throws SemanticException;

	/**
	 * Sets the item's y position using a dimension string with optional unit
	 * suffix such as "10" or "10pt". If no suffix is provided, then the units
	 * are assumed to be in the design's default units. Call this method to set
	 * a string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	void setY( String dimension ) throws SemanticException;

	/**
	 * Sets the item's y position to a value in default units. The default unit
	 * may be defined by the property in BIRT or the application unit defined in
	 * the design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	void setY( double dimension ) throws SemanticException;

	/**
	 * Sets the item's height using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are
	 * assumed to be in the design's default units. Call this method to set a
	 * string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	void setHeight( String dimension ) throws SemanticException;

	/**
	 * Sets the item's height to a value in default units. The default unit may
	 * be defined by the property in BIRT or the application unit defined in the
	 * design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	void setHeight( double dimension ) throws SemanticException;

	/**
	 * Sets the item's width using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are
	 * assumed to be in the design's default units. Call this method to set a
	 * string typed in by the user.
	 * 
	 * @param dimension
	 *            dimension string with optional unit suffix.
	 * @throws SemanticException
	 *             if the string is not valid
	 */

	void setWidth( String dimension ) throws SemanticException;

	/**
	 * Sets the item's width to a value in default units. The default unit may
	 * be defined by the property in BIRT or the application unit defined in the
	 * design session.
	 * 
	 * @param dimension
	 *            the new value in application units.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	void setWidth( double dimension ) throws SemanticException;

	/**
	 * Gets a handle to deal with the item's width.
	 * 
	 * @return a DimensionHandle for the item's width.
	 */

	String getWidth( );

	/**
	 * Gets a handle to deal with the item's height.
	 * 
	 * @return a DimensionHandle for the item's height.
	 */
	String getHeight( );

	/**
	 * Returns the bookmark of the report item.
	 * 
	 * @return the book mark as a string
	 */

	String getBookmark( );

	/**
	 * Sets the bookmark of the report item.
	 * 
	 * @param value
	 *            the property value to be set.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	void setBookmark( String value ) throws SemanticException;

	/**
	 * Sets a table of contents entry for this item. The TOC property defines an
	 * expression that returns a string that is to appear in the Table of
	 * Contents for this item or its container.
	 * 
	 * @param expression
	 *            the expression that returns a string
	 * @throws SemanticException
	 *             if the TOC property is locked by the property mask.
	 * 
	 * @see #getTocExpression()
	 */

	void setTocExpression( String expression ) throws SemanticException;

	/**
	 * Returns the expression evalueated as a table of contents entry for this
	 * item.
	 * 
	 * @return the expression evaluated as a table of contents entry for this
	 *         item
	 * @see #setTocExpression(String)
	 */

	String getTocExpression( );

}