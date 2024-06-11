package dato;

import java.util.ArrayList;
import java.util.List;

class CountConcurrente implements Runnable {
	// private int[] arr;
	private int n;
	private int exp;
	public static int[] arr;

	// Constructor de la clase
	public CountConcurrente(int[] arr, int n, int exp) {
		this.arr = arr;
		this.n = n;
		this.exp = exp;
	}

	@Override
	public void run() {
		countSort(arr, n, exp);
	}

	public static int getMax(int arr[], int n) { // Función con la que vamos a conseguir el máximo valor
		int max = arr[0]; // Tomamos el primer elemento por default como si fuera el máximo
		for (int i = 1; i < n; i++) { // Recorremos el array
			if (arr[i] > max) {// Vamos comparando el elemento actual sobre el que estamos parados en el array
				max = arr[i];
			}
		} // para ver si es mayor que el max actual.
		return max; // Devovemos el máximo valor
	}

	public static void countSort(int arr[], int n, int exp) {
		int output[] = new int[n];// Array donde vamos a almacenar los elementos ordenados
		//int count[] = new int[10];

		int mx = getMax(arr, n);

		int count[] = new int[mx + 1]; // Array para guardar la cant de veces que aparece cada dígito del 0 al 9
		
		for (int i = 0; i < n; i++) {
			// Contamos la cantidad de veces que se presentan los num en el array principal
			count[(arr[i] / exp) % 10]++;
		}

		// Cambia a count[i] para que ahora este contenga la posición real de este dígito dentro de output[]
		for (int i = 1; i < 10; i++) {
			count[i] += count[i - 1];
		}
		
		// Rellenamos el array donde vamos a tener los elementos ordenados
		for (int i = n - 1; i >= 0; i--) {
			output[count[(arr[i] / exp) % 10] - 1] = arr[i];
			count[(arr[i] / exp) % 10]--;
		}

		// Rellenamos el array principal con los números ordenados
		for (int i = 0; i < n; i++) {
			arr[i] = output[i];
		}
	}

	public static void parallelCountSort(int arr[], int n) {
		int m = getMax(arr, n);
		List<Thread> threads = new ArrayList<>();

		// Ordena el conteo para cada dígito de forma concurrente
		for (int exp = 1; m / exp > 0; exp = 10 * exp) {
			CountConcurrente task = new CountConcurrente(arr, n, exp);
			double tiempoInicial, tiempoFinal;
			tiempoInicial = System.nanoTime();

			Thread thread = new Thread(task);

			tiempoFinal = System.nanoTime() - tiempoInicial;
			System.out.print("Termine, mi tiempo fue " + tiempoFinal + "\n");

			threads.add(thread);
			thread.start();

			// Esperamos a que finalicen todos los hilos
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// int arr[] = { 170, 45, 75, 90, 802, 24, 2, 66 };
		
		//Declaro el tamaño de mi array
		int tamArray = 10000;
		// Rellenamos el arreglo con valores random
		int arr[] = Funciones.generarArrayAleatorio(tamArray, 0, 1000);

		// Defino n con el tamaño del array

		int n = arr.length;

		// Muestro por consola el array previo a ordenarse
		System.out.print("Array original: \n");
		for (int i = 0; i < n; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();

		// Llamo al ordenamiento
		parallelCountSort(arr, n);

		// Devuelvo el array ordenado
		System.out.print("Array luego de Count Sort: \n");
		for (int i = 0; i < n; i++) {
			System.out.print(arr[i] + " ");
		}
	}
}
