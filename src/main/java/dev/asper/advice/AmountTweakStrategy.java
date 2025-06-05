package dev.asper.advice;


public enum AmountTweakStrategy {
    IDENTITY {
        @Override
        public double apply(double origin, double value) {
            return origin;
        }
    },
    REPLACE {
        @Override
        public double apply(double origin, double value) {
            return value;
        }
    },
    MULTIPLY {
        @Override
        public double apply(double origin, double value) {
            return origin * value;
        }
    },
    ADD {
        @Override
        public double apply(double origin, double value) {
            return origin + value;
        }
    };

    public abstract double apply(double origin, double value);
}
