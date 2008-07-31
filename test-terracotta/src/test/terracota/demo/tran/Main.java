package test.terracota.demo.tran;

/**
  All content copyright (c) 2003-2008 Terracotta, Inc.,
  except as may otherwise be noted in a separate copyright notice.
  All rights reserved.
*/

public class Main
{
    public static Main instance = new Main();
    private Transient1 transient1 = new Transient1();
    private Transient2 transient2 = new Transient2();
    private Transient3 transient3 = new Transient3();

    public class Transient1 
    {
        private transient String foo = "Hello";
        private String bar = "World";

        public String toString()
        {
            return "-- Transient1 -- " + "\n" +
                   "transient foo is : " + foo + "\n" +
                   "          bar is : " + bar;
        }
    }

    public class Transient2
    {
        private String foo = "Hello";
        private String bar = "World";

        public String toString()
        {
            return "-- Transient2 -- " + "\n" +
                   "foo is : " + foo + "\n" +
                   "bar is : " + bar;
        }
    }

    public class Transient3
    {
        private String foo = "Hello";
        private String bar = "World";

        public String toString()
        {
            return "-- Transient3 -- " + "\n" +
                   "foo is : " + foo + "\n" +
                   "bar is : " + bar;
        }
    }

    public void run()
    {
        System.out.println(transient1);
        System.out.println(transient2);
        System.out.println(transient3);
    } 

    public static void main(String[] args) throws Exception
    {
       instance.run(); 
    }
}
