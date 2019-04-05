package Structures;
    public class Lista<T> {
        private int size;
        private Nodo<T> head;

        public void insertar(T valor){
            Nodo<T> nuevoNodo = new Nodo<T>();
            nuevoNodo.guardarDato(valor);
            if(this.head == null){
                this.head = nuevoNodo;
                head.nuevaReferencia(null);
            }else{
                Nodo<T> temp;
                temp = this.head;
                while(temp.siguiente() != null){
                    temp = temp.siguiente();
                    //System.out.println(this.head.obtenerDato());
                }
                nuevoNodo.setPrevious(temp);
                temp.nuevaReferencia(nuevoNodo);
                this.size++;
            }
        }

        public void eliminar() {
            Nodo<T> temp = new Nodo<T>();
            temp = this.head;
            this.head = head.siguiente();
            temp = null;
            size--;
        }
        public void print(){
            int i;
            i= 0;
            Nodo<T> temp;
            temp = this.head;
            while(temp.siguiente() != null) {
                //System.out.println("Valor actual: " + temp.obtenerDato());
                //System.out.println("Valor siguiente: " + temp.siguiente().obtenerDato());
                if(temp.getPrevious() != null) {
                    //System.out.println("Valor previo: " + temp.getPrevious().obtenerDato());
                }
                temp = temp.siguiente();
                i++;
            }
            //System.out.println("Valor penultimo: " + temp.getPrevious().obtenerDato());
            //System.out.println("Valor final: " + temp.obtenerDato());

        }
        public T retornarValor(int posicion) {
            int indice;
            indice = 0;
            Nodo<T> temp;
            temp = this.head;
            while (indice != posicion) {
                temp = temp.siguiente();
                indice++;
            }
            //System.out.println(temp.obtenerDato());
            return temp.obtenerDato();
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

