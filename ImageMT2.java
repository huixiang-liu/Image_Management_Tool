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
import org.im4java.core.*;
import org.im4java.process.ProcessStarter;

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
    Button uploadButton, downloadButton;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;




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
        Label label = new Label("Welcome to IM4 practice program");

        //uploadButton setting that facilitates image upload
        uploadButton = new Button("Upload Image");
        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
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
                            IMOperation op = new IMOperation();
                            gridPane.add(imageView, indexX, indexY);
                            System.out.println(file.getPath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //downloadbutton setting that facilitates image download
                downloadButton = new Button("Download Images");
                downloadButton.setOnAction(new EventHandler<ActionEvent>() {
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
                layout2.getChildren().addAll(gridPane,downloadButton);
                Scene scene2 = new Scene(layout2, 600, 500);
                window.setScene(scene2);
                window.show();
            }
        });
        //Layout1 - children are laid out in vertical column
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label, uploadButton);
        scene1 = new Scene(layout1, 640, 480);
        //window for the first scene which included the label and upload button
        window.setScene(scene1);
        window.setTitle("Image Management Tool");
        window.show();
    }

    //download function
    private void Download(File file) throws IOException {
        //Set library environment
        String myPath="C:\\Users\\forhj\\Desktop\\ImageMagick-7.0.8-25-portable-Q16-x64";
        ProcessStarter.setGlobalSearchPath(myPath);

        //select the destination
        FileChooser fileChooser = new FileChooser();
        //only convert jpg and png file
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG","*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        fileChooser.setInitialFileName(String.valueOf(file));
        File fileToBeSaved = fileChooser.showSaveDialog(window);
        String destination = fileToBeSaved.toString();
        String extention = destination.substring(destination.lastIndexOf("."));

        //test the output path
        System.out.println("The output file is " + destination);
        System.out.println("The extension is " + extention);

        String sourcePath = file.toString();

        // print the path of the image
        System.out.println("The destination path is " + destination);

        //Create ImageStick command instance for entire program
        ConvertCmd cmd = new ConvertCmd();

        //Create the ImageStick operation instance for entire program
        IMOperation op = new IMOperation();

        // Get the width and Height of the image
        int width = DEFAULT_WIDTH;
        int height = DEFAULT_HEIGHT;
        Info imageInfo;
        try {
            imageInfo= new Info(sourcePath, false); // set the second parameter to "false" to get complete info of the image
            width = imageInfo.getImageWidth();
            height = imageInfo.getImageHeight();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        op.addImage(sourcePath);
        op.resize(width, height);
        op.addImage(destination);

        try {
            cmd.run(op);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        //The following code are Soumya's original code
//        FileChooser fileChooser = new FileChooser();
//        //filtering based on image format extensions
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.*"),
//                new FileChooser.ExtensionFilter("JPG","*.jpg"),
//                new FileChooser.ExtensionFilter("PNG", "*.png"));
//        //Set name of the image file which is being downloaded to default file name
//        fileChooser.setInitialFileName(String.valueOf(file));
//        BufferedImage image1 = ImageIO.read(file);
//        //showSaveDialog pos up the save file dialog
//        File fileToBeSaved = fileChooser.showSaveDialog(window);
//        if (fileToBeSaved != null) {
//            try {
//                ImageIO.write(image1, "jpg", fileToBeSaved);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//
//            }
//        }
    }
}
