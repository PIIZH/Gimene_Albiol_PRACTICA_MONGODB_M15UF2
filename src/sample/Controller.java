package sample;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import org.bson.Document;

import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable{

    @FXML RadioButton rbRead;
    @FXML RadioButton rbDelete;
    @FXML RadioButton rbInsert;
    @FXML RadioButton rbEdit;
    public int currentRadioButton = 0;
    private final static String HOST = "localhost";
    private  final static int PORT = 27017;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            MongoClient mongoClient = new MongoClient(HOST,PORT);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("dam");
            MongoCollection<Document> collUsers = mongoDatabase.getCollection("usuaris");
            Document doc;
        }catch(Exception e){
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
    @FXML public  void rbReadDataOnClick(){
        rbDelete.setSelected(false);
        rbInsert.setSelected(false);
        rbEdit.setSelected(false);
        currentRadioButton = 0;
}

    @FXML public  void rbDeleteDataOnClick(){
        rbRead.setSelected(false);
        rbInsert.setSelected(false);
        rbEdit.setSelected(false);
        currentRadioButton = 1;
    }

    @FXML public  void rbInsertDataOnClick(){
        rbRead.setSelected(false);
        rbDelete.setSelected(false);
        rbEdit.setSelected(false);
        currentRadioButton = 2;
    }

    @FXML public  void rbEditDataOnClick(){
        rbRead.setSelected(false);
        rbDelete.setSelected(false);
        rbInsert.setSelected(false);
        currentRadioButton = 3;
    }

    @FXML public void btnDoItOnClick(){
        switch (currentRadioButton){
            case 0:
                System.out.println("I am reading data");
                break;

            case 1:
                System.out.println("I am deleting data");
                break;
            case 2:
                System.out.println("I am inserting data");
                break;
            case 3:
                System.out.println("I am editing data");
                break;
        }
    }


}

