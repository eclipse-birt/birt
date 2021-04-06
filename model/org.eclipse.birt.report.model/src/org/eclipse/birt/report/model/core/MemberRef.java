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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;

/**
 * Reference to a property, list entry, or member in a list. All the following
 * are supported:
 * <p>
 * <ul>
 * <li>property</li>
 * 
 * <li>property.member</li>
 * <li>property.member.member</li>
 * <li>property.member.list[n]</li>
 * <li>property.member.list[n].member</li>
 * 
 * <li>property.list[n]</li>
 * <li>property.list[n].member</li>
 * <li>property.list[n].member.list[n]</li>
 * <li>property.list[n].member.list[n].member</li>
 * <li>property.list[n].member.member</li>
 * 
 * </ul>
 * <p>
 * The reference supports up to two level of list.member identification. This is
 * the most used by the element definitions.
 * 
 * @deprecated since 2.5
 */

public class MemberRef {

	public final static int PROPERTY = 0;

	public final static int PROPERTY_MEMBER = 1;
	public final static int PROPERTY_MEMBER_MEMBER = 2;
	public final static int PROPERTY_MEMBER_LISTn = 3;
	public final static int PROPERTY_MEMBER_LISTn_MEMBER = 4;

	public final static int PROPERTY_LISTn = 5;
	public final static int PROPERTY_LISTn_MEMBER = 6;
	public final static int PROPERTY_LISTn_MEMBER_LISTn = 7;
	public final static int PROPERTY_LISTn_MEMBER_LISTn_MEMBER = 8;
	public final static int PROPERTY_LISTn_MEMBER_MEMBER = 9;

	public final int refType;

	/**
	 * The property definition. Identifies the property.
	 */

	protected final ElementPropertyDefn propDefn;

	/**
	 * Array of two list indexes. Represents the ".list[n]" portion of the
	 * references.
	 */

	protected final int index[] = new int[2];

	/**
	 * Array of two member names. Represents the ".member" portion of the reference.
	 */

	protected final StructPropertyDefn member[] = new StructPropertyDefn[2];

	/**
	 * Number of list indexes: always 1 or 2.
	 */

	protected final int depth;

	/**
	 * The structure context of this reference.
	 */
	protected StructureContext context = null;

	/**
	 * Constructs one member reference as same as the given one.
	 * 
	 * @param memberRef the member reference to copy
	 */

	MemberRef(MemberRef memberRef) {
		refType = memberRef.refType;
		propDefn = memberRef.propDefn;
		member[0] = memberRef.member[0];
		member[1] = memberRef.member[1];
		index[0] = memberRef.index[0];
		index[1] = memberRef.index[1];
		depth = memberRef.depth;
	}

	/**
	 * Property (list, structure)
	 * 
	 * Reference to the top-level property list. Points to the first index within
	 * that list.
	 * 
	 * @param prop the property definition
	 */

	MemberRef(ElementPropertyDefn prop) {
		propDefn = prop;

		assert propDefn != null;

		refType = PROPERTY;
		index[0] = -1;
		depth = 1;
	}

	/**
	 * Reference to the nth item within the top-level property list.
	 * <p>
	 * property.list[n]
	 * 
	 * @param prop the property definition
	 * @param n    the list index
	 */

	MemberRef(ElementPropertyDefn prop, int n) {
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.isListType();
		assert n >= 0;

		refType = PROPERTY_LISTn;
		index[0] = n;
		depth = 1;
	}

	/**
	 * Reference to a member of a top-level structure.
	 * <p>
	 * property.member
	 * 
	 * @param prop       the element property definition which is structure or
	 *                   structure list type
	 * @param memberName the structure member name of the element property
	 *                   definition
	 */

	MemberRef(ElementPropertyDefn prop, String memberName) {
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE;
		assert !propDefn.isListType();

		IPropertyDefn memberDefn = propDefn.getStructDefn().getMember(memberName);
		assert memberDefn != null;

		refType = PROPERTY_MEMBER;
		member[0] = (StructPropertyDefn) memberDefn;
		index[0] = -1;
		depth = 1;
	}

