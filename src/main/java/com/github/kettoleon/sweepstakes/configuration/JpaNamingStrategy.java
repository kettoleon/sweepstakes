package com.github.kettoleon.sweepstakes.configuration;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class JpaNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalTableName(new Identifier("Sweepstakes" + name.getText(), false), jdbcEnvironment);
    }
}
