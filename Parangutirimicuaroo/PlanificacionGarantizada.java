package Proyect1SO;

import java.util.*;

public class PlanificacionGarantizada {
	private List<Proceso> procesos;
    private int tiempoTotal;
    private int cambiosProcesos = 0;
    private List<Proceso> finalizados = new ArrayList<>();
    private List<Proceso> nuncaEjecutados = new ArrayList<>();
    private List<Proceso> procesosMuertos = new ArrayList<>();

    public PlanificacionGarantizada(List<Proceso> procesos, int tiempoSimulacion) {
        this.procesos = new ArrayList<>(procesos);
        this.tiempoTotal = tiempoSimulacion;
    }

    public void ejecutar() {
        System.out.println("\n===== Tabla Inicial de Procesos =====");
        System.out.printf("%-10s %-17s %-12s\n", "Proceso", "Tiempo Restante", "Estado");
        for (Proceso p : procesos) {
            String estado = (p.estado == 1) ? "Listo" : "Bloqueado";
            System.out.printf("%-10d %-17d %-12s\n", p.id, p.tiempoRestante, estado);
        }

        System.out.println("\n===== Ejecutando Planificación Garantizada =====");
        int tiempoTranscurrido = 0;
        
        while (tiempoTranscurrido < tiempoTotal && !procesos.isEmpty()) {
            for (Proceso proceso : new ArrayList<>(procesos)) {
                if (proceso.estado == 1) { // Proceso listo para ejecución
                    int tiempoAsignado = tiempoTotal / procesos.size();
                    int tiempoEjecutado = Math.min(proceso.tiempoRestante, tiempoAsignado);
                    proceso.tiempoRestante -= tiempoEjecutado;
                    tiempoTranscurrido += tiempoEjecutado;
                    cambiosProcesos++;
                    System.out.println("Proceso " + proceso.id + " ejecuta " + tiempoEjecutado + " unidades, tiempo restante: " + proceso.tiempoRestante);
                    
                    if (proceso.tiempoRestante <= 0) {
                        finalizados.add(proceso);
                        procesos.remove(proceso);
                    }
                } else if (proceso.estado == 2) { // Proceso bloqueado
                    if (new Random().nextInt(2) == 1) { // 50% de probabilidad de desbloqueo
                        proceso.estado = 1;
                        System.out.println("Proceso " + proceso.id + " se ha desbloqueado.");
                    } else {
                        proceso.intentosDesbloqueo++;
                        if (proceso.intentosDesbloqueo >= 3) {
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
        System.out.println("Procesos terminados: " + finalizados.stream().map(p -> p.id).toList());
        System.out.println("Procesos que nunca se ejecutaron: " + nuncaEjecutados.stream().map(p -> p.id).toList());
        System.out.println("Procesos en ejecución: " + procesos.stream().map(p -> p.id).toList());
        System.out.println("Procesos muertos por inanición: " + procesosMuertos.stream().map(p -> p.id).toList());
        System.out.println("Cambios de procesos registrados: " + cambiosProcesos);

        System.out.println("\n===== Tabla Final de Procesos =====");
        System.out.printf("%-10s %-17s %-12s\n", "Proceso", "Tiempo Restante", "Estado Final");
        for (Proceso p : finalizados) {
            System.out.printf("%-10d %-17d %-12s\n", p.id, 0, "Finalizado");
        }
        for (Proceso p : procesos) {
            String estadoFinal = (p.tiempoRestante == 0) ? "Finalizado" : "Listo";
            System.out.printf("%-10d %-17d %-12s\n", p.id, p.tiempoRestante, estadoFinal);
        }
    }
}
