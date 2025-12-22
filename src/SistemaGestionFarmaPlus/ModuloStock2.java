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
import java.util.Scanner;

public class ModuloStock extends Application {
    private static TableView<StockFila> tabla;
    private static ObservableList<StockFila> masterData = FXCollections.observableArrayList();
    private static final String ARCHIVO = "productos.txt";

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(obtenerVista(), 950, 600));
        stage.setTitle("Control de Stock - FarmaPlus");
        stage.show();
    }

    public static Pane obtenerVista() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Encabezado con estilo FarmaPlus
        Label titulo = new Label("Panel de Control de Existencias");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#0D5F56"));

        HBox hbHerramientas = new HBox(15);
        hbHerramientas.setAlignment(Pos.CENTER_LEFT);

        Button btnRefrescar = new Button("🔄 Actualizar Datos");
        btnRefrescar.setStyle("-fx-background-color: #1AA37A; -fx-text-fill: white; -fx-background-radius: 20;");
        btnRefrescar.setOnAction(e -> cargarDatos());

        Label lblLeyenda = new Label("Estado: Rojo = Crítico (<5) | Amarillo = Bajo (<15)");
        lblLeyenda.setFont(Font.font("Segoe UI", 12));

        hbHerramientas.getChildren().addAll(btnRefrescar, lblLeyenda);

        cargarDatos(); 
        
        tabla = new TableView<>(masterData);
        tabla.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #cccccc;");
        configurarColumnas();

        root.getChildren().addAll(titulo, hbHerramientas, tabla);
        return root;
    }

    public static void cargarDatos() {
        masterData.clear();
        File f = new File(ARCHIVO);
        if (f.exists()) {
            try (Scanner sc = new Scanner(f)) {
                while (sc.hasNextLine()) {
                    String[] d = sc.nextLine().split("\\|");
                    if (d.length == 5) masterData.add(new StockFila(d[0], d[1], Integer.parseInt(d[4])));
                }
            } catch (Exception e) { e.printStackTrace(); }
        } else {
            // Si el archivo no existe, inicializa con datos de InfoTienda
            masterData.add(new StockFila(InfoTienda.producto0_nombre, InfoTienda.producto0_marca, InfoTienda.producto0_stock));
            guardar();
        }
    }

    public static void guardar() {
        // Leemos el archivo completo para no perder datos (precio, laboratorio) al actualizar solo stock
        List<String> lineas = new java.util.ArrayList<>();
        try (Scanner sc = new Scanner(new File(ARCHIVO))) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                String[] d = linea.split("\\|");
                for (StockFila s : masterData) {
                    if (s.getNombre().equals(d[0])) {
                        d[4] = String.valueOf(s.getStock());
                    }
                }
                lineas.add(String.join("|", d));
            }
        } catch (Exception e) {}

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (String l : lineas) pw.println(l);
        } catch (Exception e) {}
    }

    private static void configurarColumnas() {
        TableColumn<StockFila, String> colNom = new TableColumn<>("Medicamento");
        colNom.setCellValueFactory(d -> d.getValue().nombreProperty());

        TableColumn<StockFila, String> colMar = new TableColumn<>("Marca");
        colMar.setCellValueFactory(d -> d.getValue().marcaProperty());

        TableColumn<StockFila, Number> colSto = new TableColumn<>("Cantidad");
        colSto.setCellValueFactory(d -> d.getValue().stockProperty());
        
        // Formato condicional de celdas según stock
        colSto.setCellFactory(column -> new TableCell<StockFila, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    if (item.intValue() <= 5) {
                        setTextFill(Color.RED);
                        setStyle("-fx-font-weight: bold;");
                    } else if (item.intValue() <= 15) {
                        setTextFill(Color.web("#B8860B")); // DarkGoldenRod
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });

        TableColumn<StockFila, Void> colAcc = new TableColumn<>("Reposición");
        colAcc.setCellFactory(param -> new TableCell<>() {
            private final Button btnR = new Button("✚ Reponer");
            {
                btnR.setStyle("-fx-background-color: #0D5F56; -fx-text-fill: white; -fx-background-radius: 5;");
                btnR.setOnAction(e -> {
                    StockFila prod = getTableView().getItems().get(getIndex());
                    mostrarVentanaReposicion(prod);
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnR);
            }
        });

        tabla.getColumns().addAll(colNom, colMar, colSto, colAcc);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private static void mostrarVentanaReposicion(StockFila p) {
        Stage s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("Entrada de Inventario");

        VBox v = new VBox(15);
        v.setPadding(new Insets(20));
        v.setAlignment(Pos.CENTER);

        Label lblInfo = new Label("Producto: " + p.getNombre());
        lblInfo.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        TextField tfCant = new TextField();
        tfCant.setPromptText("Cantidad a sumar...");

        Button btnAdd = new Button("Confirmar Ingreso");
        btnAdd.setStyle("-fx-background-color: #1AA37A; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAdd.setMaxWidth(Double.MAX_VALUE);

        btnAdd.setOnAction(e -> {
            try {
                int nuevaCant = Integer.parseInt(tfCant.getText());
                if (nuevaCant <= 0) throw new Exception();
                p.setStock(p.getStock() + nuevaCant);
                guardar();
                tabla.refresh();
                s.close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Ingrese una cantidad entera positiva.").show();
            }
        });

        v.getChildren().addAll(lblInfo, new Label("Ingrese cantidad que ingresa a almacén:"), tfCant, btnAdd);
        s.setScene(new Scene(v, 300, 250));
        s.show();
    }

    public static class StockFila {
        private final SimpleStringProperty nombre, marca;
        private final SimpleIntegerProperty stock;

        public StockFila(String n, String m, int s) {
            this.nombre = new SimpleStringProperty(n);
            this.marca = new SimpleStringProperty(m);
            this.stock = new SimpleIntegerProperty(s);
        }
        public String getNombre() { return nombre.get(); }
        public int getStock() { return stock.get(); }
        public void setStock(int v) { stock.set(v); }
        public SimpleStringProperty nombreProperty() { return nombre; }
        public SimpleStringProperty marcaProperty() { return marca; }
        public SimpleIntegerProperty stockProperty() { return stock; }
    }
}

