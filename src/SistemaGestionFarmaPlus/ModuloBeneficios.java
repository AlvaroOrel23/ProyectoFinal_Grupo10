package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; 
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ModuloBeneficios extends Application {

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

        // Panel Izquierdo - Perfil del Cliente
        VBox panelLateral = crearPanelPerfil();
        layoutPrincipal.setLeft(panelLateral);

        // Panel Central - Tabla de Historial y KPIs
        VBox panelCentral = crearPanelContenido();
        layoutPrincipal.setCenter(panelCentral);

        return layoutPrincipal;
    }

    // SEECIÓN: PERFIL DEL CLIENTE (IZQUIERDA)
    private static VBox crearPanelPerfil() {
        VBox cardPerfil = new VBox(15);
        cardPerfil.setPadding(new Insets(30));
        cardPerfil.setAlignment(Pos.TOP_CENTER);
        cardPerfil.setPrefWidth(300);
        cardPerfil.setStyle("-fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // BARRA DE BÚSQUEDA 
        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar cliente (DNI/Nombre)...");
        txtBuscar.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 20; -fx-border-color: transparent; -fx-padding: 8;");
        
        Button btnBuscar = new Button("🔍");
        btnBuscar.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand;");
        
        HBox cajaBusqueda = new HBox(5, txtBuscar, btnBuscar);
        cajaBusqueda.setAlignment(Pos.CENTER);
        cajaBusqueda.setPadding(new Insets(0, 0, 15, 0)); // Espacio inferior para separar del perfil
        HBox.setHgrow(txtBuscar, Priority.ALWAYS); // Que el texto ocupe el espacio disponible
        // --------------------------------

        ImageView fotoPerfil;
        try {
            fotoPerfil = new ImageView(new Image(ModuloPedidos.class.getResourceAsStream("/Iconos/User.jpg")));
        } catch (Exception e) {
            fotoPerfil = new ImageView(new Image("https://cdn-icons-png.flaticon.com/512/3135/3135715.png"));
        }

        fotoPerfil.setFitHeight(120);
        fotoPerfil.setFitWidth(120);
        
        Circle clip = new Circle(60, 60, 60);
        fotoPerfil.setClip(clip);

        Label lblNombre = new Label("Alvaro Giraldo");
        lblNombre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblRol = new Label("Cliente Preferente");
        lblRol.setStyle("-fx-font-size: 14px; -fx-text-fill: #757575;");

        Label lblDni = new Label("DNI: 935122219");
        lblDni.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-padding: 10 0 0 0;");
        
        VBox boxFidelidad = new VBox(5);
        Label lblFidelidad = new Label("Nivel de Fidelidad (Oro)");
        lblFidelidad.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");
        HBox barraFondo = new HBox();
        barraFondo.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 5; -fx-pref-height: 8;");
        HBox barraProgreso = new HBox();
        barraProgreso.setStyle("-fx-background-color: #2E7D32; -fx-background-radius: 5; -fx-pref-width: 180;");
        barraFondo.getChildren().add(barraProgreso);
        boxFidelidad.getChildren().addAll(lblFidelidad, barraFondo);
        boxFidelidad.setPadding(new Insets(20, 0, 0, 0));

        // Se agrega CajaBusqueda
        cardPerfil.getChildren().addAll(cajaBusqueda, fotoPerfil, lblNombre, lblRol, lblDni, boxFidelidad);
        return cardPerfil;
    }

    // SECCIÓN 2: CONTENIDO CENTRAL -
    private static VBox crearPanelContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(30));
        contenedor.setStyle("-fx-background-color: #F4F6F8;");

        HBox kpiContainer = new HBox(20);
        kpiContainer.getChildren().addAll(
            crearTarjetaKPI("Total Gastado", "S/. 1,240.50", "#1976D2"),
            crearTarjetaKPI("Última Compra", "14 Dic 2025", "#388E3C"),
            crearTarjetaKPI("Puntos Acumulados", "450 pts", "#F57C00")
        );

        Label lblTituloTabla = new Label("Historial de Compras Recientes");
        lblTituloTabla.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        TableView<Compra> tabla = crearTablaHistorial();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        contenedor.getChildren().addAll(kpiContainer, lblTituloTabla, tabla);
        return contenedor;
    }

    private static HBox crearTarjetaKPI(String titulo, String valor, String colorBorde) {
        HBox tarjeta = new HBox();
        tarjeta.setPadding(new Insets(15, 25, 15, 25));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2); -fx-border-color: " + colorBorde + "; -fx-border-width: 0 0 0 4;");
        
        VBox contenido = new VBox(5);
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-text-fill: #757575; -fx-font-size: 12px;");
        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-text-fill: #333333; -fx-font-size: 20px; -fx-font-weight: bold;");
        
        contenido.getChildren().addAll(lblTitulo, lblValor);
        tarjeta.getChildren().add(contenido);
        tarjeta.setPrefWidth(250);
        return tarjeta;
    }

    // CONFIGURACIÓN DE LA TABLA 
    private static TableView<Compra> crearTablaHistorial() {
        TableView<Compra> table = new TableView<>();
        
        TableColumn<Compra, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        colProducto.setPrefWidth(300);

        TableColumn<Compra, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setPrefWidth(120);

        TableColumn<Compra, String> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        
        TableColumn<Compra, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Compra, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        table.getColumns().addAll(colProducto, colFecha, colCantidad, colTotal, colEstado);

        ObservableList<Compra> datos = FXCollections.observableArrayList(
            new Compra("Amoxicilina 1g", "10/11/2025", "1 caja", "S/. 25.00", "Entregado"),
            new Compra("Salbutamol Inhalador", "02/12/2025", "1 un.", "S/. 18.50", "Entregado"),
            new Compra("Metformina 850mg", "25/11/2025", "1 caja", "S/. 12.00", "Entregado"),
            new Compra("Jarabe Tos (Miel)", "10/12/2025", "1 frasco", "S/. 35.00", "Pendiente"),
            new Compra("Naproxeno 550mg", "12/12/2025", "20 un.", "S/. 8.00", "Entregado"),
            new Compra("Clorfenamina 4mg", "14/12/2025", "1 blister", "S/. 2.50", "Cancelado")
        );
        table.setItems(datos);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return table;
    }

    //  MODELO DE DATOS 
    public static class Compra {
        private String producto;
        private String fecha;
        private String cantidad;
        private String total;
        private String estado;

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
        return 
            ".table-view { -fx-background-color: transparent; -fx-border-color: transparent; }" +
            ".table-view .column-header-background { -fx-background-color: transparent; }" +
            ".table-view .column-header { -fx-background-color: transparent; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0; -fx-padding: 10px; }" +
            ".table-view .column-header .label { -fx-text-fill: #9E9E9E; -fx-font-weight: bold; }" +
            ".table-row-cell { -fx-background-color: #FFFFFF; -fx-border-width: 0 0 1 0; -fx-border-color: #F4F6F8; -fx-padding: 5px 0; }" +
            ".table-row-cell:hover { -fx-background-color: #F9FAFB; }" +
            ".table-cell { -fx-text-fill: #333333; -fx-alignment: CENTER-LEFT; -fx-font-size: 14px; -fx-padding: 0 0 0 15px; }";
    }

    public static void main(String[] args) {
        launch(args);
    }
}