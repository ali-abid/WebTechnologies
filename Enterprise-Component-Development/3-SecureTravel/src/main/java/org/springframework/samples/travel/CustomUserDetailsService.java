package org.springframework.samples.travel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomUserDetailsService implements UserDetailsService
{
  private EntityManager em;
  
  @PersistenceContext
  public void setEntityManager(EntityManager em) 
  {
    this.em = em;
  }
  
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    CustomUserDetails cuD = new CustomUserDetails();
    try
    {
     User user =  (User) em.createQuery(
        "from User u where u.username = :username" 
        ).setParameter("username", username).getSingleResult();
     
     cuD.setUsername(user.getUsername());
     cuD.setPassword(user.getPassword());
     cuD.setAuthorities(user.getRole());
    }
    catch (NoResultException e)
    {
      cuD = null;
    }    
    return cuD;
  }
}
