package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.*;
import java.util.Scanner;

public class ModuloReporte extends Application {

    private Label lblVentasTotales;
    private Label lblIngresosTotales;
    private Label lblProductosCriticos;
    private VBox listaUltimasVentas;

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(obtenerVista(), 950, 650));
        stage.setTitle("Reportes y Estadísticas - FarmaPlus");
        stage.show();
    }

    public Pane obtenerVista() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // --- ENCABEZADO ---
        Label titulo = new Label("Reporte de Gestión Diaria");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#0D5F56"));

        // --- TARJETAS DE RESUMEN (KPIs) ---
        HBox hbCards = new HBox(20);
        hbCards.setAlignment(Pos.CENTER);
        hbCards.setPadding(new Insets(20, 0, 20, 0));

        lblVentasTotales = new Label("0");
        lblIngresosTotales = new Label("S/ 0.00");
        lblProductosCriticos = new Label("0");

        hbCards.getChildren().addAll(
            crearTarjeta("Ventas Realizadas", lblVentasTotales, "#1AA37A"),
            crearTarjeta("Ingresos del Día", lblIngresosTotales, "#0D5F56"),
            crearTarjeta("Alertas de Stock", lblProductosCriticos, "#E04A4A")
        );

        // --- SECCIÓN DE DETALLES ---
        VBox vbDetalles = new VBox(15);
        vbDetalles.setPadding(new Insets(15));
        vbDetalles.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 0);");
        
        Label lblSub1 = new Label("Historial de Transacciones Recientes");
        lblSub1.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        
        listaUltimasVentas = new VBox(10);
        listaUltimasVentas.setPadding(new Insets(10));
        
        ScrollPane scrollVentas = new ScrollPane(listaUltimasVentas);
        scrollVentas.setFitToWidth(true);
        scrollVentas.setPrefHeight(350);
        scrollVentas.setStyle("-fx-background-color: transparent; -fx-background: white;");

        vbDetalles.getChildren().addAll(lblSub1, new Separator(), scrollVentas);
        
        VBox layoutCentral = new VBox(10, titulo, hbCards, vbDetalles);
        layoutCentral.setAlignment(Pos.TOP_LEFT);
        
        root.setCenter(layoutCentral);
        
        // Ejecución automática al cargar la vista
        procesarDatos();

        return root;
    }

    private VBox crearTarjeta(String titulo, Label valor, String colorHex) {
        VBox card = new VBox(10);
        card.setPrefSize(260, 130);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        
        Label lblT = new Label(titulo);
        lblT.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblT.setTextFill(Color.GRAY);
        
        valor.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        valor.setTextFill(Color.web(colorHex));
        
        card.getChildren().addAll(lblT, valor);
        return card;
    }

    private void procesarDatos() {
        double ingresos = 0;
        int numVentas = 0;
        listaUltimasVentas.getChildren().clear();

        // 1. Procesar ventas.txt para KPIs e Historial
        File archivoVentas = new File("ventas.txt");
        if (archivoVentas.exists()) {
            try (Scanner sc = new Scanner(archivoVentas)) {
                while (sc.hasNextLine()) {
                    String linea = sc.nextLine();
                    String[] d = linea.split("\\|");
                    if (d.length >= 4) {
                        numVentas++;
                        // Limpieza del string de precio para conversión numérica
                        String precioStr = d[3].replace("S/ ", "").replace(",", ".").trim();
                        try {
                            ingresos += Double.parseDouble(precioStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Error en formato de precio: " + precioStr);
                        }
                        
                        // Crear fila de historial con estilo
                        HBox filaHistorial = new HBox(15);
                        filaHistorial.setPadding(new Insets(5));
                        filaHistorial.setStyle("-fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;");
                        
                        Label fecha = new Label(d[0].trim());
                        fecha.setPrefWidth(150);
                        Label detalle = new Label(d[2].trim());
                        detalle.setPrefWidth(350);
                        Label monto = new Label(d[3].trim());
                        monto.setStyle("-fx-font-weight: bold;");
                        
                        filaHistorial.getChildren().addAll(fecha, detalle, monto);
                        listaUltimasVentas.getChildren().add(0, filaHistorial); // Más reciente primero
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        // 2. Procesar productos.txt para Alertas de Stock
        int criticos = 0;
        File archivoProd = new File("productos.txt");
        if (archivoProd.exists()) {
            try (Scanner sc = new Scanner(archivoProd)) {
                while (sc.hasNextLine()) {
                    String[] d = sc.nextLine().split("\\|");
                    if (d.length >= 5) {
                        try {
                            int stock = Integer.parseInt(d[4].trim());
                            if (stock <= 5) criticos++;
                        } catch (NumberFormatException e) {
                            System.err.println("Error en formato de stock para producto: " + d[0]);
                        }
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        // Actualización inmediata de la interfaz
        lblVentasTotales.setText(String.valueOf(numVentas));
        lblIngresosTotales.setText(String.format("S/ %.2f", ingresos));
        lblProductosCriticos.setText(String.valueOf(criticos));
    }
}
