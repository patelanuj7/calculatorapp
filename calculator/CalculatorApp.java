import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.math.BigInteger;

public class CalculatorApp extends Application {

    private TextField display;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Calculator");

        GridPane grid = createGridPane();
        setupDisplay(grid);
        setupButtons(grid);

        Scene scene = new Scene(grid, 350, 500);
        scene.getRoot().setStyle("-fx-background-color: #2b2b2b;");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(5);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(25);
            grid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < 8; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / 8);
            grid.getRowConstraints().add(rowConstraints);
        }

        return grid;
    }

    private void setupDisplay(GridPane grid) {
        display = new TextField();
        display.setEditable(false);
        display.setMinWidth(300);
        display.setPrefHeight(50);
        display.setFont(new Font(20));
        display.setStyle("-fx-background-color: #3c3f41; -fx-text-fill: #ffffff;");
    
        display.textProperty().addListener((observable, oldValue, newValue) -> adjustTextSize());
    
        grid.add(display, 0, 0, 3, 1);
    
        Button backspaceButton = new Button("<---");
        backspaceButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        backspaceButton.setFont(new Font(16));
        backspaceButton.setStyle("-fx-background-color: #3c3f41; -fx-text-fill: #ffffff;");
        backspaceButton.setOnAction(e -> handleBackspace());
    
        grid.add(backspaceButton, 3, 0);
    }
    

    private void handleBackspace() {
        if (!display.getText().isEmpty()) {
            display.setText(display.getText().substring(0, display.getText().length() - 1));
        }
    }
    
    

    private void setupButtons(GridPane grid) {
        String[] buttonLabels = {
                "7", "8", "9", "/", 
                "4", "5", "6", "*", 
                "1", "2", "3", "-", 
                "0", ".", "=", "+", 
                "C", "Mod", "%", "!", 
                "sin", "cos", "tan", "abs", 
                "log", "ln", "exp", "^"
        };

        int row = 1;
        int col = 0;
        for (String label : buttonLabels) {
            Button button = createButton(label);
            grid.add(button, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setFont(new Font(16));
        button.setStyle("-fx-background-color: #3c3f41; -fx-text-fill: #ffffff;");
    
        button.setOnAction(e -> handleButtonClick(label));
    
        return button;
    }
    
    

    private void adjustTextSize() {
        int textLength = display.getText().length();
        if (textLength < 10) {
            display.setFont(new Font(20));
        } else if (textLength < 20) {
            display.setFont(new Font(16));
        } else {
            display.setFont(new Font(12));
        }
    }

    private void handleButtonClick(String label) {
        try {
            switch (label) {
                case "=":
                    calculateResult();
                    break;
                case "C":
                    display.clear();
                    break;
                case "Mod":
                    display.appendText("%");
                    break;
                case "!":
                    factorial();
                    break;
                case "sin":
                case "cos":
                case "tan":
                    trigonometricFunction(label);
                    break;
                case "abs":
                    absoluteValue();
                    break;
                case "log":
                    logarithm();
                    break;
                case "ln":
                    naturalLogarithm();
                    break;
                case "exp":
                    exponential();
                    break;
                default:
                    display.appendText(label);
                    break;
            }
        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    private void calculateResult() {
        try {
            String expression = display.getText();
            if (expression.contains("%")) {
                String[] parts = expression.split("%");
                if (parts.length == 2) {
                    double a = Double.parseDouble(parts[0]);
                    double b = Double.parseDouble(parts[1]);
                    display.setText(String.format("%.5f", a % b));
                } else {
                    display.setText("Error");
                }
            } else {
                double result = eval(expression);
                display.setText(String.format("%.5f", result));
            }
        } catch (Exception e) {
            display.setText("Error");
        }
    }

    private void factorial() {
        try {
            int n = Integer.parseInt(display.getText());
            if (n < 0) {
                display.setText("Error: Negative input");
                return;
            }
            BigInteger result = BigInteger.ONE;
            for (int i = 2; i <= n; i++) {
                result = result.multiply(BigInteger.valueOf(i));
            }
            display.setText(result.toString());
        } catch (NumberFormatException e) {
            display.setText("Error: Invalid input");
        }
    }

    private void trigonometricFunction(String function) {
        try {
            double value = Math.toRadians(Double.parseDouble(display.getText()));
            double result;
            switch (function) {
                case "sin":
                    result = Math.sin(value);
                    break;
                case "cos":
                    result = Math.cos(value);
                    break;
                case "tan":
                    result = Math.tan(value);
                    break;
                default:
                    result = 0;
                    break;
            }
            display.setText(String.format("%.5f", result));
        } catch (Exception e) {
            display.setText("Error: Invalid input");
        }
    }

    private void absoluteValue() {
        try {
            double value = Double.parseDouble(display.getText());
            display.setText(String.format("%.5f", Math.abs(value)));
        } catch (NumberFormatException e) {
            display.setText("Error: Invalid input");
        }
    }

    private void logarithm() {
        try {
            double value = Double.parseDouble(display.getText());
            if (value <= 0) {
                display.setText("Error: Invalid input (x <= 0)");
                return;
            }
            display.setText(String.format("%.5f", Math.log10(value)));
        } catch (NumberFormatException e) {
            display.setText("Error: Invalid input");
        }
    }

    private void naturalLogarithm() {
        try {
            double value = Double.parseDouble(display.getText());
            if (value <= 0) {
                display.setText("Error: Invalid input (x <= 0)");
                return;
            }
            display.setText(String.format("%.5f", Math.log(value)));
        } catch (NumberFormatException e) {
            display.setText("Error: Invalid input");
        }
    }

    private void exponential() {
        try {
            double value = Double.parseDouble(display.getText());
            display.setText(String.format("%.5f", Math.exp(value)));
        } catch (NumberFormatException e) {
            display.setText("Error: Invalid input");
        }
    }

    private double eval(String expression) {
        try {
            return new ExpressionParser().parse(expression).evaluate();
        } catch (Exception e) {
            throw new RuntimeException("Invalid expression");
        }
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
