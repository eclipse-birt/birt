package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CascadeParameter")
@XmlAccessorType(XmlAccessType.FIELD)
public class CascadeParameter implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "SelectionList")
	private List<SelectionList> selectionLists = new ArrayList<>();

	public CascadeParameter() {
	}

	public CascadeParameter(List<SelectionList> selectionLists) {
		this.selectionLists = selectionLists;
	}

	public List<SelectionList> getSelectionLists() {
		return selectionLists;
	}

	public void setSelectionLists(List<SelectionList> selectionLists) {
		this.selectionLists = selectionLists;
	}

	public void addSelectionList(SelectionList selection) {
		this.selectionLists.add(selection);
	}

	public SelectionList get(int index) {
		return this.selectionLists.get(index);
	}

	public void set(int index, SelectionList selection) {
		this.selectionLists.set(index, selection);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CascadeParameter))
			return false;
		CascadeParameter that = (CascadeParameter) o;
		return Objects.equals(selectionLists, that.selectionLists);
	}

	@Override
	public int hashCode() {
		return Objects.hash(selectionLists);
	}

	@Override
	public String toString() {
		return "CascadeParameter{" + "selectionLists=" + selectionLists + '}';
	}
}
