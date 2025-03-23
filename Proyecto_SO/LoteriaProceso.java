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
        PBC.imprimirEncabezadoTabla();
        for (Proceso p : procesosOriginales) {
            PBC pbc = new PBC(p.id, p.tiempoRestante, (p.estado == 1 ? "Listo" : "Bloqueado"), Integer.toString(p.prioridadOriginal), Integer.toString(p.usuario), Integer.toString(p.boletos));
            pbc.imprimirFilaTabla();
        }
    }

    private void imprimirReporteFinal() {
        List<Integer> noEjecutados = new ArrayList<>();
        int terminados = 0;
        int cambios = 0;
        
        for (Proceso p : procesos) {
            if (p.tiempoRestante == 0) {
                terminados++;
            } else {
                noEjecutados.add(p.id);
                cambios++;
            }
        }

        System.out.println("\n===== Reporte Final =====");
        System.out.println("Procesos terminados: [" + terminados + "]");
        System.out.println("Procesos que nunca se ejecutaron: " + noEjecutados);
        System.out.println("Procesos en ejecución: Ninguno");
        System.out.println("Cambios de procesos registrados: " + cambios);
    }

    private void imprimirTablaFinal() {
        System.out.println("\n===== Tabla Final de Procesos =====");
        PBC.imprimirEncabezadoTabla();
        for (Proceso p : procesosOriginales) {
            String estadoFinal = (p.tiempoRestante == 0) ? "Finalizado" : (p.estado == 1 ? "Listo" : "Bloqueado");
            PBC pbc = new PBC(p.id, p.tiempoRestante, estadoFinal, Integer.toString(p.prioridadOriginal), Integer.toString(p.usuario), Integer.toString(p.boletos));
            pbc.imprimirFilaTabla();
        }
    }
}