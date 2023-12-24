package com.example.javafx;
import com.example.javafx.dao.MessageDaoImpl;
import com.example.javafx.dao.UserDaoImpl;
import com.example.javafx.dao.entities.Message;
import com.example.javafx.dao.entities.User;
import com.example.javafx.service.*;
import com.example.javafx.util.VoiceRecorder;
import com.example.javafx.util.VoiceUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.javafx.UserSession;

import javax.swing.text.html.ImageView;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.example.javafx.MessageType.CONNECTED;


public class Controller implements Initializable {
    IMessageService service = new IServiceMessageImpl(new MessageDaoImpl());
    IUserService userService = new IserviceUserImpl(new UserDaoImpl());
    PrintWriter pw;
    @FXML
    private Circle imageUser;
    @FXML
    private  TextField search;
    ObservableList<String> listModal = FXCollections.observableArrayList();
    @FXML
    private ListView<String> listView ;
    private final String HASCONNECTED = "has connected";
    private FontAwesomeIcon microphoneActiveIcon = new FontAwesomeIcon();
    private FontAwesomeIcon microphoneInactiveIcon = new FontAwesomeIcon();
        /*
*      microphoneActiveIcon.setGlyphName("SEND");
     microphoneInactiveIcon.setGlyphName("MICROPHONE");*/
    @FXML
    private FontAwesomeIcon iconEnvoyer;
    @FXML
    private FontAwesomeIcon attachFile;
    @FXML
    private TextField textfield;
    @FXML
    private VBox leftVBox;
    private static User user;
    private IMessageService messageService;
    private UserSession userSession;
    private ObservableList<String> messages = FXCollections.observableArrayList();

    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Socket socket = new Socket("localhost", 1234);
            InputStream inputStream = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            listView.setItems(messages);
            pw = new PrintWriter(socket.getOutputStream(), true);
            new Thread( ()->{
                try {
                    while (true) {
                        String response = bufferedReader.readLine();
                        Platform.runLater(() -> {
                            listModal.add(response);
                        });

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Controller() {
        this.userSession = UserSession.getInstace(user);
        this.messageService= new IServiceMessageImpl(new MessageDaoImpl());
        // Initialization code, if needed
    }
    public Controller(IMessageService messageService,UserSession userSession) {
        this.messageService = messageService;
        this.userSession = UserSession.getInstace(user);
        System.out.println(this.userSession);
    }
    private byte[] readFileData(File file) {
        try {
            Path filePath = file.toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendMessage(MouseEvent event) {
       String content=textfield.getText();
        sendMessage(event, content, null); // Call the version with default values
    }


    void loadMessages() {
        // Clear existing messages
        messages.clear();
        // Load messages from the database
        List<Message> messageList = messageService.getMessagesForCurrentUserAndContact(
                userSession.getCurrentUser().getUser_id(),
                UserSession.getCurrentContact()
        );

        for (Message message : messageList) {
            messages.add(message.getContent());
        }
    }
    @FXML
    void DroppedList(MouseEvent event) {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem1 = new MenuItem("Profile");

        menuItem1.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
                VBox modalRoot = loader.load();
                addContactController addContact= loader.getController();
                addContact.setUser(this.user);

                Stage modalStage = new Stage();
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.setTitle("Profile");
                modalStage.setScene(new Scene(modalRoot));
                modalStage.showAndWait();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });

            MenuItem menuItem2 = new MenuItem("Add contact");

            MenuItem menuItem3 = new MenuItem("Others");

            contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3);
               menuItem2.setOnAction(e -> {
                   try {
                       FXMLLoader loader = new FXMLLoader(getClass().getResource("addContact.fxml"));
                       VBox modalRoot = loader.load();
                       addContactController addContact= loader.getController();
                       addContact.setUser(this.user);

                       Stage modalStage = new Stage();
                       modalStage.initModality(Modality.APPLICATION_MODAL);
                       modalStage.setTitle("Ajouter un contact");
                       modalStage.setScene(new Scene(modalRoot));
                       modalStage.showAndWait();
                   } catch (IOException e1) {
                       e1.printStackTrace();
                   }

         });

            contextMenu.show(imageUser, event.getScreenX(), event.getScreenY());
        }




    public void ChangeIcon(KeyEvent keyEvent) {
        iconEnvoyer.setGlyphName("SEND");

    }


    public void getMessages(String idReciever, String idSender){
        List<Message> messages = service.getMessageByUserId(idReciever, idSender);
        for(Message msg:messages){
            listModal.add(msg.getContent());
        }
    }

    public void initData(User user) {
        this.user = user;
        fillCircle(user, imageUser);

        List<User> users = new ArrayList<>();
        String contact_ids[] = user.getContacts();

        if (contact_ids != null) {
            for (String id : contact_ids) {
                User contact = userService.getUserById(id);
                if (contact != null) {
                    users.add(contact);
                } else {
                    System.out.println("User with ID " + id + " not found.");
                }
            }

            for (User contact : users) {
                HBox hbox = createHBox(contact);
                leftVBox.getChildren().add(hbox);  // Add HBox to VBox
            }
        }

        System.out.println("Exiting initData");
    }

    private HBox createHBox(User userHbox) {
        Circle circle = new Circle(23.0, Color.WHITE);
        circle.setStroke(Color.BLACK);

        fillCircle(userHbox, circle);

        Label nameLabel = new Label(userHbox.getNom());
        Label emailLabel = new Label(userHbox.getEmail());

        VBox innerVBox = new VBox(nameLabel, emailLabel);
        innerVBox.setSpacing(5.0); // Adjust the spacing according to your preference

        HBox hbox = new HBox(circle, innerVBox);
        hbox.setOnMouseClicked( e -> {
            getMessages(user.getUser_id(), userHbox.getUser_id());
            listView.getItems().setAll(listModal);
        });
        hbox.setSpacing(10.0);
        hbox.getStyleClass().add("label_style");
        hbox.setId(userHbox.getUser_id());
        hbox.setOnMouseClicked(this::handleContactClick);
        return hbox;
    }
    private void handleContactClick(MouseEvent event) {
        if (event.getSource() instanceof HBox) {
            HBox clickedHBox = (HBox) event.getSource();
            String contactId = clickedHBox.getId();
            System.out.println(clickedHBox.getId());
            UserSession.setCurrentContact(contactId.toString());
            loadMessages();
        }
    }

    @FXML
    void Rechercher(KeyEvent event) {
        String query = search.getText().toLowerCase();

        List<User> filteredUsers = userService.searchUserByQuery(query);

    }


    public void fillCircle(User user, Circle circle) {

            byte[] imageData = user.getImageData();
            if (imageData != null) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
                Image image = new Image(inputStream);
                circle.setFill(new ImagePattern(image));
            } else {
                InputStream defaultImageStream = getClass().getResourceAsStream("/com/example/img/user1.jpeg");
                Image defaultImage = new Image(defaultImageStream);
                circle.setFill(new ImagePattern(defaultImage));
            }
    }

    @FXML
    public void handleFile(MouseEvent mouseEvent) {
        // Open the file chooser dialog
        Stage stage = (Stage) attachFile.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        // Show the dialog and get the selected file
        File selectedFile = fileChooser.showOpenDialog(stage);
        // Process the selected file (you can handle this according to your needs)
        if (selectedFile != null) {
            // Process the selected file, e.g., set it in a text field or handle its content
            textfield.setText(selectedFile.getAbsolutePath());
            sendMessage(mouseEvent,null, selectedFile);
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
    }
    public void sendMessage(MouseEvent event, String content, File attachedFile) {
        //String content = textfield.getText();
        String messageContent = (content != null) ? content : "";
        loadMessages();
        if (!messageContent.isEmpty()) {
            String senderId = this.userSession.getCurrentUser().getUser_id();
            String receiverId = UserSession.getCurrentContact();
            Message message = new Message(content, senderId, receiverId, false, new Date());
            if (content instanceof String) {
                messageService.addMessage(message);
                textfield.clear();
                loadMessages();
            }else {
                if (attachedFile != null) {
                    // Set the attached file and its name
                    message.setAttachedFile(attachedFile);
                    message.setFileName(attachedFile.getName());
                }
                messageService.addMessage(message);
                textfield.clear();
                loadMessages();
            }

        }
    }
    public void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setSender(user.getUser_id());
        createMessage.setType(MessageType.USER);
        createMessage.setStatus(Status.AWAY);
        createMessage.setContent(msg);
        createMessage.setPicture(user.getImage());
        oos.writeObject(createMessage);
        oos.flush();
    }

    public void recordVoiceMessage() throws IOException {
        if (VoiceUtil.isRecording()) {
            Platform.runLater(() -> {
                        iconEnvoyer.setGlyphName(microphoneInactiveIcon.toString());
                    }
            );
            VoiceUtil.setRecording(false);
        } else {
            Platform.runLater(() -> {
                        iconEnvoyer.setGlyphName(microphoneActiveIcon.toString());

                    }
            );
            VoiceRecorder.captureAudio();
        }
    }
    public static void sendVoiceMessage(byte[] audio) throws IOException {
        Message createMessage = new Message();
        createMessage.setSender(user.getUser_id());
        createMessage.setType(MessageType.VOICE);
        createMessage.setStatus(Status.AWAY);
        createMessage.setVoiceMsg(audio);
        createMessage.setPicture(user.getImage());
        oos.writeObject(createMessage);
        oos.flush();
    }


    public void sendStatusUpdate(Status status) throws IOException {
        Message createMessage = new Message();
        createMessage.setSender(user.getUser_id());
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        createMessage.setPicture(user.getImage());
        oos.writeObject(createMessage);
        oos.flush();
    }

    public void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setSender(user.getNom());
        createMessage.setType(CONNECTED);
        createMessage.setContent(HASCONNECTED);
        createMessage.setPicture(user.getImage());
        oos.writeObject(createMessage);
    }

    public void logoutScene() {
        UserSession.cleanUserSession();
        Platform.runLater(() -> {
            FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/javafx/Login.fxml"));
            Parent window = null;
            try {
                window = (Pane) fmxlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = Application.getPrimaryStage();
            Scene scene = new Scene(window);
            stage.setMaxWidth(350);
            stage.setMaxHeight(420);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }
}