package com.bigmacdev.all_med.test;

import java.io.*;
import java.util.Base64;

import com.bigmacdev.all_med.model.Person;

public class SerializationTest {


    public static void main(String[] args) {
        Person p = new Person("bob","test",1993,05,4);
        System.out.println(p.getName());
        System.out.println("Serializing");
        try {
            String x = cerial(p);
            System.out.println(x);
            Person e = (Person) decerial(x);
            System.out.println(e.getName());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static String cerial(Serializable o)throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static Object decerial(String s) throws IOException, ClassNotFoundException{
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }
}
