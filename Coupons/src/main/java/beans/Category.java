package beans;

public enum Category {
    Food,
    Electricity,
    Restaurant,
    Vacation;

    public static int getValue(Category category) {
        switch (category) {
            case Food:
                return 1;
            case Electricity:
                return 2;
            case Restaurant:
                return 3;
            case Vacation:
                return 4;
        }
        return 0;
    }

    public static Category getValue(int categoryId) {
        switch (categoryId) {
            case 1:
                return Food;
            case 2:
                return Electricity;
            case 3:
                return Restaurant;
            case 4:
                return Vacation;
        }
        return null;
    }
}
