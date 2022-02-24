/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Reports an error during a container operation.
 * 
 */

public class ContentException extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 6436296237110208952L;

	/**
	 * The slot within the container.
	 */

	protected int slot = 0;

	/**
	 * The content in the container.
	 */

	protected DesignElement content = null;

	/**
	 * The property name within the container.
	 */
	protected String containerProp = null;

	/**
	 * Can not change the structure of an element if it is a child element, or it is
	 * within a child element, or it is a template parameter definition.
	 */

	public static final String DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN = MessageConstants.CONTENT_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN;

	/**
	 * The operation referenced a slot that does not exist.
	 */

	public static final String DESIGN_EXCEPTION_SLOT_NOT_FOUND = MessageConstants.CONTENT_EXCEPTION_SLOT_NOT_FOUND;

	/**
	 * The given content element is of the wrong type for the slot.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_TYPE = MessageConstants.CONTENT_EXCEPTION_WRONG_TYPE;

	/**
	 * The content element cannot be deleted.
	 * 
	 * @deprecated since birt 2.2
	 */

	public static final String DESIGN_EXCEPTION_DROP_FORBIDDEN = "DESIGN_EXCEPTION_DROP_FORBIDDEN"; //$NON-NLS-1$

	/**
	 * The content element does not appear within the container.
	 */

	public static final String DESIGN_EXCEPTION_CONTENT_NOT_FOUND = MessageConstants.CONTENT_EXCEPTION_CONTENT_NOT_FOUND;

	/**
	 * The purported container element is not, in fact, a container.
	 */

	public static final String DESIGN_EXCEPTION_NOT_CONTAINER = MessageConstants.CONTENT_EXCEPTION_NOT_CONTAINER;

	/**
	 * Attempt to add a second item to a single-item slot.
	 */

	public static final String DESIGN_EXCEPTION_SLOT_IS_FULL = MessageConstants.CONTENT_EXCEPTION_SLOT_IS_FULL;

	/**
	 * Attempt to move an element inside itself, or inside one of its contents.
	 */

	public static final String DESIGN_EXCEPTION_RECURSIVE = MessageConstants.CONTENT_EXCEPTION_RECURSIVE;

	/**
	 * Tried to move or delete an element that has no container. Generally occurs
	 * when trying to work with an element that either has not yet been added to a
	 * design, or has been removed from the design.
	 */

	public static final String DESIGN_EXCEPTION_HAS_NO_CONTAINER = MessageConstants.CONTENT_EXCEPTION_HAS_NO_CONTAINER;

	/**
	 * The content element cannot be deleted.
	 */

	public static final String DESIGN_EXCEPTION_MOVE_FORBIDDEN = MessageConstants.CONTENT_EXCEPTION_MOVE_FORBIDDEN;

	/**
	 * The content in component slot has descendents.
	 */

	public static final String DESIGN_EXCEPTION_HAS_DESCENDENTS = MessageConstants.CONTENT_EXCEPTION_HAS_DESCENDENTS;

	/**
	 * The content is not allowed to in one element's slot in any level.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT = MessageConstants.CONTENT_EXCEPTION_INVALID_CONTEXT_CONTAINMENT;

	/**
	 * The content is not allowed to added into the container without name.
	 */

	public static final String DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED = MessageConstants.CONTENT_EXCEPTION_CONTENT_NAME_REQUIRED;

	/**
	 * The template element has no referred template definition, it is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT = MessageConstants.CONTENT_EXCEPTION_INVALID_TEMPLATE_ELEMENT;

	/**
	 * The template element has no referred template definition, it is invalid.
	 */

	public static final String DESIGN_EXCEPTION_TEMPLATE_TRANSFORM_FORBIDDEN = MessageConstants.CONTENT_EXCEPTION_TEMPLATE_TRANSFORM_FORBIDDEN;

	/**
	 * The table/list has data binding reference to other elements. Its groups
	 * cannot be added, moved or removed.
	 */

	public static final String DESIGN_EXCEPTION_GROUPS_CHANGE_FORBIDDEN = MessageConstants.CONTENT_EXCEPTION_GROUPS_CHANGE_FORBIDDEN;

	/**
	 * The content element cannot be pasted into the container.
	 */

	public static final String DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED = MessageConstants.CONTENT_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED;

	/**
	 * Error code that indicates that the content can not be inserted twice for it
	 * is already in the tree.
	 */

	public static final String DESIGN_EXCEPTION_CONTENT_ALREADY_INSERTED = MessageConstants.CONTENT_EXCEPTION_CONTENT_ALREADY_INSERTED;

	/**
	 * Error code that indicates that the given position is out of range and
	 * invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_POSITION = MessageConstants.CONTENT_EXCEPTION_INVALID_POSITION;

	/**
	 * Error code that indicates that the shared dimension can not be inserted to
	 * report design and library.
	 */
	public static final String DESIGN_EXCEPTION_SHARE_DIMENSION_NOT_EXIST = MessageConstants.CONTENT_EXCEPTION_SHARE_DIMENSION_NOT_EXIST;

	/**
	 * Constructs the exception with container element, slot id, and error code.
	 * 
	 * @param element The container element.
	 * @param slotID  The slot within the container.
	 * @param errCode What went wrong.
	 */

	public ContentException(DesignElement element, int slotID, String errCode) {
		super(element, errCode);
		slot = slotID;
	}

	/**
	 * Constructs the exception with container element slot id, content element and
	 * error code.
	 * 
	 * @param element The container element.
	 * @param slotID  The slot within the container.
	 * @param content The content in the container element.
	 * @param errCode What went wrong.
	 */

	public ContentException(DesignElement element, int slotID, DesignElement content, String errCode) {
		super(element, errCode);
		slot = slotID;
		this.content = content;
	}

	/**
	 * Constructs the exception with container element slot id, content element and
	 * error code.
	 * 
	 * @param element The container element.
	 * @param slotID  The slot within the container.
	 * @param content The content in the container element.
	 * @param errCode What went wrong.
	 */

	public ContentException(DesignElement element, int slotID, DesignElement content, String errCode, String[] args) {
		super(element, args, errCode);
		slot = slotID;
		this.content = content;
	}

	/**
	 * Constructs the exception with container element, slot id, and error code.
	 * 
	 * @param element  The container element.
	 * @param propName The property name within the container.
	 * @param errCode  What went wrong.
	 */

	public ContentException(DesignElement element, String propName, String errCode) {
		super(element, errCode);
		containerProp = propName;
	}

	/**
	 * Constructs the exception with container element slot id, content element and
	 * error code.
	 * 
	 * @param element  The container element.
	 * @param propName The property name within the container.
	 * @param content  The content in the container element.
	 * @param errCode  What went wrong.
	 */

	public ContentException(DesignElement element, String propName, DesignElement content, String errCode) {
		super(element, errCode);
		containerProp = propName;
		this.content = content;
	}

	/**
	 * Returns the slot ID.
	 * 
	 * @return the slot ID
	 */

	public int getSlot() {
		return slot;
	}

	/**
	 * Gets the name of the container. It is either the slot name or the property
	 * name.
	 * 
	 * @return name of the container
	 */
	private String getContainerName() {
		if (containerProp != null && element.getPropertyDefn(containerProp) != null) {
			return containerProp;
		}

		return element.getDefn().getSlot(slot).getDisplayName();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_SLOT_NOT_FOUND
				|| sResourceKey == DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED) {
			String param = StringUtil.isBlank(containerProp) ? String.valueOf(slot) : containerProp;
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element), param });
		} else if (sResourceKey == DESIGN_EXCEPTION_NOT_CONTAINER || sResourceKey == DESIGN_EXCEPTION_HAS_NO_CONTAINER
				|| sResourceKey == DESIGN_EXCEPTION_MOVE_FORBIDDEN
				|| sResourceKey == DESIGN_EXCEPTION_HAS_DESCENDENTS) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_CONTENT_NOT_FOUND) {
			return ModelMessages.getMessage(sResourceKey,
					new String[] { getElementName(content), getContainerName(), getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_RECURSIVE) {
			return ModelMessages.getMessage(sResourceKey,
					new String[] { getElementName(content), getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_SLOT_IS_FULL) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getContainerName(), getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN
				|| sResourceKey == DESIGN_EXCEPTION_GROUPS_CHANGE_FORBIDDEN) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT
				|| sResourceKey == DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT
				|| sResourceKey == DESIGN_EXCEPTION_CONTENT_ALREADY_INSERTED) {
			return ModelMessages.getMessage(sResourceKey,
					new String[] { getElementName(content), getElementName(element), getContainerName() });
		} else if (sResourceKey == DESIGN_EXCEPTION_WRONG_TYPE || sResourceKey == DESIGN_EXCEPTION_DROP_FORBIDDEN) {
			return ModelMessages.getMessage(sResourceKey,
					new String[] { getElementName(element), getElementName(content), getContainerName() });
		} else if (sResourceKey == DESIGN_EXCEPTION_SHARE_DIMENSION_NOT_EXIST) {
			return ModelMessages.getMessage(sResourceKey,
					new String[] { getElementName(content), (String) this.oaMessageArguments[0] });
		}

		return ModelMessages.getMessage(sResourceKey);
	}
}
