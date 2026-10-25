# Seguimiento 2026-2 – Ranking de términos

Análisis de Algoritmos – Universidad del Quindío
Estudiante: Daniela Castañeda Cartagena y David Esteban Pérez Arevalo 
· Docente: Sergio Augusto Cardona Torres

## Función
Lee los abstracts de 20 artículos (PDF), cuenta la frecuencia de cada término
y genera el top 15 con tres ordenamientos: Counting Sort, Radix Sort y Bucket Sort. 
Mide el tiempo de cada uno en milisegundos.

## Cómo ejecutarlo
1. Tener Java 11 o superior.
2. Poner los 20 PDF en la carpeta `articulos/` (dichos artículos, no se suben por derechos de autor).
3. Abrir la carpeta en VS Code y ejecutar `src/Principal.java`.

## Dónde está cada parte
- `src/Ordenamientos.java`: countingSort, radixSort y bucketSort.
- `src/Principal.java`: medición del tiempo y top 15.
- `src/ExtractorAbstracts.java`: lectura de los PDF.
- `src/Preprocesador.java`: limpieza del texto y conteo de frecuencias.

## Uso de IA
Usamos Claude como apoyo para la versión inicial del código. A partir de ello, se verificó, ajustó, ejecutó y se medieron los tiempos de ejecución de manera manual.
