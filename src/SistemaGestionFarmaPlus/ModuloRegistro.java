package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane; // <--- IMPORTANTE: Importar ScrollPane
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ModuloRegistro extends Application {

    // Colores base
    private static final String COLOR_VERDE_PRINCIPAL = "#2E7D32";
    private static final String COLOR_GRIS_FONDO = "#F4F6F8";
    private static final String COLOR_TEXTO_DARK = "#333333";
    private static final String COLOR_TEXTO_LABEL = "#666666";
    private static final String COLOR_BOTON_CANCELAR = "#E0E0E0";
    
    // Estilo común
    private static final String INPUT_STYLE = 
            "-fx-background-color: white; " +
            "-fx-border-color: #C0C0C0; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 8;";

    @Override
    public void start(Stage stage) {
        StackPane root = obtenerVista();
        // Reduje un poco la altura para que pruebes el ScrollPane si tu pantalla es grande
        Scene scene = new Scene(root, 800, 600); 
        stage.setTitle("Sistema FarmaPlus - Registro");
        stage.setScene(scene);
        stage.show();
    }
        
    public static StackPane obtenerVista() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + COLOR_GRIS_FONDO + ";");
        // Quitamos el padding del root porque ahora lo manejará el contenido interno
        
        // --- CREACIÓN DE LA TARJETA CENTRAL (Igual que antes) ---
        VBox cardRegistro = new VBox(15);
        cardRegistro.setMaxWidth(500);
        cardRegistro.setPadding(new Insets(30));
        cardRegistro.setAlignment(Pos.TOP_LEFT);
        
        cardRegistro.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 15, 0, 0, 5);"
        );

        // 1. Título
        Label lblTitulo = new Label("Registrar Nuevo Cliente");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_DARK + ";");
        
        HBox titleContainer = new HBox(lblTitulo);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(0, 0, 10, 0));

        // 2. Campos del Formulario
        VBox groupDni = new VBox(5);
        groupDni.getChildren().add(crearLabel("DNI"));
        HBox dniInputBox = new HBox(10);
        TextField txtDni = new TextField();
        txtDni.setPromptText("DNI");
        txtDni.setStyle(INPUT_STYLE);
        HBox.setHgrow(txtDni, Priority.ALWAYS);

        Button btnBuscar = new Button("🔍");
        btnBuscar.setStyle(
                "-fx-background-color: " + COLOR_VERDE_PRINCIPAL + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 14px;" +
                "-fx-min-width: 40px;" +
                "-fx-min-height: 35px;"
        );
        dniInputBox.getChildren().addAll(txtDni, btnBuscar);
        groupDni.getChildren().add(dniInputBox);

        VBox groupNombres = crearGrupoInput("Nombres", "Nombres");
        VBox groupApellidos = crearGrupoInput("Apellidos", "Apellidos");
        
        VBox groupFecha = new VBox(5);
        groupFecha.getChildren().add(crearLabel("Fecha de Nacimiento"));
        DatePicker dateFecha = new DatePicker();
        dateFecha.setPromptText("Fecha de Nacimiento");
        dateFecha.setMaxWidth(Double.MAX_VALUE);
        dateFecha.setStyle(INPUT_STYLE);
        dateFecha.getEditor().setStyle("-fx-background-color: transparent;"); 
        groupFecha.getChildren().add(dateFecha);

        VBox groupTelefono = crearGrupoInput("Teléfono", "Teléfono");
        VBox groupCorreo = crearGrupoInput("Correo Electrónico", "Correo Electrónico");
        VBox groupDireccion = crearGrupoInput("Dirección", "Dirección");

        // 3. Botones
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button btnGuardar = new Button("Guardar Cliente");
        btnGuardar.setStyle(
                "-fx-background-color: " + COLOR_VERDE_PRINCIPAL + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10 25;"
        );

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle(
                "-fx-background-color: " + COLOR_BOTON_CANCELAR + ";" +
                "-fx-text-fill: " + COLOR_TEXTO_DARK + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10 25;"
        );

        buttonBox.getChildren().addAll(btnGuardar, btnCancelar);

        cardRegistro.getChildren().addAll(
                titleContainer, groupDni, groupNombres, groupApellidos, 
                groupFecha, groupTelefono, groupCorreo, groupDireccion, buttonBox
        );

        // --- IMPLEMENTACIÓN DEL SCROLLPANE ---

        // A. Creamos un contenedor intermedio transparente para centrar la tarjeta
        // Si ponemos la tarjeta directo al ScrollPane, se pegaría arriba a la izquierda.
        StackPane contentContainer = new StackPane(cardRegistro);
        contentContainer.setPadding(new Insets(20)); // Margen para que la sombra no se corte
        contentContainer.setStyle("-fx-background-color: transparent;");

        // B. Creamos el ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentContainer);
        
        // C. Configuración Visual del ScrollPane
        scrollPane.setFitToWidth(true);   // Ancho se adapta a la ventana
        scrollPane.setFitToHeight(true);  // Alto se adapta (clave para el centrado vertical)
        
        // CSS para quitar bordes y fondo gris del propio ScrollPane
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" + 
            "-fx-padding: 0;"
        );
        
        // D. Agregamos el ScrollPane al Root
        root.getChildren().add(scrollPane);
        
        return root;
    }

    // --- MÉTODOS AUXILIARES ---

    private static Label crearLabel(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-text-fill: " + COLOR_TEXTO_LABEL + "; -fx-font-size: 14px;");
        return lbl;
    }

    private static VBox crearGrupoInput(String labelText, String promptText) {
        VBox group = new VBox(5);
        group.getChildren().add(crearLabel(labelText));
        TextField txt = new TextField();
        txt.setPromptText(promptText);
        txt.setStyle(INPUT_STYLE);
        group.getChildren().add(txt);
        return group;
    }

    public static void main(String[] args) {
        launch(args);
    }
}