package org.cateproject.domain.auth;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserGroupTest {
	
	private UserGroup userGroup;
	
	private Set<Permission> permissions;
	
	private List<UserAccount> members;

	@Before
	public void setUp() throws Exception {
		permissions = new HashSet<Permission>();
		permissions.add(Permission.ROLE_ADMINISTRATE);
		members = new ArrayList<UserAccount>();
		userGroup = new UserGroup();
		userGroup.setName("NAME");
		userGroup.setPermissions(permissions);
		userGroup.setId(1L);
		userGroup.setMembers(members);
		userGroup.setTenant("TENANT");
		userGroup.setVersion(1);
		
	}

	@Test
	public void testToString() {
		assertEquals("toString should equal 'UserGroup<1>'", "UserGroup<1>", userGroup.toString());
	}

	@Test
	public void testGetGrantedAuthorities() {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		grantedAuthorities.add(Permission.ROLE_ADMINISTRATE);
		assertEquals("grantedAuthorities should equal ('ROLE_ADMINISTRATE')", grantedAuthorities, userGroup.getGrantedAuthorities());
	}

	@Test
	public void testGetName() {
		assertEquals("name should equal 'NAME'", "NAME", userGroup.getName());
	}

	@Test
	public void testGetPermissions() {
		assertEquals("permissions should equal permissions", permissions, userGroup.getPermissions());
	}

	@Test
	public void testGetMembers() {
		assertEquals("members should equal members", members, userGroup.getMembers());
	}

}
