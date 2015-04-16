package org.cateproject.domain.auth;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserAccountTest {
	
	private UserAccount userAccount;
	
	private List<UserGroup> groups;
	
	private Set<Permission> permissions;
	
	private PasswordEncoder passwordEncoder;
	
	@Before
	public void setUp() {
		UserGroup userGroup = new UserGroup();
		userGroup.getPermissions().add(Permission.PERMISSION_ADMINISTRATE);
		groups = new ArrayList<UserGroup>();
		groups.add(userGroup);
		permissions = new HashSet<Permission>();
		permissions.add(Permission.PERMISSION_EDIT);
		passwordEncoder = NoOpPasswordEncoder.getInstance();
		
		userAccount = new UserAccount();
		userAccount.setAccountNonExpired(true);
		userAccount.setAccountNonLocked(true);
		userAccount.setCredentialsNonExpired(true);
		userAccount.setEnabled(true);
		userAccount.setEmail("EMAIL");
		userAccount.setFirstName("FIRST_NAME");
		userAccount.setGroups(groups);
		userAccount.setId(1L);
		userAccount.setLastName("LAST_NAME");
		userAccount.setPassword("PASSWORD");
		userAccount.setPasswordEncoder(passwordEncoder);
		userAccount.setPermissions(permissions);
		userAccount.setTenant("TENANT");
		userAccount.setUsername("USERNAME");
		userAccount.setVersion(1);
	}

	@Test
	public void testHashCode() {
		assertEquals("hashCode should be based on the value of userName","USERNAME".hashCode(),userAccount.hashCode());
	}

	@Test
	public void testToString() {
		assertEquals("toString should equal 'UserAccount<1>'", "UserAccount<1>", userAccount.toString());
	}

	@Test
	public void testGetPassword() {
		assertEquals("password should equal 'PASSWORD'", "PASSWORD", userAccount.getPassword());
	}	

	@Test
	public void testGetUsername() {
		assertEquals("username should equal 'USERNAME'", "USERNAME", userAccount.getUsername());
	}

	@Test
	public void testGetPermissions() {
		assertEquals("permissions should equal permissions", permissions, userAccount.getPermissions());
	}

	@Test
	public void testGetGroups() {
		assertEquals("groups should equal groups", groups, userAccount.getGroups());
	}

	@Test
	public void testGetPasswordEncoder() {
		assertEquals("passwordEncoder should equal passwordEncoder", passwordEncoder, userAccount.getPasswordEncoder());
	}

	@Test
	public void testGetEmail() {
		assertEquals("email should equal 'EMAIL'", "EMAIL", userAccount.getEmail());
	}

	@Test
	public void testGetFirstName() {
		assertEquals("firstName should equal 'FIRST_NAME'", "FIRST_NAME", userAccount.getFirstName());
	}

	@Test
	public void testGetLastName() {
		assertEquals("lastName should equal 'LAST_NAME'", "LAST_NAME", userAccount.getLastName());
	}

	@Test
	public void testEncodePassword() {
		userAccount.encodePassword();
		assertEquals("noopPasswordEncoder should not encode the users password","PASSWORD", userAccount.getPassword());
		userAccount.setPasswordEncoder(new BCryptPasswordEncoder());
		userAccount.encodePassword();
		assertNotEquals("bcryptPasswordEncoder should encode the users password","PASSWORD", userAccount.getPassword());
	}

	@Test
	public void testGetAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(Permission.PERMISSION_ADMINISTRATE);
		authorities.add(Permission.PERMISSION_EDIT);
		assertEquals("authorities should equal ('PERMISSION_ADMINISTRATE','PERMISSION_EDIT')", authorities, userAccount.getAuthorities());
	}

	@Test
	public void testIsEnabled() {
		assertTrue("enabled should be true", userAccount.isEnabled());
	}

	@Test
	public void testIsAccountNonExpired() {
		assertTrue("accountNonExpired should be true", userAccount.isEnabled());
	}

	@Test
	public void testIsAccountNonLocked() {
		assertTrue("accountNonLocked should be true", userAccount.isAccountNonLocked());
	}

	@Test
	public void testIsCredentialsNonExpired() {
		assertTrue("credentialsNonExpired should be true", userAccount.isCredentialsNonExpired());
	}

	@Test
	public void testEqualsObject() {
		UserAccount otherUser = new UserAccount();
		otherUser.setUsername("USERNAME");
		assertTrue("equals should return true for the same object", userAccount.equals(userAccount));
		assertFalse("equals should return false a null object", userAccount.equals(null));
		assertFalse("equals should return false a different class of object", userAccount.equals(new Integer(1)));
		assertTrue("equals should return true when the username is the same", userAccount.equals(otherUser));
	}

}
