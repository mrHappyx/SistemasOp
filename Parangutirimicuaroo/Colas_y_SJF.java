package Proyect1SO;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

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
    
    public Colas_y_SJF(boolean esApropiativo, boolean esSJF) {
        this.esApropiativo = esApropiativo;
        this.esSJF = esSJF;
        generarProcesos();
    }
    
    private void generarProcesos() {
        int numProcesos = new Random().nextInt(10) + 1; 
        for (int i = 0; i < numProcesos; i++) {
            Proceso proceso = new Proceso(i + 1);
            listaProcesos.add(proceso);
            colasPrioridad.computeIfAbsent(proceso.prioridad, k -> new PriorityQueue<>(Comparator.comparingInt(p -> esSJF ? p.tiempoRestante : p.prioridad)))
                          .add(proceso);
        }
    }
    
    public void ejecutarSimulacion(int tiempoMonitoreo) {
        System.out.println("\n--- Tabla Inicial de Procesos ---");
        System.out.println("Proceso | Tiempo Restante | Estado Inicial | Prioridad");
        for (Proceso p : listaProcesos) {
            System.out.println(p.id + "\t| " + p.tiempoRestante + "\t| " + (p.estado == 1 ? "Listo" : "Bloqueado") + "\t| " + p.prioridadOriginal);
        }

        while (tiempoMonitoreo > 0 && !simulacionTerminadaPorInanicion) {
            actualizarEstadosProcesos();
            planificarProcesos();
            tiempoMonitoreo--;
        }
        mostrarReporte();
    }
    
    private void actualizarEstadosProcesos() {
        Iterator<Proceso> iterador = colaBloqueados.iterator();
        while (iterador.hasNext()) {
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
        }
    }
    
    private void planificarProcesos() {
        if (colasPrioridad.isEmpty() || simulacionTerminadaPorInanicion) return;
        
        for (int prioridad : colasPrioridad.keySet()) {
            Queue<Proceso> cola = colasPrioridad.get(prioridad);
            if (cola.isEmpty()) continue;
            
            Proceso procesoActual = cola.poll();
            if (procesoActual.estado == 2) {
                colaBloqueados.add(procesoActual);
            } else {
                int tiempoEjecutado = esApropiativo ? procesoActual.tiempoRestante : procesoActual.tiempoRestante;
                procesoActual.usoCPU++;
                procesoActual.tiempoRestante -= tiempoEjecutado;
                procesoActual.prioridad = procesoActual.usoCPU;
                logEjecucion.add("Proceso " + procesoActual.id + " ejecuta " + tiempoEjecutado + " unidades, tiempo restante: " + procesoActual.tiempoRestante);
                cambiosDeProceso++;
                
                if (procesoActual.tiempoRestante > 0) {
                    colasPrioridad.computeIfAbsent(procesoActual.prioridad, k -> new PriorityQueue<>(Comparator.comparingInt(p -> esSJF ? p.tiempoRestante : p.prioridad)))
                                  .add(procesoActual);
                } else {
                    logEjecucion.add("Proceso " + procesoActual.id + " finaliza.");
                }
                break;
            }
        }
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
        System.out.println("Proceso | Tiempo Restante | Estado Final | Prioridad");
        for (Proceso p : listaProcesos) {
            String estadoFinal = p.tiempoRestante <= 0 ? "Finalizado" : (procesosMuertosPorInanicion.contains(p.id) ? "Muerto por inanición" : "Bloqueado");
            System.out.println(p.id + "\t| " + p.tiempoRestante + "\t| " + estadoFinal + "\t| " + p.prioridadOriginal);
        }
    }
}