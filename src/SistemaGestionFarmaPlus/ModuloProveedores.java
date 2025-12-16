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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

public class ModuloProveedores extends Application {

    // --- Método Main para ejecución independiente ---
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = obtenerVista();
        Scene scene = new Scene(root, 900, 650); // Tamaño ajustado para ver tabla y formulario
        primaryStage.setTitle("Sistema FarmaPlus - Gestión de Proveedores");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
   

    // Lista observable 
    private static final ObservableList<ProveedorFila> proveedores = FXCollections.observableArrayList(
            new ProveedorFila("925454564", "Alberto Fujimo", "adsda@gmail.com", "Laboratorios", "Ventas")
    );

    //Vista principal 
    public static Pane obtenerVista() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        VBox contenido = new VBox(20);
        contenido.setFillWidth(true);
        contenido.setPadding(new Insets(20)); 

        Label titulo = new Label("Proveedores");
        titulo.setFont(new Font("Segoe UI", 24));
        titulo.setStyle("-fx-text-fill: #0D5F56;"); 

        Node seccionAnadir = crearSeccionAnadir();
        Node seccionEditar = crearSeccionEditar();

        contenido.getChildren().addAll(titulo, seccionAnadir, seccionEditar);
        
        // Implementación del ScrollPane 
        ScrollPane scrollPane = new ScrollPane(contenido);
        scrollPane.setFitToWidth(true); 
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: white;");
        
        root.setCenter(scrollPane);
        
