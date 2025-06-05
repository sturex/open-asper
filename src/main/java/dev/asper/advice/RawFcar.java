package dev.asper.advice;

import java.io.Serializable;

public record RawFcar(int f, int c, int a, int r) implements Serializable {
    public static RawFcar from(int fCount, int cCount, int aCount, int rCount) {
        return new RawFcar(fCount, cCount, aCount, rCount);
    }

    public int total() {
        return f + c + a + r;
    }
}
