package dato;

import java.util.ArrayList;
import java.util.List;

class CountConcurrente {

	public static int getMax(int arr[], int n) { // Función con la que vamos a conseguir el máximo valor
		int max = arr[0]; // Tomamos el primer elemento por default como si fuera el máximo
		for (int i = 1; i < n; i++) { // Recorremos el array
			if (arr[i] > max) {// Vamos comparando el elemento actual sobre el que estamos parados en el array
				max = arr[i];
			}
		} // para ver si es mayor que el max actual.
		return max; // Devovemos el máximo valor
	}

	// Count sort
	public static void countSort(int arr[], int n, int exp) {
		int output[] = new int[n]; // Array donde vamos a almacenar los elementos ordenados
		int i; // Variable que vamos a utilizar luego en los bucles FOR

		int mx = getMax(arr, n);

		int count[] = new int[mx + 1]; // Array para guardar la cant de veces que aparece cada dígito del 0 al 9

		for (i = 0; i < n; i++) {
			// Contamos la cantidad de veces que se presentan los num en el array principal
			count[(arr[i] / exp) % 10]++;
		}

		// Cambia a count[i] para que ahora este contenga la posición real de este
		// dígito dentro de output[]
		for (i = 1; i < (mx + 1); i++) {
			count[i] = count[i - 1] + count[i];
		}

		// Rellenamos el array donde vamos a tener los elementos ordenados
		for (i = n - 1; i >= 0; i--) {
			output[count[(arr[i] / exp) % 10] - 1] = arr[i];
			count[(arr[i] / exp) % 10]--;
		}

		// Rellenamos el array principal con los números ordenados
		for (i = 0; i < n; i++) {
			arr[i] = output[i];
		}
	}

	// Count sort
	public static void parallelCountSort(int arr[], int n) {
		int m = getMax(arr, n);
		List<Thread> threads = new ArrayList<>();

		// Ordena el conteo para cada dígito de forma concurrente
		for (int exp = 1; m / exp > 0; exp = exp * 10) {

			final int currentExp = exp;

			// Creamos un hilo para que trabaje con countSort
			Thread thread = new Thread(() -> countSort(arr, n, currentExp));
			threads.add(thread);
			thread.start();
		}

		// Esperamos a que finalicen todos los hilos
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// Test
	public static void main(String[] args) {
		// int arr[] = { 5, 1, 4, 5, 9, 3, 2, 1 };

		// Declaramos el arreglo
		int arr[] = { 100, 7, 8, 66, 44, 23, 8, 100, 89, 4, 23 };

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
