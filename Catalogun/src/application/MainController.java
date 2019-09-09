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

import javax.imageio.ImageIO;

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
import resources.SimplePropertyFirearm;

//TODO: clear empty gun from collection once there is another present OR make it so that it does not need an empty gun in first place
//TODO: seperate ADDSORTSTORE into their respective methods
//TODO: Display gun collection onto table
public class MainController implements Initializable {

	//elements that can be changed or read by fxml go here
	@FXML private TextField tfBrand;
	@FXML private TextField tfModel;
	@FXML private TextField tfSerial;
	@FXML private TextField tfCaliber;
	@FXML private TextField tfEstValue;
	@FXML private TextField tfNotes;
	@FXML private ImageView ivPicture;

	@FXML private TableView<SimplePropertyFirearm> gunTable;
	@FXML private TableColumn<SimplePropertyFirearm, Image> tcImage;
	@FXML private TableColumn<SimplePropertyFirearm, String> tcBrand;
	@FXML private TableColumn<SimplePropertyFirearm, String> tcModel;
	@FXML private TableColumn<SimplePropertyFirearm, String> tcCaliber;
	@FXML private TableColumn<SimplePropertyFirearm, String> tcSerialNum;
	@FXML private TableColumn<SimplePropertyFirearm, Double> tcEstValue;
	@FXML private TableColumn<SimplePropertyFirearm, String> tcNotes;
	@FXML private TableColumn<SimplePropertyFirearm, Button> tcDelete;

	//elements that do not need fxml or need persistance go here
	ArrayList<Firearm> gunCollection = new ArrayList<Firearm>();
	ArrayList<SimplePropertyFirearm> gunCollectionSimple = new ArrayList<SimplePropertyFirearm>();
//	ArrayList<SimplePropertyFirearm> gunSortedModel = new ArrayList<SimplePropertyFirearm>(gunCollectionSimple);
//	ArrayList<SimplePropertyFirearm> gunSortedBrand = new ArrayList<SimplePropertyFirearm>(gunCollectionSimple);
//	ArrayList<SimplePropertyFirearm> gunSortedCaliber = new ArrayList<SimplePropertyFirearm>(gunCollectionSimple);
//	ArrayList<SimplePropertyFirearm> gunSortedSerial = new ArrayList<SimplePropertyFirearm>(gunCollectionSimple);
//	ArrayList<SimplePropertyFirearm> gunSortedValue = new ArrayList<SimplePropertyFirearm>(gunCollectionSimple);

	public ObservableList<SimplePropertyFirearm> list = FXCollections.observableArrayList(gunCollectionSimple);

	File gunFile = new File("gunFile.file");


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//file reading in
		try {
			//create a file if none exists
			if(!gunFile.exists()) {
				gunFile.createNewFile();
				//set up empty firearm and store it
				Image img = new Image("/resources/missingIcon.png");
				Firearm emptyFirearm = new Firearm(img, "none", "none", "none", "none", 0, "Empty");
				gunCollection.add(emptyFirearm);
				addSortStoreGuns();
			} else {
				//read from the file, update programs collection with saved collection
				FileInputStream fi = new FileInputStream(gunFile);				
				ObjectInputStream oi = new ObjectInputStream(fi);
				gunCollection = (ArrayList<Firearm>) oi.readObject();
				gunCollection.forEach(e -> System.out.println(e.toString()));
				gunCollection.forEach(e -> gunCollectionSimple.add(new SimplePropertyFirearm(e)));
				addSortStoreGuns();
				oi.close();
				fi.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for input...\n" + e.getMessage() + e.getStackTrace());
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Class not found");
			e.printStackTrace();
		}
		
		//test
		ObservableList<SimplePropertyFirearm> list2 = FXCollections.observableArrayList(
				new SimplePropertyFirearm(new Firearm(new Image("/resources/missingIcon.png"), "a", "a", "a", "a", 2.1, "a")), 
				new SimplePropertyFirearm(new Firearm(new Image("/resources/missingIcon.png"), "a", "a", "h", "a", 3.4, "a"))
);

		//TODO: populate table if firearms exist
		//TODO: the error here may be that my getters and setters are misnamed or that I have too many columns for my data
		tcImage.setCellValueFactory(new PropertyValueFactory<SimplePropertyFirearm, Image>("tcImage"));
		tcBrand.setCellValueFactory(new PropertyValueFactory<SimplePropertyFirearm, String>("tcBrand"));
		tcModel.setCellValueFactory(new PropertyValueFactory<SimplePropertyFirearm, String>("tcModel"));
		tcCaliber.setCellValueFactory(new PropertyValueFactory<SimplePropertyFirearm, String>("tcCaliber"));
		tcSerialNum.setCellValueFactory(new PropertyValueFactory<SimplePropertyFirearm, String>("tcSerialNum"));
		tcEstValue.setCellValueFactory(new PropertyValueFactory<SimplePropertyFirearm, Double>("tcEstValue"));
		tcNotes.setCellValueFactory(new PropertyValueFactory<SimplePropertyFirearm, String>("tcNotes"));
		tcDelete.setCellValueFactory(new PropertyValueFactory<SimplePropertyFirearm, Button>("tcDelete"));
		SimplePropertyFirearm test = new SimplePropertyFirearm(new Firearm(new Image("/resources/missingIcon.png"), "a", "a", "a", "a", 2.1, "a"));
		gunTable.getItems().add(test);

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
		//TODO: Make sure there is info available to put into gun, else do nothing

		if(tfBrand.getText().isEmpty() || tfModel.getText().isEmpty() || tfSerial.getText().isEmpty() || tfCaliber.getText().isEmpty() || tfEstValue.getText().isEmpty()) {
			//TODO: prompt user to renter info
		} else {
			//Take everything from the textfields
			String brand = tfBrand.getText().trim();
			String model = tfModel.getText().trim();
			String serial = tfSerial.getText().trim();
			String caliber = tfCaliber.getText().trim();
			String toParse = tfEstValue.getText().replace('$', ' ').trim();
			double estValue = Double.parseDouble(toParse);
			String notes = tfNotes.getText().trim();
			Image image = ivPicture.getImage();
			
			//add gun to record
			Firearm gunToAdd = new Firearm(image, brand, model, serial, caliber, estValue, notes);
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
	}

	/**
	 * Add and then sort any guns added to the collection
	 */
	public void addSortStoreGuns() {
		//TODO: Figure out if sorting in table works, make sure both collections of guns update simultaniously
		//maybe use different observiable lists instead of arraylists
		
//		//ADD-update each sorted list with the complete list of guns
//		gunSortedModel = new ArrayList<Firearm>(gunCollection);
//		gunSortedBrand = new ArrayList<Firearm>(gunCollection);
//		gunSortedCaliber = new ArrayList<Firearm>(gunCollection);
//		gunSortedSerial = new ArrayList<Firearm>(gunCollection);
//		gunSortedValue = new ArrayList<Firearm>(gunCollection);
//
//		//SORT-sort each list with its respective comparator
//		gunSortedModel.sort(new FirearmComparatorByModel());
//		gunSortedBrand.sort(new FirearmComparatorByBrand());
//		gunSortedCaliber.sort(new FirearmComparatorByCaliber());
//		gunSortedSerial.sort(new FirearmComparatorBySerial());
//		gunSortedValue.sort(new FirearmComparatorByValue());

		//STORE-store into file
		try {
			FileOutputStream f = new FileOutputStream(gunFile);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(gunCollection);
			o.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("ADDSORTSTORE Error initializing stream\n"+e.getMessage());
			e.printStackTrace();
		}
		
		//Update table
	}
}