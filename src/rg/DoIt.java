package rg;




public class DoIt {

    public static void main(String[] args) {
        System.out.println("Executing program...");
        RunnableImplementation r = new RunnableImplementation();
        Thread thread1 = new Thread(r, "Thread 1");
        thread1.start();
    }
}
