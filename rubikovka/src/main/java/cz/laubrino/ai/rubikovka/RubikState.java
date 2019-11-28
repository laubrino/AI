package cz.laubrino.ai.rubikovka;

import cz.laubrino.ai.framework.State;

import java.util.Arrays;

/**
 * @author tomas.laubr on 22.11.2019.
 */
public class RubikState extends State {
    private final byte[] kostka;

    public RubikState(byte[] kostka) {
        this.kostka = Arrays.copyOf(kostka, kostka.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RubikState rubikState = (RubikState) o;
        return Arrays.equals(kostka, rubikState.kostka);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(kostka);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int surface=0; surface <24; surface++) {
            int arrayIndex = surface >> 1;
            int colorIndex = kostka[arrayIndex];

            if ((surface & 1) == 1) {   //
                colorIndex = colorIndex & 0x0f;
            } else {
                colorIndex = (colorIndex & 0xf0) >> 4;
            }

            sb.append(RubikEnvironment.Color.getByIndex(colorIndex));
        }

        return sb.toString();
    }
}
