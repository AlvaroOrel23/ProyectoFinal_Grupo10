package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ModuloConfiguracion extends Application {

    // Hacemos constantes los colores de la marca 
    private static final String PRIMARY_GREEN = "#00945E"; 
    private static final String BACKGROUND_GRAY = "#D9D9D9"; 

    // Metodo para Main para prueba independiente
    public static void main(String[] args) {
        launch(args);
    }

    
    @Override
    public void start(Stage primaryStage) {
        
        ScrollPane root = getView();
        
        Scene scene = new Scene(root, 950, 700);
        primaryStage.setTitle("FarmaPlus - Configuración (Vista Previa)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public ScrollPane getView() {
        // 1. Contenedor Principal 
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle("-fx-background-color: white;");

        // 2. Título General
        Label lblTitle = new Label("Configuración");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 28));
        lblTitle.setStyle("-fx-text-fill: #004d40;");

        // 3. Estructura de Columnas (Izquierda y Derecha) 
        GridPane layoutGrid = new GridPane();
        layoutGrid.setHgap(40); // Espacio horizontal entre columnas
        layoutGrid.setVgap(20);
        
        // COLUMNA IZQUIERDA
        VBox colLeft = new VBox(25);
        
        // Sección: Datos de la empresa
        VBox secEmpresa = createSection("Datos de la empresa");
        GridPane gridEmpresa = new GridPane();
        gridEmpresa.setHgap(10); gridEmpresa.setVgap(10);
        
        TextField txtNombre = createStyledTextField("Nombre de la farmacia");
        TextField txtMovil = createStyledTextField("Movil");
        TextField txtDireccion = createStyledTextField("Dirección");
        TextField txtRuc = createStyledTextField("RUC/ID");

        // Fila 0
        gridEmpresa.add(txtNombre, 0, 0); 
        gridEmpresa.add(txtMovil, 1, 0);
        GridPane.setHgrow(txtNombre, Priority.ALWAYS); // Nombre ocupa más espacio
        
        // Fila 1 y 2 
        gridEmpresa.add(txtDireccion, 0, 1, 2, 1);
        gridEmpresa.add(txtRuc, 0, 2, 2, 1);
        
        secEmpresa.getChildren().add(gridEmpresa);

        // Sección: Parámetros de Venta
        VBox secVenta = createSection("Parámetros de Venta");
        GridPane gridVenta = new GridPane();
        gridVenta.setHgap(15); gridVenta.setVgap(15);
        
        ComboBox<String> cmbMoneda = createStyledComboBox("PEN", "USD", "EUR");
        ComboBox<String> cmbImpuestos = createStyledComboBox("18", "10", "0");
        ComboBox<String> cmbPago = createStyledComboBox("TARJETA", "EFECTIVO", "YAPE");
        
        addLabeledControl(gridVenta, "Moneda Predeterminada", cmbMoneda, 0, 0);
        addLabeledControl(gridVenta, "Impuestos (%)", cmbImpuestos, 1, 0);
        addLabeledControl(gridVenta, "Metodos de Pago", cmbPago, 0, 1);
        
        secVenta.getChildren().add(gridVenta);

        // Sección: Idioma
        VBox secIdioma = createSection("Idioma");
        ComboBox<String> cmbIdioma = createStyledComboBox("Español", "Inglés");
        cmbIdioma.setMaxWidth(Double.MAX_VALUE);
        addLabeledControl(secIdioma, "Idioma", cmbIdioma);

        // Sección: Inventario
        VBox secInventario = createSection("Inventario");
        CheckBox chkStockMin = createStyledCheckBox("Activar alertas de stock minimo");
        CheckBox chkStockVencer = createStyledCheckBox("Activar alertas de stock proximo a vencer");
        secInventario.getChildren().addAll(chkStockMin, chkStockVencer);

        colLeft.getChildren().addAll(secEmpresa, secVenta, secIdioma, secInventario);

        //  COLUMNA DERECHA 
        VBox colRight = new VBox(25);

        // Sección: Usuario y Roles
        VBox secRoles = createSection("Usuario y Roles");
        Button btnGestionar = new Button("Gestionar usuarios  ➜");
        btnGestionar.setStyle("-fx-background-color: " + BACKGROUND_GRAY + "; -fx-background-radius: 15; -fx-padding: 8 15; -fx-font-size: 14; -fx-cursor: hand;");
        btnGestionar.setMaxWidth(Double.MAX_VALUE);
        
        ComboBox<String> cmbRoles = createStyledComboBox("Administrador", "Vendedor", "Cajero");
        cmbRoles.setMaxWidth(Double.MAX_VALUE);
        
        secRoles.getChildren().add(btnGestionar);
        VBox boxRolesContainer = new VBox(5);
        boxRolesContainer.getChildren().addAll(new Label("Roles"), cmbRoles);
        secRoles.getChildren().add(boxRolesContainer);

        // Sección: Reportes
        VBox secReportes = createSection("Reportes");
        ComboBox<String> cmbExport = createStyledComboBox("PDF", "Excel", "CSV");
        cmbExport.setMaxWidth(Double.MAX_VALUE);
        addLabeledControl(secReportes, "Formato de exportación", cmbExport);

        colRight.getChildren().addAll(secRoles, secReportes);

        // Añadir columnas al Grid Layout principal
        layoutGrid.add(colLeft, 0, 0);
        layoutGrid.add(colRight, 1, 0);
        
        // Configurar constraints 
        ColumnConstraints col1Const = new ColumnConstraints();
        col1Const.setPercentWidth(60);
        ColumnConstraints col2Const = new ColumnConstraints();
        col2Const.setPercentWidth(40);
        layoutGrid.getColumnConstraints().addAll(col1Const, col2Const);

        // 4. Botones de Acción 
        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(20, 0, 0, 0));

        Button btnDescartar = new Button("Descartar");
        btnDescartar.setStyle("-fx-background-color: " + BACKGROUND_GRAY + "; -fx-text-fill: black; -fx-background-radius: 5; -fx-padding: 8 20; -fx-font-weight: bold; -fx-cursor: hand;");
        
        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setStyle("-fx-background-color: " + PRIMARY_GREEN + "; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 20; -fx-font-weight: bold; -fx-cursor: hand;");

        buttonBar.getChildren().addAll(btnDescartar, btnGuardar);

        // Agregar todo al contenedor principal
        mainContainer.getChildren().addAll(lblTitle, layoutGrid, buttonBar);

        // 5. Retornar dentro de un ScrollPane
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        return scrollPane;
    }

  
    // MÉTODOS AUXILIARES 
    
    private VBox createSection(String title) {
        VBox section = new VBox(10);
        Label lblHeader = new Label(title);
        lblHeader.setFont(Font.font("System", FontWeight.BOLD, 16));
        section.getChildren().add(lblHeader);
        return section;
    }

    private TextField createStyledTextField(String placeholder) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        tf.setStyle("-fx-background-color: " + BACKGROUND_GRAY + "; -fx-background-radius: 15; -fx-padding: 8;");
        return tf;
    }

    private ComboBox<String> createStyledComboBox(String... items) {
        ComboBox<String> cmb = new ComboBox<>();
        cmb.getItems().addAll(items);
        if (items.length > 0) cmb.getSelectionModel().selectFirst();
        cmb.setStyle("-fx-background-color: white; -fx-border-color: #D9D9D9; -fx-border-radius: 5; -fx-background-radius: 5;");
        return cmb;
    }

    private CheckBox createStyledCheckBox(String text) {
        CheckBox chk = new CheckBox(text);
        chk.setFont(Font.font("System", 14));
        chk.setStyle("-fx-font-size: 14px; -fx-text-fill: black;"); 
        return chk;
    }

    private void addLabeledControl(Pane parent, String labelText, Control control) {
        VBox box = new VBox(5);
        Label lbl = new Label(labelText);
        lbl.setTextFill(Color.web("#555555"));
        box.getChildren().addAll(lbl, control);
        parent.getChildren().add(box);
    }

    private void addLabeledControl(GridPane grid, String labelText, Control control, int col, int row) {
        VBox box = new VBox(5);
        Label lbl = new Label(labelText);
        lbl.setTextFill(Color.web("#555555"));
        box.getChildren().addAll(lbl, control);
        grid.add(box, col, row);
    }

} 
