package ir.coleo.varam.activities.reports;

enum State {

    info, injury, drugs, moreInfo;

    public static int getNumber(State state) {
        switch (state) {
            case info:
                return 0;
            case injury:
                return 1;
            case drugs:
                return 2;
            case moreInfo:
                return 3;
        }
        return 0;
    }

}
