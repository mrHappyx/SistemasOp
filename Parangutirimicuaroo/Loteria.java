package Proyect1SO;

import java.util.*;

public class Loteria {
    private List<Proceso> procesos;
    private Random random;
    
    public Loteria(List<Proceso> procesos) {
        this.procesos = procesos;
        this.random = new Random();
    }
    
    public void ejecutarSimulacion(int tiempoSimulacion) {
        int tiempoActual = 0;
        while (tiempoActual < tiempoSimulacion) {
            Proceso procesoSeleccionado = seleccionarProceso();
            if (procesoSeleccionado != null) {
                System.out.println("Ejecutando proceso " + procesoSeleccionado.id);
                int tiempoEjecutado = Math.min(procesoSeleccionado.tiempoRestante, 3);
                procesoSeleccionado.tiempoRestante -= tiempoEjecutado;
                tiempoActual += tiempoEjecutado;
                if (procesoSeleccionado.tiempoRestante <= 0) {
                    System.out.println("Proceso " + procesoSeleccionado.id + " ha finalizado.");
                    procesos.remove(procesoSeleccionado);
                }
            } else {
                System.out.println("No hay procesos listos para ejecutar.");
                break;
            }
        }
        imprimirReporte();
    }
    
    private Proceso seleccionarProceso() {
        List<Proceso> listaBoletos = new ArrayList<>();
        for (Proceso p : procesos) {
            if (p.estado == 1) {   for (int i = 0; i < p.prioridad; i++) {
                    listaBoletos.add(p);
                }
            }
        }
        return listaBoletos.isEmpty() ? null : listaBoletos.get(random.nextInt(listaBoletos.size()));
    }
    
    private void imprimirReporte() {
        System.out.println("===== Reporte Final =====");
        System.out.println("Procesos restantes: " + procesos.size());
        for (Proceso p : procesos) {
            System.out.println("Proceso " + p.id + " - Tiempo Restante: " + p.tiempoRestante + " - Estado: " + (p.estado == 1 ? "Listo" : "Bloqueado"));
        }
    }
}