        return root;
    }

    //  Sección Añadir 
    private static Node crearSeccionAnadir() {
        VBox box = new VBox(15);

        Label subtitulo = new Label("Añadir");
        subtitulo.setFont(new Font("Segoe UI", 18));
        subtitulo.setStyle("-fx-text-fill: #0D5F56;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);

        Label lblNombre = new Label("Nombre:");
        Label lblCelular = new Label("Celular:");
        Label lblCorreo = new Label("Correo:");
        Label lblEmpresa = new Label("Empresa:");
        Label lblCargo = new Label("Cargo:");

        TextField tfNombre = new TextField();
        TextField tfCelular = new TextField();
        TextField tfCorreo = new TextField();
        TextField tfEmpresa = new TextField();
        TextField tfCargo = new TextField();

        tfNombre.setPromptText("Ej.: Juan Pérez");
        tfCelular.setPromptText("Ej.: 999888777");
        tfCorreo.setPromptText("Ej.: correo@dominio.com");
        tfEmpresa.setPromptText("Ej.: Laboratorios");
        tfCargo.setPromptText("Ej.: Ventas");

        // Estilo suave de inputs
        String estiloCampo = ""
                + "-fx-background-color: #F3F5F7;"
                + "-fx-background-radius: 10;"
                + "-fx-padding: 8 12;"
                + "-fx-border-color: transparent;"
                + "-fx-font-size: 13px;";
        tfNombre.setStyle(estiloCampo);
        tfCelular.setStyle(estiloCampo);
        tfCorreo.setStyle(estiloCampo);
        tfEmpresa.setStyle(estiloCampo);
        tfCargo.setStyle(estiloCampo);

        grid.add(lblNombre, 0, 0); grid.add(tfNombre, 1, 0);
        grid.add(lblCelular, 0, 1); grid.add(tfCelular, 1, 1);
        grid.add(lblCorreo, 0, 2); grid.add(tfCorreo, 1, 2);
        grid.add(lblEmpresa, 0, 3); grid.add(tfEmpresa, 1, 3);
        grid.add(lblCargo, 0, 4); grid.add(tfCargo, 1, 4);

        Button btnAgregar = new Button("Agregar Proveedor");
        btnAgregar.setStyle(
                "-fx-background-color: #1AA37A;" +
                "-fx-text-fill: white;" +
                "-fx-padding: 8 18;" +
                "-fx-background-radius: 20;" +
                "-fx-font-weight: bold;"
        );

        // Acción de agregar (se reutiliza para ENTER en los campos)
        Runnable onAgregarProveedor = () -> {
            String nombre = tfNombre.getText().trim();
            String celular = tfCelular.getText().trim();
            String correo = tfCorreo.getText().trim();
            String empresa = tfEmpresa.getText().trim();
            String cargo = tfCargo.getText().trim();

            // Validaciones mínimas
            if (nombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Nombre requerido", "Por favor ingresa el nombre.");
                return;
            }
            if (!celular.isEmpty() && !esSoloDigitos(celular)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Celular inválido", "El celular debe contener solo dígitos.");
                return;
            }
            if (!correo.isEmpty() && !esCorreoValido(correo)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Correo inválido", "Ingresa un correo con formato válido.");
                return;
            }

            // Añade nueva fila
            proveedores.add(new ProveedorFila(celular, nombre, correo, empresa, cargo));

            // Limpia y notifica
            tfNombre.clear(); tfCelular.clear(); tfCorreo.clear(); tfEmpresa.clear(); tfCargo.clear();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Proveedor registrado",
                    "El proveedor se ha agregado correctamente.");
        };

        btnAgregar.setOnAction(e -> onAgregarProveedor.run());

        // Enter en los campos dispara el agregar
        tfNombre.setOnAction(e -> onAgregarProveedor.run());
        tfCelular.setOnAction(e -> onAgregarProveedor.run());
        tfCorreo.setOnAction(e -> onAgregarProveedor.run());
        tfEmpresa.setOnAction(e -> onAgregarProveedor.run());
        tfCargo.setOnAction(e -> onAgregarProveedor.run());

        box.getChildren().addAll(subtitulo, grid, btnAgregar);
        return box;
    }

    // Sección Editar 
    private static Node crearSeccionEditar() {
        VBox box = new VBox(10);

        Label subtitulo = new Label("Editar");
        subtitulo.setFont(new Font("Segoe UI", 18));
        subtitulo.setStyle("-fx-text-fill: #0D5F56;");

        TableView<ProveedorFila> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setItems(proveedores);
        tabla.setEditable(true); 
        tabla.setStyle("-fx-background-color: white;");
        tabla.setPrefHeight(300); 

        // Columnas
        TableColumn<ProveedorFila, String> colCelular = new TableColumn<>("Celular");
        TableColumn<ProveedorFila, String> colNombre  = new TableColumn<>("Nombre");
        TableColumn<ProveedorFila, String> colCorreo  = new TableColumn<>("Correo");
        TableColumn<ProveedorFila, String> colEmpresa = new TableColumn<>("Empresa");
        TableColumn<ProveedorFila, String> colCargo   = new TableColumn<>("Cargo");

        colCelular.setCellValueFactory(data -> data.getValue().celularProperty());
        colNombre.setCellValueFactory (data -> data.getValue().nombreProperty());
        colCorreo.setCellValueFactory (data -> data.getValue().correoProperty());
        colEmpresa.setCellValueFactory(data -> data.getValue().empresaProperty());
        colCargo.setCellValueFactory  (data -> data.getValue().cargoProperty());

        // Hacer cada columna editable al hacer click 
        colCelular.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombre .setCellFactory(TextFieldTableCell.forTableColumn());
        colCorreo .setCellFactory(TextFieldTableCell.forTableColumn());
        colEmpresa.setCellFactory(TextFieldTableCell.forTableColumn());
        colCargo  .setCellFactory(TextFieldTableCell.forTableColumn());

        //  Guardar cambios 
        colCelular.setOnEditCommit(evt -> {
            ProveedorFila fila = evt.getRowValue();
            String nuevo = evt.getNewValue().trim();
            if (!nuevo.isEmpty() && !esSoloDigitos(nuevo)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Celular inválido", "Debe contener solo dígitos.");
               
                fila.setCelular(evt.getOldValue());
                tabla.refresh();
            } else {
                fila.setCelular(nuevo);
            }
        });

        colNombre.setOnEditCommit(evt -> {
            ProveedorFila fila = evt.getRowValue();
            String nuevo = evt.getNewValue().trim();
            if (nuevo.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Nombre requerido", "El nombre no puede estar vacío.");
                fila.setNombre(evt.getOldValue());
                tabla.refresh();
            } else {
                fila.setNombre(nuevo);
            }
        });

        colCorreo.setOnEditCommit(evt -> {
            ProveedorFila fila = evt.getRowValue();
            String nuevo = evt.getNewValue().trim();
            if (!nuevo.isEmpty() && !esCorreoValido(nuevo)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Correo inválido", "Usa un formato de correo válido.");
                fila.setCorreo(evt.getOldValue());
                tabla.refresh();
            } else {
                fila.setCorreo(nuevo);
            }
        });

        colEmpresa.setOnEditCommit(evt -> evt.getRowValue().setEmpresa(evt.getNewValue().trim()));
        colCargo  .setOnEditCommit(evt -> evt.getRowValue().setCargo(evt.getNewValue().trim()));

        // Columna Acciones (Borrar) 
        TableColumn<ProveedorFila, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(tc -> new TableCell<>() {
            private final Button btnBorrar = new Button("Borrar");
            private final HBox caja = new HBox(5);

            {
                caja.setAlignment(Pos.CENTER_LEFT);
                btnBorrar.setStyle("-fx-background-color: #E04A4A; -fx-text-fill: white; -fx-padding: 4 10; -fx-background-radius: 8;");
                btnBorrar.setOnAction(e -> {
                    // Verificación de seguridad
                    if (getTableView() != null && getIndex() < getTableView().getItems().size()) {
                        ProveedorFila fila = getTableView().getItems().get(getIndex());
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Confirmar borrado");
                        confirm.setHeaderText(null);
                        confirm.setContentText("¿Eliminar el proveedor \"" + fila.getNombre() + "\"?");
                        Optional<ButtonType> resp = confirm.showAndWait();
                        if (resp.isPresent() && resp.get() == ButtonType.OK) {
                            proveedores.remove(fila);
                        }
                    }
                });
                caja.getChildren().addAll(btnBorrar);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : caja);
            }
        });

        tabla.getColumns().addAll(colCelular, colNombre, colCorreo, colEmpresa, colCargo, colAcciones);

        box.getChildren().addAll(subtitulo, tabla);
        return box;
    }

    //  Modelo de fila 
    public static class ProveedorFila {
        private final SimpleStringProperty celular;
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty correo;
        private final SimpleStringProperty empresa;
        private final SimpleStringProperty cargo;

        public ProveedorFila(String celular, String nombre, String correo, String empresa, String cargo) {
            this.celular = new SimpleStringProperty(celular);
            this.nombre = new SimpleStringProperty(nombre);
            this.correo = new SimpleStringProperty(correo);
            this.empresa = new SimpleStringProperty(empresa);
            this.cargo = new SimpleStringProperty(cargo);
        }

        public SimpleStringProperty celularProperty() { return celular; }
        public SimpleStringProperty nombreProperty()  { return nombre; }
        public SimpleStringProperty correoProperty()  { return correo; }
        public SimpleStringProperty empresaProperty() { return empresa; }
        public SimpleStringProperty cargoProperty()   { return cargo; }

        public String getCelular() { return celular.get(); }
        public String getNombre()  { return nombre.get(); }
        public String getCorreo()  { return correo.get(); }
        public String getEmpresa() { return empresa.get(); }
        public String getCargo()   { return cargo.get(); }

        public void setCelular(String v) { celular.set(v); }
        public void setNombre (String v) { nombre.set(v); }
        public void setCorreo (String v) { correo.set(v); }
        public void setEmpresa(String v) { empresa.set(v); }
        public void setCargo  (String v) { cargo.set(v); }
    }

    //  Utilidades 
    private static boolean esSoloDigitos(String s) {
        return s.matches("\\d+");
    }

    private static boolean esCorreoValido(String email) {
        return email.matches("^[\\w.!#$%&'*+/=?^`{|}~-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

    private static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
