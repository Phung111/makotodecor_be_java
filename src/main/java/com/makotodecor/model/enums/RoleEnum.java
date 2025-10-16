package com.makotodecor.model.enums;

public enum RoleEnum implements PersistableEnum<String> {
  USER("USER"),
  STAFF("STAFF"),
  ADMIN("ADMIN");

  private final String value;

  RoleEnum(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

  public static final class Auth {
    private Auth() {
    }

    public static final String USER = "hasRole('USER')";
    public static final String STAFF = "hasRole('STAFF')";
    public static final String ADMIN = "hasRole('ADMIN')";

    public static final String ADMIN_OR_STAFF = "hasAnyRole('ADMIN', 'STAFF')";
    public static final String ADMIN_OR_USER = "hasAnyRole('ADMIN', 'USER')";
    public static final String STAFF_OR_USER = "hasAnyRole('STAFF', 'USER')";

    public static final String IS_AUTHENTICATED = "isAuthenticated()";
    public static final String IS_ANONYMOUS = "isAnonymous()";
    public static final String PERMIT_ALL = "permitAll()";
    public static final String DENY_ALL = "denyAll()";
  }
}
