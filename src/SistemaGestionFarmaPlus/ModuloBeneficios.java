package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class ModuloBeneficios extends Application {
	
	static Label lblNombre = new Label("Nombre del Cliente");
	static Label lblDNI = new Label("DNI");
    
    // VARIABLES PARA ACTUALIZACIÓN DINÁMICA
    private static TableView<Compra> tablaHistorial;
    private static Label lblTotalVal, lblFechaVal, lblPuntosVal;

    @Override
    public void start(Stage stage) {
        BorderPane vista = obtenerVista();
        Scene scene = new Scene(vista, 1100, 700);
        scene.getStylesheets().add("data:text/css," + obtenerEstilosCSS());

        stage.setTitle("Sistema FarmaPlus - Gestión de Clientes 360°");
        stage.setScene(scene);
        stage.show();
    }

    public static BorderPane obtenerVista() {
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setStyle("-fx-background-color: #F4F6F8;");

        layoutPrincipal.setLeft(crearPanelPerfil());
        layoutPrincipal.setCenter(crearPanelContenido());

        return layoutPrincipal;
    }

    private static VBox crearPanelPerfil() {
        VBox cardPerfil = new VBox(15);
        cardPerfil.setPadding(new Insets(30));
        cardPerfil.setAlignment(Pos.TOP_CENTER);
        cardPerfil.setPrefWidth(300);
        cardPerfil.setStyle("-fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar cliente (DNI)...");
        txtBuscar.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 20; -fx-padding: 8;");
        
        Button btnBuscar = new Button("🔍");
        btnBuscar.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand;");
        
        btnBuscar.setOnAction (e -> {
            String dni = txtBuscar.getText().trim();
            if(AgregarClientes.existeCliente(dni)) {
                AgregarClientes perfil = AgregarClientes.buscarPorDNI(dni);
                lblNombre.setText(perfil.Nombre + " " + perfil.Apellidos);
                lblDNI.setText("DNI: " + perfil.DNI);
                
                // ACTUALIZACIÓN DE TABLA Y KPIs
                actualizarDatosCliente(dni);
            } else {
                lblNombre.setText("No registrado");
                lblDNI.setText("");
                tablaHistorial.setItems(FXCollections.observableArrayList());
            }
        });
        
        HBox cajaBusqueda = new HBox(5, txtBuscar, btnBuscar);
        cajaBusqueda.setAlignment(Pos.CENTER);
        cajaBusqueda.setPadding(new Insets(0, 0, 15, 0));
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);

        ImageView fotoPerfil;
        try {
            fotoPerfil = new ImageView(new Image(ModuloBeneficios.class.getResourceAsStream("/Iconos/User.jpg")));
        } catch (Exception e) {
            fotoPerfil = new ImageView(new Image("https://cdn-icons-png.flaticon.com/512/3135/3135715.png"));
        }
        fotoPerfil.setFitHeight(120);
        fotoPerfil.setFitWidth(120);
        fotoPerfil.setClip(new Circle(60, 60, 60));
       
        cardPerfil.getChildren().addAll(cajaBusqueda, fotoPerfil, lblNombre, lblDNI);
        return cardPerfil;
    }

    private static VBox crearPanelContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(30));

        HBox kpiContainer = new HBox(20);
        
        // Inicializamos las labels de los KPIs para poder editarlas luego
        lblTotalVal = new Label("S/. 0.00");
        lblFechaVal = new Label("-");
        lblPuntosVal = new Label("0 pts");

        kpiContainer.getChildren().addAll(
            crearTarjetaKPI("Total Gastado", lblTotalVal, "#1976D2"),
            crearTarjetaKPI("Última Compra", lblFechaVal, "#388E3C"),
            crearTarjetaKPI("Puntos Acumulados", lblPuntosVal, "#F57C00")
        );

        Label lblTituloTabla = new Label("Historial de Compras Recientes");
        lblTituloTabla.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tablaHistorial = crearTablaHistorial();
        VBox.setVgrow(tablaHistorial, Priority.ALWAYS);

        contenedor.getChildren().addAll(kpiContainer, lblTituloTabla, tablaHistorial);
        return contenedor;
    }

    private static void actualizarDatosCliente(String dni) {
        ObservableList<Compra> historial = FXCollections.observableArrayList();
        double totalGastado = 0;
        String ultimaFecha = "Sin compras";

        try (Scanner sc = new Scanner(new File("ventas.txt"))) {
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(" \\| ");
                if (partes.length >= 4 && partes[1].trim().equals(dni)) {
                    String fecha = partes[0];
                    String productos = partes[2];
                    String totalStr = partes[3].replace("S/ ", "").replace(",", "");
                    double total = Double.parseDouble(totalStr);

                    historial.add(new Compra(productos, fecha, "Ver detalle", "S/. " + totalStr, "Entregado"));
                    totalGastado += total;
                    ultimaFecha = fecha.split(" ")[0]; // Solo fecha, sin hora
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer historial");
        }

        // Actualizar Tabla
        tablaHistorial.setItems(historial);
        
        // Actualizar KPIs
        lblTotalVal.setText(String.format("S/. %.2f", totalGastado));
        lblFechaVal.setText(ultimaFecha);
        lblPuntosVal.setText((int)(totalGastado / 10) + " pts"); // 1 punto por cada 10 soles
    }

    private static HBox crearTarjetaKPI(String titulo, Label lblValor, String colorBorde) {
        HBox tarjeta = new HBox();
        tarjeta.setPadding(new Insets(15, 25, 15, 25));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: " + colorBorde + "; -fx-border-width: 0 0 0 4;");
        
        VBox contenido = new VBox(5);
        Label lblT = new Label(titulo);
        lblT.setStyle("-fx-text-fill: #757575; -fx-font-size: 12px;");
        lblValor.setStyle("-fx-text-fill: #333333; -fx-font-size: 20px; -fx-font-weight: bold;");
        
        contenido.getChildren().addAll(lblT, lblValor);
        tarjeta.getChildren().add(contenido);
        tarjeta.setPrefWidth(250);
        return tarjeta;
    }

    private static TableView<Compra> crearTablaHistorial() {
        TableView<Compra> table = new TableView<>();
        
        TableColumn<Compra, String> colProducto = new TableColumn<>("Detalle Productos");
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        colProducto.setPrefWidth(300);

        TableColumn<Compra, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Compra, String> colTotal = new TableColumn<>("Monto Pagado");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        table.getColumns().addAll(colProducto, colFecha, colTotal);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    // MODELO DE DATOS
    public static class Compra {
        private String producto, fecha, cantidad, total, estado;
        public Compra(String p, String f, String c, String t, String e) {
            this.producto = p; this.fecha = f; this.cantidad = c; this.total = t; this.estado = e;
        }
        public String getProducto() { return producto; }
        public String getFecha() { return fecha; }
        public String getCantidad() { return cantidad; }
        public String getTotal() { return total; }
        public String getEstado() { return estado; }
    }

    private String obtenerEstilosCSS() {
        return ".table-view { -fx-background-color: transparent; }" +
               ".table-row-cell:hover { -fx-background-color: #F9FAFB; }" +
               ".table-cell { -fx-padding: 10px; }";
    }

    public static void main(String[] args) { launch(args); }
}
