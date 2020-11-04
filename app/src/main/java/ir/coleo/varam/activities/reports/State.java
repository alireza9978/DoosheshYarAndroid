package ir.coleo.varam.activities.reports;

enum State {

    info, injury, cartieState, drugs, moreInfo;

    public static int getNumber(State state) {
        switch (state) {
            case info:
                return 0;
            case injury:
                return 1;
            case cartieState:
                return 2;
            case drugs:
                return 3;
            case moreInfo:
                return 4;
        }
        return 0;
    }

}
