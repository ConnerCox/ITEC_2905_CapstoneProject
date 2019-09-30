package resources;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DisplayFirearm {
	//attributes of a Firearm
	private final SimpleObjectProperty<ImageView> imageView;
	private final SimpleStringProperty brand;
	private final SimpleStringProperty model;
	private final SimpleStringProperty serialNum;
	private final SimpleStringProperty caliber;
	private final SimpleDoubleProperty estValue;
	private final SimpleStringProperty notes;
	
	//zero arg constructor
	public DisplayFirearm() {
		imageView = null;
		brand = null;
		model = null;
		serialNum = null;
		caliber = null;
		estValue = null;
		notes = null;
	}
	
	//constructor
	public DisplayFirearm(Firearm gun) {
		this.imageView = new SimpleObjectProperty<ImageView>(new ImageView(gun.getImage()));
		this.brand = new SimpleStringProperty(gun.getBrand());
		this.model = new SimpleStringProperty(gun.getModel());
		this.serialNum = new SimpleStringProperty(gun.getSerialNum());
		this.caliber = new SimpleStringProperty(gun.getCaliber());
		this.estValue = new SimpleDoubleProperty(gun.getEstValue());
		this.notes = new SimpleStringProperty(gun.getNotes());
	}
	
	//getters
	public ImageView getImageView() {
		return imageView.get();
	}
	public String getBrand() {
		return brand.get();
	}
	public String getModel() {
		return model.get();
	}
	public String getSerialNum() {
		return serialNum.get();
	}
	public String getCaliber() {
		return caliber.get();
	}
	public double getEstValue() {
		return estValue.get();
	}
	public String getNotes() {
		return notes.get();
	}
	
	
	@Override
	public String toString() {
		String s = "Brand: "+brand.get()+", Model: "+model.get()+", Serial Number: "+serialNum.get()+", Caliber: "+caliber.get()+", EstValue: "+estValue.get()+", and Notes: "+notes.get();
		return s;
	}
	
}
