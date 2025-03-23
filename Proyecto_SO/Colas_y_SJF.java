package Proyect1SO;

import java.util.*;

public class Colas_y_SJF {
    private List<Proceso> listaProcesos = new ArrayList<>(); 
    private Map<Integer, Queue<Proceso>> colasPrioridad = new HashMap<>();
    private Queue<Proceso> colaBloqueados = new LinkedList<>(); 
    private boolean esApropiativo;
    private boolean esSJF;
    private int cambiosDeProceso = 0; 
    private List<String> logEjecucion = new ArrayList<>(); 
    private boolean simulacionTerminadaPorInanicion = false;
    private List<Integer> procesosMuertosPorInanicion = new ArrayList<>();
    private int tiempoSimulacion; // Tiempo límite de simulación
    
    public Colas_y_SJF(boolean esApropiativo, boolean esSJF, int tiempoSimulacion) {
        this.esApropiativo = esApropiativo;
        this.esSJF = esSJF;
        this.tiempoSimulacion = tiempoSimulacion;
        generarProcesos();
    }
    
    private void generarProcesos() {
        int numProcesos = new Random().nextInt(10) + 1; 
        for (int i = 0; i < numProcesos; i++) {
            Proceso proceso = new Proceso(i + 1, new Random().nextInt(8) + 3, new Random().nextInt(2) + 1, new Random().nextInt(3) + 1, 0); // Usuario es 0
            listaProcesos.add(proceso);
            colasPrioridad.computeIfAbsent(proceso.prioridad, k -> new PriorityQueue<>(Comparator.comparingInt(p -> esSJF ? p.tiempoRestante : p.prioridad)))
                          .add(proceso);
        }
    }
    
    public void ejecutarSimulacion() {
        System.out.println("\n--- Tabla Inicial de Procesos ---");
        PBC.imprimirEncabezadoTabla();
        for (Proceso p : listaProcesos) {
            PBC pbc = new PBC(p.id, p.tiempoRestante, (p.estado == 1 ? "Listo" : "Bloqueado"), Integer.toString(p.prioridadOriginal), "---", Integer.toString(p.boletos)); // Usuario es "---"
            pbc.imprimirFilaTabla();
        }

        int tiempoTranscurrido = 0;

        // Intentar desbloquear procesos en el primer ciclo
        tiempoTranscurrido += actualizarEstadosProcesos();

        // Iterar hasta que el tiempo de simulación se agote o ocurra una inanición
        while (tiempoTranscurrido < tiempoSimulacion && !simulacionTerminadaPorInanicion) {
            int tiempoUsado = planificarProcesos(tiempoSimulacion - tiempoTranscurrido);
            tiempoTranscurrido += tiempoUsado;
            tiempoTranscurrido += actualizarEstadosProcesos();
        }
        mostrarReporte();
    }
    
    private int actualizarEstadosProcesos() {
        Iterator<Proceso> iterador = colaBloqueados.iterator();
        int tiempoUtilizado = 0;

        while (iterador.hasNext() && tiempoUtilizado < tiempoSimulacion) {
            Proceso p = iterador.next();
            if (new Random().nextBoolean()) {
                p.estado = 1;
                p.intentosDesbloqueo = 0;
                colasPrioridad.computeIfAbsent(p.prioridad, k -> new PriorityQueue<>(Comparator.comparingInt(pro -> esSJF ? pro.tiempoRestante : pro.prioridad)))
                              .add(p);
                iterador.remove();
                logEjecucion.add("Proceso " + p.id + " se desbloquea.");
            } else {
                p.intentosDesbloqueo++;
                if (p.intentosDesbloqueo >= 3) {
                    logEjecucion.add("Muerte del proceso " + p.id + " por inanición.");
                    procesosMuertosPorInanicion.add(p.id);
                    iterador.remove();
                    simulacionTerminadaPorInanicion = true;
                }
            }
            tiempoUtilizado++;
        }
        return tiempoUtilizado;
    }
    
