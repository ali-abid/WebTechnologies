package org.springframework.samples.travel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * A user who can book hotels.
 */
@Entity
@Table(name = "Customer")
public class User implements Serializable {

	private String username;
	private String password;
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Authority> role = new HashSet<Authority>();

	public User() {
	}

	public User(String username, String password, String name) {
		this.username = username;
		this.password = password;
		this.name = name;
	}

	@Id
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User(" + username + ")";
	}
	

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="USER_ROLE")
	public void setRole(Set<Authority> role)
	{
	  this.role = role;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="USER_ROLE")
	public Set<Authority> getRole()
	{
	  return role;
	}

}
