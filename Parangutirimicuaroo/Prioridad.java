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
            Proceso proceso = new Proceso(i + 1, tiempoRestante, estado, prioridad);
            procesos.add(proceso);
            todosLosProcesos.add(proceso);
            if (estado == 1) {
                noEjecutados.add(proceso);
            }
        }
    }

    public void mostrarTablaInicial() {
        System.out.println("\nTabla Inicial de Procesos:");
        System.out.printf("%-10s%-20s%-15s%-10s\n", "Proceso", "Tiempo Restante", "Estado", "Prioridad");
        for (Proceso p : procesos) {
            System.out.printf("   P%-5d%-20d%-15s%-10d\n", p.id, p.tiempoRestante, 
                (p.estado == 1) ? "Listo" : "Bloqueado", p.prioridad);
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
        System.out.printf("%-10s%-20s%-15s%-10s\n", "Proceso", "Tiempo Restante", "Estado", "Prioridad");
        for (Proceso p : todosLosProcesos) {
            String estadoFinal = (p.tiempoRestante == 0) ? "Finalizado" : (p.estado == 1 ? "Listo" : "Bloqueado");
            System.out.printf("   P%-5d%-20d%-15s%-10d\n", p.id, p.tiempoRestante, estadoFinal, p.prioridad);
        }
    }
}

