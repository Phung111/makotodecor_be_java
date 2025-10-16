package com.makotodecor.model.enums;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public abstract class PsqlEnumT<T, E extends Enum<E> & PersistableEnum<T>>
    implements UserType<E> {

  protected abstract Class<E> getEnumClass();

  protected abstract Class<T> getPersistentType();

  @Override
  public int getSqlType() {
    return Types.OTHER;
  }

  @Override
  public boolean equals(E x, E y) {
    return x == y; // since enum is singleton
  }

  @Override
  public int hashCode(E x) {
    return x.hashCode();
  }

  protected T getCellContent(ResultSet rs, int position) throws SQLException {
    if (String.class.isAssignableFrom(getPersistentType())) {
      return getPersistentType().cast(rs.getString(position));
    }
    if (Integer.class.isAssignableFrom(getPersistentType())) {
      return getPersistentType().cast(rs.getInt(position));
    }
    throw new RuntimeException("Unsupported type: " + getPersistentType().getSimpleName());
  }

  @Override
  public E nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
      throws SQLException {
    // implement
    final T cellContent = getCellContent(rs, position);
    if (cellContent == null) {
      return null;
    }
    return PersistableEnum.fromValue(getEnumClass(), cellContent)
        .orElseThrow(
            () -> new IllegalArgumentException("Unknown " + getEnumClass().getSimpleName() + " value: " + cellContent));
  }

  @Override
  public void nullSafeSet(PreparedStatement st, E value, int index,
      SharedSessionContractImplementor session) throws SQLException {
    if (value == null) {
      st.setNull(index, Types.OTHER);
    } else {
      st.setObject(index, value.getValue(), Types.OTHER);
    }
  }

  @Override
  public E deepCopy(E value) {
    return value; // since enum is singleton
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(E value) {
    return value; // don't know, but it works. If value.getPersistentValue(),
                  // findByScmProviderType() fails
  }

  @Override
  public E assemble(Serializable cached, Object owner) {
    // assemble from cache
    if (cached == null) {
      return null;
    }
    return PersistableEnum.fromValue(getEnumClass(), (T) cached)
        .orElseThrow(
            () -> new IllegalArgumentException("Unknown " + getEnumClass().getSimpleName() + " value: " + cached));
  }
}