	/**
	 * Reference to a member of a top-level structure.
	 * <p>
	 * property.member
	 * 
	 * @param prop      the element property definition which is structure or
	 *                  structure list type
	 * @param memberDef the structure member definition of the element property
	 *                  definition
	 */

	MemberRef(ElementPropertyDefn prop, StructPropertyDefn memberDef) {
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE;
		assert !propDefn.isList();

		refType = PROPERTY_MEMBER;
		member[0] = memberDef;
		index[0] = -1;
		depth = 1;
	}

	/**
	 * Reference to the the named member in the nth structure in the top-level list.
	 * <p>
	 * property.list[n].member
	 * 
	 * @param prop       the property definition
	 * @param n          the list index
	 * @param memberName the name of a member
	 */

	MemberRef(ElementPropertyDefn prop, int n, String memberName) {
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE;
		assert propDefn.isList();
		assert n >= 0;

		StructPropertyDefn memberDefn = (StructPropertyDefn) propDefn.getStructDefn().getMember(memberName);
		assert memberDefn != null;

		refType = PROPERTY_LISTn_MEMBER;
		index[0] = n;
		depth = 1;
		member[0] = memberDefn;
	}

	/**
	 * Reference to the identified member of the structure at the nth position
	 * within the top-level list.
	 * <p>
	 * property.list[n].member
	 * 
	 * @param prop      the property definition
	 * @param n         the list index
	 * @param memberDef the definition of the member
	 */

	MemberRef(ElementPropertyDefn prop, int n, StructPropertyDefn memberDef) {
		propDefn = prop;

		assert propDefn != null;
		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE;
		assert propDefn.isList();
		assert n >= 0;
		assert memberDef != null;

		refType = PROPERTY_LISTn_MEMBER;
		index[0] = n;
		member[0] = memberDef;
		depth = 1;
	}

	/**
	 * Reference to the nth item in a first or second level list.
	 * <p>
	 * property.member.list[n] <br>
	 * 
	 * property.list[n] <br>
	 * property.list[n].member.list[n]
	 * 
	 * @param ref reference to a property or member
	 * @param n   the list index
	 */

	MemberRef(MemberRef ref, int n) {
		propDefn = ref.propDefn;

		assert ref.depth < 2;
		assert n >= 0;
		assert ref.isListRef();

		if (propDefn.isListType()) {
			if (ref.refType == PROPERTY) {
				// property.list[n]

				refType = PROPERTY_LISTn;
				index[0] = n;
				depth = 1;
			} else {
				// property.list[n].member.list[n]

				assert ref.refType == PROPERTY_LISTn_MEMBER;
				assert ref.member[0].isListType();

				refType = PROPERTY_LISTn_MEMBER_LISTn;
				index[0] = ref.index[0];
				member[0] = ref.member[0];

				index[1] = n;
				depth = 2;
			}
		} else {
			// property.member.list[n]

			assert ref.refType == PROPERTY_MEMBER;
			assert ref.member[0].isList();

			refType = PROPERTY_MEMBER_LISTn;
			index[0] = n;
			member[0] = ref.member[0];
			depth = 1;
		}
	}

	/**
	 * Reference to a member in the nth item in a second-level list. The top-level
	 * list item is given by the member ref.
	 * <p>
	 * property.member.list[n].member <br>
	 * 
	 * property.list[n].member <br>
	 * property.list[n].member.list[n].member
	 * 
	 * @param ref        reference to a property or member
	 * @param n          the list index
	 * @param memberDefn the definition of the member
	 */

