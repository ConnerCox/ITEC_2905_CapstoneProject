package resources;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class SimplePropertyFirearm {
	//attributes of a Firearm
	private final SimpleObjectProperty<Image> image;
	private final SimpleStringProperty brand;
	private final SimpleStringProperty model;
	private final SimpleStringProperty serialNum;
	private final SimpleStringProperty caliber;
	private final SimpleDoubleProperty estValue;
	private final SimpleStringProperty notes;
	
	//zero arg constructor
	public SimplePropertyFirearm() {
		image = null;
		brand = null;
		model = null;
		serialNum = null;
		caliber = null;
		estValue = null;
		notes = null;
	}
	
	//constructor
	public SimplePropertyFirearm(Firearm gun) {
		this.image = new SimpleObjectProperty<Image>(gun.getImage());
		this.brand = new SimpleStringProperty(gun.getBrand());
		this.model = new SimpleStringProperty(gun.getModel());
		this.serialNum = new SimpleStringProperty(gun.getSerialNum());
		this.caliber = new SimpleStringProperty(gun.getCaliber());
		this.estValue = new SimpleDoubleProperty(gun.getEstValue());
		this.notes = new SimpleStringProperty(gun.getNotes());
	}
	
	//getters
	public Image getImage() {
		return image.get();
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

//	//setters
//	public void setImage(Image image) {
//		this.image = new SimpleObjectProperty<Image>(image);
//	}
//	public void setBrand(String brand) {
//		this.brand = new SimpleStringProperty(brand);
//	}
//	public void setModel(String model) {
//		this.model = new SimpleStringProperty(model);
//	}
//	public void setSerialNum(String serialNum) {
//		this.serialNum = new SimpleStringProperty(serialNum);
//	}
//	public void setCaliber(String caliber) {
//		this.caliber = new SimpleStringProperty(caliber);
//	}
//	public void setEstValue(double estValue) {
//		this.estValue = new SimpleDoubleProperty(estValue);
//	}
//	public void setNotes(String notes) {
//		this.notes = new SimpleStringProperty(notes);
//	}
	
	@Override
	public String toString() {
		String s = "Brand: "+brand.get()+", Model: "+model.get()+", Serial Number: "+serialNum.get()+", Caliber: "+caliber.get()+", EstValue: "+estValue.get()+", and Notes: "+notes.get();
		return s;
	}
	
}
