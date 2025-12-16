package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ModuloPuntoVenta {

    // --- COLORES CORPORATIVOS FARMAPLUS ---
    private static final Color COLOR_PRIMARIO = Color.web("#00695C"); // El "Teal" del menú lateral
    private static final Color COLOR_FONDO = Color.web("#F9F9F9");    // Gris muy claro para el fondo de contenido
    private static final Color COLOR_TEXTO_PRINCIPAL = Color.web("#333333");
    private static final Color COLOR_TEXTO_SECUNDARIO = Color.web("#757575");
    
    // Colores de Estado
    private static final Color ESTADO_DISPONIBLE = Color.web("#4CAF50"); // Verde vibrante
    private static final Color ESTADO_POCAS = Color.web("#FFC107");      // Ámbar
    private static final Color ESTADO_AGOTADO = Color.web("#F44336");    // Rojo

    private static final Font FUENTE_TITULO = Font.font("Segoe UI", FontWeight.BOLD, 22);
    private static final Font FUENTE_SUBTITULO = Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16);
    private static final Font FUENTE_NORMAL = Font.font("Segoe UI", 14);


   
    public VBox obtenerVista() {
        // 1. Contenedor Raíz
        VBox root = new VBox(25);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: " + toHexString(COLOR_FONDO) + ";");
        root.setFillWidth(true);

        // 2. Encabezado
        VBox header = new VBox(5);
        Label lblTitulo = new Label("Disponibilidad en Sucursales");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setTextFill(COLOR_PRIMARIO); 

        Label lblSubtitulo = new Label("Consulta el stock del producto seleccionado en tiempo real.");
        lblSubtitulo.setFont(FUENTE_NORMAL);
        lblSubtitulo.setTextFill(COLOR_TEXTO_SECUNDARIO);
        header.getChildren().addAll(lblTitulo, lblSubtitulo);


        // 3. Barra de Búsqueda 
        HBox searchContainer = new HBox(15);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(10, 15, 10, 15));
        searchContainer.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #E0E0E0; -fx-border-radius: 10; -fx-border-width: 1;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);" // Sombra sutil
        );

        SVGPath searchIcon = createIcon("M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z", COLOR_PRIMARIO);

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Buscar por nombre de sucursal...");
        txtSearch.setFont(FUENTE_NORMAL);
        // Quitamos el estilo por defecto del TextField para integrarlo en el HBox
        txtSearch.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: #9E9E9E;");
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        searchContainer.getChildren().addAll(searchIcon, txtSearch);


        // 4. Contenedor de Tarjetas (FlowPane para diseño responsivo)
        // Usamos FlowPane para que las tarjetas se acomoden automáticamente según el ancho de la ventana
        FlowPane cardsFlowPane = new FlowPane(105, 25); // Espacio horizontal y vertical entre tarjetas
        cardsFlowPane.setPadding(new Insets(10));
        cardsFlowPane.setAlignment(Pos.TOP_LEFT);

        // Creación de las tarjetas de ejemplo
        cardsFlowPane.getChildren().add(createSucursalCard("FarmaPlus Centro", 3.4, 10, ESTADO_DISPONIBLE));
        cardsFlowPane.getChildren().add(createSucursalCard("FarmaPlus Norte", 4.2, 0, ESTADO_AGOTADO));
        cardsFlowPane.getChildren().add(createSucursalCard("FarmaPlus Sur Mall", 2.1, 3, ESTADO_POCAS));
        cardsFlowPane.getChildren().add(createSucursalCard("FarmaPlus Este", 5.5, 25, ESTADO_DISPONIBLE));
        cardsFlowPane.getChildren().add(createSucursalCard("FarmaPlus Oeste", 1.2, 2, ESTADO_POCAS));
        


        // Envolvemos el FlowPane en un ScrollPane por si hay muchas sucursales
        ScrollPane scrollPane = new ScrollPane(cardsFlowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;"); // Hacer transparente el fondo del scroll
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().addAll(header, searchContainer, scrollPane);

        return root;
    }


    /**
     * Crea una tarjeta de diseño moderno para una sucursal individual.
     */
    private HBox createSucursalCard(String nombre, double distanciaKm, int stock, Color colorEstado) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(340); // Ancho fijo para uniformidad
        // Diseño de tarjeta moderna: Blanca, borde muy sutil y sombra suave para dar profundidad ("elevación")
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #EEEEEE; -fx-border-radius: 12; -fx-border-width: 1;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 4);"
        );

        //  Icono de la Sucursal 
        // Usamos el color primario (Teal) para el icono para reforzar la marca
        SVGPath iconHouse = createIcon("M12 7V3H2v18h20V7H12zM6 19H4v-2h2v2zm0-4H4v-2h2v2zm0-4H4V9h2v2zm0-4H4V5h2v2zm4 12H8v-2h2v2zm0-4H8v-2h2v2zm0-4H8V9h2v2zm0-4H8V5h2v2zm10 12h-8v-2h2v-2h-2v-2h2v-2h-2V9h8v10zm-2-8h-2v2h2v-2zm0 4h-2v2h2v-2z", COLOR_PRIMARIO);
        iconHouse.setScaleX(1.5);
        iconHouse.setScaleY(1.5);
        
        StackPane iconContainer = new StackPane(iconHouse);
        iconContainer.setPrefSize(50, 50);
        // Un fondo circular suave detrás del icono
        iconContainer.setStyle("-fx-background-color: #" + toHexString(COLOR_PRIMARIO.deriveColor(0, 1, 1, 0.1)) + "; -fx-background-radius: 50%;");


        //  Detalles de la Sucursal 
        VBox details = new VBox(6);
        HBox.setHgrow(details, Priority.ALWAYS);

        Label lblName = new Label(nombre);
        lblName.setFont(FUENTE_SUBTITULO);
        lblName.setTextFill(COLOR_TEXTO_PRINCIPAL);

        HBox infoRow = new HBox(15);
        infoRow.setAlignment(Pos.CENTER_LEFT);
        
        Label lblDistance = new Label("\uD83D\uDCCD " + distanciaKm + " km"); // Icono de pin unicode
        lblDistance.setFont(FUENTE_NORMAL);
        lblDistance.setTextFill(COLOR_TEXTO_SECUNDARIO);

        Label lblStock = new Label("Stock: " + stock);
        lblStock.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblStock.setTextFill(COLOR_TEXTO_PRINCIPAL);
        
        infoRow.getChildren().addAll(lblStock, lblDistance);

        //  Badge de Estado 
        Label lblStatusBadge = createStatusBadge(stock, colorEstado);
        
        details.getChildren().addAll(lblName, infoRow, lblStatusBadge);

        card.getChildren().addAll(iconContainer, details);
        return card;
    }
    
    // Método auxiliar para crear el "Badge" (la etiqueta de color)
    private Label createStatusBadge(int stock, Color colorEstado) {
        String textoEstado;
        if (stock == 0) { textoEstado = "AGOTADO";}
        else if (stock <= 5) { textoEstado = "POCAS UNIDADES";}
        else { textoEstado = "DISPONIBLE";}
        
        Label badge = new Label(textoEstado);
        badge.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        badge.setPadding(new Insets(4, 12, 4, 12));
        
        // Color del texto: Blanco, excepto si el fondo es amarillo (para contraste)
        String textColorHex = colorEstado.equals(ESTADO_POCAS) ? toHexString(COLOR_TEXTO_PRINCIPAL) : "white";

        badge.setStyle(
            "-fx-background-color: " + toHexString(colorEstado) + ";" +
            "-fx-text-fill: " + textColorHex + ";" +
            "-fx-background-radius: 20;" // Bordes completamente redondeados
        );
        return badge;
    }


    //  Métodos de Utilidad 
    private SVGPath createIcon(String pathContent, Color fill) {
        SVGPath icon = new SVGPath();
        icon.setContent(pathContent);
        icon.setFill(fill);
        return icon;
    }

    private String toHexString(Color color) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }


    // Método Main para la ejecución independiente 
    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            ModuloPuntoVenta modulo = new ModuloPuntoVenta();
            // Usamos un tamaño ancho para apreciar el FlowPane
            Scene scene = new Scene(modulo.obtenerVista(), 900, 600);
            primaryStage.setTitle("Prueba de Diseño - Stock Sucursales FarmaPlus");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}