	MemberRef(MemberRef ref, int n, StructPropertyDefn memberDefn) {
		propDefn = ref.propDefn;
		assert propDefn != null;
		assert n >= 0;
		assert memberDefn != null;

		if (propDefn.isListType()) {
			if (ref.refType == PROPERTY_LISTn_MEMBER) {
				// property.list[n].member.list[n].member

				assert ref.member[0].isList();

				refType = PROPERTY_LISTn_MEMBER_LISTn_MEMBER;

				member[0] = ref.member[0];
				index[0] = ref.index[0];

				member[1] = memberDefn;
				index[1] = n;

				depth = 2;
			} else {
				// property.list[n].member

				assert ref.refType == PROPERTY;

				refType = PROPERTY_LISTn_MEMBER;
				member[0] = memberDefn;
				index[0] = n;

				depth = 1;
			}
		} else {
			// property.member.list[n].member

			assert ref.refType == PROPERTY_MEMBER;
			assert ref.member[0].isList();

			refType = PROPERTY_MEMBER_LISTn_MEMBER;
			index[0] = n;
			member[0] = ref.member[0];

			member[1] = memberDefn;

			depth = 1;
		}
	}

	/**
	 * Reference to a member within a first- or second-level structure. The
	 * structure is identified with the member ref.
	 * <p>
	 * property.member <br>
	 * property.member.member <br>
	 * 
	 * property.list[n].member <br>
	 * property.list[n].member.list[n].member <br>
	 * 
	 * property.list[n].member.member
	 * 
	 * @param ref        reference a structure
	 * @param memberName the name of a member
	 */

	MemberRef(MemberRef ref, String memberName) {
		this(ref, (StructPropertyDefn) ref.getStructDefn().getMember(memberName));
	}

	/**
	 * Reference to a member within a first- or second-level structure. The
	 * structure is identified with the member ref.
	 * <p>
	 * property.member <br>
	 * property.member.member <br>
	 * property.member.list[n].member
	 * 
	 * property.list[n].member <br>
	 * property.list[n].member.list[n].member <br>
	 * 
	 * property.list[n].member.member
	 * 
	 * @param ref        reference a structure
	 * @param memberDefn the definition of the member
	 */

	MemberRef(MemberRef ref, StructPropertyDefn memberDefn) {
		assert memberDefn != null;

		propDefn = ref.propDefn;

		if (propDefn.isListType()) {
			if (ref.refType == PROPERTY_LISTn) {
				// property.list[n].member

				refType = PROPERTY_LISTn_MEMBER;
				member[0] = memberDefn;
				index[0] = ref.index[0];

				depth = 1;
			} else {
				if (ref.refType == PROPERTY_LISTn_MEMBER_LISTn) {
					// property.list[n].member.list[n].member

					assert ref.member[0].isList();

					refType = PROPERTY_LISTn_MEMBER_LISTn_MEMBER;

					member[0] = ref.member[0];
					index[0] = ref.index[0];

					member[1] = memberDefn;
					index[1] = ref.index[1];

					depth = 2;
				} else {
					// property.list[n].member.member

					assert ref.refType == PROPERTY_LISTn_MEMBER;
					assert !ref.member[0].isList();

					refType = PROPERTY_LISTn_MEMBER_MEMBER;
					member[0] = ref.member[0];
					index[0] = ref.index[0];

					member[1] = memberDefn;

					depth = 1;
				}
			}
		} else {
			if (ref.refType == PROPERTY) {
				// property.member

				refType = PROPERTY_MEMBER;

				member[0] = memberDefn;
				index[0] = -1;

				depth = 1;
			} else if (ref.refType == PROPERTY_MEMBER) {
				// property.member.member

				assert !ref.member[0].isList();

				refType = PROPERTY_MEMBER_MEMBER;

				member[0] = ref.member[0];
				index[0] = -1;

				member[1] = memberDefn;
				depth = 1;
			} else {
				// property.member.list[n].member

				assert ref.refType == PROPERTY_MEMBER_LISTn;

				refType = PROPERTY_MEMBER_LISTn_MEMBER;

				member[0] = ref.member[0];
				index[0] = ref.index[0];

				member[1] = memberDefn;
				depth = 1;
			}
		}

	}

