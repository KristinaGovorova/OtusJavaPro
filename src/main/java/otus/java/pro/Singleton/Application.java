package otus.java.pro.Singleton;


import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        ItemsSertviceProxy itemsSertviceProxy = new ItemsSertviceProxy();

        itemsSertviceProxy.createItems();
        itemsSertviceProxy.doublePrices();
    }
}
