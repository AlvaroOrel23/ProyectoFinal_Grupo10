package SistemaGestionFarmaPlus;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.Optional;
import java.util.Scanner;

public class ModuloProducto extends Application {
    private static TableView<ProductoFila> tabla;
    private static ObservableList<ProductoFila> masterData = FXCollections.observableArrayList();
    private static final String ARCHIVO = "productos.txt";

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(obtenerVista(), 950, 600));
        stage.setTitle("Gestión de Inventario - FarmaPlus");
        stage.show();
    }

    public static Pane obtenerVista() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Título del Módulo
        Label titulo = new Label("Inventario General de Productos");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#0D5F56"));

        // Botón con el estilo verde corporativo
        Button btnNuevo = new Button("+ Añadir Producto");
        btnNuevo.setStyle("-fx-background-color: #1AA37A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20;");
        btnNuevo.setCursor(javafx.scene.Cursor.HAND);
        btnNuevo.setOnAction(e -> mostrarVentana(null));

        cargarDatos(); 
        
        tabla = new TableView<>(masterData);
        tabla.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #cccccc;");
        configurarColumnas();

        root.getChildren().addAll(titulo, btnNuevo, tabla);
        return root;
    }

    public static void cargarDatos() {
        masterData.clear();
        File f = new File(ARCHIVO);
        if (f.exists()) {
            try (Scanner sc = new Scanner(f)) {
                while (sc.hasNextLine()) {
                    String[] d = sc.nextLine().split("\\|");
                    if (d.length == 5) masterData.add(new ProductoFila(d[0], d[1], d[2], Double.parseDouble(d[3]), Integer.parseInt(d[4])));
                }
            } catch (Exception e) { e.printStackTrace(); }
        } else {
            // Carga inicial desde InfoTienda si no existe el archivo
            masterData.add(new ProductoFila(InfoTienda.producto0_nombre, InfoTienda.producto0_marca, InfoTienda.producto0_laboratorio, InfoTienda.producto0_precio, InfoTienda.producto0_stock));
            guardar();
        }
    }

    public static void guardar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (ProductoFila p : masterData) {
                pw.println(p.getNombre()+"|"+p.getMarca()+"|"+p.getLab()+"|"+p.getPrecio()+"|"+p.getStock());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void configurarColumnas() {
        TableColumn<ProductoFila, String> colNom = new TableColumn<>("Producto");
        colNom.setCellValueFactory(d -> d.getValue().nombreProperty());

        TableColumn<ProductoFila, Number> colPre = new TableColumn<>("Precio");
        colPre.setCellValueFactory(d -> d.getValue().precioProperty());
        colPre.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : "S/ " + String.format("%.2f", item.doubleValue()));
            }
        });

        TableColumn<ProductoFila, Number> colSto = new TableColumn<>("Stock");
        colSto.setCellValueFactory(d -> d.getValue().stockProperty());

        TableColumn<ProductoFila, Void> colAcc = new TableColumn<>("Acciones");
        colAcc.setCellFactory(param -> new TableCell<>() {
            private final Button btnE = new Button("✎");
            private final Button btnB = new Button("🗑");
            private final HBox p = new HBox(10, btnE, btnB);
            {
                p.setAlignment(Pos.CENTER);
                btnE.setStyle("-fx-background-color: #0D5F56; -fx-text-fill: white; -fx-background-radius: 5;");
                btnB.setStyle("-fx-background-color: #E04A4A; -fx-text-fill: white; -fx-background-radius: 5;");
                
                btnE.setOnAction(e -> mostrarVentana(getTableView().getItems().get(getIndex())));
                
                // VALIDACIÓN DE BORRADO
                btnB.setOnAction(e -> { 
                    ProductoFila producto = getTableView().getItems().get(getIndex());
                    
                    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacion.setTitle("Confirmar Eliminación");
                    confirmacion.setHeaderText("¿Seguro que desea eliminar este producto?");
                    confirmacion.setContentText("Producto: " + producto.getNombre());
                    
                    Optional<ButtonType> resultado = confirmacion.showAndWait();
                    if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                        masterData.remove(producto); 
                        guardar(); 
                    }
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : p);
            }
        });
        
        tabla.getColumns().addAll(colNom, colPre, colSto, colAcc);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private static void mostrarVentana(ProductoFila p) {
        Stage s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle(p == null ? "Nuevo Medicamento" : "Editar Producto");

        VBox v = new VBox(15);
        v.setPadding(new Insets(20));
        v.setStyle("-fx-background-color: white;");

        Label lblV = new Label(p == null ? "Registrar Producto" : "Actualizar Información");
        lblV.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblV.setTextFill(Color.web("#0D5F56"));

        TextField tfN = new TextField(p == null ? "" : p.getNombre());
        tfN.setPromptText("Nombre del producto");
        TextField tfP = new TextField(p == null ? "" : String.valueOf(p.getPrecio()));
        tfP.setPromptText("Precio (0.00)");
        TextField tfS = new TextField(p == null ? "" : String.valueOf(p.getStock()));
        tfS.setPromptText("Stock disponible");

        Button btn = new Button(p == null ? "Guardar" : "Actualizar");
        btn.setStyle("-fx-background-color: #1AA37A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        btn.setMaxWidth(Double.MAX_VALUE);
        
        btn.setOnAction(e -> {
            try {
                if (p == null) {
                    masterData.add(new ProductoFila(tfN.getText(), "Marca", "Lab", Double.parseDouble(tfP.getText()), Integer.parseInt(tfS.getText())));
                } else {
                    p.setNombre(tfN.getText());
                    p.setPrecio(Double.parseDouble(tfP.getText()));
                    p.setStock(Integer.parseInt(tfS.getText()));
                    tabla.refresh();
                }
                guardar(); 
                s.close();
            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Por favor ingrese valores numéricos válidos.");
                a.show();
            }
        });

        v.getChildren().addAll(lblV, new Label("Nombre:"), tfN, new Label("Precio:"), tfP, new Label("Stock:"), tfS, btn);
        s.setScene(new Scene(v, 300, 400)); 
        s.show();
    }

    public static class ProductoFila {
        private final SimpleStringProperty nombre, marca, lab;
        private final SimpleDoubleProperty precio;
        private final SimpleIntegerProperty stock;
        public ProductoFila(String n, String m, String l, double p, int s) {
            this.nombre = new SimpleStringProperty(n); this.marca = new SimpleStringProperty(m);
            this.lab = new SimpleStringProperty(l); this.precio = new SimpleDoubleProperty(p);
            this.stock = new SimpleIntegerProperty(s);
        }
        public String getNombre() { return nombre.get(); }
        public String getMarca() { return marca.get(); }
        public String getLab() { return lab.get(); }
        public void setNombre(String v) { nombre.set(v); }
        public double getPrecio() { return precio.get(); }
        public void setPrecio(double v) { precio.set(v); }
        public int getStock() { return stock.get(); }
        public void setStock(int v) { stock.set(v); }
        public SimpleStringProperty nombreProperty() { return nombre; }
        public SimpleDoubleProperty precioProperty() { return precio; }
        public SimpleIntegerProperty stockProperty() { return stock; }
    }
}