	/**
	 * Constructs the member reference with the context.
	 * 
	 * @param context
	 */
	public MemberRef(StructureContext context) {
		this.context = context;
		propDefn = null;

		refType = PROPERTY;
		index[0] = -1;
		depth = 1;
	}

	/**
	 * Returns a reference to the parent.
	 * <p>
	 * <strong>property.list[n].member.list[n][member] </strong>
	 * 
	 * @return a reference to the parent member
	 */

	public MemberRef getParentRef() {
		if (depth == 1)
			return null;
		return new MemberRef(propDefn, index[0], member[0]);
	}

	/**
	 * Gets the value of the referenced property, structure, or member.
	 * 
	 * @param module  the module
	 * 
	 * @param element the element for which to retrieve the value
	 * @return the retrieved value, which may be null
	 */

	public Object getValue(Module module, DesignElement element) {
		if (context != null)
			return context.getValue(module);
		if (propDefn.isListType()) {
			// property
			// property.list[n]
			// property.list[n].member
			// property.list[n].member.list[n]
			// property.list[n].member.list[n].member
			// property.list[n].member.member

			List list = getList(module, element);
			switch (refType) {
			case PROPERTY:
				return list;
			case PROPERTY_LISTn:
			case PROPERTY_LISTn_MEMBER_LISTn:
				return getValue(list, 0);
			case PROPERTY_LISTn_MEMBER: // reference the list itself.
			{
				Structure struct = getStructure(module, element);
				if (struct == null)
					return null;
				return struct.getProperty(module, member[0]);
			}
			case PROPERTY_LISTn_MEMBER_MEMBER:
			case PROPERTY_LISTn_MEMBER_LISTn_MEMBER: {
				Structure struct = getStructure(module, element);
				if (struct == null)
					return null;
				return struct.getProperty(module, member[1]);
			}
			default: {
				assert false;
				return null;
			}
			}
		}

		// property

		// property.member
		// property.member.member
		// property.member.list[n]
		// property.member.list[n].member

		Structure struct = getStructure(module, element);
		if (struct == null)
			return null;

		switch (refType) {
		case PROPERTY:
		case PROPERTY_MEMBER_LISTn:
			return struct;
		case PROPERTY_MEMBER:
			return struct.getProperty(module, member[0]);
		case PROPERTY_MEMBER_MEMBER:
		case PROPERTY_MEMBER_LISTn_MEMBER:
			return struct.getProperty(module, member[1]);
		default: {
			assert false;
			return null;
		}
		}
	}

	/**
	 * Gets the local value of the referenced property, structure, or member.
	 * 
	 * @param module  the module
	 * 
	 * @param element the element for which to retrieve the value
	 * @return the retrieved value, which may be null
	 */

	public Object getLocalValue(Module module, DesignElement element) {
		if (context != null)
			return context.getLocalValue(module);
		Structure struct = getStructure(module, element);
		if (struct == null)
			return null;
		if (propDefn.isListType()) {
			// property
			// property.list[n]
			// property.list[n].member
			// property.list[n].member.list[n]
			// property.list[n].member.list[n].member
			// property.list[n].member.member

			switch (refType) {
			case PROPERTY:
				return getList(module, element);
			case PROPERTY_LISTn:
			case PROPERTY_LISTn_MEMBER_LISTn:
				return struct;
			case PROPERTY_LISTn_MEMBER: // reference the list itself.
				return struct.getLocalProperty(module, member[0]);
			case PROPERTY_LISTn_MEMBER_MEMBER:
			case PROPERTY_LISTn_MEMBER_LISTn_MEMBER:
				return struct.getLocalProperty(module, member[1]);
			default: {
				assert false;
				return null;
			}
			}
		}

		// property

		// property.member
		// property.member.member
		// property.member.list[n]
		// property.member.list[n].member

		switch (refType) {
		case PROPERTY:
		case PROPERTY_MEMBER_LISTn:
			return struct;
		case PROPERTY_MEMBER:
			return struct.getLocalProperty(module, member[0]);
		case PROPERTY_MEMBER_MEMBER:
		case PROPERTY_MEMBER_LISTn_MEMBER:
			return struct.getLocalProperty(module, member[1]);
		default: {
			assert false;
			return null;
		}
		}
	}

