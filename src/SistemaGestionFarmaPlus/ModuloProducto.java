package SistemaGestionFarmaPlus;

import javafx.application.Application;
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

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

public class ModuloProducto extends Application {

    // Método Main para ejecución independiente 
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = obtenerVista();
     
        Scene scene = new Scene(root, 900, 600); 
        primaryStage.setTitle("Sistema FarmaPlus - Gestión de Productos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // -----------------------------------------------

    // Lista observable para la tabla
    private static final ObservableList<ProductoFila> inventario = FXCollections.observableArrayList(
            new ProductoFila("P001", "Savital Shampoo", "Higiene", "S/ 10.90", "50")
    );

    // Formato de precio en soles 
    private static String formatearPrecio(double valor) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "PE"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return "S/ " + nf.format(valor);
    }

    public static Pane obtenerVista() {
        BorderPane root = new BorderPane();
        // Fondo blanco general
        root.setStyle("-fx-background-color: white;"); 

        // Creamos el contenido principal (VBox)
        VBox contenido = new VBox(20);
        contenido.setFillWidth(true);
        contenido.setPadding(new Insets(20)); 

        Label titulo = new Label("Productos");
        titulo.setFont(new Font("Segoe UI", 24));
        titulo.setStyle("-fx-text-fill: #0D5F56;"); 

        Node seccionAnadir = crearSeccionAnadir();
        Node seccionEditar = crearSeccionEditar();

        contenido.getChildren().addAll(titulo, seccionAnadir, seccionEditar);

        // Creamos el ScrollPane y metemos el contenido dentro
        ScrollPane scrollPane = new ScrollPane(contenido);
        
        scrollPane.setFitToWidth(true);
        
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: white;");
        
        root.setCenter(scrollPane);
        
        return root;
    }

