package fechashoras;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FechasHoras {

    public static void main(String[] args) {
        long inicioEjecucion;
        long finEjecucion;
        double tiempoEjecucion;

        var fechaActual = LocalDate.now();
        var horaActual = LocalTime.now();

        var formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var formatoHora = DateTimeFormatter.ofPattern("hh:mm:ss a");

        var fechaFormateada = fechaActual.format(formatoFecha);
        var horaFormateada = horaActual.format(formatoHora);

        System.out.println("Fecha Formateada: " + fechaFormateada);
        System.out.println("Hora Formateada: " + horaFormateada);

        inicioEjecucion = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            System.out.println("Ejecutando Ciclo For En Java");
        }

        finEjecucion = System.nanoTime();
        tiempoEjecucion = (finEjecucion - inicioEjecucion) / 1000000.0;

        System.out.println("Tiempo de Ejecucion: " + tiempoEjecucion + " milisegundos");
    }

}
