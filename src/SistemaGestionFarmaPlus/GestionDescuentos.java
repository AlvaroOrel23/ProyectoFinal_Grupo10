package SistemaGestionFarmaPlus;

import java.io.*;
import java.util.*;

public class GestionDescuento {

    // Método para calcular el descuento basado en Historial de Visitas Y Puntos Acumulados
	
    public static double calcularDescuentoPorHistorial(String dniBuscado) {
        int contadorCompras = 0;
        double totalGastadoHistorico = 0.0; 
        
        File archivo = new File("ventas.txt");
        
        if (!archivo.exists()) return 0.0;

        try (Scanner lector = new Scanner(archivo)) {
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                String[] partes = linea.split(" \\| "); 
                
                // Formato del archivo ventas.txt: FECHA | DNI | PRODUCTOS | TOTAL
                // Verificamos DNI y que la línea tenga las 4 partes
                if (partes.length >= 4 && partes[1].trim().equals(dniBuscado.trim())) {
                    
                    // 1. Contamos la visita (para premiar recurrencia)
                    contadorCompras++;
                    
                    // 2. Sumamos el monto (para premiar compras)
                    try {
                        // Limpiar el formato del precio y convertirlo en Double 
                        String precioLimpio = partes[3]
                                .replace("S/", "")
                                .replace(" ", "")
                                .replace(",", "") 
                                .trim();
                        
                        double montoVenta = Double.parseDouble(precioLimpio);
                        totalGastadoHistorico += montoVenta;
                        
                    } catch (NumberFormatException e) {
                        // Si hay un error en el formato del número en el txt, ignoramos esa línea
                        System.out.println("Error leyendo monto en historial: " + partes[3]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return 0.0;
        }

        // --- LÓGICA DE PUNTOS ---
        
        // Se pondera 10 unidades monetarias en un punto 
        int puntosCalculados = (int)(totalGastadoHistorico / 10);

        // CONDICIÓN : Si llega a 100 puntos, gana el 25% inmediatamente
        // Solo se ejecuta un descueto. Al ser este mayor, se le da prioridad
        if (puntosCalculados >= 100) {
            return 0.25; 
        }

        // --- LÓGICA DESCUENTO POR VISITAS ---
        // Si no le alcanzan los puntos, verificamos si tiene descuento por visitas frecuentes
        if (contadorCompras > 5) return 0.10; // 10%
        if (contadorCompras >= 3) return 0.05; // 5%
        
        return 0.0;
    }
}


