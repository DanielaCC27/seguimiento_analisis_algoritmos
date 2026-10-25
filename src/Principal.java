import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Ranking de terminos con Counting Sort, Radix Sort y Bucket Sort.
 */
public class Principal
{
    public static void main(String[] args) throws IOException
    {
        // 1. Leer el abstract de cada articulo
        String nombreCarpeta = "abstracts";
        if (args.length > 0)
        {
            nombreCarpeta = args[0];
        }
        File carpeta = new File(nombreCarpeta);
        if (!carpeta.isDirectory())
        {
            System.out.println("No encuentro la carpeta " + carpeta.getAbsolutePath());
            System.out.println("Abre en VS Code la carpeta seguimiento-2026-2 (la que tiene src y abstracts).");
            return;
        }
        File[] archivos = carpeta.listFiles();
        Arrays.sort(archivos);
        String corpus = "";
        int articulos = 0;
        for (int i = 0; i < archivos.length; i++)
        {
            if (archivos[i].getName().endsWith(".txt"))
            {
                corpus = corpus + Files.readString(archivos[i].toPath()) + " ";
                articulos++;
            }
        }

        // 2. Preprocesar y contar frecuencias
        Termino[] terminos = new Preprocesador().contarTerminos(corpus);
        int n = terminos.length;
        Ordenamientos ordenamientos = new Ordenamientos();
        int k = ordenamientos.obtenerMayor(terminos, n);
        System.out.println("Articulos leidos: " + articulos);
        System.out.println("Terminos distintos (n): " + n);
        System.out.println("Frecuencia mayor (k): " + k);
        System.out.println();

        // 3. Medir el tiempo de cada algoritmo
        System.out.println("Tiempo promedio de " + REPETICIONES + " ejecuciones:");
        System.out.printf("Counting Sort: %.4f ms%n", medirTiempo(ordenamientos, terminos, 1));
        System.out.printf("Radix Sort:    %.4f ms%n", medirTiempo(ordenamientos, terminos, 2));
        System.out.printf("Bucket Sort:   %.4f ms%n", medirTiempo(ordenamientos, terminos, 3));
        System.out.println();

        // 4. Ordenar y mostrar los 15 terminos mas frecuentes (desde el final)
        Termino[] porCounting = ordenamientos.countingSort(terminos, n);
        Termino[] porRadix = terminos.clone();
        ordenamientos.radixSort(porRadix, n);
        Termino[] porBucket = ordenamientos.bucketSort(terminos, n);

        System.out.println("Top 15     Counting Sort          Radix Sort             Bucket Sort");
        boolean iguales = true;
        for (int i = 0; i < 15; i++)
        {
            Termino c = porCounting[n - 1 - i];
            Termino r = porRadix[n - 1 - i];
            Termino b = porBucket[n - 1 - i];
            System.out.printf("%2d         %-16s %4d  %-16s %4d  %-16s %4d%n", i + 1,
                c.palabra, c.frecuencia, r.palabra, r.frecuencia, b.palabra, b.frecuencia);
            if (c != r || c != b)
            {
                iguales = false;
            }
        }
        System.out.println();
        System.out.println("Los tres algoritmos dan el mismo top 15: " + iguales);
    }

    static final int REPETICIONES = 1000;

    public static double medirTiempo(Ordenamientos ordenamientos, Termino[] terminos, int algoritmo)
    {
        int n = terminos.length;
        long tiempoTotal = 0;
        for (int r = 0; r < REPETICIONES; r++)
        {
            Termino[] copia = terminos.clone();
            long tiempoInicial = System.nanoTime();
            if (algoritmo == 1)
            {
                ordenamientos.countingSort(copia, n);
            }
            else if (algoritmo == 2)
            {
                ordenamientos.radixSort(copia, n);
            }
            else
            {
                ordenamientos.bucketSort(copia, n);
            }
            long tiempoFinal = System.nanoTime();
            tiempoTotal = tiempoTotal + (tiempoFinal - tiempoInicial);
        }
        double promedioNanosegundos = tiempoTotal / (double) REPETICIONES;
        return promedioNanosegundos / 1000000.0;
    }
}
