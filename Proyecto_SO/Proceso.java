package Proyect1SO;

public class Proceso {
    public int id;
    public int tiempoRestante;
    public int estado; // 1 = Listo, 2 = Bloqueado
    public int prioridad;
    public int usuario;
    public int intentosDesbloqueo;
    public boolean ejecutado;
    public int usoCPU; // Nuevo atributo para rastrear el uso de CPU
    public int boletos; // Atributo para el planificador de lotería
    public int prioridadOriginal; // Atributo para almacenar la prioridad original

    public Proceso(int id, int tiempoRestante, int estado, int prioridad, int usuario, int boletos) {
        this.id = id;
        this.tiempoRestante = tiempoRestante;
        this.estado = estado;
        this.prioridad = prioridad;
        this.usuario = usuario;
        this.intentosDesbloqueo = 0;
        this.ejecutado = false;
        this.usoCPU = 0; // Inicializamos el uso de CPU a 0
        this.boletos = boletos; // Inicializamos boletos
        this.prioridadOriginal = prioridad; // Almacenamos la prioridad original
    }

    public Proceso(int id, int tiempoRestante, int estado, int prioridad, int usuario) {
        this(id, tiempoRestante, estado, prioridad, usuario, 0); // Inicializamos boletos a 0 por defecto
    }

    public Proceso(int id, int tiempoRestante, int estado) {
        this(id, tiempoRestante, estado, 1, -1); // Prioridad y usuario por defecto
    }

    public String estadoFinal() {
        return (this.tiempoRestante == 0) ? "Finalizado" : (this.estado == 1 ? "Listo" : "Bloqueado");
    }
}