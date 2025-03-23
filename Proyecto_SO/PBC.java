package Proyect1SO;

public class PBC {
    private int id;
    private int tiempoRestante;
    private String estado;
    private String prioridad;
    private String usuario;
    private String boletos; // Nueva columna para boletos

    public PBC(int id, int tiempoRestante, String estado, String prioridad, String usuario, String boletos) {
        this.id = id;
        this.tiempoRestante = tiempoRestante;
        this.estado = estado;
        this.prioridad = prioridad;
        this.usuario = usuario;
        this.boletos = boletos; // Inicializar boletos
    }

    public static void imprimirEncabezadoTabla() {
        System.out.printf("%-10s | %-20s | %-15s | %-10s | %-10s | %-10s\n", "Proceso", "Tiempo Restante", "Estado", "Prioridad", "Usuario", "Boletos"); // Actualizar encabezado
    }

    public void imprimirFilaTabla() {
        System.out.printf("%-10d | %-20d | %-15s | %-10s | %-10s | %-10s\n", id, tiempoRestante, estado, formatearCampo(prioridad), formatearCampo(usuario), formatearCampo(boletos)); // Actualizar fila
    }
    
    private String formatearCampo(String campo) {
        return (campo.equals("0") || campo.equals("null")) ? "---" : campo;
    }
}