    // Sección Añadir
    private static Node crearSeccionAnadir() {
        VBox box = new VBox(15);

        Label subtitulo = new Label("Añadir");
        subtitulo.setFont(new Font("Segoe UI", 18));
        subtitulo.setStyle("-fx-text-fill: #0D5F56;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);

        Label lblNombre = new Label("Nombre:");
        Label lblCodigo = new Label("Código:");
        Label lblCategoria = new Label("Categoría:");
        Label lblPrecio = new Label("Precio:");
        Label lblCantidad = new Label("Cantidad:");

        TextField tfNombre = new TextField();
        TextField tfCodigo = new TextField();
        TextField tfCategoria = new TextField();
        TextField tfPrecio = new TextField();
        TextField tfCantidad = new TextField();

        tfNombre.setPromptText("Ej.: Savital Shampoo");
        tfCodigo.setPromptText("Ej.: P001");
        tfCategoria.setPromptText("Ej.: Higiene");
        tfPrecio.setPromptText("Ej.: 10.90");
        tfCantidad.setPromptText("Ej.: 50");

        String estiloCampo = ""
                + "-fx-background-color: #F3F5F7;"
                + "-fx-background-radius: 10;"
                + "-fx-padding: 8 12;"
                + "-fx-border-color: transparent;"
                + "-fx-font-size: 13px;";
        tfNombre.setStyle(estiloCampo);
        tfCodigo.setStyle(estiloCampo);
        tfCategoria.setStyle(estiloCampo);
        tfPrecio.setStyle(estiloCampo);
        tfCantidad.setStyle(estiloCampo);

        grid.add(lblNombre, 0, 0); grid.add(tfNombre, 1, 0);
        grid.add(lblCodigo, 0, 1); grid.add(tfCodigo, 1, 1);
        grid.add(lblCategoria, 0, 2); grid.add(tfCategoria, 1, 2);
        grid.add(lblPrecio, 0, 3); grid.add(tfPrecio, 1, 3);
        grid.add(lblCantidad, 0, 4); grid.add(tfCantidad, 1, 4);

        Button btnAgregar = new Button("Agregar Producto");
        btnAgregar.setStyle(
                "-fx-background-color: #1AA37A;"
                        + "-fx-text-fill: white;"
                        + "-fx-padding: 8 18;"
                        + "-fx-background-radius: 20;"
                        + "-fx-font-weight: bold;"
        );

        Runnable onAgregarProducto = () -> {
            String nombre = tfNombre.getText().trim();
            String codigo = tfCodigo.getText().trim();
            String categoria = tfCategoria.getText().trim();
            String precioTxt = tfPrecio.getText().trim();
            String cantidadTxt = tfCantidad.getText().trim();

            if (nombre.isEmpty() || codigo.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos obligatorios",
                        "Por favor ingresa al menos Nombre y Código.");
                return;
            }

            double precio;
            int cantidad;
            try {
                precio = Double.parseDouble(precioTxt.replace(",", "."));
            } catch (Exception ex) {
                mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido",
                        "Ingresa un precio numérico, por ejemplo: 10.90");
                return;
            }
            try {
                cantidad = Integer.parseInt(cantidadTxt);
                if (cantidad < 0) throw new NumberFormatException();
            } catch (Exception ex) {
                mostrarAlerta(Alert.AlertType.WARNING, "Cantidad inválida",
                        "Ingresa una cantidad entera mayor o igual a 0.");
                return;
            }

            Optional<ProductoFila> existente = inventario.stream()
                    .filter(f -> f.getCodigo().equalsIgnoreCase(codigo))
                    .findFirst();

            if (existente.isPresent()) {
                ProductoFila fila = existente.get();
                int stockActual;
                try {
                    stockActual = Integer.parseInt(fila.getStock());
                } catch (Exception e) {
                    stockActual = 0;
                }
                fila.setStock(String.valueOf(stockActual + cantidad));
                if (!nombre.isEmpty()) fila.setNombre(nombre);
                if (!categoria.isEmpty()) fila.setCategoria(categoria);
                fila.setPrecio(formatearPrecio(precio));
            } else {
                inventario.add(new ProductoFila(
                        codigo,
                        nombre,
                        categoria,
                        formatearPrecio(precio),
                        String.valueOf(cantidad)
                ));
            }

            tfNombre.clear(); tfCodigo.clear(); tfCategoria.clear(); tfPrecio.clear(); tfCantidad.clear();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto registrado",
                    "El producto se ha agregado/actualizado correctamente.");
        };

        btnAgregar.setOnAction(e -> onAgregarProducto.run());
        tfNombre.setOnAction(e -> onAgregarProducto.run());
        tfCodigo.setOnAction(e -> onAgregarProducto.run());
        tfCategoria.setOnAction(e -> onAgregarProducto.run());
        tfPrecio.setOnAction(e -> onAgregarProducto.run());
        tfCantidad.setOnAction(e -> onAgregarProducto.run());

        box.getChildren().addAll(subtitulo, grid, btnAgregar);
        return box;
    }

    //  Sección Editar 
    private static Node crearSeccionEditar() {
        VBox box = new VBox(10);

        Label subtitulo = new Label("Editar");
        subtitulo.setFont(new Font("Segoe UI", 18));
        subtitulo.setStyle("-fx-text-fill: #0D5F56;");

        TableView<ProductoFila> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setItems(inventario);
        tabla.setStyle("-fx-background-color: white;");
        tabla.setPrefHeight(300); 

        TableColumn<ProductoFila, String> colCodigo = new TableColumn<>("Código");
        TableColumn<ProductoFila, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<ProductoFila, String> colCategoria = new TableColumn<>("Categoría");
        TableColumn<ProductoFila, String> colPrecio = new TableColumn<>("Precio");
        TableColumn<ProductoFila, String> colStock = new TableColumn<>("Stock");

        colCodigo.setCellValueFactory(data -> data.getValue().codigoProperty());
        colNombre.setCellValueFactory(data -> data.getValue().nombreProperty());
        colCategoria.setCellValueFactory(data -> data.getValue().categoriaProperty());
        colPrecio.setCellValueFactory(data -> data.getValue().precioProperty());
        colStock.setCellValueFactory(data -> data.getValue().stockProperty());

        TableColumn<ProductoFila, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(tc -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnBorrar = new Button("Borrar");
            private final HBox caja = new HBox(5);

            {
                caja.setAlignment(Pos.CENTER_LEFT);
                btnEditar.setStyle("-fx-background-color: #0D5F56; -fx-text-fill: white; -fx-padding: 4 10; -fx-background-radius: 8;");
                btnBorrar.setStyle("-fx-background-color: #E04A4A; -fx-text-fill: white; -fx-padding: 4 10; -fx-background-radius: 8;");

                btnEditar.setOnAction(e -> {
                    if (getTableView() != null && getIndex() < getTableView().getItems().size()) {
                        ProductoFila fila = getTableView().getItems().get(getIndex());
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Editar (demo)",
                                "Aquí puedes abrir un formulario para editar:\n\n"
                                        + "Código: " + fila.getCodigo() + "\n"
                                        + "Nombre: " + fila.getNombre() + "\n"
                                        + "Categoría: " + fila.getCategoria() + "\n"
                                        + "Precio: " + fila.getPrecio() + "\n"
                                        + "Stock: " + fila.getStock());
                    }
                });

                btnBorrar.setOnAction(e -> {
                    if (getTableView() != null && getIndex() < getTableView().getItems().size()) {
                        ProductoFila fila = getTableView().getItems().get(getIndex());
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Confirmar borrado");
                        confirm.setHeaderText(null);
                        confirm.setContentText("¿Eliminar el producto \"" + fila.getNombre() + "\"?");
                        confirm.showAndWait().ifPresent(resp -> {
                            if (resp == ButtonType.OK) {
                                inventario.remove(fila);
                            }
                        });
                    }
                });
                caja.getChildren().addAll(btnEditar, btnBorrar);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : caja);
            }
        });

        tabla.getColumns().addAll(colCodigo, colNombre, colCategoria, colPrecio, colStock, colAcciones);
        box.getChildren().addAll(subtitulo, tabla);
        return box;
    }

    // ---------------------- Modelo simple de producto ----------------------
    public static class ProductoFila {
        private final SimpleStringProperty codigo;
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty categoria;
        private final SimpleStringProperty precio;
        private final SimpleStringProperty stock;

        public ProductoFila(String codigo, String nombre, String categoria, String precio, String stock) {
            this.codigo = new SimpleStringProperty(codigo);
            this.nombre = new SimpleStringProperty(nombre);
            this.categoria = new SimpleStringProperty(categoria);
            this.precio = new SimpleStringProperty(precio);
            this.stock = new SimpleStringProperty(stock);
        }

        public SimpleStringProperty codigoProperty() { return codigo; }
        public SimpleStringProperty nombreProperty() { return nombre; }
        public SimpleStringProperty categoriaProperty() { return categoria; }
        public SimpleStringProperty precioProperty() { return precio; }
        public SimpleStringProperty stockProperty() { return stock; }

        public String getCodigo() { return codigo.get(); }
        public String getNombre() { return nombre.get(); }
        public String getCategoria() { return categoria.get(); }
        public String getPrecio() { return precio.get(); }
        public String getStock() { return stock.get(); }

        public void setNombre(String v) { nombre.set(v); }
        public void setCategoria(String v) { categoria.set(v); }
        public void setPrecio(String v) { precio.set(v); }
        public void setStock(String v) { stock.set(v); }
    }

    private static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}