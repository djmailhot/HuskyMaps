/**
 * This is part of Problem Set 0.
 */
package ps0;

/**
 * HelloWorld is an implementation of the token
 * introductory "Hello World" program.
 *
 * HelloWorld is also the superclass for other classes in this package.
 */
public class HelloWorld {

    /** the greeting to display when this getGreeting() is invoked */
    static final String greeting = "Hello World!";

    /**
     * @effects prints the string "Hello World!" to the console
     */
    public static void main(String[] args) {
        HelloWorld myFirstHW = new HelloWorld();
        System.out.println(myFirstHW.getGreeting());
    }

    /**
     @return Returns a greeting (in English).
     */
    public String getGreeting() {
        return greeting;
    }

}
