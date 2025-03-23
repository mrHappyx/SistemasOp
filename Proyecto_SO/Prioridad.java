package Proyect1SO;

import java.util.*;

public class Prioridad {
    private List<Proceso> procesos;
    private List<Proceso> todosLosProcesos;
    private List<Proceso> noEjecutados;
    private List<Proceso> finalizados;
    private int cambios;

    public Prioridad() {
        this.procesos = new ArrayList<>();
        this.todosLosProcesos = new ArrayList<>();
        this.noEjecutados = new ArrayList<>();
        this.finalizados = new ArrayList<>();
        this.cambios = 0;
    }

    public void generarProcesos(int numProcesos, Random random) {
        for (int i = 0; i < numProcesos; i++) {
            int tiempoRestante = random.nextInt(8) + 3;
            int estado = random.nextInt(2) + 1;
            int prioridad = random.nextInt(3) + 1;
            Proceso proceso = new Proceso(i + 1, tiempoRestante, estado, prioridad, -1, random.nextInt(10) + 1);
            procesos.add(proceso);
            todosLosProcesos.add(proceso);
            if (estado == 1) {
                noEjecutados.add(proceso);
            }
        }
    }

    public void mostrarTablaInicial() {
        System.out.println("\nTabla Inicial de Procesos:");
        PBC.imprimirEncabezadoTabla();
        for (Proceso p : procesos) {
            PBC pbc = new PBC(p.id, p.tiempoRestante, (p.estado == 1) ? "Listo" : "Bloqueado", Integer.toString(p.prioridadOriginal), Integer.toString(p.usuario), Integer.toString(p.boletos));
            pbc.imprimirFilaTabla();
        }
    }

    public void ejecutarSimulacion(int tipoPlanificacion, int quantum, int tiempoSimulacion, Random random) {
        for (int tiempo = 0; tiempo < tiempoSimulacion && !procesos.isEmpty(); tiempo++) {
            procesos.sort(Comparator.comparingInt(p -> p.prioridad));

            Proceso actual = procesos.get(0);
            noEjecutados.remove(actual);

            if (actual.estado == 2) {
                int intentosBloqueo = 0;
                while (intentosBloqueo < 3 && actual.estado == 2) {
                    if (random.nextInt(2) == 1) {
                        actual.estado = 1;
                        break;
                    }
                    intentosBloqueo++;
                }

                if (actual.estado == 2) {
                    System.out.println("Muerte del proceso P" + actual.id + " por inanición");
                    break;
                }
            }

            int tiempoEjecutado = (tipoPlanificacion == 1) ? Math.min(quantum, actual.tiempoRestante) : actual.tiempoRestante;
            actual.tiempoRestante -= tiempoEjecutado;
            actual.usoCPU++; // Incrementamos el uso de CPU
            cambios++;

            if (actual.estado == 1) {
                if (actual.tiempoRestante <= 0) {
                    System.out.println("Proceso " + actual.id + ": Ejecuta " + tiempoEjecutado + " unidades y finaliza.");
                    finalizados.add(actual);
                    procesos.remove(actual);
                } else {
                    System.out.println("Proceso " + actual.id + ": Ejecuta " + tiempoEjecutado + " unidades, queda con " + actual.tiempoRestante + " restantes.");
                }
            } else {
                System.out.println("Proceso " + actual.id + ": No ejecuta (permanece bloqueado).");
            }
        }
    }

    public void mostrarInformeFinal() {
        System.out.println("\n--- Informe Final ---");

        System.out.print("Procesos terminados: ");
        if (finalizados.isEmpty()) System.out.println("Ninguno.");
        else System.out.println(finalizados.stream().map(p -> "P" + p.id).toList());

        System.out.print("Procesos que nunca entraron en ejecución: ");
        List<Proceso> nuncaEjecutados = new ArrayList<>(noEjecutados);
        if (nuncaEjecutados.isEmpty()) System.out.println("Ninguno.");
        else System.out.println(nuncaEjecutados.stream().map(p -> "P" + p.id).toList());

        System.out.print("Procesos aún en ejecución: ");
        if (procesos.isEmpty()) System.out.println("Ninguno.");
        else System.out.println(procesos.stream().map(p -> "P" + p.id).toList());

        System.out.println("Cambios de procesos registrados: " + cambios + ".");

        System.out.println("\nTabla Final de Procesos:");
        PBC.imprimirEncabezadoTabla();
        for (Proceso p : todosLosProcesos) {
            String estadoFinal = (p.tiempoRestante == 0) ? "Finalizado" : (p.estado == 1 ? "Listo" : "Bloqueado");
            PBC pbc = new PBC(p.id, p.tiempoRestante, estadoFinal, Integer.toString(p.prioridadOriginal), Integer.toString(p.usuario), Integer.toString(p.boletos));
            pbc.imprimirFilaTabla();
        }
    }
}