	/**
	 * Returns the definition of the property portion of the reference.
	 * 
	 * @return the property definition
	 */

	public ElementPropertyDefn getPropDefn() {
		if (context != null)
			return context.getElementProp();
		return propDefn;
	}

	/**
	 * Returns the definition of the member to which the reference points.
	 * <p>
	 * property. <strong>member </strong> <br>
	 * property.member. <strong>member </strong> <br>
	 * property.member.list[n]. <strong>member </strong> <br>
	 * 
	 * property.list[n]. <strong>member </strong> <br>
	 * property.list[n].member.list[n]. <strong>member </strong> <br>
	 * 
	 * @return the definition of the target member
	 */

	public PropertyDefn getMemberDefn() {
		if (context != null)
			return context.getPropDefn();
		return member[1] == null ? member[0] : member[1];
	}

	/**
	 * Returns the structure pointed to by the reference.
	 * <p>
	 * <strong>property </strong>[.member] <br>
	 * property. <strong>member </strong>.member <br>
	 * property.member. <strong>list[n] </strong>[.member] <br>
	 * 
	 * property. <strong>list[n] </strong>[.member] <br>
	 * property.list[n].member. <strong>list[n] </strong>[.member] <br>
	 * property.list[n]. <strong>member </strong>.member
	 * 
	 * @param module  the module
	 * 
	 * @param element the element from which to retrieve the structure
	 * @return the value of the referenced structure
	 */

	public Structure getStructure(Module module, DesignElement element) {
		if (context != null)
			return context.getStructure();
		if (propDefn.isListType()) {
			ArrayList list = (ArrayList) element.getProperty(module, propDefn);
			if (list == null)
				return null;

			Object tmpValue = null;
			switch (refType) {
			case PROPERTY_LISTn:
			case PROPERTY_LISTn_MEMBER:
				tmpValue = getValue(list, 0);
				if (!(tmpValue instanceof Structure))
					return null;

				return (Structure) tmpValue;
			case PROPERTY_LISTn_MEMBER_LISTn:
			case PROPERTY_LISTn_MEMBER_LISTn_MEMBER:

				// If the top-level index is out of range, then there
				// is no value.

				tmpValue = getValue(list, 0);
				if (!(tmpValue instanceof Structure))
					return null;

				Structure struct = (Structure) tmpValue;

				// Check the second-level list if needed.

				assert member[0].isListType();
				list = (ArrayList) struct.getProperty(module, member[0]);

				tmpValue = getValue(list, 1);
				if (!(tmpValue instanceof Structure))
					return null;

				return (Structure) tmpValue;

			case PROPERTY_LISTn_MEMBER_MEMBER:

				// If the top-level index is out of range, then there
				// is no value.

				tmpValue = getValue(list, 0);
				if (!(tmpValue instanceof Structure))
					return null;

				struct = (Structure) tmpValue;

				assert member[0].getStructDefn() != null;

				struct = (Structure) struct.getProperty(null, member[0]);

				return struct;

			}

			return null;
		}

		Structure struct = (Structure) element.getProperty(module, propDefn);
		if (struct == null)
			return null;

		if (index[0] >= 0) {
			// property.member.list[n]
			// property.member.list[n].member

			assert member[0].isListType();
			ArrayList list = (ArrayList) struct.getProperty(module, member[0]);

			Object tmpValue = getValue(list, 0);
			if (!(tmpValue instanceof Structure))
				return null;

			return (Structure) tmpValue;
		}

		if (member[1] != null) {
			// property.member.member

			assert !member[0].isListType();
			return (Structure) struct.getProperty(module, member[0]);
		}

		// property.member
		// property

		return struct;
	}

