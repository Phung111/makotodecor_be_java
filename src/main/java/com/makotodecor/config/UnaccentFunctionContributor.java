package com.makotodecor.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

public class UnaccentFunctionContributor implements FunctionContributor {

  @Override
  public void contributeFunctions(FunctionContributions functionContributions) {
    functionContributions.getFunctionRegistry().registerPattern(
        "unaccent",
        "unaccent(?1)",
        functionContributions.getTypeConfiguration()
            .getBasicTypeRegistry()
            .resolve(StandardBasicTypes.STRING));
  }
}
