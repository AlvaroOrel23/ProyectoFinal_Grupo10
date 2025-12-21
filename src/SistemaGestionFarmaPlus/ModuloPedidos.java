package SistemaGestionFarmaPlus;

import java.util.HashMap;
import java.util.Map;
import java.io.File; 
import java.util.Scanner; 
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

    // Variables de interfaz
    private static GridPane tablaProductos;
    private static VBox contenidoCarrito;
    private static Label lblTotalPagar;
    private static TextField txtBuscar;

    // Estructuras de datos
    private static double totalPedidoActual = 0.0;
    private static Map<Integer, ItemCarrito> mapaCarrito = new HashMap<>();

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

    // --- MÉTODOS DE APOYO UI ---

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

    // --- VISTA PRINCIPAL ---

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

        // Acción Procesar
        btnProcesar.setOnAction(e -> {
            if (mapaCarrito.isEmpty()) {
                mostrarAlerta("Carrito Vacío", "Agregue productos antes de procesar.");
                return;
            }

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Finalizar Venta");
            dialog.setHeaderText("Verificación de Cliente");
            dialog.setContentText("Ingrese el DNI del cliente para validar registro:");

            dialog.showAndWait().ifPresent(dniInput -> {
                String dniProcesar = dniInput.trim();

                if (dniProcesar.isEmpty()) {
                    mostrarAlerta("Error", "Debe ingresar un DNI válido para continuar.");
                    return;
                }

                // --- VALIDACIÓN CON EL ARCHIVO Clientes.txt ---
                boolean registrado = validarClienteEnArchivo(dniProcesar);
                
                if (!registrado) {
                    // Si no está en el archivo, forzamos el código de "No Registrado"
                    mostrarAlerta("Cliente No Encontrado", 
                        "El DNI " + dniProcesar + " no figura en Clientes.txt.\n" +
                        "Se procesará como cliente genérico (999999).");
                    dniProcesar = "999999";
                }
                
            
                double porcentajeDesc = dniProcesar.equals("999999") ? 0.0 : GestionDescuentos.calcularDescuentoPorHistorial(dniProcesar);
               
                double montoDescuento = totalPedidoActual * porcentajeDesc;
                double totalFinal = totalPedidoActual - montoDescuento;

                // Variable final para usar en el lambda
                final String dniFinal = dniProcesar; 

                String resumen = String.format(
                        "Resumen de Venta\n"
                        + "----------------------------\n"
                        + "Cliente: %s\n" // Mostramos DNI o 999999
                        + "Subtotal: S/ %.2f\n"
                        + "Descuento (%d%%): S/ %.2f\n"
                        + "----------------------------\n"
                        + "TOTAL A PAGAR: S/ %.2f",
                        dniFinal, totalPedidoActual, (int) (porcentajeDesc * 100), montoDescuento, totalFinal);

                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar Transacción");
                confirmacion.setHeaderText(null);
                confirmacion.setContentText(resumen);

                confirmacion.showAndWait().ifPresent(respuesta -> {
                    if (respuesta == ButtonType.OK) {
                        guardarVentaEnArchivo(dniFinal, totalFinal);
                        InfoTienda.contadorVentas++;
                        InfoTienda.acumuladoTotalGeneral += totalFinal;

                        reiniciarCarrito();
                        mostrarAlerta("Venta Exitosa", "La venta ha sido registrada correctamente.");
                    }
                });
            });
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

        ScrollPane scrollPane = new ScrollPane(zonaCentral);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #f0f0f0;");
        scrollPane.setPadding(new Insets(0, 10, 0, 0));
        vistaPrincipal.setCenter(scrollPane);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            llenarTabla(newValue);
        });

        return vistaPrincipal;
    }

    private static void reiniciarCarrito() {
        mapaCarrito.clear();
        totalPedidoActual = 0.0;
        lblTotalPagar.setText("S/ 0.00");
        contenidoCarrito.getChildren().clear();
        contenidoCarrito.getChildren().add(new Label("El carrito está vacío"));
    }

    private static void llenarTabla(String filtro) {
        tablaProductos.getChildren().clear();
        tablaProductos.add(crearHeaderColumna("Producto / Lab"), 0, 0);
        tablaProductos.add(crearHeaderColumna("Marca"), 1, 0);
        tablaProductos.add(crearHeaderColumna("Stock"), 2, 0);
        tablaProductos.add(crearHeaderColumna("Precio"), 3, 0);
        tablaProductos.add(crearHeaderColumna("Estado"), 4, 0);

        String texto = filtro.toLowerCase();
        int fila = 1;

        if (filtro.isEmpty() || InfoTienda.producto0_nombre.toLowerCase().contains(texto)) {
            agregarFila(0, InfoTienda.producto0_nombre, InfoTienda.producto0_laboratorio, InfoTienda.producto0_marca, InfoTienda.producto0_stock, InfoTienda.producto0_precio, fila++);
        }
        if (filtro.isEmpty() || InfoTienda.producto1_nombre.toLowerCase().contains(texto)) {
            agregarFila(1, InfoTienda.producto1_nombre, InfoTienda.producto1_laboratorio, InfoTienda.producto1_marca, InfoTienda.producto1_stock, InfoTienda.producto1_precio, fila++);
        }
        if (filtro.isEmpty() || InfoTienda.producto2_nombre.toLowerCase().contains(texto)) {
            agregarFila(2, InfoTienda.producto2_nombre, InfoTienda.producto2_laboratorio, InfoTienda.producto2_marca, InfoTienda.producto2_stock, InfoTienda.producto2_precio, fila++);
        }
        if (filtro.isEmpty() || InfoTienda.producto3_nombre.toLowerCase().contains(texto)) {
            agregarFila(3, InfoTienda.producto3_nombre, InfoTienda.producto3_laboratorio, InfoTienda.producto3_marca, InfoTienda.producto3_stock, InfoTienda.producto3_precio, fila++);
        }
        if (filtro.isEmpty() || InfoTienda.producto4_nombre.toLowerCase().contains(texto)) {
            agregarFila(4, InfoTienda.producto4_nombre, InfoTienda.producto4_laboratorio, InfoTienda.producto4_marca, InfoTienda.producto4_stock, InfoTienda.producto4_precio, fila++);
        }

        if (fila == 1) {
            Label lbl = new Label("No se encontraron coincidencias.");
            lbl.setTextFill(Color.RED);
            tablaProductos.add(lbl, 0, 1, 3, 1);
        }
    }

    private static void agregarFila(int id, String nombre, String lab, String marca, int stock, double precio, int fila) {
        VBox boxNombre = new VBox(2);
        Label lblNombre = new Label(nombre);
        lblNombre.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        Label lblLab = new Label(lab);
        lblLab.setFont(Font.font("Segoe UI", 11));
        lblLab.setTextFill(Color.GRAY);
        boxNombre.getChildren().addAll(lblNombre, lblLab);
        tablaProductos.add(boxNombre, 0, fila);

        tablaProductos.add(new Label(marca), 1, fila);
        tablaProductos.add(new Label(stock + " unid."), 2, fila);
        tablaProductos.add(new Label("S/ " + String.format("%.2f", precio)), 3, fila);

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

        Button btn = new Button(activo ? "Agregar" : "—");
        if (activo) {
            btn.setStyle("-fx-background-color: #1AA37A; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
            btn.setOnAction(e -> {
                switch (id) {
                    case 0: InfoTienda.producto0_stock--; break;
                    case 1: InfoTienda.producto1_stock--; break;
                    case 2: InfoTienda.producto2_stock--; break;
                    case 3: InfoTienda.producto3_stock--; break;
                    case 4: InfoTienda.producto4_stock--; break;
                }

                if (mapaCarrito.containsKey(id)) {
                    mapaCarrito.get(id).cantidad++;
                } else {
                    mapaCarrito.put(id, new ItemCarrito(id, nombre, precio));
                }

                actualizarVistaCarrito();
                llenarTabla(txtBuscar.getText());
            });
        } else {
            btn.setStyle("-fx-background-color: #eee; -fx-text-fill: #999; -fx-background-radius: 5;");
            btn.setDisable(true);
        }
        tablaProductos.add(btn, 5, fila);
    }

    private static void actualizarVistaCarrito() {
        contenidoCarrito.getChildren().clear();
        totalPedidoActual = 0.0;

        for (ItemCarrito item : mapaCarrito.values()) {
            double subtotalItem = item.precioUnitario * item.cantidad;
            totalPedidoActual += subtotalItem;

            HBox fila = new HBox(15);
            fila.setAlignment(Pos.CENTER_LEFT);
            Label lblCant = new Label(item.cantidad + "x");
            lblCant.setStyle("-fx-font-weight: bold;");
            Label lblNombre = new Label(item.nombre);
            lblNombre.setPrefWidth(120);
            Label lblPrecio = new Label("S/ " + String.format("%.2f", subtotalItem));

            fila.getChildren().addAll(lblCant, lblNombre, lblPrecio);
            contenidoCarrito.getChildren().add(fila);
        }
        lblTotalPagar.setText("S/ " + String.format("%.2f", totalPedidoActual));
    }

    private static void guardarVentaEnArchivo(String dni, double totalPagado) {
        String nombreArchivo = "ventas.txt";
        String fechaHora = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        StringBuilder detalleProductos = new StringBuilder();
        for (ItemCarrito item : mapaCarrito.values()) {
            detalleProductos.append(item.nombre).append(" (x").append(item.cantidad).append(") ");
        }

        try (java.io.FileWriter fw = new java.io.FileWriter(nombreArchivo, true); java.io.BufferedWriter bw = new java.io.BufferedWriter(fw)) {
            String lineaRegistro = String.format("%s | %s | %s | S/ %.2f", fechaHora, dni, detalleProductos.toString().trim(), totalPagado);
            bw.write(lineaRegistro);
            bw.newLine();
        } catch (java.io.IOException ex) {
            mostrarAlerta("Error de Persistencia", "No se pudo escribir en ventas.txt.");
            ex.printStackTrace();
        }
    }

    // --- VALIDACIÓN DE CLIENTE MODIFICADA ---
    private static boolean validarClienteEnArchivo(String dni) {
        File archivoClientes = new File("Clientes.txt");
        if (!archivoClientes.exists()) {
            // Si no existe el archivo, nadie está registrado.
            return false; 
        }

        try (Scanner scanner = new Scanner(archivoClientes)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                
                // Basado en el ModuloRegistro, el DNI es el dato principal.
                // Asumimos el formato: DNI | Nombre | Apellido... 
                // o simplemente que el DNI está en la primera columna (índice 0)
                String[] partes = linea.split("\\|");
                
                if (partes.length > 0) {
                    String dniEnArchivo = partes[0].trim();
                    if (dniEnArchivo.equals(dni)) {
                        return true; // ¡DNI encontrado!
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