    private int planificarProcesos(int tiempoRestanteSimulacion) {
        if (colasPrioridad.isEmpty() || simulacionTerminadaPorInanicion) return 0;

        int tiempoUtilizado = 0;

        if (esSJF) {
            // Si es SJF, ordenar todos los procesos por tiempo restante y ejecutarlos en ese orden
            List<Proceso> procesosOrdenados = new ArrayList<>();
            for (Queue<Proceso> cola : colasPrioridad.values()) {
                procesosOrdenados.addAll(cola);
            }
            procesosOrdenados.sort(Comparator.comparingInt(p -> p.tiempoRestante));

            for (Proceso procesoActual : procesosOrdenados) {
                if (tiempoUtilizado >= tiempoRestanteSimulacion) break;

                if (procesoActual.estado == 2) {
                    colaBloqueados.add(procesoActual);
                } else {
                    int tiempoEjecutado = Math.min(procesoActual.tiempoRestante, tiempoRestanteSimulacion - tiempoUtilizado);
                    tiempoUtilizado += tiempoEjecutado; // Incrementar el tiempo utilizado antes de actualizar el proceso
                    procesoActual.usoCPU++; // Incrementamos el uso de CPU
                    procesoActual.tiempoRestante -= tiempoEjecutado;
                    logEjecucion.add("Proceso " + procesoActual.id + " ejecuta " + tiempoEjecutado + " unidades, tiempo restante: " + procesoActual.tiempoRestante);
                    cambiosDeProceso++;

                    if (procesoActual.tiempoRestante > 0) {
                        colasPrioridad.computeIfAbsent(procesoActual.prioridad, k -> new PriorityQueue<>(Comparator.comparingInt(p -> esSJF ? p.tiempoRestante : p.prioridad)))
                                      .add(procesoActual);
                    } else {
                        logEjecucion.add("Proceso " + procesoActual.id + " finaliza.");
                    }
                }
            }
        } else {
            // Asegurarse de que las prioridades se procesen en el orden correcto
            List<Integer> prioridadesOrdenadas = new ArrayList<>(colasPrioridad.keySet());
            Collections.sort(prioridadesOrdenadas);

            for (int prioridad : prioridadesOrdenadas) {
                Queue<Proceso> cola = colasPrioridad.get(prioridad);
                while (!cola.isEmpty() && tiempoUtilizado < tiempoRestanteSimulacion) {
                    Proceso procesoActual = cola.poll();
                    if (procesoActual.estado == 2) {
                        colaBloqueados.add(procesoActual);
                    } else {
                        int tiempoEjecutado = Math.min(procesoActual.tiempoRestante, tiempoRestanteSimulacion - tiempoUtilizado);
                        tiempoUtilizado += tiempoEjecutado; // Incrementar el tiempo utilizado antes de actualizar el proceso
                        procesoActual.usoCPU++; // Incrementamos el uso de CPU
                        procesoActual.tiempoRestante -= tiempoEjecutado;
                        logEjecucion.add("Proceso " + procesoActual.id + " ejecuta " + tiempoEjecutado + " unidades, tiempo restante: " + procesoActual.tiempoRestante);
                        cambiosDeProceso++;

                        if (procesoActual.tiempoRestante > 0) {
                            colasPrioridad.computeIfAbsent(procesoActual.prioridad, k -> new PriorityQueue<>(Comparator.comparingInt(p -> esSJF ? p.tiempoRestante : p.prioridad)))
                                          .add(procesoActual);
                        } else {
                            logEjecucion.add("Proceso " + procesoActual.id + " finaliza.");
                        }

                        // Si el tiempo de simulación se ha agotado, actualizar el proceso y detener la simulación
                        if (tiempoUtilizado >= tiempoRestanteSimulacion) {
                            break;
                        }
                    }
                }
                // Si se ha utilizado todo el tiempo restante de simulación, detener la planificación
                if (tiempoUtilizado >= tiempoRestanteSimulacion) {
                    break;
                }
            }
        }

        return tiempoUtilizado;
    }
    
    private void mostrarReporte() {
        System.out.println("\n--- Ejecución de Procesos ---");
        logEjecucion.forEach(System.out::println);

        List<Integer> finalizados = new ArrayList<>();
        List<Integer> enEjecucion = new ArrayList<>();
        List<Integer> nuncaEjecutados = new ArrayList<>();
        
        for (Proceso p : listaProcesos) {
            if (p.tiempoRestante <= 0) {
                finalizados.add(p.id);
            } else if (p.usoCPU == 0) {
                nuncaEjecutados.add(p.id);
            } else {
                enEjecucion.add(p.id);
            }
        }
        
        System.out.println("\n--- Informe Final ---");
        System.out.println("Procesos terminados: " + finalizados);
        System.out.println("Procesos que nunca se ejecutaron: " + nuncaEjecutados);
        System.out.println("Procesos en ejecución: " + enEjecucion);
        System.out.println("Cambios de procesos registrados: " + cambiosDeProceso);
        
        if (simulacionTerminadaPorInanicion) {
            System.out.println("Simulación terminada debido a inanición de procesos: " + procesosMuertosPorInanicion);
        }
        
        System.out.println("\n--- Tabla Final ---");
        PBC.imprimirEncabezadoTabla();
        for (Proceso p : listaProcesos) {
            String estadoFinal = p.tiempoRestante <= 0 ? "Finalizado" : (procesosMuertosPorInanicion.contains(p.id) ? "Muerto por inanición" : "Bloqueado");
            PBC pbc = new PBC(p.id, p.tiempoRestante, estadoFinal, Integer.toString(p.prioridadOriginal), "---", Integer.toString(p.boletos)); // Usuario es "---"
            pbc.imprimirFilaTabla();
        }
    }
}