	/**
	 * Returns the definition of the structure pointed to by the reference.
	 * <p>
	 * <strong>property </strong> <br>
	 * <strong>property </strong>.member <br>
	 * 
	 * property. <strong>member </strong>.member <br>
	 * property.member. <strong>list[n] </strong> <br>
	 * property.member. <strong>list[n] </strong>.member <br>
	 * 
	 * property. <strong>list[n] </strong>[.member] <br>
	 * property.list[n].member. <strong>list[n] </strong>[.member] <br>
	 * property.list[n]. <strong>member </strong>.member
	 * 
	 * @return the definition of the structure pointed to by the reference
	 */

	public IStructureDefn getStructDefn() {
		if (context != null)
			return context.getStructDefn();
		switch (refType) {
		case PROPERTY:
		case PROPERTY_LISTn:
		case PROPERTY_MEMBER:
			return propDefn.getStructDefn();
		case PROPERTY_LISTn_MEMBER:
		case PROPERTY_LISTn_MEMBER_MEMBER:
		case PROPERTY_LISTn_MEMBER_LISTn:
		case PROPERTY_LISTn_MEMBER_LISTn_MEMBER:
		case PROPERTY_MEMBER_MEMBER:
		case PROPERTY_MEMBER_LISTn_MEMBER:
		case PROPERTY_MEMBER_LISTn:
			return member[0].getStructDefn();
		default:
			assert false;
			return null;
		}

	}

	/**
	 * Returns the list index pointed to by this reference.
	 * <p>
	 * property.member.list[ <strong>n </strong> ][.member] <br>
	 * 
	 * property.list[ <strong>n </strong>][.member] <br>
	 * property.list[n].member.list[ <strong>n </strong>][.member]
	 * 
	 * @return the list index pointed to by this reference
	 */

	public int getIndex() {
		if (context != null)
			return context.getIndex(null);
		if (propDefn.isListType())
			return index[depth - 1];

		return index[0];
	}

	/**
	 * Returns the depth of the reference: 1 or 2.
	 * <p>
	 * property.list[n][.member] --&gt 1 <br>
	 * property.member[.list[n]]
	 * 
	 * property.list[n].member.list[n][.member] --&gt 2
	 * property.member.list[n].member property.member.member
	 * 
	 * @return the depth of the reference
	 */

	public int getDepth() {
		return depth;
	}

	/**
	 * Returns the list pointed to by this reference.
	 * <p>
	 * <strong>property </strong> <br>
	 * property. <strong>list </strong>[n][.member] <br>
	 * property.list[n].member. <strong>list </strong>[n][.member] property.list[n]
	 * <strong>.member.member </strong> <br>
	 * 
	 * property.member. <strong>list </strong>[n][.member] <br>
	 * 
	 * @param module  the module
	 * 
	 * @param element the element for which to retrieve the list
	 * @return the list of structures
	 */

	public List getList(Module module, DesignElement element) {
		if (context != null)
			return context.getList(module);
		if (propDefn.isListType()) {
			// Get the property list. If the list is null, there
			// is no value.

			List list = (ArrayList) element.getProperty(module, propDefn);
			if (list == null)
				return null;

			switch (refType) {
			case PROPERTY:
			case PROPERTY_LISTn:
				return list;

			case PROPERTY_LISTn_MEMBER:
			case PROPERTY_LISTn_MEMBER_LISTn:

				if (!member[0].isListType())
					return list;

				// If the top-level index is out of range, then there
				// is no value.

				Structure struct = (Structure) getValue(list, 0);
				if (struct == null) {
					return null;
				}

				// Check the second-level list if needed.

				assert member[0].isListType();
				list = (List) struct.getProperty(module, member[0]);

				return list;

			case PROPERTY_LISTn_MEMBER_LISTn_MEMBER:
			case PROPERTY_LISTn_MEMBER_MEMBER:

				// these 2 cases are not supported.

				return null;

			}

			return list;
		}

		// not a list property

		Structure struct = (Structure) element.getProperty(module, propDefn);
		if (struct != null && member[0] != null && member[0].isList())
			return (ArrayList) struct.getProperty(module, member[0]);

		return null;
	}

