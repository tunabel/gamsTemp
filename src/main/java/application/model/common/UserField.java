package application.model.common;

public enum UserField {
    ID("id"),
    FIRSTNAME("firstName"),
    SURNAME("surName"),
    EMAIL("email"),
    BIRTHDAY("birthDay"),
    BIRTHYEAR("birthYear"),
    BIRTHPLACE("birthPlace"),
    DEPARTMENT("department"),
    ROLE("role"),
    ACTIVE("active");

    private final String name;

    UserField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
