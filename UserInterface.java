
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scence.layout.*;
import javafx.stage.Stage;
import java.util.Optional;

import javax.swing.text.html.ListView;
class Item{
    private final int id;
        private final String name;
        private final String description;
        public Item(int id,String name,String description){
            this.id=id;
            this.name=name;
            this.description=description;

        }
        public int getId(){return id;}
        public String getName(){return name;}
        public String getDescription(){return description;}
@Override
public String toString(){
    return name+"(ID:"+id+")";
}
}
class ItemService{
    private final ObservableList<Item> items=FXCollections.observableArrayList();
    private int nextId=1;
    public ObservableList<Item> listItems(){
        return items;
    }
    public Item addItem(String name,String description){
        Item newItem=new Item(nextId++,name.trim().description.trim());
        items.add(newItem);
        return newItem;
    }
    public Optional<Item> getItemById(int id){
        return items.stream().filter(i->i.getId()==id).findFirst();
    }
    public boolean removeItem(int id){
        return items.removeIf(i->i.getId()==id);
    }
}
public class UserInterface extends Application{
    private final ItemService itemService=new ItemService();
    private ListView<Item> itemListView;
    private TextField nameField;
    private TextArea descriptionField;
    private Button addButton;
    private Button deleteButton;
    @Override
    public void Start(Stage stage){
        stage.setTitle("Item Manager");
        itemListView=new ListView<>(itemService.listItems());
        Label nameLabel=new Label("Description:");
        nameField=new TextField();
        nameField.setPromptText("Enter item name");
        Label descriptionLabel=new Label("Description:");
        descriptionField=new TextArea();
        descriptionField.setPromptText("Enter item description");
        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(3);
        addButton=new Button("Add Item");
        deleteButton=new Button("Deleet selected Item");
        detailsArea=new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setWrapText(true);
        detailsArea.setPrefHeight(100);
        itemListView.getSelectionModel().selectedItemProperty().addListener((obs,oldsel,newsel)->displayItemDetails(newSel));
        addButton.setOnAction(e->handleAddItem());
        deleteButton.setOnAction(e->handleDeleteItem());
        VBox inputBox=new VBox(5,nameLabel,nameField,descriptionLabel,descriptionField,addButton,deleteButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setPrefWidth(300);
        VBox detailsBox=new VBox(new Label("Select Item Details"),detailsArea);
        detailsBox.setPadding(new Insets(10));
        VBox rightPane=new VBox(inputBox,detailsBox);
        rightPane.setSpacing(10);
        BorderPane root=new BorderPane();
        root.setPadding(new Insets(10));
        root.setLeft(itemListView);
        root.setCenter(rightPane);
        BorderPane.setMargin(itemListView,new Insets(0,10,0,0));
        itemListView.setPrefWidth(250);
        Scene scene=new Scene(root,700,400);
        stage.setScene(scene);
        stage.show();

    }
    private void displayItemDeatils(Item item){
        if(item!=null){
            detailsArea.setText( "ID: " + item.getId() + "\n" +
                "Name: " + item.getName() + "\n" +
                "Description: " + item.getDescription());
                nameField.setText(item.getName());
                descriptionField.setText(item.getDescription());
        }
        else{
            detailsArea.clear();
            nameField.clear();
            descriptionField.clear();
        }
    }
    private void handleAddItem(){
        String name=nameField.getText().trim();
        String description =descriptionField.getText().trim();
        if(name.isEmpty()){
            showAlert(Alert.AlterType.WARNING,"Validation Error","Name cannot be empty");
            return;
        }
        itemService.addItem(name,description);
        itemListView.getSelectionModel().clearSelection();
        nameField.clear();
        descriptionField.clear();

    }
    private void handleDeleteItem(){
        Item selected=itemListView.getSelectionModel().getSelectedItem();
        if(selected==null){
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to delete.");
            return;
        }
        itemService.reoveItem(selected.getId());
        itemListView.getSelectionModel().clearSelection();
    }
    private void showAlert(Alert.AlertType type ,String title,String message){
        Alert alert=new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContectText(message);
        alert.showAndWait();
    }
    public static void main(String [] args){
        launch();
    }
}