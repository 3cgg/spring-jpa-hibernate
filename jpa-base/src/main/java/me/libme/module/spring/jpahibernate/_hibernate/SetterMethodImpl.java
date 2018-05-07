package me.libme.module.spring.jpahibernate._hibernate;

import org.hibernate.PropertyAccessException;
import org.hibernate.PropertySetterAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by J on 2017/10/15.
 */
public class SetterMethodImpl {

    private Logger logger= LoggerFactory.getLogger(SetterMethodImpl.class);

    private final Class containerClass;
    private final String propertyName;
    private final Method setterMethod;

    private final boolean isPrimitive;

    public SetterMethodImpl(Class containerClass, String propertyName, Method setterMethod) {
        this.containerClass = containerClass;
        this.propertyName = propertyName;
        this.setterMethod = setterMethod;
        this.isPrimitive = setterMethod.getParameterTypes()[0].isPrimitive();
    }

    public void set(Object target, Object value) {
        try {
            setterMethod.invoke( target, value );
        }
        catch (NullPointerException npe) {
            if ( value == null && isPrimitive ) {
                throw new PropertyAccessException(
                        npe,
                        "Null value was assigned to a property of primitive type",
                        true,
                        containerClass,
                        propertyName
                );
            }
            else {
                throw new PropertyAccessException(
                        npe,
                        "NullPointerException occurred while calling",
                        true,
                        containerClass,
                        propertyName
                );
            }
        }
        catch (InvocationTargetException ite) {
            throw new PropertyAccessException(
                    ite,
                    "Exception occurred inside",
                    true,
                    containerClass,
                    propertyName
            );
        }
        catch (IllegalAccessException iae) {
            throw new PropertyAccessException(
                    iae,
                    "IllegalAccessException occurred while calling",
                    true,
                    containerClass,
                    propertyName
            );
            //cannot occur
        }
        catch (IllegalArgumentException iae) {
            if ( value == null && isPrimitive ) {
                throw new PropertyAccessException(
                        iae,
                        "Null value was assigned to a property of primitive type",
                        true,
                        containerClass,
                        propertyName
                );
            }
            else {
                final Class expectedType = setterMethod.getParameterTypes()[0];
                throw new PropertySetterAccessException(
                        iae,
                        containerClass,
                        propertyName,
                        expectedType,
                        target,
                        value
                );
            }
        }
    }






}
