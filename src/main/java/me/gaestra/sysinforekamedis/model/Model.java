/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import me.gaestra.sysinforekamedis.DatabaseManager;
import me.gaestra.sysinforekamedis.extras.Field;
import me.gaestra.sysinforekamedis.helper.Logger;
import me.gaestra.sysinforekamedis.helper.UtilityMiscHelper;

/**
 *
 * @author Ganesha
 */
public abstract class Model {

    public Integer id;
    public boolean isEdited = false;
    public boolean fromDatabase = false;

    public void save() {
        if (this.fromDatabase)
            this.update();
        else this.store();
    }
    
    public void store() {
        Method[] mt = this.getClass().getDeclaredMethods();
        List<Field> fields = UtilityMiscHelper.getFields(mt, this, false);
        DatabaseManager connector = DatabaseManager.getInstance();

        String sql = "INSERT INTO " + UtilityMiscHelper.tableOf(this) + " (";
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);

            sql += field.getName();

            if ((i + 1) < fields.size()) {
                sql += ", ";
            }
        }

        sql += ") VALUES (";
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);

            sql += ("'" + (field.getType().toLowerCase().contains("boolean") ? ((boolean)field.getValue() ? 1 : 0) : field.getValue())  + "'");

            if ((i + 1) != fields.size()) {
                sql += ", ";
            }
        }

        sql += ")";

        connector.execute(sql);
        System.out.println(sql);
    }

    public void update() {
        Method[] mt = this.getClass().getDeclaredMethods();
        List<Field> fields = UtilityMiscHelper.getFields(mt, this, false);
        DatabaseManager connector = DatabaseManager.getInstance();

        String sql = "UPDATE " + UtilityMiscHelper.tableOf(this) + " SET ";
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            
            sql += field.getName() + " = '" + field.getValue() + "'";

            if ((i + 1) < fields.size()) {
                sql += ", ";
            }
        }

        if (id == null) {
            Logger.error("id cannot be null");
            return;
        }

        sql += " WHERE id = " + id;

        connector.execute(sql);
        System.out.println(sql);
    }

    public void delete() {
        String sql = "DELETE FROM " + UtilityMiscHelper.tableOf(this);
        DatabaseManager connector = DatabaseManager.getInstance();

        if (id == null) {
            Logger.log(Logger.Level.Error, "id cannot be null");
            return;
        }

        sql += " WHERE id = " + id;

        connector.execute(sql);
        System.out.println(sql);
    }

    public static void create(Model model) {
        model.store();
    }

    public static void update(Model model) {
        model.update();
    }
    
    public static <M extends Model> M first(String column, String property, Supplier<M> constructor) {
        return first(column + " = '" + property + "'", constructor);
    }
    
    public static <M extends Model> M first(String whereClause, Supplier<M> constructor) {
        List<M> list = where(whereClause, constructor);
        return (!list.isEmpty() ? list.get(0) : constructor.get());
    }
    
    public static <M extends Model> List<M> where(String column, String property, Supplier<M> constructor) {
        return where(column + " = '" + property + "'", constructor);
    }
    
    public static <M extends Model> List<M> where(String whereClause, Supplier<M> constructor) {
        M instance = constructor.get();
        List<M> models = new ArrayList<>();
        Method[] methods = instance.getClass().getDeclaredMethods();
        List<Method> setters = new ArrayList<>();
        List<Field> fields = UtilityMiscHelper.getFields(methods, instance, true);
        DatabaseManager connector = DatabaseManager.getInstance();

        for (Method m : methods) {
            if (m.getName().contains("set")) {
                setters.add(m);
            }
        }

        String sql = "SELECT * FROM " + UtilityMiscHelper.tableOf(instance) + (!whereClause.isEmpty() ? " WHERE " + whereClause : "");
        try {
            ResultSet rs = connector.executeQuery(sql);

            while (rs.next()) {
                M model = constructor.get();
                model.setId(rs.getInt("id"));

                for (int i = 0; i < setters.size(); i++) {
                    Method tempMethod = setters.get(i);
                    for (int j = 0; j < fields.size(); j++) {
                        Field tempField = fields.get(j);
                        if (tempMethod.getName().toLowerCase().contains(tempField.getName())) {
                            tempMethod.invoke(model, connector.getResult(rs, tempField.getType(), tempField.getName()));
                        }
                    }
                }
                
                model.fromDatabase = true;
                models.add(model);
            }
        } catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Logger.error("Failed to execute query: " + e.getMessage());
        }
        System.out.println(sql);

        return models;
    }
    
    public static <M extends Model> int count(Supplier<M> constructor) {
        M instance = constructor.get();
        String sql = "SELECT COUNT(id) as count FROM " + UtilityMiscHelper.tableOf(instance);
        DatabaseManager connector = DatabaseManager.getInstance();
        
        try {
            ResultSet rs = connector.executeQuery(sql);

            while (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            Logger.error("Failed execute query: " + e.getMessage());
        }
        System.out.println(sql);
        
        return 0;
    }
    
    public static <M extends Model> int latestId(Supplier<M> constructor) {
        M instance = constructor.get();
        String sql = "SELECT max(id) as latest FROM " + UtilityMiscHelper.tableOf(instance);
        DatabaseManager connector = DatabaseManager.getInstance();
        
        try {
            ResultSet rs = connector.executeQuery(sql);

            while (rs.next()) {
                return rs.getInt("latest");
            }
        } catch (SQLException e) {
            Logger.error("Failed execute query: " + e.getMessage());
        }
        System.out.println(sql);
        
        return 0;
    }

    public static <M extends Model> M find(int id, Supplier<M> constructor) {
        M instance = constructor.get();
        Method[] methods = instance.getClass().getDeclaredMethods();
        List<Method> setters = new ArrayList<>();
        List<Field> fields = UtilityMiscHelper.getFields(methods, instance, true);
        DatabaseManager connector = DatabaseManager.getInstance();

        for (Method m : methods) {
            if (m.getName().contains("set")) {
                setters.add(m);
            }
        }

        String sql = "SELECT * FROM " + UtilityMiscHelper.tableOf(instance) + " WHERE id = " + id;

        try {
            ResultSet rs = connector.executeQuery(sql);

            while (rs.next()) {
                instance.setId(id);
                for (int i = 0; i < setters.size(); i++) {
                    Method tempMethod = setters.get(i);
                    for (int j = 0; j < fields.size(); j++) {
                        Field tempField = fields.get(j);
                        if (tempMethod.getName().toLowerCase().contains(tempField.getName())) {
                            tempMethod.invoke(instance, connector.getResult(rs, tempField.getType(), tempField.getName()));
                        }
                    }
                }
                instance.fromDatabase = true;
            }

        } catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Logger.error("Failed execute query: " + e.getMessage());
        }
        System.out.println(sql);

        return instance;
    }

    public static <M extends Model> List<M> all(Supplier<M> constructor) {
        M instance = constructor.get();
        List<M> models = new ArrayList<>();
        Method[] methods = instance.getClass().getDeclaredMethods();
        List<Method> setters = new ArrayList<>();
        List<Field> fields = UtilityMiscHelper.getFields(methods, instance, true);
        DatabaseManager connector = DatabaseManager.getInstance();

        for (Method m : methods) {
            if (m.getName().contains("set")) {
                setters.add(m);
            }
        }

        String sql = "SELECT * FROM " + UtilityMiscHelper.tableOf(instance);
        try {
            ResultSet rs = connector.executeQuery(sql);

            while (rs.next()) {
                M model = constructor.get();
                model.setId(rs.getInt("id"));

                for (int i = 0; i < setters.size(); i++) {
                    Method tempMethod = setters.get(i);
                    for (int j = 0; j < fields.size(); j++) {
                        Field tempField = fields.get(j);
                        if (tempMethod.getName().toLowerCase().contains(tempField.getName())) {
                            tempMethod.invoke(model, connector.getResult(rs, tempField.getType(), tempField.getName()));
                        }
                    }
                }
                
                model.fromDatabase = true;
                models.add(model);
            }
        } catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Logger.error("Failed to execute query: " + e.getMessage());
        }
        System.out.println(sql);

        return models;
    }
    
    public boolean exist() {
        return id != null && id != 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    // Comparator Hook
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Model) {
            return Objects.equals(((Model)obj).id, this.id); 
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
