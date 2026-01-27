package com.project.pja.databases.generalisation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.project.pja.databases.generalisation.annotation.AttributDb;
import com.project.pja.databases.generalisation.annotation.IdDb;
import com.project.pja.databases.generalisation.annotation.ShowTable;
import com.project.pja.databases.generalisation.annotation.TableDb;
import com.project.pja.databases.generalisation.utils.InfoObject;

public class DB {

    public static boolean isComplexObject(Class<?> type) throws Exception {
        if (type.isPrimitive())
            return false;

        if (type == String.class)
            return false;
        if (Number.class.isAssignableFrom(type))
            return false;
        if (type == Boolean.class || type == Character.class)
            return false;

        if (java.util.Date.class.isAssignableFrom(type))
            return false;
        if (java.time.temporal.Temporal.class.isAssignableFrom(type))
            return false;

        if (Collection.class.isAssignableFrom(type))
            return false;
        if (Map.class.isAssignableFrom(type))
            return false;

        Package pkg = type.getPackage();
        if (pkg != null && pkg.getName().startsWith("java."))
            return false;

        return true;
    }

    public static InfoObject getAllInformationsObjects(Object table) throws Exception {
        InfoObject infoObject = new InfoObject();
        Class<?> classTable = table.getClass();

        if (!classTable.isAnnotationPresent(TableDb.class))
            throw new Exception("Le classe '" + classTable.getSimpleName() + "' n'est pas relier a une table");

        TableDb annotation = classTable.getAnnotation(TableDb.class);
        infoObject.nameTable = annotation.name();
        int i = 0;

        List<String> nameAttributs = new ArrayList<>();
        List<String> nameAttributsInTable = new ArrayList<>();
        List<String> typeAttributs = new ArrayList<>();
        String typeIdAttribut = "";
        for (Field field : classTable.getDeclaredFields()) {
            if (field.isAnnotationPresent(AttributDb.class))
                i++;
        }

        if (i == 0)
            throw new Exception("Il n'y a aucun attribut dans votre classe '" + classTable.getSimpleName()
                    + "' qui se relie a un table '" + infoObject.nameTable + "'");
        i = 0;
        for (Field field : classTable.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(AttributDb.class)) {
                nameAttributs.add(field.getName());
                nameAttributsInTable.add(field.getAnnotation(AttributDb.class).name());
                typeAttributs.add(field.getType().getName());
            }
            if (field.isAnnotationPresent(IdDb.class)) {
                infoObject.nameIdAttributs = (field.getName());
                infoObject.nameIdAttributsInTable = field.getAnnotation(AttributDb.class).name();
                infoObject.objectIdAttribut = field.getType();
                infoObject.valueId = field.get(table);
                infoObject.typeIdAttributs = field.getType().getName();
            }
        }

