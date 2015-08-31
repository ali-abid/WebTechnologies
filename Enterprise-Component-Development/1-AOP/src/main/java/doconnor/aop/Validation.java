package doconnor.aop;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Aspect
public class Validation
{
  @Autowired
  private List<Validator> validators = new ArrayList<Validator>();
  
  @Pointcut("execution(* doconnor.aop.service.LeagueManager.sign*(..)) ||"
      + "execution(* doconnor.aop.service.LeagueManager.add*(..)) ||"
      + "execution(* doconnor.aop.service.LeagueManager.setup*(..))")
  public void validation(){}
  
  
  @Before("validation()")
  public void startValidation(JoinPoint joinPoint) throws Throwable
  {    
    Validator validator = null;
    // Starting from top level, find out which can be validated first
    for(Object arg : joinPoint.getArgs())
    { 
      // Determine Validator
      validator = determineValidator(arg);
      if(validator != null)
      {
       System.out.println("+--------------------------------------------------------+");
       // Call the appropriate validator
       callValidator(arg, validator);
      }
    }
    System.out.println("<-------------------------------------------------------->");
  }
  
  
  public Validator determineValidator(Object obj)
  {
    // Determines which validator under doconnor.aop.domain.validation can validate the object
    Validator validator = null;
    for(Validator validatorList : validators)
    {
      if(validatorList.supports(obj.getClass()))
      {
        validator = validatorList;
      }
    }  
    return validator;
  }
  
  
  public void callValidator(Object obj, Validator validator) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    Errors errors = new BindException(obj, obj.getClass().getName());
    System.out.println(obj.getClass().getSimpleName() + " Object being validated");
    // Call appropriate Validator in validation domain
    validator.validate(obj, errors);
    if(errors.getFieldError() != null)
    {
     System.out.println("Errors : " + errors.getFieldError());
    }
    else
    {
      System.out.println("Errors : " + errors.getFieldErrorCount());
    }
    System.out.println("+--------------------------------------------------------+");
    
    // Validate properties within the Domain Objects
    callValidatedomobjs(obj);  
  }
  
  public void callValidatedomobjs(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    Object propertyClass = null;
    
    PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(obj.getClass());
    for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
    {
      propertyClass = propertyDescriptor.getReadMethod().invoke(obj);
 
      // Ensure that propertyClass object is not null before proceeding
      if(propertyClass != null)
      {
        // Validates mulitple arguments
        if(propertyDescriptor.getPropertyType().getName().equals("java.util.Set"))
        {
          // Iterates through the list of arguments and validate them individually
         @SuppressWarnings("unchecked")
         Iterator<Object> propIt = ((Set<Object>) propertyClass).iterator();
         while(propIt.hasNext())
         {
          validateProperties(propIt.next());
         }
        }
        else
        {
          // Validates single argument
          validateProperties(propertyClass);
        }
      }
    }
  }
  
  public void validateProperties(Object propertyClass) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    Errors propErrors = new BindException(propertyClass, propertyClass.getClass().getName());
    // Determines if the properties can be validated
    Validator propValidator = determineValidator(propertyClass);
    
    if(propValidator != null)
    {
      // If the properties are not null, validate the properties
      System.out.println("+--------------------------------------------------------+");
      System.out.println("Support property : " + propertyClass.getClass().getSimpleName());
      propValidator.validate(propertyClass, propErrors);
      
       if(propErrors.getFieldErrorCount() == 0 || propErrors.getFieldErrors() == null)
       {
         System.out.println("Errors : " + propErrors.getFieldErrorCount() );
       }
       else
       {
         System.out.println("Errors : " + propErrors.getFieldErrors());
       }
       if(propertyClass.getClass().toString().endsWith(propertyClass.getClass().getSimpleName()))
       {
         // Jumps into another layer for property validation
         callValidatedomobjs(propertyClass);
       }
    }
  }
}
