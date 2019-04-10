package Structures;

/**
 * Lista circular genérica
 *
 * @param <T> Tipo de dato a almacenar en la lista
 * @author marlon
 */
public class CircularList<T> {
    private int size;
    private Node<T> head;
    private Node<T> current;
    private Node<T> tail;

    public CircularList() {
        size = 0;
        head = tail = current = null;
    }

    /**
     * Añande un elemento al final de la lista
     *
     * @param element Elemento a añadir
     */
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (size == 0) {
            head = tail = current = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
            tail.setNext(head);
        }
        size++;
    }

    /**
     * Añande un elemento en el índice especificado
     * @param element Elemento a añadir
     * @param index Índice donde añadir el elemento
     */
    public void addAt(T element, int index) {
        if (size == 0) {
            add(element);
        }
        else if (index < 0 || index > size-1) {
            throw new IndexOutOfBoundsException("El índice dado está fuera del rango de la lista");
        }
        else {
            Node<T> newNode = new Node<>(element);
            Node<T> temp = head;
            Node<T> prevTemp = tail;
            int c = 0;
            while (c != index) {
                temp = temp.getNext();
                prevTemp = prevTemp.getNext();
                c++;
            }
            prevTemp.setNext(newNode);
            newNode.setNext(temp);
            size++;
        }
    }

    /**
     * Añande un elemento al inicio de la lista
     * @param element Elemento a añadir
     */
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        tail.setNext(newNode);
        newNode.setNext(head);
        head = newNode;
        size++;
    }

    /**
     * Añade un elemento al final de la lista
     * @param element Elemento a añadir
     */
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        tail.setNext(newNode);
        newNode.setNext(head);
        tail = newNode;
        size++;
    }

    /**
     * Devuelve el elemento actual
     *
     * @return Elemento actual de la lista
     */
    public T getCurrent() {
        if (size == 0) {
            return null;
        } else {
            return current.getValue();
        }
    }

    /**
     * Devuelve el elemento del índice dado
     * @param index Índice del elemento
     */
    public T get(int index) {
        if (size == 0) {
            return null;
        }
        else if (index < 0 || index > size-1) {
            throw new IndexOutOfBoundsException();
        }
        else {
            int c = 0;
            Node<T> temp = head;
            while (c != index) {
                temp = temp.getNext();
                c++;
            }
            return temp.getValue();
        }
    }

    /**
     * Devuelve el índice del elemento actual
     * @return Índice del elemento actual
     */
    public int getCurrentIndex() {
        int c = 0;
        Node<T> temp = head;
        while (temp != current) {
            temp = temp.getNext();
            c++;
            if (temp == current) {
                break;
            }
        }
        return c;
    }

    /**
     * Se adelanta el elemento actual al siguiente de la lista
     */
    public void next() {
        current = current.getNext();
    }

    /**
     * Se devuelve el elemento actual al anterior de la lista
     */
    public void prev() {
        if (current == head) {
            current = tail;
        } else {
            Node<T> temp = head;
            while (temp.getNext() != current) {
                temp = temp.getNext();
            }
            current = temp;
        }
    }

    /**
     * Elimina un elemento dado
     *
     * @param element Elemento a eliminar de la lista
     * @return true si el elemento se eliminó, false caso contrario
     */
    public boolean remove(T element) {
        if (size == 0) {
            return false;
        } else {
            Node<T> temp = this.head;
            Node<T> prevTemp = tail;
            for (int i = 0; i < size; i++) {
                if (temp.getValue().equals(element)) {
                    if (temp == head) {
                        head = head.getNext();
                    } else if (temp == tail) {
                        tail = prevTemp;
                        prevTemp.setNext(head);
                    } else {
                        prevTemp.setNext(temp.getNext());
                    }
                    size--;
                    return true;
                }
                prevTemp = temp;
                temp = temp.getNext();
            }
            return false;
        }
    }

    /**
     * Elimina el elemento de la posición dada
     * @param index Índice de donde eliminar el elemento
     */
    public void remove(int index) {
        T element = get(index);
        remove(element);
    }

    /**
     * Devuelve la cantidad de elementos de la lista
     *
     * @return Cantidad de elementos de la lista
     */
    public int getSize() {
        return size;
    }

    /**
     * Devuelve una representación textual de la lista y sus elementos
     *
     * @return Representación textual de la lista
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        Node<T> temp = head;
        for (int i = 0; i < size; i++) {
            buffer.append(temp.getValue());
            if (temp != tail) {
                buffer.append(", ");
            } else {
                buffer.append("]");
            }
            temp = temp.getNext();
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        CircularList<Integer> list = new CircularList<>();
        list.add(10);
        list.add(15);
        list.add(18);
        list.add(22);
        list.add(30);
        System.out.println(list);

        list.remove(30);

        System.out.println(list);

    }

}
