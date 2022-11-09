package run.mycode.zombieland;

public enum Dir {
    RIGHT,
    DOWN,
    LEFT,
    UP;

    public String toString() {
        return super.toString().toLowerCase();
    }

    public int dx() {
        switch (this) {
            case RIGHT:
                return 1;
            case LEFT:
                return -1;
            default:
                return 0;
        }
    }

    public int dy() {
        switch (this) {
            case DOWN:
                return 1;
            case UP:
                return -1;
            default:
                return 0;
        }
    }

    public Dir toRight() {
        switch (this) {
            case RIGHT:
                return DOWN;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            default:
                return RIGHT;
        }
    }


    public Dir toLeft() {
        switch (this) {
            case RIGHT:
                return UP;
            case DOWN:
                return RIGHT;
            case LEFT:
                return DOWN;
            default:
                return LEFT;
        }
    }
}
