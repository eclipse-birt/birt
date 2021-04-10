
package org.eclipse.birt.report.designer.internal.ui.views.memento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.util.DEUtil;

public class MementoElement {

	public static final String Type_Element = "Element"; //$NON-NLS-1$
	public static final String Type_Viewer = "Viewer"; //$NON-NLS-1$
	public static final String Type_View = "View"; //$NON-NLS-1$
	public static final String Type_Memento = "Memento"; //$NON-NLS-1$

	public static final String ATTRIBUTE_SELECTED = "Selected"; //$NON-NLS-1$

	private String key;
	private Object value;
	private MementoElement parent;
	private List children = new ArrayList();
	private HashMap attributeMap = new HashMap();

	private String type = Type_Memento;

	public void setAttribute(String name, Object value) {
		attributeMap.put(name, value);
	}

	public Object getAttribute(String name) {
		return attributeMap.get(name);
	}

	public MementoElement(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public MementoElement(String key, Object value, String type) {
		this.key = key;
		this.value = value;
		this.type = type;
	}

	public MementoElement(String key) {
		this.key = key;
	}

	public MementoElement() {
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public MementoElement getParent() {
		return parent;
	}

	public void setParent(MementoElement parent) {
		this.parent = parent;
		this.parent.addChild(this);
	}

	public MementoElement[] getChildren() {
		MementoElement[] childrenMementos = new MementoElement[children.size()];
		children.toArray(childrenMementos);
		return childrenMementos;
	}

	public void setChildren(List children) {
		this.children = children;
		for (int i = 0; i < children.size(); i++) {
			MementoElement property = (MementoElement) children.get(i);
			property.setParent(this);
		}
	}

	public void addChild(MementoElement child) {
		if (!children.contains(child)) {
			children.add(child);
			child.setParent(this);
		} else {
			if (child.getParent() != this)
				child.setParent(this);
		}
	}

	public MementoElement getChild(String key) {
		for (int i = 0; i < children.size(); i++) {
			MementoElement property = (MementoElement) children.get(i);
			if (property.getKey().equals(key))
				return property;
		}
		return null;
	}

	public MementoElement getChild(int index) {
		if (index > -1 && index < children.size())
			return (MementoElement) children.get(index);
		else
			return null;
	}

	public boolean equals(Object obj) {
		if (obj instanceof MementoElement) {
			MementoElement memento = (MementoElement) obj;
			if (this == obj)
				return true;
			if (memento.getMementoType().equals(MementoElement.Type_Memento))
				return true;
			if (memento.getMementoType().equals(MementoElement.Type_View)
					|| memento.getMementoType().equals(MementoElement.Type_Viewer)) {
				if (memento.getKey().equals(key))
					return true;
				else
					return false;
			}
			if (memento.getMementoType().equals(MementoElement.Type_Element)) {
				if (memento.getValue() == null)
					return false;
				if (memento.getKey().equals(key) && memento.getValue().equals(value))
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public int hashCode() {
		if (getMementoType().equals(MementoElement.Type_Memento)) {
			return getMementoType().hashCode();
		} else if (getMementoType().equals(MementoElement.Type_View)
				|| getMementoType().equals(MementoElement.Type_Viewer)) {
			return getMementoType().hashCode() * 13 + DEUtil.resolveNull(getKey()).hashCode();
		} else if (getMementoType().equals(MementoElement.Type_Element)) {
			return getMementoType().hashCode() * 13 + DEUtil.resolveNull(getKey()).hashCode() * 7
					+ (getValue() == null ? "" : getValue()).hashCode();
		}
		return super.hashCode();
	}

	public void removeChild(MementoElement child) {
		children.remove(child);
		child.dispose();
	}

	public void dispose() {
		for (int i = 0; i < children.size(); i++) {
			MementoElement property = (MementoElement) children.get(i);
			property.dispose();
		}
		children.clear();
	}

	public String getMementoType() {
		return type;
	}

	public void setMementoType(String type) {
		this.type = type;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
