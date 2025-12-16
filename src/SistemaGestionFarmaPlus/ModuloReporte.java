package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ModuloReporte extends Application {

    // Método Main para ejecución independiente
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = obtenerVista();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Sistema FarmaPlus - Reportes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // -----------------------------------------------

    // --- Datos de la tabla
    public static class ProductoReporte {
        private final String codigo;
        private final String producto;
        private final String stockActual;
        private final String vencimiento;
        private final String ubicacion;
        private final String proveedor;

        public ProductoReporte(String codigo, String producto, String stockActual, String vencimiento, String ubicacion, String proveedor) {
            this.codigo = codigo;
            this.producto = producto;
            this.stockActual = stockActual;
            this.vencimiento = vencimiento;
            this.ubicacion = ubicacion;
            this.proveedor = proveedor;
        }

        // Getters
        public String getCodigo() { return codigo; }
        public String getProducto() { return producto; }
        public String getStockActual() { return stockActual; }
        public String getVencimiento() { return vencimiento; }
        public String getUbicacion() { return ubicacion; }
        public String getProveedor() { return proveedor; }
    }

    public static Pane obtenerVista() {
        // Vista Principal
        VBox reporte = new VBox(20);
        reporte.setPadding(new Insets(30)) ;
        reporte.setStyle("-fx-background-color: #ffffff; -fx-padding: 30;");

        Label titulo = new Label("Reportes");
        titulo.setFont(new Font("Segoe UI", 28));

        HBox busquedaFiltros = crearBarraBusquedaYFiltros();

        Label fecha = new Label("Fecha: 29/11/2025");
        fecha.setFont(Font.font("Segoe UI", 14));
        fecha.setPadding(new Insets(10, 0, 10, 0));

        TableView<ProductoReporte> tablaReporte = crearTablaReporte();
        VBox observaciones = crearPanelObservaciones();
        
        reporte.getChildren().addAll(titulo, busquedaFiltros, fecha, tablaReporte, observaciones);

        return reporte;
    }

    // --- Barra de Búsqueda y Filtros ---
    private static HBox crearBarraBusquedaYFiltros() {
        HBox contenedor = new HBox(15);
        contenedor.setAlignment(Pos.CENTER_LEFT);
        return contenedor; 
    }

    // Tabla de Reporte 
    private static TableView<ProductoReporte> crearTablaReporte() {
        TableView<ProductoReporte> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPrefHeight(300);

        // Columnas
        TableColumn<ProductoReporte, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setPrefWidth(50);

        TableColumn<ProductoReporte, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        colProducto.setPrefWidth(120);

        TableColumn<ProductoReporte, String> colStock = new TableColumn<>("Stock Actual");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colStock.setPrefWidth(80);

        TableColumn<ProductoReporte, String> colVencimiento = new TableColumn<>("Vencimiento");
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("vencimiento"));
        colVencimiento.setPrefWidth(80);

        TableColumn<ProductoReporte, String> colUbicacion = new TableColumn<>("Ubicacion");
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colUbicacion.setPrefWidth(80);

        TableColumn<ProductoReporte, String> colProveedor = new TableColumn<>("Proveedor");
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        colProveedor.setPrefWidth(100);

        tabla.getColumns().addAll(colCodigo, colProducto, colStock, colVencimiento, colUbicacion, colProveedor);

        // Productos
        ObservableList<ProductoReporte> data = FXCollections.observableArrayList(
            new ProductoReporte("P001", "Savital shampoo", "S/ 10.90", "07/27", "Almacen A", "Alex SA"),
            new ProductoReporte("P002", "Savital acondicionador", "S/ 10.90", "11/26", "Tienda B", "Farmacia SA"),
            new ProductoReporte("P003", "Fish Oil Omega 3", "S/ 49.90", "05/26", "Almacen B", "Lock A"),
            new ProductoReporte("P004", "Vitamina C", "S/ 9.20", "11/30", "Farmacia C", "Lorent")
        );
        tabla.setItems(data);

        return tabla;
    }

    // Panel de Observaciones 
    private static VBox crearPanelObservaciones() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-border-color: #ff9900; -fx-border-width: 2; -fx-background-color: #fff0e0; -fx-border-radius: 5;");
        panel.setMaxWidth(400);

        Label tituloObs = new Label("⚠️ Observaciones");
        tituloObs.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        Label contenidoObs = new Label("Vitamina C esta próximo a vencimiento");
        contenidoObs.setFont(Font.font("Segoe UI", 12));

        panel.getChildren().addAll(tituloObs, contenidoObs);
        return panel;
    }
}
