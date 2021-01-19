package de.nordakademie.bachelor.gaestebuch.components.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import de.nordakademie.bachelor.gaestebuch.components.XVSlider;

public class XVSliderTest {

	private XVSlider slider = new XVSlider();

	public XVSliderTest() {

	}

	@Test
	public void testOneChildren() {
		Label label = new Label();
		slider.add(label);
		slider.open();
		assertEquals(Collections.singletonList(label), readChildren());
	}

	@Test
	public void testTwoChildren() {
		Label label = new Label();
		TextField field = new TextField();
		slider.add(label, field);
		slider.open();
		assertEquals(Arrays.asList(label, field), readChildren());
	}

	@Test
	public void testRemoveOneChildrenFromTwo() {
		Label firstLabel = new Label();
		Label secondLabel = new Label();
		slider.add(firstLabel, secondLabel);
		slider.remove(firstLabel);
		slider.open();
		assertEquals(Collections.singletonList(secondLabel), readChildren());
	}

	@Test
	public void testRemoveAllChildren() {
		Label label = new Label();
		TextField field = new TextField();
		slider.add(label, field);
		slider.remove(label, field);
		slider.open();
		assertEquals(Collections.emptyList(), readChildren());
	}

	@Test
	public void addAnotherComponent() {
		Label label = new Label();
		TextField field = new TextField();
		slider.add(label);
		slider.add(field);
		slider.open();
		assertEquals(Arrays.asList(label, field), readChildren());
	}

	@Test
	public void testRemoveNotAddedComponent() {
		Label label = new Label();
		assertThrows(IllegalArgumentException.class, () -> slider.remove(label));
	}

	@Test
	public void testReloadContent() {
		Label label = new Label();
		TextField field = new TextField();
		slider.add(label);
		slider.open();
		slider.add(field);
		slider.reloadContent();
		assertEquals(Arrays.asList(label, field), readChildren());
	}

	private List<Component> readChildren() {
		return slider.getChildren().collect(Collectors.toList());
	}
}