	/**
	 * Indicates whether this member reference points to a list.
	 * 
	 * @return true if points to a list.
	 */

	public boolean isListRef() {
		if (context != null)
			return context.isListRef();
		switch (refType) {
		case PROPERTY:
			return propDefn.isListType();
		case PROPERTY_MEMBER:
		case PROPERTY_LISTn_MEMBER:
			return member[0].isListType();
		case PROPERTY_MEMBER_MEMBER:
		case PROPERTY_MEMBER_LISTn_MEMBER:
		case PROPERTY_LISTn_MEMBER_LISTn_MEMBER:
		case PROPERTY_LISTn_MEMBER_MEMBER:
			return member[1].isListType();
		default:
			return false;
		}

	}

	/**
	 * Returns the structure at the given position in structure list.
	 * 
	 * @param list  structure list
	 * @param level the structure position in first index or second index
	 * @return structure if the position is in list range, otherwise, return null.
	 */

	protected Object getValue(List list, int level) {
		assert level == 0 || level == 1;

		if (list == null)
			return null;
		if (index[level] < 0 || index[level] >= list.size())
			return null;

		Object retValue = list.get(index[level]);

		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof MemberRef))
			return false;

		MemberRef temp = (MemberRef) obj;

		return (temp.depth == this.depth && temp.refType == this.refType && temp.propDefn.equals(this.propDefn)
				&& equalsIntArray(temp.index, this.index) && equalArray(temp.member, this.member));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */

	public int hashCode() {
		int hash = 1;

		hash = 7 * hash + depth;
		hash = 7 * hash + refType;
		hash = 7 * hash + propDefn.hashCode();
		hash = addIntArrayHashCode(hash, index);
		hash = addObjectArrayHashCode(hash, member);
		return hash;
	}

	/**
	 * Compare two int arary value.
	 * 
	 * @param arrayOne
	 * @param arrayTwo
	 * @return
	 */
	protected boolean equalsIntArray(int[] arrayOne, int[] arrayTwo) {
		if (arrayOne == arrayTwo)
			return true;

		if (arrayOne == null || arrayTwo == null)
			return false;

		if (arrayOne.length != arrayTwo.length)
			return false;
		for (int i = 0; i < arrayOne.length; ++i) {
			int one = arrayOne[i];
			int two = arrayTwo[i];
			if (one != two)
				return false;
		}
		return true;
	}

	/**
	 * Compare two object array value.
	 * 
	 * @param arrayOne
	 * @param arrayTwo
	 * @return
	 */
	protected boolean equalArray(Object[] arrayOne, Object[] arrayTwo) {
		if (arrayOne == arrayTwo)
			return true;

		if (arrayOne == null || arrayTwo == null)
			return false;

		if (arrayOne.length != arrayTwo.length)
			return false;
		for (int i = 0; i < arrayOne.length; ++i) {
			Object one = arrayOne[i];
			Object two = arrayTwo[i];
			if ((one != null && !one.equals(two)) || (two != null && !two.equals(one)))
				return false;
		}
		return true;
	}

	/**
	 * Add int array's hash code.
	 * 
	 * @param hash
	 * @param array
	 * @return
	 */
	protected int addIntArrayHashCode(int hash, int[] array) {
		assert array != null;
		hash = 7 * hash;
		for (int i = 0; i < array.length; ++i) {
			hash += array[i];
		}
		return hash;
	}

	/**
	 * Add object array's hash code
	 * 
	 * @param hash
	 * @param array
	 * @return
	 */
	protected int addObjectArrayHashCode(int hash, Object[] array) {
		assert array != null;
		hash = 7 * hash;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == null)
				continue;
			hash += array[i].hashCode();
		}
		return hash;
	}

	/**
	 * Gets the context of this reference.
	 * 
	 * @return
	 */
	public StructureContext getContext() {
		return this.context;
	}

}