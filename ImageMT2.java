import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ImageMT2 extends Application {

    Stage window;
    Scene scene1, scene2;
    Button uploadbutton, downloadbutton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        GridPane gridPane = new GridPane();
        gridPane.setHgap(25);
        gridPane.setVgap(25);
        //Label  setting for the output window
        Label label = new Label("Welcome to Image Management Tool");
        //uploadbutton setting that facilitates image upload
        uploadbutton = new Button("Upload Image");
        uploadbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                final FileChooser fileChooser = new FileChooser();
                FileInputStream input;
                Image image;
                ImageView imageView;
                //filelist is used to upload more than one image
                List<File> fileList = fileChooser.showOpenMultipleDialog(window);
                if (fileList != null) {
                    int indexX = 0;
                    int indexY = 0;
                    //to iterate through the number of images to be uploaded
                    for (int i = 0; i < fileList.size(); i++) {
                        File file = fileList.get(i);
                        indexX = i % 5;
                        indexY = i / 5;
                        try {
                            input = new FileInputStream(file);
                            image = new Image(input);
                            imageView = new ImageView(image);
                            imageView.setFitWidth(100);
                            imageView.setFitHeight(100);
                            gridPane.add(imageView, indexX, indexY);
                            System.out.println(file.getPath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //downloadbutton setting that facilitates image download
                downloadbutton = new Button("Download Images");
                downloadbutton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        for (int i = 0; i < fileList.size(); i++) {
                            File file = fileList.get(i);
                            try {
                                Download(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                });
                VBox layout2 = new VBox(20);
                //Layout2 - children are laid out in vertical column
                layout2.getChildren().addAll(gridPane,downloadbutton);
                Scene scene2 = new Scene(layout2, 600, 500);
                window.setScene(scene2);
                window.show();
            }
        });
        //Layout1 - children are laid out in vertical column
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label, uploadbutton);
        scene1 = new Scene(layout1, 640, 480);
        //window for the first scene which included the label and upload button
        window.setScene(scene1);
        window.setTitle("Image Management Tool");
        window.show();
    }

    //download function
    private void Download(File file) throws IOException {
        FileChooser fileChooser = new FileChooser();
        //filtering based on image format extensions
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG","*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        //Set name of the image file which is being downloaded to default file name
        fileChooser.setInitialFileName(String.valueOf(file));
        BufferedImage image1 = ImageIO.read(file);
        //showSaveDialog pos up the save file dialog
        File fileToBeSaved = fileChooser.showSaveDialog(window);
        if (fileToBeSaved != null) {
            try {
                ImageIO.write(image1, "jpg", fileToBeSaved);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
    }
}
