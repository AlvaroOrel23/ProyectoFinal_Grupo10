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

public class ModuloProveedores extends Application {
    private static TableView<ProveedorFila> tabla;
    private static ObservableList<ProveedorFila> masterData = FXCollections.observableArrayList();
    private static final String ARCHIVO = "proveedores.txt";

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(obtenerVista(), 950, 600));
        stage.setTitle("Gestión de Proveedores - FarmaPlus");
        stage.show();
    }

    public static Pane obtenerVista() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Estilo de encabezado consistente con el sistema
        Label titulo = new Label("Panel de Gestión de Proveedores");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#0D5F56")); 

        Button btnNuevo = new Button("+ Registrar Proveedor");
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
                    if (d.length == 4) masterData.add(new ProveedorFila(d[0], d[1], d[2], d[3]));
                }
            } catch (Exception e) { e.printStackTrace(); }
        } else {
            // Datos iniciales de ejemplo
            masterData.add(new ProveedorFila("Droguería Alfa", "987654321", "alfa@farma.com", "Av. Industrial 123"));
            guardar();
        }
    }

    public static void guardar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (ProveedorFila p : masterData) {
                pw.println(p.getNombre()+"|"+p.getTelefono()+"|"+p.getCorreo()+"|"+p.getDireccion());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void configurarColumnas() {
        TableColumn<ProveedorFila, String> colNom = new TableColumn<>("Razón Social / Nombre");
        colNom.setCellValueFactory(d -> d.getValue().nombreProperty());

        TableColumn<ProveedorFila, String> colTel = new TableColumn<>("Teléfono");
        colTel.setCellValueFactory(d -> d.getValue().telefonoProperty());

        TableColumn<ProveedorFila, String> colCorreo = new TableColumn<>("Correo Electrónico");
        colCorreo.setCellValueFactory(d -> d.getValue().correoProperty());

        TableColumn<ProveedorFila, Void> colAcc = new TableColumn<>("Acciones");
        colAcc.setCellFactory(param -> new TableCell<>() {
            private final Button btnE = new Button("✎");
            private final Button btnB = new Button("🗑");
            private final HBox p = new HBox(10, btnE, btnB);
            {
                p.setAlignment(Pos.CENTER);
                btnE.setStyle("-fx-background-color: #0D5F56; -fx-text-fill: white; -fx-background-radius: 5;");
                btnB.setStyle("-fx-background-color: #E04A4A; -fx-text-fill: white; -fx-background-radius: 5;");
                
                btnE.setOnAction(e -> mostrarVentana(getTableView().getItems().get(getIndex())));
                
                // Validación de borrado solicitada
                btnB.setOnAction(e -> { 
                    ProveedorFila prov = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Eliminar Proveedor");
                    confirm.setHeaderText("¿Seguro que desea eliminar a este proveedor?");
                    confirm.setContentText("Empresa: " + prov.getNombre());
                    
                    Optional<ButtonType> res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.OK) {
                        masterData.remove(prov); 
                        guardar(); 
                    }
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : p);
            }
        });
        
        tabla.getColumns().addAll(colNom, colTel, colCorreo, colAcc);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private static void mostrarVentana(ProveedorFila p) {
        Stage s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle(p == null ? "Nuevo Proveedor" : "Editar Proveedor");

        VBox v = new VBox(10);
        v.setPadding(new Insets(20));
        v.setStyle("-fx-background-color: white;");

        TextField tfN = new TextField(p == null ? "" : p.getNombre());
        tfN.setPromptText("Nombre de la empresa");
        TextField tfT = new TextField(p == null ? "" : p.getTelefono());
        tfT.setPromptText("Teléfono de contacto");
        TextField tfC = new TextField(p == null ? "" : p.getCorreo());
        tfC.setPromptText("Correo (ejemplo@mail.com)");
        TextField tfD = new TextField(p == null ? "" : p.getDireccion());
        tfD.setPromptText("Dirección fiscal");

        Button btn = new Button(p == null ? "Registrar" : "Actualizar");
        btn.setStyle("-fx-background-color: #1AA37A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        btn.setMaxWidth(Double.MAX_VALUE);
        
        btn.setOnAction(e -> {
            if (tfN.getText().isEmpty() || tfT.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Nombre y Teléfono son obligatorios.").show();
                return;
            }
            if (p == null) {
                masterData.add(new ProveedorFila(tfN.getText(), tfT.getText(), tfC.getText(), tfD.getText()));
            } else {
                p.setNombre(tfN.getText()); p.setTelefono(tfT.getText());
                p.setCorreo(tfC.getText()); p.setDireccion(tfD.getText());
                tabla.refresh();
            }
            guardar(); s.close();
        });

        v.getChildren().addAll(new Label("Razón Social:"), tfN, new Label("Teléfono:"), tfT, 
                             new Label("Correo:"), tfC, new Label("Dirección:"), tfD, btn);
        s.setScene(new Scene(v, 320, 480)); 
        s.show();
    }

    public static class ProveedorFila {
        private final SimpleStringProperty nombre, telefono, correo, direccion;
        public ProveedorFila(String n, String t, String c, String d) {
            this.nombre = new SimpleStringProperty(n); this.telefono = new SimpleStringProperty(t);
            this.correo = new SimpleStringProperty(c); this.direccion = new SimpleStringProperty(d);
        }
        public String getNombre() { return nombre.get(); }
        public void setNombre(String v) { nombre.set(v); }
        public String getTelefono() { return telefono.get(); }
        public void setTelefono(String v) { telefono.set(v); }
        public String getCorreo() { return correo.get(); }
        public void setCorreo(String v) { correo.set(v); }
        public String getDireccion() { return direccion.get(); }
        public void setDireccion(String v) { direccion.set(v); }
        
        public SimpleStringProperty nombreProperty() { return nombre; }
        public SimpleStringProperty telefonoProperty() { return telefono; }
        public SimpleStringProperty correoProperty() { return correo; }
    }
}
