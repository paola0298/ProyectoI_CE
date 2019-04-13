package Structures;

public class Node<T> {
    private T value;
    private Node<T> next;

    /**
     * Constructor vacio del nodo
     */
    public Node(){}

    /**
     * Constructor del nodo
     * @param value Valor a ingresar en el nodo
     */
    public Node(T value) {
        this.value = value;
        this.next = null;
    }

    /**
     * @return Devuelve el valor del nodo
     */
    public T getValue() {
        return value;
    }

    /**
     * @param value Retorna el valor del nodo
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return Retorna el siguiente nodo
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * @param next Establece el nodo siguiente
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

}
