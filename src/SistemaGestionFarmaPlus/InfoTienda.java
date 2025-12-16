package SistemaGestionFarmaPlus;

public class InfoTienda {

    // --- Inventario de Productos ---

    // Producto 0: 
    public static String producto0_nombre = "Ibuprofeno 400mg"; 
    public static String producto0_marca  = "Advil Caps";       
    public static String producto0_laboratorio = "Haleon";      
    public static double producto0_precio = 22.50; 
    public static int producto0_stock = 15; 
    
    // Producto 1: 
    public static String producto1_nombre = "Ibuprofeno 400mg";
    public static String producto1_marca  = "Motrin";
    public static String producto1_laboratorio = "Johnson & Johnson";
    public static double producto1_precio = 18.00; 
    public static int producto1_stock = 8; 

    // Producto 2: 
    public static String producto2_nombre = "Ibuprofeno 400mg";
    public static String producto2_marca  = "Genérico";
    public static String producto2_laboratorio = "Farmindustria";
    public static double producto2_precio = 5.00;
    public static int producto2_stock = 50; 
    
    // Producto 3: 
    public static String producto3_nombre = "Paracetamol 500mg";
    public static String producto3_marca  = "Panadol Forte";
    public static String producto3_laboratorio = "GSK";
    public static double producto3_precio = 8.50;
    public static int producto3_stock = 20;
    
    // Producto 4: 
    public static String producto4_nombre = "Paracetamol 500mg";
    public static String producto4_marca  = "Kitadol";
    public static String producto4_laboratorio = "Bayer";
    public static double producto4_precio = 6.00;
    public static int producto4_stock = 12;

    // --- Datos Globales de Ventas ---

    public static int contadorVentas = 0; 
    public static double acumuladoTotalGeneral = 0.0; 
    public static final double CUOTA_DIARIA = 50000.0; 
}
