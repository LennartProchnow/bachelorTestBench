package de.nordakademie.bachelor.gaestebuch;

import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import de.nordakademie.bachelor.gaestebuch.components.XVSlider;
import de.nordakademie.bachelor.gaestebuch.model.Post;

@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
public class MainView extends VerticalLayout {

	private static final String NAME_FIELD_LABEL = "Name:";
	private static final String RATING_FIELD_LABEL = "Nachricht:";
	private ArrayList<Post> posts = new ArrayList<>();
	private HorizontalLayout inputFormularLayout = new HorizontalLayout();
	private TextField textFieldName = new TextField(NAME_FIELD_LABEL);
	private TextField textFieldRating = new TextField(RATING_FIELD_LABEL);
	private Button saveButton = new Button(new Icon(VaadinIcon.PENCIL));
	private Grid<Post> grid = new Grid<>(Post.class);
	private XVSlider slider = new XVSlider();
	private TextField sliderNameField = new TextField(NAME_FIELD_LABEL);
	private TextField sliderRatingField = new TextField(RATING_FIELD_LABEL);
	private Button sliderButton = new Button(new Icon(VaadinIcon.PENCIL));
	private Binder<Post> sliderBinder = new Binder<>(Post.class);
	
    public MainView() {
    	add(new Label("Willkommen zum Gästebuch"));
    	setupInputFormular();
    	setupGrid();
    	setupSlider();
    }
    
    private void setupInputFormular() {
    	setupInputFields();
    	Binder<Post> binder = new Binder<>(Post.class);
    	binder.setBean(new Post("",""));
    	binder.forField(this.textFieldName)
    		.asRequired("Dieses Feld darf nicht leergelassen werden")
    		.withValidator(text -> text.matches("^[a-zA-Z]+$"), "Hier sind nur Buchstaben erlaubt")
    		.bind(Post::getName, Post::setName);
    	binder.forField(this.textFieldRating)
    		.asRequired("Dieses Feld darf nicht leergelassen werden")
    		.bind(Post::getName, Post::setName);
    	binder.addStatusChangeListener(e -> {
    		if(binder.isValid()) {
    			this.saveButton.setEnabled(true);
    		} else {
    			this.saveButton.setEnabled(false);
    		}
    	});
    	this.saveButton.addClickListener(e -> {
    		if(binder.isValid()) {
    			this.posts.add(new Post(this.textFieldName.getValue(), 
    									this.textFieldRating.getValue()));
    			this.grid.setItems(posts);
    		}
    	});
    }
    
    private void setupInputFields() {
    	this.textFieldName.setId("textFieldName");
    	this.textFieldName.getElement()
    		.setAttribute("aria-label","Dieses Textfeld ist mit dem Namen des Kommentierenden zu füllen");
    	this.textFieldName.setValueChangeMode(ValueChangeMode.EAGER);
    	this.textFieldRating.getElement()
    		.setAttribute("aria-label","Dieses Textfeld ist mit dem Gästebucheintrag zu füllen");
    	this.textFieldRating.setId("textFieldRating");
    	this.textFieldRating.setValueChangeMode(ValueChangeMode.EAGER);
    	this.saveButton.getElement()
    		.setAttribute("aria-label","Mit diesem Button werden die Eingaben gespeichert.");
    	this.saveButton.setId("saveButton");
    	this.saveButton.setEnabled(false);
    	this.inputFormularLayout.add(this.textFieldName, this.textFieldRating, this.saveButton);
    	this.add(this.inputFormularLayout);
    }

    private void setupGrid() {
    	this.grid.setItems(this.posts);
    	this.grid.getColumnByKey("name").setHeader("Name");
    	this.grid.getColumnByKey("ratingText").setHeader("Nachricht");
    	this.grid.setSelectionMode(SelectionMode.SINGLE);
    	SingleSelect<Grid<Post>,Post> postSelect = this.grid.asSingleSelect();
    	postSelect.addValueChangeListener(e -> {
    		this.slider.open();
    		updateSliderProperties(e.getValue());
    	});
    	this.add(grid);
    }
    
    private void reloadGrid () {
    	this.grid.setItems(posts);
    }
    
    private void setupSlider() {
    	setupSliderFields();
    	this.slider.add(this.sliderNameField, this.sliderRatingField, this.sliderButton);
    	this.add(this.slider);
    }
    
    private void setupSliderFields() {
    	this.sliderNameField.setId("sliderNameField");
    	this.sliderNameField.setValueChangeMode(ValueChangeMode.EAGER);
    	this.sliderRatingField.setId("sliderRatingField");
    	this.sliderRatingField.setValueChangeMode(ValueChangeMode.EAGER);
    	this.sliderButton.setId("sliderButton");
    	this.sliderBinder.forField(this.sliderNameField)
	    	.asRequired("Dieses Feld darf nicht leergelassen werden")
			.withValidator(text -> text.matches("^[a-zA-Z]+$"), "Hier sind nur Buchstaben erlaubt")
			.bind(Post::getName, Post::setName);
		this.sliderBinder.forField(this.sliderRatingField)
			.asRequired("Dieses Feld darf nicht leergelassen werden")
			.bind(Post::getRatingText, Post::setRatingText);
    }

    private void updateSliderProperties(Post post) {
    	this.sliderBinder.setBean(post);
    	if(this.sliderBinder.isValid()) {
    		this.sliderButton.setEnabled(true);
    	}
    	this.sliderBinder.addStatusChangeListener(e -> {
    		if(this.sliderBinder.isValid()) {
    			this.sliderButton.setEnabled(true);
    		} else {
    			this.sliderButton.setEnabled(false);
    		}
    	});
    	this.sliderButton.addClickListener(e -> {
    		if(this.sliderBinder.isValid()) {
    			this.posts.set(this.posts.indexOf(post), post);
    			reloadGrid();
    			this.slider.close();
    		}
    	});
    }
}
