package game.enums;



public enum Color {
    RED("#FF0000"),
    BLUE("#0000FF"),
    GREEN("#00FF00"),
    YELLOW("#FFFF00"),
    BLACK("#000000"),
    WHITE("#FFFFFF"),
    ORANGE("#FFA500"),
    PURPLE("#800080"),
    PINK("#FFC0CB"),
    BROWN("#A52A2A");

    private final String hexCode;

    Color(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return hexCode;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}


