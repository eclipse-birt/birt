/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.gef.Request;

/**
 * Insert action
 */

public class InsertAction extends AbstractElementAction {

	public final static String ID = "org.eclipse.birt.report.designer.ui.views.action.InsertAction";//$NON-NLS-1$

	public final static String ABOVE = "above"; //$NON-NLS-1$

	public final static String BELOW = "below"; //$NON-NLS-1$

	public final static String CURRENT = "current"; //$NON-NLS-1$

	private final static String INSERT_TEXT = Messages.getString("InsertAction.text"); //$NON-NLS-1$

	private final static String NEW_MASTER_PAGE_TEXT = Messages.getString("NewMasterPage.text");

	private SlotHandle slotHandle;

	private PropertyHandle propertyHandle;

	public PropertyHandle getPropertyHandle() {
		return propertyHandle;
	}

	private boolean isDone;
	private Object createElement;

	protected SlotHandle getSlotHandle() {
		return slotHandle;
	}

	private final String position;

	protected String getPosition() {
		return position;
	}

	private String type = null;

	protected String getType() {
		return type;
	}

	/**
	 * Create a new insert action with given selection and text at specified
	 * position
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * 
	 */
	public InsertAction(Object selectedObject) {
		this(selectedObject,
				selectedObject instanceof SlotHandle
						&& ((SlotHandle) selectedObject).getSlotID() == IModuleModel.PAGE_SLOT
						&& ((SlotHandle) selectedObject).getElementHandle() instanceof ReportDesignHandle
								? NEW_MASTER_PAGE_TEXT
								: INSERT_TEXT);
	}

	/**
	 * Create a new insert action with given selection and text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public InsertAction(Object selectedObject, String text) {
		this(selectedObject, text, null);
	}

	/**
	 * Create a new insert action with given selection and text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public InsertAction(Object selectedObject, String text, String type) {
		this(selectedObject, text, type, CURRENT);
	}

	/**
	 * Create a new insert action with given selection and text at specified
	 * position
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 * @param type           the type of the element to insert
	 * @param pos            the insert position
	 */
	public InsertAction(Object selectedObject, String text, String type, String pos) {
		this(selectedObject, text, (SlotHandle) null, type, pos);
	}

	public InsertAction(Object selectedObject, String text, PropertyHandle propertyHandle, String type) {
		this(selectedObject, text, propertyHandle, type, CURRENT);
	}

	public InsertAction(Object selectedObject, String text, PropertyHandle propertyHandle, String type, String pos) {
		super(selectedObject, text);
		this.type = type;
		this.position = pos;
		if (propertyHandle != null) {
			this.propertyHandle = propertyHandle;
		} else {
			this.propertyHandle = getDefaultPropertyHandle();
		}
	}

	public InsertAction(Object selectedObject, String text, SlotHandle slotHandle, String type, String pos) {
		super(selectedObject, text);
		this.type = type;
		this.position = pos;
		if (slotHandle != null) {
			this.slotHandle = slotHandle;
		} else {
			this.slotHandle = getDefaultSlotHandle();
		}
	}

	public boolean isEnabled() {
		/*
		 * Check the case that a table refer other element and whether can insert a
		 * group.
		 */
		if (getSelection() instanceof SlotHandle
				&& ((SlotHandle) getSelection()).getSlotID() == IListingElementModel.GROUP_SLOT
				&& ((SlotHandle) getSelection()).getElementHandle() instanceof ListingHandle) {
			return ((SlotHandle) getSelection()).canContain(ReportDesignConstants.LIST_GROUP_ELEMENT)
					|| ((SlotHandle) getSelection()).canContain(ReportDesignConstants.TABLE_GROUP_ELEMENT);
		}
		return super.isEnabled();
	}

	protected PropertyHandle getDefaultPropertyHandle() {
		Object obj = getSelection();
		// if ( obj instanceof ReportElementModel )
		// {
		// return ( (ReportElementModel) obj ).getSlotHandle( );
		// }else
		if (obj instanceof PropertyHandle) {
			return (PropertyHandle) obj;
		}
		ElementDetailHandle handle = (ElementDetailHandle) obj;
		if (position == CURRENT) {
			String str = DEUtil.getDefaultContentName(handle);
			if (str != null) {
				return handle.getElementHandle().getPropertyHandle(str);
			}
		}
		return handle.getElementHandle().getContainerPropertyHandle();
	}

	/**
	 * Gets the default slot handle to insert
	 * 
	 * @return Returns the default slot handle to insert
	 */
	protected SlotHandle getDefaultSlotHandle() {
		Object obj = getSelection();
		// if ( obj instanceof ReportElementModel )
		// {
		// return ( (ReportElementModel) obj ).getSlotHandle( );
		// }else
		if (obj instanceof SlotHandle) {
			return (SlotHandle) obj;
		}
		DesignElementHandle handle = (DesignElementHandle) obj;
		if (position == CURRENT) {
			int slotId = DEUtil.getDefaultSlotID(handle);
			if (slotId != -1) {
				return handle.getSlot(slotId);
			}
		}
		return handle.getContainerSlotHandle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	protected boolean doAction() throws Exception {
		Request request = new Request(IRequestConstants.REQUEST_TYPE_INSERT);
		Map extendsData = new HashMap();
		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_SLOT, slotHandle);

		if (type != null) {
			extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_TYPE, type);
		}
		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_POSITION, position);
		request.setExtendedData(extendsData);
		isDone = ProviderFactory.createProvider(getSelection()).performRequest(getSelection(), request);
		createElement = request.getExtendedData().get(IRequestConstants.REQUEST_KEY_RESULT);

		return isDone;

		// CommandUtils.getHandlerService( )
		// .getCurrentState( )
		// .addVariable( "position", position );
		//
		// if ( type != null )
		// CommandUtils.getHandlerService( )
		// .getCurrentState( )
		// .addVariable( "type", type );
		//
		// Object returnVlaue = CommandUtils.executeCommand( "insert" );
		//
		// CommandUtils.getHandlerService( )
		// .getCurrentState( )
		// .removeVariable( "position" );
		// CommandUtils.getHandlerService( )
		// .getCurrentState( )
		// .removeVariable( "type" );
		//
		// return Boolean.TRUE.equals( returnVlaue );
	}

	@Override
	protected void postDoAction() {
		super.postDoAction();
		if (isDone && createElement != null) {
			List list = new ArrayList();

			list.add(createElement);
			ReportRequest r = new ReportRequest();
			r.setType(ReportRequest.CREATE_ELEMENT);

			r.setSelectionObject(list);
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);

		}

		createElement = null;
	}
}