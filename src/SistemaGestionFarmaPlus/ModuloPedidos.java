package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ModuloPedidos extends Application {

    // Metodo Main para prueba independiente 
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = obtenerVista();
        
        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setTitle("Sistema FarmaPlus - Módulo de Pedidos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Variables de interfaz
    private static GridPane tablaProductos;
    private static VBox contenidoCarrito;
    private static Label lblTotalPagar;
    private static TextField txtBuscar;
    
    // Variable local para el pedido actual
    private static double totalPedidoActual = 0.0;

  

    private static Label crearEtiquetaEstado(String texto, Color colorFondo) {
        Label estado = new Label(texto);
        estado.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        estado.setTextFill(Color.BLACK);
        estado.setPadding(new Insets(3, 8, 3, 8));
        estado.setBackground(new Background(new BackgroundFill(colorFondo, new CornerRadii(3), Insets.EMPTY)));
        return estado;
    }

    private static HBox crearHeaderTarjeta(String titulo) {
        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        Rectangle rectIcon = new Rectangle(20, 20, Color.BLACK);
        HBox header = new HBox(10, rectIcon, lblTitulo);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 10, 0));
        return header;
    }

    private static Label crearHeaderColumna(String texto) {
        Label l = new Label(texto);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        l.setTextFill(Color.DARKGRAY);
        return l;
    }

    // Vista Principal 

    public static Pane obtenerVista() {
        totalPedidoActual = 0.0;

        BorderPane vistaPrincipal = new BorderPane();
        vistaPrincipal.setPadding(new Insets(15));
        vistaPrincipal.setStyle("-fx-background-color: #f0f0f0;");

        // 1. TOP: Barra de búsqueda
        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por nombre (ej. Ibuprofeno)...");
        txtBuscar.setPrefHeight(40);
        txtBuscar.setStyle("-fx-background-radius: 5; -fx-border-color: #ccc; -fx-border-radius: 5;");

        Button btnBuscar = new Button("\uD83D\uDD0D");
        btnBuscar.setFont(new Font("Arial", 18));
        btnBuscar.setStyle("-fx-background-color: transparent;");

        HBox barraBusqueda = new HBox(txtBuscar, btnBuscar);
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);
        barraBusqueda.setAlignment(Pos.CENTER);
        barraBusqueda.setPadding(new Insets(0, 0, 15, 0));

        VBox zonaSuperior = new VBox(barraBusqueda);
        zonaSuperior.setStyle("-fx-border-color: #ccc; -fx-border-width: 0 0 1 0;");
        vistaPrincipal.setTop(zonaSuperior);

        // 2. RIGHT: Carrito de Compras
        VBox carrito = new VBox(15);
        carrito.setPrefWidth(280);
        carrito.setPadding(new Insets(20));
        carrito.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label lblCarrito = new Label("Carrito de Compras");
        lblCarrito.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        contenidoCarrito = new VBox(10);
        contenidoCarrito.setAlignment(Pos.TOP_CENTER);
        contenidoCarrito.setPrefHeight(300);
        contenidoCarrito.getChildren().add(new Label("El carrito está vacío"));

        Label lblTotal = new Label("Total:");
        lblTotalPagar = new Label("S/ 0.00");
        lblTotalPagar.setTextFill(Color.web("#009900"));
        lblTotalPagar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        HBox hboxTotal = new HBox(lblTotal, lblTotalPagar);
        hboxTotal.setAlignment(Pos.CENTER_RIGHT);
        hboxTotal.setSpacing(10);

        Button btnProcesar = new Button("Confirmar Venta");
        btnProcesar.setPrefWidth(Double.MAX_VALUE);
        btnProcesar.setStyle("-fx-background-color: #009900; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 5;");

        Button btnVaciar = new Button("Cancelar / Limpiar");
        btnVaciar.setPrefWidth(Double.MAX_VALUE);
        btnVaciar.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 5;");

        // Acción Vaciar Carrito 
        btnVaciar.setOnAction(e -> {
            reiniciarCarrito();
            mostrarAlerta("Carrito Limpiado", "Se ha vaciado la lista visual.");
        });

        // Acción Procesar (Conexión con InfoTienda)
        btnProcesar.setOnAction(e -> {
            if (totalPedidoActual > 0) {
                // Actualizamos las variables en InfoTienda
                InfoTienda.contadorVentas++;
                InfoTienda.acumuladoTotalGeneral += totalPedidoActual;
                
                mostrarAlerta("Venta Exitosa", 
                    "Venta #" + InfoTienda.contadorVentas + " registrada.\n" +
                    "Monto: S/ " + String.format("%.2f", totalPedidoActual) + "\n\n" +
                    "Acumulado del día: S/ " + String.format("%.2f", InfoTienda.acumuladoTotalGeneral)
                );
                reiniciarCarrito();
            } else {
                mostrarAlerta("Carrito Vacío", "Agrega productos antes de procesar.");
            }
        });

        carrito.getChildren().addAll(lblCarrito, contenidoCarrito, hboxTotal, btnProcesar, btnVaciar);
        vistaPrincipal.setRight(carrito);

        // CENTER: Lista de Productos
        VBox zonaCentral = new VBox(15);
        VBox listaProductosCard = new VBox(10);
        listaProductosCard.setPadding(new Insets(20));
        listaProductosCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        listaProductosCard.getChildren().add(crearHeaderTarjeta("Catálogo de Medicamentos"));

        tablaProductos = new GridPane();
        tablaProductos.setHgap(20);
        tablaProductos.setVgap(15);
        tablaProductos.setPadding(new Insets(10));
        tablaProductos.setStyle("-fx-border-color: #ccc; -fx-border-width: 1 0 0 0; -fx-padding: 15 0 0 0;");

        llenarTabla(""); 

        listaProductosCard.getChildren().add(tablaProductos);
        VBox.setVgrow(listaProductosCard, Priority.ALWAYS);
        zonaCentral.getChildren().add(listaProductosCard);

        // Envolver en ScrollPane
        ScrollPane scrollPane = new ScrollPane(zonaCentral);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #f0f0f0;");
        scrollPane.setPadding(new Insets(0, 10, 0, 0));

        vistaPrincipal.setCenter(scrollPane);

        // Listener de búsqueda
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            llenarTabla(newValue);
        });

        return vistaPrincipal;
    }

    private static void reiniciarCarrito() {
        totalPedidoActual = 0.0;
        lblTotalPagar.setText("S/ 0.00");
        contenidoCarrito.getChildren().clear();
        contenidoCarrito.getChildren().add(new Label("El carrito está vacío"));
    }

    // Método para llenar la tabla leyendo de InfoTienda
    private static void llenarTabla(String filtro) {
        tablaProductos.getChildren().clear();

        // Encabezados
        tablaProductos.add(crearHeaderColumna("Producto / Lab"), 0, 0);
        tablaProductos.add(crearHeaderColumna("Marca"), 1, 0);
        tablaProductos.add(crearHeaderColumna("Stock"), 2, 0);
        tablaProductos.add(crearHeaderColumna("Precio"), 3, 0);
        tablaProductos.add(crearHeaderColumna("Estado"), 4, 0);

        String texto = filtro.toLowerCase();
        int fila = 1;

        // Lectura de datos desde InfoTienda
        
        // Producto 0
        if (filtro.isEmpty() || InfoTienda.producto0_nombre.toLowerCase().contains(texto)) {
            agregarFila(0, InfoTienda.producto0_nombre, InfoTienda.producto0_laboratorio, 
                       InfoTienda.producto0_marca, InfoTienda.producto0_stock, InfoTienda.producto0_precio, fila++);
        }
        // Producto 1
        if (filtro.isEmpty() || InfoTienda.producto1_nombre.toLowerCase().contains(texto)) {
            agregarFila(1, InfoTienda.producto1_nombre, InfoTienda.producto1_laboratorio, 
                       InfoTienda.producto1_marca, InfoTienda.producto1_stock, InfoTienda.producto1_precio, fila++);
        }
        // Producto 2
        if (filtro.isEmpty() || InfoTienda.producto2_nombre.toLowerCase().contains(texto)) {
            agregarFila(2, InfoTienda.producto2_nombre, InfoTienda.producto2_laboratorio, 
                       InfoTienda.producto2_marca, InfoTienda.producto2_stock, InfoTienda.producto2_precio, fila++);
        }
        // Producto 3
        if (filtro.isEmpty() || InfoTienda.producto3_nombre.toLowerCase().contains(texto)) {
            agregarFila(3, InfoTienda.producto3_nombre, InfoTienda.producto3_laboratorio, 
                       InfoTienda.producto3_marca, InfoTienda.producto3_stock, InfoTienda.producto3_precio, fila++);
        }
        // Producto 4
        if (filtro.isEmpty() || InfoTienda.producto4_nombre.toLowerCase().contains(texto)) {
            agregarFila(4, InfoTienda.producto4_nombre, InfoTienda.producto4_laboratorio, 
                       InfoTienda.producto4_marca, InfoTienda.producto4_stock, InfoTienda.producto4_precio, fila++);
        }

        if (fila == 1) {
            Label lbl = new Label("No se encontraron coincidencias.");
            lbl.setTextFill(Color.RED);
            tablaProductos.add(lbl, 0, 1, 3, 1);
        }
    }

    private static void agregarFila(int id, String nombre, String lab, String marca, int stock, double precio, int fila) {
        // Columna 1
        VBox boxNombre = new VBox(2);
        Label lblNombre = new Label(nombre);
        lblNombre.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        Label lblLab = new Label(lab);
        lblLab.setFont(Font.font("Segoe UI", 11));
        lblLab.setTextFill(Color.GRAY);
        boxNombre.getChildren().addAll(lblNombre, lblLab);
        tablaProductos.add(boxNombre, 0, fila);

        // Columna 2
        tablaProductos.add(new Label(marca), 1, fila);

        // Columna 3
        tablaProductos.add(new Label(stock + " unid."), 2, fila);

        // Columna 4
        tablaProductos.add(new Label("S/ " + String.format("%.2f", precio)), 3, fila);

        // Columna 5
        String txtEstado;
        Color colorBg;
        boolean activo = true;

        if (stock <= 0) {
            txtEstado = "Agotado";
            colorBg = Color.LIGHTGRAY;
            activo = false;
        } else if (stock <= 5) {
            txtEstado = "Crítico";
            colorBg = Color.web("#ffcccc");
        } else if (stock <= 15) {
            txtEstado = "Bajo";
            colorBg = Color.web("#fff4cc");
        } else {
            txtEstado = "Disponible";
            colorBg = Color.web("#ccffcc");
        }
        tablaProductos.add(crearEtiquetaEstado(txtEstado, colorBg), 4, fila);

        // Botón Agregar
        Button btn = new Button(activo ? "Agregar" : "—");
        if (activo) {
            btn.setStyle("-fx-background-color: #1AA37A; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
            btn.setOnAction(e -> {
                // MODIFICACIÓN DEL STOCK EN LA CLASE EXTERNA INFO TIENDA
                switch(id) {
                    case 0: InfoTienda.producto0_stock--; break;
                    case 1: InfoTienda.producto1_stock--; break;
                    case 2: InfoTienda.producto2_stock--; break;
                    case 3: InfoTienda.producto3_stock--; break;
                    case 4: InfoTienda.producto4_stock--; break;
                }

                if (totalPedidoActual == 0) contenidoCarrito.getChildren().clear();
                
                HBox itemCarro = new HBox(10);
                itemCarro.getChildren().addAll(
                    new Label("1x"),
                    new Label(marca.length() > 10 ? marca.substring(0,10)+"..." : marca),
                    new Label("S/ " + precio)
                );
                contenidoCarrito.getChildren().add(itemCarro);

                totalPedidoActual += precio;
                lblTotalPagar.setText("S/ " + String.format("%.2f", totalPedidoActual));

                // Recargar tabla para reflejar el cambio de stock
                llenarTabla(txtBuscar.getText());
            });
        } else {
            btn.setStyle("-fx-background-color: #eee; -fx-text-fill: #999; -fx-background-radius: 5;");
            btn.setDisable(true);
        }
        tablaProductos.add(btn, 5, fila);
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}