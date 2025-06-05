package dev.asper.poker.engine;


import dev.asper.advice.Decision;
import dev.asper.poker.enums.Action;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public enum Branch {
    NONE(0., Action.F),
    F(0., Action.F),
    C(0., Action.C),
    A(0., Action.A),
    R_10(0.10, Action.R) {
    },
    R_20(0.20, Action.R) {
    },
    R_25(0.25, Action.R) {
    },
    R_33(0.33, Action.R) {
    },
    R_50(0.50, Action.R) {
    },
    R_66(0.66, Action.R) {
    },
    R_75(0.75, Action.R) {
    },
    R_100(1.00, Action.R) {
    },
    R_125(1.25, Action.R) {
    },
    R_133(1.33, Action.R) {
    },
    R_150(1.50, Action.R) {
    },
    R_166(1.66, Action.R) {
    },
    R_175(1.75, Action.R) {
    },
    R_200(2.00, Action.R) {
    },
    R_225(2.25, Action.R) {
    },
    R_250(2.50, Action.R) {
    },
    R_275(2.75, Action.R) {
    },
    R_300(3.00, Action.R) {
    },
    R_350(3.50, Action.R) {
    },
    R_500(5.00, Action.R) {
    };

    private static final Branch[] rBranchesArr = Arrays.stream(Branch.values())
            .filter(decisionBranch -> decisionBranch.action == Action.R)
            .sorted(Comparator.comparingDouble(value -> value.percentage))
            .toArray(Branch[]::new);

    private final double percentage;
    private final Action action;
    public final static EnumSet<Branch> postflopRoughBranches = EnumSet.of(F,
            C,
            R_20,
            R_33,
            R_66,
            R_125,
            R_500);

    public final static EnumSet<Branch> rPreflopRoughBranches = EnumSet.of(R_75,
            R_100,
            R_166,
            R_250,
            R_500);

    public final static EnumSet<Branch> rBranches = Arrays.stream(rBranchesArr)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(Branch.class)));

    public final static EnumSet<Branch> branchesWithoutAmount = EnumSet.of(F, C, A);

    Branch(double percentage, Action action) {
        this.percentage = percentage;
        this.action = action;
    }

    public static Branch fromDecision(Decision decision, double potSize) {
        Action action = decision.action();
        return switch (action) {
            case F -> F;
            case C -> C;
            case A -> A;
            case R -> fromPotSize(decision.amount() / potSize);
        };
    }

    public Decision toDecision(int potSize, int callAmount, int stackStub) {
        return switch (this.action) {
            case F -> new Decision(Action.F, 0);
            case C -> new Decision(Action.C, callAmount);
            case R -> new Decision(Action.R, (int) (Math.round(this.percentage * potSize)));
            case A -> new Decision(Action.A, stackStub);
        };
    }

    private static Branch fromPotSize(double amountByPotSize) {
        return Arrays.stream(rBranchesArr)
                .filter(value -> value.percentage >= amountByPotSize)
                .findFirst().orElse(R_500);
    }

    public double getPercentage() {
        return percentage;
    }

    public Action getAction() {
        return action;
    }

    public Branch findClosest(List<Branch> branches) {
        if (branches.isEmpty()){
            return NONE;
        }
        if (this == NONE) {
            return this;
        } else if (branchesWithoutAmount.contains(this)) {
            return this;
        } else {
            return branches.stream()
                    .filter(value -> value.getPercentage() >= percentage)
                    .findFirst().orElseGet(() -> branches.get(branches.size() - 1));
        }
    }
}
