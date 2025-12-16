package SistemaGestionFarmaPlus;

import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MenuPrincipal extends Application {

    // Variables Globales 
    private final Color customWhite = Color.rgb(255, 255, 255);
    private final Color customSteelBlue = Color.rgb(1, 98, 94);
    
    private final String estiloNormal = "-fx-background-color: rgb(1,98,94); -fx-text-fill: white; -fx-alignment: CENTER_LEFT;";
    private final String estiloHover  = "-fx-background-color: rgb(100, 160, 210); -fx-text-fill: white; -fx-alignment: CENTER_LEFT;";
    private final String bordeRedondo = "-fx-background-radius: 9;-fx-border-color: #cccccc;-fx-border-radius: 9; -fx-background-color: white; -fx-padding: 5";
    
    private BorderPane PanelPrincipal;
    private Pane panelColumna;
    private ContextMenu menuActivo = null;
    
    
    //Equivalente al Construtor en JavaFx
    public void start(Stage primaryStage) {
    
    	    PanelPrincipal = new BorderPane();
        PanelPrincipal.setBackground(new Background(new BackgroundFill(customWhite, null, null)));

       //Creacion de la Barra que contiene el las Opciones 
        panelColumna = new Pane();
        panelColumna.setPrefWidth(180);
        panelColumna.setBackground(new Background(new BackgroundFill(customSteelBlue, null, null)));
        
        // LLamada al Modulo IniciarLogo
        iniciarLogo();

        //LLamada a las opciones 
        iniciarBotonInicio();
        iniciarBotonVentas();
        iniciarBotonClientes(); 
        iniciarBotonInventario();
        iniciarBotonReporte();
        iniciarBotonConfiguracion();
        iniciarBotonAyuda();
        
        // Armado de Ventana 
        PanelPrincipal.setLeft(panelColumna);
        Scene scene = new Scene(PanelPrincipal, 950, 520);
        primaryStage.setTitle("Sistema de Gestión de Ventas");
        primaryStage.setScene(scene);
        primaryStage.show();
              
    }

    //Modulo del Logo 
    private void iniciarLogo() {
        ImageView logo = cargarImagen("/iconos/Logo.png", 150, 80);
        if (logo != null) {
            logo.setLayoutX(0);
            logo.setLayoutY(0);
            panelColumna.getChildren().add(logo);
        }
    }
    
    private void iniciarBotonInicio() {
    	 
      
        Button btnInicio = crearBotonModular("  Inicio", "/Iconos/tienda.png", 100);
        
        btnInicio.setOnAction(e -> { 
        	PanelPrincipal.setCenter(null);
        });
        
        panelColumna.getChildren().add(btnInicio);
    }
    
    //Modulo del Boton Ventas
    private void iniciarBotonVentas() {
 
        MenuItem item1 = new MenuItem("Pedidos");
        MenuItem item2 = new MenuItem("Puntos de venta");
        
        item1.setOnAction(e -> {
        	Pane vistaPedidos = ModuloPedidos.obtenerVista();
        	PanelPrincipal.setCenter(vistaPedidos);});
        item2.setOnAction(e -> {
        	ModuloPuntoVenta modulo = new ModuloPuntoVenta();
        	VBox vistaPuntoVenta = modulo.obtenerVista();
        	PanelPrincipal.setCenter(vistaPuntoVenta);
        });
        
        Button btnVenta = crearBotonModular("  Ventas", "/Iconos/carro-de-la-compra.png", 160, item1, item2);
        
        panelColumna.getChildren().add(btnVenta);
    }
    
    //Módulo del boton Clientes
    private void iniciarBotonClientes() {
        
        MenuItem item1 = new MenuItem("Beneficios");
        MenuItem item2 = new MenuItem("Agregar Cliente");
        
        item1.setOnAction(e -> {
        	Pane vistaBeneficios = ModuloBeneficios.obtenerVista();
        	PanelPrincipal.setCenter(vistaBeneficios);
        });
        item2.setOnAction(e -> {
        	StackPane vistaRegistro = ModuloRegistro.obtenerVista();
        	PanelPrincipal.setCenter(vistaRegistro);});

        Button btnCliente = crearBotonModular("  Clientes", "/Iconos/cliente.png", 220, item1, item2);
        
        panelColumna.getChildren().add(btnCliente);
    }
    
    //Módulo del botón Inventario
    private void iniciarBotonInventario() {
        
        MenuItem item1 = new MenuItem("Productos");
        MenuItem item2 = new MenuItem("Stock");
        MenuItem item3 = new MenuItem("Proveedores");
        
        item1.setOnAction(e -> {
        	Pane vistaProducto = ModuloProducto.obtenerVista();
        	PanelPrincipal.setCenter(vistaProducto);
        });
    
        item2.setOnAction(e -> {
        	Pane vistaStock = ModuloStock.obtenerVista();
        	PanelPrincipal.setCenter(vistaStock);
        });
        
        item3.setOnAction(e -> {
        Pane vistaProveedores = ModuloProveedores.obtenerVista();
        	PanelPrincipal.setCenter(vistaProveedores);
        });
     
        Button btnInventario = crearBotonModular("  Inventario", "/Iconos/Inventario.png", 280, item1, item2, item3);
        
        panelColumna.getChildren().add(btnInventario);
    }
    
    //Módulo del botón Reporte 
	private void iniciarBotonReporte() {
	        
		Button btnReporte = crearBotonModular("  Reporte", "/Iconos/reporte.png", 340);
	        
	    btnReporte.setOnAction(e -> {
	    	Pane vistaReporte = ModuloReporte.obtenerVista();
        	PanelPrincipal.setCenter(vistaReporte);
        });
	        
	        panelColumna.getChildren().add(btnReporte);
	    }
	
	//Módulo del botón Configuración 
	private void iniciarBotonConfiguracion() {
		Button btnConfiguracion = crearBotonModular("  Configuracion", "/Iconos/llave.png", 400);
		
	    btnConfiguracion.setOnAction(e -> {
	    	ModuloConfiguracion moduloConfig = new ModuloConfiguracion();
	    	ScrollPane vistaConfiguracion = moduloConfig.getView();
	    	PanelPrincipal.setCenter(vistaConfiguracion);
        });
	    
	    panelColumna.getChildren().add(btnConfiguracion);
	}
	
	//Módulo del botón Ayuda
	private void iniciarBotonAyuda() {
		
		Button btnAyuda = crearBotonModular("  Ayuda", "/Iconos/ayudar.png", 460);
	    
	    btnAyuda.setOnAction(e -> {
	    	Pane vistaAyuda = ModuloAyuda.obtenerVista();
	    	PanelPrincipal.setCenter(vistaAyuda);
	    	});
	 
	    panelColumna.getChildren().add(btnAyuda);
	}

    // Módulo de diseño de botones con Parámetros y Acciones
	private Button crearBotonModular(String texto, String rutaIcono, double posY, MenuItem... items) {
	    Button btn = new Button(texto);
	    
	    btn.setLayoutX(0);
	    btn.setLayoutY(posY);
	    btn.setPrefSize(180, 50);
	    btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
	    btn.setStyle(estiloNormal);
	    
	    ImageView icono = cargarImagen(rutaIcono, 24, 24);
	    if (icono != null) {
	        btn.setGraphic(icono);
	    }

	    ContextMenu menu = new ContextMenu();
	    menu.getItems().addAll(items);
	    menu.setStyle(bordeRedondo); 
	    
	    btn.setOnAction(e -> {
	        if (menuActivo != null) {
	            menuActivo.hide();
	        }
	        
	        menu.show(btn, Side.RIGHT, 0, 0);
	        menuActivo = menu;
	    });

	    btn.setOnMouseEntered(e -> {
	        btn.setStyle(estiloHover);
	        btn.setCursor(Cursor.HAND);
	        
	        if (menuActivo != null && menuActivo != menu) {
	            menuActivo.hide(); 
	            menuActivo = null; 
	        }
	    });

	    btn.setOnMouseExited(e -> {
	        if (menuActivo != menu) {
	            btn.setStyle(estiloNormal);
	        }
	        btn.setCursor(Cursor.DEFAULT);
	    });
	    
	    menu.setOnHidden(e -> {
	        menuActivo = null;
	        btn.setStyle(estiloNormal); 
	    });

	    return btn;
	}
    
    //Módulo Cargar Imagen con Parámetros
    private ImageView cargarImagen(String ruta, int ancho, int alto) {
        try {
            Image img = new Image(getClass().getResourceAsStream(ruta));
            ImageView view = new ImageView(img);
            view.setFitWidth(ancho);
            view.setFitHeight(alto);
            view.setPreserveRatio(true);
            view.setSmooth(true);
            return view;
        } catch (Exception e) {
            System.out.println("⚠️ No se encontró imagen: " + ruta);
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
