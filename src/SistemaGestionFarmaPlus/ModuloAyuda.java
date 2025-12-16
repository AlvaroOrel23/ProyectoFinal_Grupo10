package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ModuloAyuda extends Application {

    // Metodo Main para prueba independiente
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane content = obtenerVista();

        // Se crea un ScrollPane para obtener la vista de los elementos si la ventana es pequeña 
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true); 
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #f4f6f8;");

        Scene scene = new Scene(scrollPane, 900, 600);
        primaryStage.setTitle("Sistema FarmaPlus - Soporte y Ayuda");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // -----------------------------------------------

    public static Pane obtenerVista() {
        
        // Panel principal 
        VBox vistaPrincipal = new VBox(20);
        vistaPrincipal.setPadding(new Insets(30));
        vistaPrincipal.setStyle("-fx-background-color: #f4f6f8;"); 
        vistaPrincipal.setAlignment(Pos.TOP_CENTER);

         
        Label titulo = new Label("Apoyo y Soporte");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        titulo.setTextFill(Color.web("#2c3e50"));

        Label subtitulo = new Label("Estamos aquí para ayudarte en cada paso");
        subtitulo.setFont(Font.font("Segoe UI", 16));
        subtitulo.setTextFill(Color.GRAY);

        // Contenedor para dividir la pantalla en dos columnas
        HBox contenedorColumnas = new HBox(30); 
        contenedorColumnas.setAlignment(Pos.TOP_CENTER);
        
        // Columna izquierda
        VBox colIzquierda = new VBox(15);
        HBox.setHgrow(colIzquierda, Priority.ALWAYS); 
        
        Label lblContacto = new Label("Opciones de Contacto");
        lblContacto.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblContacto.setPadding(new Insets(0, 0, 10, 0));

        // Creamos las tarjetas usando el método de abajo para no repetir
        VBox cardLlamanos = crearTarjetaContacto("\uD83D\uDCDE", "Llámanos", "999999999", "Lun-Vie 9:00-18:00");
        VBox cardEmail = crearTarjetaContacto("\u2709", "Email", "soporte@farmacia.com", "");
        
        // Tarjeta del Chat con una etiqueta verde
        VBox cardChat = crearTarjetaContacto("\uD83D\uDCAC", "Chat en Vivo", "Disponible ahora", "");
        
        Label lblEnLinea = new Label("En línea");
        lblEnLinea.setTextFill(Color.WHITE);
        lblEnLinea.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        lblEnLinea.setPadding(new Insets(2, 8, 2, 8));
        lblEnLinea.setBackground(new Background(new BackgroundFill(Color.web("#2ecc71"), new CornerRadii(10), Insets.EMPTY)));
        cardChat.getChildren().add(lblEnLinea); 

        // Botón oscuro de abajo
        Button btnTicket = new Button("Abrir Ticket de Soporte");
        btnTicket.setMaxWidth(Double.MAX_VALUE);
        btnTicket.setPrefHeight(45);
        btnTicket.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnTicket.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        
        colIzquierda.getChildren().addAll(lblContacto, cardLlamanos, cardEmail, cardChat, btnTicket);

        // Columna derecha
        VBox colDerecha = new VBox(20);
        HBox.setHgrow(colDerecha, Priority.ALWAYS); 
        colDerecha.setPadding(new Insets(30));
        colDerecha.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 0);");
        
        Label lblEquipo = new Label("Nuestro Equipo");
        lblEquipo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        
        Text txtDesc = new Text("Contamos con un equipo de expertos dedicados a resolver tus dudas y problemas técnicos. Nuestros agentes están capacitados para ofrecerte soluciones rápidas y efectivas.");
        txtDesc.setFont(Font.font("Segoe UI", 14));
        txtDesc.setFill(Color.web("#555"));
        txtDesc.setWrappingWidth(350); 
        
        // Fila para "98%" "2h" "24/7"
        HBox filaStats = new HBox(20);
        filaStats.setAlignment(Pos.CENTER);
        filaStats.setPadding(new Insets(30, 0, 0, 0));
        
        filaStats.getChildren().addAll(
            crearStat("98%", "Satisfacción"),
            crearStat("2h", "Tiempo Respuesta"),
            crearStat("24/7", "Disponibilidad")
        );

        colDerecha.getChildren().addAll(lblEquipo, txtDesc, filaStats);

        // Juntar todo
        contenedorColumnas.getChildren().addAll(colIzquierda, colDerecha);
        vistaPrincipal.getChildren().addAll(titulo, subtitulo, contenedorColumnas);
        
        return vistaPrincipal;
    }

    // Crear los cuadros blancos de contacto 
    private static VBox crearTarjetaContacto(String icono, String titulo, String dato, String subDato) {
        VBox tarjeta = new VBox(5);
        tarjeta.setPadding(new Insets(15));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        
        HBox contenido = new HBox(15);
        contenido.setAlignment(Pos.CENTER_LEFT);
        
        // Icono 
        Label lblIcono = new Label(icono);
        lblIcono.setFont(Font.font("Segoe UI Emoji", 24));
        lblIcono.setTextFill(Color.web("#34495e"));
        
        // Textos
        VBox textos = new VBox(2);
        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        Label lblDato = new Label(dato);
        lblDato.setFont(Font.font("Segoe UI", 13));
        lblDato.setTextFill(Color.web("#555"));
        
        textos.getChildren().addAll(lblTitulo, lblDato);
        
        // Para los subtitulos de contacto
        if (!subDato.isEmpty()) {
            Label lblSub = new Label(subDato);
            lblSub.setFont(Font.font("Segoe UI", 11));
            lblSub.setTextFill(Color.GRAY);
            textos.getChildren().add(lblSub);
        }
        
        contenido.getChildren().addAll(lblIcono, textos);
        tarjeta.getChildren().add(contenido);
        
        return tarjeta;
    }

    // Para crear los numeros grandes
    private static VBox crearStat(String valor, String etiqueta) {
        VBox stat = new VBox(5);
        stat.setAlignment(Pos.CENTER);
        
        Label lblValor = new Label(valor);
        lblValor.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        lblValor.setTextFill(Color.web("#2c3e50"));
        
        Label lblEtiqueta = new Label(etiqueta);
        lblEtiqueta.setFont(Font.font("Segoe UI", 12));
        lblEtiqueta.setTextFill(Color.GRAY);
        
        stat.getChildren().addAll(lblValor, lblEtiqueta);
        return stat;
    }
}