        infoObject.nameAttributs = nameAttributs;
        infoObject.nameAttributsInTable = nameAttributsInTable;
        return infoObject;
    }

    public static Object completeField(Object data, InfoObject infoObject, ResultSet resultset, Connection connection)
            throws Exception {
        for (Field field : data.getClass().getDeclaredFields()) {
            int j = 0;
            for (String nameAttribut : infoObject.nameAttributs) {
                if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    String fieldTypeName = fieldType.getName();
                    String columnName = infoObject.nameAttributsInTable.get(j);

                    try {
                        // Gérer les types primitifs et leurs wrappers
                        if (fieldType == int.class || fieldType == Integer.class) {
                            int value = resultset.getInt(columnName);
                            if (resultset.wasNull()) {
                                field.set(data, fieldType == int.class ? 0 : null);
                            } else {
                                field.set(data, value);
                            }
                        } else if (fieldType == long.class || fieldType == Long.class) {
                            long value = resultset.getLong(columnName);
                            if (resultset.wasNull()) {
                                field.set(data, fieldType == long.class ? 0L : null);
                            } else {
                                field.set(data, value);
                            }
                        } else if (fieldType == double.class || fieldType == Double.class) {
                            double value = resultset.getDouble(columnName);
                            if (resultset.wasNull()) {
                                field.set(data, fieldType == double.class ? 0.0 : null);
                            } else {
                                field.set(data, value);
                            }
                        } else if (fieldType == float.class || fieldType == Float.class) {
                            float value = resultset.getFloat(columnName);
                            if (resultset.wasNull()) {
                                field.set(data, fieldType == float.class ? 0.0f : null);
                            } else {
                                field.set(data, value);
                            }
                        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                            boolean value = resultset.getBoolean(columnName);
                            if (resultset.wasNull()) {
                                field.set(data, fieldType == boolean.class ? false : null);
                            } else {
                                field.set(data, value);
                            }
                        } else if (fieldType == short.class || fieldType == Short.class) {
                            short value = resultset.getShort(columnName);
                            if (resultset.wasNull()) {
                                field.set(data, fieldType == short.class ? (short) 0 : null);
                            } else {
                                field.set(data, value);
                            }
                        } else if (fieldType == byte.class || fieldType == Byte.class) {
                            byte value = resultset.getByte(columnName);
                            if (resultset.wasNull()) {
                                field.set(data, fieldType == byte.class ? (byte) 0 : null);
                            } else {
                                field.set(data, value);
                            }
                        } else if (fieldType == char.class || fieldType == Character.class) {
                            String strValue = resultset.getString(columnName);
                            if (resultset.wasNull()) {
                                field.set(data, fieldType == char.class ? '\u0000' : null);
                            } else if (strValue != null && !strValue.isEmpty()) {
                                field.set(data, strValue.charAt(0));
                            } else {
                                field.set(data, fieldType == char.class ? '\u0000' : null);
                            }
                        }
                        // Gérer les types objets communs
                        else if (fieldType == String.class) {
                            String value = resultset.getString(columnName);
                            field.set(data, resultset.wasNull() ? null : value);
                        } else if (fieldType == java.math.BigDecimal.class) {
                            java.math.BigDecimal value = resultset.getBigDecimal(columnName);
                            field.set(data, resultset.wasNull() ? null : value);
                        }
                        // GESTION COMPLÈTE DES TYPES DATE/TIME/TIMESTAMP
                        else if (fieldType == java.util.Date.class) {
                            // Essaye d'abord avec getTimestamp (contient date + heure)
                            java.sql.Timestamp timestamp = resultset.getTimestamp(columnName);
                            if (!resultset.wasNull() && timestamp != null) {
                                field.set(data, new java.util.Date(timestamp.getTime()));
                            } else {
                                // Si timestamp est null, essaie avec getDate
                                java.sql.Date sqlDate = resultset.getDate(columnName);
                                if (!resultset.wasNull() && sqlDate != null) {
                                    field.set(data, new java.util.Date(sqlDate.getTime()));
                                } else {
                                    field.set(data, null);
                                }
                            }
                        } else if (fieldType == java.sql.Date.class) {
                            java.sql.Date value = resultset.getDate(columnName);
                            field.set(data, resultset.wasNull() ? null : value);
                        } else if (fieldType == java.sql.Time.class) {
                            java.sql.Time value = resultset.getTime(columnName);
                            field.set(data, resultset.wasNull() ? null : value);
                        } else if (fieldType == java.sql.Timestamp.class) {
                            java.sql.Timestamp value = resultset.getTimestamp(columnName);
                            field.set(data, resultset.wasNull() ? null : value);
                        }
                        // Java 8+ Date/Time API
                        else if (fieldType == java.time.LocalDate.class) {
                            java.sql.Date sqlDate = resultset.getDate(columnName);
                            if (!resultset.wasNull() && sqlDate != null) {
                                field.set(data, sqlDate.toLocalDate());
                            } else {
                                // Essayer de convertir depuis un timestamp si la date seule n'est pas
                                // disponible
                                java.sql.Timestamp timestamp = resultset.getTimestamp(columnName);
                                if (!resultset.wasNull() && timestamp != null) {
                                    field.set(data, timestamp.toLocalDateTime().toLocalDate());
                                } else {
                                    field.set(data, null);
                                }
                            }
                        } else if (fieldType == java.time.LocalTime.class) {
                            java.sql.Time sqlTime = resultset.getTime(columnName);
                            if (!resultset.wasNull() && sqlTime != null) {
                                field.set(data, sqlTime.toLocalTime());
                            } else {
                                // Essayer de convertir depuis un timestamp
                                java.sql.Timestamp timestamp = resultset.getTimestamp(columnName);
                                if (!resultset.wasNull() && timestamp != null) {
                                    field.set(data, timestamp.toLocalDateTime().toLocalTime());
                                } else {
                                    field.set(data, null);
                                }
                            }
                        } else if (fieldType == java.time.LocalDateTime.class) {
                            java.sql.Timestamp timestamp = resultset.getTimestamp(columnName);
                            if (!resultset.wasNull() && timestamp != null) {
                                field.set(data, timestamp.toLocalDateTime());
                            } else {
                                // Essayer de construire à partir de date et time séparés
                                java.sql.Date sqlDate = resultset.getDate(columnName);
                                java.sql.Time sqlTime = resultset.getTime(columnName);
                                if (sqlDate != null) {
                                    if (sqlTime != null) {
                                        field.set(data, java.time.LocalDateTime.of(
                                                sqlDate.toLocalDate(),
                                                sqlTime.toLocalTime()));
                                    } else {
                                        field.set(data, sqlDate.toLocalDate().atStartOfDay());
                                    }
                                } else {
                                    field.set(data, null);
                                }
                            }
                        } else if (fieldType == java.time.Instant.class) {
                            java.sql.Timestamp timestamp = resultset.getTimestamp(columnName);
                            if (!resultset.wasNull() && timestamp != null) {
                                field.set(data, timestamp.toInstant());
                            } else {
                                field.set(data, null);
                            }
                        } else if (fieldType == java.time.ZonedDateTime.class) {
                            java.sql.Timestamp timestamp = resultset.getTimestamp(columnName);
                            if (!resultset.wasNull() && timestamp != null) {
                                field.set(data, timestamp.toInstant().atZone(java.time.ZoneId.systemDefault()));
                            } else {
                                field.set(data, null);
                            }
                        } else if (fieldType == java.time.OffsetDateTime.class) {
                            java.sql.Timestamp timestamp = resultset.getTimestamp(columnName);
                            if (!resultset.wasNull() && timestamp != null) {
                                field.set(data, timestamp.toInstant().atZone(java.time.ZoneId.systemDefault())
                                        .toOffsetDateTime());
                            } else {
                                // Certains drivers supportent directement OffsetDateTime
                                try {
                                    Object value = resultset.getObject(columnName, java.time.OffsetDateTime.class);
                                    field.set(data, resultset.wasNull() ? null : value);
                                } catch (SQLException e) {
                                    field.set(data, null);
                                }
                            }
                        } else if (fieldType == java.time.Year.class) {
                            java.sql.Date sqlDate = resultset.getDate(columnName);
                            if (!resultset.wasNull() && sqlDate != null) {
                                field.set(data, java.time.Year.of(sqlDate.toLocalDate().getYear()));
                            } else {
                                // Essayer de lire comme un nombre
                                int year = resultset.getInt(columnName);
                                if (!resultset.wasNull()) {
                                    field.set(data, java.time.Year.of(year));
                                } else {
                                    field.set(data, null);
                                }
                            }
                        } else if (fieldType == java.time.Month.class) {
                            int monthValue = resultset.getInt(columnName);
                            if (!resultset.wasNull()) {
                                field.set(data, java.time.Month.of(monthValue));
                            } else {
                                field.set(data, null);
                            }
                        } else if (fieldType == java.time.Duration.class) {
                            // Peut être stocké comme un nombre (secondes) ou comme une chaîne
                            try {
                                // Essayer comme nombre (secondes)
                                long seconds = resultset.getLong(columnName);
                                if (!resultset.wasNull()) {
                                    field.set(data, java.time.Duration.ofSeconds(seconds));
                                } else {
                                    field.set(data, null);
                                }
                            } catch (SQLException e) {
                                // Essayer comme chaîne
                                String durationStr = resultset.getString(columnName);
                                if (!resultset.wasNull() && durationStr != null) {
                                    field.set(data, java.time.Duration.parse(durationStr));
                                } else {
                                    field.set(data, null);
                                }
                            }
                        } else if (fieldType == java.time.Period.class) {
                            String periodStr = resultset.getString(columnName);
                            if (!resultset.wasNull() && periodStr != null) {
                                field.set(data, java.time.Period.parse(periodStr));
                            } else {
                                field.set(data, null);
                            }
                        }
                        // Types binaires
                        else if (fieldType == byte[].class || fieldType == Byte[].class) {
                            byte[] value = resultset.getBytes(columnName);
                            field.set(data, resultset.wasNull() ? null : value);
                        } else if (fieldType == java.sql.Blob.class) {
                            java.sql.Blob blob = resultset.getBlob(columnName);
                            field.set(data, resultset.wasNull() ? null : blob);
                        } else if (fieldType == java.sql.Clob.class) {
                            java.sql.Clob clob = resultset.getClob(columnName);
                            field.set(data, resultset.wasNull() ? null : clob);
                        } else if (fieldType == java.net.URL.class) {
                            java.net.URL url = resultset.getURL(columnName);
                            field.set(data, resultset.wasNull() ? null : url);
                        }
                        // Gérer les objets complexes (relations)
                        else if (isComplexObject(fieldType)) {
                            if (fieldType.isArray()) {
                                // Gestion des tableaux
                                throw new Exception(
                                        "Les tableaux ne sont pas encore supportés pour le champ " + field.getName());
                            } else {
                                Class<?> classField = fieldType;
                                InfoObject infoObjectField = DB
                                        .getAllInformationsObjects(classField.getConstructor().newInstance());
                                Object valeurId = null;

                                if (infoObjectField.nameIdAttributs != null) {
                                    Class<?> idType = infoObjectField.objectIdAttribut;
                                    String idTypeName = idType.getName();

                                    if (idType == int.class || idType == Integer.class) {
                                        valeurId = resultset.getInt(columnName);
                                        if (resultset.wasNull()) {
                                            valeurId = null;
                                        }
                                    } else if (idType == long.class || idType == Long.class) {
                                        valeurId = resultset.getLong(columnName);
                                        if (resultset.wasNull()) {
                                            valeurId = null;
                                        }
                                    } else if (idType == String.class) {
                                        valeurId = resultset.getString(columnName);
                                        if (resultset.wasNull()) {
                                            valeurId = null;
                                        }
                                    } else if (idType == boolean.class || idType == Boolean.class) {
                                        valeurId = resultset.getBoolean(columnName);
                                        if (resultset.wasNull()) {
                                            valeurId = null;
                                        }
                                    } else if (idType == double.class || idType == Double.class) {
                                        valeurId = resultset.getDouble(columnName);
                                        if (resultset.wasNull()) {
                                            valeurId = null;
                                        }
                                    } else if (idType == short.class || idType == Short.class) {
                                        valeurId = resultset.getShort(columnName);
                                        if (resultset.wasNull()) {
                                            valeurId = null;
                                        }
                                    } else {
                                        throw new Exception(
                                                "Type d'ID non supporté pour l'objet complexe: " + idTypeName);
                                    }
                                } else {
                                    throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j)
                                            + "' n'est pas annoté IdDb dans sa classe");
                                }

                                if (valeurId != null) {
                                    Object relatedObject = getById(classField.getConstructor().newInstance(), valeurId,
                                            connection);
                                    field.set(data, relatedObject);
                                } else {
                                    field.set(data, null);
                                }
                            }
                        } else if (fieldType.isEnum()) {
                            // Gestion des énumérations
                            String enumValue = resultset.getString(columnName);
                            if (!resultset.wasNull() && enumValue != null) {
                                try {
                                    @SuppressWarnings("unchecked")
                                    Object enumConstant = Enum.valueOf((Class<Enum>) fieldType, enumValue);
                                    field.set(data, enumConstant);
                                } catch (IllegalArgumentException e) {
                                    // Essaye de chercher par valeur ou par nom insensible à la casse
                                    @SuppressWarnings("unchecked")
                                    Enum<?>[] enumConstants = ((Class<Enum>) fieldType).getEnumConstants();
                                    for (Enum<?> constant : enumConstants) {
                                        if (constant.name().equalsIgnoreCase(enumValue)) {
                                            field.set(data, constant);
                                            break;
                                        }
                                    }
                                }
                            } else {
                                field.set(data, null);
                            }
                        } else {
                            // Essayer avec getObject() pour les types non supportés explicitement
                            try {
                                Object value = resultset.getObject(columnName);
                                if (resultset.wasNull()) {
                                    field.set(data, null);
                                } else if (fieldType.isAssignableFrom(value.getClass())) {
                                    field.set(data, value);
                                } else {
                                    throw new Exception("Type non pris en charge pour le champ " + field.getName()
                                            + ": " + fieldTypeName);
                                }
                            } catch (SQLException e) {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName() + ": "
                                        + fieldTypeName, e);
                            }
                        }
                    } catch (SQLException e) {
                        throw new Exception("Erreur lors de la lecture de la colonne '" + columnName
                                + "' pour le champ '" + field.getName() + "'", e);
                    }
                }
                j++;
            }
        }
        return data;
    }

    public static Object getAll(Object table, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllWhere(Object table, String where, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where;

        System.out.println(query);
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllOrder(Object table, String order, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " ORDER BY " + order;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllOrderAndLimit(Object table, String order, String limit, Connection connection)
            throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " ORDER BY " + order + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllOrderAndLimit(Object table, String order, int limit, Connection connection)
            throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " ORDER BY " + order + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllLimit(Object table, int limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllLimit(Object table, String limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllOrderAndWhere(Object table, String where, String order, Connection connection)
            throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " + " ORDER BY " + order;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllOrderAndLimitAndWhere(Object table, String where, String order, String limit,
            Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " + " ORDER BY " + order
                + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllOrderAndLimitAndWhere(Object table, String where, String order, int limit,
            Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " + " ORDER BY " + order
                + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllLimitAndWhere(Object table, String where, int limit, Connection connection)
            throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getAllLimitAndWhere(Object table, String where, String limit, Connection connection)
            throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data = constructor.newInstance();
                data = DB.completeField(data, infoObject, resultset, connection);
                result.add(data);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static Object getById(Object table, Object id, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }

        String idValue;
        if (id instanceof String) {
            idValue = "'" + ((String) id).replace("'", "''") + "'";
        } else if (id instanceof Integer || id instanceof Long || id instanceof Double || id instanceof Float) {
            idValue = id.toString();
        } else if (id == null) {
            throw new Exception("L'ID du get by id ne peut pas être null");
        } else {
            idValue = "'" + id.toString().replace("'", "''") + "'";
        }

        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + infoObject.nameIdAttributsInTable + " = "
                + idValue;
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;

        Class<?> classTable = table.getClass();
        Object result = null;
        try {
            if (connection == null)
                throw new Exception("Le connexion qui prend les '" + nameTable + "' n'est pas ouvert");

            statement = connection.prepareStatement(query);
            statementOpen = true;

            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                result = constructor.newInstance();
                result = DB.completeField(result, infoObject, resultset, connection);
            }

            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        return result;
    }

    public static void save(Object table, Connection connection)
        throws Exception {
    InfoObject infoObject = DB.getAllInformationsObjects(table);
    boolean isInsert = true;
    if (infoObject.valueId != null) {
        isInsert = false;
    }
    String query = "";

    if (isInsert) {
        String attrs = "";
        String data = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (!attrTable.equals(infoObject.nameIdAttributs)) {
                if (i > 0) {
                    attrs = attrs + " , ";
                    data = data + " , ";
                }
                attrs = attrs + attrTable;
                data = data + " ? ";
                i++;
            }
        }
        query = "INSERT INTO " + infoObject.nameTable + "(" + attrs + ") VALUES (" + data + ")";
    } else {
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (!attrTable.equals(infoObject.nameIdAttributs)) {
                if (i > 0) {
                    attrs = attrs + " , ";
                }
                attrs = attrs + attrTable + " = ? ";
                i++;
            }
        }
        query = "UPDATE " + infoObject.nameTable + " SET " + attrs + " WHERE " + infoObject.nameIdAttributsInTable
                + " = ?";
    }

    System.out.println(query + " query ");
    PreparedStatement statement = null;
    ResultSet resultset = null;
    boolean statementOpen = false;
    boolean resultsetOpen = false;
    boolean closeable = false;

    try {
        if (connection == null)
            throw new Exception("Le connexion qui sauvegarde le '" + infoObject.nameTable + "' n'est pas ouvert");

        if (isInsert) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        } else {
            statement = connection.prepareStatement(query);
        }
        
        int j = 1;
        for (int i = 0; i < infoObject.nameAttributs.size(); i++) {
            if (!infoObject.nameAttributs.get(i).equals(infoObject.nameIdAttributs)) {
                for (Field field : table.getClass().getDeclaredFields()) {
                    if (field.getName().compareToIgnoreCase(infoObject.nameAttributs.get(i)) == 0) {
                        field.setAccessible(true);
                        Class<?> type = field.getType();
                        Object value = field.get(table);

                        if (type.getName().equals("int") || type.getName().equals("java.lang.Integer")) {
                            if (value != null) {
                                statement.setInt(j, (Integer) value);
                            } else {
                                statement.setNull(j, java.sql.Types.INTEGER);
                            }

                        } else if (type.getName().equals("long") || type.getName().equals("java.lang.Long")) {
                            if (value != null) {
                                statement.setLong(j, (Long) value);
                            } else {
                                statement.setNull(j, java.sql.Types.BIGINT);
                            }

                        } else if (type.getName().equals("double") || type.getName().equals("java.lang.Double")) {
                            if (value != null) {
                                statement.setDouble(j, (Double) value);
                            } else {
                                statement.setNull(j, java.sql.Types.DOUBLE);
                            }

                        } else if (type.getName().equals("float") || type.getName().equals("java.lang.Float")) {
                            if (value != null) {
                                statement.setFloat(j, (Float) value);
                            } else {
                                statement.setNull(j, java.sql.Types.FLOAT);
                            }

                        } else if (type.getName().equals("boolean") || type.getName().equals("java.lang.Boolean")) {
                            if (value != null) {
                                statement.setBoolean(j, (Boolean) value);
                            } else {
                                statement.setNull(j, java.sql.Types.BOOLEAN);
                            }

                        } else if (type.getName().equals("java.lang.String")) {
                            statement.setString(j, (String) value);

                        } else if (type.getName().equals("java.sql.Date")) {
                            statement.setDate(j, (java.sql.Date) value);

                        } else if (type.getName().equals("java.sql.Time")) {
                            statement.setTime(j, (java.sql.Time) value);

                        } else if (type.getName().equals("java.sql.Timestamp")) {
                            statement.setTimestamp(j, (java.sql.Timestamp) value);

                        } else if (type.getName().equals("java.util.Date")) {
                            if (value != null) {
                                statement.setTimestamp(
                                        j,
                                        new java.sql.Timestamp(((java.util.Date) value).getTime()));
                            } else {
                                statement.setNull(j, java.sql.Types.TIMESTAMP);
                            }

                        } else if (isComplexObject(type)) {

                            // Cas tableau / collection → ignoré ici
                            if (type.isArray() || java.util.Collection.class.isAssignableFrom(type)) {
                                statement.setNull(j, java.sql.Types.INTEGER);
                            } else {
                                // Objet complexe (ex: Voiture)
                                if (value == null) {
                                    statement.setNull(j, java.sql.Types.INTEGER);
                                } else {

                                    // Charger les infos ORM de la classe (Voiture)
                                    Class<?> classField = type;
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(value);

                                    if (infoObjectField.objectIdAttribut == null) {
                                        throw new Exception(
                                                "La classe " + classField.getSimpleName() +
                                                        " n'a pas d'attribut annoté @IdDb");
                                    }

                                    Object valeurId = infoObjectField.valueId;

                                    if (valeurId == null) {
                                        statement.setNull(j, java.sql.Types.INTEGER);
                                    } else {
                                        Class<?> idType = valeurId.getClass();

                                        if (idType == int.class || idType == Integer.class) {
                                            statement.setInt(j, (Integer) valeurId);

                                        } else if (idType == long.class || idType == Long.class) {
                                            statement.setLong(j, (Long) valeurId);

                                        } else if (idType == String.class) {
                                            statement.setString(j, (String) valeurId);

                                        } else {
                                            throw new Exception(
                                                    "Type d'ID non supporté : " + idType.getName());
                                        }
                                    }
                                }
                            }
                        } else {
                            throw new Exception("Type non pris en charge pour le champ " + field.getName());
                        }
                        j++;
                    }
                }
            }
        }

        if (!isInsert) {
            Object value = infoObject.valueId;
            Class<?> type = value.getClass();

            if (type.getName().equals("int") || type.getName().equals("java.lang.Integer")) {
                if (value != null) {
                    statement.setInt(j, (Integer) value);
                } else {
                    statement.setNull(j, java.sql.Types.INTEGER);
                }

            } else if (type.getName().equals("long") || type.getName().equals("java.lang.Long")) {
                if (value != null) {
                    statement.setLong(j, (Long) value);
                } else {
                    statement.setNull(j, java.sql.Types.BIGINT);
                }

            } else if (type.getName().equals("double") || type.getName().equals("java.lang.Double")) {
                if (value != null) {
                    statement.setDouble(j, (Double) value);
                } else {
                    statement.setNull(j, java.sql.Types.DOUBLE);
                }

            } else if (type.getName().equals("float") || type.getName().equals("java.lang.Float")) {
                if (value != null) {
                    statement.setFloat(j, (Float) value);
                } else {
                    statement.setNull(j, java.sql.Types.FLOAT);
                }

            } else if (type.getName().equals("boolean") || type.getName().equals("java.lang.Boolean")) {
                if (value != null) {
                    statement.setBoolean(j, (Boolean) value);
                } else {
                    statement.setNull(j, java.sql.Types.BOOLEAN);
                }

            } else if (type.getName().equals("java.lang.String")) {
                statement.setString(j, (String) value);

            } else if (type.getName().equals("java.sql.Date")) {
                statement.setDate(j, (java.sql.Date) value);

            } else if (type.getName().equals("java.sql.Time")) {
                statement.setTime(j, (java.sql.Time) value);

            } else if (type.getName().equals("java.sql.Timestamp")) {
                statement.setTimestamp(j, (java.sql.Timestamp) value);

            } else if (type.getName().equals("java.util.Date")) {
                if (value != null) {
                    statement.setTimestamp(
                            j,
                            new java.sql.Timestamp(((java.util.Date) value).getTime()));
                } else {
                    statement.setNull(j, java.sql.Types.TIMESTAMP);
                }
            }
        }

        statementOpen = true;
        int affectedRows = statement.executeUpdate();
        
        // CORRECTION ICI : Utiliser l'index numérique pour getGeneratedKeys()
        if (affectedRows > 0 && isInsert) {
            resultset = statement.getGeneratedKeys();
            if (resultset != null && resultset.next()) {
                for (Field field : table.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(IdDb.class)) {
                        field.setAccessible(true);
                        Class<?> type = field.getType();

                        try {
                            Object generatedValue = null;
                            
                            // IMPORTANT : Utiliser l'index 1, pas le nom de colonne
                            // Les clés générées n'ont pas de nom spécifique
                            if (type.equals(int.class) || type.equals(Integer.class)) {
                                generatedValue = resultset.getInt(1); // CHANGEMENT ICI
                            } else if (type.equals(long.class) || type.equals(Long.class)) {
                                generatedValue = resultset.getLong(1); // CHANGEMENT ICI
                            } else if (type.equals(String.class)) {
                                generatedValue = resultset.getString(1); // CHANGEMENT ICI
                            } else if (type.equals(double.class) || type.equals(Double.class)) {
                                generatedValue = resultset.getDouble(1); // CHANGEMENT ICI
                            } else if (type.equals(float.class) || type.equals(Float.class)) {
                                generatedValue = resultset.getFloat(1); // CHANGEMENT ICI
                            } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                                generatedValue = resultset.getBoolean(1); // CHANGEMENT ICI
                            } else if (type.equals(java.sql.Date.class)) {
                                generatedValue = resultset.getDate(1); // CHANGEMENT ICI
                            } else if (type.equals(java.sql.Time.class)) {
                                generatedValue = resultset.getTime(1); // CHANGEMENT ICI
                            } else if (type.equals(java.sql.Timestamp.class)) {
                                generatedValue = resultset.getTimestamp(1); // CHANGEMENT ICI
                            } else if (type.equals(java.util.Date.class)) {
                                java.sql.Timestamp timestamp = resultset.getTimestamp(1); // CHANGEMENT ICI
                                if (timestamp != null) {
                                    generatedValue = new java.util.Date(timestamp.getTime());
                                }
                            } else if (type.equals(short.class) || type.equals(Short.class)) { 
                                generatedValue = resultset.getShort(1); // CHANGEMENT ICI
                            } else if (type.equals(byte.class) || type.equals(Byte.class)) {
                                generatedValue = resultset.getByte(1); // CHANGEMENT ICI
                            } else if (type.equals(BigDecimal.class)) {
                                generatedValue = resultset.getBigDecimal(1); // CHANGEMENT ICI
                            } else {
                                // Pour les autres types, essayer avec getObject
                                generatedValue = resultset.getObject(1); // CHANGEMENT ICI
                            }

                            if (resultset.wasNull() && !type.isPrimitive()) {
                                generatedValue = null;
                            }

                            field.set(table, generatedValue);

                        } catch (IllegalAccessException e) {
                            throw new SQLException("Erreur lors de l'affectation de l'ID généré", e);
                        }

                        break;
                    }
                }
            }
        }
        
        // Déplacer statement.close() à la fin du try
        statement.close();
        statementOpen = false;

    } catch (Exception e) {
        System.out.println("Erreur dans save(): " + e.getMessage());
        e.printStackTrace();
        throw e;
    } finally {
        if (resultset != null) {
            try {
                resultset.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        if (statementOpen && statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (closeable && connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

    public static String getTableau(Object listObject, Object object, String titre, String sous_titre)
            throws Exception {

        String templateTable = """

                    <div class="row">
                  <div class="col-lg-12 grid-margin stretch-card">
                    <div class="card">
                      <div class="card-body">
                        <h4 class="card-title">%%title%%</h4>
                        <p class="card-description">%%subtitle%%</p>
                        <div class="table-responsive">
                          %%tableau%%
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                        """;

        return templateTable
                .replace("%%title%%", titre)
                .replace("%%subtitle%%", sous_titre)
                .replace("%%tableau%%", DB.getHTMLTable(listObject, object));
    }

    // Version avec filtre + tri
    public static List<Method> getSortedMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(methods -> {
                    ShowTable rang = methods.getAnnotation(ShowTable.class);
                    return rang != null && rang.numero() != 0;
                })
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(ShowTable.class).numero()))
                .collect(Collectors.toList());
    }

    // Version retournant un tableau
    public static Method[] getSortedMethodssArray(Class<?> clazz) {
        List<Method> methodsList = getSortedMethods(clazz);
        return methodsList.toArray(new Method[0]);
    }

    private static String getHTMLTable(Object listObject, Object object) throws Exception {
        Method[] methods = DB.getSortedMethodssArray(object.getClass());
        String headerTable = "";
        for (Method method : methods) {
            ShowTable showTable = method.getAnnotation(ShowTable.class);
            String name = showTable.name();
            headerTable += "<th>" + name + "</th> \n";
        }
        int numberListObject = 0;
        if (listObject == null) {
            numberListObject = 0;
        }

        if (listObject instanceof List<?>) {
            numberListObject = ((List<?>) listObject).size();
        }

        String bodyTable = "";
        if (numberListObject == 0) {
            bodyTable = """
                        <tr>
                            <td colspan="%numberColspan%" class="text-center">Aucun donne</td>
                        </tr>
                    """;
            bodyTable.replace("%numberColspan%", methods.length + "");
        } else {
            List<?> list = (List<?>) listObject;
            StringBuilder rows = new StringBuilder();

            for (Object item : list) {
                StringBuilder row = new StringBuilder("<tr>\n");

                for (Method method : methods) {
                    try {
                        Object value = method.invoke(item);
                        String displayValue = (value != null) ? value.toString() : "";
                        row.append("<td>").append(displayValue).append("</td>\n");
                    } catch (Exception e) {
                        row.append("<td class=\"text-danger\">Erreur</td>\n");
                    }
                }

                row.append("</tr>\n");
                rows.append(row);
            }

            bodyTable = rows.toString();
        }
        String templateTable = """
                <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            %%headerTable%%
                          </tr>
                        </thead>
                        <tbody>
                            %%bodyTable%%
                        </tbody>
                      </table>
                """;

        return templateTable.replace("%%headerTable%%", headerTable).replace("%%bodyTable%%", bodyTable);
    }

}
