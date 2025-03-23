package Proyect1SO;

import java.util.*;

public class PlanificacionEquitativa {
	private List<Proceso> procesos;
    private int tiempoTotal;
    private int cambiosProcesos = 0;
    private Map<Integer, Integer> tiempoPorUsuario;
    private List<Proceso> finalizados = new ArrayList<>();
    private List<Proceso> nuncaEjecutados = new ArrayList<>();
    private List<Proceso> procesosMuertos = new ArrayList<>();
    private List<Proceso> procesosOriginales; // Guardamos todos los procesos iniciales

    public PlanificacionEquitativa(List<Proceso> procesos, int tiempoSimulacion) {
        this.procesos = new ArrayList<>(procesos);
        this.procesosOriginales = new ArrayList<>(procesos); // Almacenar copia original
        this.tiempoTotal = tiempoSimulacion;
        this.tiempoPorUsuario = new HashMap<>();
        for (Proceso p : procesos) {
            tiempoPorUsuario.put(p.usuario, 0);
        }
    }

    public void ejecutar() {
        System.out.println("\n===== Tabla Inicial de Procesos =====");
        System.out.printf("%-10s %-17s %-12s %-10s\n", "Proceso", "Tiempo Restante", "Estado", "Usuario");
        for (Proceso p : procesosOriginales) { // Usamos la lista original
            String estado = (p.estado == 1) ? "Listo" : "Bloqueado";
            System.out.printf("%-10d %-17d %-12s %-10d\n", p.id, p.tiempoRestante, estado, p.usuario);
        }

        System.out.println("\n===== Ejecutando Planificación Equitativa =====");
        int tiempoTranscurrido = 0;

        while (tiempoTranscurrido < tiempoTotal && !procesos.isEmpty()) {
            for (Proceso proceso : new ArrayList<>(procesos)) {
                if (proceso.estado == 1) { // Si está listo, lo ejecutamos
                    int tiempoAsignado = tiempoTotal / tiempoPorUsuario.size();
                    int tiempoEjecutado = Math.min(proceso.tiempoRestante, tiempoAsignado);
                    proceso.tiempoRestante -= tiempoEjecutado;
                    tiempoTranscurrido += tiempoEjecutado;
                    tiempoPorUsuario.put(proceso.usuario, tiempoPorUsuario.get(proceso.usuario) + tiempoEjecutado);
                    cambiosProcesos++;
                    System.out.println("Proceso " + proceso.id + " del usuario " + proceso.usuario + " ejecuta " + tiempoEjecutado + " unidades, tiempo restante: " + proceso.tiempoRestante);

                    if (proceso.tiempoRestante <= 0) {
                        finalizados.add(proceso);
                        procesos.remove(proceso);
                    }
                } else if (proceso.estado == 2) { // Si está bloqueado, intentamos desbloquearlo
                    if (new Random().nextInt(2) == 1) { // 50% de probabilidad de desbloqueo
                        proceso.estado = 1;
                        System.out.println("Proceso " + proceso.id + " se ha desbloqueado.");
                    } else {
                        proceso.intentosDesbloqueo++;
                        if (proceso.intentosDesbloqueo >= 3) { // Si no se desbloquea en 3 intentos, muere por inanición
                            System.out.println("Muerte del proceso " + proceso.id + " por inanición.");
                            procesosMuertos.add(proceso);
                            procesos.remove(proceso);
                        }
                    }
                }
            }
        }
        mostrarReporte();
    }

    private void mostrarReporte() {
        System.out.println("\n===== Reporte Final =====");
        System.out.println("Tiempo total asignado por usuario: " + tiempoPorUsuario);
        System.out.println("Procesos terminados: " + finalizados.stream().map(p -> p.id).toList());
        System.out.println("Procesos que nunca se ejecutaron: " + nuncaEjecutados.stream().map(p -> p.id).toList());
        System.out.println("Procesos en ejecución: " + procesos.stream().map(p -> p.id).toList());
        System.out.println("Procesos muertos por inanición: " + procesosMuertos.stream().map(p -> p.id).toList());
        System.out.println("Cambios de procesos registrados: " + cambiosProcesos);

        System.out.println("\n===== Tabla Final de Procesos =====");
        System.out.printf("%-10s %-17s %-15s\n", "Proceso", "Tiempo Restante", "Estado Final");
        for (Proceso p : procesosOriginales) { // Revisamos todos los procesos originales
            String estadoFinal;
            if (finalizados.contains(p)) {
                estadoFinal = "Finalizado";
            } else if (procesosMuertos.contains(p)) {
                estadoFinal = "Muerto por inanición";
            } else if (procesos.contains(p)) {
                estadoFinal = "Listo";
            } else {
                estadoFinal = "Nunca ejecutado";
            }
            System.out.printf("%-10d %-17d %-15s\n", p.id, p.tiempoRestante, estadoFinal);
        }
    }
}
