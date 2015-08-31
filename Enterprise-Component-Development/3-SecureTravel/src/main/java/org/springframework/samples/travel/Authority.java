package org.springframework.samples.travel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name="authority")
public class Authority implements Serializable
{
  @Id
  @GeneratedValue
  private long id;
  private String role_name;
  
  public void setId(long id)
  {
    this.id = id;
  }
  
  public long getId()
  {
    return id;
  }
  
  public void setRole(String role)
  {
    this.role_name = role;
  }
  
  public String getRole()
  {
    return role_name;
  }
  
}
