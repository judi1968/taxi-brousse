package com.project.databases.generalisation;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.project.databases.MyConnection;
import com.project.databases.generalisation.annotation.AttributDb;
import com.project.databases.generalisation.annotation.IdDb;
import com.project.databases.generalisation.annotation.TableDb;
import com.project.databases.generalisation.utils.InfoObject;



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
            }
            if (field.isAnnotationPresent(IdDb.class)) {
                infoObject.nameIdAttributs = (field.getName());
                infoObject.nameIdAttributsInTable = field.getAnnotation(AttributDb.class).name();
                infoObject.objectIdAttribut = field.getType();
                infoObject.valueId = field.get(table);
                System.out.println(field.get(table)+ " yoooooo");
            }
        } 

       

        infoObject.nameAttributs = nameAttributs;
        infoObject.nameAttributsInTable = nameAttributsInTable;
        return infoObject;
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
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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


     public static Object getAllWhere(Object table, String where,Connection connection) throws Exception {
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
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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


    public static Object getAllOrder(Object table, String order ,Connection connection) throws Exception {
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
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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

    public static Object getAllOrderAndLimit(Object table, String order , String limit, Connection connection) throws Exception {
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
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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

    public static Object getAllOrderAndLimit(Object table, String order , int limit, Connection connection) throws Exception {
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
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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
    public static Object getAllOrderAndWhere(Object table,String where , String order ,Connection connection) throws Exception {
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
        String query = "SELECT " + attrs + " FROM "  + nameTable + " WHERE " + where + "  " + " ORDER BY " + order;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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

    public static Object getAllOrderAndLimitAndWhere(Object table,String where, String order , String limit, Connection connection) throws Exception {
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
        String query = "SELECT " + attrs + " FROM "  + nameTable + " WHERE " + where + "  " + " ORDER BY " + order + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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

    public static Object getAllOrderAndLimitAndWhere(Object table,String where, String order , int limit, Connection connection) throws Exception {
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
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " + " ORDER BY " + order + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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


    public static Object getAllLimitAndWhere(Object table,String where, int limit, Connection connection) throws Exception {
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
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " +" LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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

    public static Object getAllLimitAndWhere(Object table,String where, String limit, Connection connection) throws Exception {
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
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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
        
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE "+infoObject.nameIdAttributsInTable+" = " + idValue;
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        
        Class<?> classTable = table.getClass();
        Object result = null;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                result =  constructor.newInstance();
                for (Field field : result.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(result, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(result, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(result, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(result, value);
                            } else if (isComplexObject(field.getType())) { 
                                 if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(result, getById(classField.getConstructor().newInstance(), valeurId, connection));
                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
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
        if (infoObject.valueId == null) {
            System.out.println("null oooh");
        }else{
            System.out.println("tsy null aaah");
        }
		// PreparedStatement statement = null;
		// ResultSet resultset = null;
		// boolean statementOpen = false;
		// boolean resultsetOpen = false;
		// boolean closeable = false;
		// String query = "INSERT INTO note_etudiant (etudiant_fk, matiere_fk, note) VALUES \r\n" + //
		// 		"( ?,  ? , ? )";
		// try {
		// 	if (connection == null) {
		// 		connection = MyConnection.connectDefault();
		// 		connection.setAutoCommit(false);
		// 		closeable = true;
		// 	}

		// 	statement = connection.prepareStatement(query);
		// 	statement.setString(1, etudiant.getId_etudiant());
		// 	statement.setString(2, matiere.getId_matiere());
		// 	statement.setDouble(3, note);
		// 	statementOpen = true;
		// 	statement.executeUpdate();
		// 	statement.close();
		// 	connection.commit();

		// } catch (Exception e) {
		// 	System.out.println(e.getMessage());
		// 	throw e;
		// } finally {
		// 	if (statementOpen) {
		// 		statement.close();
		// 	}
		// 	if (resultsetOpen) {
		// 		resultset.close();
		// 	}
		// 	if (closeable) {
		// 		connection.close();
		// 	}
		// }
    }
    
}
