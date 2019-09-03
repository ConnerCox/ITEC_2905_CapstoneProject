/*
 * Author: Conner Cox
 * Date: August 26, 2019
 * 
 * Version: 1.15 (halfway though first draft of version 2)
 * 
 * Description: This application allows a user to add the information and image of a firearm, 
 * and then it stores and sorts it. This allows for easy record keeping of firearms.
 */
package application;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import resources.Firearm;
import resources.FirearmComparatorByBrand;
import resources.FirearmComparatorByCaliber;
import resources.FirearmComparatorByModel;
import resources.FirearmComparatorBySerial;
import resources.FirearmComparatorByValue;

public class MainController implements Initializable {

	//elements that can be changed or read by fxml go here
	@FXML private TextField tfBrand;
	@FXML private TextField tfModel;
	@FXML private TextField tfSerial;
	@FXML private TextField tfCaliber;
	@FXML private TextField tfEstValue;
	@FXML private TextField tfNotes;
	@FXML private ImageView ivPicture;

	@FXML private TableView<Firearm> gunTable;
	@FXML private TableColumn<Firearm, Image> tcImage;
	@FXML private TableColumn<Firearm, String> tcBrand;
	@FXML private TableColumn<Firearm, String> tcModel;
	@FXML private TableColumn<Firearm, String> tcCaliber;
	@FXML private TableColumn<Firearm, String> tcSerialNum;
	@FXML private TableColumn<Firearm, Double> tcEstValue;
	@FXML private TableColumn<Firearm, String> tcNotes;
	@FXML private TableColumn<Firearm, Button> tcDelete;

	//elements that do not need fxml or need persistance go here
	ArrayList<Firearm> gunCollection = new ArrayList<Firearm>();
	ArrayList<Firearm> gunSortedModel = new ArrayList<Firearm>(gunCollection);
	ArrayList<Firearm> gunSortedBrand = new ArrayList<Firearm>(gunCollection);
	ArrayList<Firearm> gunSortedCaliber = new ArrayList<Firearm>(gunCollection);
	ArrayList<Firearm> gunSortedSerial = new ArrayList<Firearm>(gunCollection);
	ArrayList<Firearm> gunSortedValue = new ArrayList<Firearm>(gunCollection);

	public ObservableList<Firearm> list = FXCollections.observableArrayList(gunCollection);

	File gunFile = new File("gunFile.file");


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//file reading in
		try {
			//create a file if none exists
			if(!gunFile.exists()) {
				//TODO test
				System.out.println("Didnt exist, made file at: " + gunFile.getPath());
				gunFile.createNewFile();
			} else {
				//TODO test
				System.out.println("Did exist, read from file");
				FileInputStream fi = new FileInputStream(gunFile);
				ObjectInputStream oi = new ObjectInputStream(fi);
				if(oi.available() > 0) {
					gunCollection = (ArrayList<Firearm>) oi.readObject();
				}
				addSortStoreGuns();
				oi.close();
				fi.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error initializing stream for input...\n" + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found");
			e.printStackTrace();
		}

//		//populate table if firearms exist
//		tcImage.setCellValueFactory(new PropertyValueFactory<Firearm, Image>("tcImage"));
//		tcBrand.setCellValueFactory(new PropertyValueFactory<Firearm, String>("tcBrand"));
//		tcModel.setCellValueFactory(new PropertyValueFactory<Firearm, String>("tcModel"));
//		tcCaliber.setCellValueFactory(new PropertyValueFactory<Firearm, String>("tcCaliber"));
//		tcSerialNum.setCellValueFactory(new PropertyValueFactory<Firearm, String>("tcSerialNum"));
//		tcEstValue.setCellValueFactory(new PropertyValueFactory<Firearm, Double>("tcEstValue"));
//		tcNotes.setCellValueFactory(new PropertyValueFactory<Firearm, String>("tcNotes"));
//		tcDelete.setCellValueFactory(new PropertyValueFactory<Firearm, Button>("tcDelete"));
//		gunTable.setItems(list);

	}

	/**
	 * Choose a firearm image
	 */
	public void selectImage(ActionEvent Event) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(
				new ExtensionFilter("PDF and JPG Files", "*.pdf", "*.jpeg", "*.jpg"));
		File selectedFile = fc.showOpenDialog(null);

		if(selectedFile != null) {
			Image img  = new Image(selectedFile.getPath());
			ivPicture.setImage(img);
		} else {
			System.out.println("File is not valid");
		}
	}

	/**
	 * Adds a new gun to the collection.
	 */
	public void enterGun(ActionEvent Event) {
		//TODO :if checking to make sure gun is properly entered
		//TODO: Make sure there is info availible to put into gun, else do nothing

		//Take everything from the textfields
		String brand = tfBrand.getText().trim();
		String model = tfModel.getText().trim();
		String serial = tfSerial.getText().trim();
		String caliber = tfCaliber.getText().trim();
		String toParse = tfEstValue.getText().replace('$', ' ').trim();
		double estValue = Double.parseDouble(toParse);
		String notes = tfNotes.getText().trim();
		Image image = ivPicture.getImage();
		BufferedImage bImg = SwingFXUtils.fromFXImage(image, null);

		//add gun to record
		Firearm gunToAdd = new Firearm(bImg, brand, model, serial, caliber, estValue, notes);
		gunCollection.add(gunToAdd);

		//update records
		addSortStoreGuns();

		//clear text fields
		tfBrand.setText("");
		tfModel.setText("");
		tfSerial.setText("");
		tfCaliber.setText("");
		tfEstValue.setText("");
		tfNotes.setText("");

		tfBrand.requestFocus();
	}

	/**
	 * Add and then sort any guns added to the collection
	 */
	public void addSortStoreGuns() {		
		//ADD-update each sorted list with the complete list of guns
		gunSortedModel = new ArrayList<Firearm>(gunCollection);
		gunSortedBrand = new ArrayList<Firearm>(gunCollection);
		gunSortedCaliber = new ArrayList<Firearm>(gunCollection);
		gunSortedSerial = new ArrayList<Firearm>(gunCollection);
		gunSortedValue = new ArrayList<Firearm>(gunCollection);

		//SORT-sort each list with its respective comparator
		gunSortedModel.sort(new FirearmComparatorByModel());
		gunSortedBrand.sort(new FirearmComparatorByBrand());
		gunSortedCaliber.sort(new FirearmComparatorByCaliber());
		gunSortedSerial.sort(new FirearmComparatorBySerial());
		gunSortedValue.sort(new FirearmComparatorByValue());

		//STORE-store into file
		try {
			FileOutputStream f = new FileOutputStream(gunFile);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(gunCollection);
			o.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println(e.getStackTrace()+"\nError initializing stream\n"+e.getMessage());
		}
		//TODO test
		System.out.println("finished addsortstore");
	}
}