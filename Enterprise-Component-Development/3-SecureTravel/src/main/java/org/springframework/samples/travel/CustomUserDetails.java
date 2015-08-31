package org.springframework.samples.travel;

import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails
{
  private static final long serialVersionUID = 1L;
  private String username;
  private String password;
  private Set<Authority> authorities;

  public CustomUserDetails()
  {
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  @Override
  public String getPassword()
  {
    return password;
  }

  @Override
  public String getUsername()
  {
    return username;
  }

  @Override
  public boolean isAccountNonExpired()
  {
    return true;
  }

  @Override
  public boolean isAccountNonLocked()
  {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @Override
  public boolean isEnabled()
  {
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() 
  {
    String role = "";
    
    for(Authority auth : authorities)
    {
      role = role + auth.getRole() + ", ";
    }
    
    Collection<? extends GrantedAuthority> ga = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    return ga;
  }

  public void setAuthorities(Set<Authority> role)
  {
    this.authorities = role;
  }


}
