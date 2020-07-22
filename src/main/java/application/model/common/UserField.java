package application.model.common;

public enum UserField {
    ID("id"),
    FIRSTNAME("firstName"),
    SURNAME("surName"),
//    USERNAME("username"),
    EMAIL("email"),
    BIRTHDAY("birthDay"),
    BIRTHYEAR("birthYear"),
    BIRTHPLACE("birthPlace"),
    DEPARTMENT("department"),
    ROLES("roles"),
    ACTIVE("active");

    private final String name;

    UserField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
