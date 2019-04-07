package Structures;

public class Node<T> {
    private T value;
    private Node<T> next;
    private Node<T> previous = null;


    public Node(T value) {
        this.value = value;
        this.next = null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public void saveValue(T newValue){
    this.value = newValue;
    }

    public void newReference(Node<T> newNext){
        this.next = newNext;
    }

    public Node<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }
}
