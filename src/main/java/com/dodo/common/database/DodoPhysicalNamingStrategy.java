package com.dodo.common.database;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoPhysicalNamingStrategy implements PhysicalNamingStrategy {
    private final static Pattern         pattern  = Pattern.compile("[A-Z]");
    private String                       tablePrefix;
    public static PhysicalNamingStrategy INSTANCE = new DodoPhysicalNamingStrategy();

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(Identifier.toIdentifier(tablePrefix + "_" + name), jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name == null) {
            return null;
        }
        String nameStr = name.getText().replace('.', '_');
        nameStr = addMarkFontUnderlines(nameStr).replaceAll("__", "_");
        return getIdentifier(nameStr, name.isQuoted(), jdbcEnvironment);
    }

    private static String addMarkFontUnderlines(String srcName) {
        if (srcName == null) {
            return null;
        }
        srcName = StringUtils.uncapitalize(srcName);
        Matcher matcher = pattern.matcher(srcName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group());
        }
        matcher.appendTail(sb);

        return sb.toString().toLowerCase();
    }

    /**
     * Get an identifier for the specified details. By default this method will
     * return an identifier with the name adapted based on the result of
     * {@link #isCaseInsensitive(JdbcEnvironment)}
     * 
     * @param name
     *            the name of the identifier
     * @param quoted
     *            if the identifier is quoted
     * @param jdbcEnvironment
     *            the JDBC environment
     * @return an identifier instance
     */
    protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
        if (isCaseInsensitive(jdbcEnvironment)) {
            name = name.toLowerCase(Locale.ROOT);
        }
        return new Identifier(name, quoted);
    }

    /**
     * Specify whether the database is case sensitive.
     * 
     * @param jdbcEnvironment
     *            the JDBC environment which can be used to determine case
     * @return true if the database is case insensitive sensitivity
     */
    protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
        return true;
    }

    public String getTablePrefix() {
        return this.tablePrefix;
    }

    public void setTablePrefix(String paramString) {
        this.tablePrefix = paramString;
    }
}
