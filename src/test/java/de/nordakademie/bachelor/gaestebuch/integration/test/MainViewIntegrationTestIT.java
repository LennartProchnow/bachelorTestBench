package de.nordakademie.bachelor.gaestebuch.integration.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchTestCase;

public class MainViewIntegrationTestIT extends TestBenchTestCase{

	private TextFieldElement textFieldName;
	private TextFieldElement textFieldRating;
	private ButtonElement saveButton;
	private GridElement grid;
	private TextFieldElement sliderNameField;
	private TextFieldElement sliderRatingField;
	private ButtonElement sliderSaveButton;
	
	@BeforeEach
	public void setup() throws Exception {
		String seperator = System.getProperty("file.separator");
		String path = "."+seperator+"driver"+seperator+"chromedriver.exe";
		System.setProperty("webdriver.chrome.driver",path);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--disable-gpu");
		setDriver(new ChromeDriver(options));

		getDriver().get("http://localhost:8080");
		
		this.textFieldName = $(TextFieldElement.class).id("textFieldName");
		this.textFieldRating = $(TextFieldElement.class).id("textFieldRating");
		this.saveButton = $(ButtonElement.class).id("saveButton");
		this.grid = $(GridElement.class).first();
	}


	@Test
	public void testAriaLabelTextFieldName() {
		assertTrue($(TextFieldElement.class).attribute("aria-label", "Dieses Textfeld ist mit dem Namen des Kommentierenden zu füllen").exists());
	}

	@Test
	public void testAriaLabelRatingField() {
		assertTrue($(TextFieldElement.class).attribute("aria-label", "Dieses Textfeld ist mit dem Gästebucheintrag zu füllen").exists());
	}

	@Test
	public void testAlphanumericValidationTextFieldName() {
		this.textFieldName.setValue("a1a");
		assertEquals("true",this.textFieldName.getAttribute("invalid"));
	}

	@Test
	public void testIsRequiredValidationInput() {
		this.textFieldName.setValue("a");
		this.textFieldName.setValue("");
		this.textFieldRating.setValue("a");
		this.textFieldRating.setValue("");
		assertEquals("true",this.textFieldName.getAttribute("invalid"));
		assertEquals("true",this.textFieldRating.getAttribute("invalid"));
	}
	
	@Test
	public void testDisabledSaveButtonAtInit () {
		assertFalse(this.saveButton.isEnabled());
	}
	
	@Test
	public void testDisabledSaveButtonInvalidTextFieldName () {
		this.textFieldName.setValue("a1");
		this.textFieldRating.setValue("a");
		assertFalse(this.saveButton.isEnabled());
	}
	
	@Test
	public void testDisabledSaveButtonInvalidTextFieldRating () {
		this.textFieldName.setValue("a");
		this.textFieldRating.setValue("a");
		this.textFieldRating.setValue("");
		assertFalse(this.saveButton.isEnabled());
	}
	
	@Test
	public void testDisabledSaveButtonInvalidBothTextFields () {
		this.textFieldName.setValue("a1");
		this.textFieldRating.setValue("a");
		this.textFieldRating.setValue("");
		assertFalse(this.saveButton.isEnabled());
	}
	
	@Test
	public void addAPostIntoGrid() {
		makeAValidPost();
		this.grid.getCell(0, 0).getText();
		assertEquals("Lennart", this.grid.getCell(0, 0).getText());
	}
	
	@Test
	public void testSliderComponentsDoNotExistsYet() {
		assertFalse($(TextFieldElement.class).attributeContains("id", "sliderNameField").exists());
	}
	
	@Test
	public void testSliderFillUp() {
		fillUpSliderComponents();
		initSliderComponents();
		assertEquals("Lennart", this.sliderNameField.getValue());
		assertEquals("Der Aufenthalt war sehr schön.", this.sliderRatingField.getValue());
	}
	
	@Test
	public void testValidationSliderBeanValidInput() {
		fillUpSliderComponents();
		initSliderComponents();
		this.sliderRatingField.setValue("Der Aufenthalt war echt cool.");
		assertEquals("false",this.textFieldRating.getAttribute("invalid"));
		assertTrue(this.sliderSaveButton.isEnabled());
	}

	@Test
	public void testValidationSliderRatingTextRequired() {
		fillUpSliderComponents();
		initSliderComponents();
		this.sliderRatingField.setValue("a");
		this.sliderRatingField.setValue("");
		assertEquals("true",this.sliderRatingField.getAttribute("invalid"));
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationSliderNameTextRequired() {
		fillUpSliderComponents();
		initSliderComponents();
		this.sliderNameField.setValue("a");
		this.sliderNameField.setValue("");
		assertEquals("true",this.sliderNameField.getAttribute("invalid"));
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationSliderNameTextAlphanumerics() {
		fillUpSliderComponents();
		initSliderComponents();
		this.sliderNameField.setValue("a1a");
		assertEquals("true",this.sliderNameField.getAttribute("invalid"));
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationTextSliderBeanBothRequired() {
		fillUpSliderComponents();
		initSliderComponents();
		this.sliderNameField.setValue("a");
		this.sliderNameField.setValue("");
		this.sliderRatingField.setValue("a");
		this.sliderRatingField.setValue("");
		assertEquals("true",this.sliderRatingField.getAttribute("invalid"));
		assertEquals("true",this.sliderRatingField.getAttribute("invalid"));
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationBothTextFieldsSliderBean() {
		fillUpSliderComponents();
		initSliderComponents();
		this.sliderNameField.setValue("a1a");
		this.sliderRatingField.setValue("a");
		this.sliderRatingField.setValue("");
		assertEquals("true",this.sliderNameField.getAttribute("invalid"));
		assertEquals("true",this.sliderRatingField.getAttribute("invalid"));
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void addAnotherPost() {
		makeAValidPost();
		this.textFieldName.setValue("Hans");
		this.textFieldRating.setValue("Ne so schön fand ich es nicht.");
		this.saveButton.click();
		assertEquals("Ne so schön fand ich es nicht.", this.grid.getCell(1, 1).getText());
	}
	
	@Test
	public void addADuplicatedPost() {
		makeAValidPost();
		makeAValidPost();
		assertEquals("Der Aufenthalt war sehr schön.", this.grid.getCell(1, 1).getText());
	}
	
	@Test
	public void editAPostInGrid() {
		fillUpSliderComponents();
		initSliderComponents();
		this.sliderRatingField.setValue("Der Aufenthalt war wirklich sehr schön.");
		this.sliderSaveButton.click();
		assertEquals("Der Aufenthalt war wirklich sehr schön.", this.grid.getCell(0, 1).getText());
	}
	
	private void makeAValidPost() {
		this.textFieldName.setValue("Lennart");
		this.textFieldRating.setValue("Der Aufenthalt war sehr schön.");
		this.saveButton.click();
	}
	
	private void fillUpSliderComponents() {
		makeAValidPost();
		this.grid.select(0);
	}
	
	private void initSliderComponents() {
		this.sliderNameField = $(TextFieldElement.class).id("sliderNameField");
		this.sliderRatingField = $(TextFieldElement.class).id("sliderRatingField");
		this.sliderSaveButton = $(ButtonElement.class).id("sliderButton");
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		getDriver().quit();
	}
}
