package resources;
/*
 * Author: Conner Cox
 * Date: June 19, 2019
 * 
 * Description: This is the firearm class that is used in GunApplicationWindow.java. 
 * It defines the attributes of a firearm object.
 */
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

@SuppressWarnings("serial")
public class Firearm implements Serializable {
	//attributes of a Firearm
	private byte[] imgArray;
	private String brand;
	private String model;
	private String serialNum;
	private String caliber;
	private double estValue;
	private String notes;
	
	//zero arg constructor
	public Firearm() {
		imgArray = null;
		brand = null;
		model = null;
		serialNum = null;
		caliber = null;
		estValue = 0;
		notes = "";
	}
	
	//constructor
	public Firearm(Image image, String brand, String model, String serialNum, String caliber, double estValue, String notes) {
		
		if (image == null) {
			image = new Image("missingIcon.png");
		} else {
			try {
				BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bImage, "jpg", baos);
				baos.flush();
				imgArray = baos.toByteArray();
				baos.close();
			} catch (Exception e) {
				System.out.println("Error writing image to byte array in Firearm Class");
				e.printStackTrace();
			}
		}
		this.brand = brand;
		this.model = model;
		this.serialNum = serialNum;
		this.caliber = caliber;
		this.estValue = estValue;
		this.notes = notes;
	}
	
	//getters
	public Image getImage() {
		Image img = new Image(new ByteArrayInputStream(imgArray));
		return img;
	}
	public String getBrand() {
		return brand;
	}
	public String getModel() {
		return model;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public String getCaliber() {
		return caliber;
	}
	public double getEstValue() {
		return estValue;
	}
	public String getNotes() {
		return notes;
	}
	public byte[] getImgArray() {
		return imgArray;
	}

	//setters
	public void setImage(Image image) {
		try {
			BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bImage, "jpg", baos);
			baos.flush();
			imgArray = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			System.out.println("Error setting image in Firearm Class");
			e.printStackTrace();
		}
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public void setCaliber(String caliber) {
		this.caliber = caliber;
	}
	public void setEstValue(double estValue) {
		this.estValue = estValue;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public void setImgArray(byte[] imgArray) {
		this.imgArray = imgArray;
	}
	
	@Override
	public String toString() {
		String s = "Image: "+ imgArray.toString() + "Brand: "+brand+", Model: "+model+", Serial Number: "+serialNum+", Caliber: "+caliber+", EstValue: "+estValue+", and Notes: "+notes;
		return s;
	}
	
}
