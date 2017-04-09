package sample;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.bson.Document;
import java.net.URL;
import java.util.ResourceBundle;


public class MongoViewController implements Initializable {

    private final static String HOST = "localhost";
    private final static int PORT = 27017;
    MongoCollection<Document> collUsers;
    DBCollection collectionUpdate;
    Document doc;

    @FXML
    RadioButton rbRead;
    @FXML
    RadioButton rbDelete;
    @FXML
    RadioButton rbInsert;
    @FXML
    RadioButton rbEdit;
    @FXML
    TextField txtfUserInteraction;
    @FXML
    TextArea txtaResult;
    @FXML
    TextField txtHobbies;
    @FXML
    CheckBox chckStatus;

    @FXML
    Label lblCheckQuery;

    public int currentRadioButton = 0;
    boolean changeTextBoxName = true;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            MongoClient mongoClient = new MongoClient(HOST, PORT);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("dam");
            collUsers = mongoDatabase.getCollection("usuaris");
            DB db = mongoClient.getDB("DB");
            collectionUpdate = db.getCollection("usuaris");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() +": " + e.getMessage());
        }
    }

    @FXML
    public void chckChangeStatus(){
        if (changeTextBoxName){
            chckStatus.setText("Hobby");
            changeTextBoxName=false;
        }else{
            chckStatus.setText("Name");
            changeTextBoxName=true;
        }
    }

    @FXML
    public void rbReadDataOnClick() {
        lblCheckQuery.setVisible(false);
        txtHobbies.setVisible(false);
        rbDelete.setSelected(false);
        rbInsert.setSelected(false);
        rbEdit.setSelected(false);
        currentRadioButton = 0;
        txtfUserInteraction.setPromptText("Read data");
        txtaResult.setLayoutY(80.0);
    }

    @FXML
    public void rbDeleteDataOnClick() {
        txtHobbies.setVisible(false);
        rbRead.setSelected(false);
        rbInsert.setSelected(false);
        rbEdit.setSelected(false);
        lblCheckQuery.setVisible(false);
        currentRadioButton = 1;
        txtfUserInteraction.setPromptText("Delete data");
        txtaResult.setLayoutY(80.0);
    }

    @FXML
    public void rbInsertDataOnClick() {
        lblCheckQuery.setVisible(false);
        rbRead.setSelected(false);
        rbDelete.setSelected(false);
        rbEdit.setSelected(false);
        currentRadioButton = 2;
        txtHobbies.setPromptText("Hobbies");
        txtHobbies.setVisible(true);
        txtfUserInteraction.setPromptText("Name");
        txtaResult.setLayoutY(121.0);
    }

    @FXML
    public void rbEditDataOnClick() {
        lblCheckQuery.setVisible(false);
        txtHobbies.setVisible(true);
        txtHobbies.setPromptText("Old");
        rbRead.setSelected(false);
        rbDelete.setSelected(false);
        rbInsert.setSelected(false);
        currentRadioButton = 3;
        txtfUserInteraction.setPromptText("New");
        txtaResult.setLayoutY(121.0);
    }

    @FXML
    public void btnClearScreen(){
        txtaResult.setText("");
    }

    @FXML
    public void btnDoItOnClick() {
        lblCheckQuery.setText("Inserted the values!");
        switch (currentRadioButton) {
            case 0:
                readData(txtfUserInteraction.getText());
                break;

            case 1:
                removeData(txtfUserInteraction.getText());
                break;
            case 2:
                insertData(txtfUserInteraction.getText(),txtHobbies.getText());
                break;
            case 3:
                updateData(txtfUserInteraction.getText(),txtHobbies.getText());
                break;
        }
    }


    private void readData(String check) {
        MongoCursor<Document> mongoCursor;

            switch(chckStatus.getText()){
                case "Name":
                    try {
                        if (check.equals("")) {
                            mongoCursor = collUsers.find().iterator();
                        }else {
                            BasicDBObject search = new BasicDBObject();
                            search.put("name", check);
                            mongoCursor = collUsers.find(search).iterator();
                        }
                        while (mongoCursor.hasNext()) {
                            doc = mongoCursor.next();
                            txtaResult.appendText("Name: " + doc.getString("name") + "\nHobbies: " + doc.get("hobbies")+"\n____________________________\n");
                        }
                    }catch (Exception e) {
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    }

                    break;

                case "Hobby":
                    try {
                        if (check.equals("")) {
                            mongoCursor = collUsers.find().iterator();
                        }else {
                            BasicDBObject search = new BasicDBObject();
                            search.put("hobbies", check);
                            mongoCursor = collUsers.find(search).iterator();
                        }
                        while (mongoCursor.hasNext()) {
                            doc = mongoCursor.next();
                            System.out.println(doc.getString("name"));
                            txtaResult.appendText("Hobbies: " + doc.get("hobbies")+"\nName:"+doc.getString("name")+"\n____________________________\n");
                        }
                    }catch (Exception e) {
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    }
                    break;
            }

    }

    private void insertData(String name, String hobby){
        String[] finalHobby = hobby.split("[^a-zA-Z0-9-\\s']+");
        doc = new Document("name", name);

        for (String littlHobby:finalHobby){
            doc.append("hobbies", littlHobby);
        }

        try{
            collUsers.insertOne(doc);
            lblCheckQuery.setVisible(true);
        }catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " +e.getMessage());
        }
    }

    private void removeData(String deleteRow){
        switch(chckStatus.getText()){
            case "Name":
                collUsers.deleteOne(new Document("name",deleteRow));
                break;

            case "Hobby":
                collUsers.deleteOne(new Document("hobbies",deleteRow));
                break;
        }
        lblCheckQuery.setText("Deleted all the values");
        lblCheckQuery.setVisible(true);
    }

    private void updateData(String newRow,String oldRow){
        BasicDBObject oldFile = new BasicDBObject();
        oldFile.append("name",oldRow);
        BasicDBObject newFile = new BasicDBObject();
        newFile.append("$set", newRow);
        collectionUpdate.findAndModify(oldFile,newFile);
        lblCheckQuery.setText("Updated value");
        lblCheckQuery.setVisible(true);
    }

}

