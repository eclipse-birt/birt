package org.eclipse.birt.report.soapengine.api;

import java.util.Arrays;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BindingList")
@XmlAccessorType(XmlAccessType.FIELD)
public class BindingList {
	@XmlElement(name = "Binding")
	private Binding[] binding;

	public BindingList() {
	}

	public BindingList(Binding[] binding) {
		this.binding = binding;
	}

	public Binding[] getBinding() {
		return binding;
	}

	public void setBinding(Binding[] binding) {
		this.binding = binding;
	}

	public Binding getBinding(int i) {
		return binding[i];
	}

	public void setBinding(int i, Binding value) {
		binding[i] = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BindingList))
			return false;
		if (this == obj)
			return true;
		BindingList other = (BindingList) obj;
		return Arrays.equals(this.binding, other.binding);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(binding);
	}
}
