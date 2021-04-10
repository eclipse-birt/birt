/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.util.mediator.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.gef.Request;

/**
 * An Object used to communicate with Views. Request encapsulates the
 * information views need to perform various functions. Requests are used for
 * obtaining selection, and performing generic operations.
 */

public class ReportRequest extends Request implements IMediatorRequest, ReportRequestConstants {

	private Object source;

	private IRequestConverter converter;

	private List selectionObject = new ArrayList();

	/**
	 * Create a report request.
	 */
	public ReportRequest() {
		this(null, SELECTION);
	}

	public ReportRequest(String type) {
		this(null, type);
	}

	/**
	 * Create a report request with give source object.
	 * 
	 * @param source
	 */
	public ReportRequest(Object source) {
		this(source, SELECTION);
	}

	public ReportRequest(Object source, String type) {
		super();
		setSource(source);
		setType(type);
	}

	/**
	 * Get the source of request.
	 * 
	 * @return Returns the source.
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Set the source of request.
	 * 
	 * @param source The source to set.
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	/**
	 * Get the selection objcect of request source.
	 * 
	 * @return Returns the selectionObject.
	 */
	public List getSelectionObject() {
		return selectionObject;
	}

	/**
	 * Get the selection objcect of request source.
	 * 
	 * @return Returns the selectionObject.
	 */
	public List getSelectionModelList() {
		if (converter != null) {
			return converter.convertSelectionToModelLisr(getSelectionObject());
		}
		return getSelectionObject();
	}

	/**
	 * Set the selection object of reqeust source
	 * 
	 * @param selectionObject The selectionObject to set.
	 */
	public void setSelectionObject(List selectionObject) {
		assert selectionObject != null;
		this.selectionObject = selectionObject;
	}

	/**
	 * @return Returns the request converter.
	 */
	public IRequestConverter getRequestConverter() {
		return converter;
	}

	/**
	 * @param convert The converter to set.
	 * 
	 * @deprecated use {@link #setRequestConverter(IRequestConverter)} instead.
	 */
	public void setRequestConvert(IRequestConvert converter) {
		this.converter = converter;
	}

	/**
	 * @param convert The converter to set.
	 */
	public void setRequestConverter(IRequestConverter converter) {
		this.converter = converter;
	}

	public String getType() {
		return String.valueOf(super.getType());
	}

	public Object getData() {
		return getSelectionModelList();
	}

	public boolean isSticky() {
		return SELECTION.equals(getType());
	}

	public Map<?, ?> getExtras() {
		return getExtendedData();
	}

}