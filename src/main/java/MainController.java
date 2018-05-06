import com.gluonhq.charm.glisten.control.ProgressIndicator;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import photo.processor.core.filters.Convolution;
import photo.processor.core.filters.contour.PrewittKernels;
import photo.processor.core.filters.contour.SobelKernels;
import photo.processor.core.filters.emboss.EmbossKernels;
import photo.processor.core.filters.gradient.GradientKernels;
import photo.processor.core.filters.hpf.HighPassKernels;
import photo.processor.core.filters.laplace.LaplaceKernels;
import photo.processor.core.filters.lpf.LowPassKernels;
import photo.processor.core.filters.statistic.Maximum;
import photo.processor.core.filters.statistic.Median;
import photo.processor.core.filters.statistic.Minimum;
import photo.processor.core.filters.subtract.DirectionKernels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MainController {

    @FXML
    private ImageView imageView;
    @FXML
    private JFXComboBox<String> filterSelector;
    @FXML
    private JFXComboBox<String> filterTypeSelector;
    @FXML
    private JFXCheckBox originalTransform;
    @FXML
    private ProgressIndicator progressIndicator;

    private Stage window;
    private File imageFile;
    private Image originalImage;
    private Image image;

    private ObservableList<String> lpfOptions;
    private Map<String, Supplier<BufferedImage>> lpfMethods;
    private ObservableList<String> hpfOptions;
    private Map<String, Supplier<BufferedImage>> hpfMethods;
    private ObservableList<String> subtractOptions;
    private Map<String, Supplier<BufferedImage>> subtractMethods;
    private ObservableList<String> gradientOptions;
    private Map<String, Supplier<BufferedImage>> gradientMethods;
    private ObservableList<String> embossOptions;
    private Map<String, Supplier<BufferedImage>> embossMethods;
    private ObservableList<String> laplaceOptions;
    private Map<String, Supplier<BufferedImage>> laplaceMethods;
    private ObservableList<String> contoursOptions;
    private Map<String, Supplier<BufferedImage>> contoursMethods;
    private ObservableList<String> statisticOptions;
    private Map<String, Supplier<BufferedImage>> statisticMethods;


    @FXML
    private void initialize() {
        lpfOptions = FXCollections.observableArrayList(
                "Average", "Square", "Circular", "LP1", "LP2", "LP3", "Pyramidal", "Conical",
                "Gauss1", "Gauss2", "Gauss3", "Gauss4", "Gauss5"
        );
        lpfMethods = new HashMap<>();
        lpfMethods.put("Average", () -> new Convolution(LowPassKernels.AVERAGE).getTransformedImage(getParameter()));
        lpfMethods.put("Square", () -> new Convolution(LowPassKernels.SQUARE).getTransformedImage(getParameter()));
        lpfMethods.put("Circular", () -> new Convolution(LowPassKernels.CIRCULAR).getTransformedImage(getParameter()));
        lpfMethods.put("LP1", () -> new Convolution(LowPassKernels.LP1).getTransformedImage(getParameter()));
        lpfMethods.put("LP2", () -> new Convolution(LowPassKernels.LP2).getTransformedImage(getParameter()));
        lpfMethods.put("LP3", () -> new Convolution(LowPassKernels.LP3).getTransformedImage(getParameter()));
        lpfMethods.put("Pyramidal", () -> new Convolution(LowPassKernels.PYRAMIDAL).getTransformedImage(getParameter()));
        lpfMethods.put("Conical", () -> new Convolution(LowPassKernels.CONICAL).getTransformedImage(getParameter()));
        lpfMethods.put("Gauss1", () -> new Convolution(LowPassKernels.GAUSS1).getTransformedImage(getParameter()));
        lpfMethods.put("Gauss2", () -> new Convolution(LowPassKernels.GAUSS2).getTransformedImage(getParameter()));
        lpfMethods.put("Gauss3", () -> new Convolution(LowPassKernels.GAUSS3).getTransformedImage(getParameter()));
        lpfMethods.put("Gauss4", () -> new Convolution(LowPassKernels.GAUSS4).getTransformedImage(getParameter()));
        lpfMethods.put("Gauss5", () -> new Convolution(LowPassKernels.GAUSS5).getTransformedImage(getParameter()));

        hpfOptions = FXCollections.observableArrayList(
                "Mean removal", "HP1", "HP2", "HP3"
        );
        hpfMethods = new HashMap<>();
        hpfMethods.put("Mean removal", () -> new Convolution(HighPassKernels.MEAN_REMOVAL).getTransformedImage(getParameter()));
        hpfMethods.put("HP1", () -> new Convolution(HighPassKernels.HP1).getTransformedImage(getParameter()));
        hpfMethods.put("HP2", () -> new Convolution(HighPassKernels.HP2).getTransformedImage(getParameter()));
        hpfMethods.put("HP3", () -> new Convolution(HighPassKernels.HP3).getTransformedImage(getParameter()));

        subtractOptions = FXCollections.observableArrayList(
                "Horizontal", "Vertical", "Right diagonal", "Left diagonal"
        );
        subtractMethods = new HashMap<>();
        subtractMethods.put("Horizontal", () -> new Convolution(DirectionKernels.HORIZONTAL).getTransformedImage(getParameter()));
        subtractMethods.put("Vertical", () -> new Convolution(DirectionKernels.VERTICAL).getTransformedImage(getParameter()));
        subtractMethods.put("Right diagonal", () -> new Convolution(DirectionKernels.RIGHT_DIAGONAL).getTransformedImage(getParameter()));
        subtractMethods.put("Left diagonal", () -> new Convolution(DirectionKernels.LEFT_DIAGONAL).getTransformedImage(getParameter()));

        gradientOptions = FXCollections.observableArrayList(
                "East", "Southeast", "South", "Southwest", "West", "Northwest", "North", "Northeast"
        );
        gradientMethods = new HashMap<>();
        gradientMethods.put("East", () -> new Convolution(GradientKernels.EAST).getTransformedImage(getParameter()));
        gradientMethods.put("Southeast", () -> new Convolution(GradientKernels.SOUTHEAST).getTransformedImage(getParameter()));
        gradientMethods.put("South", () -> new Convolution(GradientKernels.SOUTH).getTransformedImage(getParameter()));
        gradientMethods.put("Southwest", () -> new Convolution(GradientKernels.SOUTHWEST).getTransformedImage(getParameter()));
        gradientMethods.put("West", () -> new Convolution(GradientKernels.WEST).getTransformedImage(getParameter()));
        gradientMethods.put("Northwest", () -> new Convolution(GradientKernels.NORTHWEST).getTransformedImage(getParameter()));
        gradientMethods.put("North", () -> new Convolution(GradientKernels.NORTH).getTransformedImage(getParameter()));
        gradientMethods.put("Northeast", () -> new Convolution(GradientKernels.NORTHEAST).getTransformedImage(getParameter()));

        embossOptions = FXCollections.observableArrayList(
                "East", "Southeast", "South", "Southwest", "West", "Northwest", "North", "Northeast"
        );
        embossMethods = new HashMap<>();
        embossMethods.put("East", () -> new Convolution(EmbossKernels.EAST).getTransformedImage(getParameter()));
        embossMethods.put("Southeast", () -> new Convolution(EmbossKernels.SOUTHEAST).getTransformedImage(getParameter()));
        embossMethods.put("South", () -> new Convolution(EmbossKernels.SOUTH).getTransformedImage(getParameter()));
        embossMethods.put("Southwest", () -> new Convolution(EmbossKernels.SOUTHWEST).getTransformedImage(getParameter()));
        embossMethods.put("West", () -> new Convolution(EmbossKernels.WEST).getTransformedImage(getParameter()));
        embossMethods.put("Northwest", () -> new Convolution(EmbossKernels.NORTHWEST).getTransformedImage(getParameter()));
        embossMethods.put("North", () -> new Convolution(EmbossKernels.NORTH).getTransformedImage(getParameter()));
        embossMethods.put("Northeast", () -> new Convolution(EmbossKernels.NORTHEAST).getTransformedImage(getParameter()));

        laplaceOptions = FXCollections.observableArrayList(
                "LAPL1", "LAPL2", "LAPL3", "Diagonal", "Horizontal", "Vertical"
        );
        laplaceMethods = new HashMap<>();
        laplaceMethods.put("LAPL1", () -> new Convolution(LaplaceKernels.LAPL1).getTransformedImage(getParameter()));
        laplaceMethods.put("LAPL2", () -> new Convolution(LaplaceKernels.LAPL2).getTransformedImage(getParameter()));
        laplaceMethods.put("LAPL3", () -> new Convolution(LaplaceKernels.LAPL3).getTransformedImage(getParameter()));
        laplaceMethods.put("Diagonal", () -> new Convolution(LaplaceKernels.DIAGONAL).getTransformedImage(getParameter()));
        laplaceMethods.put("Horizontal", () -> new Convolution(LaplaceKernels.HORIZONTAL).getTransformedImage(getParameter()));
        laplaceMethods.put("Vertical", () -> new Convolution(LaplaceKernels.VERTICAL).getTransformedImage(getParameter()));

        contoursOptions = FXCollections.observableArrayList(
                "Sobel horizontal", "Sobel vertical", "Prewitt horizontal", "Prewitt vertical"
        );
        contoursMethods = new HashMap<>();
        contoursMethods.put("Sobel horizontal", () -> new Convolution(SobelKernels.HORIZONTAL).getTransformedImage(getParameter()));
        contoursMethods.put("Sobel vertical", () -> new Convolution(SobelKernels.VERTICAL).getTransformedImage(getParameter()));
        contoursMethods.put("Prewitt horizontal", () -> new Convolution(PrewittKernels.HORIZONTAL).getTransformedImage(getParameter()));
        contoursMethods.put("Prewitt vertical", () -> new Convolution(PrewittKernels.VERTICAL).getTransformedImage(getParameter()));

        statisticOptions = FXCollections.observableArrayList(
                "Minimum", "Maximum", "Median"
        );
        statisticMethods = new HashMap<>();
        statisticMethods.put("Minimum", () -> new Minimum().getTransformedImage(getParameter()));
        statisticMethods.put("Maximum", () -> new Maximum().getTransformedImage(getParameter()));
        statisticMethods.put("Median", () -> new Median().getTransformedImage(getParameter()));
    }

    private BufferedImage getParameter() {
        return SwingFXUtils.fromFXImage(image, null);
    }

    public void setStage(Stage primaryStage) {
        window = primaryStage;
    }

    @FXML
    private void openFile(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseButton.PRIMARY)
            return;
        if (imageFile != null)
            imageFile = ImageReaderWriter.showOpenDialog(window, imageFile.getParent());
        else
            imageFile = ImageReaderWriter.showOpenDialog(window, System.getProperty("user.home"));
        openImage(imageFile);
    }

    private void openImage(File imageFile) {
        if (imageFile == null)
            return;
        this.imageFile = imageFile;
        image = ImageReaderWriter.openImage(this.imageFile, false);
        if (image != null) {
            imageView.setImage(image);
            originalImage = image;
        }
    }

    @FXML
    private void saveImage(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseButton.PRIMARY || image == null)
            return;
        File newImageFile = ImageReaderWriter.showSaveDialog(window, imageFile);
        if (newImageFile == null)
            return;
        ImageReaderWriter.saveImage(image, newImageFile);
        imageFile = newImageFile;
        originalImage = image;
    }

    @FXML
    private void discardChanges(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseButton.PRIMARY)
            return;
        image = originalImage;
        if (image != null)
            imageView.setImage(image);
        filterTypeSelector.getSelectionModel().select(-1);
    }

    @FXML
    private void selectFilterType() {
        String selected = filterTypeSelector.getSelectionModel().getSelectedItem();
        filterSelector.setItems(null);
        if (selected == null)
            return;
        switch (selected) {
            case "Low-pass":
                filterSelector.setItems(lpfOptions);
                break;
            case "High-pass":
                filterSelector.setItems(hpfOptions);
                break;
            case "Move and subtract":
                filterSelector.setItems(subtractOptions);
                break;
            case "Gradient":
                filterSelector.setItems(gradientOptions);
                break;
            case "Emboss":
                filterSelector.setItems(embossOptions);
                break;
            case "Laplace":
                filterSelector.setItems(laplaceOptions);
                break;
            case "Contours":
                filterSelector.setItems(contoursOptions);
                break;
            case "Statistic":
                filterSelector.setItems(statisticOptions);
                break;
        }
    }

    @FXML
    private void transformImage() {
        if (image == null)
            return;
        String selected = filterTypeSelector.getSelectionModel().getSelectedItem();
        String algorithm = filterSelector.getSelectionModel().getSelectedItem();
        if (algorithm == null)
            return;
        BufferedImage result = null;
        if (originalTransform.isSelected())
            image = originalImage;
        switch (selected) {
            case "Low-pass":
                result = lpfMethods.get(algorithm).get();
                break;
            case "High-pass":
                result = hpfMethods.get(algorithm).get();
                break;
            case "Move and subtract":
                result = subtractMethods.get(algorithm).get();
                break;
            case "Gradient":
                result = gradientMethods.get(algorithm).get();
                break;
            case "Emboss":
                result = embossMethods.get(algorithm).get();
                break;
            case "Laplace":
                result = laplaceMethods.get(algorithm).get();
                break;
            case "Contours":
                result = contoursMethods.get(algorithm).get();
                break;
            case "Statistic":
                result = statisticMethods.get(algorithm).get();
                break;
        }
        if (result != null) {
            image = SwingFXUtils.toFXImage(result, null);
            imageView.setImage(image);
        }
    }
}
