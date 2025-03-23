package Proyect1SO;

import java.util.*;

public class LoteriaProceso {
    private List<Proceso> procesos;
    private List<Proceso> procesosOriginales;
    private Loteria loteria;
    private boolean esApropiativo;

    public LoteriaProceso(List<Proceso> procesos) {
        this.procesos = new ArrayList<>(procesos); 
        this.procesosOriginales = new ArrayList<>(procesos);
        this.loteria = new Loteria(this.procesos);
    }

    public void configurarModoLoteria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione el tipo de planificación para el método de lotería:");
        System.out.println("1. Apropiativa ");
        System.out.println("2. No Apropiativa ");
        System.out.print("Opción: ");
        int opcion = scanner.nextInt();
        esApropiativo = (opcion == 1);
    }

    public void iniciarSimulacion(int tiempoSimulacion) {
        configurarModoLoteria();
        imprimirTablaInicial();
        loteria.ejecutarSimulacion(tiempoSimulacion);
        imprimirReporteFinal();
        imprimirTablaFinal();
    }

    private void imprimirTablaInicial() {
        System.out.println("===== Tabla Inicial de Procesos =====");
        System.out.printf("%-10s %-17s %-12s %-10s\n", "Proceso", "Tiempo Restante", "Estado", "Boletos");
        for (Proceso p : procesosOriginales) {
            String estado = (p.estado == 1) ? "Listo" : "Bloqueado";
            System.out.printf("%-10d %-17d %-12s %-10d\n", p.id, p.tiempoRestante, estado, p.prioridad);
        }
    }

    private void imprimirReporteFinal() {
        int terminados = 0;
        int cambios=0;
        List<Integer> noEjecutados = new ArrayList<>();
        int cambiosProcesos = 0;
        
        for (Proceso p : procesos) {
            if (p.tiempoRestante == 0) {
                terminados++;
            } else {
                noEjecutados.add(p.id);
                cambios++;
            }
        }

        System.out.println("\n===== Reporte Final =====");
        System.out.println("Procesos terminados: [" + cambios + "]");
        System.out.println("Procesos que nunca se ejecutaron: " + noEjecutados);
        System.out.println("Procesos en ejecución: Ninguno");
        System.out.println("Cambios de procesos registrados: " + cambios);
    }

    private void imprimirTablaFinal() {
        System.out.println("\n===== Tabla Final de Procesos =====");
        System.out.printf("%-10s %-17s %-15s\n", "Proceso", "Tiempo Restante", "Estado Final");
        for (Proceso p : procesos) {
            String estadoFinal;
            if (p.tiempoRestante == 0) {
                estadoFinal = "finalizados";
            } else if (p.estado == 1) {
                estadoFinal = "Listo";
            } else {
                estadoFinal = "bloqueado";
            }
            System.out.printf("%-10d %-17d %-15s\n", p.id, p.tiempoRestante, estadoFinal);
        }
        
    }
    
}





