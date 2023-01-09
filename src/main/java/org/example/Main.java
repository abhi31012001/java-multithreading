package org.example;


public class Main {
    public volatile boolean flag = false;
    public static void main(String[] args) throws Exception{
        Hello hello = new Hello();
        Thread t1 = new Thread(() -> {
            for (int i =0; i < 5; i++){
                System.out.println("RunThread "+ Thread.currentThread().getPriority());
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i =0; i < 5; i++){
                System.out.println("H1 "+ Thread.currentThread().getPriority());
            }
        });
        System.out.println(t1.getName());
        t1.setName("Run");
        System.out.println(t1.getName());
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);

        t2.start();
        t1.start();
        hello.start();
        t1.join();
        t2.join();
        hello.join();
        System.out.println("Done");

        Counter counter = new Counter();

        Thread c1 = new Thread(() -> {
           for(int i = 0; i < 1000; i++){
               counter.increase();
           }
        });
        Thread c2 = new Thread(() -> {
            for(int i = 0; i < 1000; i++){
                counter.increase();
            }
        });
        c1.start();
        c2.start();
        c1.join();
        c2.join();


        Q q = new Q();
        new Producer(q);
        new Consumer(q);
        System.out.println(counter.count);

    }
}

class RunThread implements Runnable{
    @Override
    public void run() {
        for (int i =0; i < 5; i++){
            System.out.println("RunThread");
        }
    }
}


class Hello extends Thread{
    public void run() {
        for (int i =0; i < 5; i++){
            System.out.println("Hello");
        }
    }
}

class Counter {
    int count;

    public synchronized void increase() {
        count++;
    }
}

class  Q {
    int num;
    boolean k = false;
    public synchronized void put(int num) throws InterruptedException {
        while (k){
            wait();
        }
        System.out.println("PUT "+num);
        this.num = num;
        k = true;
        notify();
    }
    public synchronized void get() throws InterruptedException {
        while (!k)
            wait();
        System.out.println("GET "+num);
        k = false;
        notify();
    }
}

class Producer implements Runnable{
    Q q;
    public Producer(Q q){
        this.q = q;
        Thread t1 = new Thread(this);
        t1.start();
    }
    @Override
    public void run() {
        int i = 0;
        while (i <= 10){

            try {
                q.put(i++);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Consumer implements Runnable{
    Q q;

    public Consumer(Q q) {
        this.q = q;
        Thread t1 = new Thread(this);
        t1.start();
    }

    @Override
    public void run() {
        int i = 0;
        while (i <= 10){
           i++ ;
            try {
                q.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}







