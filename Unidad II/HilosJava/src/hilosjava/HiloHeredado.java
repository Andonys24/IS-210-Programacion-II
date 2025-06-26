package hilosjava;

public class HiloHeredado extends Thread {

    @Override
    public void run() {
        System.out.println("Hilo Corriendo: " + Thread.currentThread().getName());
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " en Ejecucion...");
            }

            Thread.sleep(500);

        } catch (InterruptedException e) {
            System.out.println("Error al ejecutar el Hilo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }
}
