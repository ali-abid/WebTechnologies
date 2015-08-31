package org.springframework.samples.travel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomAuthenticationProvider implements AuthenticationProvider
{
  private EntityManager em;
  
  @PersistenceContext
  public void setEntityManager(EntityManager em) 
  {
    this.em = em;
  }
  
  @Override
  public boolean supports(Class<? extends Object> authentication)
  {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException
  {
    CustomUserDetailsService cudService = new CustomUserDetailsService();
    cudService.setEntityManager(em);
   
    try
    {
      // Try to load our user from dB
     CustomUserDetails userDetails = (CustomUserDetails) cudService.loadUserByUsername(authentication.getName());

     if(userDetails != null)
     {
       // Check inputted password with our dB details
       if(userDetails.getPassword().equals(authentication.getCredentials()))
       {
         // return Token, but do not pass password back
         return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
       }
       else
       {
         return null;
       }
     }
     else
     {
        return null;
     }
    }
    catch (UsernameNotFoundException e)
    {
      return null;
    }
  }

}
