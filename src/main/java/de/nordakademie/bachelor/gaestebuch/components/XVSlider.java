package de.nordakademie.bachelor.gaestebuch.components;

import java.util.ArrayList;
import java.util.Arrays;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;

public class XVSlider extends Div implements HasSize {

	private ArrayList<Component> components = new ArrayList<>();
	private String size = "0%";

	public XVSlider() {
		this.components = new ArrayList<>();
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public void add(Component... components) {
		if (this.components.size() == 0) {
			this.components.addAll(Arrays.asList(components));
		} else {
			attachComponents(components);
		}
	}

	@Override
	public void remove(Component... components) {
		if (this.components.containsAll(Arrays.asList(components))) {
			this.components.removeAll(Arrays.asList(components));
		} else {
			throw new IllegalArgumentException("All components to remove has to been added to the Slider before");
		}
	}

	public void open() {
		if (components.size() != 0) {
			this.components.forEach(c -> super.add(c));
		}
		super.setHeight(size);
	}

	public void close() {
		super.removeAll();
		super.setHeight("0%");
	}

	public void reloadContent() {
		super.removeAll();
		open();
	}

	private void attachComponents(Component... components) {
		ArrayList<Component> lists = new ArrayList<>();
		lists.addAll(this.components);
		lists.addAll(Arrays.asList(components));
		this.components = lists;
	}
}
