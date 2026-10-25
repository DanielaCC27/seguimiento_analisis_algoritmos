import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Lee los 20 articulos en PDF y guarda el abstract de cada uno en un .txt.
 * Usa la libreria PDFBox (lib/pdfbox-app-3.0.3.jar).
 * Uso: java -cp "bin;lib/pdfbox-app-3.0.3.jar" ExtractorAbstracts articulos abstracts_extraidos
 *
 * Regla: el abstract empieza despues de la palabra "Abstract" y termina en la
 * primera de "Keywords" (al inicio de una linea), "Key words", "Introduction" o el simbolo de copyright.
 * Los .txt que salgan vacios o muy largos se deben revisar a mano contra el PDF.
 */
public class ExtractorAbstracts
{
    public static void main(String[] args) throws IOException
    {
        String carpetaPdf = "articulos";
        String carpetaSalida = "abstracts_extraidos";
        if (args.length > 1)
        {
            carpetaPdf = args[0];
            carpetaSalida = args[1];
        }
        File[] archivos = new File(carpetaPdf).listFiles();
        if (archivos == null)
        {
            System.out.println("No encuentro la carpeta " + carpetaPdf);
            return;
        }
        Arrays.sort(archivos);
        new File(carpetaSalida).mkdirs();

        for (int i = 0; i < archivos.length; i++)
        {
            String nombre = archivos[i].getName();
            if (!nombre.toLowerCase().endsWith(".pdf"))
            {
                continue;
            }
            // Los PDF se llaman "1. Titulo.pdf", "2. Titulo.pdf", ...
            int numero = Integer.parseInt(nombre.substring(0, nombre.indexOf('.')));
            String texto = leerPdf(archivos[i]);
            String abstractTexto = obtenerAbstract(texto);
            String nombreSalida = String.format("%02d.txt", numero);
            Files.writeString(new File(carpetaSalida, nombreSalida).toPath(), abstractTexto);

            int palabras = abstractTexto.trim().isEmpty() ? 0 : abstractTexto.trim().split("\\s+").length;
            String aviso = "";
            if (palabras < 50 || palabras > 400)
            {
                aviso = "  <- revisar a mano";
            }
            System.out.println(nombreSalida + ": " + palabras + " palabras" + aviso);
        }
    }

    // Texto de las dos primeras paginas, donde esta el abstract
    public static String leerPdf(File archivo) throws IOException
    {
        PDDocument documento = Loader.loadPDF(archivo);
        PDFTextStripper lector = new PDFTextStripper();
        lector.setEndPage(2);
        lector.setLineSeparator("\n");
        String texto = lector.getText(documento);
        documento.close();
        return texto;
    }

    public static String obtenerAbstract(String texto)
    {
        String minusculas = texto.toLowerCase();
        // Algunas revistas escriben el titulo separado: "A B S T R A C T"
        int inicio = minusculas.indexOf("a b s t r a c t");
        int largoTitulo = 15;
        if (inicio == -1)
        {
            inicio = minusculas.indexOf("abstract");
            largoTitulo = 8;
        }
        if (inicio == -1)
        {
            return "";
        }
        inicio = inicio + largoTitulo;

        String[] marcasFin = { "\nkeywords", "key words", "introduction", "©" };
        int fin = texto.length();
        for (int i = 0; i < marcasFin.length; i++)
        {
            int posicion = minusculas.indexOf(marcasFin[i], inicio);
            if (posicion != -1 && posicion < fin)
            {
                fin = posicion;
            }
        }
        String resultado = texto.substring(inicio, fin).trim();
        // Une las palabras que el PDF corta al final de la linea: "edu-\ncation" -> "education"
        resultado = resultado.replace("-\n", "");
        resultado = resultado.replace("\n", " ");
        if (resultado.startsWith(":"))
        {
            resultado = resultado.substring(1).trim();
        }
        return resultado;
    }
}
