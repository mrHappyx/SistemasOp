package Proyect1SO;

import java.util.Random;

public class Proceso {
    
    int id;
    int tiempoRestante;
    int estado; // 1 = Listo, 2 = Bloqueado
    int prioridad;
    int prioridadOriginal; // Nueva propiedad para guardar la prioridad original
    int usoCPU;
    int intentosDesbloqueo;
    int usuario;
    boolean ejecutado = false;
    
    //RR
    public Proceso(int id, int tiempoRestante, int estado) {
        this.id = id;
        this.tiempoRestante = tiempoRestante;
        this.estado = estado;
        this.prioridadOriginal = this.prioridad; // Guardar la prioridad original
    }

    public String estadoFinal() {
        if (tiempoRestante == 0)
            return "Finalizado";
        else if (estado == 1)
            return "Listo";
        else if (estado == 2)
            return "Bloqueado";
        return "Desconocido";
    }
    
    //Prioridad
    public Proceso(int id, int tiempoRestante, int estado, int prioridad) {
        this.id = id;
        this.tiempoRestante = tiempoRestante;
        this.estado = estado; // 1 = Listo, 2 = Bloqueado
        this.prioridad = prioridad;
        this.prioridadOriginal = this.prioridad; // Guardar la prioridad original
    }
    
    
    //Garantizada Equitativa
    public Proceso(int id, int tiempoRestante, int estado, int prioridad, int usuario) {
        this.id = id;
        this.tiempoRestante = tiempoRestante;
        this.estado = estado;
        this.prioridad = prioridad;
        this.prioridadOriginal = this.prioridad; // Guardar la prioridad original
        this.intentosDesbloqueo = 0;
        this.usuario = usuario;
    }

    String estadoTexto() {
        return switch (estado) {
            case 1 -> "Listo";
            case 2 -> "Bloqueado";
            default -> "Finalizado";
        };
    }
    

    // Constructor original
    public Proceso(int id) {
        this.id = id;
        this.tiempoRestante = new Random().nextInt(8) + 3; // 3-10
        this.estado = new Random().nextBoolean() ? 1 : 2;
        this.prioridad = new Random().nextInt(3) + 1;
        this.prioridadOriginal = this.prioridad; // Guardar la prioridad original
        this.usoCPU = 0;
        this.intentosDesbloqueo = 0;
    }
}