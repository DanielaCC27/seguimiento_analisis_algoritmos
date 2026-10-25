/**
 * Nodo de la cola enlazada.
 */
public class NodoTermino
{
    public Termino valor;
    public NodoTermino siguiente;

    public NodoTermino(Termino a)
    {
        valor = a;
        siguiente = null;
    }
}
