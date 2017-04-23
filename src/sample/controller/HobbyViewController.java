package sample.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import org.bson.Document;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class HobbyViewController implements Initializable{

    MongoCollection<Document> collUsers;
    DBCollection collectionUpdate;
    Document doc;
    ArrayList<String> hobbiesOrder = new ArrayList<>();
    @FXML RadioButton rbAsc;

    @FXML RadioButton rbDesc;

    @FXML RadioButton rbAlph;

    @FXML RadioButton rbRand;

    @FXML ListView lvContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            MongoClient mongoClient = new MongoClient(MongoViewController.HOST, MongoViewController.PORT);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("dam");
            collUsers = mongoDatabase.getCollection("usuaris");
            DB db = mongoClient.getDB("dam");
            collectionUpdate = db.getCollection("usuaris");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() +": " + e.getMessage());
        }
    }

    //MainButton
    @FXML public void btnSeracHoobbies() {
        btnClearScreen();

        MongoCursor<Document> mongoCursor = collUsers.find().iterator();
        addToArryList(mongoCursor);
        if(rbAsc.isSelected()){
            ascendant();
        }
        else if(rbDesc.isSelected()){
            desccendant();
        }
        else if(rbRand.isSelected()){
            random();
        }
        else if(rbAlph.isSelected()){
            alphabetical();
        }
    }
    //ArrayList
    public void addToArryList(MongoCursor<Document> mongoCursor){
        try {
            while (mongoCursor.hasNext()) {
                doc = mongoCursor.next();
                hobbiesOrder.add(doc.getString("name"));
            }}catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    public void ascendant(){
        for (String x: hobbiesOrder) {
            lvContainer.getItems().add(x);

        }

    }
    public void desccendant(){
       Collections.reverse(hobbiesOrder);
       ascendant();
       Collections.reverse(hobbiesOrder);
    }
    public void random(){
        ArrayList<String > randomArray = hobbiesOrder;
        Collections.shuffle(randomArray);
        for(String x: randomArray){
            lvContainer.getItems().add(x);
        }
    }
    public void alphabetical(){
        List<String> alph = hobbiesOrder;

        Collections.sort(alph);

        for (String x:alph) {
            lvContainer.getItems().add(x);
        }
    }
    //RadioButtons
    @FXML public void rbOnActionAscd(){
        changeRadioButtons(rbAsc);
    }
    @FXML public void rbOnActionDesc(){
        changeRadioButtons(rbDesc);
    }
    @FXML public void rbOnActionRand(){
        changeRadioButtons(rbRand);
    }
    @FXML public void rbOnActionAlph(){
        changeRadioButtons(rbAlph);
    }
    public void changeRadioButtons(RadioButton x){
        RadioButton[] rb = {rbAlph,rbAsc,rbRand,rbDesc};
        for (RadioButton r:rb) {
            if(r.equals(x)) r.setSelected(true);
            else r.setSelected(false);
        }
    }
    //Graphic
    @FXML public void btnMongoViewOnCLick() throws IOException {
        Stage stage = (Stage) rbAsc.getScene().getWindow();
        stage.setTitle("MongoDB");
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("../view/mongoView.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }
    @FXML public void btnClearScreen(){
        hobbiesOrder.clear();
        lvContainer.getItems().clear();
    }

}
