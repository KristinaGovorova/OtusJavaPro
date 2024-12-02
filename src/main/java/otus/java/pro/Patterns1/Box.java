package otus.java.pro.Patterns1;

import java.util.Iterator;
import java.util.List;

public class Box {
    private final Matryoshka red;        // "red0", "red1", ..., "red9"
    private final Matryoshka green;
    private final Matryoshka blue;
    private final Matryoshka magenta;

    public Box() {
        red = new Matryoshka("red");
        green = new Matryoshka("green");
        blue = new Matryoshka("blue");
        magenta = new Matryoshka("magenta");
    }

    // expected: "red0", "green0", "blue0", "magenta0", "red1", "green1", "blue1", "magenta1",...
    public Iterator<String> getSmallFirstIterator() {
        return new Iterator<String>() {
            private int sizeIndex = 0;
            private int colorIndex = 0;
            private final List<Matryoshka> matryoshkas = List.of(red, green, blue, magenta);

            @Override
            public boolean hasNext() {
                return colorIndex < matryoshkas.size() && sizeIndex < 10;
            }

            @Override
            public String next() {
                String item = matryoshkas.get(colorIndex).getItems().get(sizeIndex);
                colorIndex++;
                if (colorIndex == 4) { // 4 цвета
                    colorIndex = 0;
                    sizeIndex++;
                }
                return item;
            }
        };
    }

    // expected: "red0", "red1", ..., "red9", "green0", "green1", ..., "green9", ...
    public Iterator<String> getColorFirstIterator() {
        return new Iterator<String>() {
            private int sizeIndex = 0;
            private int colorIndex = 0;
            private final List<Matryoshka> matryoshkas = List.of(red, green, blue, magenta);

            @Override
            public boolean hasNext() {
                return colorIndex < matryoshkas.size() && sizeIndex < 10;
            }

            @Override
            public String next() {
                String item = matryoshkas.get(colorIndex).getItems().get(sizeIndex);
                sizeIndex++;
                if (sizeIndex == 10) {
                    sizeIndex = 0;
                    colorIndex++;
                }
                return item;
            }
        };
    }
}
