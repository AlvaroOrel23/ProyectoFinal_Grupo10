package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ModuloStock extends Application {

    // --- Método Main para ejecución independiente ---
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = obtenerVista();
        // Tamaño sugerido para visualizar la tabla correctamente
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Sistema FarmaPlus - Stock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // -----------------------------------------------

    private static TableView<StockFila> tablaRef;

    private static final ObservableList<StockFila> stockData = FXCollections.observableArrayList(
            new StockFila("P001", "Savital Shampoo", 50, 50),
            new StockFila("P002", "Savital acondicionador", 100, 30),
            new StockFila("P003", "Fish Oil Omega 3", 5, 50),
            new StockFila("P004", "Vitamina C", 20, 20)
    );

    /** Devuelve el contenido central de la pantalla de Stock */
    public static Pane obtenerVista() {
        BorderPane root = new BorderPane();
        		
        root.setStyle("-fx-background-color: white;");

        VBox contenido = new VBox(20);
        contenido.setFillWidth(true);
        contenido.setPadding(new Insets(20)); 

        // Encabezado: "Stock" + botón a la derecha
        Node encabezado = crearEncabezado();

        // Tabla
        tablaRef = crearTabla();

        contenido.getChildren().addAll(encabezado, tablaRef);

        // --- Implementación del ScrollPane ---
        ScrollPane scrollPane = new ScrollPane(contenido);
        scrollPane.setFitToWidth(true); // Estira el contenido a lo ancho
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: white;");
        
        root.setCenter(scrollPane);

        return root;
    }

    /** Crea el encabezado con el título a la izquierda y el botón a la derecha */
    private static Node crearEncabezado() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label titulo = new Label("Stock");
        titulo.setFont(new Font("Segoe UI", 24));

        Region separador = new Region();
        // Se añade la importación de Priority que faltaba en el original
        HBox.setHgrow(separador, Priority.ALWAYS);

        Button btnActualizar = new Button("Actualizar Stock");
        // Estilo "pill"
        btnActualizar.setStyle(
                "-fx-background-color: #1AA37A; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 8 18; " +
                "-fx-background-radius: 20; " +
                "-fx-font-weight: bold;"
        );

        btnActualizar.setOnAction(e -> onActualizarStock());

        header.getChildren().addAll(titulo, separador, btnActualizar);
        return header;
    }

    /** Crea la TableView como en el wireframe */
    private static TableView<StockFila> crearTabla() {
        TableView<StockFila> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setItems(stockData);
        // Altura mínima para que se vea bien
        tabla.setPrefHeight(400); 

        TableColumn<StockFila, String> colCodigo = new TableColumn<>("Código");
        TableColumn<StockFila, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<StockFila, Number> colStockActual = new TableColumn<>("Stock Actual");
        TableColumn<StockFila, Number> colStockMin = new TableColumn<>("Stock Min");

        colCodigo.setCellValueFactory(data -> data.getValue().codigoProperty());
        colNombre.setCellValueFactory(data -> data.getValue().nombreProperty());
        colStockActual.setCellValueFactory(data -> data.getValue().stockActualProperty());
        colStockMin.setCellValueFactory(data -> data.getValue().stockMinProperty());

        // Alineación (números a la derecha)
        colStockActual.setStyle("-fx-alignment: CENTER-RIGHT;");
        colStockMin.setStyle("-fx-alignment: CENTER-RIGHT;");

        // Resaltar fila cuando el stock actual está por debajo del mínimo
        tabla.setRowFactory(tv -> new TableRow<StockFila>() {
            @Override
            protected void updateItem(StockFila item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    if (item.getStockActual() < item.getStockMin()) {
                        setStyle("-fx-background-color: #ffeeee;"); // alerta roja suave
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        tabla.getColumns().addAll(colCodigo, colNombre, colStockActual, colStockMin);
        return tabla;
    }

    /** Acción del botón "Actualizar Stock": dialog para modificar la fila seleccionada */
    private static void onActualizarStock() {
        StockFila seleccionado = tablaRef.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Selecciona una fila",
                    "Elige un producto en la tabla para actualizar su stock.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(seleccionado.getStockActual()));
        dialog.setTitle("Actualizar Stock");
        dialog.setHeaderText("Producto: " + seleccionado.getNombre());
        dialog.setContentText("Nuevo Stock Actual:");

        dialog.showAndWait().ifPresent(valor -> {
            try {
                int nuevo = Integer.parseInt(valor.trim());
                if (nuevo < 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Valor no válido", "El stock no puede ser negativo.");
                    return;
                }
                seleccionado.setStockActual(nuevo);
                tablaRef.refresh();
            } catch (NumberFormatException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Entrada inválida", "Ingresa un número entero válido.");
            }
        });
    }

    private static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    /** Modelo de fila de stock */
    public static class StockFila {
        private final SimpleStringProperty codigo;
        private final SimpleStringProperty nombre;
        private final SimpleIntegerProperty stockActual;
        private final SimpleIntegerProperty stockMin;

        public StockFila(String codigo, String nombre, int stockActual, int stockMin) {
            this.codigo = new SimpleStringProperty(codigo);
            this.nombre = new SimpleStringProperty(nombre);
            this.stockActual = new SimpleIntegerProperty(stockActual);
            this.stockMin = new SimpleIntegerProperty(stockMin);
        }

        public SimpleStringProperty codigoProperty() { return codigo; }
        public SimpleStringProperty nombreProperty() { return nombre; }
        public SimpleIntegerProperty stockActualProperty() { return stockActual; }
        public SimpleIntegerProperty stockMinProperty() { return stockMin; }

        public String getCodigo() { return codigo.get(); }
        public String getNombre() { return nombre.get(); }
        public int getStockActual() { return stockActual.get(); }
        public int getStockMin() { return stockMin.get(); }

        public void setStockActual(int v) { stockActual.set(v); }
        public void setStockMin(int v) { stockMin.set(v); }
    }
}
