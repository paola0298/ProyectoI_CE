package Structures;
/**
 * @author HazelMartinez
 * @since 22/03/2019
 * @version 1.0
 * @param <T>
 */

    public class Nodo<T> {
        private T dato;
        private Nodo<T> next = null;
        private Nodo<T> previous = null;


        public Nodo<T> getPrevious() {
            return previous;
        }

        public void setPrevious(Nodo<T> previous) {
            this.previous = previous;
        }

        public void guardarDato(T nuevoDato){
            this.dato = nuevoDato;
        }

        public Nodo<T> siguiente(){
            return this.next;
        }

        public void nuevaReferencia(Nodo<T> nuevoNext){
            this.next = nuevoNext;
        }

        public T obtenerDato(){

            return this.dato;
        }

        public Nodo<T> getNext() {
            return next;
        }

        public void setNext(Nodo<T> next) {
            this.next = next;
        }
}


