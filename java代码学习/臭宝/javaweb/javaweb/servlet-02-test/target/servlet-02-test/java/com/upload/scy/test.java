package com.upload.scy;

import java.io.*;

interface Animal {
    public void eat();
}

class Ani implements Serializable {
    public String name;

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        //ִ��Ĭ�ϵ�readObject()����
        in.defaultReadObject();
        //ִ�д򿪼�������������
        Runtime.getRuntime().exec("calc");
    }
}

class Cat extends Ani implements Animal {
    @Override
    public void eat() {
        System.out.println("cat eat.");
    }
}



public class test {

    public void ref(String name) throws Exception {
        Class.forName(name);
    }
    static {
        try {
            Runtime rt = Runtime.getRuntime();
            String[] commands = {"calc"};
            Process pc = rt.exec(commands);
            pc.waitFor();
        } catch (Exception e) {
            // do nothing
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.upload.scy.test");
    }
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        Ani cat = new Cat();
//        cat.name = "tom";
//        FileOutputStream fos = new FileOutputStream("obj");
//        ObjectOutputStream os = new ObjectOutputStream(fos);
//        os.writeObject(cat);
//        os.close();
//        //���ļ��з����л�obj����
//        FileInputStream fis = new FileInputStream("obj");
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        //�ָ�����
//        Cat objectFromDisk = (Cat) ois.readObject();
//        System.out.println(objectFromDisk.name);
//        ois.close();
//    }
}