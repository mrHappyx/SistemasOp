package Proyect1SO;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulador {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Proceso> procesos = new ArrayList<>();
        Random random = new Random();

        // Generación de procesos aleatorios
        int cantidadProcesos = random.nextInt(10) + 1;
        for (int i = 1; i <= cantidadProcesos; i++) {
            int tiempoRestante = random.nextInt(8) + 3;
            int estado = random.nextBoolean() ? 1 : 2; 
            int usuario = random.nextInt(3) + 1; // Usuarios entre 1 y 3 (para Planificación Equitativa)
            procesos.add(new Proceso(i, tiempoRestante, estado, 1, usuario));
        }

        int opcion;
        int tiempoSimulacion = random.nextInt(16) + 20;

        do {
            // Menú de selección del algoritmo de planificación
            System.out.println("\nSeleccione el algoritmo de planificación:");
            System.out.println("1. Múltiples colas de prioridad");
            System.out.println("2. Proceso Más Corto Primero (SJF)");
            System.out.println("3. Boletos de Lotería");
            System.out.println("4. Round Robin");
            System.out.println("5. Prioridad");
            System.out.println("6. Planificación Garantizada");
            System.out.println("7. Planificación Equitativa");
            System.out.print("Opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\nEjecutando Múltiples Colas de Prioridad:");
                    System.out.println("Tiempo total de simulación: " + tiempoSimulacion);
                    Colas_y_SJF MCP = new Colas_y_SJF(true, false, tiempoSimulacion);
                    MCP.ejecutarSimulacion();
                    break;
                case 2:
                    System.out.println("\nEjecutando Proceso Más Corto Primero (SJF):");
                    System.out.println("Tiempo total de simulación: " + tiempoSimulacion);
                    Colas_y_SJF SJF = new Colas_y_SJF(true, true, tiempoSimulacion);
                    SJF.ejecutarSimulacion();
                    break;
                case 3:
                    System.out.println("\nEjecutando Planificación por Boletos de Lotería:");
                    System.out.println("Tiempo total de simulación: " + tiempoSimulacion);
                    LoteriaProceso loteria = new LoteriaProceso(procesos);
                    loteria.iniciarSimulacion(tiempoSimulacion);
                    break;
                case 4:
                    System.out.println("\nEjecutando Round Robin:");
                    System.out.print("Seleccione el tipo de planificación para Round Robin \n(1 = Apropiativa, 2 = No Apropiativa): ");
                    int tipoRR = scanner.nextInt();
                    RoundRobin rr = new RoundRobin(tipoRR);
                    rr.mostrarTablaInicial();
                    rr.ejecutar();
                    break;
                case 5:
                    System.out.println("\nEjecutando Planificación por Prioridad:");
                    System.out.print("Seleccione el tipo de planificación para Prioridad \n(1 = Apropiativa, 2 = No Apropiativa): ");
                    int tipoPrioridad = scanner.nextInt();
                    System.out.println("Tiempo total de simulación: " + tiempoSimulacion);
                    Prioridad prioridad = new Prioridad();
                    prioridad.generarProcesos(cantidadProcesos, random);
                    prioridad.mostrarTablaInicial();
                    prioridad.ejecutarSimulacion(tipoPrioridad, 4, tiempoSimulacion, random);
                    prioridad.mostrarInformeFinal();
                    break;
                case 6:
                    System.out.println("\nEjecutando Planificación Garantizada:");
                    System.out.println("Tiempo total de simulación: " + tiempoSimulacion);
                    PlanificacionGarantizada garantizada = new PlanificacionGarantizada(procesos, tiempoSimulacion);
                    garantizada.ejecutar();
                    break;
                case 7:
                    System.out.println("\nEjecutando Planificación Equitativa:");
                    System.out.println("Tiempo total de simulación: " + tiempoSimulacion);
                    PlanificacionEquitativa equitativa = new PlanificacionEquitativa(procesos, tiempoSimulacion);
                    equitativa.ejecutar();
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.\n");
            }
        } while (opcion < 1 || opcion > 7);
    }
}