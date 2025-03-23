package Proyect1SO;

import java.util.*;

public class RoundRobin {
    private Queue<Proceso> cola;
    private List<Proceso> procesos;
    private int tipoPlanificacion;
    private int quantum;
    private int tiempoSimulacion;
    private int cambiosProcesos = 0;
    private boolean simulacionTerminadaPorInanicion = false;
    private Random random = new Random();

    public RoundRobin(int tipoPlanificacion) {
        this.tipoPlanificacion = tipoPlanificacion;
        this.quantum = (tipoPlanificacion == 1) ? random.nextInt(4) + 2 : -1; // Quantum aleatorio entre 2 y 5
        this.tiempoSimulacion = random.nextInt(16) + 20; // Tiempo entre 20 y 35 unidades
        this.procesos = generarProcesos();
        this.cola = new LinkedList<>(procesos);
    }

    private List<Proceso> generarProcesos() {
        List<Proceso> lista = new ArrayList<>();
        int numProcesos = random.nextInt(10) + 1; // Entre 1 y 10 procesos
        for (int i = 1; i <= numProcesos; i++) {
            int tiempoRestante = random.nextInt(8) + 3; // Entre 3 y 10 unidades
            int estado = random.nextInt(2) + 1; // 1 = Listo, 2 = Bloqueado
            lista.add(new Proceso(i, tiempoRestante, estado, 1, random.nextInt(3) + 1, random.nextInt(10) + 1)); // Prioridad por defecto
        }
        return lista;
    }

    public void mostrarTablaInicial() {
        System.out.println("\n===== Tabla Inicial de Procesos =====");
        PBC.imprimirEncabezadoTabla();
        for (Proceso p : procesos) {
            PBC pbc = new PBC(p.id, p.tiempoRestante, (p.estado == 1 ? "Listo" : "Bloqueado"), Integer.toString(p.prioridadOriginal), Integer.toString(p.usuario), Integer.toString(p.boletos));
            pbc.imprimirFilaTabla();
        }
        System.out.println();
    }

    public void ejecutar() {
        int tiempoTranscurrido = 0;

        System.out.println("\n===== Configuración de la Simulación =====");
        System.out.println("Tipo de planificación: " + (tipoPlanificacion == 1 ? "Apropiativa" : "No Apropiativa"));
        System.out.println("Algoritmo seleccionado: Round Robin");
        System.out.println("Tiempo de simulación: " + tiempoSimulacion);
        if (tipoPlanificacion == 1) {
            System.out.println("Quantum: " + quantum);
        }

        while (tiempoTranscurrido < tiempoSimulacion && !cola.isEmpty()) {
            Proceso actual = cola.poll();

            if (actual.estado == 2) {
                int intentos = 0;
                while (actual.estado == 2 && intentos < 3) {
                    if (random.nextInt(2) == 1) {
                        actual.estado = 1;
                        System.out.println("Proceso " + actual.id + " se ha desbloqueado.");
                    } else {
                        intentos++;
                    }
                }
                if (actual.estado == 2) {
                    System.out.println("Muerte del proceso " + actual.id + " por inanición.");
                    simulacionTerminadaPorInanicion = true;
                    break;
                }
            }

            actual.ejecutado = true;
            int tiempoEjecutado = (tipoPlanificacion == 1) ? Math.min(actual.tiempoRestante, quantum) : actual.tiempoRestante;
            actual.tiempoRestante -= tiempoEjecutado;
            actual.usoCPU++; // Incrementamos el uso de CPU
            tiempoTranscurrido += tiempoEjecutado;
            cambiosProcesos++;

            System.out.println("Ejecutando proceso " + actual.id + " por " + tiempoEjecutado + " unidades.");

            if (actual.tiempoRestante > 0) {
                cola.add(actual);
            } else {
                System.out.println("Proceso " + actual.id + " ha finalizado.");
            }
        }

        if (simulacionTerminadaPorInanicion) {
            System.out.println("\nLa simulación se terminó debido a inanición.");
        }

        generarReporteFinal();
    }

    public void generarReporteFinal() {
        List<Integer> terminados = new ArrayList<>();
        List<Integer> enEjecucion = new ArrayList<>();
        List<Integer> nuncaEjecutados = new ArrayList<>();

        for (Proceso p : procesos) {
            if (!p.ejecutado) {
                nuncaEjecutados.add(p.id);
            } else if (p.tiempoRestante == 0) {
                terminados.add(p.id);
            } else {
                enEjecucion.add(p.id);
            }
        }

        System.out.println("\n===== Reporte Final =====");
        System.out.println("Procesos terminados: " + (terminados.isEmpty() ? "Ninguno" : terminados));
        System.out.println("Procesos que nunca se ejecutaron: " + (nuncaEjecutados.isEmpty() ? "Ninguno" : nuncaEjecutados));
        System.out.println("Procesos en ejecución: " + (enEjecucion.isEmpty() ? "Ninguno" : enEjecucion));
        System.out.println("Cambios de procesos registrados: " + cambiosProcesos);

        mostrarTablaFinal();
    }

    public void mostrarTablaFinal() {
        System.out.println("\n===== Tabla Final de Procesos =====");
        PBC.imprimirEncabezadoTabla();
        for (Proceso p : procesos) {
            PBC pbc = new PBC(p.id, p.tiempoRestante, p.estadoFinal(), Integer.toString(p.prioridadOriginal), Integer.toString(p.usuario), Integer.toString(p.boletos));
            pbc.imprimirFilaTabla();
        }
    }
}